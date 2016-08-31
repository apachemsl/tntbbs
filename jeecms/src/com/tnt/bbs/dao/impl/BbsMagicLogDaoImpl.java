package com.tnt.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.tnt.bbs.dao.BbsMemberMagicDao;
import com.tnt.bbs.entity.BbsMemberMagic;
import com.tnt.common.hibernate3.Finder;
import com.tnt.common.hibernate3.HibernateBaseDao;
import com.tnt.common.page.Pagination;

@Repository
public class BbsMagicLogDaoImpl extends
		HibernateBaseDao<BbsMemberMagic, Integer> implements BbsMemberMagicDao {
	public Pagination getPage(Integer userId, int pageNo, int pageSize) {
		String hql = "from BbsMemberMagic magic ";
		Finder finder = Finder.create(hql);
		Pagination page = find(finder, pageNo, pageSize);
		return page;
	}

	public BbsMemberMagic findById(Integer id) {
		BbsMemberMagic entity = get(id);
		return entity;
	}

	public BbsMemberMagic save(BbsMemberMagic bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsMemberMagic deleteById(Integer id) {
		BbsMemberMagic entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	protected Class<BbsMemberMagic> getEntityClass() {
		return BbsMemberMagic.class;
	}
}