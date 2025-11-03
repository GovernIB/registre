package es.caib.regweb3.persistence.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

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

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.ColaLocal;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaConsultaLocal;
import es.caib.regweb3.persistence.ejb.SignatureServerLocal;
import es.caib.regweb3.utils.RegwebConstantes;

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
	@EJB(mappedName = "regweb3/AnexoEJB/local")
	private AnexoLocal anexoEjb;
    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    private RegistroDetalleLocal registroDetalleEjb; 
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void actualizarAnexoSistraPendienteVerificacionFirma(Long anexoId, Long idEntidad)
			throws I18NException, Exception {
		Anexo anexo = anexoEjb.findById(anexoId);
		String custodyID = anexo.getCustodiaID();
		DocumentCustody documentCustody = null;
		SignatureCustody signatureCustody = null;
		IDocumentCustodyPlugin custody = null;
		try {
			if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {
				// Cargamos el plugin de Custodia
				custody = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_ANEXOS);
				documentCustody = custody.getDocumentInfo(custodyID);
				if (documentCustody == null) {
					signatureCustody = custody.getSignatureInfo(custodyID);
				}
			}
		} catch (Exception ex) {
			log.error("Ha habido un error recuperando el documento con uuid " + custodyID + " para la verificación de firma", ex.getCause());
		} catch (I18NException ex) {
			log.error("Ha habido un error recuperando el documento con uuid " + custodyID + " para la verificación de firma", ex.getCause());
		}

		// Verifica la firma en Alresco y Regweb para no dejar información inconsistente
		verificarFirmaAnexo(
				custody,
				idEntidad,
				custodyID, 
				documentCustody, 
				signatureCustody, 
				anexo);
		
		anexoEjb.actualizarFirmaVerificada(anexoId);
		
		log.info("finish: actualizarAnexoSistraPendienteVerificacionFirma [anexoId=" + anexoId + "]");
	}
	
	public void actualizarAnexosVerificadosCola(Long idEntidad, Long idRegistroDetalle, boolean anexosVerificados) {
		Long idRegistroEntrada = null;
		try {
			if (anexosVerificados) {
				idRegistroEntrada = registroEntradaConsultaEjb.findIdByRegistroDetalle(idRegistroDetalle);
				Cola cola = colaEjb.findByIdObjeto(idRegistroEntrada, idEntidad);
				colaEjb.actualizarAnexosVerificados(cola.getId());
			}
		} catch (Exception e) {
			log.error("===== Ha habido un error actualizando el campo anexosVerificados de la cola (idRegistro=" + idRegistroEntrada);
		}
	}

	private void verificarFirmaAnexo (
			IDocumentCustodyPlugin custody,
			Long idEntidad,
			String custodyID, 
			DocumentCustody documentCustody, 
			SignatureCustody signatureCustody, 
			Anexo anexo) throws I18NException, CustodyException, NotSupportedCustodyException, MetadataFormatException {
		if (documentCustody == null && signatureCustody != null) {
			// Firmado en SGD per sin información de firma en Regweb?
			arreglarFirmaAnexo(anexo, null, signatureCustody, idEntidad, true);
		} else if (documentCustody != null) {
			// Firmado per sin información en Regweb ni en SGD
			byte[] contenido = documentCustody.getData();
			String contentType = documentCustody.getMime();
			
			boolean signed = false;
			try {
				log.debug("=====Comprobando si el anexo " + anexo.getId() + " contiene firmas");
				boolean force = false;
				signed = isAnexoSigned(contenido, contentType, anexo.getId(), force);
				log.debug("=====Anexo con id " + anexo.getId() + " signed: " + signed);
			} catch (Exception e) {
				arreglarFirmaAnexo(anexo, documentCustody, null, idEntidad, signed);
			}
			
			if (signed) {
				log.debug("=====Arreglando la firma del anexo " + anexo.getId());
				arreglarFirmaAnexo(anexo, documentCustody, null, idEntidad, signed);

				// Actualizar datos documento en SGD
				Map<String, Object> custodyParameters = new HashMap<String, Object>();
				custodyParameters.put("updateOnlySignature", true);
				SignatureCustody signature = new SignatureCustody(null, documentCustody.getData(), null);
				custody.saveAll(custodyID, custodyParameters, null, signature, null);
				log.debug("=====La firma del anexo " + anexo.getId() + " se ha solucionado correctamente");
			}

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
				(RegwebConstantes.ANEXO_FIRMA_NOINFO == estadoFirma && signed)) {
			anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
		}
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

	public void eliminarAnexos(IRegistro registro) {
		try {
			RegistroDetalle registroDetalle = registro.getRegistroDetalle();
	    	Entidad entidad = registro.getUsuario().getEntidad();
	    	List<Anexo> anexos = new ArrayList<>(registroDetalle.getAnexos());
	    	
	    	for (Anexo anexo : anexos) {
	        	registroDetalleEjb.eliminarAnexoRegistroDetalle(
	        			anexo.getId(), 
	        			registroDetalle.getId(), 
	        			entidad.getId(), 
	        			true);
	        	
	        	log.info("Anexo con id " + anexo.getId() + " eliminado correctamente");
			}
		} catch (I18NException e) {
			log.error("Ha habido un error eliminando un anexo del registro " + registro.getId());
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Ha habido un error eliminando un anexo del registro " + registro.getId());
			e.printStackTrace();
		}
	}

}
