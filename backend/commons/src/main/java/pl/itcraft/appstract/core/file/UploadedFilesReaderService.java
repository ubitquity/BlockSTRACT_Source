package pl.itcraft.appstract.core.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.UploadedFile;

@Service
public class UploadedFilesReaderService extends UploadedFilesBaseService {

	private static final Logger logger = LoggerFactory.getLogger(UploadedFilesReaderService.class);

	@PersistenceContext
	private EntityManager em;
	
	@PreAuthorize("permitAll")
	public void printDisposableFileToResponse(UploadedFile uploadedFile, HttpServletResponse response) {
		printFileToResponseImpl(uploadedFile, response);
	}
	
	@PreAuthorize("hasPermission(#uploadedFile, 'UPLOADED_FILE_ACCESS')")
	public void checkPermission(UploadedFile uploadedFile) {
		return;
	}

	@PreAuthorize("hasPermission(#uploadedFile, 'UPLOADED_FILE_ACCESS')")
	public void printFileToResponse(UploadedFile uploadedFile, HttpServletResponse response) {
		printFileToResponseImpl(uploadedFile, response);
	}
	
	private void printFileToResponseImpl(UploadedFile uploadedFile, HttpServletResponse response) {
		File file = new File( uploadDir + relativeUrlToPath(uploadedFile.getPath()) );
		if (file.exists()) {
			response.setContentType(uploadedFile.getMimeType());
			response.setHeader("Cache-Control", "max-age=31536000"); // According to RFC: SHOULD NOT be > 1 year
			try {
				FileUtils.copyFile(file, response.getOutputStream());
			} catch (IOException e) {
				logger.error("Problem while serving file "+uploadedFile.getPath(), e);
				throw new ApiException(RC.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.warn("File registered bu not found on disk '{}'", uploadedFile.getPath());
			throw new ApiException(RC.INTERNAL_SERVER_ERROR);
		}
	}

	public UploadedFile findById(Long id) {
		List<UploadedFile> list = em.createQuery("SELECT f FROM UploadedFile f WHERE f.id = :id AND f.toDelete = false", UploadedFile.class)
			.setParameter("id", id)
			.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}
	
	public UploadedFile findByPath(String path) {
		List<UploadedFile> list = em.createQuery(
			"SELECT f FROM UploadedFile f WHERE f.path = :path AND f.toDelete = false", UploadedFile.class)
			.setParameter("path", path)
			.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}
	
	public String getUploadDir() {
		return uploadDir;
	}
	
}
