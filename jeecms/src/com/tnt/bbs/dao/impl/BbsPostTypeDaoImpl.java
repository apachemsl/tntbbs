package com.tnt.bbs.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tnt.bbs.dao.BbsPostTypeDao;
import com.tnt.bbs.entity.BbsPostType;
import com.tnt.common.hibernate3.Finder;
import com.tnt.common.hibernate3.HibernateBaseDao;
import com.tnt.common.page.Pagination;

@Repository
public class BbsPostTypeDaoImpl extends HibernateBaseDao<BbsPostType, Integer> implements
                                                                              BbsPostTypeDao {
    @Override
    public Pagination getPage(Integer siteId, Integer forumId, Integer parentId, int pageNo,
                              int pageSize) {
        String hql = "from BbsPostType postType where 1=1 ";
        Finder finder = Finder.create(hql);
        if (siteId != null) {
            finder.append("  and postType.site.id=:siteId").setParam("siteId", siteId);
        }
        if (forumId != null) {
            finder.append("  and postType.forum.id=:forumId").setParam("forumId", forumId);
        }
        if (parentId != null) {
            finder.append("  and postType.parent.id=:parentId").setParam("parentId", parentId);
        } else {
            finder.append("  and postType.parent is null");
        }
        finder.append(" order by postType.priority asc");
        return find(finder, pageNo, pageSize);
    }

    @Override
    public List getList(Integer siteId, Integer forumId, Integer parentId) {
        String hql = "from BbsPostType postType ";
        Finder finder = Finder.create(hql);
        if (siteId != null) {
            finder.append(" where postType.site.id=:siteId").setParam("siteId", siteId);
        }
        if (forumId != null) {
            finder.append("  and postType.forum.id=:forumId").setParam("forumId", forumId);
        }
        if (parentId != null) {
            finder.append("  and postType.parent.id=:parentId").setParam("parentId", parentId);
        } else {
            finder.append("  and postType.parent is null");
        }
        finder.append(" order by postType.priority asc");
        return find(finder);
    }

    @Override
    public BbsPostType findById(Integer id) {
        BbsPostType entity = get(id);
        return entity;
    }

    @Override
    public BbsPostType save(BbsPostType bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public BbsPostType deleteById(Integer id) {
        BbsPostType entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<BbsPostType> getEntityClass() {

        return BbsPostType.class;
    }
}