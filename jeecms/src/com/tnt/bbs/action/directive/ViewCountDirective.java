package com.tnt.bbs.action.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tnt.bbs.cache.TopicCountEhCache;
import com.tnt.bbs.entity.BbsTopicCountEnum;
import com.tnt.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ViewCountDirective implements TemplateDirectiveModel {

	public static final String TOPIC_ID = "topicId";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer topicId = DirectiveUtils.getInt(TOPIC_ID, params);
		Long viewCount = topicCountEhCache.getViewCount(topicId);
		Long viewDayCount = topicCountEhCache.getViewCount(topicId,BbsTopicCountEnum.day);
		Long viewWeekCount = topicCountEhCache.getViewCount(topicId,BbsTopicCountEnum.week);
		Long viewMonthCount = topicCountEhCache.getViewCount(topicId,BbsTopicCountEnum.month);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put("viewCount", DEFAULT_WRAPPER.wrap(viewCount));
		paramWrap.put("viewDayCount", DEFAULT_WRAPPER.wrap(viewDayCount));
		paramWrap.put("viewWeekCount", DEFAULT_WRAPPER.wrap(viewWeekCount));
		paramWrap.put("viewMonthCount", DEFAULT_WRAPPER.wrap(viewMonthCount));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private TopicCountEhCache topicCountEhCache;
}
