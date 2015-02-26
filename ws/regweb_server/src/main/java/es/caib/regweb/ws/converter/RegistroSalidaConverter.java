package es.caib.regweb.ws.converter;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb.persistence.ejb.IdiomaRegistroLocal;
import es.caib.regweb.persistence.ejb.TipoAsuntoLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.model.InteresadoWs;
import es.caib.regweb.ws.model.RegistroSalidaWs;
import es.caib.regweb.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Conversor entre las clases {@link es.caib.regweb.ws.model.RegistroSalidaWs} y {@link es.caib.regweb.model.RegistroSalida}
 * @author earrivi
 */
public class RegistroSalidaConverter extends CommonConverter {

    /**
     * Convierte un {@link es.caib.regweb.ws.model.RegistroSalidaWs} en un {@link es.caib.regweb.model.RegistroSalida}
     * @param registroSalidaWs
     * @return
     * @throws Exception
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
     */
    public static RegistroSalida getRegistroSalida(RegistroSalidaWs registroSalidaWs,
        UsuarioEntidad usuario,Libro libro, Oficina oficina,
        Organismo organismo,IdiomaRegistroLocal idiomaRegistroEjb,
        CodigoAsuntoLocal codigoAsuntoEjb, TipoAsuntoLocal tipoAsuntoEjb) throws Exception, I18NException{

        if (registroSalidaWs == null){
            return  null;
        }

        RegistroSalida registroSalida = new RegistroSalida();
        RegistroDetalle registroDetalle = new RegistroDetalle();

        registroSalida.setOrigen(organismo);
        registroSalida.setOficina(oficina);
        registroSalida.setFecha(new Date());
        registroSalida.setUsuario(usuario);
        registroSalida.setEstado(RegwebConstantes.ESTADO_VALIDO);
        registroSalida.setLibro(libro);

        registroDetalle.setTipoAsunto(getTipoAsunto(registroSalidaWs.getTipoAsunto(), tipoAsuntoEjb));
        registroDetalle.setCodigoAsunto(getCodigoAsunto(registroSalidaWs.getCodigoAsunto(), codigoAsuntoEjb));
        registroDetalle.setTipoDocumentacionFisica(registroSalidaWs.getDocFisica());
        registroDetalle.setTransporte(RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(registroSalidaWs.getTipoTransporte()));
        registroDetalle.setIdioma(getIdiomaRegistro(registroSalidaWs.getIdioma(), idiomaRegistroEjb));
        registroDetalle.setExtracto(registroSalidaWs.getExtracto());
        registroDetalle.setAplicacion(registroSalidaWs.getAplicacion());
        registroDetalle.setVersion(registroSalidaWs.getVersion());
        registroDetalle.setReferenciaExterna(registroSalidaWs.getRefExterna());
        registroDetalle.setExpediente(registroSalidaWs.getNumExpediente());
        registroDetalle.setNumeroTransporte(registroSalidaWs.getNumTransporte());
        registroDetalle.setObservaciones(registroSalidaWs.getObservaciones());
        registroDetalle.setExpone(registroSalidaWs.getExpone());
        registroDetalle.setSolicita(registroSalidaWs.getSolicita());

        registroSalida.setRegistroDetalle(registroDetalle);

        return registroSalida;
    }

    public static RegistroSalidaWs getRegistroSalidaWs(RegistroSalida registroSalida) throws Exception{

        if (registroSalida == null) {
            return null;
        }

       

        // Creamos los datos comunes mediante RegistroWs
        RegistroSalidaWs registroWs = new RegistroSalidaWs();

        registroWs.setFecha(registroSalida.getFecha());
        registroWs.setNumero(registroSalida.getNumeroRegistro());
        registroWs.setNumeroRegistroFormateado(registroSalida.getNumeroRegistroFormateado());

        registroWs.setOficina(registroSalida.getOficina().getCodigo());
        registroWs.setLibro(registroSalida.getLibro().getCodigo());

        registroWs.setExtracto(registroSalida.getRegistroDetalle().getExtracto());
        registroWs.setDocFisica(registroSalida.getRegistroDetalle().getTipoDocumentacionFisica());
        registroWs.setIdioma(registroSalida.getRegistroDetalle().getIdioma().getCodigo());
        registroWs.setTipoAsunto(registroSalida.getRegistroDetalle().getTipoAsunto().getCodigo());

        registroWs.setAplicacion(registroSalida.getRegistroDetalle().getAplicacion());
        registroWs.setVersion(registroSalida.getRegistroDetalle().getVersion());

        registroWs.setCodigoUsuario(registroSalida.getUsuario().getUsuario().getIdentificador());

        registroWs.setContactoUsuario("");

        registroWs.setNumExpediente(registroSalida.getRegistroDetalle().getExpediente());
        registroWs.setNumTransporte(registroSalida.getRegistroDetalle().getNumeroTransporte());
        registroWs.setObservaciones(registroSalida.getRegistroDetalle().getObservaciones());

        registroWs.setRefExterna(registroSalida.getRegistroDetalle().getReferenciaExterna());

        if(registroSalida.getRegistroDetalle().getCodigoAsunto() != null){
            registroWs.setCodigoAsunto(registroSalida.getRegistroDetalle().getCodigoAsunto().getCodigo());
        }else{
            registroWs.setCodigoAsunto(null);
        }

        if(registroSalida.getRegistroDetalle().getTransporte() != null){
            registroWs.setTipoTransporte(RegwebConstantes.CODIGO_SICRES_BY_TRANSPORTE.get(registroSalida.getRegistroDetalle().getTransporte()));
        }else{
            registroWs.setTipoTransporte(null);
        }


        registroWs.setExpone(registroSalida.getRegistroDetalle().getExpone());
        registroWs.setSolicita(registroSalida.getRegistroDetalle().getSolicita());


        //Interesados
        if(registroSalida.getRegistroDetalle().getInteresados() != null){
            List<InteresadoWs> interesadosWs = procesarInteresadosWs(registroSalida.getRegistroDetalle().getInteresados());

            registroWs.setInteresados(interesadosWs);
        }
        
        // Creamos el RegistroSalidaWs
        RegistroSalidaWs registroSalidaWs = registroWs;
        registroSalidaWs.setOrigen(registroSalida.getOrigen().getCodigo());

        return registroSalidaWs;

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
