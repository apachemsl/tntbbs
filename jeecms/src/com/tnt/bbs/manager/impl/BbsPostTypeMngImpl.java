package com.tnt.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsPostTypeDao;
import com.tnt.bbs.entity.BbsPostType;
import com.tnt.bbs.manager.BbsPostTypeMng;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

@Service
@Transactional
public class BbsPostTypeMngImpl implements BbsPostTypeMng {

    @Override
    @Transactional(readOnly = true)
    public Pagination getPage(Integer siteId, Integer forumId, Integer parentId, int pageNo,
                              int pageSize) {
        Pagination page = dao.getPage(siteId, forumId, parentId, pageNo, pageSize);
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public List getList(Integer siteId, Integer forumId, Integer parentId) {
        List list = dao.getList(siteId, forumId, parentId);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public BbsPostType findById(Integer id) {
        BbsPostType entity = dao.findById(id);
        return entity;
    }

    @Override
    public BbsPostType save(BbsPostType bean) {
        dao.save(bean);
        return bean;
    }

    @Override
    public BbsPostType update(BbsPostType bean) {
        Updater<BbsPostType> updater = new Updater<BbsPostType>(bean);
        bean = dao.updateByUpdater(updater);
        return bean;
    }

    @Override
    public BbsPostType deleteById(Integer id) {
        BbsPostType bean = dao.deleteById(id);
        return bean;
    }

    @Override
    public BbsPostType[] deleteByIds(Integer[] ids) {
        BbsPostType[] beans = new BbsPostType[ids.length];
        for (int i = 0, len = ids.length; i < len; i++) {
            beans[i] = deleteById(ids[i]);
        }
        return beans;
    }

    private BbsPostTypeDao dao;

    @Autowired
    public void setDao(BbsPostTypeDao bbsPostTypeDao) {
        this.dao = bbsPostTypeDao;
    }

}
