package es.caib.regweb3.persistence.ejb;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public abstract class BaseEjbJPA<T extends Serializable, E> implements BaseEjb<T, E> {

    public final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final int RESULTADOS_PAGINACION = 10;

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    public void remove(T persistentInstance) throws I18NException {

        try{
            em.remove(em.merge(persistentInstance));
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }
    
    
  
    

    public T persist(T transientInstance) throws I18NException{

        try{
            em.persist(transientInstance);
            return transientInstance;
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }

    public T merge(T instance) throws I18NException{

        try{
            return em.merge(instance);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }
}
