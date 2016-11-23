package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
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
 * @author jpernia
 *         Date: 05/07/16
 */

@Stateless(name = "InformeEJB")
@SecurityDomain("seycon")
public class InformeBean implements InformeLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB
    public RegistroEntradaLocal registroEntradaEjb;
    @EJB
    public RegistroSalidaLocal registroSalidaEjb;
    @EJB
    public OrganismoLocal organismoEjb;


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> buscaLibroRegistroEntradas(Date fechaInicio, Date fechaFin, String numRegistro, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String extracto, String usuario, List<Libro> libros, Long estado, Long idOficina, Long idTipoAsunto, String organoDest, Long idEntidad) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroEntrada.id, " +
                "registroEntrada.libro.id, " +
                "registroEntrada.libro.nombre, " +
                "registroEntrada.oficina.id, " +
                "registroEntrada.oficina.denominacion, " +
                "registroEntrada.fecha, " +
                "registroEntrada.numeroRegistro, " +
                "registroDetalle.extracto, " +
                "registroDetalle.tipoAsunto.id, " +
                "registroDetalle.oficinaOrigen.id, " +
                "registroDetalle.oficinaOrigen.denominacion, " +
                "registroDetalle.numeroRegistroOrigen, " +
                "registroDetalle.fechaOrigen, " +
                "registroEntrada.destinoExternoDenominacion, " +
                "registroEntrada.destino.id, " +
                "registroEntrada.destino.denominacion, " +
                "registroDetalle.tipoDocumentacionFisica, " +
                "registroDetalle.idioma, " +
                "registroDetalle.observaciones, " +
                "registroEntrada.estado, " +
                "registroDetalle.expediente, " +
                "registroDetalle.codigoAsunto.id, " +
                "registroDetalle.referenciaExterna, " +
                "registroDetalle.transporte, " +
                "registroDetalle.numeroTransporte," +
                "registroDetalle.id," +
                "registroEntrada.destinoExternoCodigo " +
                "from RegistroEntrada as registroEntrada, RegistroDetalle as registroDetalle " +
                "left outer join registroDetalle.oficinaOrigen as oficinaOrigen " +
                "left outer join registroEntrada.destino as destino " +
                "left outer join registroDetalle.codigoAsunto as codigoAsunto ");


        // Afegim condició necessària pels joins
        where.add(" registroDetalle.id=registroEntrada.registroDetalle.id ");

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
            if (/*organismoEjb.findByCodigoLigero(organoDest) == null*/organismoEjb.findByCodigoEntidad(organoDest, idEntidad) == null) {
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
            query.append(" order by registroEntrada.fecha desc");
            q = em.createQuery(query.toString());
        }


        // Executa la query
        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();
