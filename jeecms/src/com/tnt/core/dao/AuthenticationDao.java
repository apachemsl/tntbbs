package com.tnt.core.dao;

import java.util.Date;

import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;
import com.tnt.core.entity.Authentication;

public interface AuthenticationDao {

	public int deleteExpire(Date d);

	public Authentication getByUserId(Long userId);

	public Pagination getPage(int pageNo, int pageSize);

	public Authentication findById(String id);

	public Authentication save(Authentication bean);

	public Authentication updateByUpdater(Updater<Authentication> updater);

	public Authentication deleteById(String id);
}