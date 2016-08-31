package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsThirdAccount;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsThirdAccountDao {
	public Pagination getPage(String username,String source,int pageNo, int pageSize);

	public BbsThirdAccount findById(Long id);
	
	public BbsThirdAccount findByKey(String key);

	public BbsThirdAccount save(BbsThirdAccount bean);

	public BbsThirdAccount updateByUpdater(Updater<BbsThirdAccount> updater);

	public BbsThirdAccount deleteById(Long id);
}