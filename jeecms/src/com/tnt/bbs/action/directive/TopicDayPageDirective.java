package com.tnt.bbs.action.directive;

import static com.tnt.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.tnt.bbs.action.directive.abs.AbstractTopicPageDirective;
import com.tnt.bbs.web.FrontUtils;
import com.tnt.common.page.Pagination;
import com.tnt.common.web.freemarker.DirectiveUtils;
import com.tnt.common.web.freemarker.DirectiveUtils.InvokeType;
import com.tnt.core.entity.CmsSite;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TopicDayPageDirective extends AbstractTopicPageDirective {

	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "topic_day_page";

	private static final String DAY = "day";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		InvokeType type = DirectiveUtils.getInvokeType(params);
		Integer day = DirectiveUtils.getInt(DAY, params);
		Pagination page = bbsTopicMng.getForSearchDate(site.getId(),
				getForumId(params), null, day, FrontUtils.getPageNo(env),
				FrontUtils.getCount(params));
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_PAGINATION, DEFAULT_WRAPPER.wrap(page));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		if (InvokeType.custom == type) {
			FrontUtils.includeTpl(TPL_NAME, site, params, env);
		} else if (InvokeType.body == type) {
			body.render(env.getOut());
			FrontUtils.includePagination(site, params, env);
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

}
