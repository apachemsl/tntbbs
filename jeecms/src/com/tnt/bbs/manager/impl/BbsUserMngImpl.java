package com.tnt.bbs.manager.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.bbs.dao.BbsUserDao;
import com.tnt.bbs.entity.BbsCommonMagic;
import com.tnt.bbs.entity.BbsMemberMagic;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsUserActiveLevel;
import com.tnt.bbs.entity.BbsUserExt;
import com.tnt.bbs.entity.BbsUserGroup;
import com.tnt.bbs.entity.BbsUserOnline;
import com.tnt.bbs.manager.BbsCommonMagicMng;
import com.tnt.bbs.manager.BbsMemberMagicMng;
import com.tnt.bbs.manager.BbsUserActiveLevelMng;
import com.tnt.bbs.manager.BbsUserExtMng;
import com.tnt.bbs.manager.BbsUserGroupMng;
import com.tnt.bbs.manager.BbsUserMng;
import com.tnt.bbs.manager.BbsUserOnlineMng;
import com.tnt.common.email.EmailSender;
import com.tnt.common.email.MessageTemplate;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;
import com.tnt.core.entity.UnifiedUser;
import com.tnt.core.manager.CmsConfigMng;
import com.tnt.core.manager.UnifiedUserMng;

@Service
@Transactional
public class BbsUserMngImpl implements BbsUserMng {

