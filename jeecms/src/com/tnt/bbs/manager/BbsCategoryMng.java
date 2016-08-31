package com.tnt.bbs.manager;

import java.util.List;

import com.tnt.bbs.entity.BbsCategory;

public interface BbsCategoryMng {
	public List<BbsCategory> getList(Integer webId);

	public BbsCategory findById(Integer id);

	public BbsCategory save(BbsCategory bean);

	public BbsCategory update(BbsCategory bean);

	public BbsCategory deleteById(Integer id);

	public BbsCategory[] deleteByIds(Integer[] ids);

	public BbsCategory[] updatePriority(Integer[] ids, Integer[] priority);
}