//        log.info("Resultats: " + q.getResultList().size());
        List<Object[]> result = q.getResultList();

        // Recorr tots els resultats trobats
        for (Object[] object : result) {

            // Obté identificadors del resultat de la query
            long idRE = (Long) object[0];
            long idDetalle = (Long) object[25];

            // Crea query per trobar els Interessats del registre actual
            Query q2;
            Map<String, Object> parametros2 = new HashMap<String, Object>();
            List<String> where2 = new ArrayList<String>();

            StringBuffer query2 = new StringBuffer("Select interessat.id, " +
                    "interessat.nombre, " +
                    "interessat.apellido1, " +
                    "interessat.apellido2, " +
                    "interessat.isRepresentante, " +
                    "interessat.razonSocial, " +
                    "interessat.documento," +
                    "interessat.tipo " +
                    "from Interesado as interessat ");

            // Afegim condició necessària per la query
            where2.add(" interessat.registroDetalle.id = :idDetalle ");
            parametros2.put("idDetalle", idDetalle);

            // Nombre interesado
            if (!StringUtils.isEmpty(interesadoNom)) {
                where2.add("((" + DataBaseUtils.like("interessat.nombre", "interesadoNom", parametros2, interesadoNom) +
                        ") or (" + DataBaseUtils.like("interessat.razonSocial", "interesadoNom", parametros2, interesadoNom) +
                        "))");
            }

            // Primer apellido interesado
            if (!StringUtils.isEmpty(interesadoLli1)) {
                where2.add(DataBaseUtils.like("interessat.apellido1", "interesadoLli1", parametros2, interesadoLli1));
            }

            // Segundo apellido interesado
            if (!StringUtils.isEmpty(interesadoLli2)) {
                where2.add(DataBaseUtils.like("interessat.apellido2", "interesadoLli2", parametros2, interesadoLli2));
            }

            // Documento interesado
            if (!StringUtils.isEmpty(interesadoDoc)) {
                where2.add(" (UPPER(interessat.documento) LIKE UPPER(:interesadoDoc)) ");
                parametros2.put("interesadoDoc", "%" + interesadoDoc.trim() + "%");
            }


            query2.append("where ");
            int count = 0;
            for (String w : where2) {
                if (count != 0) {
                    query2.append(" and ");
                }
                query2.append(w);
                count++;
            }


            // Executa la query
            q2 = em.createQuery(query2.toString());

            for (Map.Entry<String, Object> param : parametros2.entrySet()) {
                q2.setParameter(param.getKey(), param.getValue());
            }

            List<Interesado> interesados = new ArrayList<Interesado>();
            List<Object[]> result2 = q2.getResultList();

            // Composa els interessats del registre
            for (Object[] object2 : result2) {

                Interesado interesado = new Interesado((Long) object2[0], (String) object2[1], (String) object2[2],
                        (String) object2[3], (Boolean) object2[4], (String) object2[5], (String) object2[6], (Long) object2[7]);

                interesados.add(interesado);
            }

            // Si el registre d'entrada te qualque interessat, ho afegeix a la llista
            if (interesados.size() > 0) {

                RegistroEntrada registroEntrada = new RegistroEntrada(idRE, (Long) object[1], (String) object[2],
                        (Long) object[3], (String) object[4], (Date) object[5], (Integer) object[6], (String) object[7], (Long) object[8],
                        (Long) object[9], (String) object[10], (String) object[11], (Date) object[12], (String) object[13], (Long) object[14],
                        (String) object[15], (Long) object[16], (Long) object[17], (String) object[18], (Long) object[19], (String) object[20],
                        (Long) object[21], (String) object[22], (Long) object[23], (String) object[24], (Long) object[25], (String) object[26], interesados);


                registrosEntrada.add(registroEntrada);

            }
        }

        return registrosEntrada;
    }

    @Override
    public List<RegistroSalida> buscaLibroRegistroSalidas(Date fechaInicio, Date fechaFin, String numRegistro, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, Boolean anexos, String observaciones, String extracto, String usuario, List<Libro> libros, Long estado, Long idOficina, Long idTipoAsunto, String organoOrig, Long idEntidad) throws Exception {

        Query q;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroSalida.id, " +
                "registroSalida.libro.id, " +
                "registroSalida.libro.nombre, " +
                "registroSalida.oficina.id, " +
                "registroSalida.oficina.denominacion, " +
                "registroSalida.fecha, " +
                "registroSalida.numeroRegistro, " +
                "registroDetalle.extracto, " +
                "registroDetalle.tipoAsunto.id, " +
                "registroDetalle.oficinaOrigen.id, " +
                "registroDetalle.oficinaOrigen.denominacion, " +
                "registroDetalle.numeroRegistroOrigen, " +
                "registroDetalle.fechaOrigen, " +
                "registroSalida.origenExternoDenominacion, " +
                "registroSalida.origen.id, " +
                "registroSalida.origen.denominacion, " +
                "registroDetalle.tipoDocumentacionFisica, " +
                "registroDetalle.idioma, " +
                "registroDetalle.observaciones, " +
                "registroSalida.estado, " +
                "registroDetalle.expediente, " +
                "registroDetalle.codigoAsunto.id, " +
                "registroDetalle.referenciaExterna, " +
                "registroDetalle.transporte, " +
                "registroDetalle.numeroTransporte," +
                "registroDetalle.id," +
                "registroSalida.origenExternoCodigo " +
                "from RegistroSalida as registroSalida, RegistroDetalle as registroDetalle " +
                "left outer join registroDetalle.oficinaOrigen as oficinaOrigen " +
                "left outer join registroSalida.origen as origen " +
                "left outer join registroDetalle.codigoAsunto as codigoAsunto ");


        // Afegim condició necessària pels joins
        where.add(" registroDetalle.id=registroSalida.registroDetalle.id ");

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
            Organismo organismo = organismoEjb.findByCodigoEntidad(organoOrig, idEntidad);
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

        // Executa la query
        List<RegistroSalida> registrosSalida = new ArrayList<RegistroSalida>();
        log.info("Resultats: " + q.getResultList().size());
        List<Object[]> result = q.getResultList();

        // Recorr tots els resultats trobats
        for (Object[] object : result) {

            // Obté identificadors del resultat de la query
            long idRS = (Long) object[0];
            long idDetalle = (Long) object[25];

            // Crea query per trobar els Interessats del registre actual
            Query q2;
            Map<String, Object> parametros2 = new HashMap<String, Object>();
            List<String> where2 = new ArrayList<String>();

            StringBuffer query2 = new StringBuffer("Select interessat.id, " +
                    "interessat.nombre, " +
                    "interessat.apellido1, " +
                    "interessat.apellido2, " +
                    "interessat.isRepresentante, " +
                    "interessat.razonSocial, " +
                    "interessat.documento," +
                    "interessat.tipo " +
                    "from Interesado as interessat ");

            // Afegim condició necessària per la query
            where2.add(" interessat.registroDetalle.id = :idDetalle ");
            parametros2.put("idDetalle", idDetalle);

            // Nombre interesado
            if (!StringUtils.isEmpty(interesadoNom)) {
                where2.add("((" + DataBaseUtils.like("interessat.nombre", "interesadoNom", parametros2, interesadoNom) +
                        ") or (" + DataBaseUtils.like("interessat.razonSocial", "interesadoNom", parametros2, interesadoNom) +
                        "))");
            }

            // Primer apellido interesado
            if (!StringUtils.isEmpty(interesadoLli1)) {
                where2.add(DataBaseUtils.like("interessat.apellido1", "interesadoLli1", parametros2, interesadoLli1));
            }

            // Segundo apellido interesado
            if (!StringUtils.isEmpty(interesadoLli2)) {
                where2.add(DataBaseUtils.like("interessat.apellido2", "interesadoLli2", parametros2, interesadoLli2));
            }

            // Documento interesado
            if (!StringUtils.isEmpty(interesadoDoc)) {
                where2.add(" (UPPER(interessat.documento) LIKE UPPER(:interesadoDoc)) ");
                parametros2.put("interesadoDoc", "%" + interesadoDoc.trim() + "%");
            }

            query2.append("where ");
            int count = 0;
            for (String w : where2) {
                if (count != 0) {
                    query2.append(" and ");
                }
                query2.append(w);
                count++;
            }

            // Executa la query
            q2 = em.createQuery(query2.toString());

            for (Map.Entry<String, Object> param : parametros2.entrySet()) {
                q2.setParameter(param.getKey(), param.getValue());
            }

            List<Interesado> interesados = new ArrayList<Interesado>();
            List<Object[]> result2 = q2.getResultList();

            // Composa els interessats del registre
            for (Object[] object2 : result2) {

                Interesado interesado = new Interesado((Long) object2[0], (String) object2[1], (String) object2[2],
                        (String) object2[3], (Boolean) object2[4], (String) object2[5], (String) object2[6], (Long) object2[7]);

                interesados.add(interesado);
            }

            // Si el registre d'entrada te qualque interessat, ho afegeix a la llista
            if (interesados.size() > 0) {

                RegistroSalida registroSalida = new RegistroSalida(idRS, (Long) object[1], (String) object[2],
                        (Long) object[3], (String) object[4], (Date) object[5], (Integer) object[6], (String) object[7], (Long) object[8],
                        (Long) object[9], (String) object[10], (String) object[11], (Date) object[12], (String) object[13], (Long) object[14],
                        (String) object[15], (Long) object[16], (Long) object[17], (String) object[18], (Long) object[19], (String) object[20],
                        (Long) object[21], (String) object[22], (Long) object[23], (String) object[24], (Long) object[25], (String) object[26], interesados);

                registrosSalida.add(registroSalida);

            }

        }

        return registrosSalida;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long buscaIndicadoresEntradaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.estado != :anulado and registroEntrada.estado != :reserva and " +
                "registroEntrada.libro.organismo.entidad.id = :idEntidad ");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaIndicadoresSalidaTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count (registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.estado != :anulado and registroSalida.estado != :reserva and " +
                "registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaEntradaPorUsuarioLibro(Integer pageNumber, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception {

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
            RegistroEntrada registroEntrada = new RegistroEntrada(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosEntrada.add(registroEntrada);
        }

        paginacion.setListado(registrosEntrada);

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaSalidaPorUsuarioLibro(Integer pageNumber, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception {

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
            RegistroSalida registroSalida = new RegistroSalida(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosSalida.add(registroSalida);
        }

        paginacion.setListado(registrosSalida);

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaEntradaPorUsuario(Integer pageNumber, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception {

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
            RegistroEntrada registroEntrada = new RegistroEntrada(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosEntrada.add(registroEntrada);
        }

        paginacion.setListado(registrosEntrada);

        return paginacion;
    }

    @Override
    public Paginacion buscaSalidaPorUsuario(Integer pageNumber, Date fechaInicio, Date fechaFin, Long usuario, List<Libro> libros) throws Exception {

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
            RegistroSalida registroSalida = new RegistroSalida(null, (Integer) object[0], (Date) object[1], null, (String) object[2], (String) object[3], (String) object[4]);

            registrosSalida.add(registroSalida);
        }

        paginacion.setListado(registrosSalida);

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscaEntradasPorLibroTipoNumero(Integer pageNumber, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroEntrada.id, registroEntrada.numeroRegistro, registroEntrada.fecha, " +
                "registroEntrada.libro.id, registroEntrada.libro.nombre, registroEntrada.oficina.denominacion, registroEntrada.libro.organismo.denominacion " +
                "from RegistroEntrada as registroEntrada ");

        StringBuffer query2 = new StringBuffer("Select count(registroEntrada.id)from RegistroEntrada as registroEntrada ");

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
            RegistroEntrada registroEntrada = new RegistroEntrada((Long) object[0], (Integer) object[1], (Date) object[2], (Long) object[3], (String) object[4], (String) object[5], (String) object[6]);

            registrosEntrada.add(registroEntrada);
        }

        paginacion.setListado(registrosEntrada);

        return paginacion;
    }

    @Override
    public Paginacion buscaSalidasPorLibroTipoNumero(Integer pageNumber, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception {

        Query q;
        Query q2;

        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select registroSalida.id, registroSalida.numeroRegistro, registroSalida.fecha, " +
                "registroSalida.libro.id, registroSalida.libro.nombre, registroSalida.oficina.denominacion, registroSalida.libro.organismo.denominacion " +
                "from RegistroSalida as registroSalida ");

        StringBuffer query2 = new StringBuffer("Select count(registroSalida.id) from RegistroSalida as registroSalida ");

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
            RegistroSalida registroSalida = new RegistroSalida((Long) object[0], (Integer) object[1], (Date) object[2], (Long) object[3], (String) object[4], (String) object[5], (String) object[6]);

            registrosSalida.add(registroSalida);
        }

        paginacion.setListado(registrosSalida);

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long buscaIndicadoresOficinaTotalEntrada(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.estado != :anulado and registroEntrada.estado != :reserva and " +
                "registroEntrada.oficina.id = :idOficina ");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaIndicadoresOficinaTotalSalida(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.estado != :anulado and registroSalida.estado != :reserva and " +
                "registroSalida.oficina.id = :idOficina ");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long buscaEntradaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.oficina.organismoResponsable.id = :conselleria and registroEntrada.estado != :anulado and registroEntrada.estado != :reserva");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("conselleria", conselleria);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

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
    @SuppressWarnings(value = "unchecked")
    public Long buscaEntradaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.tipoAsunto.id = :tipoAsunto and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :reserva and registroEntrada.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("tipoAsunto", tipoAsunto);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.idioma = :idioma and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :reserva and registroEntrada.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.registroDetalle.idioma = :idioma and " +
                "registroEntrada.estado != :anulado and registroEntrada.estado != :reserva and registroEntrada.oficina.id = :idOficina");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.libro.id = :libro and registroEntrada.estado != :anulado  and registroEntrada.estado != :reserva");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("libro", libro);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaEntradaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada where registroEntrada.fecha >= :fechaInicio " +
                "and registroEntrada.fecha <= :fechaFin and registroEntrada.oficina.id = :oficina and registroEntrada.estado != :anulado and registroEntrada.estado != :reserva");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("oficina", oficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.tipoAsunto.id = :tipoAsunto and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :reserva and registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("tipoAsunto", tipoAsunto);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.idioma = :idioma and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :reserva and registroSalida.libro.organismo.entidad.id = :idEntidad");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long buscaSalidaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida where registroSalida.fecha >= :fechaInicio " +
                "and registroSalida.fecha <= :fechaFin and registroSalida.registroDetalle.idioma = :idioma and " +
                "registroSalida.estado != :anulado and registroSalida.estado != :reserva and registroSalida.oficina.id = :idOficina");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idioma", idioma);
        q.setParameter("idOficina", idOficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

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
                "and registroSalida.fecha <= :fechaFin and registroSalida.oficina.id = :oficina and registroSalida.estado != :anulado and registroSalida.estado != :reserva");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("oficina", oficina);
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("reserva", RegwebConstantes.REGISTRO_RESERVA);

        return (Long) q.getSingleResult();
    }

}