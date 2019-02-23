package pl.itcraft.appstract.core.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pl.itcraft.appstract.core.CoreConstants;
import pl.itcraft.appstract.core.entities.UploadedFile;

@Service
public class UploadedFilesWriterService extends UploadedFilesBaseService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UploadedFilesWriterService.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public int cleanupFiles() {
		String q = "SELECT f.path FROM UploadedFile f"
			+ " WHERE f.toDelete = TRUE OR (f.temporary = :tmp AND f.createdAt < :date)";
		List<String> paths = em.createQuery(q, String.class)
			.setParameter("tmp", Boolean.TRUE)
			.setParameter("date", new DateTime().minusMinutes(CoreConstants.MAX_MINUTES_FOR_TEMP_FILES).toDate())
			.getResultList();
		
		for (String path : paths) {
			String absPath = uploadDir + relativeUrlToPath(path);
			try {
				Files.delete(Paths.get(absPath));
				em.createQuery("DELETE FROM UploadedFile f WHERE f.path = :p")
				.setParameter("p", path)
				.executeUpdate();
			}
			catch (Exception e) {
				LOG.error("Deleting file failed: " + absPath, e);
			}
		}
		return paths.size();
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public UploadedFile[] uploadAllFiles(UploadedFileDescriptor[] fileDescriptors) {
		UploadedFile[] saved = new UploadedFile[fileDescriptors.length];
		int i = 0;
		try {
			for (i=0; i < fileDescriptors.length; i++) {
				saved[i] = uploadFile( fileDescriptors[i] );
			}
		} catch (Exception e) {
			LOG.error("Problem z zapisaniem pliku #{}. {}('{}')", i, e.getClass(), e.getMessage());
			for (i=0; i < saved.length; i++) {
				if (saved[i] != null) {
					unlinkFile( saved[i].getPath() ); // Usuwamy poprzednie
				}
			}
			throw e;
		}
		return saved;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public UploadedFile uploadFile(UploadedFileDescriptor fileDescriptor) {
		String sourceFileName = fileDescriptor.getSourceName();
		if (!StringUtils.hasText(sourceFileName)) {
			throw new RuntimeException("Source file name is required. Use 'unknown' at least.");
		}
		
		String mimeType = fileDescriptor.getMimeType();
		if (!StringUtils.hasText(sourceFileName)) {
			throw new RuntimeException("Mime type is required.");
		}
		
		byte[] content = fileDescriptor.getBytes();
		if (content == null) {
			throw new RuntimeException("File descriptor has no content.");
		}
		
		boolean temporary = fileDescriptor.isTemporary();
		boolean secured = fileDescriptor.isSecured();
		String entityType = fileDescriptor.getEntityType();
		Long entityId = fileDescriptor.getEntityId();
		
		return saveAndRegisterFile(sourceFileName, mimeType, content, temporary, secured, entityType, entityId);
	}
	
	private UploadedFile saveAndRegisterFile(String sourceFileName, String mimeType, byte[] content, boolean temporary, boolean secured, String entityType, Long entityId) {
		String path = createFilePath();
		try {
			writeContentToFile(path, content);
		} catch (Exception e) {
			String msg = "Saving file failed. Mime: '"+mimeType+"'. Source: '" + sourceFileName + "'. Target: '" + mimeType + "'. Size: "+content.length;
			LOG.error(msg, e);
			throw new RuntimeException(msg, e);
		}
		UploadedFile file = new UploadedFile(sourceFileName, path, mimeType, temporary, secured, entityType, entityId, content);
		em.persist(file);
		return file;
	}
	
	private void writeContentToFile(String path, byte[] content) throws IOException {
		String absPath = uploadDir + relativeUrlToPath(path);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(absPath)));
		stream.write(content);
		stream.close();
	}
	
	private void unlinkFile(String path) {
		String absPath = uploadDir + relativeUrlToPath(path);
		try {
			Files.delete(Paths.get(absPath));
		} catch (Exception e) {
			LOG.error("Deleting file failed: " + absPath, e);
		}
	}
	
	private String createFilePath() {
		String dirName = RandomStringUtils.random(2, "0123456789abcdef"); // 00 - ff
		new File(uploadDir + dirName).mkdirs(); // Makes dir if not exists
		return dirName + DIR_TO_FILE_DELIMITER + DigestUtils.sha256Hex( RandomStringUtils.random(256, true, true) );
	}
	
}
