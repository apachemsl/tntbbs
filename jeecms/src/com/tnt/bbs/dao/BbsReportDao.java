package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsReport;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsReportDao {
	public BbsReport findById(Integer id);

	public BbsReport save(BbsReport bean);

	public BbsReport updateByUpdater(Updater<BbsReport> updater);

	public BbsReport deleteById(Integer id);

	public Pagination getPage(Boolean status,Integer pageNo, Integer pageSize);

	public BbsReport findByUrl(String url);
}