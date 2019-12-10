package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mgonzalez on 21/03/2018.
 */
@Stateless(name = "ColaEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ColaBean extends BaseEjbJPA<Cola, Long> implements ColaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private IntegracionLocal integracionEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;


    @Override
    public Cola getReference(Long id) throws Exception {

        return em.getReference(Cola.class, id);
    }

    @Override
    public Cola findById(Long id) throws Exception {

        return em.find(Cola.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Cola> findByTipoEntidad(Long tipo, Long idEntidad,Integer total, int maxReintentos) throws Exception {

        Query q = em.createQuery( "select cola from Cola as cola where cola.tipo=:tipo and cola.usuarioEntidad.entidad.id=:idEntidad  and cola.numeroReintentos < :maxReintentos order by cola.numeroReintentos asc");
        q.setParameter("tipo", tipo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", maxReintentos);
        q.setHint("org.hibernate.readOnly", true);

        if(total != null) {
            q.setMaxResults(total);
        }

        return q.getResultList();
    }

    @Override
    public Cola findByIdObjeto(Long idObjeto,Long idEntidad) throws Exception{

        Query q = em.createQuery( "select cola from Cola as cola where cola.idObjeto=:idObjeto and cola.usuarioEntidad.entidad.id=:idEntidad");
        q.setParameter("idObjeto", idObjeto);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        if(q.getResultList().size()>0){
            return (Cola)q.getResultList().get(0);
        }else{
            return null;
        }
    }



    @Override
    public List<Cola> findByTipoEntidadMaxReintentos(Long tipo, Long idEntidad,Integer total, int maxReintentos) throws Exception {

        Query q = em.createQuery( "select cola from Cola as cola where cola.tipo=:tipo and cola.usuarioEntidad.entidad.id=:idEntidad  and cola.numeroReintentos = :maxReintentos order by cola.usuarioEntidad.entidad.id asc");
        q.setParameter("tipo", tipo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", maxReintentos);
        q.setHint("org.hibernate.readOnly", true);

        if(total != null) {
            q.setMaxResults(total);
        }

        return q.getResultList();
    }




    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Cola> getAll() throws Exception {

        return  em.createQuery("Select cola from Cola as cola order by cola.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(cola.id) from Cola as cola");

        return (Long) q.getSingleResult();
    }



    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Cola> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select cola from Cola as cola order by cola.id");
        q.setFirstResult(inicio);
        q.setMaxResults(Cola.RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }




    @Override
    public Paginacion busqueda(Cola cola, Long idEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select cola from Cola as cola ");

        //MaxReintentos
       // where.add("cola.numeroMaximoReintentos = cola.numeroReintentos ");

        // Tipo integración
        if (cola.getTipo() != -1) {
            where.add("cola.tipo = :tipo ");
            parametros.put("tipo", cola.getTipo());
        }

        // Estado
        if (cola.getEstado() != null) {
            where.add("cola.estado = :estado ");
            parametros.put("estado", cola.getEstado());
        }

        // Entidad
        where.add("cola.usuarioEntidad.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);

        // Añadimos los parámetros a la query
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
            q2 = em.createQuery(query.toString().replaceAll("Select cola from Cola as cola ", "Select count(cola.id) from Cola as cola "));
            query.append("order by cola.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select cola from Cola as cola ", "Select count(cola.id) from Cola as cola "));
            query.append("order by cola.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

        if (cola.getPageNumber() != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), cola.getPageNumber(),Cola.RESULTADOS_PAGINACION);
            int inicio = (cola.getPageNumber() - 1) * Cola.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(Cola.RESULTADOS_PAGINACION);
            q.setHint("org.hibernate.readOnly", true);

        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public boolean enviarAColaDistribucion(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException, I18NValidationException {

        try {
            //Comprobamos que el objeto no esté ya en cola. Ha ocurrido alguna vez de ver en cola el mismo registro 2 veces
            if(findByIdObjeto(re.getId(),usuarioEntidad.getEntidad().getId())==null){
                //Creamos un elemento nuevo de la cola de distribución
                Cola cola = new Cola();
                cola.setIdObjeto(re.getId());
                cola.setDescripcionObjeto(re.getNumeroRegistroFormateado());
                cola.setTipo(RegwebConstantes.COLA_DISTRIBUCION);
                cola.setUsuarioEntidad(usuarioEntidad);
                cola.setDenominacionOficina(re.getOficina().getDenominacion());

                persist(cola);

                log.info("RegistroEntrada: " + re.getNumeroRegistroFormateado() + " enviado a la Cola de Distribución");
                registroEntradaEjb.cambiarEstado(re.getId(),RegwebConstantes.REGISTRO_DISTRIBUYENDO);
                return true;
            }else{ // Si ya existe, no se incluye en la cola
                log.error("El registre ja es troba a la coa; No es tornarà a afegir");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public Paginacion reiniciarColabyEntidadTipo(Long idEntidad, Long tipo, Cola cola) throws Exception, I18NException {

        //Obtenemos el numero máximo de reintentos de una propiedad global
        Integer maxReintentos = PropiedadGlobalUtil.getMaxReintentosCola(idEntidad);

        IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_DISTRIBUCION);
        //Obtenemos todos los que han alcanzado el máximo de reintentos.
        List<Cola> pendientesDistribuir = findByTipoEntidadMaxReintentos(tipo,idEntidad,null,maxReintentos);
        //Reseteamos los valores de los pendientes para que vuelvan a entrar en la cola.
        for(Cola pendiente: pendientesDistribuir){
            reiniciarElementoCola(pendiente);
        }

        //Gestionamos la paginación
        int total = pendientesDistribuir.size();
        Paginacion paginacion = new Paginacion(total, cola.getPageNumber(),Cola.RESULTADOS_PAGINACION);
        int inicio = (cola.getPageNumber() - 1) * Cola.RESULTADOS_PAGINACION;
        int fin = pendientesDistribuir.size()<Cola.RESULTADOS_PAGINACION ? pendientesDistribuir.size():Cola.RESULTADOS_PAGINACION;
        paginacion.setListado(pendientesDistribuir.subList(inicio,fin));
        return paginacion;
    }


    @Override
    public void actualizarElementoCola(Cola elemento,String descripcion, StringBuilder peticion,long tiempo,Long entidadId, String hora, String idioma, Throwable th, List<UsuarioEntidad> administradores, int maxReintentos) throws Exception{

        //Montamos el string de la causa del error
        String causa = "";
        if(th != null){
            causa = th.getCause() + "<br>";
        }else{//En este caso el plugin no lanza excepción, simplemente devuelve false
            causa = " El plugin de distribución ha devuelto false <br>";
        }

        try {
            //Incrementar numeroreintentos si no hemos llegado al máximo
            if (elemento.getNumeroReintentos() < maxReintentos) {
                elemento.setNumeroReintentos(elemento.getNumeroReintentos() + 1);
                //Si hemos alcanzado el máximo de reintentos marcamos estado a error
                if (elemento.getNumeroReintentos() == maxReintentos) {
                    elemento.setEstado(RegwebConstantes.COLA_ESTADO_ERROR);
                } else {
                    elemento.setEstado(RegwebConstantes.COLA_ESTADO_WARNING);
                }
            }
            //Guardamos que ha ocurrido un error en integraciones
            elemento.setError(elemento.getError() + "&nbsp;" + hora + causa);
            merge(elemento);

            // Añadimos el error a la integración
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, peticion.toString(), th, null,System.currentTimeMillis() - tiempo, entidadId, elemento.getDescripcionObjeto());

        }catch (Exception e){
            elemento.setError(elemento.getError() + "&nbsp;" + hora + causa);
            merge(elemento);
            e.printStackTrace();
        }
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> colas = em.createQuery("Select distinct(id) from Cola as cola where cola.usuarioEntidad.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : colas) {
            remove(findById((Long) id));
        }

        return colas.size();
    }



    @Override
    public void reiniciarElementoCola(Cola cola) throws Exception {

           cola.setNumeroReintentos(0); //Contador a 0
           cola.setError("&nbsp;");
           cola.setEstado(null);
           merge(cola);
    }

}