    @Override
    public List<BbsUser> getList(String username, Integer count) {
        return dao.getList(username, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination getPage(String username, String email, Integer groupId, Boolean disabled,
                              Boolean admin, Boolean official, Integer lastLoginDay,
                              Integer orderBy, int pageNo, int pageSize) {
        Pagination page = dao.getPage(username, email, groupId, disabled, admin, official,
            lastLoginDay, orderBy, pageNo, pageSize);
        return page;
    }

    @Transactional(readOnly = true)
    public List<BbsUser> getList(Integer count) {
        return dao.getList(count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BbsUser> getAdminList(Integer siteId, Boolean allChannel, Boolean disabled,
                                      Integer rank) {
        return dao.getAdminList(siteId, allChannel, disabled, rank);
    }

    @Override
    @Transactional(readOnly = true)
    public BbsUser findById(Integer id) {
        BbsUser entity = dao.findById(id);
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public BbsUser findByUsername(String username) {
        BbsUser entity = null;
        if ((!StringUtils.isBlank(username))) {
            entity = dao.findByUsername(username);
        }
        return entity;
    }

    @Override
    public BbsUser registerMember(String username, String email, Boolean official, String password,
                                  String ip, Integer groupId, BbsUserExt userExt,
                                  Map<String, String> attr) throws UnsupportedEncodingException,
                                                           MessagingException {
        UnifiedUser unifiedUser = unifiedUserMng.save(username, email, password, ip);
        BbsUser user = new BbsUser();
        user.forMember(unifiedUser);
        user.setAttr(attr);
        BbsUserGroup group = null;
        if (groupId != null) {
            group = bbsUserGroupMng.findById(groupId);
        } else {
            group = bbsUserGroupMng.getRegDef();
        }
        if (group == null) {
            throw new RuntimeException("register default member group not found!");
        }
        Integer defaultActiveLevel = cmsConfigMng.get().getDefaultActiveLevel();
        user.setActiveLevel(bbsUserActiveLevelMng.findById(defaultActiveLevel));
        if (official != null) {
            user.setOfficial(official);
        } else {
            user.setOfficial(false);
        }
        user.setGroup(group);
        user.init();
        user = dao.save(user);
        bbsUserExtMng.save(userExt, user);
        bbsUserOnlineMng.saveByUser(user);
        return user;
    }

    @Override
    public BbsUser registerMember(String username, String email, String password, String ip,
                                  Integer groupId, BbsUserExt userExt, Map<String, String> attr,
                                  Boolean activation, EmailSender sender, MessageTemplate msgTpl)
                                                                                                 throws UnsupportedEncodingException,
                                                                                                 MessagingException {
        UnifiedUser unifiedUser = unifiedUserMng.save(username, email, password, ip, activation,
            sender, msgTpl);
        BbsUser user = new BbsUser();
        user.forMember(unifiedUser);
        user.setAttr(attr);
        BbsUserGroup group = null;
        if (groupId != null) {
            group = bbsUserGroupMng.findById(groupId);
        } else {
            group = bbsUserGroupMng.getRegDef();
        }
        if (group == null) {
            throw new RuntimeException("register default member group not found!");
        }
        Integer defaultActiveLevel = cmsConfigMng.get().getDefaultActiveLevel();
        user.setActiveLevel(bbsUserActiveLevelMng.findById(defaultActiveLevel));
        user.setGroup(group);
        user.init();
        user = dao.save(user);
        bbsUserExtMng.save(userExt, user);
        return user;
    }

    @Override
    public void updateLoginInfo(Integer userId, String ip, Date loginTime, String sessionId) {
        BbsUser user = findById(userId);
        if (user != null) {
            user.setLoginCount(user.getLoginCount() + 1);
            if (StringUtils.isNotBlank(ip)) {
                user.setLastLoginIp(ip);
            }
            if (loginTime != null) {
                user.setLastLoginTime(loginTime);
            }
            user.setSessionId(sessionId);
        }
    }

    @Override
    public void updateUploadSize(Integer userId, Integer size) {
        BbsUser user = findById(userId);
        user.setUploadTotal(user.getUploadTotal() + size);
        if (user.getUploadDate() != null) {
            if (BbsUser.isToday(user.getUploadDate())) {
                size += user.getUploadSize();
            }
        }
        user.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
        user.setUploadSize(size);
    }

    @Override
    public boolean isPasswordValid(Integer id, String password) {
        return unifiedUserMng.isPasswordValid(id, password);
    }

    @Override
    public void updatePwdEmail(Integer id, String password, String email) {
        BbsUser user = findById(id);
        if (!StringUtils.isBlank(email)) {
            user.setEmail(email);
        } else {
            user.setEmail(null);
        }
        unifiedUserMng.update(id, password, email);
    }

    @Override
    public void updateGroup(Integer id, Integer groupId) {
        BbsUser user = findById(id);
        user.setGroup(bbsUserGroupMng.findById(groupId));
    }

    @Override
    public BbsUser saveAdmin(String username, String email, String password, String ip,
                             boolean viewOnly, boolean selfAdmin, int rank, Integer groupId,
                             Integer[] roleIds, Integer[] channelIds, Integer[] siteIds,
                             Byte[] steps, Boolean[] allChannels, BbsUserExt userExt)
                                                                                     throws UnsupportedEncodingException,
                                                                                     MessagingException {
        UnifiedUser unifiedUser = unifiedUserMng.save(username, email, password, ip);
        BbsUser user = new BbsUser();
        user.forAdmin(unifiedUser, viewOnly, selfAdmin, rank);
        BbsUserGroup group = null;
        if (groupId != null) {
            group = bbsUserGroupMng.findById(groupId);
        } else {
            group = bbsUserGroupMng.getRegDef();
        }
        if (group == null) {
            throw new RuntimeException("register default member group not setted!");
        }
        Integer defaultActiveLevel = cmsConfigMng.get().getDefaultActiveLevel();
        user.setActiveLevel(bbsUserActiveLevelMng.findById(defaultActiveLevel));
        user.setGroup(group);
        user.init();
        dao.save(user);
        bbsUserExtMng.save(userExt, user);
        return user;
    }

    @Override
    public BbsUser updateAdmin(BbsUser bean, BbsUserExt ext, String password, Integer groupId,
                               Integer[] roleIds, Integer[] channelIds, Integer[] siteIds,
                               Byte[] steps, Boolean[] allChannels) {
        Updater<BbsUser> updater = new Updater<BbsUser>(bean);
        updater.include("email");
        BbsUser user = dao.updateByUpdater(updater);
        user.setGroup(bbsUserGroupMng.findById(groupId));
        bbsUserExtMng.update(ext, user);
        unifiedUserMng.update(bean.getId(), password, bean.getEmail());
        return user;
    }

    @Override
    public BbsUser updateMember(Integer id, String email, String password, Boolean isDisabled,
                                String signed, String avatar, BbsUserExt ext,
                                Map<String, String> attr, Integer groupId) {
        BbsUser entity = findById(id);
        if (!StringUtils.isBlank(email)) {
            entity.setEmail(email);
        }
        if (isDisabled != null) {
            entity.setDisabled(isDisabled);
        }
        if (signed != null) {
            entity.setSigned(signed);
        }
        if (avatar != null) {
            entity.setAvatar(avatar);
        }
        if (groupId != null) {
            entity.setGroup(bbsUserGroupMng.findById(groupId));
        }
        // 更新属性表
        if (attr != null) {
            Map<String, String> attrOrig = entity.getAttr();
            attrOrig.clear();
            attrOrig.putAll(attr);
        }
        bbsUserExtMng.update(ext, entity);
        unifiedUserMng.update(id, password, email);
        return entity;
    }

    @Override
    public BbsUser updateMember(Integer id, String email, String password, String realname,
                                Boolean gender, String tel) {
        BbsUser entity = findById(id);
        if (!StringUtils.isBlank(email)) {
            entity.setEmail(email);
        }
        BbsUserExt ext = entity.getUserExt();
        ext.setRealname(realname);
        ext.setGender(gender);
        ext.setMoble(tel);
        bbsUserExtMng.update(ext, entity);
        unifiedUserMng.update(id, password, email);
        return entity;
    }

    @Override
    public BbsUser deleteById(Integer id) {
        unifiedUserMng.deleteById(id);
        BbsUser bean = dao.deleteById(id);
        return bean;
    }

    @Override
    public BbsUser[] deleteByIds(Integer[] ids) {
        BbsUser[] beans = new BbsUser[ids.length];
        for (int i = 0, len = ids.length; i < len; i++) {
            beans[i] = deleteById(ids[i]);
        }
        return beans;
    }

    @Override
    public boolean usernameNotExist(String username) {
        return dao.countByUsername(username) <= 0;
    }

    @Override
    public boolean emailNotExist(String email) {
        return dao.countByEmail(email) <= 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BbsUser> getSuggestMember(String username, Integer count) {
        return dao.getSuggestMember(username, count);
    }

    @Override
    public void updateActiveLevel() {
        List<BbsUserActiveLevel> levels = bbsUserActiveLevelMng.getList(Integer.MAX_VALUE);
        List<BbsUser> users = getList(Integer.MAX_VALUE);
        for (BbsUser u : users) {
            BbsUserActiveLevel l = getUpdateToLevel(u, levels);
            dao.updateActiveLevel(u, l);
        }
    }

    private BbsUserActiveLevel getUpdateToLevel(BbsUser user, List<BbsUserActiveLevel> levels) {
        if (levels != null && levels.size() > 0) {
            BbsUserActiveLevel level = levels.get(0);
            for (BbsUserActiveLevel l : levels) {
                BbsUserOnline online = user.getUserOnline();
                if (online != null) {
                    if (online.getOnlineTotal() > l.getRequiredHour()) {
                        level = l;
                        continue;
                    } else {
                        break;
                    }
                }
            }
            return level;
        } else {
            return null;
        }
    }

    @Override
    public void updatePoint(Integer userId, Integer point, Integer prestige, String mid, int num,
                            int operator) {
        BbsUser user = findById(userId);
        if (point != null) {
            user.setPoint(user.getPoint() + point);
        }
        if (prestige != null) {
            user.setPrestige(user.getPrestige() + prestige);
        }
        // operator=-1无须下面操作
        if (StringUtils.isNotBlank(mid) && operator != -1) {
            BbsMemberMagic magic;
            BbsCommonMagic comMagic = bbsCommonMagicMng.findByIdentifier(mid);
            magic = user.getMemberMagic(mid);
            // operator==0出售道具，=1使用道具 =2丢弃道具 =3购买道具=4系统赠送道具
            if (operator == 0) {
                // 用户存在该道具---减少数量
                if (magic != null) {
                    magic.setNum(magic.getNum() - num);
                    // 减少包容量
                    user.setMagicPacketSize(user.getMagicPacketSize() - num * comMagic.getWeight());
                    // 增加系统包数量
                    comMagic.setNum(num + comMagic.getNum());
                    bbsCommonMagicMng.update(comMagic);
                }
            } else if (operator == 1) {
                // 减少数量
                if (magic != null) {
                    magic.setNum(magic.getNum() - num);
                    // 减少包容量
                    user.setMagicPacketSize(user.getMagicPacketSize() - num * comMagic.getWeight());
                }
            } else if (operator == 2) {
                // 减少数量
                if (magic != null) {
                    magic.setNum(magic.getNum() - num);
                    // 减少包容量
                    user.setMagicPacketSize(user.getMagicPacketSize() - num * comMagic.getWeight());
                }
            } else if (operator == 3) {
                // 增加数量
                if (magic != null) {
                    magic.setNum(magic.getNum() + num);
                    // 增加包容量
                    user.setMagicPacketSize(user.getMagicPacketSize() + num * comMagic.getWeight());
                    // 减少系统包数量
                    comMagic.setNum(comMagic.getNum() - num);
                    bbsCommonMagicMng.update(comMagic);
                } else {
                    magic = new BbsMemberMagic();
                    magic.setMagic(comMagic);
                    magic.setNum(num);
                    magic.setUser(user);
                    bbsMemberMagicMng.save(magic);
                    user.addToMemberMagics(magic);
                }
            } else if (operator == 4) {
                // 增加数量
                if (magic != null) {
                    magic.setNum(magic.getNum() + num);
                    // 增加包容量
                    user.setMagicPacketSize(user.getMagicPacketSize() + num * comMagic.getWeight());
                    bbsCommonMagicMng.update(comMagic);
                } else {
                    magic = new BbsMemberMagic();
                    magic.setMagic(comMagic);
                    magic.setNum(num);
                    magic.setUser(user);
                    bbsMemberMagicMng.save(magic);
                    user.addToMemberMagics(magic);
                }
            }

        }
    }

    // private CmsSiteMng cmsSiteMng;
    private BbsUserGroupMng       bbsUserGroupMng;
    private UnifiedUserMng        unifiedUserMng;
    private BbsUserExtMng         bbsUserExtMng;
    private BbsUserDao            dao;
    @Autowired
    private BbsCommonMagicMng     bbsCommonMagicMng;
    @Autowired
    private BbsMemberMagicMng     bbsMemberMagicMng;
    @Autowired
    private BbsUserOnlineMng      bbsUserOnlineMng;
    @Autowired
    private CmsConfigMng          cmsConfigMng;
    @Autowired
    private BbsUserActiveLevelMng bbsUserActiveLevelMng;

    // @Autowired
    // public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
    // this.cmsSiteMng = cmsSiteMng;
    // }

    @Autowired
    public void setUnifiedUserMng(UnifiedUserMng unifiedUserMng) {
        this.unifiedUserMng = unifiedUserMng;
    }

    @Autowired
    public void setBbsUserExtMng(BbsUserExtMng bbsUserExtMng) {
        this.bbsUserExtMng = bbsUserExtMng;
    }

    @Autowired
    public void setDao(BbsUserDao bbsUserDao) {
        this.dao = bbsUserDao;
    }

    @Autowired
    public void setBbsUserGroupMng(BbsUserGroupMng bbsUserGroupMng) {
        this.bbsUserGroupMng = bbsUserGroupMng;
    }
}