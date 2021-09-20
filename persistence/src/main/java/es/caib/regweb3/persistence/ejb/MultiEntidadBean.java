package es.caib.regweb3.persistence.ejb;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 * Date: 07/07/2021
 */

@Stateless(name = "MultiEntidadEJB")
@SecurityDomain("seycon")
public class MultiEntidadBean implements MultiEntidadLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public boolean isMultiEntidad() throws Exception {
        return em.createQuery("Select entidad.id from Entidad as entidad where entidad.sir = true order by entidad.id").getResultList().size() > 1;
    }

}


