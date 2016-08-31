package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsMagicConfig;
import com.tnt.common.hibernate3.Updater;

public interface BbsMagicConfigDao {
	
	public BbsMagicConfig findById(Integer id);

	public BbsMagicConfig save(BbsMagicConfig bean);

	public BbsMagicConfig updateByUpdater(Updater<BbsMagicConfig> updater);

	public BbsMagicConfig deleteById(Integer id);
}