package com.tnt.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsUserExtDao;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsUserExt;
import com.tnt.bbs.manager.BbsUserExtMng;
import com.tnt.common.hibernate3.Updater;

@Service
@Transactional
public class BbsUserExtMngImpl implements BbsUserExtMng {
	public BbsUserExt save(BbsUserExt ext, BbsUser user) {
		ext.blankToNull();
		ext.setUser(user);
		dao.save(ext);
		return ext;
	}

	public BbsUserExt update(BbsUserExt ext, BbsUser user) {
		BbsUserExt entity = dao.findById(user.getId());
		if (entity == null) {
			entity = save(ext, user);
			user.getUserExtSet().add(entity);
			return entity;
		} else {
			Updater<BbsUserExt> updater = new Updater<BbsUserExt>(ext);
			updater.include("gender");
			updater.include("birthday");
			ext = dao.updateByUpdater(updater);
			ext.blankToNull();
			return ext;
		}
	}

	private BbsUserExtDao dao;

	@Autowired
	public void setDao(BbsUserExtDao dao) {
		this.dao = dao;
	}
}