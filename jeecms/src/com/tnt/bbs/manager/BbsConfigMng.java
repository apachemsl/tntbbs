package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsConfig;
import com.tnt.core.entity.BbsConfigAttr;

public interface BbsConfigMng {
	public BbsConfig findById(Integer id);
	
	public BbsConfig get();

	public BbsConfig updateConfigForDay(Integer siteId);

	public BbsConfig update(BbsConfig bean);
	
}