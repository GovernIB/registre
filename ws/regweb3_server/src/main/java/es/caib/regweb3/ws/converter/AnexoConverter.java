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
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;

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
        if(RegwebConstantes.ANEXO_TIPOVALIDEZDOCUMENTO_COPIA.equals(anexoWs.getValidezDocumento())){ 
            anexoWs.setNombreFirmaAnexada("");
            anexoWs.setFirmaAnexada(null);
            //anexoWs.setTamanoFirmaAnexada(null);
            anexoWs.setTipoMIMEFirmaAnexada(null);
        }


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

        return  anexoFull;
   }

   /**
   * Convierte un {@link es.caib.regweb3.model.Anexo} en un {@link es.caib.regweb3.ws.model.AnexoWs}
   * @param anexo
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
       if(anexoWs.getFirmacsv() != null){anexo.setFirmacsv(anexoWs.getFirmacsv());}
       
       /*
       if(anexoWs.getCertificado() != null){anexo.setCertificado(anexoWs.getCertificado());}
       if(anexoWs.getTimestamp() != null){anexo.setTimestamp(anexoWs.getTimestamp());}
       if(anexoWs.getValidacionOCSP() != null){anexo.setValidacionOCSP(anexoWs.getValidacionOCSP());}
       */


       // Part de fichero Anexado
       /*
       if(!StringUtils.isEmpty(anexoWs.getNombreFicheroAnexado())){anexo.setNombreFicheroAnexado(StringUtils.recortarNombre(anexoWs.getNombreFicheroAnexado(), RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH));}
       if(!StringUtils.isEmpty(anexoWs.getTipoMIMEFicheroAnexado())){anexo.setTipoMIME(anexoWs.getTipoMIMEFicheroAnexado());}
       anexo.setTamano(anexoWs.getTamanoFicheroAnexado());
       */
       if(anexoWs.getFechaCaptura()!= null){anexo.setFechaCaptura(anexoWs.getFechaCaptura().getTime());}

       // Part de firma Anexada
       if(anexoWs.getModoFirma()!=null){anexo.setModoFirma(anexoWs.getModoFirma());}
       //if(!StringUtils.isEmpty(anexoWs.getNombreFirmaAnexada())){anexo.setNombreFirmaAnexada(StringUtils.recortarNombre(anexoWs.getNombreFirmaAnexada(), RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH));}
      // anexo.setTamano(anexoWs.getTamanoFirmaAnexada());

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
         anexoWs.setTipoDocumento(RegwebConstantes.CODIGO_NTI_BY_TIPO_DOCUMENTO.get(anexo.getTipoDocumento()));
       }
       if(!StringUtils.isEmpty(anexo.getObservaciones())){anexoWs.setObservaciones(anexo.getObservaciones());}
       if(anexo.getOrigenCiudadanoAdmin()!=null){anexoWs.setOrigenCiudadanoAdmin(anexo.getOrigenCiudadanoAdmin());}
       
       if(anexo.getCsv() != null){anexoWs.setCsv(anexo.getCsv());}
       if(anexo.getFirmacsv() != null){anexoWs.setFirmacsv(anexo.getFirmacsv());}
       /*
       if(anexo.getCertificado() != null){anexoWs.setCertificado(anexo.getCertificado());}
       if(anexo.getTimestamp() != null){anexoWs.setTimestamp(anexo.getTimestamp());}
       if(anexo.getValidacionOCSP() != null){anexoWs.setValidacionOCSP(anexo.getValidacionOCSP());}
       */

       
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
             anexoWs.setFirmaAnexada(anexoEjb.getInstance().getSignature(custodyID));
         }
         
         
  
         // Documento fichero Anexado
         if (anexoFull.getDocumentoCustody() != null) {
            DocumentCustody doc = anexoFull.getDocumentoCustody();
            anexoWs.setNombreFicheroAnexado(doc.getName());
            anexoWs.setTipoMIMEFicheroAnexado(doc.getMime());
             anexoWs.setFicheroAnexado(anexoEjb.getInstance().getDocument(custodyID));
         }
       }
       

       // Transformamos de Date a Calendar
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(anexo.getFechaCaptura());
       if(anexo.getFechaCaptura()!= null){anexoWs.setFechaCaptura(calendar);}

       // Part de firma Anexada
       if(anexo.getModoFirma()!= 0){anexoWs.setModoFirma(anexo.getModoFirma());}
       
       // anexoWs.setTamanoFirmaAnexada(anexo.getTamano());

       return anexoWs;

  }



}
