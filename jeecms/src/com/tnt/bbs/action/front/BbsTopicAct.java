package com.tnt.bbs.action.front;

import static com.tnt.bbs.Constants.TPLDIR_FORUM;
import static com.tnt.bbs.Constants.TPLDIR_TOPIC;
import static com.tnt.bbs.entity.BbsTopic.TOPIC_NORMAL;
import static com.tnt.bbs.entity.BbsTopic.TOPIC_VOTE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tnt.bbs.cache.TopicCountEhCache;
import com.tnt.bbs.entity.BbsCategory;
import com.tnt.bbs.entity.BbsForum;
import com.tnt.bbs.entity.BbsPostType;
import com.tnt.bbs.entity.BbsTopic;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsUserGroup;
import com.tnt.bbs.entity.BbsVoteItem;
import com.tnt.bbs.entity.BbsVoteTopic;
import com.tnt.bbs.manager.BbsCategoryMng;
import com.tnt.bbs.manager.BbsConfigMng;
import com.tnt.bbs.manager.BbsForumMng;
import com.tnt.bbs.manager.BbsLimitMng;
import com.tnt.bbs.manager.BbsPostTypeMng;
import com.tnt.bbs.manager.BbsTopicMng;
import com.tnt.bbs.manager.BbsUserGroupMng;
import com.tnt.bbs.manager.BbsVoteItemMng;
import com.tnt.bbs.manager.BbsVoteRecordMng;
import com.tnt.bbs.web.CmsUtils;
import com.tnt.bbs.web.FrontUtils;
import com.tnt.bbs.web.WebErrors;
import com.tnt.common.web.RequestUtils;
import com.tnt.common.web.ResponseUtils;
import com.tnt.common.web.springmvc.MessageResolver;
import com.tnt.core.entity.CmsSite;

