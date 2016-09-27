package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;

import java.util.Calendar;


/**
 * Created 1/12/14 13:45
 *
 * @author mgonzalez
 * @author anadal
 */
public class AnexoConverter extends CommonConverter {
  
  
  public static final Logger log = Logger.getLogger(AnexoConverter.class);

  /**
   * Convierte un {@link es.caib.regweb3.ws.model.AnexoWs} en un {@link es.caib.regweb3.model.Anexo}
   * @param anexoWs
   * @return
   * @throws Exception
   * @throws I18NException
   */

   public static AnexoFull getAnexoFull(AnexoWs anexoWs, Long idEntidad,TipoDocumentalLocal tipoDocumentalEjb

       ) throws Exception, I18NException {

        if (anexoWs == null){
            return  null;
        }


        Anexo anexo = procesarAnexo(anexoWs, idEntidad,tipoDocumentalEjb);
        
        AnexoFull anexoFull = new AnexoFull(anexo);
       

        // Guardamos el Anexo datos básicos
        //anexo = anexoEjb.persist(anexo);
        
        
        // Si validez Documento --> Copia no se admite firma
       if (RegwebConstantes.ANEXO_TIPOVALIDEZDOCUMENTO_COPIA.equals(anexoWs.getValidezDocumento()) && anexoWs.getNombreFirmaAnexada() != null && anexoWs.getFirmaAnexada() != null) {
           //anexoWs.setNombreFirmaAnexada("");
           //anexoWs.setFirmaAnexada(null);
            //anexoWs.setTamanoFirmaAnexada(null);
           //anexoWs.setTipoMIMEFirmaAnexada(null);
           throw new Exception("Si la validessa del Document es  còpia = 01"
                   + " no s'admet firma");
        }


       final int modoFirma = anexoWs.getModoFirma();
       DocumentCustody doc = null;
       if (anexoWs.getNombreFicheroAnexado() != null && anexoWs.getFicheroAnexado() != null) {
           doc = new DocumentCustody();
           doc.setData(anexoWs.getFicheroAnexado());
           doc.setMime(anexoWs.getTipoMIMEFicheroAnexado());
           doc.setName(anexoWs.getNombreFicheroAnexado());
       }
       anexoFull.setDocumentoCustody(doc);
       anexoFull.setDocumentoFileDelete(false);


       SignatureCustody sign = null;
       if (anexoWs.getNombreFirmaAnexada() != null && anexoWs.getFirmaAnexada() != null) {
           sign = new SignatureCustody();

           sign.setData(anexoWs.getFirmaAnexada());
           sign.setMime(anexoWs.getTipoMIMEFirmaAnexada());
           sign.setName(anexoWs.getNombreFirmaAnexada());
       }

       switch (modoFirma) {
           // Document amb firma adjunta
           case RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED:
               // TODO Emprar mètode per descobrir tipus de signatura
               // TODO  Intentar obtenir tipus firma a partir de mime, nom o contingut
               sign = new SignatureCustody();
               sign.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);
               sign.setAttachedDocument(null);
               //El documento con la firma viene en la parte de documento y hay que pasarla a firma
               anexoFull.setDocumentoCustody(null);
               if (anexoWs.getNombreFicheroAnexado() != null && anexoWs.getFicheroAnexado() != null) {
                   sign.setData(anexoWs.getFicheroAnexado());
                   sign.setMime(anexoWs.getTipoMIMEFicheroAnexado());
                   sign.setName(anexoWs.getNombreFicheroAnexado());
               } else {
                   throw new Exception("Els camps NombreFicheroAnexado i FicheroAnexado no poden ser null");
               }

               break;

           // Firma en document separat
           case RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED:
               // TODO Emprar mètode per descobrir tipus de signatura
               // TODO  Intentar obtenir tipus firma a partir de mime, nom o contingut
               if (anexoWs.getNombreFirmaAnexada() == null || anexoWs.getFirmaAnexada() == null) {
                   throw new Exception("Els camps NombreFirmaAnexada i FirmaAnexada no poden ser null");
               }
               sign.setSignatureType(SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT);
               sign.setAttachedDocument(false);
               if (doc == null) {
                   throw new Exception("El document no s'envia i el modo de firma "
                           + " és 'Firma en document separat'");
               }
               break;

       }


        anexoFull.setSignatureCustody(sign);
        anexoFull.setSignatureFileDelete(false);

