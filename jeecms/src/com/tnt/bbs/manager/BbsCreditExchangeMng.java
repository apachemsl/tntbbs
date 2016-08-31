package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsCreditExchange;

public interface BbsCreditExchangeMng {
	public BbsCreditExchange findById(Integer id);

	public BbsCreditExchange update(BbsCreditExchange bean);
}