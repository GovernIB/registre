package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.persistence.validator.AnexoBeanValidator;
import es.caib.regweb3.persistence.validator.AnexoValidator;
import es.caib.regweb3.ws.converter.AnexoConverter;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.utils.AuthenticatedBaseWsImpl;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author anadal
 *
 */
public abstract class AbstractRegistroWsImpl extends AuthenticatedBaseWsImpl {

  
  @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
  public TipoDocumentalLocal tipoDocumentalEjb;
  
  @EJB(mappedName = "regweb3/AnexoEJB/local")
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
 protected List<AnexoFull> procesarAnexos(List<AnexoWs> anexosWs, Long entidadID) throws Exception, I18NValidationException, I18NException {

     List<AnexoFull> anexos  = new ArrayList<AnexoFull>();

     for (AnexoWs anexoWs : anexosWs) {
         //Convertimos a anexo

         AnexoFull anexoFull = AnexoConverter.getAnexoFull(anexoWs, entidadID,tipoDocumentalEjb);

         validateAnexo(anexoFull.getAnexo(), true);

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
         //anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, tipoRegistro);
                     
         anexos.add(anexoFull);
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
