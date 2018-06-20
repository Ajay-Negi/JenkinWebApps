package com.dmi.dao;

import java.util.List;

import com.dmi.dao.model.VhrTemplate;

public interface IVhrTemplateDAO {

	VhrTemplate getByDeviceTypeId(Long deviceTypeId);

	void delete(Long vhrTemplateId);

	void save(VhrTemplate vhrTemplate);

	void update(VhrTemplate vhrTemplate);

	VhrTemplate getById(Long id);

	List<VhrTemplate> getAllByOemId(Long oemId);
}
