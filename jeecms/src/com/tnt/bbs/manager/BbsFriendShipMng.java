package com.tnt.bbs.manager;


import java.util.List;

import com.tnt.bbs.entity.BbsFriendShip;
import com.tnt.bbs.entity.BbsUser;
import com.tnt.common.page.Pagination;


public interface BbsFriendShipMng {
	public BbsFriendShip findById(Integer id);

	public BbsFriendShip save(BbsFriendShip bean);

	public BbsFriendShip update(BbsFriendShip bean);

	public BbsFriendShip deleteById(Integer id);

	public BbsFriendShip[] deleteByIds(Integer[] ids);
	
	public BbsFriendShip getFriendShip(Integer userId, Integer friendId);

	public void apply(BbsUser user, BbsUser friend);
	
	public void accept(BbsFriendShip friendShip);
	
	public void refuse(BbsFriendShip friendShip);
	
	public Pagination getPageByUserId(Integer userId, Integer pageNo, Integer pageSize);
	
	public List<BbsFriendShip> getListByUserId(Integer userId);
	
	public Pagination getApplyByUserId(Integer userId, Integer pageNo, Integer pageSize);
	
	public List<BbsFriendShip> getList(Integer userId);
}