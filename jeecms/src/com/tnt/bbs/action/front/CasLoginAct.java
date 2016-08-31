package com.tnt.bbs.action.front;

import static com.tnt.bbs.Constants.TPLDIR_MEMBER;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tnt.bbs.web.CmsUtils;
import com.tnt.bbs.web.FrontUtils;
import com.tnt.core.entity.CmsSite;
import com.tnt.core.manager.UnifiedUserMng;

/**
 * 前台的登陆
 *  先走 拦截器
 *          com.tnt.bbs.web.FrontContextInterceptor
 *          com.tnt.bbs.web.FrontLocaleInterceptor
 * @author shaol.ma@aliyun.com
 * @version $Id: CasLoginAct.java, v 0.1 2016年8月31日 上午11:43:02 Exp $
 */
@Controller
public class CasLoginAct {

    public static final String LOGIN_INPUT             = "tpl.loginInput";
    public static final String LOGIN_STATUS            = "tpl.loginStatus";
    public static final String REGISTER_ACTIVE_SUCCESS = "tpl.registerActiveSuccess";

    @RequestMapping(value = "/login.jspx")
    public String login(HttpServletRequest request, ModelMap model) {
        CmsSite site = CmsUtils.getSite(request);
        String sol = site.getSolutionPath();
        model.addAttribute("site", site);
        FrontUtils.frontData(request, model, site);
        return FrontUtils.getTplPath(request, site, TPLDIR_MEMBER, LOGIN_INPUT);
    }

    @RequestMapping(value = "/login.jspx", method = RequestMethod.POST)
    public String submit(String username, HttpServletRequest request, ModelMap model) {
        CmsSite site = CmsUtils.getSite(request);
        String sol = site.getSolutionPath();
        Object error = request.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("errorRemaining", unifiedUserMng.errorRemaining(username));
        }
        FrontUtils.frontData(request, model, site);
        return FrontUtils.getTplPath(request, site, TPLDIR_MEMBER, LOGIN_INPUT);
    }

    @RequestMapping(value = "/bbs.jspx", method = RequestMethod.GET)
    public String bbs(String username, String password, HttpServletRequest request, ModelMap model) {
        try {
            /* String uri = "http://localhost:8081/jeecms/login.jspx";
             Map<String, String> params = new HashMap<String, String>();
             params.put("username", username);
             params.put("password", password);
             String result = HttpRequestUtil.request(uri, params, "post", "utf-8");
             System.out.println("result:" + result.equals("/"));*/
            return submit(username, request, model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:";
        }

    }

    @Autowired
    private UnifiedUserMng unifiedUserMng;
}
