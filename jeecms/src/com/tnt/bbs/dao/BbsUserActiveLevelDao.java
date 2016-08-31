package com.tnt.bbs.dao;

import java.util.List;

import com.tnt.bbs.entity.BbsUserActiveLevel;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsUserActiveLevelDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsUserActiveLevel> getList(Integer count);

	public BbsUserActiveLevel findById(Integer id);

	public BbsUserActiveLevel save(BbsUserActiveLevel bean);

	public BbsUserActiveLevel updateByUpdater(Updater<BbsUserActiveLevel> updater);

	public BbsUserActiveLevel deleteById(Integer id);
}