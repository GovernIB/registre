package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.sir.TipoMensaje;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 06/03/18
 */

@Stateless(name = "MensajeControlEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MensajeControlBean extends BaseEjbJPA<MensajeControl, Long> implements MensajeControlLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private RegistroSirLocal registroSirEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private OficinaLocal oficinaEjb;


    @Override
    public MensajeControl getReference(Long id) throws Exception {

        return em.getReference(MensajeControl.class, id);
    }

    @Override
    public MensajeControl findById(Long id) throws Exception {

        return em.find(MensajeControl.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MensajeControl> getAll() throws Exception {

        return  em.createQuery("Select mc from MensajeControl as mc order by mc.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(mc.id) from MensajeControl as mc");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MensajeControl> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select mc from MensajeControl as mc order by mc.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MensajeControl> getByEntidad(Long idEntidad) throws Exception{

        Query q = em.createQuery("Select mc from MensajeControl as mc where mc.entidad.id = :idEntidad order by mc.id");
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, MensajeControl mensajeControl, Entidad entidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select mensaje from MensajeControl as mensaje ");

        // Entidad
        where.add(" mensaje.entidad.id = :idEntidad "); parametros.put("idEntidad",entidad.getId());

        // Tipo Mensaje Control
        if(StringUtils.isNotEmpty(mensajeControl.getTipoMensaje())){
            where.add(" mensaje.tipoMensaje = :tipoMensaje "); parametros.put("tipoMensaje",mensajeControl.getTipoMensaje());
        }

        // Tipo Comunicación
        if(mensajeControl.getTipoComunicacion() != null){
            where.add(" mensaje.tipoComunicacion = :tipoComunicacion "); parametros.put("tipoComunicacion",mensajeControl.getTipoComunicacion());
        }

        // Identificador Intercambio
        if(StringUtils.isNotEmpty(mensajeControl.getIdentificadorIntercambio())){
            where.add(DataBaseUtils.like("mensaje.identificadorIntercambio", "identificadorIntercambio", parametros, mensajeControl.getIdentificadorIntercambio()));
        }

        // Intervalo fechas
        where.add(" (mensaje.fecha >= :fechaInicio  "); parametros.put("fechaInicio", fechaInicio);
        where.add(" mensaje.fecha <= :fechaFin) "); parametros.put("fechaFin", fechaFin);

        // Parametros
        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select mensaje from MensajeControl as mensaje ", "Select count(mensaje.id) from MensajeControl as mensaje "));
            query.append(" order by mensaje.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select mensaje from MensajeControl as mensaje ", "Select count(mensaje.id) from MensajeControl as mensaje "));
            query.append("order by mensaje.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

        if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }

        List<MensajeControl> mensajes = q.getResultList();

        paginacion.setListado(mensajes);

        return paginacion;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MensajeControl> getByIdentificadorIntercambio(String identificadorIntercambio, Long idEntidad) throws Exception{

        Query q = em.createQuery("Select mc from MensajeControl as mc where " +
                "mc.entidad.id = :idEntidad and mc.identificadorIntercambio =:identificadorIntercambio order by mc.id");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("identificadorIntercambio",identificadorIntercambio);

        return q.getResultList();

    }

    /**
     * Realiza las acciones pertinentes cuando se recibie un mensaje de control
     * @param mensaje
     * @throws Exception
     */
    @Override
    public void procesarMensajeDatosControl(MensajeControl mensaje) throws Exception{

        // Comprobamos que el destino pertenece a alguna de las Entidades configuradas
        Entidad entidad = comprobarEntidadMensajeControl(mensaje.getCodigoEntidadRegistralDestino());

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Recepción MensajeControl: " + TipoMensaje.getTipoMensaje(mensaje.getTipoMensaje()).getName();
        peticion.append("IdentificadorIntercambio: ").append(mensaje.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(mensaje.getCodigoEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(mensaje.getCodigoEntidadRegistralDestino()).append(System.getProperty("line.separator"));
        peticion.append("Descripcion: ").append(mensaje.getDescripcionMensaje()).append(System.getProperty("line.separator"));

        // Mensaje ACK
        if(mensaje.getTipoMensaje().equals(TipoMensaje.ACK.getValue())){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio(), mensaje.getCodigoEntidadRegistralDestino());
            RegistroSir registroSir = registroSirEjb.getRegistroSir(mensaje.getIdentificadorIntercambio(),mensaje.getCodigoEntidadRegistralDestino());

            if(oficioRemision != null){
                procesarMensajeACK(oficioRemision);
            }else if(registroSir != null){
                procesarMensajeACK(registroSir);
            }else{
                log.info("El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0037, "El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
            }

            // Mensaje CONFIRMACIÓN
        }else if(mensaje.getTipoMensaje().equals(TipoMensaje.CONFIRMACION.getValue())){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio(), mensaje.getCodigoEntidadRegistralDestino());

            if(oficioRemision != null){
                procesarMensajeCONFIRMACION(oficioRemision, mensaje);
            }else{
                log.info("El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0037, "El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
            }


            // Mensaje ERROR
        }else if(mensaje.getTipoMensaje().equals(TipoMensaje.ERROR.getValue())){

            OficioRemision oficioRemision = oficioRemisionEjb.getByIdentificadorIntercambio(mensaje.getIdentificadorIntercambio(), mensaje.getCodigoEntidadRegistralDestino());
            RegistroSir registroSir = registroSirEjb.getRegistroSir(mensaje.getIdentificadorIntercambio(),mensaje.getCodigoEntidadRegistralDestino());

            peticion.append("CodigoError: ").append(mensaje.getCodigoError()).append(System.getProperty("line.separator"));

            if(oficioRemision != null){
                procesarMensajeERROR(oficioRemision, mensaje);
            }else if(registroSir != null){
                procesarMensajeERROR(registroSir, mensaje);
            }else{
                log.info("El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
                throw new ValidacionException(Errores.ERROR_0037, "El mensaje de control corresponde a un IdentificadorIntercambio que no existe en el sistema");
            }

        }else{
            log.info("El tipo mensaje de control no es válido: " + mensaje.getTipoMensaje());
            throw new ValidacionException(Errores.ERROR_0037, "El tipo mensaje de control no es válido: " + mensaje.getTipoMensaje());
        }

        // Guardar el mensaje
        mensaje.setEntidad(entidad);
        persist(mensaje);

        // Integración
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion,peticion.toString(),System.currentTimeMillis() - inicio.getTime(), entidad.getId(), mensaje.getIdentificadorIntercambio());

    }

    /**
     * Procesa un mensaje de control de tipo ACK
     * @param oficioRemision
     * @throws Exception
     */
    private void procesarMensajeACK(OficioRemision oficioRemision) throws Exception{

        switch (oficioRemision.getEstado()) {

            case RegwebConstantes.OFICIO_SIR_ENVIADO:

                // Actualizamos el OficioRemision
                oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO_ACK);
                oficioRemision.setFechaEstado(new Date());
                oficioRemisionEjb.merge(oficioRemision);
                break;

            case RegwebConstantes.OFICIO_SIR_REENVIADO:

                // Actualizamos el OficioRemision
                oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO_ACK);
                oficioRemision.setFechaEstado(new Date());
                oficioRemisionEjb.merge(oficioRemision);

                break;

            case RegwebConstantes.OFICIO_SIR_ENVIADO_ACK:
            case RegwebConstantes.OFICIO_SIR_REENVIADO_ACK:

                log.info("Se ha recibido un mensaje ACK duplicado con identificador: " + oficioRemision.getIdentificadorIntercambio());

                break;

            default:
                log.info("Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
                throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
        }
    }

    /**
     * Procesa un mensaje de control de tipo ACK
     * @param registroSir
     * @throws Exception
     */
    private void procesarMensajeACK(RegistroSir registroSir) throws Exception{

        if (EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado()) ||
                EstadoRegistroSir.REENVIADO_Y_ERROR.equals(registroSir.getEstado())){

            // Actualizamos el registroSir
            registroSir.setEstado(EstadoRegistroSir.REENVIADO_Y_ACK);
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.RECHAZADO.equals(registroSir.getEstado()) ||
                EstadoRegistroSir.RECHAZADO_Y_ERROR.equals(registroSir.getEstado())){

            // Actualizamos el registroSir
            registroSir.setEstado(EstadoRegistroSir.RECHAZADO_Y_ACK);
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.REENVIADO_Y_ACK.equals(registroSir.getEstado()) ||
                EstadoRegistroSir.RECHAZADO_Y_ACK.equals(registroSir.getEstado())){

            log.info("Se ha recibido un mensaje ACK duplicado con identificador: " + registroSir.getIdentificadorIntercambio());

        }else{
            log.info("Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
            throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un mensaje que no tiene el estado adecuado para recibir un ACK");
        }
    }

    /**
     * Procesa un mensaje de control de tipo CONFIRMACION
     * @param oficioRemision
     * @throws Exception
     */
    private void procesarMensajeCONFIRMACION(OficioRemision oficio, MensajeControl mensaje) throws Exception{

        switch (oficio.getEstado()) {

            case RegwebConstantes.OFICIO_SIR_ENVIADO:
            case RegwebConstantes.OFICIO_SIR_ENVIADO_ACK:
            case RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR:
            case RegwebConstantes.OFICIO_SIR_REENVIADO:
            case RegwebConstantes.OFICIO_SIR_REENVIADO_ACK:
            case RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR:

                oficio.setCodigoEntidadRegistralProcesado(mensaje.getCodigoEntidadRegistralOrigen());
                oficio.setDecodificacionEntidadRegistralProcesado(Dir3CaibUtils.denominacion(PropiedadGlobalUtil.getDir3CaibServer(oficio.getUsuarioResponsable().getEntidad().getId()), mensaje.getCodigoEntidadRegistralOrigen(), RegwebConstantes.OFICINA));
                oficio.setNumeroRegistroEntradaDestino(mensaje.getNumeroRegistroEntradaDestino());
                oficio.setFechaEntradaDestino(mensaje.getFechaEntradaDestino());
                oficio.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
                oficio.setFechaEstado(mensaje.getFechaEntradaDestino());
                oficioRemisionEjb.merge(oficio);

                // Marcamos el Registro original como ACEPTADO
                if (oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)) {
                    registroEntradaEjb.cambiarEstado(oficio.getRegistrosEntrada().get(0).getId(), RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);

                }else if(oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
                    registroSalidaEjb.cambiarEstado(oficio.getRegistrosSalida().get(0).getId(),RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);

                }

                break;

            case (RegwebConstantes.OFICIO_ACEPTADO):

                log.info("Se ha recibido un mensaje de confirmación duplicado");

                break;

            default:
                log.info("El RegistroSir no tiene el estado necesario para ser Confirmado: " + oficio.getIdentificadorIntercambio());
                throw new ValidacionException(Errores.ERROR_0037, "El RegistroSir no tiene el estado necesario para ser Confirmado: " + oficio.getIdentificadorIntercambio());
        }
    }

    /**
     * Procesa un mensaje de control de tipo ERROR
     * @param oficioRemision
     * @param mensaje
     * @throws Exception
     */
    private void procesarMensajeERROR(OficioRemision oficioRemision, MensajeControl mensaje) throws Exception{

        switch (oficioRemision.getEstado()) {

            case (RegwebConstantes.OFICIO_SIR_ENVIADO):

                if(!mensaje.getCodigoError().equals(Errores.ERROR_0039.getValue())){ // Solo modificamos su estado si no es un error 0039
                    oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR);
                    oficioRemision.setCodigoError(mensaje.getCodigoError());
                    oficioRemision.setDescripcionError(mensaje.getDescripcionMensaje());
                    oficioRemision.setFechaEstado(new Date());
                    oficioRemisionEjb.merge(oficioRemision);
                }

                break;

            case (RegwebConstantes.OFICIO_SIR_REENVIADO):

                if(!mensaje.getCodigoError().equals(Errores.ERROR_0039.getValue())){ // Solo modificamos su estado si no es un error 0039

                    oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR);
                    oficioRemision.setCodigoError(mensaje.getCodigoError());
                    oficioRemision.setDescripcionError(mensaje.getDescripcionMensaje());
                    oficioRemision.setFechaEstado(new Date());
                    oficioRemisionEjb.merge(oficioRemision);
                }

                break;

            case (RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR):
            case (RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR):

                log.info("Se ha recibido un mensaje de error duplicado con identificador: " + oficioRemision.getIdentificadorIntercambio());
                throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un mensaje de error duplicado con identificador: " + oficioRemision.getIdentificadorIntercambio());

        }
    }

    /**
     * Procesa un mensaje de control de tipo ERROR
     * @param registroSir
     * @param mensaje
     * @throws Exception
     */
    private void procesarMensajeERROR(RegistroSir registroSir, MensajeControl mensaje) throws Exception{

        if (EstadoRegistroSir.REENVIADO.equals(registroSir.getEstado())){

            registroSir.setEstado(EstadoRegistroSir.REENVIADO_Y_ERROR);
            registroSir.setCodigoError(mensaje.getCodigoError());
            registroSir.setDescripcionError(mensaje.getDescripcionMensaje());
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.RECHAZADO.equals(registroSir.getEstado())){

            registroSir.setEstado(EstadoRegistroSir.RECHAZADO_Y_ERROR);
            registroSir.setCodigoError(mensaje.getCodigoError());
            registroSir.setDescripcionError(mensaje.getDescripcionMensaje());
            registroSir.setFechaEstado(new Date());
            registroSirEjb.merge(registroSir);

        } else if (EstadoRegistroSir.REENVIADO_Y_ERROR.equals(registroSir.getEstado()) ||
                EstadoRegistroSir.RECHAZADO_Y_ERROR.equals(registroSir.getEstado())){

            log.info("Se ha recibido un mensaje de error duplicado con identificador: " + registroSir.getIdentificadorIntercambio());
            throw new ValidacionException(Errores.ERROR_0037, "Se ha recibido un mensaje de error duplicado con identificador: " + registroSir.getIdentificadorIntercambio());

        }
    }

    /**
     * Comprueba a partir de la Oficina destino, si la Entidad está integrada en SIR
     * @param codigoEntidadRegistralDestino
     * @throws Exception
     */
    private Entidad comprobarEntidadMensajeControl(String codigoEntidadRegistralDestino) throws Exception{

        Entidad entidad;
        Oficina oficina = oficinaEjb.findByMultiEntidad(codigoEntidadRegistralDestino);

        if(oficina != null){
            entidad = oficina.getOrganismoResponsable().getEntidad();

            if(!entidad.getActivo() || !entidad.getSir()){
                log.info("La Entidad de la oficina "+ oficina.getDenominacion() +" no esta activa o no se ha activado su integracion con SIR");
                throw new ValidacionException(Errores.ERROR_0037, "La Entidad de la oficina "+ oficina.getDenominacion() +" no esta activa o no se ha activado su integracion con SIR");

            }else if(!oficinaEjb.isSIREnvio(oficina.getId())){
                log.info("La Oficina "+ oficina.getDenominacion() +" no esta habilitada para enviar asientos SIR");
                throw new ValidacionException(Errores.ERROR_0037, "La Oficina "+ oficina.getDenominacion() +" no esta habilitada para enviar asientos SIR");
            }

            return entidad;

        }else{
            log.info("El CodigoEntidadRegistralDestino del FicheroIntercambio no pertenece a ninguna Entidad del sistema: " + codigoEntidadRegistralDestino);
            throw new ValidacionException(Errores.ERROR_0037, "El CodigoEntidadRegistralDestino del FicheroIntercambio no pertenece a ninguna Entidad del sistema: " + codigoEntidadRegistralDestino);
        }
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> mensajes =  em.createQuery("select distinct(mc.id) from MensajeControl as mc where mc.entidad.id = :idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = mensajes.size();

        if(mensajes.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (mensajes.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = mensajes.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from MensajeControl where id in (:mensajes)").setParameter("mensajes", subList).executeUpdate();
                mensajes.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from MensajeControl where id in (:mensajes)").setParameter("mensajes", mensajes).executeUpdate();
        }
        return total;

    }

}
