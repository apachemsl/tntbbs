package com.tnt.bbs.entity;

import com.tnt.bbs.entity.base.BaseCmsFriendlinkCtg;



public class CmsFriendlinkCtg extends BaseCmsFriendlinkCtg {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsFriendlinkCtg () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsFriendlinkCtg (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsFriendlinkCtg (
		java.lang.Integer id,
		com.tnt.core.entity.CmsSite site,
		java.lang.String name,
		java.lang.Integer priority) {

		super (
			id,
			site,
			name,
			priority);
	}

/*[CONSTRUCTOR MARKER END]*/


}