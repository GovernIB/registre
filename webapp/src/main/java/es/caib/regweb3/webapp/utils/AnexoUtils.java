package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.io.FilenameUtils;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.springframework.web.util.UriUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * Created by earrivi on 30/05/2017.
 */
public class AnexoUtils {


    /**
     *
     * @param anexos
     * @return
     * @throws Exception
     */
    public static List<String> validarAnexosSir(List<AnexoFull> anexos) throws Exception{

        List<String> mensajesError =  new ArrayList<String>();

        Long tamanyoMaximoTotalAnexos = PropiedadGlobalUtil.getTamanoMaxTotalAnexosSir();
        String extensionesPermitidas = PropiedadGlobalUtil.getFormatosAnexosSir();

        // Número máximo de Anexos permitidos
        if(anexos.size() > PropiedadGlobalUtil.getNumeroMaxAnexosSir()){
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
                    if(anexoFull.getSignatureCustody()!=null) {
                        extension = obtenerExtensionAnexo(anexoFull.getSignatureCustody().getName());
                        if (!extensionesPermitidas.contains(extension)) {
                            mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido", extension, anexoFull.getAnexo().getTitulo()));
                        }
                    }else{
                        mensajesError.add(I18NUtils.tradueix("anexo.error.noattached"));
                    }
                    break;

                case RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED:

                    if(anexoFull.getSignatureCustody()!=null) {
                        extension = obtenerExtensionAnexo(anexoFull.getSignatureCustody().getName());
                        if (!extensionesPermitidas.contains(extension)) {
                            mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido", extension, anexoFull.getAnexo().getTitulo()));
                        }
                    }else{
                        mensajesError.add(I18NUtils.tradueix("anexo.error.nofirma"));
                    }
                    if(anexoFull.getDocumentoCustody()!=null) {
                        extension = obtenerExtensionAnexo(anexoFull.getDocumentoCustody().getName());
                        if (!extensionesPermitidas.contains(extension)) {
                            mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido", extension, anexoFull.getAnexo().getTitulo()));
                        }
                    }else{
                        mensajesError.add(I18NUtils.tradueix("anexo.error.nodoc"));
                    }
                    break;

                case RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA:

                    if(anexoFull.getDocumentoCustody()!=null) {
                        extension = obtenerExtensionAnexo(anexoFull.getDocumentoCustody().getName());
                        if (!extensionesPermitidas.contains(extension)) {
                            mensajesError.add(I18NUtils.tradueix("anexo.formato.nopermitido", extension, anexoFull.getAnexo().getTitulo()));
                        }
                    }else{
                        mensajesError.add(I18NUtils.tradueix("anexo.error.nodoc"));
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

        return FilenameUtils.getExtension(fileName).toLowerCase();
    }



    /**
     * Retorna el contingut de la capçalera <i>Content-Disposition</i> que compleix
     * amb https://tools.ietf.org/html/rfc6266#section-5
     * @param attachment si és true empra attachment, sinó inline
     * @param filename nom del fitxer suggerit
     * @return contingut de la capçalera.
     * @throws Exception
     */
    public static String getContentDispositionHeader(boolean attachment, String filename)
       throws Exception {

        // Preparam el nom suggerit en UTF-8
        // No podem emprar URLEncoder.encode perquè només és per paràmetres http i
        // converteix els espais a "+" enlloc de a "%20"
        String utf8filename = UriUtils.encodePath(filename, "UTF-8");

        // Asseguram que el filename suggerit en ISO-8859-1 no té caràcters incompatibles
        CharsetEncoder charsetEncoder = ISO_8859_1.newEncoder();
        if (!charsetEncoder.canEncode(filename)) {
            charsetEncoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            charsetEncoder.replaceWith("_".getBytes(ISO_8859_1) );
            ByteBuffer buffer = charsetEncoder.encode(CharBuffer.wrap(filename));
            filename = new String(buffer.array(), ISO_8859_1);
        }

        // Implementació de RFC6266. La majoria de navegadors soporten la codificació en UTF-8 emprant
        // "filename*=", pels que no ho soportin, ficam "filename=" abans.
        return (attachment ? "attachment" : "inline")
           + "; filename=\"" + filename + "\""
           + "; filename*=UTF-8''" + utf8filename;
    }
}
