package com.tnt.bbs.entity;

import com.tnt.bbs.entity.base.BaseBbsMagicConfig;



public class BbsMagicConfig extends BaseBbsMagicConfig {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMagicConfig () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMagicConfig (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMagicConfig (
		java.lang.Integer id,
		boolean magicSwitch) {

		super (
			id,
			magicSwitch);
	}

/*[CONSTRUCTOR MARKER END]*/


}