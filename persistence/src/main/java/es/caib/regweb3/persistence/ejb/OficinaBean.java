package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RelacionOrganizativaOfi;
import es.caib.regweb3.model.utils.ObjetoBasico;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "OficinaEJB")
@SecurityDomain("seycon")
public class OficinaBean extends BaseEjbJPA<Oficina, Long> implements OficinaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;
    @EJB private CatServicioLocal catServicioLocalEjb;


    @Override
    public Oficina findById(Long id) throws Exception {

        Oficina oficina = em.find(Oficina.class, id);
        Hibernate.initialize(oficina.getOrganizativasOfi());
        return oficina;
    }



    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Oficina> getAll() throws Exception {

        return  em.createQuery("Select oficina from Oficina as oficina order by oficina.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(oficina.id) from Oficina as oficina");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Oficina> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina order by oficina.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Oficina findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo =:codigo");

        q.setParameter("codigo",codigo);

        List<Oficina> oficina = q.getResultList();
        if(oficina.size() == 1){
            return oficina.get(0);
        }else{
            return  null;
        }

    }

    @Override
    public Oficina findByCodigoVigente(String codigo) throws Exception {

        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.codigo =:codigo and " +
                "oficina.estado.codigoEstadoEntidad =:vigente");

        q.setParameter("codigo",codigo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<Oficina> oficina = q.getResultList();
        if(oficina.size() == 1){
            return oficina.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public List<Oficina> findByOrganismoResponsable(Long idOrganismo) throws Exception{
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.id =:idOrganismo and " +
                "oficina.estado.codigoEstadoEntidad=:vigente");

        q.setParameter("idOrganismo",idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<Oficina> oficinas = q.getResultList();
        for(Oficina oficina:oficinas){
          Hibernate.initialize(oficina.getOrganizativasOfi());
        }
        return oficinas;
    }

    @Override
    public List<ObjetoBasico> findByOrganismoResponsableVO(Long idOrganismo) throws Exception{
        Query q = em.createQuery("Select oficina.id, oficina.denominacion as nombre from Oficina as oficina where " +
                "oficina.organismoResponsable.id =:idOrganismo and " +
                "oficina.estado.codigoEstadoEntidad=:vigente and " +
                ":oficinaVirtual not in elements(oficina.servicios)");

        q.setParameter("oficinaVirtual",catServicioLocalEjb.findByCodigo(RegwebConstantes.REGISTRO_VIRTUAL_NO_PRESENCIAL));
        q.setParameter("idOrganismo",idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<ObjetoBasico> oficinas =  new ArrayList<ObjetoBasico>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            ObjetoBasico objetoBasico = new ObjetoBasico((Long)object[0],(String)object[1]);

            oficinas.add(objetoBasico);
        }



        return oficinas;
    }

    @Override
    public List<Oficina> findByEntidad(Long idEntidad) throws Exception{
        Query q = em.createQuery("Select oficina from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad");

        q.setParameter("idEntidad",idEntidad);
        List<Oficina> oficinas = q.getResultList();
        for(Oficina oficina:oficinas){
          Hibernate.initialize(oficina.getOrganizativasOfi());
        }
        return oficinas;
    }

    @Override
    public List<Oficina> findByEntidadByEstado(Long idEntidad, String estado) throws Exception{
        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad=:estado");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        List<Oficina> oficinas =  new ArrayList<Oficina>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Oficina oficina = new Oficina((Long)object[0],(String)object[1],(String)object[2]);
            oficinas.add(oficina);
        }
        return oficinas;
    }

    @Override
    public List<Oficina> responsableByEntidadEstado(Long idEntidad, String estado) throws Exception{
        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion, oficina.organismoResponsable.id from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad =:estado and " +
                "oficina.oficinaResponsable.id = null order by oficina.codigo");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        List<Object[]> result = q.getResultList();
        List<Oficina> oficinas = new ArrayList<Oficina>();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            oficinas.add(oficina);
        }

        return oficinas;
    }

    @Override
    public List<Oficina> dependienteByEntidadEstado(Long idEntidad, String estado) throws Exception{
        Query q = em.createQuery("Select oficina.id, oficina.codigo, oficina.denominacion, oficina.oficinaResponsable.id, oficina.organismoResponsable.id from Oficina as oficina where " +
                "oficina.organismoResponsable.entidad.id =:idEntidad and oficina.estado.codigoEstadoEntidad =:estado and " +
                "oficina.oficinaResponsable.id != null order by oficina.codigo");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        List<Object[]> result = q.getResultList();
        List<Oficina> oficinas = new ArrayList<Oficina>();

        for (Object[] object : result) {
            Oficina oficina = new Oficina((Long) object[0], (String) object[1], (String) object[2], (Long) object[3], (Long) object[4]);

            oficinas.add(oficina);
        }

        return oficinas;


        /*for(Oficina oficina:oficinas){
          Hibernate.initialize(oficina.getOrganizativasOfi());
        }*/
    }

    @Override
    public Set<ObjetoBasico> oficinasRegistro(List<Libro> libros) throws Exception{

        Set<ObjetoBasico> oficinas = new HashSet<ObjetoBasico>();  // Utilizamos un Set porque no permite duplicados

        // Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
        for (Libro libro : libros) {
            Long idOrganismo = libro.getOrganismo().getId();
            oficinas.addAll(findByOrganismoResponsableVO(idOrganismo));
            oficinas.addAll(relacionOrganizativaOfiLocalEjb.getOficinasByOrganismoVO(idOrganismo));
        }

        return oficinas;
    }

    @Override
    public Boolean tieneOficinasOrganismo(Long idOrganismo) throws Exception{
        Query q = em.createQuery("Select oficina.id from Oficina as oficina where " +
                "oficina.organismoResponsable.id =:idOrganismo and " +
                "oficina.estado.codigoEstadoEntidad=:vigente");

        q.setParameter("idOrganismo",idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<Long> oficinas = q.getResultList();

        if(oficinas.size()>0){
            return true;
        }else{
            q= em.createQuery("select relorg from RelacionOrganizativaOfi as relorg where relorg.organismo.id=:idOrganismo and relorg.estado.codigoEstadoEntidad=:vigente");
            q.setParameter("idOrganismo",idOrganismo);
            q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

            List<RelacionOrganizativaOfi> relorg= q.getResultList();
            return relorg.size() > 0;
        }

    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{


        List<?> oficinasRaiz = em.createQuery("Select distinct(id) from Oficina where organismoResponsable.entidad.id =:idEntidad and oficinaResponsable != null ").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = oficinasRaiz.size();

        if (oficinasRaiz.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (oficinasRaiz.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = oficinasRaiz.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Servicios oficinas raiz eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", subList).executeUpdate());
                em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", subList).executeUpdate();
                oficinasRaiz.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            log.info("Servicios oficinas raiz eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", oficinasRaiz).executeUpdate());
            em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", oficinasRaiz).executeUpdate();

        }

        List<?> oficinasAuxiliares = em.createQuery("Select distinct(id) from Oficina  where organismoResponsable.entidad.id =:idEntidad and oficinaResponsable is null ").setParameter("idEntidad", idEntidad).getResultList();
        total = total + oficinasAuxiliares.size();

        if (oficinasAuxiliares.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (oficinasAuxiliares.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = oficinasAuxiliares.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Servicios oficinas auxiliares eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", subList).executeUpdate());
                em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", subList).executeUpdate();
                oficinasAuxiliares.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            log.info("Servicios oficinas auxiliares eliminados: " + em.createNativeQuery("delete from RWE_OFICINA_SERVICIO WHERE IDOFICINA in(:oficinas)").setParameter("oficinas", oficinasAuxiliares).executeUpdate());
            em.createQuery("delete from Oficina where id in (:oficinas) ").setParameter("oficinas", oficinasAuxiliares).executeUpdate();
        }

        return total;

    }
}
