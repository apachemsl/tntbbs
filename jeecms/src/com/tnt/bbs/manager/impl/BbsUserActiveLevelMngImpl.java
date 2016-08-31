package com.tnt.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsUserActiveLevelDao;
import com.tnt.bbs.entity.BbsUserActiveLevel;
import com.tnt.bbs.manager.BbsUserActiveLevelMng;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

@Service
@Transactional
public class BbsUserActiveLevelMngImpl implements BbsUserActiveLevelMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsUserActiveLevel> getList(Integer count){
		return dao.getList(count);
	}

	@Transactional(readOnly = true)
	public BbsUserActiveLevel findById(Integer id) {
		BbsUserActiveLevel entity = dao.findById(id);
		return entity;
	}

	public BbsUserActiveLevel save(BbsUserActiveLevel bean) {
		dao.save(bean);
		return bean;
	}

	public BbsUserActiveLevel update(BbsUserActiveLevel bean) {
		Updater<BbsUserActiveLevel> updater = new Updater<BbsUserActiveLevel>(bean);
		BbsUserActiveLevel entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsUserActiveLevel deleteById(Integer id) {
		BbsUserActiveLevel bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsUserActiveLevel[] deleteByIds(Integer[] ids) {
		BbsUserActiveLevel[] beans = new BbsUserActiveLevel[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsUserActiveLevelDao dao;

	@Autowired
	public void setDao(BbsUserActiveLevelDao dao) {
		this.dao = dao;
	}
}