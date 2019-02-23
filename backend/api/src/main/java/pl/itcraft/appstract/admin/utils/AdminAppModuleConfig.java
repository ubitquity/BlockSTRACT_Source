package pl.itcraft.appstract.admin.utils;

import org.springframework.stereotype.Component;

import pl.itcraft.appstract.core.AppModule;
import pl.itcraft.appstract.core.BaseAppModuleConfig;

@Component
public class AdminAppModuleConfig extends BaseAppModuleConfig {

	public AdminAppModuleConfig() {
		super(AppModule.admin);
	}
	
}
