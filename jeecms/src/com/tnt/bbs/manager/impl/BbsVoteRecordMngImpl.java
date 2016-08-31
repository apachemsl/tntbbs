package com.tnt.bbs.manager.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsVoteRecordDao;
import com.tnt.bbs.entity.BbsVoteRecord;
import com.tnt.bbs.manager.BbsVoteRecordMng;

@Service
@Transactional
public class BbsVoteRecordMngImpl implements BbsVoteRecordMng {
	public BbsVoteRecord findRecord(Integer userId, Integer topicId) {
		return dao.findRecord(userId, topicId);
	}

	public BbsVoteRecord save(BbsVoteRecord bean) {
		return dao.save(bean);
	}

	private BbsVoteRecordDao dao;

	@Autowired
	public void setDao(BbsVoteRecordDao dao) {
		this.dao = dao;
	}
}
