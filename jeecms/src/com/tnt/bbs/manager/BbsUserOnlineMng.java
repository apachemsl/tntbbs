package com.tnt.bbs.manager;

import java.util.List;

import com.tnt.bbs.entity.BbsSession;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsUserOnline;
import com.tnt.common.page.Pagination;
import com.tnt.core.entity.CmsConfig;

public interface BbsUserOnlineMng {

	public List<BbsUserOnline> getList();

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserOnline findById(Integer id);

	public BbsUserOnline save(BbsUserOnline bean);
	
	public BbsUserOnline saveByUser(BbsUser user);

	public BbsUserOnline update(BbsUserOnline bean);
	
	public void clearCount(CmsConfig config);
	
	public void updateUserOnlineTime(BbsSession userSession);

	public BbsUserOnline deleteById(Integer id);

	public BbsUserOnline[] deleteByIds(Integer[] ids);
}