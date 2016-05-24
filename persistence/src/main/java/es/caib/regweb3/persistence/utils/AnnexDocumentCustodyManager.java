package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.utils.PluginsManager;

/**
 *
 * 
 * Created by Fundació BIT.
 * User: anadal
 * Date: 31/07/13
 */
// TODO  Borrar
// Aquesta clase ja no s'emplea, es varen moure al AnexoBean les seves funcionalitats.
public class AnnexDocumentCustodyManager {



    protected static final Logger log = Logger.getLogger(AnnexDocumentCustodyManager.class);
    
    
    protected static IDocumentCustodyPlugin plugin;
    
    public  static IDocumentCustodyPlugin getInstance() throws Exception {
      
      if (plugin == null) {
        // Valor de la Clau
        final String propertyName = RegwebConstantes.REGWEB3_PROPERTY_BASE + "annex.documentcustodyplugin";
        String className = System.getProperty(propertyName);
        if (className == null || className.trim().length()<=0) {
          throw new Exception("No hi ha cap propietat " + propertyName 
              + " definint la classe que gestiona el plugin de login");
        }
        // Carregant la classe
        Object obj;
        obj = PluginsManager.instancePluginByClassName(className, RegwebConstantes.REGWEB3_PROPERTY_BASE + "annex.");
        plugin = (IDocumentCustodyPlugin)obj;
      }      

      return plugin; 
    }

    /**
     * Obtiene el fichero existente en el sistema de archivos
     * @param id
     * @return
     */
    public static DocumentCustody getArchivo(String custodiaID) throws Exception {
      
      if (custodiaID == null) {
        log.warn("getArchivo :: CustodiaID vale null !!!!!", new Exception());
        return null;
      } 
      
      return getInstance().getDocumentInfo(custodiaID);

    }

    /**
     * Obtiene la firma existente en el sistema de archivos
     * @param id
     * @return
     */
    public static SignatureCustody getFirma(String custodiaID) throws Exception {
      
      if (custodiaID == null) {
        log.warn("getFirma :: CustodiaID vale null !!!!!", new Exception());
        return null;
      } 
      
      return getInstance().getSignatureInfo(custodiaID);
    }



    /**
     * Elimina completamente una custodia ( = elimicion completa de Anexo)
     * @param id
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     *         casos.
     */
    public static boolean eliminarCustodia(String custodiaID) throws Exception {

      if (custodiaID == null) {
        log.warn("eliminarCustodia :: CustodiaID vale null !!!!!", new Exception());
        return false;
      } else {
        getInstance().deleteCustody(custodiaID);
        return true;
      }

    }
    
    
    /**
     * Solo elimina el archivo asociado al documento.
     * @param custodiaID
     * @return
     * @throws Exception
     */
    public static boolean eliminarDocumento(String custodiaID) throws Exception {

      if (custodiaID == null) {
        log.warn("eliminarDocumento :: CustodiaID vale null !!!!!", new Exception());
        return false;
      } else {
        getInstance().deleteDocument(custodiaID);
        getInstance().deleteSignature(custodiaID);
        return true;
      }

    }
    
    
    /**
     * Solo elimina la el archivo asociado a la firma
     * @param custodiaID
     * @return
     * @throws Exception
     */
    public static boolean eliminarFirma(String custodiaID) throws Exception {

      if (custodiaID == null) {
        log.warn("eliminarFirma :: CustodiaID vale null !!!!!", new Exception());
        return false;
      } else {
        getInstance().deleteDocument(custodiaID);
        return true;
      }

    }
    


    /**
     * 
     * @param file
     * @param dstId
     * @return Identificador de Custodia
     * @throws Exception
     */
    
    
    /**
     * Crea o actualiza un anexos en el sistema de custodia
     * @param name
     * @param file
     * @param signatureName
     * @param signature
     * @param signatureMode
     * @param custodyID Si vale null significa que creamos el archivo. Otherwise actualizamos el fichero.
     * @param custodyParameters JSON del registre
     * @return Identificador de custodia
     * @throws Exception
     */
    public static String crearArchivo(String name, byte[] file, String signatureName, 
        byte[] signature,  int signatureMode, String custodyID, String custodyParameters) throws Exception {

      IDocumentCustodyPlugin instance = getInstance();

      if (custodyID == null) {
        custodyID = instance.reserveCustodyID(custodyParameters);
      }

      //Comprobamos el modo de Firma
      String documentType = signatureMode == 1? DocumentCustody.OTHER_DOCUMENT_WITH_SIGNATURE : DocumentCustody.DOCUMENT_ONLY;

      DocumentCustody document = new DocumentCustody(name, file, documentType);
      instance.saveDocument(custodyID, custodyParameters, document);

      //
      if(signatureName != null && signature != null){
        SignatureCustody docSignature = new SignatureCustody(signatureName, signature, SignatureCustody.OTHER_SIGNATURE, false);
        instance.saveSignature(custodyID, custodyParameters, docSignature);
      }
      
      //log.info("Creamos el file: " + getArchivosPath()+dstId.toString());

      return custodyID;
    }

}
