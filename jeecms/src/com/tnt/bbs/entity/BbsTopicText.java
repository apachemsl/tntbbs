package com.tnt.bbs.entity;

import com.tnt.bbs.entity.base.BaseBbsTopicText;



public class BbsTopicText extends BaseBbsTopicText {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsTopicText () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopicText (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopicText (
		java.lang.Integer id,
		java.lang.String title) {

		super (
			id,
			title);
	}

/*[CONSTRUCTOR MARKER END]*/


}