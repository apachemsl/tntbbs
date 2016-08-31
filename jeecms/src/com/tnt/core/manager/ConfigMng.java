package com.tnt.core.manager;

import java.util.Map;

import com.tnt.common.email.EmailSender;
import com.tnt.common.email.MessageTemplate;
import com.tnt.core.entity.Config;
import com.tnt.core.entity.Config.ConfigLogin;

public interface ConfigMng {
	public Map<String, String> getMap();

	public ConfigLogin getConfigLogin();

	public EmailSender getEmailSender();

	public MessageTemplate getForgotPasswordMessageTemplate();
	
	public MessageTemplate getRegisterMessageTemplate();
	
	public MessageTemplate getServiceExpirationMessageTemplate();

	public String getValue(String id);

	public void updateOrSave(Map<String, String> map);

	public Config updateOrSave(String key, String value);

	public Config deleteById(String id);

	public Config[] deleteByIds(String[] ids);
}