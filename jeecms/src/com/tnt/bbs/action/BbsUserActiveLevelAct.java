package com.tnt.bbs.action;

import static com.tnt.common.page.SimplePage.cpn;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tnt.bbs.entity.BbsUserActiveLevel;
import com.tnt.bbs.manager.BbsUserActiveLevelMng;
import com.tnt.common.page.Pagination;
import com.tnt.common.web.CookieUtils;
import com.tnt.core.web.WebErrors;

@Controller
public class BbsUserActiveLevelAct {
	private static final Logger log = LoggerFactory.getLogger(BbsUserActiveLevelAct.class);

	@RequestMapping("/activelevel/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		return "activelevel/list";
	}

	@RequestMapping("/activelevel/v_add.do")
	public String add(ModelMap model) {
		return "activelevel/add";
	}

	@RequestMapping("/activelevel/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		model.addAttribute("bbsactivelevel", manager.findById(id));
		return "activelevel/edit";
	}

	@RequestMapping("/activelevel/o_save.do")
	public String save(BbsUserActiveLevel bean, HttpServletRequest request, ModelMap model) {
		bean = manager.save(bean);
		log.info("save Bbsactivelevel id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/activelevel/o_update.do")
	public String update(BbsUserActiveLevel bean, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		bean = manager.update(bean);
		log.info("update Bbsactivelevel id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	@RequestMapping("/activelevel/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsUserActiveLevel[] beans = manager.deleteByIds(ids);
		for (BbsUserActiveLevel bean : beans) {
			log.info("delete Bbsactivelevel id={}", bean.getId());
		}
		return list(pageNo, request, model);
	}

	
	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id,  WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsUserActiveLevel entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsUserActiveLevel.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private BbsUserActiveLevelMng manager;
}