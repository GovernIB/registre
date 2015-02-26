package es.caib.regweb.ws.converter;

import es.caib.regweb.model.Anexo;
import es.caib.regweb.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;
import es.caib.regweb.ws.model.AnexoWs;
import es.caib.regweb.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

/**
 * Created 1/12/14 13:45
 *
 * @author mgonzalez
 */
public class AnexoConverter extends CommonConverter {

  /**
   * Convierte un {@link es.caib.regweb.ws.model.AnexoWs} en un {@link es.caib.regweb.model.Anexo}
   * @param anexoWs
   * @return
   * @throws Exception
   * @throws I18NException
   */
   public static Anexo getAnexo(AnexoWs anexoWs, TipoDocumentalLocal tipoDocumentalEjb       
       ) throws Exception, I18NException {

        if (anexoWs == null){
            return  null;
        }

        Anexo anexo = procesarAnexo(anexoWs, tipoDocumentalEjb);


        return  anexo;
    }

    private static Anexo procesarAnexo(AnexoWs anexoWs, 
        TipoDocumentalLocal tipoDocumentalEjb
         ) throws Exception, I18NException{

        Anexo anexo = new Anexo();




       if(!StringUtils.isEmpty(anexoWs.getTitulo())){anexo.setTitulo(anexoWs.getTitulo());}

       if(anexoWs.getTipoDocumental()!= null){anexo.setTipoDocumental(getTipoDocumental(anexoWs.getTipoDocumental(), tipoDocumentalEjb));}
       if(anexoWs.getValidezDocumento()!= null) {
         anexo.setValidezDocumento(getTipoValidezDocumento(anexoWs.getValidezDocumento()));
       }
       if(anexoWs.getTipoDocumento()!= null) {
         anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_BY_CODIGO_NTI.get(anexoWs.getTipoDocumento()));
       }
       if(!StringUtils.isEmpty(anexoWs.getObservaciones())){anexo.setObservaciones(anexoWs.getObservaciones());}
       if(anexoWs.getOrigenCiudadanoAdmin()!=null){anexo.setOrigenCiudadanoAdmin(anexoWs.getOrigenCiudadanoAdmin());}
       if(anexoWs.getCertificado() != null){anexo.setCertificado(anexoWs.getCertificado());}
       if(anexoWs.getFirmacsv() != null){anexo.setFirmacsv(anexoWs.getFirmacsv());}
       if(anexoWs.getTimestamp() != null){anexo.setTimestamp(anexoWs.getTimestamp());}
       if(anexoWs.getValidacionOCSP() != null){anexo.setValidacionOCSP(anexoWs.getValidacionOCSP());}


       // Part de fichero Anexado
       if(!StringUtils.isEmpty(anexoWs.getNombreFicheroAnexado())){anexo.setNombreFicheroAnexado(StringUtils.recortarNombre(anexoWs.getNombreFicheroAnexado(), RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH));}
       if(!StringUtils.isEmpty(anexoWs.getTipoMIMEFicheroAnexado())){anexo.setTipoMIME(anexoWs.getTipoMIMEFicheroAnexado());}
       anexo.setTamano(anexoWs.getTamanoFicheroAnexado());
       if(anexoWs.getFechaCaptura()!= null){anexo.setFechaCaptura(anexoWs.getFechaCaptura().getTime());}

       // Part de firma Anexada
       if(anexoWs.getModoFirma()!=null){anexo.setModoFirma(anexoWs.getModoFirma());}
       if(!StringUtils.isEmpty(anexoWs.getNombreFirmaAnexada())){anexo.setNombreFirmaAnexada(StringUtils.recortarNombre(anexoWs.getNombreFirmaAnexada(), RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH));}
       anexo.setTamano(anexoWs.getTamanoFicheroAnexado());



        return anexo;

    }


}
