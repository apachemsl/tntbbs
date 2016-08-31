package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsVoteRecord;




public interface BbsVoteRecordDao {
	public BbsVoteRecord findRecord(Integer userId, Integer topicId);
	
	public BbsVoteRecord save(BbsVoteRecord bean);
}
