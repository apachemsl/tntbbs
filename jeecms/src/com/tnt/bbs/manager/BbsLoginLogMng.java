package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsLoginLog;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.common.page.Pagination;

public interface BbsLoginLogMng {

	public Pagination getPage(int pageNo, int pageSize);

	public BbsLoginLog findById(Integer id);

	public BbsLoginLog save(BbsLoginLog bean);
	
	public BbsLoginLog loginLog(BbsUser user ,String ip);

	public BbsLoginLog update(BbsLoginLog bean);

	public BbsLoginLog deleteById(Integer id);

	public BbsLoginLog[] deleteByIds(Integer[] ids);
}