package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroLopdMigrado;
import es.caib.regweb3.model.RegistroMigrado;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 19/11/14
 */

@Stateless(name = "RegistroLopdMigradoEJB")
@SecurityDomain("seycon")
public class RegistroLopdMigradoBean extends BaseEjbJPA<RegistroLopdMigrado, Long> implements RegistroLopdMigradoLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public RegistroLopdMigrado findById(Long id) throws Exception {

        return em.find(RegistroLopdMigrado.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroLopdMigrado> getAll() throws Exception {

        return  em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado order by registroLopdMigrado.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroLopdMigrado.id) from RegistroLopdMigrado as registroLopdMigrado");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<RegistroLopdMigrado> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado order by registroLopdMigrado.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public List<RegistroLopdMigrado> getByRegistroMigrado(Long numRegistro, String accion) throws Exception {

        Query q = em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado where " +
                "registroLopdMigrado.registroMigrado.id = :numRegistro and " +
                "registroLopdMigrado.tipoAcceso like :accion order by registroLopdMigrado.fecha desc");

        q.setParameter("numRegistro", numRegistro);
        q.setParameter("accion", accion);

        return q.getResultList();
    }

    @Override
    public RegistroLopdMigrado getCreacion(Long numRegistro, String accion) throws Exception {

        Query q = em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado where " +
                "registroLopdMigrado.registroMigrado.id = :numRegistro and " +
                "registroLopdMigrado.tipoAcceso like :accion order by registroLopdMigrado.fecha desc");

        q.setParameter("numRegistro", numRegistro);
        q.setParameter("accion", accion);

        List<RegistroLopdMigrado> registroLopdMigrado = q.getResultList();
        if(registroLopdMigrado.size() == 1){
            return registroLopdMigrado.get(0);
        }else{
            return  null;
        }
    }


    @Override
    public Paginacion getByUsuario(Integer pageNumber, Date dataInici, Date dataFi, String usuario, String accion) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select registroLopdMigrado.registroMigrado.numero, registroLopdMigrado.registroMigrado.ano," +
                "registroLopdMigrado.registroMigrado.denominacionOficina, registroLopdMigrado.registroMigrado.tipoRegistro," +
                "registroLopdMigrado.fecha from RegistroLopdMigrado as registroLopdMigrado where " +
                "registroLopdMigrado.fecha >= :dataInici and registroLopdMigrado.fecha <= :dataFi and " +
                "registroLopdMigrado.usuario like :usuario and " +
                "registroLopdMigrado.tipoAcceso like :accion order by registroLopdMigrado.fecha desc");

        q.setParameter("dataInici", dataInici);
        q.setParameter("dataFi", dataFi);
        q.setParameter("usuario", usuario);
        q.setParameter("accion", accion);

        List<RegistroLopdMigrado> registrosLopdMigrado = new ArrayList<RegistroLopdMigrado>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(registroLopdMigrado.id) from RegistroLopdMigrado as registroLopdMigrado where " +
                "registroLopdMigrado.fecha >= :dataInici and registroLopdMigrado.fecha <= :dataFi and " +
                "registroLopdMigrado.usuario like :usuario and registroLopdMigrado.tipoAcceso like :accion");

        q2.setParameter("dataInici", dataInici);
        q2.setParameter("dataFi", dataFi);
        q2.setParameter("usuario", usuario);
        q2.setParameter("accion", accion);

        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(BaseEjbJPA.RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroMigrado registroMigrado = new RegistroMigrado((Integer) object[0], (Integer) object[1], (String) object[2], (Boolean) object[3]);
            RegistroLopdMigrado registroLopdMigrado = new RegistroLopdMigrado(registroMigrado, (Date) object[4]);

            registrosLopdMigrado.add(registroLopdMigrado);
        }

        paginacion.setListado(registrosLopdMigrado);

        return paginacion;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> tipos = em.createQuery("Select distinct(id) from RegistroLopdMigrado where registroMigrado.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : tipos) {
            remove(findById((Long) id));
        }

        return tipos.size();

    }

}
