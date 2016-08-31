package com.tnt.core.dao;

import java.util.List;

import com.tnt.common.hibernate3.Updater;
import com.tnt.core.entity.CmsConfig;

public interface CmsConfigDao {
	public CmsConfig findById(Integer id);

	public CmsConfig updateByUpdater(Updater<CmsConfig> updater);

	public List<CmsConfig> getList();
}