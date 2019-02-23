package pl.itcraft.appstract.core.utils;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public final class ApplicationContextUtils {
	
	private static WebApplicationContext webContext;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@PostConstruct
	public void init(){
		ApplicationContextUtils.webContext = webApplicationContext;
	}
	
    public static ApplicationContext getApplicationContext() {
        return WebApplicationContextUtils.getWebApplicationContext(webContext.getServletContext());
    }
    
    public static UtilsBean getUtilsBean() {
    	return getApplicationContext().getBean(UtilsBean.class);
    }
    
    public static <T> T getBean(Class<T> requiredType) {
    	return getApplicationContext().getBean(requiredType);
    }
    
    public static Locale currentLocale() {
    	return getUtilsBean().getCurrentLocale();
    }
}
