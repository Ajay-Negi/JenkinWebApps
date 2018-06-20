package com.dmi.processor;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import com.dmi.constant.Constants;
import com.dmi.dao.IMLModelsAndExpDAO;
import com.dmi.dao.IMLModelsTypesDAO;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.MLModelsAndExp;
import com.dmi.dao.model.MLModelsTypes;
import com.dmi.dao.model.Oem;
import com.dmi.dto.oemmodel.rawmessage.ArithExpDTO;
import com.dmi.dto.oemmodel.rawmessage.PmmlDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.utils.DateTimeUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;

import redis.clients.jedis.Jedis;

@Service
public class PluggableModelProcessor
{
	
	private static final Logger LOG = Logger.getLogger(PluggableModelProcessor.class);

	@Autowired
	private IMLModelsAndExpDAO imlModelAndExpDAO;

	@Autowired
	private IMLModelsTypesDAO imlModelsTypesDAO;

	public JSONArray getModelsByOemAndDeviceType(long oemId,
			long deviceTypeId) throws ProcessorException
	{
		JSONArray pluggableRuleElementList = new JSONArray();
		try
		{

			List<MLModelsAndExp> mlModelsAndExpList = imlModelAndExpDAO
					.fetchByOemAndDeviceType(oemId, deviceTypeId);

			if (mlModelsAndExpList == null)
				return new JSONArray();

			for (MLModelsAndExp mlModelsAndExp : mlModelsAndExpList)
			{
				JSONObject jsonObject = new JSONObject();

				if (mlModelsAndExp.getMlModelsTypes().getModelType()
						.equalsIgnoreCase("Arithmetic"))
					jsonObject.put("parentKey", "arithmetic");
				else
					jsonObject.put("parentKey", "machineLearning");
				jsonObject.put("id", mlModelsAndExp.getModelName());
				jsonObject.put("type", "integer");

				pluggableRuleElementList.put(jsonObject);
			}
			return pluggableRuleElementList;
		}
		catch (DataAccessException dae)
		{
			LOG.error(dae.getMessage(), dae);
			throw new ProcessorException(dae.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveModel(Long oemId, String userName, long deviceTypeId, String modelName,
			byte[] modelText, String modelTypeName)
			throws ProcessorException, SAXException, JAXBException
	{
		String modelNameFinal = modelName.replaceAll("\\s", "");
		MLModelsAndExp mlModelsAndExp = new MLModelsAndExp();
		try
		{
			if (imlModelAndExpDAO.checkForExistingModelNameForDeviceType(oemId, deviceTypeId, modelNameFinal))
				throw new ProcessorException(
						"Model with specified name already exists!");

			Oem oem = new Oem();
			oem.setId(oemId);
			mlModelsAndExp.setOem(oem);
			
			DeviceType deviceType = new DeviceType();
			deviceType.setId(deviceTypeId);
			mlModelsAndExp.setDeviceType(deviceType);

			MLModelsTypes mlModelsTypes = new MLModelsTypes();
			Long modelTypeId = imlModelsTypesDAO
					.getModelTypeIdFromName(modelTypeName);
			mlModelsTypes.setModelTypeId(modelTypeId);
			mlModelsAndExp.setMlModelsTypes(mlModelsTypes);
			mlModelsAndExp.setCreatedTimeStamp(new Date());
			mlModelsAndExp.setUpdatedTimeStamp(new Date());
			mlModelsAndExp.setCreatedById(userName);

			mlModelsAndExp.setModelEquation(modelText);

			mlModelsAndExp.setModelName(modelNameFinal);

			imlModelAndExpDAO.saveModel(mlModelsAndExp);

			// SAVE MODEL TO REDIS
			String pmmlModelString = new String(modelText);
			if (!modelTypeName.equalsIgnoreCase("arithmetic"))
				saveMLModelToRedis(oemId, deviceTypeId, pmmlModelString, modelName);
			else
				saveArithModelToRedis(oemId, deviceTypeId, pmmlModelString, modelName);
		}
		catch (DataAccessException dae)
		{
			Log.error(dae.getMessage(), dae);
			throw new ProcessorException(dae);
		}
		catch (Exception ex)
		{
			Log.error(ex.getMessage(), ex);
			throw new ProcessorException(ex);
		}
	}

	public JSONArray getModelsByOemId(Long oemId)
			throws ProcessorException
	{
		JSONArray pluggableRuleElementList = new JSONArray();
		try
		{

			List<MLModelsAndExp> mlModelsAndExpList = imlModelAndExpDAO.fetchByOem(oemId);

			if (mlModelsAndExpList == null)
				return new JSONArray();

			for (MLModelsAndExp mlModelsAndExp : mlModelsAndExpList)
			{
				JSONObject jsonObject = new JSONObject();

				jsonObject.put("modelId", mlModelsAndExp.getId());
				jsonObject.put("modelName", mlModelsAndExp.getModelName());

				jsonObject.put("modelType", mlModelsAndExp.getMlModelsTypes().getModelType());
				jsonObject.put("modelContent", Bytes.toString(mlModelsAndExp.getModelEquation()));
				jsonObject.put("deviceTypeId", mlModelsAndExp.getDeviceType().getId());
				jsonObject.put("deviceTypeName", mlModelsAndExp.getDeviceType().getAlias());
				jsonObject.put("lastUpdated", DateTimeUtils
						.convertToCommonTimeFormat(mlModelsAndExp.getUpdatedTimeStamp()));

				pluggableRuleElementList.put(jsonObject);
			}
			return pluggableRuleElementList;
		}
		catch (DataAccessException dae)
		{
			dae.printStackTrace();
			throw new ProcessorException(dae);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteModel(Long oemId, Long deviceTypeId, String modelName,
			Long modelId, String modelTypeName) throws ProcessorException
	{
		try
		{
			modelName = modelName.replaceAll("\\s", "");
			imlModelAndExpDAO.deleteModelById(modelId);

			// DELETE MODEL FROM REDIS
			deleteModelFromRedis(oemId, deviceTypeId, modelName);
		}
		catch (DataAccessException dae)
		{
			dae.printStackTrace();
			throw new ProcessorException(dae);
		}
	}

	private void deleteModelFromRedis(Long oemId, Long deviceTypeId,
			String modelName)
	{
		// JEDIS AUTH
		Jedis jedis = new Jedis(Constants.REDIS_IP);
		jedis.auth(Constants.REDIS_AUTH);

		String mlModelKey = oemId + "," + deviceTypeId + "," + modelName;

		jedis.del(mlModelKey);

		// closing JEDIS client
		jedis.close();
	}

	/**
	 * @author HMathur
	 * @throws SAXException
	 * @throws JAXBException
	 */
	private void saveMLModelToRedis(Long oemId, Long deviceTypeId,
			String pmmlModelString, String modelName)
			throws SAXException, JAXBException
	{

		// JEDIS AUTH
		Jedis jedis = new Jedis(Constants.REDIS_IP);
		jedis.auth(Constants.REDIS_AUTH);

		String mlModelKey = oemId + "_" + deviceTypeId + "_" + modelName;

		/*String modelEquation = Bytes
				.toString(mlModelsAndExp.getModelEquation());
		InputSource source = new InputSource(new StringReader(modelEquation));
		SAXSource transformedSource = ImportFilter.apply(source);
		PMML ret = JAXBUtil.unmarshalPMML(transformedSource);

		// Creating PmmlDTO
		PmmlDTO pmmlDTO = new PmmlDTO();
		pmmlDTO.setMlType(mlModelType);
		pmmlDTO.setMlName(mlModelsAndExp.getModelName());
		pmmlDTO.setModel(ret);
		

		// Serializing DTO for transmission using Kyro
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		kryo.register(PmmlDTO.class);

		// Converting key-value pair to bytes and transmitting
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Output output = new Output(stream);
		kryo.writeObject(output, pmmlDTO);
		output.close();
		byte[] buffer = stream.toByteArray();*/
		
		Map<String, String> hmap = new HashMap<String, String>();
		
		hmap.put(mlModelKey, pmmlModelString);
		jedis.hmset("mlmodels", hmap);

		// closing JEDIS client
		jedis.close();

	}

	/**
	 * @author HMathur
	 * @throws SAXException
	 * @throws JAXBException
	 */
	private void saveArithModelToRedis(Long oemId, Long deviceTypeId,
			String pmmlModelString, String modelName)
			throws SAXException, JAXBException
	{

		// JEDIS AUTH
		Jedis jedis = new Jedis(Constants.REDIS_IP);
		jedis.auth(Constants.REDIS_AUTH);

		String mlModelKey = oemId + "_" + deviceTypeId + "_" + modelName;

		/*String modelEquation = Bytes
				.toString(mlModelsAndExp.getModelEquation());

		// Creating ArithExpDTO
		ArithExpDTO arithExpDTO = new ArithExpDTO();
		arithExpDTO.setExpName(mlModelsAndExp.getModelName());
		arithExpDTO.setExpType(mlModelType);
		arithExpDTO.setExpression(modelEquation);

		// Serializing DTO for transmission using Kyro
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		kryo.register(PmmlDTO.class);

		// Converting key-value pair to bytes and transmitting
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Output output = new Output(stream);
		kryo.writeObject(output, arithExpDTO);
		output.close();
		byte[] buffer = stream.toByteArray();
		jedis.set(mlModelKey.getBytes(), buffer);*/

		Map<String, String> hmap = new HashMap<String, String>();
		
		hmap.put(mlModelKey, pmmlModelString);
		jedis.hmset("mlmodels", hmap);

		
		// closing JEDIS client
		jedis.close();

	}
}
