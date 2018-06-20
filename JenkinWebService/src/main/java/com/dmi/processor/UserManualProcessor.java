package com.dmi.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.constant.Constants;
import com.dmi.dao.IDeviceRegistrationDAO;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.IOemDAO;
import com.dmi.dao.IUserDAO;
import com.dmi.dao.model.DeviceRegistration;
import com.dmi.dao.model.DeviceType;
import com.dmi.dao.model.Oem;
import com.dmi.dao.model.User;
import com.dmi.exception.ProcessorException;

import io.swagger.annotations.Api;


/**
 * 
 * @author MBansal
 *
 */
@Api(tags = "UserManual Controller")
@RestController
@RequestMapping(value="/deviceData")
@Service
public class UserManualProcessor
{
	
	private static final Logger LOG = Logger.getLogger(UserManualProcessor.class);
	@Autowired
	IOemDAO oemDAO;
	@Autowired
	IDeviceRegistrationDAO deviceRegistrationDAO;
	@Autowired
	IUserDAO userDAO;
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;

	public JSONArray getManualAvailibility(String roleCode, Long oemId, String username) throws ProcessorException
	{
		JSONArray response;
		try
		{
			response = new JSONArray();
			Oem oem = oemDAO.get(oemId);

			List<DeviceType> deviceTypes = null;
			List<DeviceRegistration> deviceRegistrations = null;

			if (roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
			{
				deviceTypes = deviceTypeDAO.getAll(oem);

				for (DeviceType deviceType : deviceTypes)
				{
					String destinationPathOnServer = Constants.EMANUAL_DIR + "EManual_" + oemId + "_"
							+ deviceType.getId() + ".pdf";

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("deviceTypeId", deviceType.getId());
					jsonObject.put("deviceTypeName", deviceType.getAlias());
					jsonObject.put("eManualAvailibility", new File(destinationPathOnServer).exists());
					response.put(jsonObject);
				}
			}
			else
			{
				User user = userDAO.getUser(username);
				deviceRegistrations = deviceRegistrationDAO.getRegisteredDevicesByUser(user);
				// deviceTypes = deviceTypeDAO.getAll(user);

				for (DeviceRegistration deviceRegistration : deviceRegistrations)
				{
					String destinationPathOnServer = Constants.EMANUAL_DIR + "EManual_" + oemId + "_"
							+ deviceRegistration.getDeviceBean().getDeviceTypeBean().getId() + ".pdf";

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("deviceTypeId", deviceRegistration.getDeviceBean().getDeviceTypeBean().getId());
					jsonObject.put("deviceTypeName", deviceRegistration.getDeviceBean().getDeviceTypeBean().getAlias()
							+ " - " + deviceRegistration.getDeviceBean().getAlias());
					jsonObject.put("eManualAvailibility", new File(destinationPathOnServer).exists());
					response.put(jsonObject);
				}
			}

			return response;
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	public void handleManualUpload(String roleCode, String oemId, Long deviceTypeId, MultipartFile file)
			throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can upload EManuals.");

			// Check if file is Blank
			if (file.isEmpty())
				throw new Exception("Empty File Uploaded.");

			// Check if file type is PDF
			if (!file.getContentType().equals("application/pdf"))
				throw new Exception("Only PDF file format is supported.");

			String destinationPathOnServer = Constants.EMANUAL_DIR + "EManual_" + oemId + "_" + deviceTypeId + ".pdf";
			File destinationFile = new File(destinationPathOnServer);
			file.transferTo(destinationFile);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

	public byte[] viewManual(String oemId, Long deviceTypeId) throws ProcessorException, IOException
	{
		FileInputStream inputStream = null;
		
		try
		{
			String filePathOnServer = Constants.EMANUAL_DIR + "EManual_" + oemId + "_" + deviceTypeId + ".pdf";
			File file = new File(filePathOnServer);
			
			try
			{
				inputStream = new FileInputStream(file);
			}
			catch (FileNotFoundException e)
			{
				throw new Exception("No EManual Found. Upload one!");
			}
			return IOUtils.toByteArray(inputStream);
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
		finally{
			if(inputStream !=null)
			inputStream.close(); //sonar fix by AN
		}
	}

	public void deleteManual(String roleCode, Long oemId, Long deviceTypeId) throws ProcessorException
	{
		try
		{
			// check if user is admin
			if (!roleCode.equalsIgnoreCase(Constants.USER_ROLE_CODE_ADMIN))
				throw new Exception("Only Admin can delete EManuals.");

			String destinationPathOnServer = Constants.EMANUAL_DIR + "EManual_" + oemId + "_" + deviceTypeId + ".pdf";
			File destinationFile = new File(destinationPathOnServer);
			destinationFile.delete();
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new ProcessorException(e.getMessage());
		}
	}

}
