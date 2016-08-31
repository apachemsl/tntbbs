package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsMemberMagic;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsMemberMagicDao {

	public Pagination getPage(Integer userId, int pageNo, int pageSize);

	public BbsMemberMagic findById(Integer id);

	public BbsMemberMagic save(BbsMemberMagic bean);

	public BbsMemberMagic updateByUpdater(Updater<BbsMemberMagic> bean);

	public BbsMemberMagic deleteById(Integer id);

}