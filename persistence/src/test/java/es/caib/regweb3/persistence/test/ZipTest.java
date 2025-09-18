package es.caib.regweb3.persistence.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;
import org.plugin.lema.api.Contenido;
import org.plugin.lema.api.DocumentoAnexo;

import es.caib.regweb3.utils.MimeTypeUtils;
import es.caib.regweb3.utils.RegwebConstantes;

public class ZipTest {
	

	@Test
	public void extraerDocumentos() throws IOException {
		Contenido contenido = new Contenido();
		String zipBase64 = "UEsDBB...";
		contenido.setBase64(zipBase64);
		
		List<DocumentoAnexo> documentos = extraerDocumentosZip(contenido, "application/zip");
		
		Assert.assertNotNull(documentos);
		
		for (DocumentoAnexo documentoAnexo : documentos) {
			System.out.println(documentoAnexo.getNombre());
		}
		
	}
    private List<DocumentoAnexo> extraerDocumentosZip(Contenido contenido, String mimeType) throws IOException {
List<DocumentoAnexo> documentos = new ArrayList<DocumentoAnexo>();
    	
    	if (mimeType != null && 
                (mimeType.equalsIgnoreCase("application/zip") ||
                 mimeType.equalsIgnoreCase("application/x-zip-compressed") ||
                 mimeType.equalsIgnoreCase("multipart/x-zip"))) {
    		
    		byte[] contenidoBytes = Base64.decodeBase64(contenido.getBase64());

			File tempZip = null;
			try {
				// Guardar el zip en un archivo temporal
				tempZip = File.createTempFile("upload", ".zip");
				try (FileOutputStream fos = new FileOutputStream(tempZip)) {
					fos.write(contenidoBytes);
				}

				// Intentar con distintos charsets
				String[] charsets = new String[]{"UTF-8", "Cp437", "ISO-8859-1", "Windows-1252"};
				boolean success = false;

				for (String cs : charsets) {
					ZipFile zip = null;
	                try {
	                    zip = new ZipFile(tempZip, cs);
	                    Enumeration<ZipArchiveEntry> entries = zip.getEntries();

	                    while (entries.hasMoreElements()) {
	                        ZipArchiveEntry entry = entries.nextElement();
	                        if (!entry.isDirectory()) {
	                            InputStream is = zip.getInputStream(entry);
	                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	                            byte[] buffer = new byte[4096];
	                            int len;
	                            while ((len = is.read(buffer)) > 0) {
	                                baos.write(buffer, 0, len);
	                            }
	                            is.close();
	                            baos.close();

	                            // Crear DocumentoAnexo hijo
	                            String base64Extraido = Base64.encodeBase64String(baos.toByteArray());
	                            Contenido contenidoExtraido = new Contenido();
	                            contenidoExtraido.setBase64(base64Extraido);

	                            String mimeTypeZip = MimeTypeUtils.getExtensionFileName(entry.getName());

	                            byte[] rawName = entry.getRawName();
		                        String nombre;
		                        if (entry.getGeneralPurposeBit().usesUTF8ForNames()) {
		                            nombre = new String(rawName, "UTF-8");
		                        } else {
		                            nombre = new String(rawName, "Cp437");
		                        }

		                        // Assegurar que no contengui caracters no permesos
		                        nombre = revisarCaractersArxiu(nombre);
		                        
	                            DocumentoAnexo hijo = new DocumentoAnexo();
	                            hijo.setNombre(nombre);
	                            hijo.setContenido(contenidoExtraido);
	                            hijo.setMimeType(mimeTypeZip);

	                            documentos.add(hijo);
	                        }
	                    }

	                    zip.close();
	                    success = true;
	                    break;
	                } catch (IOException e) {
	                    if (zip != null) try { zip.close(); } catch (IOException ignored) {}
	                }
				}

				if (!success) {
					throw new IOException("No se pudo leer el ZIP con ningún charset válido.");
				}

			} catch (IOException e) {
				throw e;
			} finally {
				if (tempZip != null && tempZip.exists()) {
					tempZip.delete();
				}
			}
    	}
    	
		return documentos;
	}
    
	private String revisarCaractersArxiu(String nombre) {
		StringBuilder sb = new StringBuilder(nombre.length());

		outer:
		for (char c : nombre.toCharArray()) {
			for (char noPermitido : RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU) {
				if (c == noPermitido) {
					sb.append('_');
					continue outer;
				}
			}
			sb.append(c);
		}
		
		return sb.toString();
	}
    
}
