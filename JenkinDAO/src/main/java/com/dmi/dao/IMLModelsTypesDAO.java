package com.dmi.dao;

import org.springframework.stereotype.Component;

/**
 * @author MBansal
 *
 */
@Component
public interface IMLModelsTypesDAO
{
	public Long getModelTypeIdFromName(String modelTypeName);

	public String getModelTypeNameFromTypeId(Long modelTypeId);
}
