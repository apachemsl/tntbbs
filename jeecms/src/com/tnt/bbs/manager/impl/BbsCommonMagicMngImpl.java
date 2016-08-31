package com.tnt.bbs.manager.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsCommonMagicDao;
import com.tnt.bbs.entity.BbsCommonMagic;
import com.tnt.bbs.entity.BbsUserGroup;
import com.tnt.bbs.manager.BbsCommonMagicMng;
import com.tnt.bbs.manager.BbsUserGroupMng;
import com.tnt.common.hibernate3.Updater;

@Service
@Transactional
public class BbsCommonMagicMngImpl implements BbsCommonMagicMng {

    @Override
    @Transactional(readOnly = true)
    public List<BbsCommonMagic> getList() {
        return dao.getList();
    }

    @Override
    @Transactional(readOnly = true)
    public BbsCommonMagic findById(Integer id) {
        BbsCommonMagic config = dao.findById(id);
        return config;
    }

    @Override
    @Transactional(readOnly = true)
    public BbsCommonMagic findByIdentifier(String mid) {
        return dao.findByIdentifier(mid);
    }

    @Override
    public BbsCommonMagic update(BbsCommonMagic bean) {
        BbsCommonMagic entity = findById(bean.getId());
        Updater<BbsCommonMagic> updater = new Updater<BbsCommonMagic>(bean);
        entity = dao.updateByUpdater(updater);
        return entity;
    }

    @Override
    public BbsCommonMagic updateByGroup(BbsCommonMagic bean, Integer[] groupIds,
                                        Integer[] beUsedGroupIds) {
        BbsCommonMagic entity = findById(bean.getId());
        Updater<BbsCommonMagic> updater = new Updater<BbsCommonMagic>(bean);
        entity = dao.updateByUpdater(updater);
        // 可使用组权限
        Set<BbsUserGroup> groups = entity.getUseGroups();
        groups.clear();
        if (groupIds != null && groupIds.length > 0) {
            for (Integer groupId : groupIds) {
                entity.addToGroups(bbsUserGroupMng.findById(groupId));
            }
        }
        // 允许被使用的用户组
        Set<BbsUserGroup> beUsedGroups = entity.getToUseGroups();
        beUsedGroups.clear();
        if (beUsedGroupIds != null && beUsedGroupIds.length > 0) {
            for (Integer groupId : beUsedGroupIds) {
                entity.addToToUseGroups(bbsUserGroupMng.findById(groupId));
            }
        }
        return entity;
    }

    private BbsCommonMagicDao dao;
    @Autowired
    private BbsUserGroupMng   bbsUserGroupMng;

    @Autowired
    public void setDao(BbsCommonMagicDao bbsCommonMagicDao) {
        this.dao = bbsCommonMagicDao;
    }

}
