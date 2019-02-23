package pl.itcraft.appstract.core.api.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.itcraft.appstract.core.dto.ErrorDto;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class ErrorControler {

	@RequestMapping(value = "error")
	@ResponseBody
	public Object handleError(HttpServletRequest request, HttpServletResponse response) {
		Object appErrorDto = request.getAttribute(ErrorDto.ERROR_REQUEST_ATTR);
		int code = response.getStatus();
		
		if (appErrorDto != null) {
			// Blad wygenerowany w restowym kontrolerze.
			// Zwracamy go bez zmian.
			return appErrorDto;
		} else {
			// Blad wygenerowany przez Tomcata lub Springa poza metoda kontrolera.
			// Nie wiemy czy request byl skierowany do kontrolera restowego czy innego.
			return String.valueOf(code);
		}
	}
	
}