        return  anexoFull;
   }

   /**
   * Convierte un {@link es.caib.regweb3.model.Anexo} en un {@link es.caib.regweb3.ws.model.AnexoWs}
   * @param anexoEjb
   * @param anexoFull
   * @return
   * @throws Exception
   * @throws I18NException
   */
   public static AnexoWs getAnexoWs(AnexoFull anexoFull, AnexoLocal anexoEjb) throws Exception {

        if (anexoFull == null || anexoFull.getAnexo() == null){
            return  null;
        }

       AnexoWs anexoWs = procesarAnexoWs(anexoFull, anexoEjb);
        return anexoWs;
    }


  /**
   * Convierte un {@link es.caib.regweb3.ws.model.AnexoWs } en un {@link es.caib.regweb3.model.Anexo}
   * @param anexoWs
   * @param tipoDocumentalEjb
   * @return
   * @throws Exception
   */
  private static Anexo procesarAnexo(AnexoWs anexoWs, Long idEntidad, 
      TipoDocumentalLocal tipoDocumentalEjb) throws Exception{

       Anexo anexo = new Anexo();

       if(!StringUtils.isEmpty(anexoWs.getTitulo())){ anexo.setTitulo(anexoWs.getTitulo());}

       if(anexoWs.getTipoDocumental()!= null) {
         
         String tdws = anexoWs.getTipoDocumental();
         TipoDocumental td = getTipoDocumental(tdws, idEntidad, tipoDocumentalEjb);
         if (td == null) {
           log.warn("Se ha pasdo por parametro el tipoDocumental con ID '" 
              + tdws + "' pero el sistema no ha encontrado ninguno con este código.");
         }
         anexo.setTipoDocumental(td);
       }
       if(anexoWs.getValidezDocumento()!= null) {
         anexo.setValidezDocumento(getTipoValidezDocumento(anexoWs.getValidezDocumento()));
       }
       if(anexoWs.getTipoDocumento()!= null) {
         anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_BY_CODIGO_NTI.get(anexoWs.getTipoDocumento()));
       }
       if(!StringUtils.isEmpty(anexoWs.getObservaciones())){anexo.setObservaciones(anexoWs.getObservaciones());}
       if(anexoWs.getOrigenCiudadanoAdmin()!=null){anexo.setOrigenCiudadanoAdmin(anexoWs.getOrigenCiudadanoAdmin());}
       
       if(anexoWs.getCsv() != null){anexo.setCsv(anexoWs.getCsv());}

       if(anexoWs.getFechaCaptura()!= null){anexo.setFechaCaptura(anexoWs.getFechaCaptura().getTime());}

       // Part de firma Anexada
       if(anexoWs.getModoFirma()!=null){anexo.setModoFirma(anexoWs.getModoFirma());}

        return anexo;

  }


  /**
   * Convierte un {@link es.caib.regweb3.model.Anexo} en un {@link es.caib.regweb3.ws.model.AnexoWs}
   * @param anexoFull
   * @param anexoEjb
   * @return
   * @throws Exception
   */
  private static AnexoWs procesarAnexoWs(AnexoFull anexoFull, AnexoLocal anexoEjb) throws Exception {

       AnexoWs anexoWs = new AnexoWs();

       Anexo anexo = anexoFull.getAnexo();

       if(!StringUtils.isEmpty(anexo.getTitulo())){anexoWs.setTitulo(anexo.getTitulo());}

       if(anexo.getTipoDocumental()!= null){anexoWs.setTipoDocumental(anexo.getTipoDocumental().getCodigoNTI());}
       if(anexo.getValidezDocumento()!= null) {
         anexoWs.setValidezDocumento(anexo.getValidezDocumento().toString());
       }
       if(anexo.getTipoDocumento()!= null) {
         anexoWs.setTipoDocumento(RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()));
       }
       if(!StringUtils.isEmpty(anexo.getObservaciones())){anexoWs.setObservaciones(anexo.getObservaciones());}
       if(anexo.getOrigenCiudadanoAdmin()!=null){anexoWs.setOrigenCiudadanoAdmin(anexo.getOrigenCiudadanoAdmin());}
       
       if(anexo.getCsv() != null){anexoWs.setCsv(anexo.getCsv());}

       
       String custodyID = anexo.getCustodiaID();
       
       if (custodyID == null) {
         if (custodyID == null) {
           log.error("El anexo con id " + anexo.getId() 
               + " de un registro no tiene identificador de custodia", new Exception());
         }
       } else {
       
         // Firma Fichero Anexado
         if (anexoFull.getSignatureCustody() != null) {
           SignatureCustody sign = anexoFull.getSignatureCustody();
           anexoWs.setNombreFirmaAnexada(sign.getName());
           anexoWs.setTipoMIMEFirmaAnexada(sign.getMime());
           anexoWs.setFirmaAnexada(anexoEjb.getFirmaContent(custodyID));
         }
         
         
  
         // Documento fichero Anexado
         if (anexoFull.getDocumentoCustody() != null) {
            DocumentCustody doc = anexoFull.getDocumentoCustody();
            anexoWs.setNombreFicheroAnexado(doc.getName());
            anexoWs.setTipoMIMEFicheroAnexado(doc.getMime());
            anexoWs.setFicheroAnexado(anexoEjb.getArchivoContent(custodyID));
         }
       }
       

       // Transformamos de Date a Calendar
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(anexo.getFechaCaptura());
       if(anexo.getFechaCaptura()!= null){anexoWs.setFechaCaptura(calendar);}

       // Part de firma Anexada
       if(anexo.getModoFirma()!= 0){anexoWs.setModoFirma(anexo.getModoFirma());}


      return anexoWs;

  }



}
