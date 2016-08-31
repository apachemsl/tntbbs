package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsMagicLog;
import com.tnt.common.page.Pagination;

public interface BbsMagicLogMng {

	public Pagination getPage(Byte operator,Integer userId,int pageNo, int pageSize);

	public BbsMagicLog findById(Integer id);

	public BbsMagicLog save(BbsMagicLog bean);

	public BbsMagicLog update(BbsMagicLog bean);

	public BbsMagicLog deleteById(Integer id);

	public BbsMagicLog[] deleteByIds(Integer[] ids);
}