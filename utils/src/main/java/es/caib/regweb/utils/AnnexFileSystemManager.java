package es.caib.regweb.utils;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.utils.PluginsManager;

/**
 * Created by Fundació BIT.
 * User: anadal
 * Date: 31/07/13
 */
public class AnnexFileSystemManager {



    protected static final Logger log = Logger.getLogger(AnnexFileSystemManager.class);
    
    
    protected static IDocumentCustodyPlugin plugin;
    
    protected  static IDocumentCustodyPlugin getInstance() throws Exception {
      
      if (plugin == null) {
        // Valor de la Clau
        final String propertyName = RegwebConstantes.REGWEB_PROPERTY_BASE + "annex.documentcustodyplugin";
        String className = System.getProperty(propertyName);
        if (className == null || className.trim().length()<=0) {
          throw new Exception("No hi ha cap propietat " + propertyName 
              + " definint la classe que gestiona el plugin de login");
        }
        // Carregant la classe
        Object obj;
        obj = PluginsManager.instancePluginByClassName(className, RegwebConstantes.REGWEB_PROPERTY_BASE + "annex.");
        plugin = (IDocumentCustodyPlugin)obj;
      }      

      return plugin; 
    }

    /**
     * Obtiene el fichero existente en el sistema de archivos
     * @param id
     * @return
     */
    public static DocumentCustody getArchivo(Long id) throws Exception {
      return getInstance().getDocumentInfo(String.valueOf(id));

    }

    /**
     * Obtiene la firma existente en el sistema de archivos
     * @param id
     * @return
     */
    public static SignatureCustody getFirma(Long id) throws Exception {
      return getInstance().getSignatureInfo(String.valueOf(id));
    }



    /**
     * Elimina un Archivo del sistema de archivos
     * @param id
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     *         casos.
     */
    public static boolean eliminarArchivo(Long id) throws Exception {

      
        getInstance().deleteCustody(String.valueOf(id));
        
        return true;
    }


    /**
     * Crea un file en el sistema de archivos
     * @param file
     * @param dstId
     * @return
     * @throws Exception
     */
    public static void crearArchivo(String name, byte[] file, String signatureName, byte[] signature,  Long dstId, int signatureMode) throws Exception {

      IDocumentCustodyPlugin instance = getInstance();
      
      final String custodyParameters = null;
      String dstIdProposed = String.valueOf(dstId);
      String dstIdAssigned = instance.reserveCustodyID(dstIdProposed, custodyParameters);
      
      if (!dstIdProposed.equals(dstIdAssigned)) {
        throw new Exception("El identificador assignador por el sistema de custodia" +
        		" no és el mismo que el propuesto");
      }

      //Comprobamos el modo de Firma
      String documentType = signatureMode == 1? DocumentCustody.OTHER_DOCUMENT_WITH_SIGNATURE : DocumentCustody.DOCUMENT_ONLY;

      DocumentCustody document = new DocumentCustody(name, file, documentType);
      instance.saveDocument(dstIdAssigned, custodyParameters, document);

      //
      if(signatureName != null && signature != null){
        SignatureCustody docSignature = new SignatureCustody(signatureName, signature, SignatureCustody.OTHER_SIGNATURE, false);
        instance.saveSignature(dstIdAssigned, custodyParameters, docSignature);
      }
      
      //log.info("Creamos el file: " + getArchivosPath()+dstId.toString());

    }

}
