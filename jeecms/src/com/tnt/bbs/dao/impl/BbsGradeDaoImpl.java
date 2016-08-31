package com.tnt.bbs.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.tnt.bbs.dao.BbsGradeDao;
import com.tnt.bbs.entity.BbsGrade;
import com.tnt.common.hibernate3.HibernateBaseDao;
import com.tnt.common.page.Pagination;

@Repository
public class BbsGradeDaoImpl extends HibernateBaseDao<BbsGrade, Integer>
		implements BbsGradeDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsGrade findById(Integer id) {
		BbsGrade entity = get(id);
		return entity;
	}

	public BbsGrade save(BbsGrade bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsGrade deleteById(Integer id) {
		BbsGrade entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<BbsGrade> getEntityClass() {
		return BbsGrade.class;
	}
}