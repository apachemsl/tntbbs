package com.tnt.bbs.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.tnt.bbs.dao.BbsUserOnlineDao;
import com.tnt.bbs.entity.BbsUserOnline;
import com.tnt.common.hibernate3.Finder;
import com.tnt.common.hibernate3.HibernateBaseDao;
import com.tnt.common.page.Pagination;

@Repository
public class BbsUserOnlineDaoImpl extends
		HibernateBaseDao<BbsUserOnline, Integer> implements BbsUserOnlineDao {
	
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public List<BbsUserOnline> getList() {
		String hql="from BbsUserOnline online ";
		Finder finder=Finder.create(hql);
		return find(finder);
	}
	
	public int clearCount(boolean week, boolean month,boolean year) {
		StringBuilder hql = new StringBuilder("update BbsUserOnline bean");
		hql.append(" set bean.onlineDay=0,onlineLatest=0");
		if (week) {
			hql.append(",bean.onlineWeek=0");
		}
		if (month) {
			hql.append(",bean.onlineMonth=0");
		}
		if (year) {
			hql.append(",bean.onlineYear=0");
		}
		return getSession().createQuery(hql.toString()).executeUpdate();
	}

	public BbsUserOnline findById(Integer id) {
		BbsUserOnline entity = get(id);
		return entity;
	}

	public BbsUserOnline save(BbsUserOnline bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsUserOnline deleteById(Integer id) {
		BbsUserOnline entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	protected Class<BbsUserOnline> getEntityClass() {
		return BbsUserOnline.class;
	}
}