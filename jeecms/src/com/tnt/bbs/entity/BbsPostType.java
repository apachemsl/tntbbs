package com.tnt.bbs.entity;

import com.tnt.bbs.entity.base.BaseBbsPostType;



public class BbsPostType extends BaseBbsPostType {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsPostType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsPostType (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsPostType (
		java.lang.Integer id,
		com.tnt.core.entity.CmsSite site,
		com.tnt.bbs.entity.BbsForum forum) {

		super (
			id,
			site,
			forum);
	}

/*[CONSTRUCTOR MARKER END]*/


}