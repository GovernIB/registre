package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.*;

import org.apache.log4j.Logger;

import org.jboss.ejb3.annotation.SecurityDomain;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaCambiarEstadoEJB")
@SecurityDomain("seycon")
public class RegistroSalidaCambiarEstadoBean extends BaseEjbJPA<RegistroSalida, Long>
    implements RegistroSalidaCambiarEstadoLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;



    @Override
    public RegistroSalida findById(Long id) throws Exception {

        return em.find(RegistroSalida.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getAll() throws Exception {

        return  em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<RegistroSalida> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public void cambiarEstado(Long idRegistro, Long idEstado) throws Exception{
        RegistroSalida registroSalida = findById(idRegistro);
        registroSalida.setEstado(idEstado);
        merge(registroSalida);
    }

   

}
