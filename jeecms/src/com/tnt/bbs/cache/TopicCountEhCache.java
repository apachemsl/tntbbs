package com.tnt.bbs.cache;

import com.tnt.bbs.entity.BbsTopicCountEnum;

public interface TopicCountEhCache {

	public Long getViewCount(Integer topicId);
	
	public Long getViewCount(Integer topicId,BbsTopicCountEnum e);
	
	public Long setViewCount(Integer topicId);

	public boolean getLastReply(Integer userId, long time);

}
