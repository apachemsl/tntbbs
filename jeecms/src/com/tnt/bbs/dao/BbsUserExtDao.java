package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsUserExt;
import com.tnt.common.hibernate3.Updater;

public interface BbsUserExtDao {
	public BbsUserExt findById(Integer id);

	public BbsUserExt save(BbsUserExt bean);

	public BbsUserExt updateByUpdater(Updater<BbsUserExt> updater);
}