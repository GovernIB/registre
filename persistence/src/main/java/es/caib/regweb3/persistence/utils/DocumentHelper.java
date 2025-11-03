package es.caib.regweb3.persistence.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DocumentHelper {

	public static byte[] generarPdf(String nombrePlantilla, Map<String, Object> datos) throws Exception {
		try {
			InputStream is = obtenerPlantilla(nombrePlantilla);
			return XDocReportGenerador.generarPdfDesdeDocx(is, datos);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static ContenidoEmail generarHtml(String nombrePlantilla, Map<String, Object> datos) throws Exception {
		try {
			InputStream is = obtenerPlantilla(nombrePlantilla);
			String html = XDocReportGenerador.generarHtmlDesdeDocx(is, datos);
		
			return embederImagenesEnHtml(html, obtenerPlantilla(nombrePlantilla));
		} catch (Exception e) {
			throw e;
		}
	}
	
//	private static Map<String, Object> configurarParametros(RegistroEntrada registroEntrada, Long codigoSia) throws FileNotFoundException {
//		Map<String, Object> datos = new HashMap<String, Object>();
//		List<String> nombres = new ArrayList<String>();
//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		String fechaRegistro = formatter.format(registroEntrada.getFecha());
//    	String url = PropiedadGlobalUtil.getUrlBaseSede() + codigoSia;
//    	
//    	for (Interesado	interesado: registroEntrada.getRegistroDetalle().getInteresados()) {
//    		nombres.add(interesado.getNombreCompleto());
//		}
//    	
//		datos.put("interesados", StringUtils.join(nombres, ", "));
//		datos.put("fecha", fechaRegistro);
//		datos.put("enlace", url);
//		
//		return datos;
//	}
	
//	private static String embederImagenesEnHtml(String html, InputStream docx) throws IOException {
//	    // Directorio temporal de imágenes (como las que se extraen de DOCX)
//	    Path mediaFolder = Files.createTempDirectory("docx-media");
//
//	    // Extraer las imágenes desde el DOCX
//	    try (ZipInputStream zis = new ZipInputStream(docx)) {
//	        ZipEntry entry;
//	        while ((entry = zis.getNextEntry()) != null) {
//	            if (entry.getName().startsWith("word/media/")) {
//	                Path imagePath = mediaFolder.resolve(entry.getName().replace("word/media/", ""));
//	                Files.copy(zis, imagePath, StandardCopyOption.REPLACE_EXISTING);
//	            }
//	        }
//	    }
//
//	    // Buscar y reemplazar cada imagen en el HTML
//	    Pattern imgPattern = Pattern.compile("<img[^>]+src=[\"']word/media/([^\"']+)[\"'][^>]*>");
//	    Matcher matcher = imgPattern.matcher(html);
//	    StringBuffer sb = new StringBuffer();
//	    while (matcher.find()) {
//	        String imgName = matcher.group(1);
//	        Path imgPath = mediaFolder.resolve(imgName);
//	        String base64 = Base64.encodeBytes(Files.readAllBytes(imgPath));
//	        String mimeType = Files.probeContentType(imgPath); // ej: "image/png"
//	        String imgTag = "<img src=\"data:" + mimeType + ";base64," + base64 + "\" />";
//	        matcher.appendReplacement(sb, Matcher.quoteReplacement(imgTag));
//	    }
//	    matcher.appendTail(sb);
//	    return sb.toString();
//	}

	public static ContenidoEmail embederImagenesEnHtml(String html, InputStream docx) throws IOException {
        Path mediaFolder = Files.createTempDirectory("docx-media");
        Map<String, Path> imagenes = new HashMap<>();

        // Extraer las imágenes del DOCX
        try (ZipInputStream zis = new ZipInputStream(docx)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().startsWith("word/media/")) {
                    Path imagePath = mediaFolder.resolve(entry.getName().replace("word/media/", ""));
                    Files.copy(zis, imagePath, StandardCopyOption.REPLACE_EXISTING);
                    imagenes.put(imagePath.getFileName().toString(), imagePath);
                }
            }
        }

        // Reemplazar <img src="word/media/..."> por <img src="cid:...">
        Pattern imgPattern = Pattern.compile("<img[^>]+src=[\"']word/media/([^\"']+)[\"'][^>]*>");
        Matcher matcher = imgPattern.matcher(html);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String imgName = matcher.group(1);
            String cid = imgName.replaceAll("[^a-zA-Z0-9]", ""); // cid válido
            String imgTag = "<img src=\"cid:" + cid + "\" />";
            matcher.appendReplacement(sb, Matcher.quoteReplacement(imgTag));
        }
        matcher.appendTail(sb);

        return new ContenidoEmail(sb.toString(), imagenes);
    }

    /**
     * Genera el contenido HTML del correo con diseño moderno, formal y responsive
     */
    public static String generarHtmlCorreoJustificante(Map<String, Object> datos) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>APB - Confirmación de Registro Electrónico</title>\n" +
                "    <style>\n" +
                "        @media only screen and (max-width: 620px) {\n" +
                "            .email-container {\n" +
                "                width: 100% !important;\n" +
                "                max-width: 100% !important;\n" +
                "            }\n" +
                "            .header-padding {\n" +
                "                text-align: center !important;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; font-size: 15px; font-family: Arial, Helvetica, sans-serif; background-color: #f4f4f4;\">\n" +
                "    <table role=\"presentation\" style=\"width: 100%; border-collapse: collapse;\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px 10px;\">\n" +
                "                <table role=\"presentation\" class=\"email-container\" style=\"width: 100%; max-width: 750px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);\">\n" +
                "                    <!-- Cabecera con logo -->\n" +
                "                    <tr>\n" +
                "                        <td class=\"header-padding\" style=\"background: #00496C; padding: 15px 45px 15px; text-align: left; border-radius: 8px 8px 0 0;\">\n" +
                "                            <img src=\"https://seu.portsdebalears.gob.es/seuapb/assets/images/portsdebalearsNegH_600x200.jpg\" alt=\"Autoritat Portuaria de Baleares\" style=\"max-width: 250px; width: 100%; height: auto;\" />\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <!-- Contenido principal -->\n" +
                "                    <tr>\n" +
                "                        <td class=\"content-padding\" style=\"padding: 40px 45px 20px 45px;;\">\n" +
                "                            <p style=\"color: #333333; line-height: 1.6; margin: 0 0 20px 0;\">\n" +
                                            datos.get("saludo") + " <strong>" + datos.get("destinatarios") + "</strong>:\n" +
                "                            </p>\n" +
                "                            <p style=\"color: #333333; line-height: 1.6; margin: 0 0 30px 0;\">\n" +
                "                                Le informamos que su registro electrónico ha sido presentado correctamente a través de nuestra sede electrónica.\n" +
                "                            </p>\n" +
                "                            <p style=\"color: #333333; line-height: 1.6; margin: 0 0 15px 0;\">\n" +
                "                                A continuación, le facilitamos los datos principales del registro realizado:\n" +
                "                            </p>\n" +
                "                            <!-- Información del registro -->\n" +
                "                            <table role=\"presentation\" style=\"width: 100%; border-collapse: collapse; background-color: #f8f9fa; border-radius: 6px; margin: 0 0 30px 0;\">\n" +
                "                                <tr>\n" +
                "                                    <td class=\"info-box\" style=\"padding: 20px;\">\n" +
                "                                        <table role=\"presentation\" style=\"width: 100%; border-collapse: collapse;\">\n" +
                "                                            <tr class=\"data-row\">\n" +
                "                                                <td class=\"data-label\" style=\"padding: 6px 0px 6px 10px; color: #666666; vertical-align: top; width: 30%;\">\n" +
                "                                                    <strong style=\"color: #003d7a;\">Oficina de registro:</strong>\n" +
                "                                                </td>\n" +
                "                                                <td class=\"data-value\" style=\"padding: 6px 0px 6px 10px; color: #333333; vertical-align: top;\">\n" +
                "                                                    " + datos.get("oficinaRegistro") + "\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                            <tr class=\"data-row\">\n" +
                "                                                <td class=\"data-label\" style=\"padding: 6px 0px 6px 10px; color: #666666; vertical-align: top;\">\n" +
                "                                                    <strong style=\"color: #003d7a;\">Fecha de presentación:</strong>\n" +
                "                                                </td>\n" +
                "                                                <td class=\"data-value\" style=\"padding: 6px 0px 6px 10px; color: #333333; vertical-align: top;\">\n" +
                "                                                    " + datos.get("fechaRegistro") + "\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                            <tr class=\"data-row\">\n" +
                "                                                <td class=\"data-label\" style=\"padding: 6px 0px 6px 10px; color: #666666; vertical-align: top;\">\n" +
                "                                                    <strong style=\"color: #003d7a;\">Número de registro:</strong>\n" +
                "                                                </td>\n" +
                "                                                <td class=\"data-value\" style=\"padding: 6px 0px 6px 10px; color: #333333; vertical-align: top;\">\n" +
                "                                                    <strong style=\"color: #005eb8;\">" + datos.get("numeroRegistro") + "</strong>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                            <p style=\"color: #333333; line-height: 1.6; margin: 0 0 30px 0;\">\n" +
                "                                Adjunto a este correo encontrará el justificante de presentación en formato PDF, que acredita la correcta recepción de su solicitud.\n" +
                "                            </p>\n" +
                "                            <p style=\"color: #333333; line-height: 1.6; margin: 0 0 20px 0;\">\n" +
                "                                En caso de que haya omitido adjuntar alguna documentación durante el proceso de registro, le ofrecemos la posibilidad de completar su expediente a través del siguiente enlace: \n" +
                "                            </p>\n" +
                "                            <p style=\"color: #333333; line-height: 1.6; margin: 0 0 20px 0;\">\n" +
                "                                <a href=\"" + datos.get("enlaceTramite") + "\" style=\"color: #005eb8; text-decoration: underline;\">Acceder al trámite para aportar documentación adicional</a>\n" +
                "                            </p>\n" +
                "                            <p style=\"color: #333333; line-height: 1.6; margin: 0 0 20px 0;\">\n" +
                "                                Para cualquier consulta o aclaración, puede ponerse en contacto con nuestra oficina a través de los canales habituales de atención al ciudadano.\n" +
                "                            </p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <!-- Pie de página -->\n" +
                "                    <tr>\n" +
                "                        <td class=\"footer-padding\" style=\"background-color: #f8f9fa; padding: 25px 45px; border-radius: 0 0 8px 8px; border-top: 3px solid #005eb8;\">\n" +
                "                            <!-- Tabla de dos columnas: Atentamente e Información de contacto -->\n" +
                "                            <table role=\"presentation\" style=\"width: 100%; border-collapse: collapse; margin: 0 0 20px 0;\">\n" +
                "                                <tr>\n" +
                "                                    <td style=\"vertical-align: top; width: 50%;\">\n" +
                "                                        <p style=\"color: #666666; font-size: 14px; line-height: 1.6; margin: 0;\">\n" +
                "                                            Atentamente,\n" +
                "                                        </p>\n" +
                "                                        <p style=\"color: #666666; font-size: 14px; line-height: 1.6; margin-top: 5px;\">\n" +
                "                                            <strong style=\"color: #003d7a; font-size: 15px;\">Autoridad Portuaria de Baleares</strong>\n" +
                "                                        </p>\n" +
                "                                    </td>\n" +
                "                                    <td style=\"vertical-align: top; width: 50%; text-align: right;\">\n" +
                "                                        <p style=\"color: #666666; font-size: 12px; line-height: 1.7; margin: 0;\">\n" +
                "                                            Moll Vell 3-5, Palma 07012<br/>\n" +
                "                                            Tel: 971 228 150<br/>\n" +
                "                                            <a href=\"mailto:portsdebalears@portsdebalears.com\" style=\"color: #005eb8; text-decoration: none;\">portsdebalears@portsdebalears.com</a>\n" +
                "                                        </p>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                            \n" +
                "                            <hr style=\"border: none; border-top: 1px solid #ddd; margin: 20px 0;\" />\n" +
                "                            \n" +
                "                            <p style=\"color: #999999; font-size: 12px; line-height: 1.5; margin: 0;\">\n" +
                "                                Este es un correo electrónico automático. Por favor, no responda a este mensaje.<br/>\n" +
                "                                Para más información, visite nuestra <a href=\"https://seu.portsdebalears.gob.es\" style=\"color: #005eb8; text-decoration: none;\">sede electrónica</a>.\n" +
                "                            </p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";
    }
	
	private static InputStream obtenerPlantilla(String nombrePlantilla) throws FileNotFoundException {
    	ClassLoader classLoader = DocumentHelper.class.getClassLoader();
    	InputStream is = classLoader.getResourceAsStream("plantillas/" + nombrePlantilla);
    	
    	if (is == null) {
    	    throw new FileNotFoundException("No se encontró la plantilla");
    	}
		return is;
	}
	
}
