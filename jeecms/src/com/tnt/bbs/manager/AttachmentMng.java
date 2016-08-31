package com.tnt.bbs.manager;

import com.tnt.bbs.entity.Attachment;
import com.tnt.common.page.Pagination;

public interface AttachmentMng {
	public Pagination getPage(int pageNo, int pageSize);

	public Attachment findById(Integer id);

	public Attachment save(Attachment bean);

	public Attachment update(Attachment bean);

	public Attachment deleteById(Integer id);
	
	public Attachment[] deleteByIds(Integer[] ids);
}