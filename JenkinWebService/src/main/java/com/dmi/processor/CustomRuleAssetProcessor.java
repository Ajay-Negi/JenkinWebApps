package com.dmi.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.dao.ICustomRuleAssetDAO;
import com.dmi.dao.IDeviceTypeDAO;
import com.dmi.dao.model.CustomRuleAsset;
import com.dmi.dao.model.DeviceType;
import com.dmi.exception.ProcessorException;


@Service
public class CustomRuleAssetProcessor {

	private static final Logger LOG = Logger.getLogger(CustomRuleAssetProcessor.class);
	
	@Autowired
	ICustomRuleAssetDAO customRuleAssetDAO;
	
	@Autowired
	IDeviceTypeDAO deviceTypeDAO;
	
	@Transactional
	public void createCustomRuleAsset(CustomRuleAsset cra, long deviceTypeId, Long oemId) throws ProcessorException{
		try {
			DeviceType dt = deviceTypeDAO.get(deviceTypeId, oemId);
			CustomRuleAsset existingCRA = customRuleAssetDAO.getCRAByModelName(cra.getMessageModelName());

			if (dt == null)
				throw new ProcessorException("Device Type not found!");
			else if (existingCRA != null)
				throw new ProcessorException("Same custom model name exist, please rename" );

			cra.setDeviceTypeBean(dt);

			customRuleAssetDAO.save(cra);

		} catch (DataAccessException ex) {
			throw new ProcessorException("Try a different file name for message model.");
		} catch (Exception ex) {
			throw new ProcessorException("Assets failed to be created!");
		}

	}
	
	public CustomRuleAsset getCRAByDeviceTypeAndModelName(long deviceTypeId, String customMessageModelName)
			throws ProcessorException {

		CustomRuleAsset cra = null;
		try {
			cra = customRuleAssetDAO.getCRAByDeviceTypeAndModelName(deviceTypeId, customMessageModelName);
			System.out.println(cra);
			if (null == cra)
				throw new ProcessorException("Error fetching custom rule asset.");
		} catch (Exception ex) {
			LOG.error(ex);
			throw new ProcessorException("Error fetching custom rule asset.");
		}

		return cra;

	}
	
	public List<CustomRuleAsset> getCustomRuleAssetsByDeviceTypeId(long deviceTypeId) throws ProcessorException {
		List<CustomRuleAsset> craList = null;
		try {
			craList = customRuleAssetDAO.getCRAByDeviceTypeId(deviceTypeId);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			throw new ProcessorException("No assets found for current Device Type");
		}

		return craList;
	}
}
