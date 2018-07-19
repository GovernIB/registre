package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.MailUtils;
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
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

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


    @EJB public IntegracionLocal integracionEjb;

    @EJB public PluginLocal pluginEjb;


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
        Query query = em.createQuery( "select cola from Cola as cola where cola.tipo=:tipo and cola.usuarioEntidad.entidad.id=:idEntidad  and cola.numeroReintentos < :maxReintentos order by cola.numeroReintentos asc");
        query.setParameter("tipo", tipo);
        query.setParameter("idEntidad", idEntidad);
        query.setParameter("maxReintentos", maxReintentos);

        if(total != null) {
            query.setMaxResults(total);
        }

        return query.getResultList();
        
    }



    @Override
    public List<Cola> findByTipoEntidadMaxReintentos(Long tipo, Long idEntidad,Integer total, int maxReintentos) throws Exception {
        Query query = em.createQuery( "select cola from Cola as cola where cola.tipo=:tipo and cola.usuarioEntidad.entidad.id=:idEntidad  and cola.numeroReintentos = :maxReintentos order by cola.usuarioEntidad.entidad.id asc");
        query.setParameter("tipo", tipo);
        query.setParameter("idEntidad", idEntidad);
        query.setParameter("maxReintentos", maxReintentos);

        if(total != null) {
            query.setMaxResults(total);
        }

        return query.getResultList();

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
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), cola.getPageNumber(),Cola.RESULTADOS_PAGINACION);
            int inicio = (cola.getPageNumber() - 1) * Cola.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(Cola.RESULTADOS_PAGINACION);


        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;

    }


    @Override
    public Paginacion reiniciarColabyEntidadTipo(Long idEntidad, Long tipo, Cola cola) throws Exception, I18NException, I18NValidationException {

        IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_DISTRIBUCION);
        //Obtenemos todos los que han alcanzado el máximo de reintentos.
        List<Cola> pendientesDistribuir = findByTipoEntidadMaxReintentos(tipo,idEntidad,null,distribucionPlugin.configurarDistribucion().getMaxReintentos());
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
                    //Enviamos email a administradores entidad cuando se ha alcanzado el máximo de reintentos
                    enviarEmailAdminEntidad(idioma, administradores);

                } else {
                    elemento.setEstado(RegwebConstantes.COLA_ESTADO_WARNING);
                }
            }
            //Guardamos que ha ocurrido un error en integraciones
            elemento.setError(elemento.getError() + "&nbsp;" + hora + causa);
            merge(elemento);
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, peticion.toString(), th, null,System.currentTimeMillis() - tiempo, entidadId, elemento.getDescripcionObjeto());
            }catch(Exception ee){
                elemento.setError(elemento.getError() + "&nbsp;" + hora + causa);
                merge(elemento);
                ee.printStackTrace();
            }
        }catch (Exception e){
            elemento.setError(elemento.getError() + "&nbsp;" + hora + causa);
            merge(elemento);
            log.error(e.getCause());
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



    /**
     * Envia un email a la lista de administradores de entidad
     * @param idioma idioma del mensaje
     * @param administradores listado de administradores de la entidad
     * @throws Exception
     */
    private void enviarEmailAdminEntidad(String idioma, List<UsuarioEntidad> administradores) throws Exception{
        //Datos comunes Mail
        Locale locale = new Locale(idioma);

        try {
            String asunto = I18NLogicUtils.tradueix(locale, "cola.mail.asunto");
            String mensajeTexto = "";
            //Montamos el mensaje del mail con el nombre de la Entidad
            if (administradores.size() > 0) {
                //Montamos el mensaje del mail con el nombre de la Entidad
                mensajeTexto = I18NLogicUtils.tradueix(locale, "cola.mail.cuerpo", administradores.get(0).getEntidad().getNombre());
            }

            //Miramos que estén definidos el remitente y el nombre del remitente
            if (PropiedadGlobalUtil.getRemitente() != null && PropiedadGlobalUtil.getRemitenteNombre() != null) {
                InternetAddress addressFrom = new InternetAddress(PropiedadGlobalUtil.getRemitente(), PropiedadGlobalUtil.getRemitenteNombre());
                //Enviamos email a todos los administradores de la entidad
                for (UsuarioEntidad usuarioEntidad : administradores) {
                    String mailAdminEntidad = usuarioEntidad.getUsuario().getEmail();
                    if (!mailAdminEntidad.isEmpty()) {
                        MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, mailAdminEntidad);
                    } else {
                        log.error("Existen problemas de distribución en los registros. Por favor avise al Administrador : " + usuarioEntidad.getNombreCompleto() + " de la entidad: " + usuarioEntidad.getEntidad().getNombre());
                    }
                }
            } else {
                log.error("No está definida la propiedad global <es.caib.regweb3.mail.remitente> o la propiedad <es.caib.regweb3.mail.remitente.nombre>  para la entidad.  ");
            }
        }catch(Exception e){
            //Si se produce una excepción continuamos con el proceso.
            log.error("Se ha producido un excepcion enviando mail");
            e.printStackTrace();
        }

    }


    @Override
    public void reiniciarElementoCola(Cola cola) throws Exception {

           cola.setNumeroReintentos(0); //Contador a 0
           cola.setError("&nbsp;");
           cola.setEstado(null);
           merge(cola);

    }


}
