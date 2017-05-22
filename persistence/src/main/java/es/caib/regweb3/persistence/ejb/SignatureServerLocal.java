package es.caib.regweb3.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;

import es.caib.regweb3.model.utils.AnexoFull;

import java.util.Locale;

/**
 * Created by jpernia on 04/04/2017.
 * @author anadal
 */
@Local
@RolesAllowed({"RWE_USUARI"})
public interface SignatureServerLocal {
  
      
    // La configuracio del plugin indicarà quin certificat usar
    public static final String CONFIG_USERNAME = null; 
  

    /**Método que genera la Firma de un File para una Entidad en concreto
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @return
     * @throws Exception
     */
    public SignatureCustody signJustificante(byte[] pdfsource, String languageUI,
        Long idEntidadActiva) throws Exception, I18NException;
    
    
    /**
     * Ho hem de fer passar per un EJB a causa del BUG CXF des de capa WEB
     * @param input
     * @param idEntidad
     * @param sir
     * @param locale
     * @return
     * @throws I18NException
     */
     public AnexoFull checkDocumentAndSignature(AnexoFull input, long idEntidad,
         boolean sir, Locale locale) throws I18NException;


}
