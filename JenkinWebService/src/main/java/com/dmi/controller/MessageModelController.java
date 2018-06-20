package com.dmi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.constant.Constants;
import com.dmi.dao.model.MessageModel;
import com.dmi.dto.KeyValuePairDTO;
import com.dmi.dto.MessageModelDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.MessageModelProcessor;
import com.dmi.processor.RuleProcessor;
import com.dmi.security.JWTHelper;
import com.dmi.utils.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



@RestController
@Api(tags = "Message Model")
public class MessageModelController {

private static final Logger LOG = Logger.getLogger(RuleProcessor.class);
	
	@Autowired
	private MessageModelProcessor messageModelProcessor;

	@Autowired
	private RuleProcessor ruleProcessor;

	@RequestMapping(value = "/messageModel/ruleFilters", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
	@ApiOperation(value = "Get rule parameters as per message model", notes = "API to fetch rule parameter as per message model")
	public ResponseEntity<String> getRuleFilters(
			@ApiParam(value = "Message Model Id for which needs to fetch rule parameters ") @RequestParam(required = true, value = "messageModelId") long messageModelId) {

		try {

			JsonParser parser = null;
			KeyValuePairDTO keyValuePairVO = ruleProcessor.getMessageDataElement(messageModelId);

			parser = new JsonParser();
			JsonElement jsonElement = parser.parse(keyValuePairVO.getValue());
			JsonObject jObject = jsonElement.getAsJsonObject();

			JSONArray jsonFilterArray = new JSONArray();
			Set<Entry<String, JsonElement>> entrySet = jObject.entrySet();

			for (Map.Entry<String, JsonElement> entry : entrySet) {
				JsonElement jsonValue = entry.getValue();
				if (jsonValue.isJsonArray()) {
					JsonArray jArray = jsonValue.getAsJsonArray();
					System.out.println("groupField :" + entry.getKey());
					int arrayLength = jArray.size();
					for (int i = 0; i < arrayLength; i++) {
						JsonElement jelem = jArray.get(i);
						JsonObject jobject = jelem.getAsJsonObject();
						JSONObject jsonFilterObject = new JSONObject();

						if (jobject.get("key") != null ) {

							String id = jobject.get("key").getAsString();
							jsonFilterObject.put("id", id);
							jsonFilterObject.put("parentKey", entry.getKey());
							if (entry.getKey().equalsIgnoreCase("assetDataList")
									|| entry.getKey().equalsIgnoreCase("imageDataList")) {
								jsonFilterObject.put("type", "integer");
							} else {
								jsonFilterObject.put("type", "string");
							}
							jsonFilterArray.put(jsonFilterObject);
						}
						else if (jobject.get("codes") != null ) {

							jsonFilterObject.put("id","codes");
							jsonFilterObject.put("parentKey", entry.getKey());
							if (entry.getKey().equalsIgnoreCase("assetDataList")
									|| entry.getKey().equalsIgnoreCase("imageDataList")) {
								jsonFilterObject.put("type", "integer");
							} else {
								jsonFilterObject.put("type", "string");
							}
							jsonFilterArray.put(jsonFilterObject);
						}
					}
				}
			}
			System.out.println("************ final json array " + jsonFilterArray);
			JSONObject response = new JSONObject().put("success", true).put("msg", "Request processed successfully.")
					.put("ruleFilters", jsonFilterArray);
			return ResponseEntity.ok().body(response.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			JSONObject response = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}
	}

	//NOT NEEDED
	/*@RequestMapping(value = "/subDomainInfo/{domainName}", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public ResponseEntity<String> handleSubDomainInfo(@PathVariable Long domainName) {
		Map<Long, String> data;
		try {
			data = messageModelProcessor.findSubDomainName(domainName);
			JSONObject res = new JSONObject().put("success", true).put("msg", "Sub-Domain Name retrieved.")
					.put("response", data);
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		} catch (ProcessorException e) {
			JSONObject res = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
	}*/

	@RequestMapping(value = "/messageModel/upload", method = RequestMethod.POST, produces = "application/json")
	@ApiOperation(value = "Save update message model", notes = "Provide message model details to save/update ")
	public ResponseEntity<String> saveUpdateMessageModel(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "Message Model Name ") @RequestParam(value = "fileName") String fileName,
			@ApiParam(value = "Message model file which needs to be uploaded") @RequestParam(value = "file") MultipartFile file,
			@ApiParam(value = "Device type id for which message model is uploaded") @RequestParam(value = "deviceTypeId") Long deviceTypeId) {

		
		if (!file.isEmpty()) {
			try {
				String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
				Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken, "oemId"));
				
				byte[] bytes;
				bytes = file.getBytes();
				String tmpfilename = "autojson" + System.currentTimeMillis();
				OutputStream out;
				out = new FileOutputStream(new File(tmpfilename));
				out.write(bytes);
				out.close();
				String messageJSON = FileUtils.readFileToString(new File(tmpfilename));

				new File(tmpfilename).delete();
				boolean isJSONValid = JSONUtils.isJSONValid(messageJSON);
				String message = "Message model saved/updated.";
				
				if (isJSONValid) {
					MessageModel messageModel = messageModelProcessor.getByDeviceType(deviceTypeId);
					
					if (messageModel == null) {
						MessageModelDTO mmDTO = new MessageModelDTO();

						mmDTO.setModelName(fileName);
						mmDTO.setMessageFormat(messageJSON);
						mmDTO.setCreatedByUserId(userName);
						mmDTO.setDeviceTypeId(deviceTypeId);

						messageModelProcessor.saveMessageModel(mmDTO,userName, oemId );
						message = "Message model created.";
					} else {

						messageModel.setUpdatedById(userName);
						messageModel.setMessageFormat(messageJSON);
						messageModel.setModelName(fileName);
						messageModel.setUpdatedTimestamp(new Date());

						messageModelProcessor.updateMessageModel(messageModel);
						message = "Message model updated.";
					}
					
					JSONObject res = new JSONObject().put("success", true).put("msg", message);
					return ResponseEntity.status(HttpStatus.OK).body(res.toString());
				} else {
					JSONObject res = new JSONObject().put("success", false).put("msg", "Unparseable JSON in file.");
					return ResponseEntity.status(HttpStatus.OK).body(res.toString());
				}
			} catch (IOException ex) {
				LOG.error(ex.getMessage(), ex);
				JSONObject response = new JSONObject().put("success", false).put("msg", ex.getMessage());
				return ResponseEntity.status(HttpStatus.OK).body(response.toString());
			} catch (ProcessorException e) {
				LOG.error(e.getMessage(), e);
				JSONObject response = new JSONObject().put("success", false).put("msg", e.getMessage());
				return ResponseEntity.status(HttpStatus.OK).body(response.toString());
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				JSONObject response = new JSONObject().put("success", false).put("msg", e.getMessage());
				return ResponseEntity.status(HttpStatus.OK).body(response.toString());
			}
		} else {
			JSONObject response = new JSONObject().put("success", false).put("msg", "File is empty.");
			return ResponseEntity.status(HttpStatus.OK).body(response.toString());
		}
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	/*@RequestMapping(value = "/messageModel/getByOem", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getMessageModelByOem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

		HttpStatus returnCode = HttpStatus.OK;
		JSONObject result = new JSONObject();

		try {

			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(token, "oemId"));

			List<DeviceTypeDTO> subdomainDTOList = ruleProcessor.getAllSubDomainList(domainId);
			List<SubDomain> subdomainList = new LinkedList<SubDomain>();

			for (DeviceTypeDTO dto : subdomainDTOList) {
				SubDomain model = new SubDomain();
				model.setSubDomainId((long) dto.getSubDomainId());
				subdomainList.add(model);
			}

			List<MessageModelDTO> mmVO = messageModelProcessor.get.getMessageModelsForSubDomainList(subdomainList);

			Gson gson = new Gson();
			String data = gson.toJson(mmVO);

			result.put("success", true);
			result.put("data", data);

		} catch (Exception ex) {
			ex.printStackTrace();
			returnCode = HttpStatus.OK;
			result.put("success", false);
		}
		return ResponseEntity.status(returnCode).body(result.toString());

	}*/

