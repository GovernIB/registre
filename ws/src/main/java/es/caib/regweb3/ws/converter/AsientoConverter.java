package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.MimeTypeUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.AsientoWs;
import es.caib.regweb3.ws.model.FileContentWs;
import es.caib.regweb3.ws.model.FileInfoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;

public class AsientoConverter extends CommonConverter {

    /**
     * Transforma un {@link es.caib.regweb3.model.IRegistro} en un {@link es.caib.regweb3.ws.model.AsientoRegistralWs}
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws Exception
     */
    public static AsientoWs transformarRegistro(IRegistro registro, Long tipoRegistro,  String idioma) throws Exception{


        AsientoWs asientoWs = new AsientoWs(tipoRegistro);

        // Convertimos los campos comunes de AsientoWs
        setAsientoComun(asientoWs, registro,idioma);

        if(REGISTRO_ENTRADA.equals(tipoRegistro)){

            RegistroEntrada registroEntrada = (RegistroEntrada) registro;

            // Campos únicos de RegistroEntrada
            if(registroEntrada.getDestino() != null ){
                asientoWs.setCodigoDestino(registroEntrada.getDestino().getCodigo());
                asientoWs.setDenominacionDestino(registroEntrada.getDestino().getDenominacion());
            }else{
                asientoWs.setCodigoDestino(registroEntrada.getDestinoExternoCodigo());
                asientoWs.setDenominacionDestino(registroEntrada.getDestinoExternoDenominacion());
            }

        }else {

            RegistroSalida registroSalida = (RegistroSalida) registro;

            // Campos únicos de RegistroSalida
            if (registroSalida.getOrigen() != null) {
                asientoWs.setCodigoUnidadOrigen(registroSalida.getOrigen().getCodigo());
                asientoWs.setDenominacionUnidadOrigen(registroSalida.getOrigen().getDenominacion());
            } else {
                asientoWs.setCodigoUnidadOrigen(registroSalida.getOrigenExternoCodigo());
                asientoWs.setDenominacionUnidadOrigen(registroSalida.getOrigenExternoDenominacion());
            }
        }

        // Anexos
        asientoWs.setAnexos(transformarFilesInfoWs(registro.getRegistroDetalle()));

        //Justificante
        asientoWs.setJustificante(getJustificante(registro.getRegistroDetalle()));

        return asientoWs;
    }

    /**
     * Transforma un AnexoFull en un {@link es.caib.regweb3.ws.model.FileContentWs}
     * @param anexoFull
     * @param anexoEjb
     * @param entidad
     * @return
     */
    public static FileContentWs transformarFileContentWs(AnexoFull anexoFull, AnexoLocal anexoEjb, Entidad entidad, String idioma) {

        FileInfoWs fileInfoWs = transformarFileInfoWs(anexoFull);

        FileContentWs fileContentWs = new FileContentWs(fileInfoWs);

        fileContentWs.setFileInfoWs(fileInfoWs);

        try {

            if(!anexoFull.getAnexo().isPurgado()){

                // Obtenemos al url para descargar el documento
                String url = anexoEjb.getCsvValidationWeb(anexoFull.getAnexo(),entidad.getId());

                if(anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA ||
                   anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

                    fileContentWs.getFileInfoWs().setFilename(anexoFull.getDocFileName());

                }else if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED){

                    fileContentWs.getFileInfoWs().setFilename(anexoFull.getSignFileName());

                }

                if(StringUtils.isNotEmpty(url)){
                    fileContentWs.setUrl(url);
                }else{
                    byte[] data = anexoEjb.getArchivoContent(anexoFull.getAnexo(), entidad.getId());

                    if(data != null){
                        fileContentWs.setData(data);
                    }else{
                        fileContentWs.setError(I18NLogicUtils.tradueix(new Locale(idioma),"anexo.error.contenido"));
                    }
                }

            }else{

                fileContentWs.setError(I18NLogicUtils.tradueix(new Locale(idioma),"anexo.purgado"));
                // TODO Buscar anexos purgados en Arxiu???
            }


        } catch (I18NException | Exception e) {
            e.printStackTrace();
            fileContentWs.setError(I18NLogicUtils.tradueix(new Locale(idioma),"anexo.error") + e.getMessage());
        }

