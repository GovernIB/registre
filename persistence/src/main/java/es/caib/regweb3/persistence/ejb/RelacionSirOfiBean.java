package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RelacionSirOfi;
import es.caib.regweb3.model.RelacionSirOfiPK;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RelacionSirOfiEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class RelacionSirOfiBean extends BaseEjbJPA<RelacionSirOfi, RelacionSirOfiPK> implements RelacionSirOfiLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;

    @EJB private CatServicioLocal catServicioEjb;


    @Override
    public RelacionSirOfi getReference(RelacionSirOfiPK id) throws I18NException {

        return em.getReference(RelacionSirOfi.class, id);
    }

    @Override
    public RelacionSirOfi findById(RelacionSirOfiPK id) throws I18NException {

        return em.find(RelacionSirOfi.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RelacionSirOfi> getAll() throws I18NException {

        return em.createQuery("Select relacionSirOfi from RelacionSirOfi as relacionSirOfi ").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(relacionSirOfi.id) from RelacionSirOfi as relacionSirOfi");

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RelacionSirOfi> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select relacionSirOfi from RelacionSirOfi as relacionSirOfi ");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public void deleteAll() throws I18NException {

        em.createQuery("delete from RelacionSirOfi").executeUpdate();

    }

    @Override
    public int deleteByOficinaEntidad(Long idOficina) throws I18NException {

        Query q = em.createQuery("delete from RelacionSirOfi as rso where rso.oficina.id = :idOficina ");
        q.setParameter("idOficina", idOficina);

        return q.executeUpdate();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RelacionSirOfi getRelacionSir(Long idOficina, Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select distinct relacionSirOfi.oficina from RelacionSirOfi as relacionSirOfi " +
                "where relacionSirOfi.organismo.id = :idOrganismo and relacionSirOfi.oficina.id = :idOficina " +
                "and relacionSirOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<RelacionSirOfi> relaciones = q.getResultList();

        if (relaciones.size() > 0) {
            return relaciones.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RelacionSirOfi> relacionesSirOfiByEntidadEstado(Long idEntidad, String estado) throws I18NException {
        Query q = em.createQuery("Select relacionSirOfi.oficina.id, relacionSirOfi.oficina.codigo, relacionSirOfi.oficina.denominacion, " +
                "relacionSirOfi.organismo.id, relacionSirOfi.oficina.organismoResponsable.id from RelacionSirOfi as relacionSirOfi where " +
                "relacionSirOfi.organismo.entidad.id =:idEntidad and relacionSirOfi.estado.codigoEstadoEntidad =:estado order by relacionSirOfi.oficina.codigo");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("estado", estado);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<RelacionSirOfi> relacionSirOfis = new ArrayList<RelacionSirOfi>();

        for (Object[] object : result) {
            RelacionSirOfi relacionSirOfi = new RelacionSirOfi((Long) object[0], (String) object[1], (String) object[2], (Long) object[3], (Long) object[4]);
            relacionSirOfis.add(relacionSirOfi);
        }

        return relacionSirOfis;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> oficinasSIR(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select distinct rso.oficina.id, rso.oficina.codigo, rso.oficina.denominacion, oficina.organismoResponsable.id from RelacionSirOfi as rso " +
                "where rso.organismo.id = :idOrganismo and rso.estado.codigoEstadoEntidad = :vigente and :oficinaSir in elements(rso.oficina.servicios)");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("oficinaSir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR));
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficinas = new ArrayList<Oficina>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> oficinasSIREntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select distinct rso.oficina.id, rso.oficina.codigo, rso.oficina.denominacion, oficina.organismoResponsable.id from RelacionSirOfi as rso " +
                "where rso.organismo.entidad.id = :idEntidad and rso.estado.codigoEstadoEntidad = :vigente and :oficinaSir in elements(rso.oficina.servicios)");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("oficinaSir", catServicioEjb.findByCodigo(RegwebConstantes.OFICINA_INTEGRADA_SIR));
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficinas = new ArrayList<Oficina>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> organimosServicioSIR(Long idOficina) throws I18NException {

        Query q = em.createQuery("Select distinct rso.organismo.id, rso.organismo.codigo, rso.organismo.denominacion from RelacionSirOfi as rso " +
                "where rso.oficina.id = :idOficina and rso.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2]);

            organismos.add(organismo);
        }

        return organismos;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> relaciones = em.createQuery("Select distinct(o.id) from RelacionSirOfi as o where o.organismo.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = relaciones.size();

        if (relaciones.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (relaciones.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = relaciones.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from RelacionSirOfi where id in (:relaciones) ").setParameter("relaciones", subList).executeUpdate();
                relaciones.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from RelacionSirOfi where id in (:relaciones) ").setParameter("relaciones", relaciones).executeUpdate();
        }

        return total;

    }

}
