package com.tnt.bbs.dao;

import java.util.List;

import com.tnt.bbs.entity.BbsCommonMagic;
import com.tnt.common.hibernate3.Updater;

public interface BbsCommonMagicDao {
	
	public List<BbsCommonMagic> getList();

	public BbsCommonMagic findById(Integer id);

	public BbsCommonMagic findByIdentifier(String mid);
	
	public BbsCommonMagic save(BbsCommonMagic bean);

	public BbsCommonMagic updateByUpdater(Updater<BbsCommonMagic> updater);

	public BbsCommonMagic deleteById(Integer id);

}