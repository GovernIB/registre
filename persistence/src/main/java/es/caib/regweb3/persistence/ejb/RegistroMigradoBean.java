package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroLopdMigrado;
import es.caib.regweb3.model.RegistroMigrado;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 11/11/14
 */

@Stateless(name = "RegistroMigradoEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class RegistroMigradoBean extends BaseEjbJPA<RegistroMigrado, Long> implements RegistroMigradoLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/RegistroLopdMigradoEJB/local")
    public RegistroLopdMigradoLocal registroLopdMigradoEjb;


    @Override
    public RegistroMigrado getReference(Long id) throws Exception {

        return em.getReference(RegistroMigrado.class, id);
    }

    @Override
    public RegistroMigrado findById(Long id) throws Exception {

        RegistroMigrado registroMigrado = em.find(RegistroMigrado.class, id);
        Hibernate.initialize(registroMigrado.getEntidad());
        return registroMigrado;

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroMigrado> getAll() throws Exception {

        return  em.createQuery("Select registroMigrado from RegistroMigrado as registroMigrado order by registroMigrado.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroMigrado.id) from RegistroMigrado as registroMigrado");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroMigrado> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroMigrado from RegistroMigrado as registroMigrado order by registroMigrado.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public Boolean tieneRegistrosMigrados(Long entidad) throws Exception {

        Query q = em.createQuery("Select count(registroMigrado.id) from RegistroMigrado as registroMigrado where " +
                "registroMigrado.entidad.id = :entidad");

        q.setParameter("entidad",entidad);

        return (Long) q.getSingleResult() > 0;
    }


    @Override
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, Integer numeroRegistro, Integer anoRegistro, RegistroMigrado registroMigrado, Long idEntidad) throws Exception{

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select registroMigrado from RegistroMigrado as registroMigrado ");


        where.add(" registroMigrado.tipoRegistro = :tipoRegistro"); parametros.put("tipoRegistro",registroMigrado.isTipoRegistro());

        if(registroMigrado.getExtracto() != null && registroMigrado.getExtracto().length() > 0){where.add(DataBaseUtils.like("registroMigrado.extracto", "extracto", parametros, registroMigrado.getExtracto()));}

        if(registroMigrado.getDescripcionRemitenteDestinatario() != null && registroMigrado.getDescripcionRemitenteDestinatario().length() > 0){where.add(DataBaseUtils.like("registroMigrado.descripcionRemitenteDestinatario", "descripcionRemitenteDestinatario", parametros, registroMigrado.getDescripcionRemitenteDestinatario()));}

        if(anoRegistro != null){where.add(" registroMigrado.ano = :anoRegistro ");parametros.put("anoRegistro", anoRegistro);}

        if(numeroRegistro != null){where.add(" registroMigrado.numero = :numeroRegistro ");parametros.put("numeroRegistro", numeroRegistro);}

        if(registroMigrado.getCodigoOficina() > 0){where.add(" registroMigrado.codigoOficina = :codigoOficina"); parametros.put("codigoOficina",registroMigrado.getCodigoOficina());}

        if(fechaInicio != null){where.add(" registroMigrado.fechaRegistro >= :fechaInicio ");parametros.put("fechaInicio", fechaInicio);}

        if(fechaFin != null){where.add(" registroMigrado.fechaRegistro <= :fechaFin ");parametros.put("fechaFin", fechaFin);}

        where.add(" registroMigrado.entidad.id = :idEntidad ");parametros.put("idEntidad", idEntidad);

        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select registroMigrado from RegistroMigrado as registroMigrado ", "Select count(registroMigrado.id) from RegistroMigrado as registroMigrado "));
            query.append(" order by registroMigrado.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select registroMigrado from RegistroMigrado as registroMigrado ", "Select count(registroMigrado.id) from RegistroMigrado as registroMigrado "));
            query.append("order by registroMigrado.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

        if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String[]> getOficinas() throws Exception{

        Query q;

        q = em.createNativeQuery("Select distinct codoficina, denoficina from RWE_REGISTRO_MIGRADO " +
                "order by denoficina desc");

        return q.getResultList();
    }

    @Override
    public void insertarRegistroLopdMigrado(Long idRegistroMigrado, Long idUsuarioEntidad) throws Exception{

        RegistroMigrado registroMigrado = findById(idRegistroMigrado);
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);
        RegistroLopdMigrado registroLopdMigrado = new RegistroLopdMigrado();
        registroLopdMigrado.setRegistroMigrado(registroMigrado);
        registroLopdMigrado.setFecha(Calendar.getInstance().getTime());
        registroLopdMigrado.setUsuario(usuarioEntidad.getUsuario().getIdentificador());
        registroLopdMigrado.setTipoAcceso(RegwebConstantes.LOPDMIGRADO_CONSULTA);

        registroLopdMigradoEjb.persist(registroLopdMigrado);
    }

    @Override
    public void insertarRegistrosLopdMigrado(Paginacion paginacion, Long idUsuarioEntidad) throws Exception{

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);

        for (int i = 0; i<paginacion.getListado().size(); i++){
            RegistroMigrado registroMigrado = (RegistroMigrado) paginacion.getListado().get(i);
            RegistroLopdMigrado registroLopdMigrado = new RegistroLopdMigrado();
            registroLopdMigrado.setRegistroMigrado(registroMigrado);
            registroLopdMigrado.setFecha(Calendar.getInstance().getTime());
            registroLopdMigrado.setUsuario(usuarioEntidad.getUsuario().getIdentificador());
            registroLopdMigrado.setTipoAcceso(RegwebConstantes.LOPDMIGRADO_LISTADO);

            registroLopdMigradoEjb.persist(registroLopdMigrado);
        }
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> tipos = em.createQuery("Select distinct(id) from RegistroMigrado where entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : tipos) {
            remove(findById((Long) id));
        }

        return tipos.size();

    }

}
