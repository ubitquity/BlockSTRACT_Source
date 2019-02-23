package pl.itcraft.appstract.core.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.PropertySource;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import pl.itcraft.appstract.core.api.error.RC;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * TODO bug w springfoxie czeka na poprawe
 * https://github.com/springfox/springfox/issues/2205
 * Po poprawieniu bedzie mozna przeniesc wszystko z springfox.properties do app.properties
 * i usunac adnotacje EnableSwagger2 i PropertySource
 */
@EnableSwagger2
@PropertySource("classpath:spring/springfox.properties")
public class SwaggerDocketFactory extends AbstractFactoryBean<Docket> {

	private String description;
	private String version;
	private List<Predicate<RequestHandler>> packages;
	
	@Value("${app.name}")
	private String appName;
	
	public SwaggerDocketFactory(String description, String version, List<String> packages) {
		super();
		this.description = description + "\n\n" + RC.codesDescription;
		this.version = version;
		this.packages = new ArrayList<>();
		if (packages != null) {
			for (String p : packages) {
				this.packages.add( RequestHandlerSelectors.basePackage(p) );
			}
		}
	}

	private Docket createDocket() {
		ApiInfo apiInfo = new ApiInfoBuilder()
			.title(appName)
			.description(description)
			.version(version)
			.build();
		
		Set<String> contentTypes = new HashSet<>();
		contentTypes.add("application/json");
		
		ApiSelectorBuilder docket = new Docket(DocumentationType.SWAGGER_2)
			.enableUrlTemplating(true)
			.useDefaultResponseMessages(false)
			.produces(contentTypes)
			.consumes(contentTypes)
			.forCodeGeneration(true)
			.apiInfo(apiInfo)
			.select();
			
		docket.paths(PathSelectors.any());
		if (packages.isEmpty()) {
			docket.apis( RequestHandlerSelectors.any() );
		} else {
			docket.apis( Predicates.or(packages) );
		}
		return docket.build();
	}

	@Override
	public Class<?> getObjectType() {
		return Docket.class;
	}

	@Override
	protected Docket createInstance() throws Exception {
		return createDocket();
	}

}
