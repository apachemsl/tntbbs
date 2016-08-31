package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsReportExt;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsReportExtDao {
	public BbsReportExt findById(Integer id);

	public BbsReportExt save(BbsReportExt bean);

	public BbsReportExt updateByUpdater(Updater<BbsReportExt> updater);

	public BbsReportExt deleteById(Integer id);

	public Pagination getPage(Integer reportId,Integer pageNo, Integer pageSize);

	public BbsReportExt findByReportUid(Integer reportId, Integer userId);
}