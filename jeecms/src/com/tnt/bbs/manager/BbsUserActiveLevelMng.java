package com.tnt.bbs.manager;

import java.util.List;

import com.tnt.bbs.entity.BbsUserActiveLevel;
import com.tnt.common.page.Pagination;

public interface BbsUserActiveLevelMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsUserActiveLevel> getList(Integer count);

	public BbsUserActiveLevel findById(Integer id);

	public BbsUserActiveLevel save(BbsUserActiveLevel bean);

	public BbsUserActiveLevel update(BbsUserActiveLevel bean);

	public BbsUserActiveLevel deleteById(Integer id);
	
	public BbsUserActiveLevel[] deleteByIds(Integer[] ids);
}