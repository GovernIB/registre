package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.model.AsientoWs;
import es.caib.regweb3.ws.model.FileInfoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;

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
    public static AsientoWs transformarRegistro(IRegistro registro, Long tipoRegistro, String idioma) throws Exception{


        AsientoWs asientoWs = new AsientoWs(tipoRegistro);

        // Convertimos los campos comunes de AsientoWs
        setAsientoComun(asientoWs, registro, idioma);

        if(REGISTRO_ENTRADA.equals(tipoRegistro)){

            RegistroEntrada registroEntrada = (RegistroEntrada) registro;

            // Campos únicos de RegistroEntrada
            if(registroEntrada.getDestino() != null ){
                asientoWs.setCodigoDestino(registroEntrada.getDestino().getCodigo());
                asientoWs.setDenominacionDestino(registroEntrada.getDestino().getDenominacion());
            }else{
                asientoWs.setCodigoDestino(registroEntrada.getDestinoExternoCodigo());
                asientoWs.setCodigoDestino(registroEntrada.getDestinoExternoDenominacion());
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

    private static List<FileInfoWs> transformarFilesInfoWs(RegistroDetalle registroDetalle) {

        if(registroDetalle.getAnexosFull() != null){
            List<FileInfoWs> ficheros =  new ArrayList<>();

            for(AnexoFull anexoFull:registroDetalle.getAnexosFull()){

                ficheros.add(transformarFileInfoWs(anexoFull));
            }
            return ficheros;
        }

        return null;
    }

    private static FileInfoWs transformarFileInfoWs(AnexoFull anexoFull) {

        FileInfoWs fileInfo = new FileInfoWs();
        fileInfo.setFileID(anexoFull.getAnexo().getId());
        fileInfo.setName(anexoFull.getAnexo().getTitulo());

        if(anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA ||
                anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

            fileInfo.setSize(anexoFull.getDocSize());
            fileInfo.setMime(anexoFull.getDocMime());

        }else if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED){

            fileInfo.setSize(anexoFull.getSignSize());
            fileInfo.setMime(anexoFull.getSignMime());

        }

        return fileInfo;
    }

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

        if (registro.getNumeroRegistroFormateado() != null) { asiento.setNumeroRegistro(registro.getNumeroRegistroFormateado());}
        if (registro.getFecha() != null) { asiento.setFechaRegistro(registro.getFecha());}
        if (registroDetalle.getExtracto() != null) { asiento.setExtracto(registroDetalle.getExtracto());}
        if (registroDetalle.getTipoDocumentacionFisica() != null) { asiento.setTipoDocumetacionFisica(I18NLogicUtils.tradueix(new Locale(idioma), "tipoDocumentacionFisica." + registroDetalle.getTipoDocumentacionFisica())
        );}

        //if (registroDetalle.getIdioma() != null) { asiento.setIdioma(registroDetalle.getIdioma());}

        // Oficina Origen
        if(registroDetalle.getOficinaOrigen() != null){
            asiento.setCodigoOficinaOrigen(registroDetalle.getOficinaOrigen().getCodigo());
            asiento.setDenominacionOficinaOrigen(registroDetalle.getOficinaOrigen().getDenominacion());
        }else{
            asiento.setCodigoOficinaOrigen(registroDetalle.getOficinaOrigenExternoCodigo());
            asiento.setDenominacionOficinaOrigen(registroDetalle.getOficinaOrigenExternoDenominacion());
        }

        if (registroDetalle.getExpone() != null) { asiento.setExpone(registroDetalle.getExpone());}
        if (registroDetalle.getSolicita() != null) { asiento.setSolicita(registroDetalle.getSolicita());}
        if (registroDetalle.getCodigoSia() != null) { asiento.setCodigoSia(registroDetalle.getCodigoSia());}


        //Interesados
        if(registroDetalle.getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroDetalle.getInteresados());

            asiento.setInteresados(interesadosWs);
        }
    }
}
