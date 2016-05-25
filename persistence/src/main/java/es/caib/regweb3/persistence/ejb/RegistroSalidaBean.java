package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

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
 *         Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaEJB")
@SecurityDomain("seycon")
public class RegistroSalidaBean extends RegistroSalidaCambiarEstadoBean
        implements RegistroSalidaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb3/ContadorEJB/local")
    public ContadorLocal contadorEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;


    @Override
    public List<RegistroSalida> getByUsuario(Long idUsuarioEntidad) throws Exception {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public RegistroSalida registrarSalida(RegistroSalida registroSalida)
            throws Exception, I18NException, I18NValidationException {
        return registrarSalida(registroSalida, null, null);
    }


    @Override
    public synchronized RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                                       UsuarioEntidad usuarioEntidad, List<AnexoFull> anexos)
            throws Exception, I18NException, I18NValidationException {

        // Obtenemos el Número de registro
        Libro libro = libroEjb.findById(registroSalida.getLibro().getId());
        NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorSalida().getId());
        registroSalida.setNumeroRegistro(numeroRegistro.getNumero());
        registroSalida.setFecha(numeroRegistro.getFecha());
        if (registroSalida.getLibro().getCodigo() != null && registroSalida.getOficina().getCodigo() != null) {
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, registroSalida.getLibro(), registroSalida.getOficina()));
        } else {
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, libroEjb.findById(registroSalida.getLibro().getId()), oficinaEjb.findById(registroSalida.getOficina().getId())));
        }

        // Si no ha introducido ninguna fecha de Origen
        if (registroSalida.getRegistroDetalle().getFechaOrigen() == null) {
            registroSalida.getRegistroDetalle().setFechaOrigen(registroSalida.getFecha());
        }

        List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
        if (interesados != null && interesados.size() != 0) {
            for (Interesado interesado : interesados) {
                interesado.setRegistroDetalle(registroSalida.getRegistroDetalle());
            }
        }


        // Guardamos el RegistroSalida
        registroSalida = persist(registroSalida);

        //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
        if (StringUtils.isEmpty(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen())) {

            registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());

            registroSalida = merge(registroSalida);
        }


        // TODO Controlar custodyID y si hay fallo borrar todos los Custody
        if (anexos != null && anexos.size() != 0) {
            final Long registroID = registroSalida.getId();
            for (AnexoFull anexoFull : anexos) {
                anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
                anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "salida");
            }
        }


        return registroSalida;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, String organoOrigen, Boolean anexos, String observaciones, String usuario) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        String queryBase = "Select DISTINCT registroSalida from RegistroSalida as registroSalida left outer join registroSalida.registroDetalle.interesados interessat ";
        StringBuffer query = new StringBuffer(queryBase);

        // Numero registro
        if (!StringUtils.isEmpty(registroSalida.getNumeroRegistroFormateado())) {
            where.add(" registroSalida.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + registroSalida.getNumeroRegistroFormateado() + "%");
        }

        // Extracto
        if (!StringUtils.isEmpty(registroSalida.getRegistroDetalle().getExtracto())) {
            where.add(DataBaseUtils.like("registroSalida.registroDetalle.extracto", "extracto", parametros, registroSalida.getRegistroDetalle().getExtracto()));
        }

        // Observaciones
        if (!StringUtils.isEmpty(observaciones)) {
            where.add(DataBaseUtils.like("registroSalida.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (!StringUtils.isEmpty(usuario)) {
            where.add(DataBaseUtils.like("registroSalida.usuario.usuario.identificador", "usuario", parametros, usuario));
        }

        // Nombre interesado
        if (!StringUtils.isEmpty(interesadoNom)) {
            where.add("((" + DataBaseUtils.like("interessat.nombre", "interesadoNom", parametros, interesadoNom) +
                    ") or (" + DataBaseUtils.like("interessat.razonSocial", "interesadoNom", parametros, interesadoNom) +
                    "))");
        }

        // Primer apellido interesado
        if (!StringUtils.isEmpty(interesadoLli1)) {
            where.add(DataBaseUtils.like("interessat.apellido1", "interesadoLli1", parametros, interesadoLli1));
        }

        // Segundo apellido interesado
        if (!StringUtils.isEmpty(interesadoLli2)) {
            where.add(DataBaseUtils.like("interessat.apellido2", "interesadoLli2", parametros, interesadoLli2));
        }

        // Documento interesado
        if (!StringUtils.isEmpty(interesadoDoc)) {
            where.add(" (UPPER(interessat.documento) LIKE UPPER(:interesadoDoc)) ");
            parametros.put("interesadoDoc", "%" + interesadoDoc.trim() + "%");
        }

        // Organismo origen
        if (!StringUtils.isEmpty((organoOrigen))) {
            Organismo organismo = organismoEjb.findByCodigoLigero(organoOrigen);
            if (organismo == null) {
                where.add(" registroSalida.origenExternoCodigo = :organoOrigen ");
            } else {
                where.add(" registroSalida.origen.codigo = :organoOrigen ");
            }

            parametros.put("organoOrigen", organoOrigen);
        }

        // Estado registro
        if (registroSalida.getEstado() != null && registroSalida.getEstado() > 0) {
            where.add(" registroSalida.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", registroSalida.getEstado());
        }

        // Oficina Registro
        if (registroSalida.getOficina().getId() != null && registroSalida.getOficina().getId() > 0) {
            where.add(" registroSalida.oficina.id = :idOficina ");
            parametros.put("idOficina", registroSalida.getOficina().getId());
        }

        // Intervalo fechas
        where.add(" (registroSalida.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroSalida.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Libro
        where.add(" registroSalida.libro.id = :idLibro");
        parametros.put("idLibro", registroSalida.getLibro().getId());

        // Buscamos registros de sañida con anexos
        if (anexos) {
            where.add(" registroSalida.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
        }

        // Añadimos los parámetros a la query
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
            // Duplicamos la query solo para obtener los resultados totales
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select count(DISTINCT registroSalida.id) from RegistroSalida as registroSalida left outer join registroSalida.registroDetalle.interesados interessat "));
            query.append(" order by registroSalida.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            // Duplicamos la query solo para obtener los resultados totales
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select count(DISTINCT registroSalida.id) from RegistroSalida as registroSalida left outer join registroSalida.registroDetalle.interesados interessat "));
            query.append("order by registroSalida.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion = null;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public List<RegistroSalida> buscaLibroRegistro(Date fechaInicio, Date fechaFin, String numRegistro, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String extracto, String usuario, List<Libro> libros, Long estado, Long idOficina, Long idTipoAsunto, String organoOrig) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select DISTINCT registroSalida from RegistroSalida as registroSalida left outer join registroSalida.registroDetalle.interesados interessat ");

        // Numero registro
        if (!StringUtils.isEmpty(numRegistro)) {
            where.add(" registroSalida.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + numRegistro + "%");
        }

        // Extracto
        if (!StringUtils.isEmpty(extracto)) {
            where.add(DataBaseUtils.like("registroSalida.registroDetalle.extracto", "extracto", parametros, extracto));
        }

        // Organismo origen
        if (!StringUtils.isEmpty((organoOrig))) {
            Organismo organismo = organismoEjb.findByCodigoLigero(organoOrig);
            if (organismo == null) {
                where.add(" registroSalida.origenExternoCodigo = :organoOrig ");
            } else {
                where.add(" registroSalida.origen.codigo = :organoOrig ");
            }

            parametros.put("organoOrig", organoOrig);
        }

        // Observaciones
        if (!StringUtils.isEmpty(observaciones)) {
            where.add(DataBaseUtils.like("registroSalida.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (!StringUtils.isEmpty(usuario)) {
            where.add(DataBaseUtils.like("registroSalida.usuario.usuario.identificador", "usuario", parametros, usuario));
        }

        // Nombre interesado
        if (!StringUtils.isEmpty(interesadoNom)) {
            where.add("((" + DataBaseUtils.like("interessat.nombre", "interesadoNom", parametros, interesadoNom) +
                    ") or (" + DataBaseUtils.like("interessat.razonSocial", "interesadoNom", parametros, interesadoNom) +
                    "))");
        }

        // Primer apellido interesado
        if (!StringUtils.isEmpty(interesadoLli1)) {
            where.add(DataBaseUtils.like("interessat.apellido1", "interesadoLli1", parametros, interesadoLli1));
        }

        // Segundo apellido interesado
        if (!StringUtils.isEmpty(interesadoLli2)) {
            where.add(DataBaseUtils.like("interessat.apellido2", "interesadoLli2", parametros, interesadoLli2));
        }

        // Documento interesado
        if (!StringUtils.isEmpty(interesadoDoc)) {
            where.add(" (UPPER(interessat.documento) LIKE UPPER(:interesadoDoc)) ");
            parametros.put("interesadoDoc", "%" + interesadoDoc.trim() + "%");
        }

        // Estado registro
        if (estado != null && estado > 0) {
            where.add(" registroSalida.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", estado);
        }

        // Oficina Registro
        if (idOficina != -1) {
            where.add(" registroSalida.oficina.id = :idOficina ");
            parametros.put("idOficina", idOficina);
        }

        // Tipo Asunto
        if (idTipoAsunto != -1) {
            where.add(" registroSalida.registroDetalle.tipoAsunto.id = :idTipoAsunto ");
            parametros.put("idTipoAsunto", idTipoAsunto);
        }

        // Intervalo fechas
        where.add(" (registroSalida.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroSalida.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Libro
        where.add(" registroSalida.libro in (:libros)");
        parametros.put("libros", libros);

        // Buscamos registros de entrada con anexos
        if (anexos) {
            where.add(" registroSalida.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
        }

        // Añadimos los parámetros a la query
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

            query.append(" order by registroSalida.fecha desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }

        } else {
            query.append("order by registroSalida.fecha desc");
            q = em.createQuery(query.toString());
        }

        return q.getResultList();
    }

    @Override
    public Long buscaIndicadoresTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count (registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.estado != :anulado and registroSalida.estado != :pendiente and " +
                "registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaIndicadoresOficinaTotal(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.estado != :anulado and registroSalida.estado != :pendiente and " +
                "registroSalida.oficina.id = :idOficina ");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.oficina.organismoResponsable.id = :conselleria and registroSalida.estado != 8 and registroSalida.estado != 2");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("conselleria", conselleria);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.tipoAsunto.id = :tipoAsunto and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :pendiente and registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("tipoAsunto", tipoAsunto);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.idioma = :idioma and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :pendiente and registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.idioma = :idioma and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :pendiente and registroSalida.oficina.id = :idOficina");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.libro.id = :libro and registroSalida.estado != 8 and registroSalida.estado != 2");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libro", libro);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.oficina.id = :oficina and registroSalida.estado != :anulado and registroSalida.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("oficina", oficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<RegistroSalida> buscaSalidaPorUsuario(Date fechaInicio, Date fechaFin, Long usuario, List<Libro> libros) throws Exception {

        Query q;

        q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.usuario.id = :usuario and registroSalida.libro in (:libros) order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("usuario", usuario);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<RegistroSalida> buscaSalidaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception {

        Query q;

        q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.usuario.id = :idUsuario and registroSalida.libro.id = :idLibro and registroSalida.estado != :pendiente order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return q.getResultList();
    }


    @Override
    public RegistroSalida findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception {

        Query q = em.createQuery("Select registroSalida "
                + " from RegistroSalida as registroSalida"
                + " where registroSalida.numeroRegistro = :numero "
                + " AND  YEAR(registroSalida.fecha) = :anyo "
                + " AND  registroSalida.libro.codigo = :libro ");

        q.setParameter("numero", numero);
        q.setParameter("anyo", anyo);
        q.setParameter("libro", libro);

        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    public List<RegistroSalida> buscaPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroSalida from RegistroSalida as registroSalida ");

        if (fechaInicio != null) {
            where.add(" registroSalida.fecha >= :fechaInicio");
            parametros.put("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            where.add(" registroSalida.fecha <= :fechaFin");
            parametros.put("fechaFin", fechaFin);
        }
        if (idLibro != null && idLibro > 0) {
            where.add(" registroSalida.libro.id = :idLibro");
            parametros.put("idLibro", idLibro);
        }
        if (numeroRegistro != null && numeroRegistro > 0) {
            where.add(" registroSalida.numeroRegistro = :numeroRegistro");
            parametros.put("numeroRegistro", numeroRegistro);
        }

        query.append("where ");
        int count = 0;
        for (String w : where) {
            if (count != 0) {
                query.append(" and ");
            }
            query.append(w);
            count++;
        }
        query.append(" order by registroSalida.id desc");
        q = em.createQuery(query.toString());

        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
        }

        return q.getResultList();
    }


    @Override
    public List<RegistroBasico> getUltimosRegistros(Long idOficina, Integer total) throws Exception {

        Query q;

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, re.registroDetalle.extracto " +
                "from RegistroSalida as re where re.oficina.id = :idOficina " +
                "and re.estado = :idEstadoRegistro " +
                "order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficina", idOficina);
        q.setParameter("idEstadoRegistro", RegwebConstantes.REGISTRO_VALIDO);

        return getRegistroBasicoList(q.getResultList());
    }

    @Override
    public RegistroSalida findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida where registroSalida.numeroRegistroFormateado = :numeroRegistroFormateado ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);

        List<RegistroSalida> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroSalida old = registroSalida;

        // Estado anulado
        registroSalida.setEstado(RegwebConstantes.REGISTRO_ANULADO);

        // Actualizamos el RegistroSalida
        registroSalida = merge(registroSalida);

        // Creamos el HistoricoRegistroSalida para la modificación d estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(old, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ESTADO, false);

    }

    @Override
    public void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroSalida old = registroSalida;

        // Estado anulado
        registroSalida.setEstado(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

        // Actualizamos el RegistroSalida
        merge(registroSalida);

        // Creamos el HistoricoRegistroSalida para la modificación d estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(old, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ESTADO, false);

    }


    @Override
    public void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroSalida old = registroSalida;

        // Estado anulado
        registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);

        // Actualizamos el RegistroSalida
        merge(registroSalida);

        // Creamos el HistoricoRegistroSalida para la modificación d estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(old, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ESTADO, false);

    }


    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb3.model.utils.RegistroBasico}
     *
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoList(List<Object[]> result) throws Exception {

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        for (Object[] object : result) {
            RegistroBasico registroBasico = new RegistroBasico((Long) object[0], (String) object[1], (Date) object[2], (String) object[3], (String) object[4], (String) object[5]);

            registros.add(registroBasico);
        }

        return registros;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List registros = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where rs.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }

    @Override
    public Long getLibro(Long idRegistroSalida) throws Exception {

        Query q;

        q = em.createQuery("Select registroSalida.libro.id from RegistroSalida as registroSalida where registroSalida.id = :idRegistroSalida ");

        q.setParameter("idRegistroSalida", idRegistroSalida);

        List<Long> libros = q.getResultList();

        if (libros.size() > 0) {
            return libros.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroSalida as re where re.libro in (:libros) " +
                "and re.estado = :idEstado");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<RegistroSalida> getByLibrosEstado(List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select re from RegistroSalida as re where re.libro in (:libros) " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        return q.getResultList();

    }

    @Override
    public Long getTotalByLibro(Long idLibro) throws Exception {

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.libro.id = :idLibro ");

        q.setParameter("idLibro", idLibro);

        return (Long) q.getSingleResult();
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(rs.id) from RegistroSalida as rs where rs.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return (Long) q.getSingleResult() > 0;
    }

}
