package pl.itcraft.appstract.core.api.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.itcraft.appstract.core.BaseAppModuleConfig;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.dto.ErrorDto;
import pl.itcraft.appstract.core.dto.StringDto;
import pl.itcraft.appstract.core.entities.UploadedFile;
import pl.itcraft.appstract.core.file.UploadedFilesReaderService;
import pl.itcraft.appstract.core.security.DisposableTokenService;

@RestController
@Api(tags = {"files"}, produces = "application/json")
public class DisposableFilesController {
	
	@Autowired
	private UploadedFilesReaderService filesService;
	
	@Autowired
	private DisposableTokenService tokenService;
	
	@Autowired
	private BaseAppModuleConfig appModuleConfig;
	
	@GetMapping("disposable-url/{path:.+}")
	@ApiOperation(value = "Generate disposable URL", notes = "Generate disposable URL for given file path")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Successfully logged in"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "File not found in given path", response = ErrorDto.class)
	})
	public StringDto generateDisposableUrl(@PathVariable("path") String path) {
		UploadedFile uf = filesService.findByPath(path);
		if (uf == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		filesService.checkPermission(uf);
		String signedToken = tokenService.createToken( uf.getId().toString() );
		return new StringDto( appModuleConfig.getCurrentApiUrl() + "/disposable-file/" + signedToken );
	}
}
