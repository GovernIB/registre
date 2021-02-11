package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

import static es.caib.regweb3.persistence.ejb.BaseEjbJPA.RESULTADOS_PAGINACION;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 14/05/19
 */

@Stateless(name = "RegistroEntradaConsultaEJB")
@SecurityDomain("seycon")
public class RegistroEntradaConsultaBean implements RegistroEntradaConsultaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private AnexoLocal anexoEjb;
    @EJB private OrganismoLocal organismoEjb;


    @Override
    @SuppressWarnings(value = "unchecked")
    public Long findByNumeroRegistroOrigen(String numeroRegistroFormateado, Long idRegistro) throws Exception {

        Query q = em.createQuery("Select re.id from RegistroEntrada as re where re.registroDetalle.numeroRegistroOrigen LIKE :numeroRegistroFormateado " +
           "and re.id != :idRegistro ");

        q.setParameter("numeroRegistroFormateado", "%" +numeroRegistroFormateado+ "%");
        q.setParameter("idRegistro", idRegistro);

        List<Long> registros = q.getResultList();
        log.info("findByNumeroRegistroOrigen ("+numeroRegistroFormateado+"): " + registros.size());
        if (registros.size() == 1) {
            return registros.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public RegistroBasico findByIdLigero(Long idRegistroEntrada) throws Exception{

        Query q;

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, re.estado " +
                "from RegistroEntrada as re where re.id = :idRegistroEntrada ");

        q.setHint("org.hibernate.readOnly", true);
        q.setParameter("idRegistroEntrada", idRegistroEntrada);

        List<Object[]> result = q.getResultList();

        if(result.size() == 1){
            Object[] object = result.get(0);

            RegistroBasico registroBasico = new RegistroBasico();
            registroBasico.setId((Long)  object[0]);
            registroBasico.setNumeroRegistroFormateado((String) object[1]);
            registroBasico.setFecha((Date) object[2]);
            registroBasico.setLibro((String) object[3]);
            registroBasico.setUsuario((String) object[4]);
            registroBasico.setEstado((Long) object[5]);

            return registroBasico;
        }

        return null;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, List<Long> organismos, Date fechaInicio, Date fechaFin, RegistroEntrada re, String interesadoNom, String interesadoLli1, String interesadoLli2, String interesadoDoc, String organoDest, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        String queryBase = "Select DISTINCT registroEntrada from RegistroEntrada as registroEntrada left outer join registroEntrada.registroDetalle.interesados interessat ";
        StringBuilder query = new StringBuilder(queryBase);

        // Entidad
        where.add(" registroEntrada.usuario.entidad.id =:idEntidad  ");
        parametros.put("idEntidad", idEntidad);

        // Organismo
        if(organismos.size() == 1){
            where.add(" registroEntrada.oficina.organismoResponsable.id = :idOrganismo ");
            parametros.put("idOrganismo", organismos.get(0));
        }else{
            where.add(" registroEntrada.oficina.organismoResponsable.id in (:organismos) ");
            parametros.put("organismos", organismos);
        }

        // Oficina Registro
        if (re.getOficina() != null && (re.getOficina().getId() != null && re.getOficina().getId() > 0)) {
            where.add(" registroEntrada.oficina.id = :idOficina ");
            parametros.put("idOficina", re.getOficina().getId());
        }

        // Estado registro
        if (re.getEstado() != null && re.getEstado() > 0) {
            where.add(" registroEntrada.estado = :idEstadoRegistro ");
            parametros.put("idEstadoRegistro", re.getEstado());
        }

        // Numero registro
        if (StringUtils.isNotEmpty(re.getNumeroRegistroFormateado())) {
            where.add(" registroEntrada.numeroRegistroFormateado LIKE :numeroRegistroFormateado");
            parametros.put("numeroRegistroFormateado", "%" + re.getNumeroRegistroFormateado() + "%");
        }

        // Extracto
        if (StringUtils.isNotEmpty(re.getRegistroDetalle().getExtracto())) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.extracto", "extracto", parametros, new String(re.getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8")));
        }

        // Observaciones
        if (StringUtils.isNotEmpty(observaciones)) {
            where.add(DataBaseUtils.like("registroEntrada.registroDetalle.observaciones", "observaciones", parametros, observaciones));
        }

        // Usuario
        if (StringUtils.isNotEmpty(usuario)) {
            where.add(DataBaseUtils.like("registroEntrada.usuario.usuario.identificador", "usuario", parametros, usuario));
        }

        // Nombre interesado
        if (StringUtils.isNotEmpty(interesadoNom)) {
            where.add("((" + DataBaseUtils.like("interessat.nombre", "interesadoNom", parametros, interesadoNom) +
                    ") or (" + DataBaseUtils.like("interessat.razonSocial", "interesadoNom", parametros, interesadoNom) +
                    "))");
        }

        // Primer apellido interesado
        if (StringUtils.isNotEmpty(interesadoLli1)) {
            where.add(DataBaseUtils.like("interessat.apellido1", "interesadoLli1", parametros, interesadoLli1));
        }

        // Segundo apellido interesado
        if (StringUtils.isNotEmpty(interesadoLli2)) {
            where.add(DataBaseUtils.like("interessat.apellido2", "interesadoLli2", parametros, interesadoLli2));
        }

        // Documento interesado
        if (StringUtils.isNotEmpty(interesadoDoc)) {
            where.add(" (UPPER(interessat.documento) LIKE UPPER(:interesadoDoc)) ");
            parametros.put("interesadoDoc", "%" + interesadoDoc.trim() + "%");
        }

        // Organismo destinatario
        if (StringUtils.isNotEmpty((organoDest))) {
            Organismo organismo = organismoEjb.findByCodigoEntidadSinEstadoLigero(organoDest, idEntidad);
            if (organismo == null) {
                where.add(" registroEntrada.destinoExternoCodigo = :organoDest ");
            } else {
                where.add(" registroEntrada.destino.codigo = :organoDest ");
            }

            parametros.put("organoDest", organoDest);
        }

        // Tipo documentación física
        if (re.getRegistroDetalle().getTipoDocumentacionFisica() != null && re.getRegistroDetalle().getTipoDocumentacionFisica() > 0) {
            where.add(" registroEntrada.registroDetalle.tipoDocumentacionFisica = :tipoDocumentacion ");
            parametros.put("tipoDocumentacion", re.getRegistroDetalle().getTipoDocumentacionFisica());
        }

        // Intervalo fechas
        where.add(" (registroEntrada.fecha >= :fechaInicio  ");
        parametros.put("fechaInicio", fechaInicio);
        where.add(" registroEntrada.fecha <= :fechaFin) ");
        parametros.put("fechaFin", fechaFin);

        // Buscamos registros de entrada con anexos
        if (anexos) {
            where.add(" registroEntrada.registroDetalle.id in (select distinct(a.registroDetalle.id) from Anexo as a) ");
        }

        //Presencial
        if(re.getRegistroDetalle().getPresencial() != null){
            where.add(" registroEntrada.registroDetalle.presencial = :presencial ");
            parametros.put("presencial", re.getRegistroDetalle().getPresencial());
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

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * RESULTADOS_PAGINACION;
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
    public List<RegistroBasico> getByOficinaEstado(Long idOficina, Long idEstado, Integer total) throws Exception {

        Query q;

        String s = "re.registroDetalle.extracto ";

        if (idEstado.equals(RegwebConstantes.REGISTRO_RESERVA)) {
            s = "re.registroDetalle.reserva ";
        }

        q = em.createQuery("Select re.id, re.numeroRegistroFormateado, re.fecha, re.libro.nombre, re.usuario.usuario.identificador, " + s +
                "from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setHint("org.hibernate.readOnly", true);
        q.setMaxResults(total);
        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("idEstado", idEstado);

        return getRegistroBasicoList(q.getResultList());

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Long idEstado) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

        q2 = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado");

        q2.setParameter("idOficinaActiva", idOficinaActiva);
        q2.setParameter("idEstado", idEstado);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * RESULTADOS_PAGINACION;
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
    public Paginacion pendientesDistribuir(Integer pageNumber, Long idOficinaActiva) throws Exception{

        Query q;
        Query q2;

        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado and re.evento = :distribuir order by re.fecha desc");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("distribuir", RegwebConstantes.EVENTO_DISTRIBUIR);

        q2 = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado and re.evento = :distribuir");

        q2.setParameter("idOficinaActiva", idOficinaActiva);
        q2.setParameter("idEstado", RegwebConstantes.REGISTRO_VALIDO);q2.setParameter("distribuir", RegwebConstantes.EVENTO_DISTRIBUIR);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * RESULTADOS_PAGINACION;
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
    public Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and re.estado = :idEstado");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getByLibrosEstado(int inicio, List<Organismo> organismos, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.organismoResponsable in (:organismos) " +
                "and re.estado = :idEstado order by re.fecha desc");

        q.setParameter("organismos", organismos);
        q.setParameter("idEstado", idEstado);

        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getByLibrosEstadoCount(List<Organismo> organismos, Long idEstado) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.organismoResponsable in (:organismos) " +
                "and re.estado = :idEstado");

        q.setParameter("organismos", organismos);
        q.setParameter("idEstado", idEstado);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroRegistroFormateado(String codigoEntidad, String numeroRegistroFormateado) throws Exception {

        Query q = em.createQuery("Select re from RegistroEntrada as re where re.numeroRegistroFormateado = :numeroRegistroFormateado " +
                "and re.usuario.entidad.codigoDir3 = :codigoEntidad ");

        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);
        q.setParameter("codigoEntidad", codigoEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada findByNumeroRegistroFormateadoConAnexos(String codigoEntidad, String numeroRegistroFormateado) throws Exception, I18NException {

       RegistroEntrada registroEntrada = findByNumeroRegistroFormateado(codigoEntidad,numeroRegistroFormateado);
       if(registroEntrada != null){
           return cargarAnexosFull(registroEntrada);
       }else{
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
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroEntrada> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }

    public String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception {

        Query q = em.createQuery("Select registroEntrada.numeroRegistroFormateado "
                + " from RegistroEntrada as registroEntrada"
                + " where registroEntrada.registroDetalle.id = :idRegistroDetalle "
              );

        q.setParameter("idRegistroDetalle", idRegistroDetalle);
        q.setHint("org.hibernate.readOnly", true);

        List<String> registro = q.getResultList();

        if (registro.size() == 1) {
            return registro.get(0);
        } else {
            return null;
        }
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getLibro(Long idRegistroEntrada) throws Exception {

        Query q;

        q = em.createQuery("Select registroEntrada.libro.id from RegistroEntrada as registroEntrada where registroEntrada.id = :idRegistroEntrada ");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> libros = q.getResultList();

        if (libros.size() > 0) {
            return libros.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Organismo getOrganismo(Long idRegistroEntrada) throws Exception {

        Query q;

        q = em.createQuery("Select re.oficina.organismoResponsable.id, re.oficina.organismoResponsable.codigo, re.oficina.organismoResponsable.denominacion from RegistroEntrada as re where re.id = :idRegistroEntrada ");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> organismos = q.getResultList();

        if (organismos.size() > 0) {

            return new Organismo((Long) organismos.get(0)[0], (String) organismos.get(0)[1], (String) organismos.get(0)[2]);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isDistribuir(Long idRegistro) throws Exception {

        Query q;
        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.estado = :valido and re.evento = :distribuir " );

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("distribuir", RegwebConstantes.EVENTO_DISTRIBUIR);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    public Long queryCount(String query) throws Exception {

        Query q;

        q = em.createQuery(query);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    /**
     * Convierte los resultados de una query en una lista de {@link RegistroBasico}
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


    @Override
    public Long getTotalByLibro(Long idLibro) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.libro.id = :idLibro ");

        q.setParameter("idLibro", idLibro);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws Exception {
        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.id = :idRegistroEntrada and re.estado = :idEstado ");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);
        q.setParameter("idEstado", idEstado);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public RegistroEntrada getConAnexosFullLigero(Long id) throws Exception, I18NException {

        RegistroEntrada re = em.find(RegistroEntrada.class, id);
        Long idEntidad = re.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFullLigero(anexo.getId(),idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        re.getRegistroDetalle().setAnexosFull(anexosFull);
        return re;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception {

        Query q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada where registroEntrada.registroDetalle.identificadorIntercambio = :identificadorIntercambio ");

        q.setParameter("identificadorIntercambio", identificadorIntercambio);
        q.setHint("org.hibernate.readOnly", true);

        return (RegistroEntrada) q.getResultList().get(0);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber,Long idOficina) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select re from RegistroEntrada as re where re.oficina.id = :idOficina " +
                "and re.estado = :rechazado or re.estado = :reenviado order by re.fecha desc");

        q.setParameter("idOficina", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);

        q2 = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficina " +
                "and re.estado = :rechazado or re.estado = :reenviado");

        q2.setParameter("idOficina", idOficina);
        q2.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q2.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * RESULTADOS_PAGINACION;
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
    public List<RegistroEntrada> getSirRechazadosReenviados(Long idOficina, Integer total) throws Exception {

        Query q;

        q = em.createQuery("Select re.id, re.fecha, re.registroDetalle.decodificacionEntidadRegistralDestino," +
                " re.estado, re.registroDetalle.decodificacionTipoAnotacion from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and (re.estado = :rechazado or re.estado = :reenviado) order by re.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada();
            registroEntrada.setId((Long) object[0]);
            registroEntrada.setFecha((Date) object[1]);
            registroEntrada.getRegistroDetalle().setDecodificacionEntidadRegistralDestino((String) object[2]);
            registroEntrada.setEstado((Long) object[3]);
            registroEntrada.getRegistroDetalle().setDecodificacionTipoAnotacion((String) object[4]);

            registros.add(registroEntrada);

        }

        return registros;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getSirRechazadosReenviadosCount(Long idOficina) throws Exception {

        Query q;

        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where re.oficina.id = :idOficinaActiva " +
                "and (re.estado = :rechazado or re.estado = :reenviado)");

        q.setParameter("idOficinaActiva", idOficina);
        q.setParameter("rechazado", RegwebConstantes.REGISTRO_RECHAZADO);
        q.setParameter("reenviado", RegwebConstantes.REGISTRO_REENVIADO);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    /**
     * Carga los Anexos Completos al RegistroEntrada pasado por parámetro
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroEntrada cargarAnexosFull(RegistroEntrada registroEntrada) throws Exception, I18NException {
        Long idEntidad = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        registroEntrada.getRegistroDetalle().setAnexosFull(anexosFull);
        return registroEntrada;
    }

    /**
     * Carga los Anexos Ligero al RegistroEntrada pasado por parámetro
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroEntrada cargarAnexosLigero(RegistroEntrada registroEntrada) throws Exception, I18NException {
        Long idEntidad = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFullLigero(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        registroEntrada.getRegistroDetalle().setAnexosFull(anexosFull);

        return registroEntrada;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getByDocumento(Long idEntidad, String documento) throws Exception {

        Query q;
        q = em.createQuery("Select DISTINCT re.id, re.numeroRegistroFormateado, re.fecha, re.registroDetalle.extracto, re.destino, re.destinoExternoCodigo, re.destinoExternoDenominacion " +
                "from RegistroEntrada as re left outer join re.registroDetalle.interesados interessat LEFT JOIN re.destino destino " +
                "where (UPPER(interessat.documento) LIKE UPPER(:documento)) and re.usuario.entidad.id = :idEntidad and re.estado != :anulado order by re.fecha desc");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("documento", documento.trim());
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();

        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

        for (Object[] object : result) {
            RegistroEntrada registro = new RegistroEntrada();
            registro.setId((Long) object[0]);
            registro.setNumeroRegistroFormateado((String) object[1]);
            registro.setFecha((Date) object[2]);
            registro.getRegistroDetalle().setExtracto((String) object[3]);
            registro.setDestino((Organismo) object[4]);
            registro.setDestinoExternoCodigo((String) object[5]);
            registro.setDestinoExternoDenominacion((String) object[6]);

            registros.add(registro);
        }

        return registros;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getByDocumento(Long idEntidad, String documento, Integer pageNumber) throws Exception {

        Query q1;
        Query q2;

        // Obtenemos el total de registros del ciudadano
        q1 = em.createQuery("Select DISTINCT count(re.id) " +
                "from RegistroEntrada as re left outer join re.registroDetalle.interesados interessat LEFT JOIN re.destino destino " +
                "where (UPPER(interessat.documento) LIKE UPPER(:documento)) and re.usuario.entidad.id = :idEntidad and re.estado != :anulado");
        q1.setParameter("idEntidad", idEntidad);
        q1.setParameter("documento", documento.trim());
        q1.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q1.setHint("org.hibernate.readOnly", true);
        Long total = (Long) q1.getSingleResult();

        // Obtenemos solo los paginados
        q2 = em.createQuery("Select DISTINCT re.id, re.numeroRegistroFormateado, re.fecha, re.registroDetalle.extracto, re.destino, re.destinoExternoCodigo, re.destinoExternoDenominacion " +
                "from RegistroEntrada as re left outer join re.registroDetalle.interesados interessat LEFT JOIN re.destino destino " +
                "where (UPPER(interessat.documento) LIKE UPPER(:documento)) and re.usuario.entidad.id = :idEntidad and re.estado != :anulado order by re.fecha desc");

        q2.setParameter("idEntidad", idEntidad);
        q2.setParameter("documento", documento.trim());
        q2.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q2.setHint("org.hibernate.readOnly", true);

        int inicio = pageNumber * RESULTADOS_PAGINACION;
        q2.setFirstResult(inicio);
        q2.setMaxResults(RESULTADOS_PAGINACION);

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        List<Object[]> result = q2.getResultList();
        List<RegistroEntrada> registros = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            Object[] object = result.get(0);

            RegistroEntrada registroEntrada = new RegistroEntrada();
            registroEntrada.setId((Long)  object[0]);
            registroEntrada.setNumeroRegistroFormateado((String) object[1]);
            registroEntrada.setFecha((Date) object[2]);
            registroEntrada.getRegistroDetalle().setExtracto((String) object[3]);
            registroEntrada.setDestino((Organismo) object[4]);
            registroEntrada.setDestinoExternoCodigo((String) object[5]);
            registroEntrada.setDestinoExternoDenominacion((String) object[6]);

            registros.add(registroEntrada);
        }

        paginacion.setListado(registros);

        return paginacion;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada getByDocumentoNumeroRegistro(Long idEntidad, String documento, String numeroRegistroFormateado) throws Exception, I18NException {

        Query q = em.createQuery("Select re from RegistroEntrada as re left outer join re.registroDetalle.interesados interessat " +
                "where UPPER(interessat.documento)=UPPER(:documento) and re.numeroRegistroFormateado = :numeroRegistroFormateado " +
                "and re.usuario.entidad.id = :idEntidad");

        q.setParameter("documento", documento);
        q.setParameter("numeroRegistroFormateado", numeroRegistroFormateado);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<RegistroEntrada> registros = q.getResultList();

        if (registros.size() > 0) {
            return cargarAnexosLigero(registros.get(0));
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> ultimosOrganismosRegistro(UsuarioEntidad usuarioEntidad) throws Exception {

        Query q;
        q = em.createQuery("Select distinct(re.registroDetalle.id) from RegistroEntrada as re left outer join re.registroDetalle.interesados interesado where " +
                " re.usuario.id = :idUsuarioEntidad and re.estado != :anulado and interesado.tipo = :administracion order by re.registroDetalle.id desc");

        q.setParameter("idUsuarioEntidad", usuarioEntidad.getId());
        q.setParameter("anulado", RegwebConstantes.REGISTRO_ANULADO);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setHint("org.hibernate.readOnly", true);
        q.setMaxResults(PropiedadGlobalUtil.getTotalOrganismosSelect(usuarioEntidad.getEntidad().getId()));

        List<Long> registros = q.getResultList();

        Query q1 = em.createQuery("Select distinct(i.codigoDir3), i.razonSocial from Interesado as i where i.registroDetalle.id in (:registros)");

        // Parámetros
        q1.setParameter("registros", registros);
        q1.setHint("org.hibernate.readOnly", true);

        List<Object[]> destinos = q1.getResultList();
        List<Organismo> organismos = new ArrayList<>();

        for (Object[] object : destinos) {
            organismos.add(new Organismo(null, (String) object[0], (String) object[1]));
        }

        return organismos;
    }

}
