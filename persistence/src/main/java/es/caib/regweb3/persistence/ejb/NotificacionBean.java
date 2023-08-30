package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Notificacion;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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

@Stateless(name = "NotificacionEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class NotificacionBean extends BaseEjbJPA<Notificacion, Long> implements NotificacionLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private OficinaLocal oficinaEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private RegistroSirLocal registroSirEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
    @EJB private PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;
    @EJB private UsuarioEntidadLocal usuarioEntidadEjb;

    @Override
    public Notificacion getReference(Long id) throws I18NException {

        return em.getReference(Notificacion.class, id);
    }

    @Override
    public Notificacion findById(Long id) throws I18NException {

        return em.find(Notificacion.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Notificacion> getAll() throws I18NException {

        return em.createQuery("Select notificacion from Notificacion as notificacion order by notificacion.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(notificacion.id) from Notificacion as notificacion");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Notificacion> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select notificacion from Notificacion as notificacion order by notificacion.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getByEstadoCount(Long idUsuarioEntidad, Long idEstado) throws I18NException {

        String queryEstado = "";
        if (idEstado != null) {
            queryEstado = "and no.estado = :idEstado";
        }

        Query q = em.createQuery("Select count(no.id) from Notificacion as no where no.destinatario.id = :idUsuarioEntidad " + queryEstado);

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setHint("org.hibernate.readOnly", true);

        if (idEstado != null) {
            q.setParameter("idEstado", idEstado);
        }

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Notificacion> getByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select notificacion from Notificacion as notificacion where notificacion.destinatario.entidad.id = :idEntidad order by notificacion.id");
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    public Paginacion busqueda(Notificacion notificacion, Long idUsuarioEntidad) throws I18NException {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select notificacion from Notificacion as notificacion ");

        // Estado
        if (notificacion.getEstado() != null) {
            where.add("notificacion.estado = :estado ");
            parametros.put("estado", notificacion.getEstado());
        }

        // Entidad
        where.add("notificacion.destinatario.id = :idUsuarioEntidad ");
        parametros.put("idUsuarioEntidad", idUsuarioEntidad);

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
            q2 = em.createQuery(query.toString().replaceAll("Select notificacion from Notificacion as notificacion ", "Select count(notificacion.id) from Notificacion as notificacion "));
            query.append("order by notificacion.estado, notificacion.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select notificacion from Notificacion as notificacion ", "Select count(notificacion.id) from Notificacion as notificacion "));
            query.append("order by notificacion.estado, notificacion.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

        if (notificacion.getPageNumber() != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), notificacion.getPageNumber());
            int inicio = (notificacion.getPageNumber() - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(BaseEjbJPA.RESULTADOS_PAGINACION);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;

    }

    @Override
    public void leerNotificacion(Long idNotificacion) throws I18NException {

        Query q = em.createQuery("update from Notificacion set estado = :leido, fechaLeido = :fechaLeido where id = :idNotificacion");

        q.setParameter("leido", RegwebConstantes.NOTIFICACION_ESTADO_LEIDA);
        q.setParameter("fechaLeido", new Date());
        q.setParameter("idNotificacion", idNotificacion);

        q.executeUpdate();
    }

    @Override
    public Long notificacionesPendientes(Long idUsuarioEntidad) throws I18NException {

        Query q = em.createQuery("Select count(n.id) from Notificacion as n where n.destinatario.id = :idUsuarioEntidad " +
                "and n.estado = :nueva");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("nueva", RegwebConstantes.NOTIFICACION_ESTADO_NUEVA);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    public void notificacionAdminEntidad(Long idEntidad, String asunto, String mensaje) throws I18NException {

        List<UsuarioEntidad> administradores = usuarioEntidadEjb.findAdministradoresByEntidad(idEntidad);

        //Crear notificación para cada usuario
        for (UsuarioEntidad usuario : administradores) {

            Notificacion nueva = new Notificacion(RegwebConstantes.NOTIFICACION_TIPO_ERROR);
            nueva.setRemitente(null);
            nueva.setAsunto(asunto);
            nueva.setMensaje(mensaje);
            nueva.setDestinatario(usuario);

            persist(nueva);
        }

    }

    @Override
    public void notificacionesRegistrosSirPendientes(Long idEntidad) throws I18NException {

        List<Oficina> oficinasSir = oficinaEjb.oficinasSIREntidad(idEntidad);

        for (Oficina oficina : oficinasSir) {

            if (registroSirEjb.getPendientesProcesarCount(oficina.getCodigo()) > 10) {
                log.info("Conunicaciones RegistrosSirPendientes para la oficina: " + oficina.getDenominacion());

                //Obtener los usuarios con Permiso SIR en la Oficina indicada
                LinkedHashSet<Organismo> organismos = organismoEjb.getOrganismosRegistro(oficinaEjb.findById(oficina.getId()));
                List<UsuarioEntidad> usuarios = permisoOrganismoUsuarioEjb.getUsuariosPermiso(organismos, RegwebConstantes.PERMISO_SIR);

                //Crear notificación para cada usuario
                for (UsuarioEntidad usuario : usuarios) {

                    Locale locale = new Locale(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(usuario.getUsuario().getIdioma()));

                    Notificacion nueva = new Notificacion(RegwebConstantes.NOTIFICACION_TIPO_AVISO);
                    nueva.setRemitente(null);
                    nueva.setAsunto(I18NLogicUtils.tradueix(locale, "notificacion.RegistrosSirPendientes.asunto"));
                    nueva.setMensaje(I18NLogicUtils.tradueix(locale, "notificacion.RegistrosSirPendientes.mensaje", oficina.getDenominacion()));
                    nueva.setDestinatario(usuario);

                    persist(nueva);
                }
            }
        }
    }

    @Override
    public void notificacionesRechazadosDevueltos(Long idEntidad) throws I18NException {

        List<Oficina> oficinasSir = oficinaEjb.oficinasSIREntidad(idEntidad);

        for (Oficina oficina : oficinasSir) {

            oficina = oficinaEjb.findById(oficina.getId()); // Cargamos la oficina completa para obtener sus relaciones

            // Registros entrada Rechazados o Devueltos al origen
            if (registroEntradaConsultaEjb.getSirRechazadosReenviadosCount(oficina.getId()) > 0) {
                log.info("Conunicaciones Entradas RechazadosDevueltos para la oficina: " + oficina.getDenominacion());

                //Obtener los usuarios con Permiso SIR en la Oficina indicada
                LinkedHashSet<Organismo> organismos = organismoEjb.getOrganismosRegistro(oficina);
                List<UsuarioEntidad> usuarios = permisoOrganismoUsuarioEjb.getUsuariosPermiso(organismos, RegwebConstantes.PERMISO_SIR);

                //Crear notificación para cada usuario
                for (UsuarioEntidad usuario : usuarios) {

                    Locale locale = new Locale(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(usuario.getUsuario().getIdioma()));

                    Notificacion nueva = new Notificacion(RegwebConstantes.NOTIFICACION_TIPO_AVISO);
                    nueva.setRemitente(null);
                    nueva.setAsunto(I18NLogicUtils.tradueix(locale, "notificacion.EntradasRechazadasDevueltas.asunto"));
                    nueva.setMensaje(I18NLogicUtils.tradueix(locale, "notificacion.EntradasRechazadasDevueltas.mensaje", oficina.getDenominacion()));
                    nueva.setDestinatario(usuario);

                    persist(nueva);
                }
            }

            // Registros salida Rechazados o Devueltos al origen
            if (registroSalidaConsultaEjb.getSirRechazadosReenviadosCount(oficina.getId()) > 0) {
                log.info("Conunicaciones Salidas RechazadosDevueltos para la oficina: " + oficina.getDenominacion());

                //Obtener los usuarios con Permiso SIR en la Oficina indicada
                LinkedHashSet<Organismo> organismos = organismoEjb.getOrganismosRegistro(oficina);
                List<UsuarioEntidad> usuarios = permisoOrganismoUsuarioEjb.getUsuariosPermiso(organismos, RegwebConstantes.PERMISO_SIR);

                //Crear notificación para cada usuario
                for (UsuarioEntidad usuario : usuarios) {

                    Locale locale = new Locale(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(usuario.getUsuario().getIdioma()));

                    Notificacion nueva = new Notificacion(RegwebConstantes.NOTIFICACION_TIPO_AVISO);
                    nueva.setRemitente(null);
                    nueva.setAsunto(I18NLogicUtils.tradueix(locale, "notificacion.SalidasRechazadasDevueltas.asunto"));
                    nueva.setMensaje(I18NLogicUtils.tradueix(locale, "notificacion.SalidasRechazadasDevueltas.mensaje", oficina.getDenominacion()));
                    nueva.setDestinatario(usuario);

                    persist(nueva);
                }
            }
        }
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> notificacion = em.createQuery("select distinct(i.id) from Notificacion as i where i.destinatario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = notificacion.size();

        if (notificacion.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (notificacion.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = notificacion.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from Notificacion where id in (:notificacion)").setParameter("notificacion", subList).executeUpdate();
                notificacion.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from Notificacion where id in (:notificacion)").setParameter("notificacion", notificacion).executeUpdate();
        }
        return total;

    }

    @Override
    public void eliminarByUsuario(Long idUsuarioEntidad) throws I18NException {
        Query q = em.createQuery("delete from Notificacion as n where n.remitente.id=:idUsuarioEntidad or n.destinatario.id=:idUsuarioEntidad");
        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.executeUpdate();

    }

}
