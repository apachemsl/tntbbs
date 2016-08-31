package com.tnt.bbs.manager;

import com.tnt.bbs.entity.BbsUser;
import com.tnt.bbs.entity.BbsUserExt;

public interface BbsUserExtMng {
	public BbsUserExt save(BbsUserExt ext, BbsUser user);

	public BbsUserExt update(BbsUserExt ext, BbsUser user);
}