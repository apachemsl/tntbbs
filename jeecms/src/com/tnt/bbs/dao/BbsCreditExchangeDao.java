package com.tnt.bbs.dao;

import com.tnt.bbs.entity.BbsCreditExchange;
import com.tnt.common.hibernate3.Updater;

public interface BbsCreditExchangeDao {
	
	public BbsCreditExchange findById(Integer id);

	public BbsCreditExchange save(BbsCreditExchange bean);

	public BbsCreditExchange updateByUpdater(Updater<BbsCreditExchange> updater);

	public BbsCreditExchange deleteById(Integer id);
}