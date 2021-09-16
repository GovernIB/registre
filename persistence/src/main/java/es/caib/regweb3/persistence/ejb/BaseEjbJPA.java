package es.caib.regweb3.persistence.ejb;

import org.apache.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
public abstract class BaseEjbJPA<T extends Serializable, E> implements BaseEjb<T, E> {

    public final Logger log = Logger.getLogger(this.getClass());
    public static final int RESULTADOS_PAGINACION = 10;

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    public void remove(T persistentInstance) throws Exception{

        try{
            em.remove(em.merge(persistentInstance));
        }catch (Exception e){
            log.error(e);
            throw e;
        }

    }
    
    
  
    

    public T persist(T transientInstance) throws Exception{

        try{
            em.persist(transientInstance);
            return transientInstance;
        }catch (Exception e){
            log.error(e);
            throw e;
        }

    }

    public T merge(T instance) throws Exception{

        try{
            return em.merge(instance);
        }catch (Exception e){
            log.error(e);
            throw e;
        }

    }
}
