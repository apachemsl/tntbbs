package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsGrade;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsGradeDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsGrade findById(Integer id);

	public BbsGrade save(BbsGrade bean);

	public BbsGrade updateByUpdater(Updater<BbsGrade> updater);

	public BbsGrade deleteById(Integer id);
}