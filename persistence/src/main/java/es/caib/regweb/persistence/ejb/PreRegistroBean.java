package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Anexo;
import es.caib.regweb.model.Interesado;
import es.caib.regweb.model.PreRegistro;
import es.caib.regweb.persistence.utils.DataBaseUtils;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.persistence.utils.Respuesta;
import es.caib.regweb.persistence.utils.sir.DeMensaje;
import es.caib.regweb.persistence.utils.sir.DeMensajeFactory;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.sir.api.wssir7.WS_SIR7ServiceLocator;
import es.caib.regweb.ws.sir.api.wssir7.WS_SIR7_PortType;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */

@Stateless(name = "PreRegistroEJB")
@SecurityDomain("seycon")
public class PreRegistroBean extends BaseEjbJPA<PreRegistro, Long> implements PreRegistroLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb")
    private EntityManager em;

    @EJB(mappedName = "regweb/SirEJB/local")
    public SirLocal sirEjb;


    @Override
    public PreRegistro findById(Long id) throws Exception {

        return em.find(PreRegistro.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PreRegistro> getAll() throws Exception {

        return em.createQuery("Select preRegistro from PreRegistro as preRegistro order by preRegistro.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(preRegistro.id) from PreRegistro as preRegistro");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<PreRegistro> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select preRegistro from PreRegistro as preRegistro order by preRegistro.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public PreRegistro preRegistrar(PreRegistro preRegistro) throws Exception {

        // Obtenemos el último Número de preRegistro
        Query q = em.createQuery("Select max(preRegistro.contador) from PreRegistro as preRegistro");
        Long contador = (Long) q.getSingleResult();
        if(contador == null){
            contador = (long) 0;
        }

        // Se asigna el nuevo número de preRegistro
        contador = contador + 1;
        String numeroNuevo = contador.toString();
        String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code = "";
        for (int i = 0; i < 5; i++) {
            code+=(mayusculas.charAt((int)(Math.random() * mayusculas.length())));
        }
        preRegistro.setContador(contador);
        preRegistro.setNumeroPreregistro(code + numeroNuevo);
        preRegistro.setFecha(Calendar.getInstance().getTime());

        if (preRegistro.getRegistroDetalle() != null) {
          List<Interesado> interesados = preRegistro.getRegistroDetalle().getInteresados();
          if (interesados != null && interesados.size() != 0) {
            for (Interesado interesado : interesados) {
              interesado.setRegistroDetalle(preRegistro.getRegistroDetalle());
            }
          }
          
          
          List<Anexo> anexos  = preRegistro.getRegistroDetalle().getAnexos();
          if (anexos != null && anexos.size() != 0) {
            for (Anexo anexo : anexos) {
              anexo.setRegistroDetalle(preRegistro.getRegistroDetalle());
            }
          }
        }
        
        // Guardamos el PreRegistro
        preRegistro = persist(preRegistro);

        //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
//        if (StringUtils.isEmpty(preRegistro.getRegistroDetalle().getNumeroRegistroOrigen())) {
//            preRegistro.getRegistroDetalle().setNumeroRegistroOrigen(preRegistro.getNumeroPreregistro());
//            preRegistro = merge(preRegistro);
//        }

        return preRegistro;

    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Integer any, PreRegistro preRegistro, String codigoOficinaActiva, Long estado) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select preRegistro from PreRegistro as preRegistro ");

//        if(codigoOficinaActiva!= null){where.add(" preRegistro.codigoUnidadTramitacionDestino = :codigoOficinaActiva "); parametros.put("codigoOficinaActiva",codigoOficinaActiva);}

        if (codigoOficinaActiva != null && codigoOficinaActiva.length() > 0) {
            where.add(DataBaseUtils.like("preRegistro.codigoEntidadRegistralDestino", "codigoOficinaActiva", parametros, codigoOficinaActiva));
        }

        if (preRegistro.getRegistroDetalle().getExtracto() != null && preRegistro.getRegistroDetalle().getExtracto().length() > 0) {
            where.add(DataBaseUtils.like("preRegistro.registroDetalle.extracto", "extracto", parametros, preRegistro.getRegistroDetalle().getExtracto()));
        }

        if (preRegistro.getNumeroPreregistro() != null && preRegistro.getNumeroPreregistro().length() > 0) {
            where.add(DataBaseUtils.like("preRegistro.numeroPreregistro", "numeroPreregistro", parametros, preRegistro.getNumeroPreregistro()));
        }

        if (estado != null) {
            where.add(" preregistro.estado = :estado "); parametros.put("estado",estado);
        }

        if(any!= null){where.add(" year(preRegistro.fecha) = :any "); parametros.put("any",any);}

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
            q2 = em.createQuery(query.toString().replaceAll("Select preRegistro from PreRegistro as preRegistro ", "Select count(preRegistro.id) from PreRegistro as preRegistro "));
            query.append(" order by preRegistro.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select preRegistro from PreRegistro as preRegistro ", "Select count(preRegistro.id) from PreRegistro as preRegistro "));
            query.append("order by preRegistro.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public Respuesta<PreRegistro> crearPreRegistro(String sicres3) throws Exception{

        // Creamos el PreRegistro a partir del xml recibido
        Respuesta<PreRegistro> respuesta = sirEjb.readFicheroIntercambioSICRES3(sicres3);

        // Si el xml no estaba correcto
        if(respuesta.getObject() == null){
            log.info("Error parseando el xml");
            return respuesta;
        }

        PreRegistro preRegistro = (PreRegistro) respuesta.getObject();
        preRegistro.setEstado(RegwebConstantes.ESTADO_PREREGISTRO_PENDIENTE_PROCESAR);

        // Comprobar que el PreRegistro es correcto
        // Campos obligatorios?
        // Número de interesados?
        // Oficina destino?

        // Registramos el Pre-Registro
        preRegistro = preRegistrar(preRegistro);

        String mensajeACK = "";

        try {

            JAXBContext jc = JAXBContext.newInstance(DeMensaje.class);

            DeMensajeFactory deMensajeFactory = new DeMensajeFactory();
            DeMensaje ack = deMensajeFactory.createDeMensaje();

            ack.setCodigoEntidadRegistralOrigen(preRegistro.getCodigoEntidadRegistralOrigen());
            ack.setCodigoEntidadRegistralDestino(preRegistro.getCodigoEntidadRegistralDestino());
            ack.setIdentificadorIntercambio(preRegistro.getIdIntercambio());
            ack.setTipoMensaje("01");
            ack.setDescripcionMensaje("ACK");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            ack.setFechaHoraEntradaDestino(sdf.format(new Date()));
            ack.setIndicadorPrueba("1");

            //SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); // validation purpose
            //Schema schema = sf.newSchema(new File("SICRES3_MENSAJE_APL.xsd")); // validation purpose

            Marshaller m = jc.createMarshaller();
            //m.setSchema(schema); // validation purpose
            //m.setEventHandler(new MyValidationEventHandler()); // validation purpose
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter sw = new StringWriter();

            m.marshal(ack, sw);
            mensajeACK = sw.toString();

        }catch (JAXBException je) {
            StringWriter sw = new StringWriter();
            je.printStackTrace(new PrintWriter(sw));
            System.err.println("Error W1: " + sw.toString());
        }

        log.info("mensajeACK: " + mensajeACK);

        WS_SIR7ServiceLocator locator = new WS_SIR7ServiceLocator();
        WS_SIR7_PortType ws_sir7 = locator.getWS_SIR7();

        es.caib.regweb.ws.sir.api.wssir7.RespuestaWS respuesta1 = ws_sir7.recepcionMensajeDatosControlDeAplicacion(mensajeACK);

        log.info("RespuestaACK: " + respuesta1.getCodigo());
        log.info("RespuestaACK: " + respuesta1.getDescripcion());

        return respuesta;

    }


    @Override
    public List<PreRegistro> preRegistrosPendientesProcesar(String codigoOficinaActiva) throws Exception {

        Query q = em.createQuery("Select preRegistro from PreRegistro as preRegistro " +
                "where preRegistro.codigoEntidadRegistralDestino = :codigoOficinaActiva and preRegistro.estado = :idEstadoPreRegistro " +
                "order by preRegistro.id desc");


        q.setParameter("codigoOficinaActiva",codigoOficinaActiva);
        q.setParameter("idEstadoPreRegistro", RegwebConstantes.ESTADO_PREREGISTRO_PENDIENTE_PROCESAR);

        return q.getResultList();
    }


    public List<PreRegistro> getUltimosPreRegistrosPendientesProcesar(String codigoOficinaActiva, Integer total) throws Exception{

        Query q = em.createQuery("Select preRegistro from PreRegistro as preRegistro " +
                "where preRegistro.codigoEntidadRegistralDestino = :codigoOficinaActiva and preRegistro.estado = :idEstadoPreRegistro " +
                "order by preRegistro.fecha desc");

        q.setMaxResults(total);
        q.setParameter("codigoOficinaActiva", codigoOficinaActiva);
        q.setParameter("idEstadoPreRegistro", RegwebConstantes.ESTADO_PREREGISTRO_PENDIENTE_PROCESAR);

        return  q.getResultList();
    }


    @Override
    public Boolean tienePreRegistros(String codigoOficinaActiva) throws Exception {

        Query q = em.createQuery("Select count(preRegistro.id) from PreRegistro as preRegistro where " +
                "preRegistro.codigoEntidadRegistralDestino = :codigoOficinaActiva");

        q.setParameter("codigoOficinaActiva",codigoOficinaActiva);

        return (Long) q.getSingleResult() > 0;
    }

}