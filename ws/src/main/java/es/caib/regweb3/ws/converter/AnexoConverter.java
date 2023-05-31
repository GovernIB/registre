package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import static es.caib.regweb3.utils.StringUtils.eliminarCaracteresProhibidosArxiu;


/**
 * Created 1/12/14 13:45
 *
 * @author mgonzalez
 * @author anadal
 */
public class AnexoConverter extends CommonConverter {


   public static final Logger log = LoggerFactory.getLogger(AnexoConverter.class);

   /**
    * Convierte un {@link es.caib.regweb3.ws.model.AnexoWs} en un {@link es.caib.regweb3.model.Anexo}
    * @param anexoWs
    * @return
    * @throws Exception
    * @throws I18NException
    */

   public static AnexoFull getAnexoFull(AnexoWs anexoWs, Long idEntidad,TipoDocumentalLocal tipoDocumentalEjb) throws Exception, I18NException {

      if (anexoWs == null){return  null;}

      Anexo anexo = procesarAnexo(anexoWs, idEntidad,tipoDocumentalEjb);

      AnexoFull anexoFull = new AnexoFull(anexo);

      // Si se trata de un Anexo confidencial, no es necesario crear el transformar el AnexoFull
      if(anexoWs.getConfidencial()){
         return anexoFull;
      }

      final int modoFirma = anexoWs.getModoFirma();
      DocumentCustody doc = null;

      //Documento
      if (StringUtils.isNotEmpty(anexoWs.getNombreFicheroAnexado()) && (anexoWs.getFicheroAnexado() != null && anexoWs.getFicheroAnexado().length > 0)) {
         doc = new DocumentCustody();
         doc.setData(anexoWs.getFicheroAnexado());
         doc.setMime(anexoWs.getTipoMIMEFicheroAnexado());
         doc.setName(StringUtils.eliminarCaracteresProhibidosSIR(anexoWs.getNombreFicheroAnexado()));
      }
      anexoFull.setDocumentoCustody(doc);
      anexoFull.setDocumentoFileDelete(false);


      //Firma
      SignatureCustody sign = null;
      if (StringUtils.isNotEmpty(anexoWs.getNombreFirmaAnexada()) && (anexoWs.getFirmaAnexada()!=null && anexoWs.getFirmaAnexada().length >0)) {
         sign = new SignatureCustody();
         sign.setData(anexoWs.getFirmaAnexada());
         sign.setMime(anexoWs.getTipoMIMEFirmaAnexada());
         sign.setName(StringUtils.eliminarCaracteresProhibidosSIR(anexoWs.getNombreFirmaAnexada()));
      }

      switch (modoFirma) {
         // Document amb firma adjunta
         case RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED:
            // TODO Emprar mètode per descobrir tipus de signatura
            // TODO  Intentar obtenir tipus firma a partir de mime, nom o contingut
            sign = new SignatureCustody();
            sign.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);
            sign.setAttachedDocument(null);
               /*  Inicialmente nos enviaban el documento con firma adjunta en "FicheroAnexado".
                   Ahora soportamos los dos casos, que venga en uno o en otro, pero a partir de la versión 3.1 de regweb3,
                   se especifica en la documentación  que debe ser en "firmaAnexada" donde venga el documento.
               */
            anexoFull.setDocumentoCustody(null);
            if(StringUtils.isNotEmpty(anexoWs.getNombreFirmaAnexada()) && (anexoWs.getFirmaAnexada()!=null && anexoWs.getFirmaAnexada().length >0)){
               sign.setData(anexoWs.getFirmaAnexada());
               sign.setMime(anexoWs.getTipoMIMEFirmaAnexada());
               sign.setName(StringUtils.eliminarCaracteresProhibidosSIR(anexoWs.getNombreFirmaAnexada()));
            }else{
               if (StringUtils.isNotEmpty(anexoWs.getNombreFicheroAnexado()) && (anexoWs.getFicheroAnexado()!=null && anexoWs.getFicheroAnexado().length >0)) {
                  sign.setData(anexoWs.getFicheroAnexado());
                  sign.setMime(anexoWs.getTipoMIMEFicheroAnexado());
                  sign.setName(StringUtils.eliminarCaracteresProhibidosSIR(anexoWs.getNombreFicheroAnexado()));
               } else {
                  throw new Exception("Los campos 'nombreFirmaAnexada' o 'FirmaAnexada y 'nombreFicheroAnexado' o 'ficheroAnexado' no pueden estar vacios caso FIRMA ATTACHED");
               }
            }
            break;

         // Firma en document separat
         case RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED:
            // TODO Emprar mètode per descobrir tipus de signatura
            // TODO  Intentar obtenir tipus firma a partir de mime, nom o contingut
            if (StringUtils.isEmpty(anexoWs.getNombreFirmaAnexada()) || anexoWs.getFirmaAnexada() == null || anexoWs.getFirmaAnexada().length ==0) {
               throw new Exception("Los campos 'nombreFirmaAnexada' o 'FirmaAnexada' no pueden estar vacios caso FIRMA DETACHED");
            }
            sign.setSignatureType(SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT);
            sign.setAttachedDocument(false);
            if (doc == null) {
               throw new Exception("El documento no se envia y el modo de firma "
                  + " es 'Firma en documento separado'");
            }
            break;

      }

