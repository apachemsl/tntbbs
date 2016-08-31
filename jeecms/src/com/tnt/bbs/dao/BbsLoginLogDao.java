package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsLoginLog;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsLoginLogDao {
	
	public Pagination getPage(int pageNo, int pageSize);

	public BbsLoginLog findById(Integer id);

	public BbsLoginLog save(BbsLoginLog bean);

	public BbsLoginLog updateByUpdater(Updater<BbsLoginLog> bean);

	public BbsLoginLog deleteById(Integer id);
	
}