package com.dmi.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dmi.dao.model.MessageModel;
import com.dmi.dto.DeviceTypeDTO;
import com.dmi.dto.MessageRuleDTO;
import com.dmi.dto.NotificationTemplateDTO;
import com.dmi.dto.RuleBuilderDTO;
import com.dmi.exception.ProcessorException;
import com.dmi.processor.MessageModelProcessor;
import com.dmi.processor.RuleProcessor;
import com.dmi.security.JWTHelper;
import com.dmi.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "RuleBuilder")
@RestController
@RequestMapping(value="/ruleBuilder")
public class RuleBuilderController {

	private static final Logger LOG = Logger.getLogger(RuleBuilderController.class);
	
	@Value("${JSON_RULE_LOCATION}")
	private String jarFileRoot;

	@Autowired
	public RuleProcessor ruleProcessor;
	@Autowired
	public MessageModelProcessor messageModelProcessor;
	

	/**
	 * 
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "Get initial data for Rule Creation", 
			notes = "API to fetch initial data which include message models and device types which will be user in rule creation form")
	@RequestMapping(value = "/getInitialData", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getMessageTemplateList(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

		 JSONObject response = new JSONObject();

		try {
			MessageRuleDTO messageRuleDTO = new MessageRuleDTO();

			Long oemId = Long.parseLong(JWTHelper.parseJWTClaim(token, "oemId"));
			String userName = JWTHelper.parseJWTClaim(token, "userName");

			List<JSONObject> deviceTypeList = ruleProcessor.getDeviceTypeAndMessageModelList(oemId);
			List<NotificationTemplateDTO> templateList = ruleProcessor.getAllTemplates(oemId);
			messageRuleDTO.setNotificationTemplateList(templateList);

			/*JSONArray connectorList = null;
			connectorList = notificationEndpointProcessor.getConnectorListByUserId(userId);*/

			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
			String ruleBuilderInitData = mapper.writeValueAsString(messageRuleDTO);

			response.put("success", true).put("initData", new JSONObject(ruleBuilderInitData)
					.put("deviceTypeList", deviceTypeList));
			return ResponseEntity.ok().body(response.toString());

		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			response.put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}
		

	}

	@ApiOperation(value = "Save Rule ", notes = "API to save a new rule")
	@RequestMapping(path = "/save",method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> save(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
			@ApiParam(value = "JSON string which contain details for new rule creation") @RequestBody RuleBuilderDTO ruleBuilderDTO) {

		try {

			
			String userName = JWTHelper.parseJWTClaim(jwtToken, "userName");
			
			String jsonString = ruleBuilderDTO.getRuleContent();
			JSONObject jsonObj = new JSONObject(jsonString);
			
			

			MessageRuleDTO messageRuleDTO = new MessageRuleDTO();
			messageRuleDTO.setRuleName(ruleBuilderDTO.getRuleName());
			messageRuleDTO.setRuleText(ruleBuilderDTO.getRuleContent());
			messageRuleDTO.setEmailTemplateId(ruleBuilderDTO.getEmailTemplateId());
			messageRuleDTO.setSmsTemplateId(ruleBuilderDTO.getSmsTemplateId());
			messageRuleDTO.setMessageModelId(ruleBuilderDTO.getMessageModelId());
			messageRuleDTO.setSubServiceId(ruleBuilderDTO.getSubServiceId());
			messageRuleDTO.setCreatedTimeStamp(new Date());
			messageRuleDTO.setUpdatedTimeStamp(new Date());
			messageRuleDTO.setCreatedById(userName);
			


			DeviceTypeDTO deviceTypeDTO = ruleProcessor.getDeviceTypeByMessageModelId(ruleBuilderDTO.getMessageModelId());
			
			if (deviceTypeDTO != null)
				ruleProcessor.createRule(messageRuleDTO);

			JSONObject response = new JSONObject().put("success", true).put("msg", "Rule created successfully.");
			return ResponseEntity.ok().body(response.toString());

		}catch (ProcessorException ex) {
			LOG.error(ex.getMessage(), ex);
			JSONObject response = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}catch (JSONException je) {
			LOG.error(je.getMessage(), je);
			JSONObject response = new JSONObject().put("success", false).put("msg", "JSON format of rule content is not valid");
			return ResponseEntity.ok().body(response.toString());
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			JSONObject response = new JSONObject().put("success", false).put("msg", e.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}

	}

	@ApiOperation(value = "Get list of saved rules", notes = "API to fetch list of saved rules")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllRules(
			@ApiParam(value = "JWT authorization token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

		JSONArray resultList = new JSONArray();

		try {

			String userName = JWTHelper.parseJWTClaim(token, "userName");

			List<MessageRuleDTO> ruleDTOList = ruleProcessor.getAllAvailableRulesForUser(userName);

			for (MessageRuleDTO ruleDTO : ruleDTOList) {
				JSONObject rule = new JSONObject();

				MessageModel messageModel = messageModelProcessor.getById(ruleDTO.getMessageModelId());
				
				rule.put("ruleId", ruleDTO.getId());
				rule.put("ruleName", new JSONObject(ruleDTO.getRuleText()).getString("ruleName"));
				rule.put("ruleText", ruleDTO.getRuleText());
				rule.put("deviceTypeId", messageModel.getDeviceTypeBean().getId());
				rule.put("updatedTime", DateTimeUtils.convertToCommonTimeFormat(ruleDTO.getUpdatedTimeStamp()));
				rule.put("createdTime", DateTimeUtils.convertToCommonTimeFormat(ruleDTO.getCreatedTimeStamp()));

				resultList.put(rule);
			}

			JSONObject response = new JSONObject().put("success", true).put("msg", "Request processed successfully.")
					.put("rules", resultList);
			return ResponseEntity.ok().body(response.toString());

		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			JSONObject response = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}
	}

	/**
	 * @author AChowdhury
	 * @param ruleId
	 * @return JSON with 'success' flag
	 */
	@ApiOperation(value = "Delete a rule", notes = "API to delete rule for provided rule id")
	@RequestMapping(value = "/delete/{ruleId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteRuleById(
			@ApiParam(value = "Rule id which needs to be deleted") @PathVariable("ruleId") Long ruleId) {

		JSONObject result = new JSONObject();

		try {
			ruleProcessor.deleteRuleByRuleId(ruleId);
			result.put("success", true);
			result.put("msg", "Operation completed successfully.");
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			result.put("success", false);
			result.put("msg", ex.getMessage());
		}

		return ResponseEntity.ok().body(result.toString());
		
		
	}
	

	/**
	 * @author AChowdhury
	 * @param subDomainId
	 * @param customMessageModelName
	 * @return
	 *//*
	@RequestMapping(value = "/ruleBuilder/getCustomRuleFilters", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getCustomRuleFilters(
			@RequestParam(value = "subDomainId", required = true) Long subDomainId,
			@RequestParam(value = "messageModelName", required = true) String customMessageModelName) {

		CustomRuleAsset cRA = null;

		try {
			cRA = ruleProcessor.getCustomRuleMessageModel(subDomainId, customMessageModelName);

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
			
		} catch (Exception ex) {
			ex.printStackTrace();
			JSONObject response = new JSONObject().put("success", false).put("msg", ex.getMessage());
			return ResponseEntity.ok().body(response.toString());
		}

		JSONObject response = new JSONObject().put("success", false);
		return ResponseEntity.ok().body(response.toString());
	}
*/
}
