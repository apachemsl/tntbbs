package com.tnt.bbs.manager;

import java.util.List;

import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsVoteItem;
import com.tnt.bbs.entity.BbsVoteTopic;


public interface BbsVoteItemMng {
	public BbsVoteItem findById(Integer id);
	
	public List<BbsVoteItem> findByTopic(Integer topicId);
	
	public BbsVoteItem save(BbsVoteItem bean);
	
	public BbsVoteItem update(BbsVoteItem bean);

	public void vote(BbsUser user, BbsVoteTopic topic, Integer[] itemIds);
}
