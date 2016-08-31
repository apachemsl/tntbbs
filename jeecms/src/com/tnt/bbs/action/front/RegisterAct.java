package com.tnt.bbs.action.front;

import static com.tnt.bbs.Constants.TPLDIR_MEMBER;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.tnt.bbs.cache.BbsConfigEhCache;
import com.tnt.bbs.entity.BbsConfig;
import com.tnt.bbs.entity.BbsLoginLog;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsUserExt;
import com.tnt.bbs.entity.BbsUserGroup;
import com.tnt.bbs.entity.BbsUserOnline;
import com.tnt.bbs.manager.BbsConfigMng;
import com.tnt.bbs.manager.BbsLoginLogMng;
import com.tnt.bbs.manager.BbsUserMng;
import com.tnt.bbs.manager.BbsUserOnlineMng;
import com.tnt.bbs.web.CmsUtils;
import com.tnt.bbs.web.FrontUtils;
import com.tnt.bbs.web.WebErrors;
import com.tnt.common.email.EmailSender;
import com.tnt.common.email.MessageTemplate;
import com.tnt.common.web.LoginUtils;
import com.tnt.common.web.RequestUtils;
import com.tnt.common.web.ResponseUtils;
import com.tnt.common.web.session.SessionProvider;
import com.tnt.core.entity.CmsConfigItem;
import com.tnt.core.entity.CmsSite;
import com.tnt.core.entity.UnifiedUser;
import com.tnt.core.manager.AuthenticationMng;
import com.tnt.core.manager.CmsConfigItemMng;
import com.tnt.core.manager.ConfigMng;
import com.tnt.core.manager.UnifiedUserMng;

/**
 * 前台会员注册Action
 * 
 * @author tom
 * 
 */
@Controller
public class RegisterAct {
	private static final Logger log = LoggerFactory
			.getLogger(RegisterAct.class);
	
	public static final String TPL_INDEX = "tpl.index";
	public static final String REGISTER = "tpl.register";
	public static final String REGISTER_RESULT = "tpl.registerResult";
	public static final String REGISTER_ACTIVE_SUCCESS = "tpl.registerActiveSuccess";
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final short REGISTER_CLOSE=0;
	
