package es.caib.regweb.ws.v3.impl;

import es.caib.regweb.model.Anexo;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.persistence.ejb.AnexoFull;
import es.caib.regweb.persistence.ejb.AnexoLocal;
import es.caib.regweb.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb.persistence.validator.AnexoBeanValidator;
import es.caib.regweb.persistence.validator.AnexoValidator;
import es.caib.regweb.ws.converter.AnexoConverter;
import es.caib.regweb.ws.model.AnexoWs;
import es.caib.regweb.ws.utils.AuthenticatedBaseWsImpl;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author anadal
 *
 */
public abstract class AbstractRegistroWsImpl extends AuthenticatedBaseWsImpl {

  
  @EJB(mappedName = "regweb/TipoDocumentalEJB/local")
  public TipoDocumentalLocal tipoDocumentalEjb;
  
  @EJB(mappedName = "regweb/AnexoEJB/local")
  public AnexoLocal anexoEjb;

  public AnexoValidator<Anexo> anexoValidator = new AnexoValidator<Anexo>();

  /**
  * Procesa los Anexos recibidos
  * @param anexosWs
  * @return
  * @throws Exception
  * @throws I18NValidationException
  * @throws I18NException
  */
 protected List<Anexo> procesarAnexos(List<AnexoWs> anexosWs, UsuarioEntidad usuarioEntidad,
     Long registroID, String tipoRegistro) throws Exception, I18NValidationException, I18NException {

     List<Anexo> anexos  = new ArrayList<Anexo>();

     for (AnexoWs anexoWs : anexosWs) {
         //Convertimos a anexo
         AnexoFull anexoFull = AnexoConverter.getAnexo(anexoWs, usuarioEntidad.getEntidad().getId(),tipoDocumentalEjb);

         validateAnexo(anexoFull.getAnexo(), true);

         // Guardamos el Anexo datos básicos
         //anexo = anexoEjb.persist(anexo);
         
         
         // Si validez Documento --> Copia no se admite firma
         if(ANEXO_TIPOVALIDEZDOCUMENTO_COPIA.equals(anexoWs.getValidezDocumento())){ 
             anexoWs.setNombreFirmaAnexada("");
             anexoWs.setFirmaAnexada(null);
             anexoWs.setTamanoFirmaAnexada(null);
             anexoWs.setTipoMIMEFirmaAnexada(null);
         }
         
         
         
         
         
         // AnexoFull anexoFull = 
         DocumentCustody doc = null;
         if (anexoWs.getNombreFicheroAnexado() != null && anexoWs.getFicheroAnexado() != null ) {
           doc = new DocumentCustody();
           doc.setData(anexoWs.getFicheroAnexado());
           doc.setDocumentType(DocumentCustody.DOCUMENT_ONLY);
           doc.setMime(anexoWs.getTipoMIMEFicheroAnexado());
           doc.setName(anexoWs.getNombreFicheroAnexado());
         }
         anexoFull.setDocumentoCustody(doc);
         anexoFull.setDocumentoFileDelete(false);
         
         
         SignatureCustody  sign = null;
         if (anexoWs.getNombreFirmaAnexada() != null && anexoWs.getFirmaAnexada() != null) {
           sign = new SignatureCustody();
           sign.setAttachedDocument(null);
           sign.setData(anexoWs.getFirmaAnexada());
           sign.setMime(anexoWs.getTipoMIMEFirmaAnexada());
           sign.setName(anexoWs.getNombreFirmaAnexada());
           sign.setSignatureType(SignatureCustody.OTHER_SIGNATURE);
         }
         anexoFull.setSignatureCustody(sign);
         anexoFull.setSignatureFileDelete(false);
         
         
         
/*
         anexo = anexoEjb.actualizarAnexoConArchivos(anexo.getId(),,
             ,
             ,
             anexoWs.getTamanoFicheroAnexado(), , ,
             
            ,
             anexoWs.getTamanoFirmaAnexada(), anexoWs.getModoFirma(), 
             anexoWs.getFechaCaptura().getTime());
         */
         
         // Actualizamos el anexo y le asociamos los archivos
         anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, tipoRegistro);
                     
         anexos.add(anexoFull.getAnexo());
     }

     return anexos;

 }

 
   /**
   *
   * @param anexo
   * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
   */
  protected void validateAnexo(Anexo anexo, boolean isNou) throws I18NValidationException, I18NException {
    //anexoValidator.validateStandalone(anexo);
    AnexoBeanValidator pfbv = new AnexoBeanValidator(anexoValidator);
    
    //final boolean isNou = true;
    pfbv.throwValidationExceptionIfErrors(anexo, isNou);
  }
  
  
}
