package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsMagicConfig;

public interface BbsMagicConfigMng {

	public BbsMagicConfig findById(Integer id);

	public BbsMagicConfig update(BbsMagicConfig bean);

}