        return fileContentWs;
    }

    /**
     * Transforma los Anexos de un Registro en una Lista de {@link es.caib.regweb3.ws.model.FileInfoWs}
     * @param registroDetalle
     * @return
     */
    private static List<FileInfoWs> transformarFilesInfoWs(RegistroDetalle registroDetalle) {

        if(registroDetalle.getAnexosFull() != null){
            List<FileInfoWs> ficheros =  new ArrayList<>();

            for(AnexoFull anexoFull:registroDetalle.getAnexosFull()){
                // descartamos el justificante y los ficheros técnicos
                if(!anexoFull.getAnexo().isJustificante() && !RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO.equals(anexoFull.getAnexo().getTipoDocumento())){
                    ficheros.add(transformarFileInfoWs(anexoFull));
                }

            }
            return ficheros;
        }

        return null;
    }

    /**
     * Transforma un AnexoFull en un {@link es.caib.regweb3.ws.model.FileInfoWs}
     * @param anexoFull
     * @return
     */
    private static FileInfoWs transformarFileInfoWs(AnexoFull anexoFull) {

        FileInfoWs fileInfo = new FileInfoWs();
        fileInfo.setFileID(anexoFull.getAnexo().getId());
        fileInfo.setName(anexoFull.getAnexo().getTitulo());
        fileInfo.setConfidencial(anexoFull.getAnexo().getConfidencial());
        //Deprecated SICRES4
        fileInfo.setHash(anexoFull.getAnexo().getHash());
        if(anexoFull.getAnexo().getValidezDocumento()!=null){
            fileInfo.setValidezDocumento(anexoFull.getAnexo().getValidezDocumento().toString());
        }


        if(!anexoFull.getAnexo().getConfidencial()){//si no es confidencial

            if(anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA ||
                    anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

                fileInfo.setSize(anexoFull.getDocSize());
                fileInfo.setMime(anexoFull.getDocMime());

            }else if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED){

                fileInfo.setSize(anexoFull.getSignSize());
                fileInfo.setMime(anexoFull.getSignMime());
            }
        }else{
            fileInfo.setSize(new Long(anexoFull.getAnexo().getTamanoFichero()));
            fileInfo.setMime(MimeTypeUtils.getMimeTypeFileName(anexoFull.getAnexo().getNombreFichero()));
        }


        return fileInfo;
    }

    /**
     * Obtiene el Justificante y lo transforma en un {@link es.caib.regweb3.ws.model.FileInfoWs}
     * @param registroDetalle
     * @return
     */
    private static FileInfoWs getJustificante(RegistroDetalle registroDetalle) {

        if(registroDetalle.getAnexosFull() != null){

            for(AnexoFull anexoFull:registroDetalle.getAnexosFull()){

                if(anexoFull.getAnexo().isJustificante()){

                    return transformarFileInfoWs(anexoFull);
                }
            }
        }
        return null;
    }


    /**
     * Obtiene un {@link AsientoWs}, a partir de un {@link RegistroDetalle}
     * @param asiento
     * @param registro
     * @throws Exception
     */
    private static void setAsientoComun(AsientoWs asiento, IRegistro registro, String idioma) throws Exception{

        RegistroDetalle registroDetalle = registro.getRegistroDetalle();

        if (StringUtils.isNotEmpty(registro.getNumeroRegistroFormateado())) { asiento.setNumeroRegistro(registro.getNumeroRegistroFormateado());}
        if (registro.getFecha() != null) { asiento.setFechaRegistro(registro.getFecha());}
        if (StringUtils.isNotEmpty(registroDetalle.getExtracto())) { asiento.setExtracto(registroDetalle.getExtracto());}
        if (registroDetalle.getTipoDocumentacionFisica() != null) { asiento.setTipoDocumetacionFisica(I18NLogicUtils.tradueix(new Locale(idioma), "tipoDocumentacionFisica." + registroDetalle.getTipoDocumentacionFisica()));}
        if (registroDetalle.getIdioma() != null) { asiento.setIdioma(registroDetalle.getIdioma());}
        if (registroDetalle.getPresencial()!= null) {asiento.setPresencial(registroDetalle.getPresencial());}

        //estado
        if(registro.getEstado()!=null ){ asiento.setEstado(registro.getEstado());}

        //motivo
        if(StringUtils.isNotEmpty(registroDetalle.getDecodificacionTipoAnotacion())){ asiento.setDescripcionEstado(registroDetalle.getDecodificacionTipoAnotacion());}


        // Oficina Origen
        if(registroDetalle.getOficinaOrigen() != null){
            asiento.setCodigoOficinaOrigen(registroDetalle.getOficinaOrigen().getCodigo());
            asiento.setDenominacionOficinaOrigen(registroDetalle.getOficinaOrigen().getDenominacion());
        }else{
            asiento.setCodigoOficinaOrigen(registroDetalle.getOficinaOrigenExternoCodigo());
            asiento.setDenominacionOficinaOrigen(registroDetalle.getOficinaOrigenExternoDenominacion());
        }

        if (StringUtils.isNotEmpty(registroDetalle.getExpone())) { asiento.setExpone(registroDetalle.getExpone());}
        if (StringUtils.isNotEmpty(registroDetalle.getSolicita())) { asiento.setSolicita(registroDetalle.getSolicita());}
        if (registroDetalle.getCodigoSia() != null) { asiento.setCodigoSia(registroDetalle.getCodigoSia());}


        //Interesados
        if(registroDetalle.getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroDetalle.getInteresados());

            asiento.setInteresados(interesadosWs);
        }
    }
}