@Controller
public class BbsTopicAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsTopicAct.class);
	public static final String CATEGORY_VOTE = "vote";

	public static final String TPL_TOPICADD = "tpl.topicadd";
	public static final String TPL_TOPICEDIT = "tpl.topicedit";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_GUANSHUI = "tpl.guanshui";
	public static final String TPL_TOPIC_MOVE = "tpl.topicmove";
	public static final String TPL_TOPIC_SHIELD = "tpl.topicshield";
	public static final String TPL_TOPIC_LOCK = "tpl.topiclock";
	public static final String TPL_TOPIC_UPORDOWN = "tpl.topicupordown";
	public static final String TPL_TOPIC_PRIME = "tpl.topicprime";
	public static final String TPL_TOPIC_UPTOP = "tpl.topicuptop";
	public static final String TPL_TOPIC_HIGHLIGHT = "tpl.topichighlight";
	public static final String TPL_TOPIC_VOTERESULT = "tpl.topicVoteResult";
	public static final String TPL_DAY_TOPIC = "tpl.daytopic";
	public static final String TPL_NO_VIEW = "tpl.noview";
	public static final String TPL_NO_POSTTYPE = "tpl.noposttype";

	@RequestMapping("/topic/v_add{forumId}.jspx")
	public String add(@PathVariable Integer forumId, String category,
			Integer fid, HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = null;
		if (forumId != null) {
			model.put("forumId", forumId);
			forum = bbsForumMng.findById(forumId);
		} else {
			model.put("forumId", fid);
			forum = bbsForumMng.findById(fid);
		}
		String msg = checkTopic(request,forum, user, site);
		Set<BbsPostType>postTypes=forum.getPostTypes();
		model.put("postTypes", postTypes);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		model.put("category", parseCategory(category));
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPICADD);
	}

	@RequestMapping("/topic/o_save.jspx")
	public String save(Integer forumId, Integer postTypeId, String title,
			String content, Integer category, String[] name,
			@RequestParam(value = "code", required = false) List<String> code,
			HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkTopic(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		boolean flag = topicCountEhCache.getLastReply(user.getId(), 15);
		if (!flag) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_GUANSHUI);
		}
		if(postTypeId==null){
			postTypeId= ((BbsPostType)postTypeMng.getList(site.getId(), null, null).get(0)).getId();
		}
		if(postTypeId==null){
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_POSTTYPE);
		}
		String ip = RequestUtils.getIpAddr(request);
		title=filterUserInputContent(title);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		//content=filterUserInputContent(content);
		if(StringUtils.isBlank(title)){
			WebErrors errors=WebErrors.create(request);
			errors.addErrorCode("operate.faile");
			return FrontUtils.showError(request, response, model, errors);
		}
		//检查文件后缀
		String allowSuffix=site.getConfig().getAllowSuffix();
		List<MultipartFile>files=multipartRequest.getFiles("attachment");
		if(checkFiles(allowSuffix, files)){
			WebErrors errors=WebErrors.create(request);
			errors.addErrorCode("upload.forbidden");
			return FrontUtils.showError(request, response, model, errors);
		}
		BbsTopic bean = manager.postTopic(user.getId(), site.getId(), forumId,
				postTypeId, title, content, ip, category, name,files, code);
		log.info("save BbsTopic id={}", bean.getId());
		bbsUserGroupMng.findNearByPoint(user.getPoint(), user);
		return "redirect:" + bean.getRedirectUrl();
	}
	
	@RequestMapping("/topic/v_moveInput.jspx")
	public String moveInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkShield(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("forum", forum);
		}
		List<BbsCategory> categoryList = bbsCategoryMng.getList(site.getId());
		model.put("categoryList", categoryList);
		model.put("topicIds", topicIds);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_MOVE);
	}

	@RequestMapping("/topic/o_moveSubmit.jspx")
	public String moveSubmit(Integer[] topicIds, Integer moveTo,
			Integer forumId, String reason, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkShield(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.move(topicIds, moveTo, reason, user);
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/v_shieldInput.jspx")
	public String shieldInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkShield(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_SHIELD);
	}

	@RequestMapping("/topic/o_shieldSubmit.jspx")
	public String shieldSubmit(Integer[] topicIds, Integer forumId,
			boolean shield, String reason, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkShield(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.shieldOrOpen(topicIds, shield, reason, user);
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/v_lockInput.jspx")
	public String lockInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_LOCK);
	}

	@RequestMapping("/topic/o_lockSumbit.jspx")
	public String lockSubmit(Integer[] topicIds, Integer forumId, boolean lock,
			String reason, HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.lockOrOpen(topicIds, lock, reason, user);
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/v_upordownInput.jspx")
	public String upOrDownInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_UPORDOWN);
	}

	@RequestMapping("/topic/o_upordownSubmit.jspx")
	public String upOrDownSubmit(Integer[] topicIds, Integer forumId,
			Date time, String reason, HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.upOrDown(topicIds, time, reason, user);
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/v_primeInput.jspx")
	public String primeInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_PRIME);
	}

	@RequestMapping("/topic/o_primeSubmit.jspx")
	public String primeSubmit(Integer[] topicIds, Integer forumId,
			short primeLevel, String reason, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.prime(topicIds, primeLevel, reason, user);
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/v_upTopInput.jspx")
	public String upTopInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkUpTop(request,forum, user, site, BbsTopic.NORMAL);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_UPTOP);
	}

	@RequestMapping("/topic/o_upTopSubmit.jspx")
	public String upTopSubmit(Integer[] topicIds, Integer forumId,
			short topLevel, String reason, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkUpTop(request,forum, user, site, BbsTopic.NORMAL);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.upTop(topicIds, topLevel, reason, user);
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/v_highlightInput.jspx")
	public String highlightInput(Integer[] topicIds, Integer forumId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (forumId != null) {
			BbsForum forum = bbsForumMng.findById(forumId);
			String msg = checkManager(request,forum, user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("forum", forum);
		}
		model.put("topicIds", topicIds);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_HIGHLIGHT);
	}

	@RequestMapping("/topic/o_highlightSubmit.jspx")
	public String highlightSubmit(Integer[] topicIds, Integer forumId,
			String color, boolean bold, boolean italic, Date time,
			String reason, HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsForum forum = bbsForumMng.findById(forumId);
		String msg = checkManager(request,forum, user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.highlight(topicIds, color, bold, italic, time, reason, user);
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/searchDayTopic*.jspx")
	public String searchByDay(Integer forumId, Integer day,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.findById(forumId);
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			return FrontUtils.getTplPath(request, site,
					TPLDIR_FORUM, TPL_NO_VIEW);
		}
		model.put("manager", checkManager(request,forum, user, site) == null ? true
				: false);
		model.put("uptop", checkUpTop(forum, user, site));
		model.put("sheild", checkShield(request,forum, user, site) == null ? true
				: false);
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		model.put("day", day);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_DAY_TOPIC);
	}

	@RequestMapping("/topic/o_delete.jspx")
	public String delete(Integer[] topicIds, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		BbsTopic[] beans = manager.deleteByIds(topicIds);
		BbsForum forum = beans[0].getForum();
		for (BbsTopic bean : beans) {
			log.info("delete BbsTopic id={}", bean.getId());
		}
		return "redirect:" + forum.getRedirectUrl();
	}

	@RequestMapping("/topic/vote_result.jspx")
	public String result(Integer tid, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsTopic topic = manager.findById(tid);
		if (bbsVoteRecordMng.findRecord(user == null ? null : user.getId(), tid) != null) {
			model.addAttribute("voted", true);
		}
		List<BbsVoteItem> list = bbsVoteItemMng.findByTopic(tid);
		model.addAttribute("voteItems", list);
		model.addAttribute("topic", topic);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_VOTERESULT);
	}

	@RequestMapping("/topic/vote.jspx")
	public void vote(Integer tid, Integer[] itemIds,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException {
		BbsUser user = CmsUtils.getUser(request);
		BbsVoteTopic topic = (BbsVoteTopic) manager.findById(tid);
		JSONObject json = new JSONObject();
		WebErrors errors = checkVote(request, user, topic, itemIds);
		if (!errors.hasErrors()) {
			bbsVoteItemMng.vote(user, topic, itemIds);
			json.put("success", true);
		} else {
			json.put("success", false);
			json.put("message", errors.getErrors());
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping("/topic/v_list_json.jspx")
	public void getJsonList(Integer forumId,String keywords,Boolean mytopic,Boolean myreply,
			Integer first, Integer count,
			HttpServletRequest request,HttpServletResponse  response) {
		if(count==null){
			count=5;
		}
		if(first==null){
			first=0;
		}
		JSONArray array=new JSONArray();
		List<BbsTopic>list=null;
		if(forumId!=null){
			//板块下的主题列表
			list=manager.getList(forumId, null,null,first, count);
		}else if(StringUtils.isNotBlank(keywords)){
			//搜索帖子
			list=manager.getList(null, keywords, null, first, count);
		}else if(mytopic!=null&&mytopic){
			//我的主题
			BbsUser user=CmsUtils.getUser(request);
			if(user!=null){
				list=manager.getList(null, null, user.getId(),first, count);
			}
		}else if(myreply!=null&&myreply){
			//我的回复
			BbsUser user=CmsUtils.getUser(request);
			if(user!=null){
				list=manager.getMemberReply(CmsUtils.getSiteId(request), user.getId(), first, count);
			}
		}else{
			//整个论坛下的主题列表
			list=manager.getNewList(first,count, 1);
		}
		if(list!=null&&list.size()>0){
			SimpleDateFormat format =new SimpleDateFormat("yy-MM-dd HH:mm");
			try {
				for(int i=0;i<list.size();i++){
					BbsTopic topic=list.get(i);
					JSONObject object = new JSONObject();
					object.put("username", topic.getCreater().getUsername());
					object.put("createTime",format.format(topic.getCreateTime()));
					object.put("title", topic.getTitle());
					object.put("content", topic.getTitle());
					object.put("url", topic.getUrl());
					object.put("replyCount", topic.getReplyCount());
					array.put(object);
				}
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
		}
		ResponseUtils.renderJson(response, array.toString());
	}

	private WebErrors checkVote(HttpServletRequest request, BbsUser user,
			BbsTopic topic, Integer[] itemIds) {
		WebErrors errors = WebErrors.create(request);
		if (user == null) {
			errors.addErrorCode("vote.noLogin");
			return errors;
		}
		if (itemIds == null) {
			errors.addErrorCode("vote.noChoose");
			return errors;
		}
		// 选项不是同一主题
		for (Integer itemid : itemIds) {
			if (!topic.equals(bbsVoteItemMng.findById(itemid).getTopic())) {
				errors.addErrorCode("vote.wrongItem");
				return errors;
			}
		}
		// 已经投过票
		if (bbsVoteRecordMng.findRecord(user.getId(), topic.getId()) != null) {
			errors.addErrorCode("vote.hasVoted");
			return errors;
		}
		return errors;
	}

	private String checkTopic(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		String msg="";
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");
			return msg;
		} else {
			BbsUserGroup group = user.getGroup();
			if (user.getProhibit()) {
				msg=MessageResolver.getMessage(request, "member.gag");
				return msg;
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.checkPostToday(user.getPostToday())) {
				msg=MessageResolver.getMessage(request, "member.posttomany");
				return msg;
			}
		}
		String ip=RequestUtils.getIpAddr(request);
		boolean ipLimit=bbsLimitMng.ipIsLimit(ip);
		boolean userLimit=bbsLimitMng.userIsLimit(user.getId());
		if(ipLimit){
			msg=MessageResolver.getMessage(request, "member.ipforbidden");
			return msg;
		}
		if(userLimit){
			msg=MessageResolver.getMessage(request, "member.userforbidden");
			return msg;
		}
		return null;
	}

	private String checkManager(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		String msg;
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.topicManage()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
		}
		return null;
	}

	private String checkUpTop(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site,
			short topLevel) {
		String msg;
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (group.topicTop() == 0) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (topLevel > 0) {
				if (group.topicTop() < topLevel) {
					msg=MessageResolver.getMessage(request, "member.hasnopermission");
					return msg;
				}
			}
		}
		return null;
	}

	private String checkShield(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		String msg;
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.topicShield()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
		}
		return null;
	}

	private boolean checkView(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (forum.getGroupViews().indexOf("," + group.getId() + ",") == -1) {
				return false;
			}
		}
		return true;
	}

	private boolean checkUpTop(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
			if (group.topicTop() == 0) {
				return false;
			}
		}
		return true;
	}

	private boolean checkModerators(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
		}
		return true;
	}

	private Integer parseCategory(String category) {
		if (CATEGORY_VOTE.equals(category)) {
			return TOPIC_VOTE;
		}
		return TOPIC_NORMAL;
	}
	
	private boolean checkFiles(String allowSuffix,List<MultipartFile>files){
		//不为空设置检查
		if(StringUtils.isNotBlank(allowSuffix)){
			String[] exts=allowSuffix.split(",");
			for(MultipartFile file:files){
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
				//文件格式检查
				if(isNotInArray(exts, ext)){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	private boolean isNotInArray(String[] exts,String ext){
		if(exts!=null&&exts.length>0){
			for(String e:exts){
				if(e.equals(ext)){
					return false;
				}
			}
			return true;
		}else{
			//exts为空
			return true;
		}
	}

	private final static Whitelist user_content_filter = Whitelist.relaxed();
	static {
		user_content_filter.addTags("embed","object","param","span","div");
		user_content_filter.addAttributes(":all", "style", "class", "id", "name");
		user_content_filter.addAttributes("object", "width", "height","classid","codebase");	
		user_content_filter.addAttributes("param", "name", "value");
		user_content_filter.addAttributes("embed", "src","quality","width","height","allowFullScreen","allowScriptAccess","flashvars","name","type","pluginspage");
	}

	/**
	 * 对用户输入内容进行过滤
	 * @param html
	 * @return
	 */
	public static String filterUserInputContent(String html) {
		if(StringUtils.isBlank(html)) return "";
		return Jsoup.clean(html, user_content_filter);
	}

	@Autowired
	private BbsTopicMng manager;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsVoteItemMng bbsVoteItemMng;
	@Autowired
	private BbsVoteRecordMng bbsVoteRecordMng;
	@Autowired
	private BbsPostTypeMng postTypeMng;
	@Autowired
	private BbsLimitMng bbsLimitMng;
}
