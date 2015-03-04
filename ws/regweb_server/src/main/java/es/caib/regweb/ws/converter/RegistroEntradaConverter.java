package es.caib.regweb.ws.converter;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb.persistence.ejb.TipoAsuntoLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.model.InteresadoWs;
import es.caib.regweb.ws.model.RegistroEntradaWs;
import es.caib.regweb.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        registroDetalle.setTipoAsunto(getTipoAsunto(registroEntradaWs.getTipoAsunto(), tipoAsuntoEjb));
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

    public static RegistroEntradaWs getRegistroEntradaWs(RegistroEntrada registroEntrada) throws Exception{

        if (registroEntrada == null) {
            return null;
        }

        // Creamos el RegistroEntradaWs
        


        // Creamos los datos comunes mediante RegistroWs
        RegistroEntradaWs registroWs = new RegistroEntradaWs();
        registroWs.setFecha(registroEntrada.getFecha());
        registroWs.setNumero(registroEntrada.getNumeroRegistro());
        registroWs.setNumeroRegistroFormateado(registroEntrada.getNumeroRegistroFormateado());

        registroWs.setOficina(registroEntrada.getOficina().getCodigo());
        registroWs.setLibro(registroEntrada.getLibro().getCodigo());

        registroWs.setExtracto(registroEntrada.getRegistroDetalle().getExtracto());
        registroWs.setDocFisica(registroEntrada.getRegistroDetalle().getTipoDocumentacionFisica());
        registroWs.setIdioma(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(registroEntrada.getRegistroDetalle().getIdioma()));
        registroWs.setTipoAsunto(registroEntrada.getRegistroDetalle().getTipoAsunto().getCodigo());

        registroWs.setAplicacion(registroEntrada.getRegistroDetalle().getAplicacion());
        registroWs.setVersion(registroEntrada.getRegistroDetalle().getVersion());

        registroWs.setCodigoUsuario(registroEntrada.getUsuario().getUsuario().getIdentificador());

        registroWs.setContactoUsuario("");

        registroWs.setNumExpediente(registroEntrada.getRegistroDetalle().getExpediente());
        registroWs.setNumTransporte(registroEntrada.getRegistroDetalle().getNumeroTransporte());
        registroWs.setObservaciones(registroEntrada.getRegistroDetalle().getObservaciones());

        registroWs.setRefExterna(registroEntrada.getRegistroDetalle().getReferenciaExterna());

        if(registroEntrada.getRegistroDetalle().getCodigoAsunto() != null){
            registroWs.setCodigoAsunto(registroEntrada.getRegistroDetalle().getCodigoAsunto().getCodigo());
        }else{
            registroWs.setCodigoAsunto(null);
        }

        if(registroEntrada.getRegistroDetalle().getTransporte() != null){
            registroWs.setTipoTransporte(RegwebConstantes.CODIGO_SICRES_BY_TRANSPORTE.get(registroEntrada.getRegistroDetalle().getTransporte()));
        }else{
            registroWs.setTipoTransporte(null);
        }


        registroWs.setExpone(registroEntrada.getRegistroDetalle().getExpone());
        registroWs.setSolicita(registroEntrada.getRegistroDetalle().getSolicita());

        //Interesados
        if(registroEntrada.getRegistroDetalle().getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroEntrada.getRegistroDetalle().getInteresados());

            registroWs.setInteresados(interesadosWs);
        }

        //Anexos todo Marilen!



        // Asociamos el RegistroWs a RegistroEntradaWs
        RegistroEntradaWs registroEntradaWs = registroWs;
        registroEntradaWs.setDestino(registroEntrada.getDestino().getCodigo());

        return registroEntradaWs;

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