      anexoFull.setSignatureCustody(sign);
      anexoFull.setSignatureFileDelete(false);

      return  anexoFull;
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

      Anexo anexo = new Anexo(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);

      if(StringUtils.isNotEmpty(anexoWs.getTitulo())){ anexo.setTitulo(eliminarCaracteresProhibidosArxiu(anexoWs.getTitulo()));}

      if(anexoWs.getTipoDocumental()!= null) {

         String tdws = anexoWs.getTipoDocumental();
         TipoDocumental td = getTipoDocumental(tdws, idEntidad, tipoDocumentalEjb);
         if (td == null) {
            log.warn("Se ha pasdo por parametro el tipoDocumental con ID '"
               + tdws + "' pero el sistema no ha encontrado ninguno con este código.");
         }
         anexo.setTipoDocumental(td);
      }
      // SICRES4 deprecated
      if(anexoWs.getValidezDocumento()!= null) {
         anexo.setValidezDocumento(getTipoValidezDocumento(anexoWs.getValidezDocumento()));
      }
      if(anexoWs.getTipoDocumento()!= null) {
         //anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_BY_CODIGO_NTI.get(anexoWs.getTipoDocumento()));
         anexo.setTipoDocumento(RegwebConstantes.TIPO_ANEXO_BY_CODIGO_NTI.get(anexoWs.getTipoDocumento()));
      }
      if(StringUtils.isNotEmpty(anexoWs.getObservaciones())){anexo.setObservaciones(anexoWs.getObservaciones());}
      if(anexoWs.getOrigenCiudadanoAdmin()!=null){anexo.setOrigenCiudadanoAdmin(anexoWs.getOrigenCiudadanoAdmin());}

      if(anexoWs.getCsv() != null){anexo.setCsv(anexoWs.getCsv());}

      if(anexoWs.isJustificante() != null){
         anexo.setJustificante(anexoWs.isJustificante());
      }

      if(anexoWs.getFechaCaptura()!= null){anexo.setFechaCaptura(anexoWs.getFechaCaptura().getTime());}

      // Part de firma Anexada
      if(anexoWs.getModoFirma()!=null){anexo.setModoFirma(anexoWs.getModoFirma());}

