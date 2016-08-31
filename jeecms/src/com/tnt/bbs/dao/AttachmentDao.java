package com.tnt.bbs.dao;

import com.tnt.bbs.entity.Attachment;
import com.tnt.common.hibernate3.Updater;
import com.tnt.common.page.Pagination;

public interface AttachmentDao {
	public Pagination getPage(int pageNo, int pageSize);

	public Attachment findById(Integer id);

	public Attachment save(Attachment bean);

	public Attachment updateByUpdater(Updater<Attachment> updater);

	public Attachment deleteById(Integer id);
}