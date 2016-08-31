package com.tnt.bbs.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tnt.bbs.entity.BbsTopicCountEnum;
import com.tnt.bbs.manager.BbsForumMng;
import com.tnt.bbs.manager.BbsTopicMng;
import com.tnt.bbs.manager.BbsUserMng;

public class BbsJob {
	private static final Logger log = LoggerFactory.getLogger(BbsJob.class);

	public void execute() {
		try {
			//清除板块每日帖子总量
			manager.updateAll_topic_today();
			//清除主题每日回复量
			bbsTopicMng.updateAllTopicCount(BbsTopicCountEnum.day);
			//更新用户活跃等级
			bbsUserMng.updateActiveLevel();
			log.info("update updateAll_topic_today success!");
		} catch (Exception e) {
			// TODO: handle exception
			log.info("update updateAll_topic_today fail!");
		}
	}

	@Autowired
	private BbsForumMng manager;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
}