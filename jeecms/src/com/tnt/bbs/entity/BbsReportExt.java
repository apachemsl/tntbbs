package com.tnt.bbs.entity;

import com.tnt.bbs.entity.base.BaseBbsReportExt;



public class BbsReportExt extends BaseBbsReportExt {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsReportExt () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsReportExt (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsReportExt (
		java.lang.Integer id,
		com.tnt.bbs.entity.BbsUser reportUser,
		com.tnt.bbs.entity.BbsReport report,
		java.util.Date reportTime) {

		super (
			id,
			reportUser,
			report,
			reportTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}