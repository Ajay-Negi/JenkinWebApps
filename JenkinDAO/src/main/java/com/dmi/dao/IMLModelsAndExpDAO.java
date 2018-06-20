package com.dmi.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dmi.dao.model.MLModelsAndExp;


/**
 * @author MBansal
 *
 */
@Component
public interface IMLModelsAndExpDAO
{

	public boolean checkForExistingModelNameForDeviceType(Long oemId,
			Long deviceTypeId, String modelName);

	public void saveModel(MLModelsAndExp mlModelsAndExp);

	public List<MLModelsAndExp> fetchByOem(Long oemId);

	public void deleteModelById(Long modelId);

	List<MLModelsAndExp> fetchByOemAndDeviceType(long oemId, long deviceTypeId);
}
