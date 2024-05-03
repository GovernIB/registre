package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "EntidadEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class EntidadBean extends BaseEjbJPA<Entidad, Long> implements EntidadLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroDetalleLocal registroDetalleEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private TipoAsuntoLocal tipoAsuntoEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private PersonaLocal personaEjb;
    @EJB private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    @EJB private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    @EJB private PlantillaLocal plantillaEjb;
    @EJB private LopdLocal lopdEjb;
    @EJB private PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;
    @EJB private PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;
    @EJB private RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;
    @EJB private RelacionSirOfiLocal relacionSirOfiEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private UsuarioEntidadLocal usuarioEntidadEjb;
    @EJB private DescargaLocal descargaEjb;
    @EJB private ModeloOficioRemisionLocal modeloOficioRemisionEjb;
    @EJB private ModeloReciboLocal modeloReciboEjb;
    @EJB private RegistroLopdMigradoLocal registroMigradoLopdEjb;
    @EJB private RegistroMigradoLocal registroMigradoEjb;
    @EJB private PropiedadGlobalLocal propiedadGlobalEjb;
    @EJB private RegistroSirLocal registroSirEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private ColaLocal colaEjb;
    @EJB private NotificacionLocal notificacionEjb;


    @Override
    public Entidad getReference(Long id) throws I18NException {

        return em.getReference(Entidad.class, id);
    }

    @Override
    public Entidad findById(Long id) throws I18NException {

        return em.find(Entidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Entidad findByIdLigero(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select entidad.id, entidad.codigoDir3, entidad.nombre, entidad.logoMenu, entidad.logoPie, entidad.configuracionPersona, " +
                "entidad.sir, entidad.mantenimiento, entidad.textoPie, entidad.colorMenu, entidad.numRegistro, entidad.libro, entidad.diasVisado, entidad.perfilCustodia, entidad.sello, entidad.logoSello, entidad.regSalidasPersonas from Entidad as entidad LEFT JOIN entidad.logoMenu logoMenu LEFT JOIN entidad.logoPie logoPie LEFT JOIN entidad.logoSello where " +
                "entidad.id = :idEntidad");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();

        if (result.size() == 1) {
            Entidad entidad = new Entidad();
            entidad.setId((Long) result.get(0)[0]);
            entidad.setCodigoDir3((String) result.get(0)[1]);
            entidad.setNombre((String) result.get(0)[2]);
            entidad.setLogoMenu((Archivo) result.get(0)[3]);
            entidad.setLogoPie((Archivo) result.get(0)[4]);
            entidad.setConfiguracionPersona((Long) result.get(0)[5]);
            entidad.setSir((Boolean) result.get(0)[6]);
            entidad.setMantenimiento((Boolean) result.get(0)[7]);
            entidad.setTextoPie((String) result.get(0)[8]);
            entidad.setColorMenu((String) result.get(0)[9]);
            entidad.setNumRegistro((String) result.get(0)[10]);
            entidad.setLibro((Libro) result.get(0)[11]);
            entidad.setDiasVisado((Integer) result.get(0)[12]);
            entidad.setPerfilCustodia((Long) result.get(0)[13]);
            entidad.setSello((String) result.get(0)[14]);
            entidad.setLogoSello((Archivo) result.get(0)[15]);
            entidad.setRegSalidasPersonas((Boolean) result.get(0)[16]);
            return entidad;
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getAll() throws I18NException {

        return em.createQuery("Select entidad from Entidad as entidad order by entidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(entidad.id) from Entidad as entidad");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select entidad from Entidad as entidad order by entidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Entidad nuevaEntidad(Entidad entidad) throws I18NException {

        // Libro único
        Libro libro = new Libro();
        libro.setCodigo(entidad.getLibro().getCodigo());
        libro.setNombre(entidad.getLibro().getNombre());
        entidad.setLibro(libroEjb.crearLibro(libro));

        // Perfil custodia por defecto
        entidad.setPerfilCustodia(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);

        entidad = persist(entidad);

        // Creamos el UsuarioEntidad del propietario
        usuarioEntidadEjb.persist(new UsuarioEntidad(null, entidad.getPropietario(), entidad.getId()));

        // Creamos los TipoDocumental definidos por la Norma Técnica de Interoperabilidad de Documento Electrónico
        tipoDocumentalEjb.nuevoTraduccion("TD01", entidad.getId(), "Resolució", "Resolución");
        tipoDocumentalEjb.nuevoTraduccion("TD02", entidad.getId(), "Acord", "Acuerdo");
        tipoDocumentalEjb.nuevoTraduccion("TD03", entidad.getId(), "Contracte", "Contrato");
        tipoDocumentalEjb.nuevoTraduccion("TD04", entidad.getId(), "Conveni", "Convenio");
        tipoDocumentalEjb.nuevoTraduccion("TD05", entidad.getId(), "Declaració", "Declaración");
        tipoDocumentalEjb.nuevoTraduccion("TD06", entidad.getId(), "Comunicació", "Comunicación");
        tipoDocumentalEjb.nuevoTraduccion("TD07", entidad.getId(), "Notificació", "Notificación");
        tipoDocumentalEjb.nuevoTraduccion("TD08", entidad.getId(), "Publicació", "Publicación");
        tipoDocumentalEjb.nuevoTraduccion("TD09", entidad.getId(), "Justificant de recepció", "Acuse de recibo");
        tipoDocumentalEjb.nuevoTraduccion("TD10", entidad.getId(), "Acta", "Acta");
        tipoDocumentalEjb.nuevoTraduccion("TD11", entidad.getId(), "Certificat", "Certificado");
        tipoDocumentalEjb.nuevoTraduccion("TD12", entidad.getId(), "Diligència", "Diligencia");
        tipoDocumentalEjb.nuevoTraduccion("TD13", entidad.getId(), "Informe", "Informe");
        tipoDocumentalEjb.nuevoTraduccion("TD14", entidad.getId(), "Sol·licitud", "Solicitud");
        tipoDocumentalEjb.nuevoTraduccion("TD15", entidad.getId(), "Denúncia", "Denúncia");
        tipoDocumentalEjb.nuevoTraduccion("TD16", entidad.getId(), "Alegació", "Alegación");
        tipoDocumentalEjb.nuevoTraduccion("TD17", entidad.getId(), "Recursos", "Recursos");
        tipoDocumentalEjb.nuevoTraduccion("TD18", entidad.getId(), "Comunicació ciutadà", "Comunicación ciudadano");
        tipoDocumentalEjb.nuevoTraduccion("TD19", entidad.getId(), "Factura", "Factura");
        tipoDocumentalEjb.nuevoTraduccion("TD20", entidad.getId(), "Altres confiscats", "Otros incautados");
        tipoDocumentalEjb.nuevoTraduccion("TD51", entidad.getId(), "Llei", "Ley");
        tipoDocumentalEjb.nuevoTraduccion("TD52", entidad.getId(), "Moció", "Moción");
        tipoDocumentalEjb.nuevoTraduccion("TD53", entidad.getId(), "Instrucció", "Instrucción");
        tipoDocumentalEjb.nuevoTraduccion("TD54", entidad.getId(), "Convocatòria", "Convocatoria");
        tipoDocumentalEjb.nuevoTraduccion("TD55", entidad.getId(), "Ordre del dia", "Orden del día");
        tipoDocumentalEjb.nuevoTraduccion("TD56", entidad.getId(), "Informe de Ponència", "Informe de Ponencia");
        tipoDocumentalEjb.nuevoTraduccion("TD57", entidad.getId(), "Dictamen de Comissió", "Dictamen de Comisión");
        tipoDocumentalEjb.nuevoTraduccion("TD58", entidad.getId(), "Iniciativa legislativa", "Iniciativa legislativa");
        tipoDocumentalEjb.nuevoTraduccion("TD59", entidad.getId(), "Pregunta", "Pregunta");
        tipoDocumentalEjb.nuevoTraduccion("TD60", entidad.getId(), "Interpel·lació", "Interpelación");
        tipoDocumentalEjb.nuevoTraduccion("TD61", entidad.getId(), "Resposta", "Respuesta");
        tipoDocumentalEjb.nuevoTraduccion("TD62", entidad.getId(), "Proposició no de llei", "Proposición no de ley");
        tipoDocumentalEjb.nuevoTraduccion("TD63", entidad.getId(), "Esmena", "Enmienda");
        tipoDocumentalEjb.nuevoTraduccion("TD64", entidad.getId(), "Proposta de resolució", "Propuesta de resolución");
        tipoDocumentalEjb.nuevoTraduccion("TD65", entidad.getId(), "Compareixença", "Comparecencia");
        tipoDocumentalEjb.nuevoTraduccion("TD66", entidad.getId(), "Sol·licitud d'informació", "Solicitud de información");
        tipoDocumentalEjb.nuevoTraduccion("TD67", entidad.getId(), "Escrit", "Escrito");
        tipoDocumentalEjb.nuevoTraduccion("TD68", entidad.getId(), "Iniciativa legislativa", "Iniciativa legislativa");
        tipoDocumentalEjb.nuevoTraduccion("TD69", entidad.getId(), "Petició", "Petición");
        tipoDocumentalEjb.nuevoTraduccion("TD99", entidad.getId(), "Altres", "Otros");

        // Creamos las propiedades globales por defecto
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "dir3caib.server", "...", "Servidor de Dir3Caib con códigos de Producción", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_DIR3CAIB));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "dir3caib.username", "...", "Usuario de conexión a Dir3Caib", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_DIR3CAIB));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "dir3caib.password", "...", "Password de conexión Dir3Caib", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_DIR3CAIB));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "resultsperpage.oficios", "20", "Resultados por página en los Oficios pendientes de remisión", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_GENERAL));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "resultsperpage.lopd", "20", "Resultados por página en los informes LOPD", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_GENERAL));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "maxuploadsizeinbytes", "10485760", "Tamaño máximo permitido por anexo en bytes", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_SIR));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "sir.reintentos", "10", "Número máximo de reintentos para los envios SIR", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_SIR));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "enlaceDir3", "true", "Permite mostrar un enlace en le menú a la instalación de DIR3CAIB", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_GENERAL));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "comunicaciones.generar", "true", "Permite generar comunicaciones automáticas a los usuarios de las oficinas con mayor carga de trabajo pendiente.", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_GENERAL));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "cola.elementos", "15", "Número de elementos de cada iteración en la Cola.", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_COLA));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "cola.maxReintentos", "4", "Número máximo de reintentos de los elementos de la Cola.", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_COLA));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE + "cola.parar.distribucion", "false", "Indica si queremos detener la cola de distribución", entidad.getId(), RegwebConstantes.TIPO_PROPIEDAD_COLA));

        // Creamos los Plugins
        pluginEjb.persist(new Plugin("Custodia", "Custodia de anexos", "org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin", true, entidad.getId(), RegwebConstantes.PLUGIN_CUSTODIA_ANEXOS, null, "es.caib.regweb3.plugins.documentcustody.filesystem.prefix=ANNEX_\n" +
                "es.caib.regweb3.plugins.documentcustody.filesystem.basedir=C:/xxxx/Anexos/"));

        pluginEjb.persist(new Plugin("Custodia-Justificante", "Custodia de justificantes", "org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin", true, entidad.getId(), RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE, null, "es.caib.regweb3.plugins.documentcustody.filesystem.prefix=JUST_\n" +
                "es.caib.regweb3.plugins.documentcustody.filesystem.basedir=C:/xxxx/Justificantes/"));

        pluginEjb.persist(new Plugin("Justificante", "Genera el justificante de los registros", "es.caib.regweb3.plugins.justificante.mock.JustificanteMockPlugin", true, entidad.getId(), RegwebConstantes.PLUGIN_JUSTIFICANTE, null, "" +
                "# Mensaje para la declaración en el justificante\n" +
                "es.caib.regweb3.plugins.justificante.mock.declaracion.es=declara que las imágenes electrónicas adjuntas son imagen fiel e íntegra de los documentos en soporte físico origen, en el marco de la normativa vigente.\n" +
                "es.caib.regweb3.plugins.justificante.mock.declaracion.ca=declara que les imatges electròniques adjuntes són imatge feel i íntegra dels documents en soport físic origen, en el marc de la normativa vigent.\n" +
                "# Mensaje para la ley en el justificante\n" +
                "es.caib.regweb3.plugins.justificante.mock.ley.es=El registro realizado está amparado en el Artículo 16 de la Ley 39/2015.\n" +
                "es.caib.regweb3.plugins.justificante.mock.ley.ca=El registre realitzat està amparat a l'Article 16 de la Llei 39/2015.\n" +
                "# Mensaje para la validez en el justificante\n" +
                "es.caib.regweb3.plugins.justificante.mock.validez.es=El presente justificante tiene validez a efectos de presentación de la documentación. El inicio del cómputo de plazos para la Administración, en su caso, vendrá determinado por la fecha de la entrada de su solicitud en el registro del Organismo competente.\n" +
                "es.caib.regweb3.plugins.justificante.mock.validez.ca=El present justificant té validesa a efectes de presentació de la documentació. L'inici del còmput de plaços per l'Administració, en el seu cas, vendrà determinat per la data de l'entrada de la seva sol·licitud en el registre de l'Organismo competent.\n" +
                "# Path para el logo de la Entidad\n" +
                "es.caib.regweb3.plugins.justificante.mock.logoPath=xxxx/logo_entitat.jpg"));

        pluginEjb.persist(new Plugin("Distribución", "Implementación base del plugin, cambia el estado de un Registro a Distribuido", "es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin", true, entidad.getId(), RegwebConstantes.PLUGIN_DISTRIBUCION, null, null));


        pluginEjb.persist(new Plugin("Firma en servidor", "Firma en servidor mediante el MiniApplet", "org.fundaciobit.plugins.signatureserver.miniappletinserver.MiniAppletInServerSignatureServerPlugin", true, entidad.getId(), RegwebConstantes.PLUGIN_FIRMA_SERVIDOR, null, "# Base del Plugin de signature server\n" +
                "es.caib.regweb3.plugins.signatureserver.miniappletinserver.base_dir=xxxxxxxx/\n" +
                "# Si signaturesSet.getCommonInfoSignature().getUsername() es null, llavors\n" +
                "# s´utilitza aquest valor com a sistema de selecció del certificat amb el que firmar\n" +
                "es.caib.regweb3.plugins.signatureserver.miniappletinserver.defaultAliasCertificate=regweb3"));


        return entidad;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Entidad findByCodigoDir3(String codigo) throws I18NException {

        Query q = em.createQuery("Select entidad from Entidad as entidad where entidad.codigoDir3 = :codigo ");

        q.setParameter("codigo", codigo);

        List<Entidad> entidad = q.getResultList();

        if (entidad.size() == 1) {
            return entidad.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean tieneOrganismos(Long idEntidad) throws I18NException {
        Query q = em.createQuery("Select organismo.id from Organismo as organismo where organismo.entidad.id = :idEntidad");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> organismos = q.getResultList();

        return organismos.size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getEntidadesAdministrador(Long idUsuario) throws I18NException {

        Query q = em.createQuery("Select entidad.id, entidad.nombre from Entidad as entidad, UsuarioEntidad as usuarioEntidad where usuarioEntidad in elements(entidad.administradores) " +
                "and usuarioEntidad.usuario.id = :idUsuario and entidad.activo = true order by entidad.id");

        q.setParameter("idUsuario", idUsuario);
        q.setHint("org.hibernate.readOnly", true);

        List<Entidad> entidades = new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Entidad entidad = new Entidad((Long) object[0], (String) object[1]);

            entidades.add(entidad);
        }

        return entidades;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getEntidadesPropietario(Long idUsuario) throws I18NException {

        Query q = em.createQuery("Select entidad.id, entidad.nombre from Entidad as entidad where entidad.propietario.id = :idUsuario " +
                "and entidad.activo = true order by entidad.id");

        q.setParameter("idUsuario", idUsuario);
        q.setHint("org.hibernate.readOnly", true);

        List<Entidad> entidades = new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Entidad entidad = new Entidad((Long) object[0], (String) object[1]);

            entidades.add(entidad);
        }

        return entidades;
    }

    @Override
    public List<Entidad> getEntidadesActivas() throws I18NException {

        return em.createQuery("Select entidad from Entidad as entidad where entidad.activo = true order by entidad.id").getResultList();
    }

    @Override
    public Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad where " +
                "entidad.id != :idEntidad and entidad.codigoDir3 = :codigo");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean esAdministrador(UsuarioEntidad usuarioEntidad) throws I18NException {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad where entidad.id=:idEntidad and entidad.activo = true and :usuarioEntidad in elements(entidad.administradores) ");

        q.setParameter("idEntidad", usuarioEntidad.getEntidad().getId());
        q.setParameter("usuarioEntidad", usuarioEntidad);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean esAutorizado(Long idEntidad, Long idUsuario) throws I18NException {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad, UsuarioEntidad as usuarioEntidad where (entidad.propietario.id = :idUsuario or usuarioEntidad in elements(entidad.administradores) ) and entidad.id=:idEntidad");

        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idEntidad", idEntidad);

        return q.getResultList().size() > 0;

    }

    @Override
    public Boolean isSir(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select entidad.sir from Entidad as entidad where entidad.id = :idEntidad");

        q.setParameter("idEntidad", idEntidad);

        return (Boolean) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getEntidadesSir() throws I18NException {

        return em.createQuery("Select entidad from Entidad as entidad where entidad.sir = true order by entidad.id").getResultList();
    }

    @Override
    public Boolean puedoEliminarlo(Long idUsuarioEntidad) throws I18NException {

        return !registroEntradaConsultaEjb.obtenerPorUsuario(idUsuarioEntidad) && !registroSalidaConsultaEjb.obtenerPorUsuario(idUsuarioEntidad)
                && !historicoRegistroEntradaEjb.obtenerPorUsuario(idUsuarioEntidad) && !historicoRegistroSalidaEjb.obtenerPorUsuario(idUsuarioEntidad)
                && !plantillaEjb.obtenerPorUsuario(idUsuarioEntidad) && !lopdEjb.obtenerPorUsuario(idUsuarioEntidad);
    }


    @Override
    public void eliminarRegistros(Long idEntidad) throws I18NException {

        log.info("Dentro eliminar Registros Entidad");

        // TRAZABILIDAD 
        log.info("Trazabilidades eliminadas: " + trazabilidadEjb.eliminarByEntidad(idEntidad));

        // TRAZABILIDAD SIR 
        log.info("TrazabilidadSir eliminados: " + trazabilidadSirEjb.eliminarByEntidad(idEntidad));

        // OFICIOS REMISIÓN 
        log.info("OficiosRemision eliminados: " + oficioRemisionEjb.eliminarByEntidad(idEntidad));

        // PERSONAS 
        log.info("Personas eliminadas: " + personaEjb.eliminarByEntidad(idEntidad));


        // REGISTROS ENTRADA 

        // HistoricoRegistroEntrada
        log.info("HistoricosEntrada eliminados: " + historicoRegistroEntradaEjb.eliminarByEntidad(idEntidad));

        // RegistroEntrada
        Set<Long> registrosDetalle = registroDetalleEjb.getRegistrosDetalle(idEntidad);

        log.info("RegistrosEntrada eliminados: " + registroEntradaEjb.eliminarByEntidad(idEntidad));

        // REGISTROS SALIDA 

        // HistoricoRegistroSalida
        log.info("HistoricosSalida eliminados: " + historicoRegistroSalidaEjb.eliminarByEntidad(idEntidad));

        // RegistroSalida
        log.info("RegistrosSalida eliminados: " + registroSalidaEjb.eliminarByEntidad(idEntidad));


        // REGISTROS DETALLE
        log.info("RegistrosDetalle eliminados: " + registroDetalleEjb.eliminar(registrosDetalle, idEntidad));

        // PLANTILLAS
        log.info("Plantillas eliminadas: " + plantillaEjb.eliminarByEntidad(idEntidad));

        // LOPD 
        log.info("Lopds eliminados: " + lopdEjb.eliminarByEntidad(idEntidad));

        // REGISTRO SIR 
        log.info("RegistroSir eliminados: " + registroSirEjb.eliminarByEntidad(idEntidad));

        // REINCIAR CONTADORES LIBRO ENTIDAD
        Entidad entidad = findById(idEntidad);
        contadorEjb.reiniciarContadoresLibro(entidad.getLibro());

        // Integraciones
        integracionEjb.eliminarByEntidad(idEntidad);

        em.flush();
    }

    @Override
    public void eliminarEntidad(Long idEntidad) throws I18NException {

        Entidad entidad = findById(idEntidad);

        log.info("Dentro eliminar Entidad");

        //Eliminamos todos los datos relacionados con los RegistrosEntradad y RegistrosSalida
        eliminarRegistros(idEntidad);

        // PERMISO LIBRO USUARIO
        log.info("PermisoLibroUsuarios eliminados: " + permisoLibroUsuarioEjb.eliminarByEntidad(idEntidad));

        // PERMISO ORGANISMO USUARIO
        log.info("PermisoOrganismoUsuarios eliminados: " + permisoOrganismoUsuarioEjb.eliminarByEntidad(idEntidad));

        // LIBROS
        log.info("Libros eliminados: " + libroEjb.eliminarByEntidad(entidad));

        // RelacionOrganizativaOfi 
        log.info("RelacionOrganizativaOfi eliminadas: " + relacionOrganizativaOfiEjb.eliminarByEntidad(idEntidad));

        // RelacionSirOfi 
        log.info("RelacionOrganizativaOfi eliminadas: " + relacionSirOfiEjb.eliminarByEntidad(idEntidad));

        //Cola Distribucion
        log.info("Cola Distribución: " + colaEjb.eliminarByEntidad(idEntidad));

        // Notificaciones
        log.info("Notificaciones eliminadas: " + notificacionEjb.eliminarByEntidad(idEntidad));

        // USUARIOS ENTIDAD 
        log.info("UsuariosEntidad eliminados: " + usuarioEntidadEjb.eliminarByEntidad(idEntidad));

        // OFICINAS 
        log.info("Oficinas eliminadas: " + oficinaEjb.eliminarByEntidad(idEntidad));

        // ORGANISMOS 
        log.info("Organismos eliminados: " + organismoEjb.eliminarByEntidad(idEntidad));

        // TIPOS ASUNTO Y CODIGOS ASUNTO 
        log.info("TipoAsuntos: " + tipoAsuntoEjb.eliminarByEntidad(idEntidad));

        // TIPOS DOCUMENTAL 
        log.info("TiposDocumental: " + tipoDocumentalEjb.eliminarByEntidad(idEntidad));

        // Modelo OficioRemision 
        log.info("Modelo OficioRemision: " + modeloOficioRemisionEjb.eliminarByEntidad(idEntidad));

        // Modelo Recibo 
        log.info("Modelo Recibo: " + modeloReciboEjb.eliminarByEntidad(idEntidad));

        // DESCARGAS 
        log.info("Descargas: " + descargaEjb.eliminarByEntidad(idEntidad));

        // PROPIEDADES GLOBALES 
        log.info("Propiedades globales: " + propiedadGlobalEjb.eliminarByEntidad(idEntidad));

        // REGISTROS MIGRADOS 
        log.info("RegistrosMigradosLopd: " + registroMigradoLopdEjb.eliminarByEntidad(idEntidad));
        log.info("RegistrosMigrados: " + registroMigradoEjb.eliminarByEntidad(idEntidad));

        // PLUGINS 
        log.info("Plugins: " + pluginEjb.eliminarByEntidad(idEntidad));

        // ENTIDAD 
        em.flush();

        //em.createQuery("delete from Entidad where id = :idEntidad ").setParameter("idEntidad", idEntidad).executeUpdate();

    }

    @Override
    public void marcarEntidadMantenimiento(Long idEntidad, Boolean mantenimiento) throws I18NException {

        em.createQuery("update from Entidad set mantenimiento =:mantenimiento where id =:idEntidad").setParameter("idEntidad", idEntidad).setParameter("mantenimiento", mantenimiento).executeUpdate();

    }

    @Override
    public Boolean isJustificanteCustodiadoLocal(Long idEntidad) throws I18NException {

        IDocumentCustodyPlugin documentCustodyPlugin = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);

        if(documentCustodyPlugin != null){

            Properties properties = pluginEjb.getPropertiesPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            String custodiadoLocal = properties.getProperty(RegwebConstantes.REGWEB3_PROPERTY_BASE+"plugins.documentcustody.filesystem.custodiadoLocal");

            return "true".equals(custodiadoLocal);
        }

        return false;
    }

}
