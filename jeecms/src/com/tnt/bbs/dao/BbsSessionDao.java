package com.tnt.bbs.dao;

import java.util.List;

import net.sf.ehcache.Ehcache;

import com.tnt.bbs.entity.BbsSession;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface BbsSessionDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsSession> getList(Integer count);
	
	public Integer total(boolean member);
	
	public void freshCacheToDB(Ehcache cache);

	public BbsSession findById(Long id);
	
	public BbsSession findBySessionId(String sessionId);

	public BbsSession save(BbsSession bean);

	public BbsSession updateByUpdater(Updater<BbsSession> updater);

	public BbsSession deleteById(Long id);
}