package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PropiedadGlobal;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 05/05/16
 */
@Stateless(name = "PropiedadGlobalEJB")
@SecurityDomain("seycon")
public class PropiedadGlobalBean extends BaseEjbJPA<PropiedadGlobal, Long> implements PropiedadGlobalLocal {


    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public PropiedadGlobal getReference(Long id) throws Exception {

        return em.getReference(PropiedadGlobal.class, id);
    }

    @Override
    public PropiedadGlobal findById(Long id) throws Exception {

        return em.find(PropiedadGlobal.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getAll() throws Exception {

        return em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal order by propiedadGlobal.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(propiedadGlobal.id) from PropiedadGlobal as propiedadGlobal");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal order by propiedadGlobal.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> findByEntidad(Long idEntidad, Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select p from PropiedadGlobal as p where p.entidad = :idEntidad "+tipoWhere+" order by p.id");
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public PropiedadGlobal findByClaveEntidad(String clave, Long idEntidad, Long idPropiedadGlobal) throws Exception {

        String entidadQuery;
        String propiedadQuery = "";

        if (idEntidad != null) {
            entidadQuery = "and propiedadGlobal.entidad = :idEntidad ";
        } else {
            entidadQuery = "and propiedadGlobal.entidad is null ";
        }

        if (idPropiedadGlobal != null) {
            propiedadQuery = " and propiedadGlobal.id != :idPropiedadGlobal ";
        }

        Query q = em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal where propiedadGlobal.clave = :clave " +
                entidadQuery + propiedadQuery + " order by propiedadGlobal.id");

        q.setParameter("clave", clave);
        if (idEntidad != null) {
            q.setParameter("idEntidad", idEntidad);
        }
        if (idPropiedadGlobal != null) {
            q.setParameter("idPropiedadGlobal", idPropiedadGlobal);
        }

        q.setHint("org.hibernate.readOnly", true);

        List<PropiedadGlobal> p = q.getResultList();

        return p.size() == 1 ? p.get(0) : null;

    }

    @Override
    public Long getTotalByEntidad(Long idEntidad, Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select count(p.id) from PropiedadGlobal as p where p.entidad = :idEntidad "+tipoWhere);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getPaginationByEntidad(int inicio, Long idEntidad, Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select p from PropiedadGlobal as p where p.entidad = :idEntidad "+tipoWhere+" order by p.id");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }

        return q.getResultList();
    }

    @Override
    public Long getTotalREGWEB3(Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select count(p.id) from PropiedadGlobal as p where p.entidad is null "+tipoWhere);
        q.setHint("org.hibernate.readOnly", true);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getPaginationREGWEB3(int inicio, Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select p from PropiedadGlobal as p where p.entidad is null "+tipoWhere+" order by p.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }

        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> propiedades = em.createQuery("Select distinct(id) from PropiedadGlobal where entidad =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : propiedades) {
            remove(findById((Long) id));
        }

        return propiedades.size();
    }

    @Override
    public String getPropertyByEntidad(Long idEntidad, String clave) throws Exception{

        Query q = em.createQuery("Select pg.valor from PropiedadGlobal as pg where pg.entidad = :idEntidad and pg.clave = :clave");
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("clave", clave);
        q.setHint("org.hibernate.readOnly", true);

        List<?> list = q.getResultList();

        return list != null && list.size() != 0? (String) list.get(0):null;

    }

    @Override
    public String getProperty(String clave) throws Exception{

        Query q = em.createQuery("Select pg.valor from PropiedadGlobal as pg where pg.entidad is null and pg.clave = :clave");
        q.setParameter("clave", clave);
        q.setHint("org.hibernate.readOnly", true);

        List<?> list = q.getResultList();

        return list != null && list.size() != 0? (String) list.get(0):null;

    }

    @Override
    public Boolean getBooleanPropertyByEntidad(Long idEntidad, String clave) throws Exception {
        String value = getPropertyByEntidad(idEntidad, clave);

        return value != null && "true".equals(value);
    }
    public Boolean getBooleanProperty(String clave) throws Exception {
        String value = getProperty(clave);

        return value != null && "true".equals(value);
    }

    @Override
    public Long getLongPropertyByEntitat(Long idEntidad, String clave) throws Exception {
        String value = getPropertyByEntidad(idEntidad, clave);
        if (value == null || value.trim().length() == 0) {
            return null;
        } else {
            try {
                return Long.parseLong(value);
            } catch(NumberFormatException e) {
                log.error("Error conviertiendo a long el valor (" + value + ")  de la propiedad " + clave + ": " + e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    public Long getLongProperty(String clave) throws Exception {
        String value = getProperty(clave);
        if (value == null  || value.trim().length() == 0) {
            return null;
        } else {
            try {
                return Long.parseLong(value);
            } catch(NumberFormatException e) {
                log.error("Error conviertiendo a long el valor (" + value + ")  de la propiedad " + clave + ": " + e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    public Integer getIntegerPropertyByEntitat(Long idEntidad, String clave) throws Exception {
        String value = getPropertyByEntidad(idEntidad, clave);
        if (value == null  || value.trim().length() == 0) {
            return null;
        } else {
            try {
                return Integer.parseInt(value);
            } catch(NumberFormatException e) {
                log.error("Error conviertiendo a int el valor (" + value + ")  de la propiedad " + clave + ": " + e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    public Integer getIntegerProperty(String clave) throws Exception {
        String value = getProperty(clave);
        if (value == null  || value.trim().length() == 0) {
            return null;
        } else {
            try {
                return Integer.parseInt(value);
            } catch(NumberFormatException e) {
                log.error("Error conviertiendo a int el valor (" + value + ")  de la propiedad " + clave + ": " + e.getMessage(), e);
                return null;
            }
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getAllPropertiesByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select pg from PropiedadGlobal as pg where pg.entidad = :idEntidad ");
        q.setParameter("idEntidad", idEntidad);

        List<?> list = q.getResultList();

        return (List<PropiedadGlobal>) list;

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getAllProperties() throws Exception {

        Query q = em.createQuery("Select pg from PropiedadGlobal as pg where pg.entidad is null");
        
        List<?> list = q.getResultList();

        return (List<PropiedadGlobal>) list;

    }
}
