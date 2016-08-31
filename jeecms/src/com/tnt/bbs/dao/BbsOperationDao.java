package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsOperation;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsOperationDao {

	public Pagination getPage(int pageNo, int pageSize);

	public BbsOperation findById(Integer id);

	public BbsOperation save(BbsOperation bean);

	public BbsOperation updateByUpdater(Updater<BbsOperation> updater);

	public BbsOperation deleteById(Integer id);

}