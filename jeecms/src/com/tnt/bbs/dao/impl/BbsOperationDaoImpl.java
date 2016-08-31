package com.tnt.bbs.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.tnt.bbs.dao.BbsOperationDao;
import com.tnt.bbs.entity.BbsOperation;
import com.tnt.common.hibernate3.HibernateBaseDao;
import com.tnt.common.page.Pagination;

@Repository
public class BbsOperationDaoImpl extends
		HibernateBaseDao<BbsOperation, Integer> implements BbsOperationDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsOperation findById(Integer id) {
		BbsOperation entity = get(id);
		return entity;
	}

	public BbsOperation save(BbsOperation bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsOperation deleteById(Integer id) {
		BbsOperation entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<BbsOperation> getEntityClass() {
		return BbsOperation.class;
	}
}