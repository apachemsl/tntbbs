package com.tnt.bbs.dao;

import java.util.List;

import com.tnt.bbs.entity.BbsUserOnline;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsUserOnlineDao {

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserOnline findById(Integer id);

	public BbsUserOnline save(BbsUserOnline bean);
	
	public int clearCount(boolean week, boolean month,boolean year);

	public BbsUserOnline updateByUpdater(Updater<BbsUserOnline> bean);

	public BbsUserOnline deleteById(Integer id);

	public List<BbsUserOnline> getList();

}