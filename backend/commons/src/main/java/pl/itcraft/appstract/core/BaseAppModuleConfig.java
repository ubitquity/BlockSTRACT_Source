package pl.itcraft.appstract.core;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseAppModuleConfig {
	
	private AppModule module;
	
	public BaseAppModuleConfig(AppModule module) {
		this.module = module;
	}
	
	@Autowired
	@Qualifier("appProperties")
	private Properties appProperties;
	
	private String currentApiUrl;
	
	private String currentFrontUrl;
	
	private int authTokenTtlMinutes;
	
	private int maxLoginFailures;
	
	@PostConstruct
	protected void init() {
		String appConfPropertyPrefix = "app.conf." + module.name();
		
		currentApiUrl = appProperties.getProperty(appConfPropertyPrefix + ".api_url");
		currentFrontUrl = appProperties.getProperty(appConfPropertyPrefix + ".front_url");
		maxLoginFailures = Integer.valueOf( appProperties.getProperty(appConfPropertyPrefix + ".max_login_failures", "0") );
		
		String authTokenTtl = appProperties.getProperty(appConfPropertyPrefix + ".auth_token_ttl");
		Matcher matcher = Pattern.compile("([1-9][0-9]*)([mhdMHD])?").matcher( authTokenTtl );
		if (matcher.find()) {
			int ttlValue = Integer.valueOf( matcher.group(1) );
			String ttlUnit = matcher.group(2);
			if (ttlUnit != null) {
				switch (ttlUnit.toLowerCase()) {
					case "m": authTokenTtlMinutes = ttlValue * 1;       break;
					case "h": authTokenTtlMinutes = ttlValue * 60;      break;
					case "d": authTokenTtlMinutes = ttlValue * 60 * 24; break;
				}
			} else {
				authTokenTtlMinutes = ttlValue;
			}
		} else {
			throw new RuntimeException("Invalid {" + appConfPropertyPrefix + ".auth_token_ttl} value [" + authTokenTtl + "]");
		}
	}
	
	public String getCurrentApiUrl() {
		return currentApiUrl;
	}
	
	public String getCurrentFrontUrl() {
		return currentFrontUrl;
	}
	
	public int getAuthTokenTtlMinutes() {
		return authTokenTtlMinutes;
	}
	
	public int getMaxLoginFailures() {
		return maxLoginFailures;
	}

	public String getApiUrl(AppModule otherModule) {
		return appProperties.getProperty("app.conf." + otherModule.name() + ".api_url");
	}
	
	public String getFrontUrl(AppModule otherModule) {
		return appProperties.getProperty("app.conf." + otherModule.name() + ".front_url");
	}
	
	public boolean migrateDbOnStartup() {
		return Boolean.valueOf( appProperties.getProperty("app.conf."+module.name()+".run_flyway") );
	}
	
	public AppModule getModule() {
		return module;
	}

	public String getAbsoluteFileUrl(String uploadedFilePath) {
		if (uploadedFilePath == null) {
			return null;
		}
		if (uploadedFilePath.startsWith(CoreConstants.DUMMY_IMAGES_PREFIX)) {
			return currentApiUrl + "/" + uploadedFilePath; // Bezposrednio - bez redirecta w FilesController
		}
		return currentApiUrl + "/file/" + uploadedFilePath;
	}
	
}
