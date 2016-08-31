package com.tnt.bbs.action;

import static com.tnt.common.page.SimplePage.cpn;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tnt.bbs.entity.BbsCategory;
import com.tnt.bbs.entity.BbsForum;
import com.tnt.bbs.entity.BbsTopic;
import com.tnt.bbs.entity.BbsUserGroup;
import com.tnt.bbs.manager.BbsCategoryMng;
import com.tnt.bbs.manager.BbsForumMng;
import com.tnt.bbs.manager.BbsTopicMng;
import com.tnt.bbs.manager.BbsUserGroupMng;
import com.tnt.bbs.web.CmsUtils;
import com.tnt.common.page.Pagination;
import com.tnt.common.web.CookieUtils;
import com.tnt.core.entity.CmsSite;

@Controller
public class BbsForumAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsForumAct.class);

	@RequestMapping("/forum/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pagination.getPageNo());
		return "forum/list";
	}

	@RequestMapping("/forum/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		List<BbsCategory> categoryList = bbsCategoryMng.getList(CmsUtils
				.getSiteId(request));
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(CmsUtils
				.getSiteId(request));
		model.put("categoryList", categoryList);
		model.put("groupList", groupList);
		return "forum/add";
	}

	@RequestMapping("/forum/v_edit.do")
	public String edit(Integer id, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		List<BbsCategory> categoryList = bbsCategoryMng.getList(CmsUtils
				.getSiteId(request));
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(CmsUtils
				.getSiteId(request));
		model.put("categoryList", categoryList);
		model.put("groupList", groupList);
		model.addAttribute("bbsForum", manager.findById(id));
		model.addAttribute("pageNo", pageNo);
		return "forum/edit";
	}

	@RequestMapping("/forum/o_save.do")
	public String save(BbsForum bean, Integer categoryId, Integer[] views,
			Integer[] topics, Integer[] replies, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		bean = manager.save(bean, categoryId, site, views, topics, replies);
		log.info("save BbsForum id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/forum/o_update.do")
	public String update(BbsForum bean, Integer categoryId, Integer[] views,
			Integer[] topics, Integer[] replies, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		bean = manager.update(bean, categoryId, site, views, topics, replies);
		log.info("update BbsForum id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	@RequestMapping("/forum/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		for (int i = 0; i < ids.length; i++) {
			List<BbsTopic> list = bbsTopicMng.getList(ids[i],null,null,0,Integer.MAX_VALUE);
			for (int j = 0; j < list.size(); j++) {
				BbsTopic topic = bbsTopicMng.deleteById(list.get(j).getId());
				log.info("delete BbsTopic id={}", topic.getId());
			}
		}
		BbsForum[] beans = manager.deleteByIds(ids);
		for (BbsForum bean : beans) {
			log.info("delete BbsForum id={}", bean.getId());
		}
		
		return list(pageNo, request, model);
	}

	@RequestMapping("/forum/o_priority.do")
	public String priorityUpdate(Integer[] ids, Integer[] prioritys,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		if (ids == null || ids.length <= 0) {
			return "redirect:v_list.do";
		}
		CmsSite site = CmsUtils.getSite(request);
		BbsForum t;
		Integer id;
		Integer priority;
		for (int i = 0; i < ids.length; i++) {
			id = ids[i];
			priority = prioritys[i];
			if (id != null && priority != null) {
				t = manager.findById(id);
				if (t != null && t.getSite().getId().equals(site.getId())) {
					t.setPriority(priority);
					manager.update(t);
				}
			}
		}
		return list(pageNo, request, model);
	}

	@Autowired
	private BbsForumMng manager;
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
}