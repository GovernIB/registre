package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Sesion;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 31/10/19
 */

@Stateless(name = "SesionEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class SesionBean extends BaseEjbJPA<Sesion, Long> implements SesionLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public Sesion getReference(Long id) throws Exception {

        return em.getReference(Sesion.class, id);
    }

    @Override
    public Sesion findById(Long id) throws Exception {

        return em.find(Sesion.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Sesion> getAll() throws Exception {

        return  em.createQuery("Select sesion from Sesion as sesion order by sesion.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(sesion.id) from Sesion as sesion");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Sesion> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select sesion from Sesion as sesion order by sesion.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Sesion nuevaSesion(UsuarioEntidad usuario) throws Exception{

        // Creamos un nuevo token
        SecureRandom secureRandom = new SecureRandom();
        Long idSesion = secureRandom.nextLong();

        Sesion sesion = new Sesion(idSesion, usuario, RegwebConstantes.SESION_NO_INICIADA);

        log.info("idSesion: " + idSesion);

        return persist(sesion);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Sesion findByIdSesionUsuario(Long idSesion, UsuarioEntidad usuario) {

        Query q = em.createQuery("Select s from Sesion as s where s.idSesion = :idSesion and s.usuario.id = :idUsuario");

        q.setParameter("idSesion", idSesion);
        q.setParameter("idUsuario", usuario.getId());

        List<Sesion> result = q.getResultList();
        if(result.size() == 1){
            return result.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Sesion findByIdSesionUsuarioEstado(Long idSesion, UsuarioEntidad usuario, Long estado) {

        Query q = em.createQuery("Select s from Sesion as s where " +
                "s.idSesion = :idSesion and s.usuario.id = :idUsuario and s.estado = :estado");

        q.setParameter("idSesion", idSesion);
        q.setParameter("idUsuario", usuario.getId());
        q.setParameter("estado", estado);

        List<Sesion> result = q.getResultList();
        if(result.size() == 1){
            return result.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public void cambiarEstado(Long idSesion, UsuarioEntidad usuario, Long estado) throws Exception{

        Sesion sesion = findByIdSesionUsuario(idSesion, usuario);

        sesion.setEstado(estado);

        merge(sesion);
    }

    @Override
    public void iniciarSesion(Long idSesion, UsuarioEntidad usuario) throws Exception{

        Sesion sesion;

        Query q = em.createQuery("Select s from Sesion as s where s.idSesion = :idSesion " +
                "and s.usuario.id = :idUsuario and s.estado != :estado");

        q.setParameter("idSesion", idSesion);
        q.setParameter("idUsuario", usuario.getId());
        q.setParameter("estado", RegwebConstantes.SESION_FINALIZADA);

        List<Sesion> result = q.getResultList();

        if(result.size() == 1){

            sesion =  result.get(0);

            sesion.setEstado(RegwebConstantes.SESION_INICIADA);
            merge(sesion);

        }else{
            throw new Exception("El idSesion no existe en el sistema");
        }
    }

    @Override
    public void finalizarSesion(Long idSesion, UsuarioEntidad usuario, Long tipoRegistro, String numeroRegistro) throws Exception{

        Sesion sesion = findByIdSesionUsuario(idSesion, usuario);

        sesion.setEstado(RegwebConstantes.SESION_FINALIZADA);
        sesion.setNumeroRegistro(numeroRegistro);
        sesion.setTipoRegistro(tipoRegistro);

        merge(sesion);
    }

    @Override
    public void purgarSesiones(Long idEntidad) throws Exception{

        purgarSesionesIniciadas(idEntidad);
        purgarSesionesErrorFinalidadas(idEntidad);
        purgarSesionesNoIniciadas(idEntidad);

    }

    private void purgarSesionesIniciadas(Long idEntidad) throws Exception{

        Calendar hoy = Calendar.getInstance(); //obtiene la fecha de hoy
        hoy.add(Calendar.MINUTE, -PropiedadGlobalUtil.getSesionMinutosPurgadoIniciadas(idEntidad)); //el -X indica que se le restaran X minutos

        List<?> result =  em.createQuery("select distinct(s.id) from Sesion as s where s.usuario.entidad.id = :idEntidad and s.estado = :iniciada and s.fecha <= :fecha")
                .setParameter("idEntidad", idEntidad)
                .setParameter("iniciada", RegwebConstantes.SESION_INICIADA)
                .setParameter("fecha", hoy.getTime()).getResultList();

        eliminarSesiones(result);

    }

    private void purgarSesionesNoIniciadas(Long idEntidad) throws Exception{

        Calendar hoy = Calendar.getInstance(); //obtiene la fecha de hoy
        hoy.add(Calendar.MINUTE, -PropiedadGlobalUtil.getSesionMinutosPurgadoNoIniciadas(idEntidad)); //el -X indica que se le restaran X minutos

        List<?> result =  em.createQuery("select distinct(s.id) from Sesion as s where s.usuario.entidad.id = :idEntidad and s.estado = :no_iniciada and s.fecha <= :fecha")
                .setParameter("idEntidad", idEntidad)
                .setParameter("no_iniciada", RegwebConstantes.SESION_NO_INICIADA)
                .setParameter("fecha", hoy.getTime()).getResultList();

        eliminarSesiones(result);
    }

    private void purgarSesionesErrorFinalidadas(Long idEntidad) throws Exception{

        Calendar hoy = Calendar.getInstance(); //obtiene la fecha de hoy
        hoy.add(Calendar.MINUTE, -PropiedadGlobalUtil.getSesionMinutosPurgadoFinalizadas(idEntidad)); //el -X indica que se le restaran X minutos

        List<?> result =  em.createQuery("select distinct(s.id) from Sesion as s where s.usuario.entidad.id = :idEntidad and (s.estado = :error or s.estado = :finalizada) and s.fecha <= :fecha")
                .setParameter("idEntidad", idEntidad)
                .setParameter("error", RegwebConstantes.SESION_ERROR)
                .setParameter("finalizada", RegwebConstantes.SESION_FINALIZADA)
                .setParameter("fecha", hoy.getTime()).getResultList();

        eliminarSesiones(result);
    }

    private void eliminarSesiones(List<?> sesiones) throws Exception{

        if(sesiones.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (sesiones.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = sesiones.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from Sesion where id in (:sesiones)").setParameter("sesiones", subList).executeUpdate();
                sesiones.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }
            em.createQuery("delete from Sesion where id in (:sesiones)").setParameter("sesiones", sesiones).executeUpdate();
        }

    }
}
