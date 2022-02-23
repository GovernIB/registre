package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 02/10/14
 */

@Stateless(name = "LopdEJB")
@SecurityDomain("seycon")
public class LopdBean extends BaseEjbJPA<Lopd, Long> implements LopdLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public Lopd getReference(Long id) throws Exception {

        return em.getReference(Lopd.class, id);
    }

    @Override
    public Lopd findById(Long id) throws Exception {

        return em.find(Lopd.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Lopd> getAll() throws Exception {

        return  em.createQuery("Select lopd from Lopd as lopd order by lopd.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(lopd.id) from Lopd as lopd");

        return (Long) q.getSingleResult();
    }


    @Override
    public Paginacion getByFechasUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, List<Libro> libros, Long accion, Long tipoRegistro) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select lopd from Lopd as lopd where lopd.usuario.id = :idUsuarioEntidad and " +
                "lopd.fecha >= :fechaInicio and lopd.fecha <= :fechaFin and lopd.accion = :accion and " +
                "lopd.tipoRegistro = :tipoRegistro and lopd.libro in (:libros) order by lopd.fecha desc");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libros", libros);
        q.setParameter("accion", accion);
        q.setParameter("tipoRegistro", tipoRegistro);

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(lopd.id) from Lopd as lopd where lopd.usuario.id = :idUsuarioEntidad and " +
                "lopd.fecha >= :fechaInicio and lopd.fecha <= :fechaFin and lopd.accion = :accion and " +
                "lopd.tipoRegistro = :tipoRegistro and lopd.libro in (:libros)");

        q2.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("libros", libros);
        q2.setParameter("accion", accion);
        q2.setParameter("tipoRegistro", tipoRegistro);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public Paginacion getByFechasUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, Long idLibro, Long accion, Long tipoRegistro) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select lopd from Lopd as lopd where lopd.usuario.id = :idUsuarioEntidad and " +
                "lopd.fecha >= :fechaInicio and lopd.fecha <= :fechaFin and lopd.accion = :accion and " +
                "lopd.tipoRegistro = :tipoRegistro and lopd.libro.id = :idLibro order by lopd.fecha desc");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idLibro", idLibro);
        q.setParameter("accion", accion);
        q.setParameter("tipoRegistro", tipoRegistro);

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(lopd.id) from Lopd as lopd where lopd.usuario.id = :idUsuarioEntidad and " +
                "lopd.fecha >= :fechaInicio and lopd.fecha <= :fechaFin and lopd.accion = :accion and " +
                "lopd.tipoRegistro = :tipoRegistro and lopd.libro.id = :idLibro");

        q2.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("accion", accion);
        q2.setParameter("tipoRegistro", tipoRegistro);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Lopd> getByRegistro(String anyoRegistro, Integer numRegistro, Long idLibro, Long accion, Long tipoRegistro) throws Exception {

        Query q = em.createQuery("Select lopd from Lopd as lopd where lopd.anyoRegistro = :anyoRegistro and " +
                "lopd.accion = :accion and lopd.numeroRegistro = :numRegistro and " +
                "lopd.tipoRegistro = :tipoRegistro and lopd.libro.id = :idLibro order by lopd.fecha desc");

        q.setParameter("anyoRegistro", anyoRegistro);
        q.setParameter("numRegistro", numRegistro);
        q.setParameter("idLibro", idLibro);
        q.setParameter("accion", accion);
        q.setParameter("tipoRegistro", tipoRegistro);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Lopd> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select lopd from Lopd as lopd order by lopd.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void altaLopd(Integer numeroRegistro, Date fecha, Long idLibro, Long idUsuarioEntidad, Long tipoRegistro, Long accion) throws Exception {

        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        Lopd lopd = new Lopd();
        lopd.setNumeroRegistro(numeroRegistro);
        lopd.setTipoRegistro(tipoRegistro);
        lopd.setAnyoRegistro(formatYear.format(fecha));
        lopd.setLibro(new Libro(idLibro));
        lopd.setFecha(Calendar.getInstance().getTime());
        lopd.setUsuario(new UsuarioEntidad(idUsuarioEntidad));
        lopd.setAccion(accion);

        persist(lopd);
    }

    @Override
    public void insertarRegistros(Paginacion paginacion, UsuarioEntidad usuarioEntidad, Libro libro, Long tipoRegistro, Long accion) throws Exception{

        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

        for (int i = 0; i<paginacion.getListado().size(); i++){
            Lopd lopd = new Lopd();
            lopd.setTipoRegistro(tipoRegistro);

            if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)){
                RegistroEntrada registro = (RegistroEntrada) paginacion.getListado().get(i);
                lopd.setNumeroRegistro(registro.getNumeroRegistro());
                lopd.setAnyoRegistro(formatYear.format(registro.getFecha()));
                lopd.setLibro(libro);
            }else {
                RegistroSalida registro = (RegistroSalida) paginacion.getListado().get(i);
                lopd.setNumeroRegistro(registro.getNumeroRegistro());
                lopd.setAnyoRegistro(formatYear.format(registro.getFecha()));
                lopd.setLibro(libro);
            }

            lopd.setFecha(Calendar.getInstance().getTime());
            lopd.setUsuario(usuarioEntidad);
            lopd.setAccion(accion);

            persist(lopd);
        }
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(lopd.id) from Lopd as lopd where lopd.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> lopd =  em.createQuery("select distinct(l.id) from Lopd as l where l.usuario.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = lopd.size();

        if(lopd.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (lopd.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = lopd.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from Lopd where id in (:lopd)").setParameter("lopd", subList).executeUpdate();
                lopd.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from Lopd where id in (:lopd)").setParameter("lopd", lopd).executeUpdate();
        }
        return total;

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaEntradaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select registroEntrada.numeroRegistro, registroEntrada.fecha, registroEntrada.libro.nombre, " +
                "registroEntrada.oficina.denominacion, registroEntrada.libro.organismo.denominacion " +
                "from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.usuario.id = :idUsuario and " +
                "registroEntrada.libro.id = :idLibro and registroEntrada.estado != :reserva order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                " and registroEntrada.fecha <= :fechaFin and registroEntrada.usuario.id = :idUsuario and " +
                " registroEntrada.libro.id = :idLibro and registroEntrada.estado != :reserva");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosEntrada.add(registroEntrada);
        }

        paginacion.setListado(registrosEntrada);

        return paginacion;
    }


    @Override
    public Paginacion entradaModificadaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception{

        Query q;
        Query q2;

        String accio = I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" );

        q = em.createQuery("Select historicoRegistroEntrada.registroEntrada.numeroRegistro, historicoRegistroEntrada.registroEntrada.libro.nombre, " +
                "historicoRegistroEntrada.registroEntrada.oficina.denominacion, historicoRegistroEntrada.registroEntrada.libro.organismo.denominacion, " +
                "historicoRegistroEntrada.registroEntrada.fecha, historicoRegistroEntrada.fecha, historicoRegistroEntrada.modificacion " +
                "from HistoricoRegistroEntrada as historicoRegistroEntrada where historicoRegistroEntrada.fecha >= :fechaInicio " +
                "and historicoRegistroEntrada.fecha <= :fechaFin and historicoRegistroEntrada.usuario.id = :idUsuario and " +
                "historicoRegistroEntrada.registroEntrada.libro.id = :idLibro and historicoRegistroEntrada.modificacion != :accio " +
                "order by historicoRegistroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("accio", accio);

        List<HistoricoRegistroEntrada> historicosRegistroEntrada = new ArrayList<HistoricoRegistroEntrada>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(historicoRegistroEntrada.id) from HistoricoRegistroEntrada as historicoRegistroEntrada " +
                "where historicoRegistroEntrada.fecha >= :fechaInicio and historicoRegistroEntrada.fecha <= :fechaFin and " +
                "historicoRegistroEntrada.usuario.id = :idUsuario and historicoRegistroEntrada.registroEntrada.libro.id = :idLibro and " +
                "historicoRegistroEntrada.modificacion != :accio");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("accio", accio);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroEntrada historicoRegistroEntrada = new HistoricoRegistroEntrada(null, (Integer) object[0], (String) object[1], (String) object[2], (String) object[3], (Date) object[4], (Date) object[5], (String) object[6]);

            historicosRegistroEntrada.add(historicoRegistroEntrada);
        }

        paginacion.setListado(historicosRegistroEntrada);

        return paginacion;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaSalidaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select registroSalida.numeroRegistro, registroSalida.fecha, registroSalida.libro.nombre, " +
                "registroSalida.oficina.denominacion, registroSalida.libro.organismo.denominacion " +
                "from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.usuario.id = :idUsuario and " +
                "registroSalida.libro.id = :idLibro and registroSalida.estado != :reserva order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        List<RegistroSalida> registrosSalida = new ArrayList<RegistroSalida>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where " +
                "registroSalida.fecha >= :fechaInicio and registroSalida.fecha <= :fechaFin and " +
                "registroSalida.usuario.id = :idUsuario and registroSalida.libro.id = :idLibro and " +
                "registroSalida.estado != :reserva");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroSalida registroSalida = new RegistroSalida(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosSalida.add(registroSalida);
        }

        paginacion.setListado(registrosSalida);

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion salidaModificadaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception{

        Query q;
        Query q2;

        String accio = I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" );

        q = em.createQuery("Select historicoRegistroSalida.registroSalida.numeroRegistro, historicoRegistroSalida.registroSalida.libro.nombre, " +
                "historicoRegistroSalida.registroSalida.oficina.denominacion, historicoRegistroSalida.registroSalida.libro.organismo.denominacion, " +
                "historicoRegistroSalida.registroSalida.fecha, historicoRegistroSalida.fecha, historicoRegistroSalida.modificacion " +
                "from HistoricoRegistroSalida as historicoRegistroSalida where historicoRegistroSalida.fecha >= :fechaInicio " +
                "and historicoRegistroSalida.fecha <= :fechaFin and historicoRegistroSalida.usuario.id = :idUsuario " +
                "and historicoRegistroSalida.registroSalida.libro.id = :idLibro " +
                "and historicoRegistroSalida.modificacion != :accio order by historicoRegistroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);
        q.setParameter("accio", accio);

        List<HistoricoRegistroSalida> historicosRegistroSalida = new ArrayList<HistoricoRegistroSalida>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(historicoRegistroSalida.id) from HistoricoRegistroSalida as historicoRegistroSalida " +
                "where historicoRegistroSalida.fecha >= :fechaInicio and historicoRegistroSalida.fecha <= :fechaFin and " +
                "historicoRegistroSalida.usuario.id = :idUsuario and historicoRegistroSalida.registroSalida.libro.id = :idLibro " +
                "and historicoRegistroSalida.modificacion != :accio");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("idLibro", idLibro);
        q2.setParameter("accio", accio);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroSalida historicoRegistroSalida = new HistoricoRegistroSalida(null, (Integer) object[0], (String) object[1], (String) object[2], (String) object[3], (Date) object[4], (Date) object[5], (String) object[6]);

            historicosRegistroSalida.add(historicoRegistroSalida);
        }

        paginacion.setListado(historicosRegistroSalida);

        return paginacion;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaEntradaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select registroEntrada.numeroRegistro, registroEntrada.fecha, registroEntrada.libro.nombre, " +
                "registroEntrada.oficina.denominacion, registroEntrada.libro.organismo.denominacion " +
                "from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.usuario.id = :idUsuario and registroEntrada.libro in (:libros) order by registroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);

        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where " +
                "registroEntrada.fecha >= :fechaInicio and registroEntrada.fecha <= :fechaFin and " +
                "registroEntrada.usuario.id = :idUsuario and registroEntrada.libro in (:libros)");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("libros", libros);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosEntrada.add(registroEntrada);
        }

        paginacion.setListado(registrosEntrada);

        return paginacion;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion entradaModificadaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception{

        Query q;
        Query q2;

        String accio = I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" );

        q = em.createQuery("Select historicoRegistroEntrada.registroEntrada.numeroRegistro, historicoRegistroEntrada.registroEntrada.libro.nombre, " +
                "historicoRegistroEntrada.registroEntrada.oficina.denominacion, historicoRegistroEntrada.registroEntrada.libro.organismo.denominacion, " +
                "historicoRegistroEntrada.registroEntrada.fecha, historicoRegistroEntrada.fecha, historicoRegistroEntrada.modificacion " +
                "from HistoricoRegistroEntrada as historicoRegistroEntrada where historicoRegistroEntrada.fecha >= :fechaInicio " +
                "and historicoRegistroEntrada.fecha <= :fechaFin and historicoRegistroEntrada.usuario.id = :idUsuario " +
                "and historicoRegistroEntrada.modificacion != :accio " +
                "and historicoRegistroEntrada.registroEntrada.libro in (:libros) order by historicoRegistroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);
        q.setParameter("accio", accio);

        List<HistoricoRegistroEntrada> historicosRegistroEntrada = new ArrayList<HistoricoRegistroEntrada>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(historicoRegistroEntrada.id) from HistoricoRegistroEntrada as historicoRegistroEntrada " +
                "where historicoRegistroEntrada.fecha >= :fechaInicio and historicoRegistroEntrada.fecha <= :fechaFin and " +
                "historicoRegistroEntrada.usuario.id = :idUsuario and historicoRegistroEntrada.modificacion != :accio " +
                "and historicoRegistroEntrada.registroEntrada.libro in (:libros)");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("libros", libros);
        q2.setParameter("accio", accio);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroEntrada historicoRegistroEntrada = new HistoricoRegistroEntrada(null, (Integer) object[0], (String) object[1], (String) object[2], (String) object[3], (Date) object[4], (Date) object[5], (String) object[6]);

            historicosRegistroEntrada.add(historicoRegistroEntrada);
        }

        paginacion.setListado(historicosRegistroEntrada);

        return paginacion;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaSalidaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long usuario, List<Libro> libros) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select registroSalida.numeroRegistro, registroSalida.fecha, registroSalida.libro.nombre, " +
                "registroSalida.oficina.denominacion, registroSalida.libro.organismo.denominacion " +
                "from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.usuario.id = :usuario and registroSalida.libro in (:libros) order by registroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("usuario", usuario);
        q.setParameter("libros", libros);

        List<RegistroSalida> registrosSalida = new ArrayList<RegistroSalida>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(registroSalida.id) " +
                "from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.usuario.id = :usuario and " +
                "registroSalida.libro in (:libros)");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("usuario", usuario);
        q2.setParameter("libros", libros);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroSalida registroSalida = new RegistroSalida(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosSalida.add(registroSalida);
        }

        paginacion.setListado(registrosSalida);

        return paginacion;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion salidaModificadaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception{

        Query q;
        Query q2;

        String accio = I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" );

        q = em.createQuery("Select historicoRegistroSalida.registroSalida.numeroRegistro, historicoRegistroSalida.registroSalida.libro.nombre, " +
                "historicoRegistroSalida.registroSalida.oficina.denominacion, historicoRegistroSalida.registroSalida.libro.organismo.denominacion, " +
                "historicoRegistroSalida.registroSalida.fecha, historicoRegistroSalida.fecha, historicoRegistroSalida.modificacion " +
                "from HistoricoRegistroSalida as historicoRegistroSalida where historicoRegistroSalida.fecha >= :fechaInicio " +
                "and historicoRegistroSalida.fecha <= :fechaFin and historicoRegistroSalida.usuario.id = :idUsuario " +
                "and historicoRegistroSalida.modificacion != :accio " +
                "and historicoRegistroSalida.registroSalida.libro in (:libros) order by historicoRegistroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);
        q.setParameter("accio", accio);

        List<HistoricoRegistroSalida> historicosRegistroSalida = new ArrayList<HistoricoRegistroSalida>();

        // Duplicamos la query solo para obtener los resultados totales
        q2 = em.createQuery("Select count(historicoRegistroSalida.id) from HistoricoRegistroSalida as historicoRegistroSalida " +
                "where historicoRegistroSalida.fecha >= :fechaInicio and historicoRegistroSalida.fecha <= :fechaFin and " +
                "historicoRegistroSalida.usuario.id = :idUsuario and historicoRegistroSalida.modificacion != :accio and " +
                "historicoRegistroSalida.registroSalida.libro in (:libros)");

        q2.setParameter("fechaInicio", fechaInicio);
        q2.setParameter("fechaFin", fechaFin);
        q2.setParameter("idUsuario", idUsuario);
        q2.setParameter("libros", libros);
        q2.setParameter("accio", accio);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroSalida historicoRegistroSalida = new HistoricoRegistroSalida(null, (Integer) object[0], (String) object[1], (String) object[2], (String) object[3], (Date) object[4], (Date) object[5], (String) object[6]);

            historicosRegistroSalida.add(historicoRegistroSalida);
        }

        paginacion.setListado(historicosRegistroSalida);

        return paginacion;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getByUsuario(Integer pageNumber, final Integer resultsPerPage, Date dataInici, Date dataFi, String usuario, String accion) throws Exception {

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

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
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
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaEntradasPorLibroTipoNumero(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select registroEntrada.id, registroEntrada.numeroRegistro, registroEntrada.fecha, " +
                "registroEntrada.libro.id, registroEntrada.libro.nombre, registroEntrada.oficina.denominacion, registroEntrada.libro.organismo.denominacion " +
                "from RegistroEntrada as registroEntrada ");

        StringBuilder query2 = new StringBuilder("Select count(registroEntrada.id)from RegistroEntrada as registroEntrada ");

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
        query2.append("where ");

        int count = 0;
        for (String w : where) {
            if (count != 0) {
                query.append(" and ");
                query2.append(" and ");
            }
            query.append(w);
            query2.append(w);
            count++;
        }
        query.append(" order by registroEntrada.id desc");

        q = em.createQuery(query.toString());
        q2 = em.createQuery(query2.toString());

        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
            q2.setParameter(param.getKey(), param.getValue());
        }

        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada((Long) object[0], (Integer) object[1], (Date) object[2], (Long) object[3], (String) object[4], (String) object[5], (String) object[6]);

            registrosEntrada.add(registroEntrada);
        }

        paginacion.setListado(registrosEntrada);

        return paginacion;
    }


    @Override
    public Paginacion buscaSalidasPorLibroTipoNumero(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception {

        Query q;
        Query q2;

        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select registroSalida.id, registroSalida.numeroRegistro, registroSalida.fecha, " +
                "registroSalida.libro.id, registroSalida.libro.nombre, registroSalida.oficina.denominacion, registroSalida.libro.organismo.denominacion " +
                "from RegistroSalida as registroSalida ");

        StringBuilder query2 = new StringBuilder("Select count(registroSalida.id) from RegistroSalida as registroSalida ");

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
        query2.append("where ");

        int count = 0;
        for (String w : where) {
            if (count != 0) {
                query.append(" and ");
                query2.append(" and ");
            }
            query.append(w);
            query2.append(w);
            count++;
        }
        query.append(" order by registroSalida.id desc");
        q = em.createQuery(query.toString());
        q2 = em.createQuery(query2.toString());

        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
            q2.setParameter(param.getKey(), param.getValue());
        }

        List<RegistroSalida> registrosSalida = new ArrayList<RegistroSalida>();

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroSalida registroSalida = new RegistroSalida((Long) object[0], (Integer) object[1], (Date) object[2], (Long) object[3], (String) object[4], (String) object[5], (String) object[6]);

            registrosSalida.add(registroSalida);
        }

        paginacion.setListado(registrosSalida);

        return paginacion;
    }

}
