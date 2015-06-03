package es.caib.regweb.ws.v3.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

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

         AnexoFull anexoFull = AnexoConverter.getAnexoFull(anexoWs, usuarioEntidad.getEntidad().getId(),tipoDocumentalEjb);

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
