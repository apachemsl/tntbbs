package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsReportExt;
import com.tnt.common.page.Pagination;

public interface BbsReportExtMng {

	public Pagination getPage(Integer reportId,Integer pageNo, Integer pageSize);

	public BbsReportExt findById(Integer id);
	
	public BbsReportExt findByReportUid(Integer reportId,Integer userId);

	public BbsReportExt save(BbsReportExt bean);

	public BbsReportExt update(BbsReportExt bean);

	public BbsReportExt deleteById(Integer id);

	public BbsReportExt[] deleteByIds(Integer[] ids);
}