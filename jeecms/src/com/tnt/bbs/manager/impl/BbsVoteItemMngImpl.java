package com.tnt.bbs.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsVoteItemDao;
import com.tnt.bbs.entity.BbsTopic;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsVoteItem;
import com.tnt.bbs.entity.BbsVoteRecord;
import com.tnt.bbs.entity.BbsVoteTopic;
import com.tnt.bbs.manager.BbsVoteItemMng;
import com.tnt.bbs.manager.BbsVoteRecordMng;
import com.tnt.common.hibernate3.Updater;

@Service
@Transactional
public class BbsVoteItemMngImpl implements BbsVoteItemMng {
	public BbsVoteItem findById(Integer id) {
		return dao.findById(id);
	}

	public List<BbsVoteItem> findByTopic(Integer topicId) {
		return dao.findByTopic(topicId);
	}

	public BbsVoteItem save(BbsVoteItem bean) {
		return dao.save(bean);
	}

	public BbsVoteItem update(BbsVoteItem bean) {
		Updater<BbsVoteItem> updater = new Updater<BbsVoteItem>(bean);
		BbsVoteItem entity = dao.updateByUpdater(updater);
		return entity;
	}

	public void vote(BbsUser user, BbsVoteTopic topic, Integer[] itemIds) {
		int count = 0;
		for (Integer id : itemIds) {
			BbsVoteItem bean = findById(id);
			bean.setVoteCount(bean.getVoteCount() + 1);
			count++;
		}
		topic.setTotalCount(topic.getTotalCount() + count);
		BbsVoteRecord record = new BbsVoteRecord();
		record.setUser(user);
		record.setTopic(topic);
		record.setVoteTime(new Date());
		bbsVoteRecordMng.save(record);
	}

	private BbsVoteItemDao dao;
	@Autowired
	private BbsVoteRecordMng bbsVoteRecordMng;

	@Autowired
	public void setDao(BbsVoteItemDao dao) {
		this.dao = dao;
	}
}
