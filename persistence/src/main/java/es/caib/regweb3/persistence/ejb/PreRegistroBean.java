package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PreRegistro;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */

@Stateless(name = "PreRegistroEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class PreRegistroBean extends BaseEjbJPA<PreRegistro, Long> implements PreRegistroLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public PreRegistro getReference(Long id) throws Exception {

        return em.getReference(PreRegistro.class, id);
    }

    @Override
    public PreRegistro findById(Long id) throws Exception {

        return em.find(PreRegistro.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PreRegistro> getAll() throws Exception {

        return null;
    }

    @Override
    public Long getTotal() throws Exception {

        return null;
    }


    @Override
    public List<PreRegistro> getPagination(int inicio) throws Exception {

        return null;
    }




}