package pl.itcraft.appstract.admin.qualia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = {"qualia"}, produces = "application/json")
@PreAuthorize("hasAnyRole('ADMIN')")
public class QualiaController {
	
	@Autowired
	private QualiaService service;

	@PostMapping("qualia/import")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Import data from qualia", notes = "Import new data from qualia")
	public void importData() {
		service.importData();
	}
	
	// TODO remove, only for testing
	@Deprecated
	@PostMapping("qualia/accept/{orderId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Only for testing, remove later", notes = "Only for testing, remove later")
	public void acceptData(@PathVariable("orderId") long orderId) {
		service.acceptOrderTest(orderId);
	}
	
}
