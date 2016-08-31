package com.tnt.bbs.entity;

import com.tnt.bbs.entity.base.BaseBbsTopicMsg;



public class BbsTopicMsg extends BaseBbsTopicMsg {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsTopicMsg () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopicMsg (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopicMsg (
		java.lang.Integer id,
		com.tnt.bbs.entity.BbsTopic topic,
		java.lang.Boolean status) {

		super (
			id,
			topic,
			status);
	}

/*[CONSTRUCTOR MARKER END]*/


}