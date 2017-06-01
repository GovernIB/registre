package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "EntidadEJB")
@SecurityDomain("seycon")
public class EntidadBean extends BaseEjbJPA<Entidad, Long> implements EntidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private RegistroDetalleLocal registroDetalleEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private TipoAsuntoLocal tipoAsuntoEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private PersonaLocal personaEjb;
    @EJB private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    @EJB private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    @EJB private ReproLocal reproEjb;
    @EJB private LopdLocal lopdEjb;
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


    @Override
    public Entidad getReference(Long id) throws Exception {

        return em.getReference(Entidad.class, id);
    }

    @Override
    public Entidad findById(Long id) throws Exception {

        return em.find(Entidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getAll() throws Exception {

        return  em.createQuery("Select entidad from Entidad as entidad order by entidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(entidad.id) from Entidad as entidad");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Entidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad order by entidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Entidad nuevaEntidad(Entidad entidad) throws Exception{

        //Usuario propietario = usuarioEjb.getReference(entidad.getPropietario().getId());
        //entidad.setPropietario(propietario);
        entidad = persist(entidad);

        // Creamos el UsuarioEntidad del propietario
        usuarioEntidadEjb.persist(new UsuarioEntidad(null,entidad.getPropietario(),entidad.getId()));

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
        tipoDocumentalEjb.nuevoTraduccion("TD99", entidad.getId(), "Altres", "Otros");

        // Creamos las propiedades globales por defecto
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE+"resultsperpage.oficios","20","Resultados por página en los Oficios pendientes de remisión", entidad.getId(), 1L));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE+"resultsperpage.lopd","20","Resultados por página en los informes LOPD", entidad.getId(), 1L));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE+"maxuploadsizeinbytes","10485760","Tamaño máximo permitido por anexo en bytes", entidad.getId(), 7L));
        propiedadGlobalEjb.persist(new PropiedadGlobal(RegwebConstantes.REGWEB3_PROPERTY_BASE+"cronExpression.inicializarContadores","0 0 0 1 1 ? *","Expresión del cron para la inicializacion de contadores", entidad.getId(), 1L));


        // Creamos los Plugins
        pluginEjb.persist(new Plugin("Justificante","Implementación base del plugin, genera el justificante SIR de los registros","es.caib.regweb3.plugins.justificante.mock.JustificanteMockPlugin",true,entidad.getId(),RegwebConstantes.PLUGIN_JUSTIFICANTE,null,"" +
                "# Mensaje para la declaración en el justificante\n" +
                "es.caib.regweb3.plugins.justificante.mock.declaracion.es=declara que las imágenes electrónicas adjuntas son imagen fiel e íntegra de los documentos en soporte físico origen, en el marco de la normativa vigente.\n" +
                "es.caib.regweb3.plugins.justificante.mock.declaracion.ca=declara que les imatges electròniques adjuntes són imatge feel i íntegra dels documents en soport físic origen, en el marc de la normativa vigent.\n" +
                "# Mensaje para la ley en el justificante\n" +
                "es.caib.regweb3.plugins.justificante.mock.ley.es=El registro realizado está amparado en el Artículo 16 de la Ley 39/2016.\n" +
                "es.caib.regweb3.plugins.justificante.mock.ley.ca=El registre realitzat està amparat a l'Article 16 de la Llei 39/2016."));
        pluginEjb.persist(new Plugin("Distribución","Implementación base del plugin, marca como distribuido un Registro","es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin",true,entidad.getId(),RegwebConstantes.PLUGIN_DISTRIBUCION,null,null));
        pluginEjb.persist(new Plugin("Firma en servidor","Firma en servidor mediante el MiniApplet","org.fundaciobit.plugins.signatureserver.miniappletinserver.MiniAppletInServerSignatureServerPlugin",true,entidad.getId(),RegwebConstantes.PLUGIN_FIRMA_SERVIDOR,null,"# Base del Plugin de signature server"+
                "es.caib.regweb3.plugins.signatureserver.miniappletinserver.base_dir=C:/Users/earrivi/Documents/Proyectos/OTAE/REGWEB3/"));


        return entidad;
    }

    @Override
    public Entidad findByCodigoDir3(String codigo) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where entidad.codigoDir3 = :codigo ");

        q.setParameter("codigo", codigo);

        List<Entidad> entidad =  q.getResultList();

        if(entidad.size() == 1){
            return entidad.get(0);
        }else{
            return  null;
        }

    }

    @Override
    public Boolean tieneOrganismos(Long idEntidad) throws Exception{
        Query q = em.createQuery("Select organismo.id from Organismo as organismo where organismo.entidad.id = :idEntidad");

        q.setParameter("idEntidad",idEntidad);

        List<Long> organismos = q.getResultList();

        return organismos.size() > 0;
    }

    @Override
    public List<Entidad> getEntidadesAdministrador(Long idUsuario) throws Exception{

        Query q = em.createQuery("Select entidad.id, entidad.nombre, entidad.oficioRemision from Entidad as entidad, UsuarioEntidad as usuarioEntidad where usuarioEntidad in elements(entidad.administradores) " +
                "and usuarioEntidad.usuario.id = :idUsuario and entidad.activo = true order by entidad.id");

        q.setParameter("idUsuario",idUsuario);

        List<Entidad> entidades =  new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Entidad entidad = new Entidad((Long)object[0],(String)object[1], (Boolean) object[2]);

            entidades.add(entidad);
        }

        return entidades;
    }

    @Override
    public List<Entidad> getEntidadesPropietario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad.id, entidad.nombre, entidad.oficioRemision from Entidad as entidad where entidad.propietario.id = :idUsuario " +
                "and entidad.activo = true order by entidad.id");

        q.setParameter("idUsuario",idUsuario);

        List<Entidad> entidades =  new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Entidad entidad = new Entidad((Long)object[0],(String)object[1], (Boolean) object[2]);

            entidades.add(entidad);
        }

        return entidades;
    }

    @Override
    public Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad where " +
                "entidad.id != :idEntidad and entidad.codigoDir3 = :codigo");

        q.setParameter("codigo",codigo);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean esAdministrador(Long idEntidad, UsuarioEntidad usuarioEntidad) throws Exception {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad where entidad.id=:idEntidad and entidad.activo = true and :usuarioEntidad in elements(entidad.administradores) ");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("usuarioEntidad", usuarioEntidad);

        return q.getResultList().size() > 0;

    }

    @Override
    public Boolean esAutorizado(Long idEntidad, Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad.id from Entidad as entidad, UsuarioEntidad as usuarioEntidad where (entidad.propietario.id = :idUsuario or usuarioEntidad in elements(entidad.administradores) ) and entidad.id=:idEntidad");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;

    }

    @Override
    public Boolean isSir(Long idEntidad) throws Exception{

        Query q = em.createQuery("Select entidad.sir from Entidad as entidad where entidad.id = :idEntidad");

        q.setParameter("idEntidad",idEntidad);

        return (Boolean) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getEntidadesSir() throws Exception {

        return  em.createQuery("Select entidad from Entidad as entidad where entidad.sir = true order by entidad.id").getResultList();
    }


    @Override
    public void eliminarRegistros(Long idEntidad) throws Exception{

        log.info("Dentro eliminar Registros Entidad");

        /********* TRAZABILIDAD *********/
        log.info("Trazabilidades eliminadas: " +trazabilidadEjb.eliminarByEntidad(idEntidad));

        /********* TRAZABILIDAD SIR *********/
        log.info("TrazabilidadSir eliminados: " + trazabilidadSirEjb.eliminarByEntidad(idEntidad));

        /********* OFICIOS REMISIÓN *********/
        log.info("OficiosRemision eliminados: " +oficioRemisionEjb.eliminarByEntidad(idEntidad));

        /********* PERSONAS *********/
        log.info("Personas eliminadas: " + personaEjb.eliminarByEntidad(idEntidad));


        /********* REGISTROS ENTRADA *********/

        // HistoricoRegistroEntrada
        log.info("HistoricosEntrada eliminados: " + historicoRegistroEntradaEjb.eliminarByEntidad(idEntidad));

        // RegistroEntrada
        Set<Long> registrosDetalle =  registroDetalleEjb.getRegistrosDetalle(idEntidad);

        log.info("RegistrosEntrada eliminados: " +registroEntradaEjb.eliminarByEntidad(idEntidad));

        /********* REGISTROS SALIDA *********/

        // HistoricoRegistroSalida
        log.info("HistoricosSalida eliminados: " + historicoRegistroSalidaEjb.eliminarByEntidad(idEntidad));

        // RegistroSalida
        log.info("RegistrosSalida eliminados: " +registroSalidaEjb.eliminarByEntidad(idEntidad));


        /********* REGISTROS DETALLE *********/

        log.info("RegistrosDetalle eliminados: " +registroDetalleEjb.eliminar(registrosDetalle));

        /********* REPRO *********/
        log.info("Repros eliminados: " +reproEjb.eliminarByEntidad(idEntidad));

        /********* LOPD *********/
        log.info("Lopds eliminados: " + lopdEjb.eliminarByEntidad(idEntidad));

        /********* LIBROS: PONER CONTADORES A 0  *********/
        List<Libro> libros = libroEjb.getLibrosEntidad(idEntidad);
        for (Libro libro : libros) {
           libroEjb.reiniciarContadores(libro.getId());
        }
        log.info("Libros reiniciados: " +libros.size());

        /********* REGISTRO SIR *********/
        log.info("RegistroSir eliminados: " + registroSirEjb.eliminarByEntidad(idEntidad));

        em.flush();

    }

    @Override
    public void eliminarEntidad(Long idEntidad)throws Exception{
        //todo: Añadir pre-registros cuando se active SIR

        log.info("Dentro eliminar Entidad");

        //Eliminamos todos los datos relacionados con los RegistrosEntradad y RegistrosSalida
        eliminarRegistros(idEntidad);

        /********* PERMISO LIBRO USUARIO *********/
        log.info("PermisoLibroUsuarios eliminados: " + permisoLibroUsuarioEjb.eliminarByEntidad(idEntidad));

        /********* LIBROS *********/
        log.info("Libros eliminados: " + libroEjb.eliminarByEntidad(idEntidad));

        /********* RelacionOrganizativaOfi *********/
        log.info("RelacionOrganizativaOfi eliminadas: " + relacionOrganizativaOfiEjb.eliminarByEntidad(idEntidad));

        /********* RelacionSirOfi *********/
        log.info("RelacionOrganizativaOfi eliminadas: " + relacionSirOfiEjb.eliminarByEntidad(idEntidad));

        /********* USUARIOS ENTIDAD *********/
        log.info("UsuariosEntidad eliminados: " + usuarioEntidadEjb.eliminarByEntidad(idEntidad));

        /********* OFICINAS *********/
        log.info("Oficinas eliminadas: " + oficinaEjb.eliminarByEntidad(idEntidad));

        /********* ORGANISMOS *********/
        log.info("Organismos eliminados: " + organismoEjb.eliminarByEntidad(idEntidad));

        /********* TIPOS ASUNTO Y CODIGOS ASUNTO *********/
        log.info("TipoAsuntos: " + tipoAsuntoEjb.eliminarByEntidad(idEntidad));

        /********* TIPOS DOCUMENTAL *********/
        log.info("TiposDocumental: " + tipoDocumentalEjb.eliminarByEntidad(idEntidad));

        /********* Modelo OficioRemision *********/
        log.info("Modelo OficioRemision: " + modeloOficioRemisionEjb.eliminarByEntidad(idEntidad));

        /********* Modelo Recibo *********/
        log.info("Modelo Recibo: " + modeloReciboEjb.eliminarByEntidad(idEntidad));

        /********* DESCARGAS *********/
        log.info("Descargas: " + descargaEjb.eliminarByEntidad(idEntidad));

        /********* PROPIEDADES GLOBALES *********/
        log.info("Propiedades globales: " + propiedadGlobalEjb.eliminarByEntidad(idEntidad));

        /********* REGISTROS MIGRADOS *********/
        log.info("RegistrosMigradosLopd: " + registroMigradoLopdEjb.eliminarByEntidad(idEntidad));
        log.info("RegistrosMigrados: " + registroMigradoEjb.eliminarByEntidad(idEntidad));

        /********* PLUGINS *********/
        log.info("Plugins: " + pluginEjb.eliminarByEntidad(idEntidad));

        /********* ENTIDAD *********/
        em.flush();

        //em.createQuery("delete from Entidad where id = :idEntidad ").setParameter("idEntidad", idEntidad).executeUpdate();

    }


}
