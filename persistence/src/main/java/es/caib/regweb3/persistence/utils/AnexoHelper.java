package es.caib.regweb3.persistence.utils;

import es.caib.plugins.arxiu.api.Document;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.ColaLocal;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaConsultaLocal;
import es.caib.regweb3.persistence.ejb.SignatureServerLocal;
import es.caib.regweb3.utils.*;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.core.utils.MetadataFormatException;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.*;

/**
 * @author Limit Tecnologies S.A.
 * 
 */
@Component
public class AnexoHelper {

	protected final Logger log = Logger.getLogger(getClass());

	@EJB(mappedName = "regweb3/SignatureServerEJB/local")
	private SignatureServerLocal signatureServerEjb;
	@EJB(mappedName = "regweb3/PluginEJB/local")
	private PluginLocal pluginEjb;
	@EJB(mappedName = "regweb3/RegistroEntradaConsultaEJB/local")
	private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
	@EJB(mappedName = "regweb3/ColaEJB/local")
	private ColaLocal colaEjb;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void actualizarAnexoSistraPendienteVerificacionFirma(Anexo anexo, Long idEntidad)
			throws I18NException, CustodyException, NotSupportedCustodyException, MetadataFormatException {
		String custodyID = anexo.getCustodiaID();
		DocumentCustody documentCustody = null;// PluginCustodia
		SignatureCustody signatureCustody = null;// PluginCustodia
		Document document = null; // PluginArxiu
		IDocumentCustodyPlugin custody = null;
		try {
			if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {
				// Cargamos el plugin de Custodia
				custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_ANEXOS);
				documentCustody = custody.getDocumentInfo(custodyID);
				if (documentCustody == null)
					signatureCustody = custody.getSignatureInfo(custodyID);
			} else if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)) {
				// Cargamos el plugin de Arxiu
//				arxiuCaibUtils.cargarPlugin(idEntidad);
//				document = arxiuCaibUtils.getDocumento(custodyID, RegwebConstantes.ARXIU_VERSION_DOC, true, false);
			}
		} catch (Exception ex) {
			log.error("Ha habido un error recuperando el documento con uuid " + custodyID + " para la verificación de firma", ex.getCause());
		} catch (I18NException ex) {
			log.error("Ha habido un error recuperando el documento con uuid " + custodyID + " para la verificación de firma", ex.getCause());
		}

		// Firmado en SGD per sin información de firma en Regweb
		if (documentCustody == null && signatureCustody != null) {
			arreglarFirmaAnexo(anexo, null, signatureCustody, idEntidad, true);
		} else if (documentCustody != null || document != null) {
			// Firmado per sin información en Regweb ni en SGD
			byte[] contenido = documentCustody != null ? documentCustody.getData()
					: (document != null ? document.getContingut().getContingut() : null);
			String contentType = documentCustody != null ? documentCustody.getMime()
					: (document != null ? document.getContingut().getTipusMime() : null);
			
			boolean signed = false;
			try {
				log.info("=====Comprobando si el anexo " + anexo.getId() + " contiene firmas");
				boolean force = false;
				signed = isAnexoSigned(contenido, contentType, anexo.getId(), force);
				log.info("=====Anexo con id " + anexo.getId() + " signed: " + signed);
			} catch (Exception e) {
				arreglarFirmaAnexo(anexo, documentCustody, null, idEntidad, signed);
			}
			
			if (signed) {
				log.info("=====Arreglando la firma del anexo " + anexo.getId());
				arreglarFirmaAnexo(anexo, documentCustody, null, idEntidad, signed);

				// Actualizar datos documento en SGD
				Map<String, Object> custodyParameters = new HashMap<String, Object>();
				custodyParameters.put("updateOnlySignature", true);
				SignatureCustody signature = new SignatureCustody(null, documentCustody.getData(), null);
				custody.saveAll(custodyID, custodyParameters, null, signature, null);
				log.info("=====La firma del anexo " + anexo.getId() + " se ha solucionado correctamente");
			}

		}
		anexo.setFirmaverificada(true);
		
		
		try {
			// Actualiza estado cola
			Long idRegistroEntrada = registroEntradaConsultaEjb.findIdByRegistroDetalle(anexo.getRegistroDetalle().getId());
			Cola cola = colaEjb.findByIdObjeto(idRegistroEntrada, idEntidad);
			boolean anexosVerificados = true;
			
			if (cola != null) {
				List<Anexo> anexosRegistroActual = anexo.getRegistroDetalle().getAnexos();
				for (Anexo anexoR: anexosRegistroActual) {
					if (!anexoR.getFirmaverificada()) {
						anexosVerificados = false;
						break;
					}
				}
				if (anexosVerificados) {
					colaEjb.actualizarAnexosVerificados(cola.getId());
				}
			}
		} catch (Exception e) {
			log.error("No se ha podido actualizar el campo anexos_verificados de Cola");
		}
	}

	private void arreglarFirmaAnexo(Anexo anexo, DocumentCustody documentCustody, SignatureCustody signatureCustody,
			Long idEntidad, boolean signed) throws I18NException {
		// Validar firma
		final boolean force = false; // Indica si queremos forzar la excepción.
		AnexoFull anexoFull = new AnexoFull(anexo);
		anexoFull.setDocumentoCustody(documentCustody);
		anexoFull.setSignatureCustody(signatureCustody);
		signatureServerEjb.checkDocument(anexoFull, idEntidad, new Locale("es"), force, true, signed);
		
		int estadoFirma = anexoFull.getAnexo().getEstadoFirma();
		if (RegwebConstantes.ANEXO_FIRMA_NOINFO != estadoFirma ||
				(RegwebConstantes.ANEXO_FIRMA_NOINFO == estadoFirma && signed))
			anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
	}

	private boolean isAnexoSigned(byte[] contingut, String contentType, Long anexoId, boolean force) {
		if (contentType.equals("application/pdf")) {
			PdfReader reader;
			try {
				if (force)
					throw new RuntimeException("Prueba excepción");
				reader = new PdfReader(contingut);
				AcroFields acroFields = reader.getAcroFields();
				List<String> signatureNames = acroFields.getSignatureNames();
				if (signatureNames != null && !signatureNames.isEmpty()) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				throw new RuntimeException();
			}
		} else {
			return false;
		}
	}

}
