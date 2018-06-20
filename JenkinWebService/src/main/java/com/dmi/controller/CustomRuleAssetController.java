package com.dmi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.analytics.SparkJobInvoker;
import com.dmi.constant.Constants;
import com.dmi.dao.model.CustomRuleAsset;
import com.dmi.dao.model.MessageModel;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.CustomRuleAssetProcessor;
import com.dmi.security.JWTHelper;
import com.dmi.utils.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/customRuleAsset")
@Api(tags = "Custom Rule Asset Controller")
public class CustomRuleAssetController {
	

	private static final Logger LOG = Logger.getLogger(CustomRuleAssetController.class);
	
	@Value("${CUSTOM_JARS_LOCATION}")
	private String jarFileLocation;
	
	@Autowired
	CustomRuleAssetProcessor customRuleAssetProcessor;
	
	@Autowired
	public SparkJobInvoker sparkMaster;
	
	@ApiOperation(value = "Upload custom asset", notes="API to upload custom asset")
	@RequestMapping(value = "/uploadCustomAsset", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> customAssetUpload(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@RequestParam(value = "customModel", required = true) MultipartFile customModel,
			@RequestParam(value = "customModelName", required = true) String customModelName,
			@RequestParam(value = "jarName", required = true) String jarName,
			@RequestParam(value = "jarExecutionPath", required = true) String jarExecutionPath,
			@RequestParam(value = "jar", required = true) MultipartFile jar,
			@RequestParam(value = "deviceTypeId", required = true) Long deviceTypeId) 
	{

		
		MessageModel customMsgModel = new MessageModel();
		String directory = "";
		String jarNameMod = deviceTypeId.toString() + '_' + jarName.replace(".jar", "") + "_" + System.currentTimeMillis() + ".jar";

		try {

			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
			
			// Process message model
			if (!customModel.isEmpty()) {
				byte[] bytes;

				bytes = customModel.getBytes();
				String tmpfilename = "customModelTmp_" + System.currentTimeMillis();

				OutputStream out;
				out = new FileOutputStream(new File(tmpfilename));
				out.write(bytes);
				out.close();

				String str = FileUtils.readFileToString(new File(tmpfilename));
				new File(tmpfilename).delete();
				
				//check if the Json is valid json
				boolean firstStringValid = JSONUtils.isJSONValid(str);
				if (firstStringValid) {
					customMsgModel = new MessageModel();

					customMsgModel.setMessageFormat(str);
					customMsgModel.setModelName(customModelName);

				} else
					throw new Exception("Unparseable JSON in file.");
			} else
				throw new Exception("File is empty!");

			System.out.println("Model saved.");

			// save jar file
			if (!jar.isEmpty()) {
				byte[] bytes;

				bytes = jar.getBytes();
				String filename = jarNameMod;

				OutputStream out;
				out = new FileOutputStream(new File(filename));
				out.write(bytes);
				out.close();

				sparkMaster.saveFileInSparkMaster(directory, filename);

			} else
				throw new Exception("File is empty!");

			System.out.println("Jar saved.");

			// save entry

			CustomRuleAsset cra = new CustomRuleAsset();
			cra.setJarExecutionPath(jarExecutionPath);
			cra.setJarFilePath(jarFileLocation + "/" + jarNameMod);
			cra.setMessageModelFormat(customMsgModel.getMessageFormat());
			cra.setMessageModelName(customMsgModel.getModelName());

			customRuleAssetProcessor.createCustomRuleAsset(cra, deviceTypeId, oemId);

			System.out.println("DB entry created.");

			JSONObject res = new JSONObject().put("success", true).put("msg", "Operation created successfully!");
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());

		} catch (ProcessorException ex)
		{
			LOG.error(ex);
			
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		} catch (Exception e) {
			JSONObject res = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}

	}
	
	@ApiOperation(value = "Get custom rule filters", notes="API to get custom rule filter")
	@RequestMapping(value = "/getCustomRuleFilters", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getCustomRuleFilters(
			@RequestParam(value = "deviceTypeId", required = true) long deviceTypeId,
			@RequestParam(value = "messageModelName", required = true) String customMessageModelName) {

		CustomRuleAsset cRA = null;

		try {
			cRA = customRuleAssetProcessor.getCRAByDeviceTypeAndModelName(deviceTypeId, customMessageModelName);

			JsonParser parser = new JsonParser();
			JsonElement jsonElem = parser.parse(cRA.getMessageModelFormat());
			JsonObject jObject = jsonElem.getAsJsonObject();

			JSONArray jsonFilterArray = new JSONArray();
			Set<Entry<String, JsonElement>> entrySet = jObject.entrySet();

			for (Map.Entry<String, JsonElement> entry : entrySet)
			{
				JsonElement jsonValue = entry.getValue();
				if (jsonValue.isJsonArray())
				{
					JsonArray jArray = jsonValue.getAsJsonArray();
					System.out.println("groupField :" + entry.getKey());
					int arrayLength = jArray.size();
					for (int i = 0; i < arrayLength; i++)
					{
						JsonElement jelem = jArray.get(i);
						JsonObject jobject = jelem.getAsJsonObject();
						JSONObject jsonFilterObject = new JSONObject();

						if (jobject.get("key") != null)
						{

							String id = jobject.get("key").getAsString();
							jsonFilterObject.put("id", id);
							jsonFilterObject.put("parentKey", entry.getKey());
							if (entry.getKey()
									.equalsIgnoreCase("assetDataList"))
							{
								jsonFilterObject.put("type", "integer");
							}
							else
							{
								jsonFilterObject.put("type", "string");
							}
							jsonFilterArray.put(jsonFilterObject);
						}
					}
				}
			}
			System.out.println("************ final json array "
					+ jsonFilterArray);
			
			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "Request processed successfully.")
					.put("customRuleFilters", jsonFilterArray);
			return ResponseEntity.ok().body(response.toString());
			
		} catch (ProcessorException ex)
		{
			LOG.error(ex);
			
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		} catch (Exception ex) {
			LOG.error(ex);
			JSONObject response = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}

	}
	
	@ApiOperation(value = "Get custom rule asset by device type", notes="API to get custom rule asset by device type")
	@RequestMapping(value = "/getCRAByDeviceType", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getCustomRuleAssetsByDeviceType(
			@RequestParam(value = "deviceTypeId", required = true) long deviceTypeId) {

		JSONArray resultList = new JSONArray();
		List<CustomRuleAsset> cRAs = new LinkedList<CustomRuleAsset>();

		try {
			cRAs = customRuleAssetProcessor.getCustomRuleAssetsByDeviceTypeId(deviceTypeId);

			for (CustomRuleAsset cra : cRAs) {

				resultList.put(new JSONObject().put("jarExecutionPath", cra.getJarExecutionPath())
						.put("messageModelName", cra.getMessageModelName()));
			}

			JSONObject response = new JSONObject().put("success", true).put("msg", "Request processed successfully.")
					.put("customRuleAssets", resultList);
			return ResponseEntity.ok().body(response.toString());

		} 
		catch (ProcessorException ex){
			LOG.error(ex);
			
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			JSONObject response = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}

	}
}