	@RequestMapping(value = "/register.jspx", method = RequestMethod.GET)
	public String input(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		// 没有开启会员注册
		if (config.getRegisterStatus().equals(REGISTER_CLOSE)) {
			return FrontUtils.showMessage(request, model,"member.registerClose");
		}
		List<CmsConfigItem>items=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		model.addAttribute("items", items);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, REGISTER);
	}

	@RequestMapping(value = "/register.jspx", method = RequestMethod.POST)
	public String submit(String username, String email, String password,
			BbsUserExt userExt, String captcha, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		WebErrors errors = validateSubmit(username, email, password, captcha,
				site, request, response);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		// 没有开启会员注册
		if (config.getRegisterStatus().equals(REGISTER_CLOSE)) {
			return FrontUtils.showMessage(request, model,"member.registerClose");
		}
		String ip = RequestUtils.getIpAddr(request);
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		Integer groupId = null;
		BbsUserGroup group = bbsConfigMng.findById(site.getId())
				.getRegisterGroup();
		if (group != null) {
			groupId = group.getId();
		}
		BbsUser user = null;
		if(config.getEmailValidate()){
			EmailSender sender = configMng.getEmailSender();
			MessageTemplate msgTpl = configMng.getRegisterMessageTemplate();
			if (sender == null) {
				// 邮件服务器没有设置好
				model.addAttribute("status", 4);
			} else if (msgTpl == null) {
				// 邮件模板没有设置好
				model.addAttribute("status", 5);
			} else {
				try {
					user = bbsUserMng.registerMember(username, email, password, ip,
							groupId, userExt,attrs, false, sender, msgTpl);
					bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
					model.addAttribute("status", 0);
				} catch (Exception e) {
					// 发送邮件异常
					model.addAttribute("status", 100);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				}
			}
			log.info("member register success. username={}", username);
			if (!StringUtils.isBlank(nextUrl)) {
				response.sendRedirect(nextUrl);
				return null;
			} else {
				site = CmsUtils.getSite(request);
				FrontUtils.frontData(request, model, site);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_MEMBER, REGISTER_RESULT);
			}
		}else{ 
			try {
				user = bbsUserMng.registerMember(username, email, false,password,
						  ip, groupId, userExt,attrs);
			} catch (Exception e) {
				model.addAttribute("status", 200);
				model.addAttribute("message", e.getMessage());
				log.error("send email exception.", e);
			}
			bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			FrontUtils.frontPageData(request, model);
			model.addAttribute("success",true);
			LoginUtils.loginShiro(request, response, username);
			return "redirect:/";
		}
	}
	
	
	@RequestMapping(value = "/appregister.jspx")
	public void appsubmit(String username, String email, String password,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException {
		String callback = request.getParameter("callback"); 
		JSONObject json = new JSONObject();
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateSubmit(username, email, password,site, request, response);
		if (!errors.hasErrors()) {
			   String ip = RequestUtils.getIpAddr(request);
			   Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
			   Integer groupId = null;
			   BbsUserGroup group = bbsConfigMng.findById(site.getId()).getRegisterGroup();
			    if (group != null) {
				  groupId = group.getId();
			    }
			    BbsUser user = null; 
			    try {
			    	BbsUserExt userExt = new BbsUserExt();
					user = bbsUserMng.registerMember(username, email,false, password,ip, groupId, userExt,attrs);
			    } catch (Exception e) {
					log.error("send email exception.", e);
				}
				bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
				log.info("member register success. username={}", username);
                LoginUtils.loginShiro(request, response, username);
				json.put("msg", true);
		}else{
			json.put("msg", false);
		}
		ResponseUtils.renderJson(response, callback+"("+json.toString()+ ")");
	}

	// 激活账号
	@RequestMapping(value = "/active.jspx", method = RequestMethod.GET)
	public String active(String username, String key,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateActive(username, key, request, response);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		UnifiedUser user = unifiedUserMng.active(username, key);
		BbsUser bbsUser = bbsUserMng.findById(user.getId());
		String ip = RequestUtils.getIpAddr(request);
		authMng.activeLogin(user, ip, request, response, session);
		// 登录记录
		BbsLoginLog loginLog = new BbsLoginLog();
		loginLog.setIp(RequestUtils.getIpAddr(request));
		Calendar calendar = Calendar.getInstance();
		loginLog.setLoginTime(calendar.getTime());
		loginLog.setUser(bbsUser);
		bbsLoginMng.save(loginLog);
		// 在线时长统计
		BbsUserOnline online = bbsUser.getUserOnline();
		// 首次登陆
		online = new BbsUserOnline();
		online.setUser(bbsUser);
		online.initial();
		userOnlineMng.save(online);
		FrontUtils.frontData(request, model, site);
		//激活成功直接登录
		LoginUtils.loginShiro(request, response, username);
		return "redirect:/";
	//	return FrontUtils.getTplPath(request, site,TPLDIR_MEMBER, REGISTER_ACTIVE_SUCCESS);
	}

	@RequestMapping(value = "/username_unique.jspx")
	public void usernameUnique(HttpServletRequest request,
			HttpServletResponse response) {
		String username = RequestUtils.getQueryParam(request, "username");
		// 用户名为空，返回false。
		if (StringUtils.isBlank(username)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		ResponseUtils.renderJson(response, "true");
	}

	@RequestMapping(value = "/email_unique.jspx")
	public void emailUnique(HttpServletRequest request,
			HttpServletResponse response) {
		String email = RequestUtils.getQueryParam(request, "email");
		// email为空，返回false。
		if (StringUtils.isBlank(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		// email存在，返回false。
		if (unifiedUserMng.emailExist(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		ResponseUtils.renderJson(response, "true");
	}

	private WebErrors validateSubmit(String username, String email,
			String password, String captcha, CmsSite site,
			HttpServletRequest request, HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		try {
			if (!imageCaptchaService.validateResponseForID(session
					.getSessionId(request, response), captcha)) {
				errors.addErrorCode("error.invalidCaptcha");
				return errors;
			}
		} catch (CaptchaServiceException e) {
			errors.addErrorCode("error.exceptionCaptcha");
			log.warn("", e);
			return errors;
		}
		// TODO长度限制应该可配
		if(errors.ifNotUsername(username, "username", 3, 20)){
			return errors;
		}
		if (errors.ifNotEmail(email, "email", 100)) {
			return errors;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			errors.addErrorCode("error.usernameExist");
			return errors;
		}
		return errors;
	}
	
	
	private WebErrors validateSubmit(String username, String email,
			String password, CmsSite site,
			HttpServletRequest request, HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		// TODO长度限制应该可配
		if(errors.ifNotUsername(username, "username", 3, 20)){
			return errors;
		}
		if (errors.ifNotEmail(email, "email", 100)) {
			return errors;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			errors.addErrorCode("error.usernameExist");
			return errors;
		}
		return errors;
	}

	private WebErrors validateActive(String username, String activationCode,
			HttpServletRequest request, HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		if (StringUtils.isBlank(username)
				|| StringUtils.isBlank(activationCode)) {
			errors.addErrorCode("error.exceptionParams");
			return errors;
		}
		UnifiedUser user = unifiedUserMng.getByUsername(username);
		if (user == null) {
			errors.addErrorCode("error.usernameNotExist");
			return errors;
		}
		if (user.getActivation()
				|| StringUtils.isBlank(user.getActivationCode())) {
			errors.addErrorCode("error.usernameActivated");
			return errors;
		}
		if (!user.getActivationCode().equals(activationCode)) {
			errors.addErrorCode("error.exceptionActivationCode");
			return errors;
		}
		return errors;
	}
	

	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private AuthenticationMng authMng;
	@Autowired
	private BbsLoginLogMng bbsLoginMng;
	@Autowired
	private BbsUserOnlineMng userOnlineMng;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;

}
