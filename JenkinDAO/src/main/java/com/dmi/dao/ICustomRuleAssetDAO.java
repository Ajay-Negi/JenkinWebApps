package com.dmi.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dmi.dao.model.CustomRuleAsset;

@Component
public interface ICustomRuleAssetDAO {

	void save(CustomRuleAsset cra);

	CustomRuleAsset getByDeviceTypeId(long id);

	CustomRuleAsset getCRAByDeviceTypeAndModelName(long deviceTypeId, String customMessageModelName);

	List<CustomRuleAsset> getCRAByDeviceTypeId(long deviceTypeId);

	CustomRuleAsset getCRAByModelName(String customMessageModelName);

}
