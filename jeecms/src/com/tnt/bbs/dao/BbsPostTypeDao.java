package com.tnt.bbs.dao;

import java.util.List;

import com.tnt.bbs.entity.BbsPostType;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsPostTypeDao {
	public Pagination getPage(Integer siteId,Integer forumId,Integer parentId,int pageNo, int pageSize);
	
	public List getList(Integer siteId,Integer forumId,Integer parentId);

	public BbsPostType findById(Integer id);

	public BbsPostType save(BbsPostType bean);

	public BbsPostType updateByUpdater(Updater<BbsPostType> updater);

	public BbsPostType deleteById(Integer id);
}