	/**
	 * @author AChowdhury
	 * @return Delete message model for message model id
	 */
	@RequestMapping(value = "/messageModel/delete/{messageModelId}", produces = "application/json", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete message model", notes = "API to provide message model Id to delete it")
	public ResponseEntity<String> deleteMessageModelById(
			@ApiParam(value = "Message model id which needs to be deleted") @PathVariable("messageModelId") Long messageModelId, 
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

		
		
		JSONObject result = new JSONObject();
		try {
			String roleCode = JWTHelper.parseJWTClaim(token, "roleCode");
			if(!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new ProcessorException("Only admin can delete the message model");
			
			messageModelProcessor.deleteMessageModelById(messageModelId);
			result.put("success", true).put("msg", "Deleted successfully!");

		} catch (ProcessorException e) {
			LOG.error(e.getMessage(), e);
			result.put("success", false).put("msg", e.getMessage());
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			result.put("success", false).put("msg", ex.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(result.toString());

	}
	
	@RequestMapping(value = "/messageModel/ByDeviceType", produces = "application/json", method = RequestMethod.GET)
	@ApiOperation(value = "Get message model by device type", notes = "API to fetch message model for a device type")
	public ResponseEntity<String> getByDeviceTypeId(
			@ApiParam(value = "Device type id for which message model is required ") @RequestParam Long deviceTypeId) {

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			MessageModel messageModel = messageModelProcessor.getByDeviceType(deviceTypeId);
			
			if(messageModel != null)
				jsonString = objectMapper.writeValueAsString(messageModel);
			else
				throw new ProcessorException("No messageModel exist for this device type");
			
			
			JSONObject result = new JSONObject(jsonString);
			
			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "MessageModel retrieved.")
					.put("messageModel", result);
			
			return ResponseEntity.ok().body(response.toString());
			
		}catch (ProcessorException ex)
		{
			LOG.error(ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		} 
		catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", ex.getMessage());
			
			return ResponseEntity.ok().body(response.toString());
		}
		

	}
	
	@RequestMapping(value = "/messageModel/getById", produces = "application/json", method = RequestMethod.GET)
	@ApiOperation(value = "Get message model by Id", notes = "API to fetch message model by its Id")
	public ResponseEntity<String> getById(
			@ApiParam(value = "Message model Id") @RequestParam Long id) {

		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			String jsonString = objectMapper.writeValueAsString(messageModelProcessor.getById(id));
			
			JSONObject result = new JSONObject(jsonString);
			
			JSONObject response = new JSONObject().put("success", true)
					.put("msg", "MessageModel retrieved.")
					.put("messageModel", result);
			
			return ResponseEntity.ok().body(response.toString());
			
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			JSONObject response = new JSONObject().put("success", false)
					.put("msg", ex.getMessage());
			
			return ResponseEntity.ok().body(response.toString());
		}
		

	}
	
	@RequestMapping(value = "/messageModel/getAll", produces = "application/json", method = RequestMethod.GET)
	@ApiOperation(value = "Get all message model for an oem", notes = "API to fetch all message model in an OEM")
	public ResponseEntity<String> getAll(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {

		JSONArray messageModelList = null;
		
		try {
			
			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(jwtToken,"oemId"));
			
			messageModelList = messageModelProcessor.getAll(oemId);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, true)
					.put(Constants.RESPONSE, messageModelList);
			
			return ResponseEntity.ok().body(result.toString());
			
		} 
		catch (ProcessorException ex)
		{
			LOG.error(ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(),ex);
			JSONObject result = new JSONObject().put(Constants.SUCCESS, false).put(
					"msg", ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(
					result.toString());
		}
		

	}

}