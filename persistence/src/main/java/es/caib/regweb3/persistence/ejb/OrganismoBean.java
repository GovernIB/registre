package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
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
 * @author earrivi
 * @author anadal
 * Date: 16/01/14
 */

@Stateless(name = "OrganismoEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class OrganismoBean extends BaseEjbJPA<Organismo, Long> implements OrganismoLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private CatEstadoEntidadLocal catEstadoEntidadEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;


    @Override
    public Organismo getReference(Long id) throws Exception {

        return em.getReference(Organismo.class, id);
    }

    @Override
    public Organismo findById(Long id) throws Exception {

        Organismo organismo = em.find(Organismo.class, id);
        Hibernate.initialize(organismo.getLibros());
        Hibernate.initialize(organismo.getOrganismoRaiz());
        Hibernate.initialize(organismo.getHistoricoUO());

        return organismo;
    }

    public Organismo findByIdCompleto(Long id) throws Exception {

        Organismo organismo = em.find(Organismo.class, id);
        Hibernate.initialize(organismo.getLibros());
        Hibernate.initialize(organismo.getHistoricoUO());
        Hibernate.initialize(organismo.getOrganismoRaiz());
        Hibernate.initialize(organismo.getOrganismoSuperior());
        Hibernate.initialize(organismo.getEdpPrincipal());

        return organismo;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByIdLigero(Long idOrganismo) throws Exception {
        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion, organismo.codAmbComunidad.id, organismo.estado.id, organismo.entidad.id from Organismo as organismo where " +
                "organismo.id = :idOrganismo");

        q.setParameter("idOrganismo", idOrganismo);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) result.get(0)[3]));
            organismo.setEstado(catEstadoEntidadEjb.findById((Long) result.get(0)[4]));
            organismo.setEntidad(new Entidad((Long) result.get(0)[5]));
            return organismo;
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getAll() throws Exception {

        return em.createQuery("Select organismo from Organismo as organismo order by organismo.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(organismo.id) from Organismo as organismo");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotalByEntidad(Long entidad) throws Exception {

        Query q = em.createQuery("Select count(organismo.id) from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getAllByEntidad(Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2]);
            organismos.add(organismo);
        }

        return organismos;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo order by organismo.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getPaginationByEntidad(int inicio, Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.codigo = :codigo");

        q.setParameter("codigo", codigo);

        List<Organismo> organismo = q.getResultList();
        if (organismo.size() == 1) {
            return organismo.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoEntidad(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.codigo = :codigo and organismo.entidad.id = :idEntidad");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismo = q.getResultList();
        if (organismo.size() == 1) {
            return organismo.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoEntidadLigero(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select organismo.id,organismo.codigo, organismo.denominacion, organismo.codAmbComunidad.id, organismo.estado.id, organismo.entidad.id from Organismo as organismo where " +
                "organismo.codigo = :codigo and organismo.entidad.id = :idEntidad and organismo.estado.codigoEstadoEntidad=:vigente");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) result.get(0)[3]));
            organismo.setEstado(catEstadoEntidadEjb.findById((Long) result.get(0)[4]));
            organismo.setEntidad(new Entidad ((Long) result.get(0)[5]));
            return organismo;
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select organismo from Organismo as organismo where " +
                "organismo.codigo = :codigo and organismo.entidad.id=:idEntidad");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);

        List<Organismo> organismo = q.getResultList();
        if (organismo.size() == 1) {
            return organismo.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoEntidadSinEstadoLigero(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select organismo.id,organismo.codigo, organismo.denominacion, organismo.codAmbComunidad.id, organismo.estado, organismo.edp, organismo.entidad.id from Organismo as organismo where " +
                "organismo.codigo = :codigo and organismo.entidad.id = :idEntidad ");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) result.get(0)[3]));
            organismo.setEstado((CatEstadoEntidad) result.get(0)[4]);
            organismo.setEdp((Boolean) result.get(0)[5]);
            organismo.setEntidad(new Entidad((Long)result.get(0)[6]));
            return organismo;
        } else {
            return null;
        }

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoLigero(String codigo) throws Exception {
        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion, organismo.codAmbComunidad.id, organismo.estado.id from Organismo as organismo where " +
                "organismo.codigo = :codigo");

        q.setParameter("codigo", codigo);

        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) result.get(0)[3]));
            organismo.setEstado(catEstadoEntidadEjb.findById((Long) result.get(0)[4]));
            return organismo;
        } else {
            return null;
        }
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoOtraEntidadConLibros(String codigo, Long idEntidadActiva) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion, organismo.codAmbComunidad.id, " +
                "organismo.estado.id from Organismo as organismo inner join organismo.libros as libros where " +
                "organismo.codigo = :codigo and organismo.entidad.id != :idEntidadActiva and organismo.entidad.activo = true ");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidadActiva", idEntidadActiva);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();

        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) result.get(0)[3]));
            organismo.setEstado(catEstadoEntidadEjb.findById((Long) result.get(0)[4]));
            return organismo;
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> findByEntidadReduce(Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.denominacion from Organismo as organismo where " +
                "organismo.entidad.id = :entidad");

        q.setParameter("entidad", entidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Organismo> organismos = new ArrayList<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0], (String) object[1]);
            organismos.add(org);
        }

        return organismos;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> findByEntidadByEstado(Long entidad, String estado) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion from Organismo as organismo where " +
                "organismo.entidad.id = :entidad and organismo.estado.codigoEstadoEntidad = :codigoEstado");

        q.setParameter("entidad", entidad);
        q.setParameter("codigoEstado", estado);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Organismo> organismos = new ArrayList<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0], (String) object[1], (String) object[2]);
            organismos.add(org);
        }

        return organismos;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoMultiEntidad(String codigo) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion, organismo.codAmbComunidad.id, organismo.estado.id, organismo.entidad.id from Organismo as organismo where organismo.codigo = :codigo order by organismo.id asc");

        q.setParameter("codigo", codigo);

        List<Object[]> result = q.getResultList();

        if (result.size() == 1) {

            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) result.get(0)[3]));
            organismo.setEstado(catEstadoEntidadEjb.findById((Long) result.get(0)[4]));
            organismo.setEntidad(new Entidad ((Long) result.get(0)[5]));
            return organismo;
        } else if (result.size() > 1) { //Caso multientidad ( encuentra 2)
            for (Object[] object : result) {
                Organismo raiz = getOrganismoRaiz((Long) object[0]);
                /* La condición que determina cual es el organismo que tiene una entidad que le da soporte es que su raiz sea null
                    eso quiere decir que realmente es un organismo que depende de otro, pero que está creado como entidad
                    y por eso su raiz es null, porque al importar los datos de dir3caib,
                    la raiz a la que pertenece no existe en el organigrama, porque no la trae
                 */
                if (raiz == null) {
                    Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2]);
                    organismo.setCodAmbComunidad(new CatComunidadAutonoma((Long) object[3]));
                    organismo.setEstado(catEstadoEntidadEjb.findById((Long) object[4]));
                    organismo.setEntidad(new Entidad ((Long) object[5]));
                    return organismo;
                }
            }
        }

        return null;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getAllByEntidadMultiEntidad(Long idEntidad) throws Exception{

        List<Organismo> organismos= getAllByEntidad(idEntidad);
        List<Organismo> organismosAEliminar = new ArrayList<>();

        for(Organismo organismo: organismos){
            Organismo organismo1= findByCodigoMultiEntidad(organismo.getCodigo());
            if(organismo1 != null) {// Multientidad o interno
                Organismo raiz = getOrganismoRaiz(organismo1.getId());
                //Si la raiz es null(es parte de otra entidad) y la entidad del organismo no es igual
                // a la entidad en la que estamos se debe eliminar el organismo porque significa que hay otra entidad
                // que le da servicio
                if(raiz == null && !idEntidad.equals(organismo1.getEntidad().getId())){
                    organismosAEliminar.add(organismo);
                }
            }
        }
        organismos.removeAll(organismosAEliminar);
        return organismos;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo findByCodigoByEntidadMultiEntidad(String codigo, Long idEntidad) throws Exception{

        if (multiEntidadEjb.isMultiEntidad()) {
            return findByCodigoMultiEntidad(codigo);
        } else {
            return findByCodigoEntidadLigero(codigo, idEntidad);
        }

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOrganismoInterno(String codigo, Long idEntidad) throws Exception {

        Organismo organismo = findByCodigoEntidad(codigo, idEntidad);

        if (organismo == null) {
            return false;
        }

        if (!organismo.getEdp()) { // Si no es EDP
            return true;
        } else {
            return organismo.getPermiteUsuarios() || organismo.getEdpPrincipal().getPermiteUsuarios();
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> organismosConOficinas(Long entidad) throws Exception {


        Query q = em.createQuery("Select distinct(organismo.id) , organismo.denominacion from Organismo as organismo, Oficina as oficina " +
                "inner join oficina.organismoResponsable as orgrespon " +
                "where orgrespon.id=organismo.id and oficina.estado.codigoEstadoEntidad=:codigoEstado and " +
                "organismo.entidad.id =:entidad and organismo.estado.codigoEstadoEntidad =:codigoEstado order by organismo.denominacion");

        q.setParameter("entidad", entidad);
        q.setParameter("codigoEstado", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        Set<Organismo> organismosConOficinas = new HashSet<Organismo>();
        for (Object[] object : result) {
            Organismo org = new Organismo((Long) object[0], (String) object[1]);
            organismosConOficinas.add(org);
        }


        Query q2 = em.createQuery("Select distinct(organismo.id) , organismo.denominacion from Organismo as organismo, RelacionOrganizativaOfi as relorgOfi " +
                "inner join relorgOfi.organismo as organizativa " +
                "where organizativa.id=organismo.id  and relorgOfi.estado.codigoEstadoEntidad=:codigoEstado and " +
                "organismo.entidad.id =:entidad and organismo.estado.codigoEstadoEntidad =:codigoEstado order by organismo.denominacion");

        q2.setParameter("entidad", entidad);
        q2.setParameter("codigoEstado", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result2 = q2.getResultList();
        Set<Organismo> organismosConOficinasFuncionales = new HashSet<Organismo>();
        for (Object[] object2 : result2) {
            Organismo org = new Organismo((Long) object2[0], (String) object2[1]);
            organismosConOficinasFuncionales.add(org);
        }
        organismosConOficinas.addAll(organismosConOficinasFuncionales);

        return new ArrayList<Organismo>(organismosConOficinas);

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getOrganismosByNivel(Long nivel, Long idEntidad, String estado) throws Exception {

        Query q = em.createQuery("Select organismo.id,organismo.codigo, organismo.denominacion, organismo.organismoSuperior.id, organismo.edp from Organismo as organismo where " +
                "organismo.nivelJerarquico = :nivel and organismo.entidad.id = :idEntidad and organismo.estado.codigoEstadoEntidad = :estado order by organismo.codigo");

        q.setParameter("nivel", nivel);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("estado", estado);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2], (Long) object[3], (Boolean) object[4]);

            organismos.add(organismo);
        }

        return organismos;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, Organismo organismo) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select organismo from Organismo as organismo ");

        if (StringUtils.isNotEmpty(organismo.getCodigo())) {
            where.add(DataBaseUtils.like("organismo.codigo", "codigo", parametros, organismo.getCodigo()));
        }
        if (StringUtils.isNotEmpty(organismo.getDenominacion())) {
            where.add(DataBaseUtils.like("organismo.denominacion", "denominacion", parametros, organismo.getDenominacion()));
        }
        if (organismo.getEstado().getId() != null && organismo.getEstado().getId() > 0) {
            where.add(" organismo.estado.id = :idCatEstadoEntidad");
            parametros.put("idCatEstadoEntidad", organismo.getEstado().getId());
        }

        /*where.add(" organismo.permiteUsuarios = :permiteUsuarios");
        parametros.put("permiteUsuarios", organismo.getPermiteUsuarios());*/

        // Añadimos la Entidad
        where.add("organismo.entidad.id = :idEntidad ");
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
            q2 = em.createQuery(query.toString().replaceAll("Select organismo from Organismo as organismo ", "Select count(organismo.id) from Organismo as organismo "));
            query.append("order by organismo.denominacion");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select organismo from Organismo as organismo ", "Select count(organismo.id) from Organismo as organismo "));
            query.append("order by organismo.denominacion");
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

        List<Organismo> organismos = q.getResultList();
        for (Organismo org : organismos) {
            Hibernate.initialize(org.getLibros());
            Hibernate.initialize(org.getOrganismoRaiz());
            Hibernate.initialize(org.getOrganismoSuperior());
        }
        paginacion.setListado(organismos);

        return paginacion;

    }

    /**
     * Método que obtiene los organismos vigentes y en los que puede registrar de la oficina activa,
     * sin generar OficioRemisión.
     *
     * @param oficinaActiva
     * @return List
     * @throws Exception
     */
    @Override
    public LinkedHashSet<Organismo> getByOficinaActiva(Oficina oficinaActiva, String estado) throws Exception {

        // Añadimos los organismos a los que da servicio la Oficina (Directos y Funcionales)
        LinkedHashSet<Organismo> organismos = oficinaActiva.getOrganismosFuncionales(estado);

        // Añadimos todos los hijos de los Organismos obtenidos anteriormetne
        LinkedHashSet<Organismo> hijosTotales = new LinkedHashSet<Organismo>();
        obtenerHijosOrganismos(organismos, hijosTotales, estado);
        organismos.addAll(hijosTotales);

        return organismos;

    }

    /**
     * Método que obtiene todos los organismo de la oficina activa sin generar OficioRemisión.
     * Este método permitirá mostrar el botón distribuir en caso de que el organismo esté extinguido,
     * anulado o transitorio, además de vigente
     *
     * @param oficinaActiva
     * @return List
     * @throws Exception
     */
    @Override
    public LinkedHashSet<Organismo> getAllByOficinaActiva(Oficina oficinaActiva) throws Exception {

        LinkedHashSet<Organismo> organismos = new LinkedHashSet<Organismo>();

        organismos.addAll(getByOficinaActiva(oficinaActiva, RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        organismos.addAll(getByOficinaActiva(oficinaActiva, RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO));
        organismos.addAll(getByOficinaActiva(oficinaActiva, RegwebConstantes.ESTADO_ENTIDAD_ANULADO));
        organismos.addAll(getByOficinaActiva(oficinaActiva, RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO));

        return organismos;

    }


    /**
     * Metodo que recursivamente obtiene todos los hijos de una lista de organismos
     *
     * @param organismosPadres organismos de los que obtener sus hijos
     * @param totales          organismos totales obtenidos despues del proceso recursivo.
     * @throws Exception
     */
    @SuppressWarnings(value = "unchecked")
    private void obtenerHijosOrganismos(LinkedHashSet<Organismo> organismosPadres, LinkedHashSet<Organismo> totales, String estado) throws Exception {

        // recorremos para todos los organismos Padres
        for (Organismo org : organismosPadres) {

            Query q = em.createQuery("select organismo.id,organismo.codigo, organismo.denominacion, organismo.edp from Organismo as organismo where organismo.organismoSuperior.id =:idOrganismoSuperior " +
                    "and organismo.estado.codigoEstadoEntidad =:estado and organismo.edp =:edp");
            q.setParameter("idOrganismoSuperior", org.getId());
            q.setParameter("estado", estado);

            // Si el organismo padre es EDP, buscamos sus hijos EDP
            if (org.getEdp()) {
                q.setParameter("edp", true);
            } else {
                q.setParameter("edp", false);
            }

            LinkedHashSet<Organismo> hijos = new LinkedHashSet<Organismo>();

            q.setHint("org.hibernate.readOnly", true);

            List<Object[]> result = q.getResultList();

            for (Object[] object : result) {
                Organismo hijo = new Organismo((Long) object[0], (String) object[1], (String) object[2], (Boolean) object[3]);

                hijos.add(hijo);
            }
            totales.addAll(hijos);

            // Hijos de cada organismo
            obtenerHijosOrganismos(hijos, totales, estado);


        }

    }

    /**
     * Método que nos devuelve los códigos DIR3 de las oficinas SIR de un organismo
     *
     * @param idOrganismo identificador del organismo
     * @return
     * @throws Exception
     */
    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> organismoSir(Long idOrganismo) throws Exception {
        List<String> oficinasSir = new ArrayList<String>();

        Query q = em.createQuery("Select relacionSirOfi from RelacionSirOfi as relacionSirOfi where " +
                "relacionSirOfi.organismo.id = :idOrganismo ");

        q.setHint("org.hibernate.readOnly", true);

        List<RelacionSirOfi> relSir = q.getResultList();
        if (!relSir.isEmpty()) {
            for (RelacionSirOfi rel : relSir) {
                oficinasSir.add(rel.getOficina().getCodigo());
            }
        }
        return oficinasSir;
    }


    /**
     * Activa la opción de permitir usuarios de un Organismo
     *
     * @param idOrganismo
     * @throws Exception
     */
    public void activarUsuarios(Long idOrganismo) throws Exception {

        em.createQuery("update from Organismo set permiteUsuarios = true where id  =:idOrganismo")
                .setParameter("idOrganismo", idOrganismo).executeUpdate();
    }

    /**
     * Desactiva la opción de permitir usuarios de un Organismo
     *
     * @param idOrganismo
     * @throws Exception
     */
    public void desactivarUsuarios(Long idOrganismo) throws Exception {

        em.createQuery("update from Organismo set permiteUsuarios = false where id  =:idOrganismo")
                .setParameter("idOrganismo", idOrganismo).executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getPermitirUsuarios(Long entidad) throws Exception {

        Query q = em.createQuery("Select organismo.id, organismo.codigo, organismo.denominacion from Organismo as organismo where " +
                "organismo.entidad.id = :entidad and organismo.permiteUsuarios = true and organismo.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("entidad", entidad);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2]);
            organismos.add(organismo);
        }

        return organismos;
    }

    @Override
    public Long getEntidad(Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select organismo.entidad.id from Organismo as organismo where " +
                "organismo.id = :idOrganismo");

        q.setParameter("idOrganismo", idOrganismo);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    public Organismo getOrganismoSuperior(Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select organismo.organismoSuperior.id, organismoSuperior.codigo from Organismo as organismo where " +
                "organismo.id = :idOrganismo");

        q.setParameter("idOrganismo", idOrganismo);
        q.setHint("org.hibernate.readOnly", true);


        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], null);
            return organismo;
        } else {
            return null;
        }
    }


    @Override
    public Organismo getOrganismoRaiz(Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select organismo.organismoRaiz.id, organismo.organismoRaiz.codigo, organismo.organismoRaiz.denominacion from Organismo as organismo where " +
                "organismo.id = :idOrganismo");

        q.setParameter("idOrganismo", idOrganismo);
        q.setHint("org.hibernate.readOnly", true);


        List<Object[]> result = q.getResultList();
        if (result.size() == 1) {
            Organismo organismo = new Organismo((Long) result.get(0)[0], (String) result.get(0)[1], (String) result.get(0)[2]);
            return organismo;
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> organismos = em.createQuery("Select distinct(id) from Organismo where entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = organismos.size();

        if (organismos.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (organismos.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = organismos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Historico UO eliminados: " + em.createNativeQuery("delete from RWE_HISTORICOUO WHERE CODULTIMA in(:organismos)").setParameter("organismos", subList).executeUpdate());
                em.createQuery("delete from Organismo where id in (:organismos) ").setParameter("organismos", subList).executeUpdate();
                organismos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            log.info("Historico UO eliminados: " + em.createNativeQuery("delete from RWE_HISTORICOUO WHERE CODULTIMA in(:organismos)").setParameter("organismos", organismos).executeUpdate());
            em.createQuery("delete from Organismo where id in (:organismos) ").setParameter("organismos", organismos).executeUpdate();
        }

        return total;

    }

    @Override
    public void obtenerHistoricosFinales(Long id, Set<Organismo> historicosFinales) throws Exception {
        Organismo org = em.find(Organismo.class, id);
        Hibernate.initialize(org.getHistoricoUO());
        Set<Organismo> parciales = org.getHistoricoUO();
        for (Organismo parcial : parciales) {
            Organismo par = em.find(Organismo.class, parcial.getId());
            Hibernate.initialize(par.getHistoricoUO());
            if (par.getHistoricoUO().size() == 0) {
                historicosFinales.add(par);
            } else {
                obtenerHistoricosFinales(par.getId(), historicosFinales);
            }
        }

    }

    @Override
    public Boolean isEdpConLibro(Long idOrganismo) throws Exception {

        Organismo organismo = findByIdCompleto(idOrganismo);

        if (organismo.getEdpPrincipal() == null) {

            return libroEjb.tieneLibro(organismo.getId());

        } else if (libroEjb.tieneLibro(idOrganismo)) {

            return true;

        } else {
            if(organismo.getOrganismoSuperior()!=null) {
                return isEdpConLibro(organismo.getOrganismoSuperior().getId());
            }else{
                return false;
            }
        }

    }

    @Override
    public Libro obtenerLibroRegistro(Long idOrganismo) throws Exception {

        Organismo organismo = findById(idOrganismo);

        if (organismo.getLibros() != null && organismo.getLibros().size() > 0) {
            return organismo.getLibros().get(0);

        } else if (organismo.getOrganismoSuperior() == null) {
            return null;

        } else {
            return obtenerLibroRegistro(organismo.getOrganismoSuperior().getId());
        }
    }


    @Override
    public UnidadTF obtenerDestinoExterno(String codigo) throws Exception {

        //Buscamos el destino externo a partir de su código
        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
        return unidadesService.buscarUnidad(codigo);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UnidadTF> obtenerSustitutosExternosSIR(String codigo) throws Exception {

        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
        return unidadesService.obtenerHistoricosFinalesSIR(codigo);

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UnidadTF> obtenerSustitutosExternos(String codigo) throws Exception {

        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
        return unidadesService.obtenerHistoricosFinales(codigo);

    }


}