      //Condifencial
      if(anexoWs.getConfidencial()){
         anexo.setConfidencial(anexoWs.getConfidencial());
         if(StringUtils.isNotEmpty(anexoWs.getNombreFicheroAnexado())){
            anexo.setNombreFichero(anexoWs.getNombreFicheroAnexado());
         }else{
            anexo.setNombreFichero(anexoWs.getTitulo());
         }
         //SICRES4 deprecated
         if(anexoWs.getHash() != null){ anexo.setHash(anexoWs.getHash()); }
         anexo.setTamanoFichero(anexoWs.getTamanoFichero());
      }

      return anexo;

   }

   /**
    * Convierte un {@link es.caib.regweb3.model.Anexo} en un {@link es.caib.regweb3.ws.model.AnexoWs}
    * @param anexo
    * @return
    * @throws Exception
    */
   public static AnexoWs transformarAnexoWs(Anexo anexo) throws Exception {

      AnexoWs anexoWs = new AnexoWs();

      if(StringUtils.isNotEmpty(anexo.getTitulo())){anexoWs.setTitulo(anexo.getTitulo());}

      if(anexo.getTipoDocumental()!= null){anexoWs.setTipoDocumental(anexo.getTipoDocumental().getCodigoNTI());}
      // SICRES4 deprecated
      if(anexo.getValidezDocumento()!= null) {
         anexoWs.setValidezDocumento(anexo.getValidezDocumento().toString());
      }
      if(anexo.getTipoDocumento()!= null) {
         anexoWs.setTipoDocumento(RegwebConstantes.CODIGO_SICRES_BY_TIPO_ANEXO.get(anexo.getTipoDocumento()));
      }
      if(StringUtils.isNotEmpty(anexo.getObservaciones())){anexoWs.setObservaciones(anexo.getObservaciones());}
      if(anexo.getOrigenCiudadanoAdmin()!=null){anexoWs.setOrigenCiudadanoAdmin(anexo.getOrigenCiudadanoAdmin());}

      if(anexo.getCsv() != null){anexoWs.setCsv(anexo.getCsv());}

      anexoWs.setJustificante(anexo.isJustificante());

      // Transformamos de Date a Calendar
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(anexo.getFechaCaptura());
      if(anexo.getFechaCaptura()!= null){anexoWs.setFechaCaptura(calendar);}

      // Part de firma Anexada
      if(anexo.getModoFirma()!= 0){anexoWs.setModoFirma(anexo.getModoFirma());}

      //Confidencial
      if(anexo.getConfidencial()){
         anexoWs.setConfidencial(anexo.getConfidencial());
         if(StringUtils.isNotEmpty(anexo.getNombreFichero())){ anexoWs.setNombreFicheroAnexado(anexo.getNombreFichero());}
         // SICRES4 Deprecated
         if(anexo.getHash() != null){ anexoWs.setHash(anexo.getHash()); }
         anexoWs.setTamanoFichero(anexo.getTamanoFichero());
      }

      return anexoWs;

   }

   /**
    * Convierte un {@link es.caib.regweb3.model.utils.AnexoFull} en un {@link es.caib.regweb3.ws.model.AnexoWs}
    * @param anexoFull
    * @return
    * @throws Exception
    */
   public static AnexoWs transformarAnexoWs(AnexoFull anexoFull) throws Exception {

      AnexoWs anexoWs = transformarAnexoWs(anexoFull.getAnexo());

      // Firma Fichero Anexado
      if (anexoFull.getSignatureCustody() != null) {
         SignatureCustody sign = anexoFull.getSignatureCustody();
         anexoWs.setNombreFirmaAnexada(sign.getName());
         anexoWs.setTipoMIMEFirmaAnexada(sign.getMime());
         anexoWs.setFirmaAnexada(sign.getData());
      }

      // Documento fichero Anexado
      if (anexoFull.getDocumentoCustody() != null) {
         DocumentCustody doc = anexoFull.getDocumentoCustody();
         anexoWs.setNombreFicheroAnexado(doc.getName());
         anexoWs.setTipoMIMEFicheroAnexado(doc.getMime());
         anexoWs.setFicheroAnexado(doc.getData());
      }

      return anexoWs;
   }

}
