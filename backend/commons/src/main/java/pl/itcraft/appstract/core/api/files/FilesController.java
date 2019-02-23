package pl.itcraft.appstract.core.api.files;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.UploadedFile;
import pl.itcraft.appstract.core.file.UploadedFilesReaderService;
import pl.itcraft.appstract.core.security.DisposableTokenService;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class FilesController {
	
	@Autowired
	private UploadedFilesReaderService filesService;
	
	@Autowired
	private DisposableTokenService tokenService;
	
	@GetMapping("disposable-file/{token}")
	public void getFileByDisposableToken(@PathVariable("token") String signedToken, HttpServletResponse response) throws IOException {
		
		String fileId = tokenService.decodeToken(signedToken);
		if (fileId == null) {
			throw new ApiException(RC.FORBIDDEN);
		}
		UploadedFile uf = filesService.findById( Long.parseLong(fileId) );
		if (uf == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		filesService.printDisposableFileToResponse(uf, response);
	}
	
	@GetMapping("file/{path:.+}")
	public void getSecuredFile(
		@PathVariable("path") String path,
		@RequestParam(value = "download", required = false, defaultValue="false")
		@ApiParam(required = false, value = "Send Content-Disposition header for download") boolean download,
		HttpServletResponse response
	) {
		getFile(path, download, true, response);
	}
	
	@GetMapping("public-file/{path:.+}")
	public void getPublicFile(
		@PathVariable("path") String path,
		@RequestParam(value = "download", required = false, defaultValue="false")
		@ApiParam(required = false, value = "Send Content-Disposition header for download") boolean download,
		HttpServletResponse response
	) {
		getFile(path, download, false, response);
	}
	
	private void getFile(String path, boolean download, boolean secured, HttpServletResponse response) {
		UploadedFile uf = filesService.findByPath(path);
		if (uf == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		if (!secured && uf.isSecured()) {
			throw new ApiException(RC.FORBIDDEN);
		}
		if (download) {
			response.addHeader("Content-Disposition", "attachment; filename=\"" + uf.getSourceFileName() + "\"");
		}
		filesService.printFileToResponse(uf, response);
	}
	
}
