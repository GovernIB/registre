package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Persona;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "PersonaEJB")
@SecurityDomain("seycon")
public class PersonaBean extends BaseEjbJPA<Persona, Long> implements PersonaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public Persona getReference(Long id) throws Exception {

        return em.getReference(Persona.class, id);
    }

    @Override
    public Persona findById(Long id) throws Exception {

        return em.find(Persona.class, id);
    }

    @Override
    public Persona guardarPersona(Persona persona) throws Exception {

        persona.setNombre(StringUtils.capitailizeWord(persona.getNombre(), false));
        persona.setApellido1(StringUtils.capitailizeWord(persona.getApellido1(), false));
        persona.setApellido2(StringUtils.capitailizeWord(persona.getApellido2(), false));
        persona.setRazonSocial(StringUtils.capitailizeWord(persona.getRazonSocial(), true));

        return persist(persona);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getAll() throws Exception {

        return em.createQuery("Select persona from Persona as persona order by persona.id").getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getAllbyEntidadTipo(Long idEntidad, Long tipoPersona) throws Exception {

        StringBuilder query = new StringBuilder("Select persona from Persona as persona  " +
                "where persona.entidad.id = :idEntidad ");

        if (tipoPersona != null) {
            query.append("and persona.tipo = :tipoPersona ");
        }

        query.append("order by persona.apellido1");

        Query q = em.createQuery(query.toString());

        q.setParameter("idEntidad", idEntidad);

        if (tipoPersona != null) {
            q.setParameter("tipoPersona", tipoPersona);
        }

        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getFisicasByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select persona.id, persona.nombre, persona.apellido1,persona.apellido2, persona.documento, persona.tipo from Persona as persona  " +
                "where persona.entidad.id = :idEntidad and persona.tipo = :tipoPersona  order by persona.apellido1");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("tipoPersona", RegwebConstantes.TIPO_PERSONA_FISICA);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Persona> fisicas = new ArrayList<Persona>();

        for (Object[] object : result) {
            Persona persona = new Persona((Long) object[0], (String) object[1], (String) object[2], (String) object[3], (String) object[4], (Long) object[5]);

            fisicas.add(persona);
        }

        return fisicas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getJuridicasByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select persona.id, persona.razonSocial, persona.documento, persona.tipo from Persona as persona  " +
                "where persona.entidad.id = :idEntidad and persona.tipo = :tipoPersona  order by persona.razonSocial");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("tipoPersona", RegwebConstantes.TIPO_PERSONA_JURIDICA);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Persona> juridicas = new ArrayList<Persona>();

        for (Object[] object : result) {
            Persona persona = new Persona((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            juridicas.add(persona);
        }

        return juridicas;
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(persona.id) from Persona as persona");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select persona from Persona as persona order by persona.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Boolean existeDocumentoNew(String documento, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select persona.id from Persona as persona where " +
                "UPPER(persona.documento) = :documento and persona.entidad.id = :idEntidad");

        q.setParameter("documento", documento);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean existeDocumentoEdit(String documento, Long idPersona, Long idEntidad) throws Exception {
        Query q = em.createQuery("Select persona.id from Persona as persona where " +
                "persona.id != :idPersona and UPPER(persona.documento) = :documento and persona.entidad.id = :idEntidad");

        q.setParameter("documento", documento);
        q.setParameter("idPersona", idPersona);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long tipo) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select persona from Persona as persona ");

        if (nombre != null && nombre.length() > 0) {
            where.add(
                    "( (" + DataBaseUtils.like("persona.nombre", "nombre", parametros, nombre)
                            + " ) OR ( "
                            + DataBaseUtils.like("persona.razonSocial", "nombre", parametros, nombre)
                            + " ) ) ");
        }
        if (apellido1 != null && apellido1.length() > 0) {
            where.add(DataBaseUtils.like("persona.apellido1", "apellido1", parametros, apellido1));
        }
        if (apellido2 != null && apellido2.length() > 0) {
            where.add(DataBaseUtils.like("persona.apellido2", "apellido2", parametros, apellido2));
        }
        if (documento != null && documento.length() > 0) {
            where.add(" upper(persona.documento) like upper(:documento) ");
            parametros.put("documento", "%" + documento.toLowerCase() + "%");
        }
        if (tipo != -1) {
            where.add("persona.tipo = :tipo ");
            parametros.put("tipo", tipo);
        }
        where.add("persona.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);


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
            q2 = em.createQuery(query.toString().replaceAll("Select persona from Persona as persona ", "Select count(persona.id) from Persona as persona "));
            query.append("order by persona.id");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select persona from Persona as persona ", "Select count(persona.id) from Persona as persona "));
            query.append("order by persona.id");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> busquedaFisicas(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long idTipoPersona) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select persona.id, persona.nombre, persona.apellido1, persona.apellido2, persona.documento, persona.tipo from Persona as persona ");

        if (nombre != null && nombre.length() > 0) {
            where.add(DataBaseUtils.like("persona.nombre", "nombre", parametros, nombre));
        }
        if (apellido1 != null && apellido1.length() > 0) {
            where.add(DataBaseUtils.like("persona.apellido1", "apellido1", parametros, apellido1));
        }
        if (apellido2 != null && apellido2.length() > 0) {
            where.add(DataBaseUtils.like("persona.apellido2", "apellido2", parametros, apellido2));
        }
        if (documento != null && documento.length() > 0) {
            where.add(" upper(persona.documento) like upper(:documento) ");
            parametros.put("documento", "%" + documento.toLowerCase() + "%");
        }
        where.add("persona.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);
        where.add("persona.tipo = :idTipoPersona ");
        parametros.put("idTipoPersona", idTipoPersona);

        // Añadimos los parametros de búsqueda
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

            query.append("order by persona.apellido1");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());

            }

        } else {

            query.append("order by persona.apellido1");
            q = em.createQuery(query.toString());
        }

        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Persona> fisicas = new ArrayList<Persona>();

        for (Object[] object : result) {
            Persona persona = new Persona((Long) object[0], (String) object[1], (String) object[2], (String) object[3], (String) object[4], (Long) object[5]);

            fisicas.add(persona);
        }

        return fisicas;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> busquedaJuridicas(Long idEntidad, String razonSocial, String documento, Long idTipoPersona) throws Exception {
        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select persona.id, persona.razonSocial, persona.documento, persona.tipo from Persona as persona ");

        if (razonSocial != null && razonSocial.length() > 0) {
            where.add(DataBaseUtils.like("persona.razonSocial", "razonSocial", parametros, razonSocial));
        }
        if (documento != null && documento.length() > 0) {
            where.add(" upper(persona.documento) like upper(:documento) ");
            parametros.put("documento", "%" + documento.toLowerCase() + "%");
        }
        where.add("persona.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);
        where.add("persona.tipo = :idTipoPersona ");
        parametros.put("idTipoPersona", idTipoPersona);

        // Añadimos los parametros de búsqueda
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

            query.append("order by persona.razonSocial");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());

            }

        } else {

            query.append("order by persona.razonSocial");
            q = em.createQuery(query.toString());
        }

        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Persona> juridicas = new ArrayList<Persona>();

        for (Object[] object : result) {
            Persona persona = new Persona((Long) object[0], (String) object[1], (String) object[2], (Long) object[3]);

            juridicas.add(persona);
        }

        return juridicas;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        Query query = em.createQuery("delete from Persona where entidad.id = :idEntidad");
        return query.setParameter("idEntidad", idEntidad).executeUpdate();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ObjetoBasico> busquedaPersonas(String text, Long tipoPersona, Long idEntidad) throws Exception {
        Query q;
        String queryBase = "";

        List<String> where = new ArrayList<String>();
        Map<String, Object> parametros = new HashMap<String, Object>();

        if (tipoPersona.equals(RegwebConstantes.TIPO_PERSONA_FISICA)) {
            queryBase = "Select persona.id, CONCAT(persona.nombre,' ',persona.apellido1,' ', persona.apellido2,' - ', persona.documento) as completo from Persona as persona ";
            where.add(DataBaseUtils.like("persona.documento", "text", parametros, text));
            //where.add(DataBaseUtils.like("CONCAT(persona.nombre,' ',persona.apellido1,' ',persona.apellido2,' - ', persona.documento)", "text", parametros, text));
            where.add(" persona.tipo = :tipoPersona ");
            parametros.put("tipoPersona", RegwebConstantes.TIPO_PERSONA_FISICA);

        } else if (tipoPersona.equals(RegwebConstantes.TIPO_PERSONA_JURIDICA)) {
            queryBase = "Select persona.id, CONCAT(persona.razonSocial,' - ', persona.documento) as completo from Persona as persona ";
            where.add(DataBaseUtils.like("persona.documento", "text", parametros, text));

            //where.add(DataBaseUtils.like("CONCAT(persona.razonSocial,' - ', persona.documento)", "text", parametros, text));
            where.add(" persona.tipo = :tipoPersona ");
            parametros.put("tipoPersona", RegwebConstantes.TIPO_PERSONA_JURIDICA);
        }

        where.add(" persona.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);

        StringBuilder query = new StringBuilder(queryBase);
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
            query.append(" order by persona.id desc");
        }

        q = em.createQuery(query.toString());

        for (Map.Entry<String, Object> param : parametros.entrySet()) {

            q.setParameter(param.getKey(), param.getValue());
        }

        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<ObjetoBasico> personas = new ArrayList<ObjetoBasico>();

        for (Object[] object : result) {
            ObjetoBasico persona = new ObjetoBasico((Long) object[0], (String) object[1]);

            personas.add(persona);
        }
        return personas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> findByDocumento(String documento, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select persona from Persona as persona where " +
                "persona.documento = :documento and persona.entidad.id = :idEntidad");

        q.setParameter("documento", documento);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> buscarDuplicados(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select documento from Persona as persona " +
                "where persona.entidad.id = :idEntidad " +
                "group by persona.documento having(count(persona.documento) > 1 )");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<String> duplicados = q.getResultList();

        List<Persona> personasDuplicadas = new ArrayList<Persona>();

        for (String duplicado : duplicados) {
            personasDuplicadas.addAll(findByDocumento(duplicado, idEntidad));
        }

        return personasDuplicadas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Persona> getExportarExcel(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long tipo) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select persona.id, persona.nombre, persona.apellido1, persona.apellido2, persona.razonSocial, persona.documento, persona.tipo, persona.email, persona.telefono from Persona as persona ");

        if (nombre != null && nombre.length() > 0) {
            where.add(
                    "( (" + DataBaseUtils.like("persona.nombre", "nombre", parametros, nombre)
                            + " ) OR ( "
                            + DataBaseUtils.like("persona.razonSocial", "nombre", parametros, nombre)
                            + " ) ) ");
        }
        if (apellido1 != null && apellido1.length() > 0) {
            where.add(DataBaseUtils.like("persona.apellido1", "apellido1", parametros, apellido1));
        }
        if (apellido2 != null && apellido2.length() > 0) {
            where.add(DataBaseUtils.like("persona.apellido2", "apellido2", parametros, apellido2));
        }
        if (documento != null && documento.length() > 0) {
            where.add(" upper(persona.documento) like upper(:documento) ");
            parametros.put("documento", "%" + documento.toLowerCase() + "%");
        }
        if (tipo != -1) {
            where.add("persona.tipo = :tipo ");
            parametros.put("tipo", tipo);
        }
        where.add("persona.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);

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
            query.append("order by persona.id");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }

        } else {
            query.append("order by persona.id");
            q = em.createQuery(query.toString());
        }

        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Persona> personas = new ArrayList<Persona>();

        for (Object[] object : result) {
            Persona persona = new Persona((Long) object[0], (String) object[1], (String) object[2], (String) object[3],
                    (String) object[4], (String) object[5], (Long) object[6], (String) object[7], (String) object[8]);

            personas.add(persona);
        }

        return personas;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    @TransactionTimeout(value = 1200)  // 20 minutos
    public void capitalizarPersonasJuridicas(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select persona.id, persona.razonSocial from Persona as persona  " +
                "where persona.entidad.id =:idEntidad and persona.tipo =:tipoPersona order by persona.id");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("tipoPersona", RegwebConstantes.TIPO_PERSONA_JURIDICA);

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            Query u = em.createQuery("update Persona set razonSocial =:razonSocial where id =:idPersona ");
            u.setParameter("idPersona", object[0]);
            u.setParameter("razonSocial", StringUtils.capitailizeWord((String) object[1], true));
            u.executeUpdate();
            em.flush();
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    @TransactionTimeout(value = 1200)  // 20 minutos
    public void capitalizarPersonasFisicas(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select persona.id, persona.nombre, persona.apellido1, persona.apellido2 from Persona as persona  " +
                "where persona.entidad.id =:idEntidad and persona.tipo =:tipoPersona order by persona.id");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("tipoPersona", RegwebConstantes.TIPO_PERSONA_FISICA);

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            Query u = em.createQuery("update Persona set nombre =:nombre, apellido1 =:apellido1, apellido2 =:apellido2 where id =:idPersona ");
            u.setParameter("idPersona", object[0]);
            u.setParameter("nombre", StringUtils.capitailizeWord((String) object[1], false));
            u.setParameter("apellido1", StringUtils.capitailizeWord((String) object[2], false));
            u.setParameter("apellido2", StringUtils.capitailizeWord((String) object[3], false));
            u.executeUpdate();
            em.flush();
        }
    }
}
