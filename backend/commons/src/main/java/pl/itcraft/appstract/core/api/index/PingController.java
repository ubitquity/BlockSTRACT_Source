package pl.itcraft.appstract.core.api.index;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.itcraft.appstract.core.utils.AppDateFormat;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class PingController {
	
	@PersistenceContext
	private EntityManager em;
	
	@GetMapping(value = "ping", produces = "text/plain")
	public String ping() {
		Object now = em.createNativeQuery("SELECT NOW()").getSingleResult();
		String datetime = AppDateFormat.createDateTime().formatDate((Date) now);
		return "OK "+datetime;
	}
}
