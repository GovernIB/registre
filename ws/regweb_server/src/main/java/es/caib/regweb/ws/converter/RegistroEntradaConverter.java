package es.caib.regweb.ws.converter;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.AnexoLocal;
import es.caib.regweb.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb.persistence.ejb.TipoAsuntoLocal;
import es.caib.regweb.persistence.utils.I18NLogicUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.model.AnexoWs;
import es.caib.regweb.ws.model.InteresadoWs;
import es.caib.regweb.ws.model.RegistroEntradaWs;
import es.caib.regweb.ws.v3.impl.CommonConverter;

import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 * Conversor entre las clases {@link es.caib.regweb.ws.model.RegistroEntradaWs} y {@link es.caib.regweb.model.RegistroEntrada}
 * @author earrivi
 */
public class RegistroEntradaConverter extends CommonConverter {

    /**
     * Convierte un {@link es.caib.regweb.ws.model.RegistroEntradaWs} en un {@link es.caib.regweb.model.RegistroEntrada}
     * @param registroEntradaWs
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public static RegistroEntrada getRegistroEntrada(RegistroEntradaWs registroEntradaWs,
        UsuarioEntidad usuario,Libro libro, Oficina oficina,  Organismo organismo,
        CodigoAsuntoLocal codigoAsuntoEjb, TipoAsuntoLocal tipoAsuntoEjb)
            throws Exception, I18NException {

        if (registroEntradaWs == null){
            return  null;
        }

        RegistroEntrada registroEntrada = new RegistroEntrada();
        RegistroDetalle registroDetalle = new RegistroDetalle();

        registroEntrada.setDestino(organismo);
        registroEntrada.setOficina(oficina);
        registroEntrada.setFecha(new Date());
        registroEntrada.setUsuario(usuario);
        registroEntrada.setEstado(RegwebConstantes.ESTADO_VALIDO);
        registroEntrada.setLibro(libro);

        registroDetalle.setTipoAsunto(getTipoAsunto(registroEntradaWs.getTipoAsunto(),usuario.getEntidad().getId(), tipoAsuntoEjb));
        registroDetalle.setCodigoAsunto(getCodigoAsunto(registroEntradaWs.getCodigoAsunto(), codigoAsuntoEjb));
        registroDetalle.setTipoDocumentacionFisica(registroEntradaWs.getDocFisica());
        registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(registroEntradaWs.getTipoTransporte()));
        registroDetalle.setIdioma(getIdiomaRegistro(registroEntradaWs.getIdioma()));
        registroDetalle.setExtracto(registroEntradaWs.getExtracto());
        registroDetalle.setAplicacion(registroEntradaWs.getAplicacion());
        registroDetalle.setVersion(registroEntradaWs.getVersion());
        registroDetalle.setReferenciaExterna(registroEntradaWs.getRefExterna());
        registroDetalle.setExpediente(registroEntradaWs.getNumExpediente());
        registroDetalle.setNumeroTransporte(registroEntradaWs.getNumTransporte());
        registroDetalle.setObservaciones(registroEntradaWs.getObservaciones());
        registroDetalle.setExpone(registroEntradaWs.getExpone());
        registroDetalle.setSolicita(registroEntradaWs.getSolicita());

        registroEntrada.setRegistroDetalle(registroDetalle);

        return registroEntrada;
    }

    public static RegistroEntradaWs getRegistroEntradaWs(RegistroEntrada registroEntrada,
        String idioma, AnexoLocal anexoEjb) throws Exception, I18NException {

        if (registroEntrada == null) {
            return null;
        }

        // Creamos los datos comunes mediante RegistroWs
        RegistroEntradaWs registroWs = new RegistroEntradaWs();
        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        registroWs.setFecha(registroEntrada.getFecha());
        registroWs.setNumero(registroEntrada.getNumeroRegistro());
        registroWs.setNumeroRegistroFormateado(registroEntrada.getNumeroRegistroFormateado());

        registroWs.setOficina(registroEntrada.getOficina().getDenominacion());
        registroWs.setLibro(registroEntrada.getLibro().getNombreCompleto());

        registroWs.setExtracto(registroDetalle.getExtracto());
        registroWs.setDocFisica(registroDetalle.getTipoDocumentacionFisica());

        registroWs.setIdioma(I18NLogicUtils.tradueix(new Locale(idioma), "idioma." + registroDetalle.getIdioma()));

        TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroDetalle.getTipoAsunto().getTraduccion(idioma);
        registroWs.setTipoAsunto(traduccionTipoAsunto.getNombre());

        registroWs.setAplicacion(registroDetalle.getAplicacion());
        registroWs.setVersion(registroDetalle.getVersion());

        registroWs.setCodigoUsuario(registroEntrada.getUsuario().getUsuario().getIdentificador());

        registroWs.setContactoUsuario("");

        registroWs.setNumExpediente(registroDetalle.getExpediente());
        registroWs.setNumTransporte(registroDetalle.getNumeroTransporte());
        registroWs.setObservaciones(registroDetalle.getObservaciones());

        registroWs.setRefExterna(registroDetalle.getReferenciaExterna());

        if(registroDetalle.getCodigoAsunto() != null){
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(idioma);
            registroWs.setCodigoAsunto(traduccionCodigoAsunto.getNombre());
        }else{
            registroWs.setCodigoAsunto(null);
        }

        if(registroDetalle.getTransporte() != null){
            registroWs.setTipoTransporte(I18NLogicUtils.tradueix(new Locale(idioma), "transporte." + registroDetalle.getTransporte()));
        }else{
            registroWs.setTipoTransporte(null);
        }


        registroWs.setExpone(registroDetalle.getExpone());
        registroWs.setSolicita(registroDetalle.getSolicita());

        //Interesados
        if(registroDetalle.getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroDetalle.getInteresados());

            registroWs.setInteresados(interesadosWs);
        }

        if(registroDetalle.getAnexos() != null){
            List<AnexoWs> anexosWs = procesarAnexosWs(registroDetalle.getAnexos(), anexoEjb);

            registroWs.setAnexos(anexosWs);
        }

        // Campos únicos de RegistroEntrada
        registroWs.setDestino(registroEntrada.getDestino().getDenominacion());

        return registroWs;

    }

    /**
     *
     * @param interesados
     * @return
     * @throws Exception
     */
    private static List<InteresadoWs> procesarInteresadosWs(List<Interesado> interesados) throws Exception{

        List<InteresadoWs> interesadosWs = new ArrayList<InteresadoWs>();

        for (Interesado interesado : interesados) {

            InteresadoWs interesadoWs =  DatosInteresadoConverter.getInteresadoWs(interesado);

            interesadosWs.add(interesadoWs);
        }

        return interesadosWs;
    }

}
