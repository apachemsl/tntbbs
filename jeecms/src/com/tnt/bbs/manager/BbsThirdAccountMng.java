package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsThirdAccount;
import com.tnt.common.page.Pagination;

public interface BbsThirdAccountMng {
	public Pagination getPage(String username,String source,int pageNo, int pageSize);

	public BbsThirdAccount findById(Long id);
	
	public BbsThirdAccount findByKey(String key);

	public BbsThirdAccount save(BbsThirdAccount bean);

	public BbsThirdAccount update(BbsThirdAccount bean);

	public BbsThirdAccount deleteById(Long id);
	
	public BbsThirdAccount[] deleteByIds(Long[] ids);
}