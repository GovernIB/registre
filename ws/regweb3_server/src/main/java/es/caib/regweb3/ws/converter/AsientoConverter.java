package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.OficioRemisionLocal;
import es.caib.regweb3.persistence.ejb.TrazabilidadSirLocal;
import es.caib.regweb3.ws.model.AsientoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;

import java.util.List;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;

public class AsientoConverter extends CommonConverter {

    /**
     * Transforma un {@link es.caib.regweb3.model.IRegistro} en un {@link es.caib.regweb3.ws.model.AsientoRegistralWs}
     * @param registro
     * @param tipoRegistro
     * @param entidad
     * @param idioma
     * @param oficioRemisionEjb
     * @param trazabilidadEjb
     * @return
     * @throws Exception
     */
    public static AsientoWs transformarRegistro(IRegistro registro, Long tipoRegistro, Entidad entidad, String idioma, OficioRemisionLocal oficioRemisionEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception{


        AsientoWs asientoRegistral = new AsientoWs(tipoRegistro);

        // Convertimos los campos comunes de AsientoWs
        setAsientoComun(asientoRegistral, registro, entidad, idioma, oficioRemisionEjb, trazabilidadEjb);

        if(REGISTRO_ENTRADA.equals(tipoRegistro)){

            RegistroEntrada registroEntrada = (RegistroEntrada) registro;

            // Campos únicos de RegistroEntrada
            if(registroEntrada.getDestino() != null ){
                asientoRegistral.setCodigoDestino(registroEntrada.getDestino().getCodigo());
                asientoRegistral.setDenominacionDestino(registroEntrada.getDestino().getDenominacion());
            }else{
                asientoRegistral.setCodigoDestino(registroEntrada.getDestinoExternoCodigo());
                asientoRegistral.setCodigoDestino(registroEntrada.getDestinoExternoDenominacion());
            }

        }else {

            RegistroSalida registroSalida = (RegistroSalida) registro;

            // Campos únicos de RegistroSalida
            if (registroSalida.getOrigen() != null) {
                asientoRegistral.setCodigoUnidadOrigen(registroSalida.getOrigen().getCodigo());
                asientoRegistral.setDenominacionUnidadOrigen(registroSalida.getOrigen().getDenominacion());
            } else {
                asientoRegistral.setCodigoUnidadOrigen(registroSalida.getOrigenExternoCodigo());
                asientoRegistral.setDenominacionUnidadOrigen(registroSalida.getOrigenExternoDenominacion());
            }
        }

        // Anexos
        asientoRegistral.setAnexos(transformarFileInfoWs(registro.getRegistroDetalle()));

        return asientoRegistral;
    }


    /**
     * Obtiene un {@link AsientoWs}, a partir de un {@link RegistroDetalle}
     * @param asiento
     * @param registro
     * @param entidad
     * @param idioma
     * @param oficioRemisionEjb
     * @param trazabilidadEjb
     * @throws Exception
     */
    private static void setAsientoComun(AsientoWs asiento, IRegistro registro, Entidad entidad, String idioma, OficioRemisionLocal oficioRemisionEjb, TrazabilidadSirLocal trazabilidadEjb) throws Exception{

        RegistroDetalle registroDetalle = registro.getRegistroDetalle();

        if (registro.getNumeroRegistroFormateado() != null) { asiento.setNumeroRegistro(registro.getNumeroRegistroFormateado());}
        if (registro.getFecha() != null) { asiento.setFechaRegistro(registro.getFecha());}
        if (registroDetalle.getExtracto() != null) { asiento.setExtracto(registroDetalle.getExtracto());}
        if (registroDetalle.getTipoDocumentacionFisica() != null) { asiento.setTipoDocumetacionFisica(registroDetalle.getTipoDocumentacionFisica());}
        if (registroDetalle.getIdioma() != null) { asiento.setIdioma(registroDetalle.getIdioma());}

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
