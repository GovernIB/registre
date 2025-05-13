package es.caib.regweb3.persistence.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Component
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class DehuDocumentManager {

	protected final Logger log = Logger.getLogger(getClass());
	
	// Método para guardar un documento
	public boolean saveDocument(String identificadorCarpeta, String fileName, byte[] content) throws IOException {
		Path dirPath = Paths.get(getBaseDirectory(), identificadorCarpeta);
		try {
			Files.createDirectories(dirPath);
			fileName = fileName.replace("/", "").replace("=", "");
			Path filePath = dirPath.resolve(fileName);
			// Guarda el contenido del archivo
			try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
				out.write(content);
			}
			return true;
		} catch (IOException e) {
			log.error("Error al guardar el documento: " + e.getMessage());
			throw e;
		}
	}

	// Método para recuperar la lista de documentos de un identificador
	public List<File> getDocuments(String identificadorCarpeta) {
		Path dirPath = Paths.get(getBaseDirectory(), identificadorCarpeta);
		List<File> files = new ArrayList<>();
		if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
			File[] fileList = dirPath.toFile().listFiles();
			if (fileList != null) {
				for (File file : fileList) {
					if (file.isFile()) {
						files.add(file);
					}
				}
			}
		}
		return files;
	}

	// Método para borrar todos los documentos de un identificador
	public boolean deleteDocuments(String identificadorCarpeta) throws IOException {
		Path dirPath = Paths.get(getBaseDirectory(), identificadorCarpeta);
		try {
			if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
				File[] fileList = dirPath.toFile().listFiles();
				if (fileList != null) {
					for (File file : fileList) {
						Files.deleteIfExists(file.toPath());
					}
				}
				Files.deleteIfExists(dirPath);
			}
			return true;
		} catch (IOException e) {
			log.error("Error al borrar documentos: " + e.getMessage());
			throw e;
		}
	}
	
	public boolean deleteDocument(String identifier, String fileName) throws IOException {
        Path filePath = Paths.get(getBaseDirectory(), identifier, fileName);
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
        	log.error("Error al borrar el documento: " + e.getMessage());
            throw e;
        }
    }
	
    // Método para obtener un documento específico por identifier y fileName
    public File getDocument(String identifier, String fileName) {
        Path filePath = Paths.get(getBaseDirectory(), identifier, fileName);
        File file = filePath.toFile();
        if (file.exists() && file.isFile()) {
            return file;
        } else {
            return null;
        }
    }
	
    public boolean documentExists(String identificadorCarpeta, String fileName) {
    	fileName = fileName.replace("/", "").replace("=", "");
        Path filePath = Paths.get(getBaseDirectory(), identificadorCarpeta, fileName);
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }
	
	private String getBaseDirectory() {
		String baseDirectory = PropiedadGlobalUtil.getDehuDocumentsPath();
		return baseDirectory != null ? baseDirectory : "/opt/lema-proxy/files";
	}
}
