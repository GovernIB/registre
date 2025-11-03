package org.fundaciobit.plugins.alfrescogdapb.custody.helper;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.fundaciobit.plugins.alfrescogdapb.custody.plugin.ArxiuDigitalApbCustodyPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe d'ajuda del plugin
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class Utils {
	
	public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();

    public static final char[] DEFAULT_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static final int DEFAULT_SIZE = 8;
    
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	/**
	 * Canvia el format d'una data
	 * 
	 * @return la data amb el format yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static DateFormat getFormatData() {
		TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		df.setTimeZone(tz);
		
		return df;
	}
	
	/**
	 * Invoca un metode d'un objecte usant reflection
	 * 
	 * @param object
	 * @param methodName
	 * @return
	 */
	public static Object invokeMethod(Object object, String methodName) {
		Object val = null;
		try {
			Method method = object.getClass().getMethod(methodName, null);
			
			if (method != null)
				val = method.invoke(object, null);
		} catch (Exception e) {
			throw new RuntimeException("No s'ha pogut trobar el metode " + methodName + " de " + object.getClass());
		}
		return val;	
	}

	/**
	 * Retorna el codi ISO 639-1 del idioma.
	 * 
	 * @param codi
	 * @return
	 */
	public static String getIdiomaCodi(int codi) {
		switch (codi) {
		case 1:
			return "ca";
		case 2:
			return "es";
		case 3:
			return "gl";
		case 4:
			return "eu";
		case 5:
			return "en";
		}
		return "es";
	}

	/**
	 * Retorna la id d'Alfresco amb la extensi� que toca.
	 * @param id
	 * @param name
	 * @return
	 */
	public static String getNewName(String id, String name) {
		id = id.substring(0, id.indexOf("#"));
		return id + (name.substring(name.lastIndexOf("."), name.length()));
	}
	
	/**
	 * Returna el valor d'un Map que acaba amb la key que s'indica.
	 * @param map
	 * @param key
	 * @return
	 */
	public static Object getEndsWith(Map<String,Object> map, String key){
		Object metadada = null;
		for(String k : map.keySet()){
			if (k.endsWith(key)){
				metadada = map.get(k);
			}
		}
		if (metadada == null)
			logger.error("No s'ha pogut trobar la metadata: " + key);
		
		return metadada;
	}
	
	/**
	 * Genera el valor del campo descripci�n del registro
	 * @param registroDetalle
	 * @return
	 */
	public static String generateDescripcion(Object registroDetalle, String numeroRegistro) {
		logger.debug("--------------------------");
		logger.debug("Generant la descripció...");
		String descripcion = null;
		String numeroRegistroOrigen = null;
		String extracto = null;
		String interesado = "";
		String representante = "";
		String documento, documentoRepr;
		String nombre = "";
		String apellido1 = "";
		String apellido2 = "";
		List<String> interesados = new ArrayList<String>();
		
		try {
			if (registroDetalle != null) {
				Object interesadosO = invokeMethod(registroDetalle, "getInteresados");
				if (interesadosO != null) {
//					if((ArrayList<String>) interesadosO != null) {
						logger.debug("Get interessats...");
						interesados.addAll((Collection<? extends String>) interesadosO);
						logger.debug("S'han recuperat " + interesados.size() + " interessats..");
//					}
				}
//				N�mero registro origem
				Object numeroRegistroOrigenO = invokeMethod(registroDetalle, "getNumeroRegistroOrigen");
				if(numeroRegistroOrigenO != null) {
					numeroRegistroOrigen = numeroRegistroOrigenO.toString();
				}
//				Extracto
				Object extractoO = invokeMethod(registroDetalle, "getExtracto");
				if(extractoO != null) {
					extracto = extractoO.toString();
				}
//				Interesados
				for (Object inter : interesados) {
					Object isRepresentanteO = invokeMethod(inter, "getIsRepresentante");
					if (isRepresentanteO.equals(false)) {
//						Documento interesado
						Object documentoO = invokeMethod(inter, "getDocumento");
						if (documentoO != null) {
							documento = documentoO.toString();
						} else {
							documento = "-";
						}
//						Nombre interesado
						Object nombreO = invokeMethod(inter, "getNombre");
						Object razonSocialO = invokeMethod(inter, "getRazonSocial");
						if (nombreO != null) {
							nombre = nombreO.toString();
							Object apellido1O = invokeMethod(inter, "getApellido1");
							if (apellido1O != null) {
								apellido1 = apellido1O.toString();
							}
							Object apellido2O = invokeMethod(inter, "getApellido2");
							if (apellido2O != null) {
								apellido2 = apellido2O.toString();
							}
						} else if(razonSocialO != null) {
							nombre = razonSocialO.toString();
						}
//						Dades representant
						Object representanteO = invokeMethod(inter, "getRepresentante");
						if (representanteO != null) {
							Object repre = representanteO;
							String nombreRep = "";
							String apellido1Rep = "";
							String apellido2Rep = "";
							Object documentoReO = invokeMethod(repre, "getDocumento");
							if (documentoReO != null) {
								documentoRepr = documentoReO.toString();
							} else {
								documentoRepr = "-";
							}
							Object nombreReO = invokeMethod(repre, "getNombre");
							Object razonSocialReO = invokeMethod(repre, "getRazonSocial");
							if (nombreReO != null) {
								nombreRep = nombreReO.toString();
								Object apellido1ReO = invokeMethod(repre, "getApellido1");
								if (apellido1ReO != null) {
									apellido1Rep = apellido1ReO.toString();
								}
								Object apellido2ReO = invokeMethod(repre, "getApellido2");
								if (apellido2ReO != null) {
									apellido2Rep = apellido2ReO.toString();
								}
	
							} else if (razonSocialReO != null){
								nombreRep = razonSocialReO.toString();
							}
							representante = " (Rep.: [" + documentoRepr + "] " + nombreRep + " " + apellido1Rep + " " + apellido2Rep + ")";
						}
						interesado += "[" + documento + "] " + nombre + " " + apellido1 + " " + apellido2 + " " + representante + ", ";
					}
				}
				
				descripcion = "Núm. Registro: " + numeroRegistro + " | Núm. Registro Origen: " + numeroRegistroOrigen + " | Extracto: " + extracto + " | Interesados: " + interesado;
				//Llevam la darrera comma
				descripcion = descripcion.replaceAll(", $", "");
			}
		} catch (Exception ex) {
			logger.error("No s'ha pogut generar la descripció del registre", ex);
		}
		logger.debug("-----------------------------");
		return descripcion;
	}

	public static String getJustificantNom(
			String fileName,
			boolean entrada) {
		int index = fileName.lastIndexOf('.');
		if (entrada)
			return fileName.substring(0, index) + "_E.pdf";
		else
			return fileName.substring(0, index) + "_S.pdf";
	}
	
	public static String getDocumentName(
			String documentNom,
			String mimetype,
			String fitxerNom,
			boolean controlarColisions) {
		String fileNameWithExtension = null;
		String extension = null;
		try {
			if (mimetype != null && mimetype.equals(MimeTypeHelper.MIME_APPLICATION_OCTET_STREAM)) //tipo gen�rico que podr�a generar mal la extensi�n
				mimetype = null;
			
			if (mimetype != null) 
				extension = MimeTypeHelper.getDefaultExt(mimetype);
			
			if (extension != null && !extension.equals("unknown")) {
				fileNameWithExtension = documentNom + "." + extension;
			} else if(fitxerNom.lastIndexOf(".") != -1 && fitxerNom.lastIndexOf(".") != 0) {
				fileNameWithExtension = documentNom + "." + fitxerNom.substring(fitxerNom.lastIndexOf(".") + 1);
			} else {
				return controlarColisions ? formatearNom(documentNom) : documentNom;
			}
		} catch (Exception e) {
			throw new RuntimeException("No s'ha pogut generar el nom del document: " + e.getMessage());
		}

		return controlarColisions ? formatearNom(fileNameWithExtension) : fileNameWithExtension;
	} 	
	
	private static String formatearNom(String fileName) {
		String sufix = fileName.substring(0, fileName.lastIndexOf("."));
		String prefix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		return sufix + "_" + randomUUID() + prefix;
	}
	
	private static String randomUUID() {
        return randomUUID(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }
	
	private static String randomUUID(final Random random, final char[] alphabet, final int size) {
        if (random == null) throw new IllegalArgumentException("Random no puede ser nulo");
        if (alphabet == null) throw new IllegalArgumentException("El alfabeto no puede ser nulo");
        if (alphabet.length == 0 || alphabet.length >= 256) throw new IllegalArgumentException("El alfabeto debe contener entre 1 y 255 símbolos");
        if (size <= 0) throw new IllegalArgumentException("El tamanyo debe ser mayor que cero");
        final int mask = (2 << (int) Math.floor(Math.log(alphabet.length - 1) / Math.log(2))) - 1;
        final int step = (int) Math.ceil(1.6 * mask * size / alphabet.length);
        final StringBuilder idBuilder = new StringBuilder();
        while (true) {
            final byte[] bytes = new byte[step];
            random.nextBytes(bytes);
            for (int i = 0; i < step; i++) {
                final int alphabetIndex = bytes[i] & mask;
                if (alphabetIndex < alphabet.length) {
                    idBuilder.append(alphabet[alphabetIndex]);
                    if (idBuilder.length() == size) {
                        return idBuilder.toString();
                    }
                }
            }
        }
    }
}
