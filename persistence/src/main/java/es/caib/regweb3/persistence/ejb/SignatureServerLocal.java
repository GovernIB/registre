package es.caib.regweb3.persistence.ejb;

import es.caib.plugins.arxiu.api.Firma;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NTranslation;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Locale;

/**
 * Created by jpernia on 04/04/2017.
 * @author anadal
 */
@Local
public interface SignatureServerLocal {
  
      
    // La configuracio del plugin indicarà quin certificat usar
    String CONFIG_USERNAME = null;
  

    /**
     * Método que genera el {@link org.fundaciobit.plugins.documentcustody.api.SignatureCustody} de un Justificante
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @param fileName
     * @return
     * @throws Exception
     */
    SignatureCustody signJustificante(byte[] pdfsource, String languageUI, Long idEntidadActiva, StringBuilder peticion, String numeroRegistro, String fileName) throws Exception, I18NException;

    /**
     * Método que genera una  {@link es.caib.plugins.arxiu.api.Firma} de un Justificante
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @param fileName
     * @return
     * @throws Exception
     */
    Firma signJustificanteApiArxiu(byte[] pdfsource, String languageUI, Long idEntidadActiva, StringBuilder peticion, String numeroRegistro, String fileName) throws Exception, I18NException;
    
    
    /**
     * Ho hem de fer passar per un EJB a causa del BUG CXF des de capa WEB
     * @param input
     * @param idEntidad
     * @param sir
     * @param locale
     * @return
     * @throws I18NException
     */
    I18NTranslation checkDocumentAndSignature(AnexoFull input, long idEntidad,
                                              boolean sir, Locale locale, boolean force, String numeroRegistro) throws I18NException;


    /**
     *  mitjancant el plugin de validar Firma
     * @param input
     * @param idEntidad
     * @param locale
     * @param force
     * @return
     * @throws I18NException
     */
    I18NTranslation checkDocument(AnexoFull input, long idEntidad, Locale locale, boolean force) throws I18NException;


    /**
     * 
     * @param input Parametre d'entrada sortida. Si tot al final aquest
     *  objecte contindrà la signatura. 
     * @param idEntidad
     * @param locale
     * @throws I18NException
     */
    void firmaPAdESEPES(AnexoFull input, long idEntidad, Locale locale, String numeroRegistro) throws I18NException;

    /**
     *
     * @param anexosEnviarASir
     * @param idEntidad
     * @param locale
     * @param force
     * @throws I18NException
     */
    List<AnexoFull> firmarAnexosEnvioSir(List<AnexoFull> anexosEnviarASir, Long idEntidad, Locale locale, boolean force, String numeroRegistro) throws I18NException;
    
}
