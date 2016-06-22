package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.distribucion.ConfiguracionDistribucion;
import org.fundaciobit.plugins.distribucion.IDistribucionPlugin;
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

@Stateless(name = "RegistroEntradaEJB")
@SecurityDomain("seycon")
public class RegistroEntradaBean extends RegistroEntradaCambiarEstadoBean
        implements RegistroEntradaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB(name = "LibroEJB")
    public LibroLocal libroEjb;

    @EJB(name = "ContadorEJB")
    public ContadorLocal contadorEjb;

    @EJB(name = "OficinaEJB")
    public OficinaLocal oficinaEjb;

    @EJB(name = "HistoricoRegistroEntradaEJB")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    public InteresadoLocal interesadoEjb;


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getByUsuario(Long idUsuarioEntidad) throws Exception {

        Query q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }


    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, List<Interesado> interesados)
            throws Exception, I18NException, I18NValidationException {
        return registrarEntrada(registroEntrada, usuarioEntidad,interesados, null);
    }


    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                            UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull)
            throws Exception, I18NException, I18NValidationException {


        // Obtenemos el Número de registro
        Libro libro = libroEjb.findById(registroEntrada.getLibro().getId());
        NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorEntrada().getId());
        registroEntrada.setNumeroRegistro(numeroRegistro.getNumero());
        registroEntrada.setFecha(numeroRegistro.getFecha());

        if (registroEntrada.getLibro().getCodigo() != null && registroEntrada.getOficina().getCodigo() != null) {
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, registroEntrada.getLibro(), registroEntrada.getOficina()));
        } else {
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, libroEjb.findById(registroEntrada.getLibro().getId()), oficinaEjb.findById(registroEntrada.getOficina().getId())));
        }

        // Si no ha introducido ninguna fecha de Origen
        if (registroEntrada.getRegistroDetalle().getFechaOrigen() == null) {
            registroEntrada.getRegistroDetalle().setFechaOrigen(registroEntrada.getFecha());
        }

        //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
        if (StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen())) {

            registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
        }

        // Guardar RegistroEntrada
        registroEntrada = persist(registroEntrada);

        // Guardar el HistorioRegistroEntrada
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" ),false);

        // Procesamos los Interesados
        if(interesados != null && interesados.size() > 0){
            interesadoEjb.guardarInteresados(interesados, registroEntrada.getRegistroDetalle());
        }

        // TODO Controlar custodyID y si hay fallo borrar todos los Custody
        if (anexosFull != null && anexosFull.size() != 0) {
            final Long registroID = registroEntrada.getId();
            for (AnexoFull anexoFull : anexosFull) {

                anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
                anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "entrada");

            }
        }

        return registroEntrada;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, String organoDest, Boolean anexos, String observaciones, String usuario) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        String queryBase = "Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat ";
        StringBuffer query = new StringBuffer(queryBase);

        // Numero registro
        if (!StringUtils.isEmpty(registroEntrada.getNumeroRegistroFormateado())) {
            where.add(" registroEntrada.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + registroEntrada.getNumeroRegistroFormateado() + "%");
        }

        // Extracto
        if (!StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getExtracto())) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.extracto", "extracto", parametros, registroEntrada.getRegistroDetalle().getExtracto()));
        }

        // Observaciones
        if (!StringUtils.isEmpty(observaciones)) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (!StringUtils.isEmpty(usuario)) {
            where.add(DataBaseUtils.like("registroEntrada.usuario.usuario.identificador", "usuario", parametros, usuario));
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

        // Organismo destinatario
        if (!StringUtils.isEmpty((organoDest))) {
            Organismo organismo = organismoEjb.findByCodigoLigero(organoDest);
            if (organismo == null) {
                where.add(" registroEntrada.destinoExternoCodigo = :organoDest ");
            } else {
                where.add(" registroEntrada.destino.codigo = :organoDest ");
            }

            parametros.put("organoDest", organoDest);
        }

        // Estado registro
        if (registroEntrada.getEstado() != null && registroEntrada.getEstado() > 0) {
            where.add(" registroEntrada.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", registroEntrada.getEstado());
        }

        // Oficina Registro
        if (registroEntrada.getOficina().getId() != null && registroEntrada.getOficina().getId() > 0) {
            where.add(" registroEntrada.oficina.id = :idOficina ");
            parametros.put("idOficina", registroEntrada.getOficina().getId());
        }

        // Intervalo fechas
        where.add(" (registroEntrada.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroEntrada.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Libro
        where.add(" registroEntrada.libro.id = :idLibro");
        parametros.put("idLibro", registroEntrada.getLibro().getId());

        // Buscamos registros de entrada con anexos
        if (anexos) {
            where.add(" registroEntrada.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
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
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select count(DISTINCT registroEntrada.id) from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat "));
            query.append(" order by registroEntrada.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            // Duplicamos la query solo para obtener los resultados totales
            q2 = em.createQuery(query.toString().replaceAll(queryBase, "Select DISTINCT count(registroEntrada.id) from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat "));
            query.append("order by registroEntrada.id desc");
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
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> buscaLibroRegistro(Date fechaInicio, Date fechaFin, String numRegistro, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String extracto, String usuario, List<Libro> libros, Long estado, Long idOficina, Long idTipoAsunto, String organoDest) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat ");


        // Numero registro
        if (!StringUtils.isEmpty(numRegistro)) {
            where.add(" registroEntrada.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + numRegistro + "%");
        }

        // Extracto
        if (!StringUtils.isEmpty(extracto)) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.extracto", "extracto", parametros, extracto));
        }

        // Organismo destinatario
        if (!StringUtils.isEmpty((organoDest))) {
            if (organismoEjb.findByCodigoLigero(organoDest) == null) {
                where.add(" registroEntrada.destinoExternoCodigo = :organoDest ");
            } else {
                where.add(" registroEntrada.destino.codigo = :organoDest ");
            }
            parametros.put("organoDest", organoDest);
        }

        // Observaciones
        if (!StringUtils.isEmpty(observaciones)) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (!StringUtils.isEmpty(usuario)) {
            where.add(DataBaseUtils.like("registroEntrada.usuario.usuario.identificador", "usuario", parametros, usuario));
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
            where.add(" registroEntrada.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", estado);
        }

        // Oficina Registro
        if (idOficina != -1) {
            where.add(" registroEntrada.oficina.id = :idOficina ");
            parametros.put("idOficina", idOficina);
        }

        // Tipo Asunto
        if (idTipoAsunto != -1) {
            where.add(" registroEntrada.registroDetalle.tipoAsunto.id = :idTipoAsunto ");
            parametros.put("idTipoAsunto", idTipoAsunto);
        }

        // Intervalo fechas
        where.add(" (registroEntrada.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroEntrada.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Libro
        where.add(" registroEntrada.libro in (:libros)");
        parametros.put("libros", libros);

        // Buscamos registros de entrada con anexos
        if (anexos) {
            where.add(" registroEntrada.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
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

            query.append(" order by registroEntrada.fecha desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }

        } else {
            query.append("order by registroEntrada.fecha desc");
            q = em.createQuery(query.toString());
        }

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long buscaIndicadoresTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and " +
                "registroEntrada.libro.organismo.entidad.id = :idEntidad ");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long buscaIndicadoresOficinaTotal(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and " +
                "registroEntrada.oficina.id = :idOficina ");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long buscaEntradaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.oficina.organismoResponsable.id = :conselleria and registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("conselleria", conselleria);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long buscaEntradaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.tipoAsunto.id = :tipoAsunto and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and registroEntrada.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("tipoAsunto", tipoAsunto);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.idioma = :idioma and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and registroEntrada.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.idioma = :idioma and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente and registroEntrada.oficina.id = :idOficina");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.libro.id = :libro and registroEntrada.estado != :anulado  and registroEntrada.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libro", libro);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.oficina.id = :oficina and registroEntrada.estado != :anulado and registroEntrada.estado != :pendiente");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("oficina", oficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroBasico> getByOficinaEstado(Long idOficinaActiva, Long idEstado, Integer total) throws Exception {

        Query q;

        String s = "re.registroDetalle.extracto ";

        if (idEstado.equals(RegwebConstantes.REGISTRO_PENDIENTE)) {
            s = "re.registroDetalle.reserva ";
        }

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, " + s +
                "from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

        return getRegistroBasicoList(q.getResultList());

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getByOficinaEstado(Long idOficinaActiva, Long idEstado) throws Exception {

        Query q;


        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

        return q.getResultList();

    }

    @Override
    public Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> buscaEntradaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception {

        Query q;

        q = em.createQuery("Select registroEntrada.numeroRegistro, registroEntrada.fecha, registroEntrada.libro.nombre, " +
                "registroEntrada.oficina.denominacion, registroEntrada.libro.organismo.denominacion " +
                "from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.usuario.id = :idUsuario and registroEntrada.libro in (:libros) order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);

        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosEntrada.add(registroEntrada);
        }

        return registrosEntrada;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> buscaEntradaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception {

        Query q;

        q = em.createQuery("Select registroEntrada.numeroRegistro, registroEntrada.fecha, registroEntrada.libro.nombre, " +
                "registroEntrada.oficina.denominacion, registroEntrada.libro.organismo.denominacion " +
                "from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.usuario.id = :idUsuario and " +
                "registroEntrada.libro.id = :idLibro and registroEntrada.estado != :pendiente order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("pendiente", RegwebConstantes.REGISTRO_PENDIENTE);

        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosEntrada.add(registroEntrada);
        }

        return registrosEntrada;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getByLibrosEstado(List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select re from RegistroEntrada as re where re.libro in (:libros) " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.libro in (:libros) " +
                "and re.estado = :idEstado");

        q.setParameter("libros", libros);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> buscaPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroEntrada.id, registroEntrada.numeroRegistro, registroEntrada.fecha, " +
                "registroEntrada.libro.id, registroEntrada.libro.nombre, registroEntrada.oficina.denominacion, registroEntrada.libro.organismo.denominacion " +
                "from RegistroEntrada as registroEntrada ");

        if (fechaInicio != null) {
            where.add(" registroEntrada.fecha >= :fechaInicio");
            parametros.put("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            where.add(" registroEntrada.fecha <= :fechaFin");
            parametros.put("fechaFin", fechaFin);
        }
        if (idLibro != null && idLibro > 0) {
            where.add(" registroEntrada.libro.id = :idLibro");
            parametros.put("idLibro", idLibro);
        }
        if (numeroRegistro != null && numeroRegistro > 0) {
            where.add(" registroEntrada.numeroRegistro = :numeroRegistro");
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
        query.append(" order by registroEntrada.id desc");
        q = em.createQuery(query.toString());

        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
        }

        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada((Long) object[0], (Integer) object[1], (Date) object[2], (Long) object[3], (String) object[4], (String) object[5], (String) object[6]);

            registrosEntrada.add(registroEntrada);
        }

        return registrosEntrada;
    }

    @Override
    public void cambiarEstado(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();

        // Creamos el HistoricoRegistroEntrada para la modificación d estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada,
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), false);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroBasico> getUltimosRegistros(Long idOficina, Integer total) throws Exception {

        Query q;

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, re.registroDetalle.extracto " +
                "from RegistroEntrada as re where re.oficina.id = :idOficina " +
                "and re.estado = :valido " +
                "order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficina", idOficina);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);

        return getRegistroBasicoList(q.getResultList());
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception {

        Query q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.numeroRegistroFormateado = :numeroRegistroFormateado ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception {

        Query q = em.createQuery("Select registroEntrada "
                + " from RegistroEntrada as registroEntrada"
                + " where registroEntrada.numeroRegistro = :numero "
                + " AND  YEAR(registroEntrada.fecha) = :anyo "
                + " AND  registroEntrada.libro.codigo = :libro ");

        q.setParameter("numero", numero);
        q.setParameter("anyo", anyo);
        q.setParameter("libro", libro);

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    public void anularRegistroEntrada(RegistroEntrada registroEntrada,
                                      UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroEntrada old = registroEntrada;

        // Estado anulado
        cambiarEstado(registroEntrada, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad);

    }

    @Override
    public void activarRegistroEntrada(RegistroEntrada registroEntrada,
                                       UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroEntrada old = registroEntrada;

        // Actualizamos el estado del RegistroEntrada
        cambiarEstado(registroEntrada, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

    }

    @Override
    public void visarRegistroEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroEntrada old = registroEntrada;

        // Modificamos el estado del RegistroEntrada
        cambiarEstado(registroEntrada, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);

    }

    @Override
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada,
                                        UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroEntrada old = registroEntrada;

        cambiarEstado(registroEntrada, RegwebConstantes.REGISTRO_TRAMITADO, usuarioEntidad);

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getLibro(Long idRegistroEntrada) throws Exception {

        Query q;

        q = em.createQuery("Select registroEntrada.libro.id from RegistroEntrada as registroEntrada where registroEntrada.id = :idRegistroEntrada ");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);

        List<Long> libros = q.getResultList();

        if (libros.size() > 0) {
            return libros.get(0);
        } else {
            return null;
        }
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
            //RegistroBasico registroBasico = new RegistroBasico((Long)object[0],(String)object[1],(Date)object[2],(String)object[3],(String)object[4],(String)object[5]);
            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long) object[0]);
            registroBasico.setNumeroRegistroFormateado((String) object[1]);
            registroBasico.setFecha((Date) object[2]);
            registroBasico.setLibro((String) object[3]);
            registroBasico.setUsuario((String) object[4]);
            if (StringUtils.isEmpty((String) object[5])) {
                registroBasico.setExtracto((String) object[6]);
            } else {
                registroBasico.setExtracto((String) object[5]);
            }
            registros.add(registroBasico);

        }

        return registros;
    }

    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb3.model.utils.RegistroBasico}
     *
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoCompleto(List<Object[]> result) throws Exception {

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        log.info("Total registros: " + result.size());

        for (Object[] object : result) {
            //RegistroBasico registroBasico = new RegistroBasico((Long)object[0],(String)object[1],(Date)object[2],(String)object[3],(String)object[4],(String)object[5]);
            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long) object[0]);
            registroBasico.setNumeroRegistroFormateado((String) object[1]);
            registroBasico.setFecha((Date) object[2]);
            registroBasico.setLibro((String) object[3]);
            registroBasico.setUsuario((String) object[4]);
            if (StringUtils.isEmpty((String) object[5])) {
                registroBasico.setExtracto((String) object[6]);
            } else {
                registroBasico.setExtracto((String) object[5]);
            }


            registroBasico.setIdOficina((Long) object[7]);
            registroBasico.setOficina((String) object[8]);
            if (StringUtils.isEmpty((String) object[9])) {
                registroBasico.setDestinatario((String) object[10]);
            } else {
                registroBasico.setDestinatario((String) object[9]);
            }
            registroBasico.setEstado((Long) object[11]);
            //registroBasico.setAnexos((Long)object[12]);
            registros.add(registroBasico);
        }

        return registros;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> registros = em.createQuery("Select distinct(re.id) from RegistroEntrada as re where re.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }

    @Override
    public Long getTotalByLibro(Long idLibro) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.libro.id = :idLibro ");

        q.setParameter("idLibro", idLibro);

        return (Long) q.getSingleResult();
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return (Long) q.getSingleResult() > 0;
    }

    public Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws Exception {
        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.id = :idRegistroEntrada and re.estado = :idEstado ");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);
        q.setParameter("idEstado", idEstado);

        return (Long) q.getSingleResult() > 0;
    }

    public RegistroEntrada getConAnexosFull(Long id) throws Exception, I18NException {

        RegistroEntrada re = findById(id);
        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFullCompleto(anexo.getId());
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        re.getRegistroDetalle().setAnexosFull(anexosFull);
        return re;
    }


    public RespuestaDistribucion distribuir(RegistroEntrada re) throws Exception, I18NException {
        RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();
        respuestaDistribucion.setHayPlugin(false);
        respuestaDistribucion.setDestinatarios(null);

        //Obtenemos plugin
        IDistribucionPlugin distribucionPlugin = RegwebDistribucionPluginManager.getInstance();
        //Si han especificado plug-in
        if (distribucionPlugin != null) {
            respuestaDistribucion.setHayPlugin(true);
            //Obtenemos la configuración de la distribución
            ConfiguracionDistribucion configuracionDistribucion = distribucionPlugin.configurarDistribucion();
            re = configuracionAnexosDistribucion(re, configuracionDistribucion.configuracionAnexos);
            respuestaDistribucion.setListadoDestinatariosModificable(configuracionDistribucion.isListadoDestinatariosModificable());
            if (configuracionDistribucion.listadoDestinatariosModificable) {// Si es modificable, mostraremos pop-up
                respuestaDistribucion.setDestinatarios(distribucionPlugin.distribuir(re)); // isListado = true , puede escoger a quien lo distribuye de la listas propuestas.
                //Despues lo tramita en una segunda fase desde el metodo distribuir() en distribuir.js
            } else { // Si no es modificable, obtendra los destinatarios del propio registro y nos saltamos una llamada al plugin
                respuestaDistribucion.setEnviado(distribucionPlugin.enviarDestinatarios(re, null, ""));
            }
        }

        return respuestaDistribucion;
    }


    public Boolean enviar(RegistroEntrada re, DestinatarioWrapper wrapper) throws Exception, I18NException {

        IDistribucionPlugin distribucionPlugin = RegwebDistribucionPluginManager.getInstance();
        if (distribucionPlugin != null) {
            ConfiguracionDistribucion configuracionDistribucion = distribucionPlugin.configurarDistribucion();
            re = configuracionAnexosDistribucion(re, configuracionDistribucion.configuracionAnexos);
            return distribucionPlugin.enviarDestinatarios(re, wrapper.getDestinatarios(), wrapper.getObservaciones());
        }
        return false;

    }

    /**
     * Método que prepara el registro de entrada para distribuirlo.
     * La variable confAnexos indica que datos se envian en el segmento de anexo del registro de entrada.
     * <p/>
     * 1 = custodiaId + metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
     * 2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
     * 3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
     *
     * @param original
     * @param confAnexos
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroEntrada configuracionAnexosDistribucion(RegistroEntrada original, int confAnexos) throws Exception, I18NException {
        switch (confAnexos) {
            case 1: {//1.  Fitxer + firma + metadades + custodiaId
                List<Anexo> anexos = original.getRegistroDetalle().getAnexos();
                List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
                for (Anexo anexo : anexos) {
                    AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId());
                    if (anexoFull != null) {
                        log.info(anexoFull.getDocumentoCustody().getName());
                    }
                    anexosFull.add(anexoFull);
                }
                //Asignamos los documentos recuperados de custodia al registro de entrada.
                original.getRegistroDetalle().setAnexosFull(anexosFull);
            }
            case 2: {//2. custodiaId

                // Montamos una nueva lista de anexos solo con el custodiaID, sin metadatos ni nada
                List<Anexo> anexos = original.getRegistroDetalle().getAnexos();
                List<Anexo> nuevosAnexos = new ArrayList<Anexo>();
                for (Anexo anexo : anexos) {
                    Anexo nuevoAnexo = new Anexo();
                    nuevoAnexo.setCustodiaID(anexo.getCustodiaID());
                    nuevosAnexos.add(nuevoAnexo);
                }
                original.getRegistroDetalle().setAnexos(nuevosAnexos);
            }
            // 3. custodiaId + metadades (No hace falta hacer nada es el caso por defecto)

        }
        return original;
    }


}
