package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsConfig;
import com.tnt.common.hibernate3.Updater;

public interface BbsConfigDao {
	/**
	 * 清理当日数据
	 */
	public void clearTodayData();
	
	public BbsConfig findById(Integer id);

	public BbsConfig save(BbsConfig bean);

	public BbsConfig updateByUpdater(Updater<BbsConfig> updater);

	public BbsConfig deleteById(Integer id);
}