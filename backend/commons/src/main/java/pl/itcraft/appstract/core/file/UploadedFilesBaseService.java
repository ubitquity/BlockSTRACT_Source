package pl.itcraft.appstract.core.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import pl.itcraft.appstract.core.CoreConstants;

public abstract class UploadedFilesBaseService {
	
	@Value("${app.upload_dir}")
	protected String uploadDir;
	
	protected final String DIR_TO_FILE_DELIMITER = "_";

	@PostConstruct
	protected void postConstruct() {
		if (!uploadDir.endsWith(CoreConstants.DS)) {
			uploadDir = uploadDir + CoreConstants.DS;
		}
		Path fullPath = Paths.get(uploadDir);
		if (!Files.exists(fullPath)) {
			throw new RuntimeException("Folder " + uploadDir + " nie istnieje.");
		}
		if (!Files.isDirectory(fullPath)) {
			throw new RuntimeException("Plik " + uploadDir + " nie jest folderem.");
		}
		if (!Files.isWritable(fullPath)) {
			throw new RuntimeException("Brak uprawnień do zapisu w folderze " + uploadDir);
		}
	}
	
	protected String relativePathToUrl(String path) {
		return path.replace(CoreConstants.DS, DIR_TO_FILE_DELIMITER);
	}
	
	protected String relativeUrlToPath(String url) {
		return url.replace(DIR_TO_FILE_DELIMITER, CoreConstants.DS);
	}
}
