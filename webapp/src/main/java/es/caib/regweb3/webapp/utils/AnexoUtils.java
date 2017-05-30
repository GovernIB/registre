package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.io.FilenameUtils;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earrivi on 30/05/2017.
 */
public class AnexoUtils {


    /**
     *
     * @param anexos
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public static List<String> validarAnexosSir(List<AnexoFull> anexos, Long idEntidad) throws Exception{

        List<String> mensajesError =  new ArrayList<String>();

        Long tamanyoMaximoTotalAnexos = PropiedadGlobalUtil.getMaxUploadSizeTotal(idEntidad);
        String extensionesPermitidas = PropiedadGlobalUtil.getFormatosPermitidos(idEntidad);

        // Número máximo de Anexos permitidos
        if(anexos.size() > PropiedadGlobalUtil.getMaxAnexosPermitidos(idEntidad)){
            mensajesError.add(I18NUtils.tradueix("anexo.numeroMaximo.superado"));
            return mensajesError;
        }

        // Tamaño total de todos los anexos
        if(obtenerTamanoTotalAnexos(anexos) > tamanyoMaximoTotalAnexos){
            mensajesError.add(I18NUtils.tradueix("anexo.tamanoMaximo.superado"));
            return mensajesError;
        }

        // Extensiones de cada uno de los anexos
        for (AnexoFull anexoFull:anexos){
            String extension;

            switch (anexoFull.getAnexo().getModoFirma()){

                case RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED:

                    extension = AnexoUtils.obtenerExtensionAnexo(anexoFull.getSignatureCustody().getName());
                    if(!extensionesPermitidas.contains(extension)){
                        mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido",extension,anexoFull.getAnexo().getTitulo()));
                    }
                    break;

                case RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED:

                    extension = AnexoUtils.obtenerExtensionAnexo(anexoFull.getSignatureCustody().getName());
                    if(!extensionesPermitidas.contains(extension)){
                        mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido",extension,anexoFull.getAnexo().getTitulo()));
                    }
                    extension = AnexoUtils.obtenerExtensionAnexo(anexoFull.getDocumentoCustody().getName());
                    if(!extensionesPermitidas.contains(extension)){
                        mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido",extension,anexoFull.getAnexo().getTitulo()));
                    }
                    break;

                case RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA:

                    extension = AnexoUtils.obtenerExtensionAnexo(anexoFull.getDocumentoCustody().getName());
                    if(!extensionesPermitidas.contains(extension)){
                        mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido",extension,anexoFull.getAnexo().getTitulo()));
                    }
                    break;
            }
        }

        return mensajesError;

    }

    /**
     * Calcula el tamaño total de los anexos que nos pasan en la lista
     * @param anexosFull
     * @return
     */
    public static long obtenerTamanoTotalAnexos(List<AnexoFull> anexosFull) throws Exception{
        long tamanyoTotalAnexos = 0;
        long tamanyoanexo = 0;
        for (AnexoFull anexoFull : anexosFull) {
            //Obtenemos los bytes del documento que representa el anexo, en el caso 4 Firma Attached,
            // el documento está en SignatureCustody
            DocumentCustody dc = anexoFull.getDocumentoCustody();
            if (dc != null) {//Si documentCustody es null tenemos que coger SignatureCustody.
                tamanyoanexo = anexoFull.getDocumentoCustody().getLength();
            } else {
                SignatureCustody sc = anexoFull.getSignatureCustody();
                if (sc != null) {
                    tamanyoanexo = anexoFull.getSignatureCustody().getLength();
                }
            }
            tamanyoTotalAnexos += tamanyoanexo;
        }

        return tamanyoTotalAnexos;

    }

    /**
     * Obtiene la extensión del anexo introducido en el formulario
     * @param fileName
     * @return
     */
    public static String obtenerExtensionAnexo(String fileName){

        return FilenameUtils.getExtension(fileName);
    }
}
