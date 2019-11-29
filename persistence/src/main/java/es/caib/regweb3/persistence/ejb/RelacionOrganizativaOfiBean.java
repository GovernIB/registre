package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RelacionOrganizativaOfi;
import es.caib.regweb3.model.RelacionOrganizativaOfiPK;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

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
 * Date: 10/10/13
 */
@Stateless(name = "RelacionOrganizativaOfiEJB")
@SecurityDomain("seycon")
public class RelacionOrganizativaOfiBean extends BaseEjbJPA<RelacionOrganizativaOfi, RelacionOrganizativaOfiPK> implements RelacionOrganizativaOfiLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CatServicioLocal catServicioLocalEjb;


    @Override
    public RelacionOrganizativaOfi getReference(RelacionOrganizativaOfiPK id) throws Exception {

        return em.getReference(RelacionOrganizativaOfi.class, id);
    }

    @Override
    public RelacionOrganizativaOfi findById(RelacionOrganizativaOfiPK id) throws Exception {

        return em.find(RelacionOrganizativaOfi.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RelacionOrganizativaOfi> getAll() throws Exception {

        return  em.createQuery("Select relacionOrganizativaOfi from RelacionOrganizativaOfi as relacionOrganizativaOfi ").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(relacionOrganizativaOfi.id) from RelacionOrganizativaOfi as relacionOrganizativaOfi");

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RelacionOrganizativaOfi> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select relacionOrganizativaOfi from RelacionOrganizativaOfi as relacionOrganizativaOfi ");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public void deleteAll() throws Exception {

        em.createQuery("delete from RelacionOrganizativaOfi").executeUpdate();
        
    }

    @Override
    public int deleteByOficinaEntidad(Long idOficina) throws Exception {

        Query q = em.createQuery("delete from RelacionOrganizativaOfi as roo where roo.oficina.id = :idOficina ");
      q.setParameter("idOficina", idOficina);

        return q.executeUpdate();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> getOficinasByOrganismo(Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select distinct relacionOrganizativaOfi.oficina from RelacionOrganizativaOfi as relacionOrganizativaOfi " +
                "where relacionOrganizativaOfi.organismo.id = :idOrganismo and relacionOrganizativaOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Oficina> oficinas = q.getResultList();
        for(Oficina oficina:oficinas){
            Hibernate.initialize(oficina.getOrganizativasOfi());
        }

        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws Exception {

        String oficinaVirtualWhere = "";

        if (!oficinaVirtual) {
            oficinaVirtualWhere = " and :oficinaVirtual not in elements(roo.oficina.servicios)";
        }

        Query q = em.createQuery("Select distinct roo.oficina.id,roo.oficina.codigo, roo.oficina.denominacion, roo.oficina.organismoResponsable.id, roo.oficina.organismoResponsable.codigo, roo.oficina.organismoResponsable.denominacion  from RelacionOrganizativaOfi as roo " +
                "where roo.organismo.id = :idOrganismo and roo.estado.codigoEstadoEntidad = :vigente " +
                oficinaVirtualWhere);

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        if (!oficinaVirtual) {
            q.setParameter("oficinaVirtual", catServicioLocalEjb.findByCodigo(RegwebConstantes.REGISTRO_VIRTUAL_NO_PRESENCIAL));
        }

        List<Oficina> oficinas =  new ArrayList<Oficina>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Oficina oficina = new Oficina((Long)object[0],(String)object[1],(String)object[2],(Long)object[3],(String)object[4],(String)object[5]);

            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getOrganismosByOficina(Long idOficina) throws Exception{

        Query q = em.createQuery("Select distinct relacionOrganizativaOfi.organismo from RelacionOrganizativaOfi as relacionOrganizativaOfi " +
                "where relacionOrganizativaOfi.oficina.id = :idOficina and relacionOrganizativaOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RelacionOrganizativaOfi getRelacionOrganizativa(Long idOficina, Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select distinct relacionOrganizativaOfi.oficina from RelacionOrganizativaOfi as relacionOrganizativaOfi " +
                "where relacionOrganizativaOfi.organismo.id = :idOrganismo and relacionOrganizativaOfi.oficina.id = :idOficina " +
                "and relacionOrganizativaOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<RelacionOrganizativaOfi> relaciones =  q.getResultList();

        if(relaciones.size() > 0){
            return  relaciones.get(0);
        }else{
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RelacionOrganizativaOfi> organizativaByEntidadEstado(Long idEntidad, String estado) throws Exception{
        Query q = em.createQuery("Select relacionOrganizativaOfi.oficina.id, relacionOrganizativaOfi.oficina.codigo, relacionOrganizativaOfi.oficina.denominacion, " +
                "relacionOrganizativaOfi.organismo.id, relacionOrganizativaOfi.oficina.organismoResponsable.id, relacionOrganizativaOfi.organismo.organismoRaiz.id " +
                "from RelacionOrganizativaOfi as relacionOrganizativaOfi where " +
                "relacionOrganizativaOfi.organismo.entidad.id =:idEntidad and relacionOrganizativaOfi.estado.codigoEstadoEntidad =:estado order by relacionOrganizativaOfi.oficina.codigo");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<RelacionOrganizativaOfi> relacionOrganizativaOfis = new ArrayList<RelacionOrganizativaOfi>();

        for (Object[] object : result) {
            RelacionOrganizativaOfi relacionOrganizativaOfi = new RelacionOrganizativaOfi((Long) object[0], (String) object[1], (String) object[2], (Long) object[3], (Long) object[4], (Long) object[5]);
            relacionOrganizativaOfis.add(relacionOrganizativaOfi);
        }

        return relacionOrganizativaOfis;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> relaciones = em.createQuery("Select distinct(o.id) from RelacionOrganizativaOfi as o where o.organismo.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = relaciones.size();

        if(relaciones.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (relaciones.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = relaciones.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from RelacionOrganizativaOfi where id in (:relaciones) ").setParameter("relaciones", subList).executeUpdate();
                relaciones.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from RelacionOrganizativaOfi where id in (:relaciones) ").setParameter("relaciones", relaciones).executeUpdate();
        }

        return total;

    }

}
