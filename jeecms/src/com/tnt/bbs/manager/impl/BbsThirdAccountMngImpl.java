package com.tnt.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsThirdAccountDao;
import com.tnt.bbs.entity.BbsThirdAccount;
import com.tnt.bbs.manager.BbsThirdAccountMng;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

@Service
@Transactional
public class BbsThirdAccountMngImpl implements BbsThirdAccountMng {
	@Transactional(readOnly = true)
	public Pagination getPage(String username,String source,int pageNo, int pageSize) {
		Pagination page = dao.getPage(username,source,pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsThirdAccount findById(Long id) {
		BbsThirdAccount entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsThirdAccount findByKey(String key){
		return dao.findByKey(key);
	}

	public BbsThirdAccount save(BbsThirdAccount bean) {
		dao.save(bean);
		return bean;
	}

	public BbsThirdAccount update(BbsThirdAccount bean) {
		Updater<BbsThirdAccount> updater = new Updater<BbsThirdAccount>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsThirdAccount deleteById(Long id) {
		BbsThirdAccount bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsThirdAccount[] deleteByIds(Long[] ids) {
		BbsThirdAccount[] beans = new BbsThirdAccount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsThirdAccountDao dao;

	@Autowired
	public void setDao(BbsThirdAccountDao dao) {
		this.dao = dao;
	}
}