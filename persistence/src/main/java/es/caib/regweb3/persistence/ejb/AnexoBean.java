package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.persistence.validator.AnexoBeanValidator;
import es.caib.regweb3.persistence.validator.AnexoValidator;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.utils.Metadata;
import org.fundaciobit.plugins.utils.PluginsManager;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * @author anadal
 * Date: 6/03/13
 */
@Stateless(name = "AnexoEJB")
@SecurityDomain("seycon")
public class AnexoBean extends BaseEjbJPA<Anexo, Long> implements AnexoLocal {

    @Resource
    public javax.ejb.SessionContext ejbContext;

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;
    
    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;
    

    @EJB(mappedName = "regweb3/RegistroEntradaCambiarEstadoEJB/local")
    public RegistroEntradaCambiarEstadoLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaCambiarEstadoEJB/local")
    public RegistroSalidaCambiarEstadoLocal registroSalidaEjb;

    
    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    
    
    
    @Override
    public AnexoFull getAnexoFull(Long anexoID) throws I18NException {
      
      try {
        Anexo anexo = em.find(Anexo.class, anexoID);
        
        String custodyID = anexo.getCustodiaID();
        
        AnexoFull anexoFull = new AnexoFull(anexo);

          //IDocumentCustodyPlugin custody = AnnexDocumentCustodyManager.getInstance();
          IDocumentCustodyPlugin custody = getInstance();

        anexoFull.setDocumentoCustody(custody.getDocumentInfoOnly(custodyID));

        anexoFull.setDocumentoFileDelete(false);
        
        anexoFull.setSignatureCustody(custody.getSignatureInfoOnly(custodyID));
        
        anexoFull.setSignatureFileDelete(false);
        
        return anexoFull;
        
      } catch(Exception e) {
        log.error(e.getMessage(), e);
        throw new I18NException(e, "anexo.error.obteniendo",
            new I18NArgumentString(String.valueOf(anexoID)),
            new I18NArgumentString(e.getMessage()));
        
      }
    }


    @Override
    public AnexoFull getAnexoFullCompleto(Long anexoID) throws I18NException {

        try {
            Anexo anexo = em.find(Anexo.class, anexoID);

            String custodyID = anexo.getCustodiaID();

            AnexoFull anexoFull = new AnexoFull(anexo);

            //IDocumentCustodyPlugin custody = AnnexDocumentCustodyManager.getInstance();
            IDocumentCustodyPlugin custody = getInstance();

            anexoFull.setDocumentoCustody(custody.getDocumentInfo(custodyID));

            anexoFull.setDocumentoFileDelete(false);

            anexoFull.setSignatureCustody(custody.getSignatureInfo(custodyID));

            anexoFull.setSignatureFileDelete(false);

            return anexoFull;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new I18NException(e, "anexo.error.obteniendo",
                    new I18NArgumentString(String.valueOf(anexoID)),
                    new I18NArgumentString(e.getMessage()));

        }
    }
    
    
    

    @Override
    public AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
        Long registroID, String tipoRegistro) throws I18NException, I18NValidationException {
      
      IDocumentCustodyPlugin custody = null;
      boolean error = false;
      String custodyID = null;
      final boolean isNew = true;
      try {
        
        Anexo anexo = anexoFull.getAnexo();
        
        // Validador
        validateAnexo(anexo, isNew);
        
        anexo.setFechaCaptura(new Date());
        
        // Revisar si tipusdocumental està carregat
        Long id = anexo.getTipoDocumental().getId();
        TipoDocumental td = tipoDocumentalEjb.findById(id);
        if (td == null) {
          I18NException i18n = new I18NException("anexo.tipoDocumental.obligatorio");
          log.error("No trob tipoDocumental amb ID = ]" + id + "[");
          throw i18n;
        } else {
          anexo.setTipoDocumental(td);
        }

          custody = getInstance();

        IRegistro registro = getIRegistro(registroID, tipoRegistro, anexo, isNew);

        final String custodyParameters = getCustodyParameters(registro, anexo);
        
        custodyID = custody.reserveCustodyID(custodyParameters);
        anexo.setCustodiaID(custodyID);
  
        
        updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
             registro, isNew);

        anexo = this.persist(anexo);
        
        
        crearHistorico(anexoFull, usuarioEntidad, registroID, tipoRegistro, isNew);
        
        anexoFull.setAnexo(anexo);
        
        return anexoFull;
      
      } catch(I18NException i18n) {
        error = true;
        throw i18n;
      } catch(Exception e) {
        error = true;
        log.error("Error creant un anexe: " + e.getMessage(), e);
        throw new I18NException(e, "anexo.error.guardando", new I18NArgumentString(e.getMessage()));
      } finally {
        if (error) {
          ejbContext.setRollbackOnly();
          
          if (custody != null && custodyID != null) {
        
            try {
              custody.deleteCustody(custodyID);
            } catch (Throwable th) {
              log.warn("Error borrant custodia: " + th.getMessage(), th );
            }
          }
        }
      }
      
    }




    protected void validateAnexo(Anexo anexo, final boolean isNou)
        throws I18NValidationException, I18NException {
      AnexoValidator<Anexo> anexoValidator = new AnexoValidator<Anexo>();
      AnexoBeanValidator pfbv = new AnexoBeanValidator(anexoValidator);        
      pfbv.throwValidationExceptionIfErrors(anexo, isNou);
    }

    
    @Override
    public AnexoFull actualizarAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
        Long registroID, String tipoRegistro) throws I18NException, I18NValidationException {
      
      try {
        
        Anexo anexo = anexoFull.getAnexo();
        
        // Validador        
        final boolean isNew = false;
        validateAnexo(anexo, isNew);
        
        anexo.setFechaCaptura(new Date());


          IDocumentCustodyPlugin custody = getInstance();

        
        IRegistro registro = getIRegistro(registroID, tipoRegistro, anexo, isNew);
        
        
        final String custodyParameters = getCustodyParameters(registro, anexo);
        
        
        final String custodyID = anexo.getCustodiaID();
    
        
        anexo = this.merge(anexo);
        anexoFull.setAnexo(anexo);

        // Crea historico y lo enlaza con el RegistroDetalle
        crearHistorico(anexoFull, usuarioEntidad, registroID, tipoRegistro, isNew);
  
        updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
            registro, isNew);

        return anexoFull;
      
      } catch(I18NException i18n) {
        ejbContext.setRollbackOnly();
        throw i18n;
      } catch(Exception e) {
        ejbContext.setRollbackOnly();
        log.error("Error actualitzant un anexe: " + e.getMessage(), e);
        throw new I18NException(e, "anexo.error.guardando", new I18NArgumentString(e.getMessage()));
      }
      
    }


    protected IRegistro getIRegistro(Long registroID, String tipoRegistro, Anexo anexo, boolean isNou) throws Exception {
      IRegistro registro;
      IRegistro cloneRegistro;
      if ("entrada".equals(tipoRegistro)) {
        registro = registroEntradaEjb.findById(registroID);
        
      } else {
        registro = registroSalidaEjb.findById(registroID);
        
      }
      
      Hibernate.initialize(registro.getRegistroDetalle().getTipoAsunto());
      Hibernate.initialize(registro.getRegistroDetalle().getInteresados());
      
      if ("entrada".equals(tipoRegistro)) {
        cloneRegistro = new RegistroEntrada((RegistroEntrada)registro);
      } else {
        cloneRegistro = new RegistroSalida((RegistroSalida)registro);
      }
      

      List<Anexo> anexos = Anexo.clone(registro.getRegistroDetalle().getAnexos());

      cloneRegistro.getRegistroDetalle().setAnexos(anexos);


      return cloneRegistro;
    }
    
    
    
    
    
    protected void crearHistorico(AnexoFull anexoFull, UsuarioEntidad  usuarioEntidad,
        Long registroID, String tipoRegistro, boolean isNew) throws Exception, I18NException {
      Entidad entidadActiva = usuarioEntidad.getEntidad();
      if("entrada".equals(tipoRegistro)) {
        RegistroEntrada registroEntrada = registroEntradaEjb.findById(registroID);
        // Dias que han pasado desde que se creó el registroEntrada
        Long dias = RegistroUtils.obtenerDiasRegistro(registroEntrada.getFecha());

        if(isNew){//NUEVO ANEXO
            // Si han pasado más de los dias de visado de la entidad se crearan historicos de todos los
            // cambios y se cambia el estado del registroEntrada a pendiente visar
            if(dias >= entidadActiva.getDiasVisado()){
               registroEntradaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

               // Creamos el historico de registro de entrada
               historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad,  I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.anexos" ),true);

            }

        } else {// MODIFICACION DE ANEXO

            if(dias >= entidadActiva.getDiasVisado()){ // Si han pasado más de los dias de visado cambiamos estado registro
                registroEntradaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            }

            // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.anexos" ),true);
        }
        
        anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
        
    } else {
        RegistroSalida registroSalida = registroSalidaEjb.findById(registroID);
        // Dias que han pasado desde que se creó el registroEntrada
        Long dias = RegistroUtils.obtenerDiasRegistro(registroSalida.getFecha());

        if(isNew){//NUEVO ANEXO
            // Si han pasado más de los dias de visado de la entidad se crearan historicos de todos los
            // cambios y se cambia el estado del registroEntrada a pendiente visar
            if(dias >= entidadActiva.getDiasVisado()){
               registroSalidaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

               // Creamos el historico de registro de entrada
               historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.anexos" ),true);
            }

        } else {// MODIFICACION DE ANEXO

            if(dias >= entidadActiva.getDiasVisado()){ // Si han pasado más de los dias de visado cambiamos estado registro
                registroSalidaEjb.cambiarEstado(registroID, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            }
            // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
            historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.anexos" ),true);
        }
        anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
      }
    }
    



    protected void updateCustodyInfoOfAnexo(AnexoFull anexoFull, IDocumentCustodyPlugin custody,
        final String custodyParameters, final String custodyID, 
        IRegistro registro, boolean isNou) throws Exception, I18NException {
      
      // Validador: Sempre amb algun arxiu
      if (isNou) {
        if (anexoFull.getDocumentoCustody() == null && anexoFull.getSignatureCustody() == null) {
          //"No ha definit cap fitxer en aquest annex"
          throw new I18NException("anexo.error.sinfichero");
        }
      } else {
        
        int total = 0;
        
        if (custody.getDocumentInfoOnly(custodyID) == null) {
          // Afegim un
          if (anexoFull.getDocumentoCustody() != null) {
            total += 1;
          }
        } else {
          total += 1;
          if (anexoFull.isDocumentoFileDelete()) {
            total -=1;
          }
        }
        
        
        if (custody.getSignatureInfoOnly(custodyID) == null) {
          // Afegim un
          if (anexoFull.getSignatureCustody() != null) {
            total += 1;
          }
        } else {
          total += 1;
          if (anexoFull.isSignatureFileDelete()) {
            total -=1;
          }
        }
        
        if (total <= 0) {
          //La combinació elegida deixa aquest annex sense cap fitxer
          throw new I18NException("anexo.error.quedarsesinfichero");
        }
        
        
        
      }
      

      // TODO Falta Check DOC
      Anexo anexo = anexoFull.getAnexo();
      
      boolean updateDate = false;
      
      String mimeFinal = null;

      DocumentCustody doc = null;
      if (anexoFull.isDocumentoFileDelete()) {
        custody.deleteDocument(custodyID);
        updateDate = true;
      } else {

        doc = anexoFull.getDocumentoCustody();

        if (doc != null && doc.getData() != null) {


          if(doc.getMime() == null) {
            doc.setMime("application/octet-stream");
          }
          mimeFinal = doc.getMime(); 

          doc.setName(checkFileName(doc.getName() , "file.bin"));

          anexo.setFechaCaptura(new Date());

          custody.saveDocument(custodyID, custodyParameters, doc);
          
          updateDate = true;
        }
      }

      if (anexoFull.isSignatureFileDelete()) {
        custody.deleteSignature(custodyID);
        updateDate = true;
      } else {
        SignatureCustody signature = anexoFull.getSignatureCustody();
        if (signature != null && signature.getData() != null) {
          
          String signType = (doc == null) ? SignatureCustody.OTHER_SIGNATURE_WITH_ATTACHED_DOCUMENT : SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT;

          signature.setName(checkFileName(signature.getName(), "signature.bin"));

          final String mime = signature.getMime();
          if (mime == null) {
            signature.setMime("application/octet-stream");
          } else {
            
            if ("application/pdf".equals(mime)) {
              signType = SignatureCustody.PADES_SIGNATURE;              
            } else if ("application/xml".equals(mime) || 
                "text/xml".equals(mime)) {
              signType = SignatureCustody.XADES_SIGNATURE;              
            }
          }
          
          mimeFinal = signature.getMime(); // Sobreescriu Mime de doc 
          
          signature.setSignatureType(signType);
          // TODO Fallarà en update
          signature.setAttachedDocument(doc == null? true : false);
          
          custody.saveSignature(custodyID, custodyParameters, signature);
          
          updateDate = true;
        }
      }

      // Actualitzar Metadades
      final String lang = Configuracio.getDefaultLanguage(); 
      final Locale loc = new Locale(lang);
      List<Metadata> metadades = new ArrayList<Metadata>();
      
      // fechaDeEntradaEnElSistema 
      if (updateDate) {
        metadades.add(new Metadata("anexo.fechaCaptura", anexo.getFechaCaptura()));
      }
      
      // String tipoDeDocumento; //  varchar(100)
      if (anexo.getTitulo() != null) {
        metadades.add(new Metadata("anexo.titulo", anexo.getTitulo()));
      }

      //  String tipoDeDocumento; //  varchar(100)
      if (anexo.getTipoDocumento() != null) {
        metadades.add(new Metadata("anexo.tipoDocumento",
            I18NLogicUtils.tradueix(loc,"tipoDocumento." + anexo.getTipoDocumento())));
      }

      if (registro.getOficina() != null && registro.getOficina().getNombreCompleto() != null) {
        metadades.add(new Metadata("oficina",
            registro.getOficina().getNombreCompleto()));
      }


      if ( anexo.getOrigenCiudadanoAdmin() != null) {
        metadades.add(new Metadata("anexo.origen", 
              I18NLogicUtils.tradueix(loc, "anexo.origen." + anexo.getOrigenCiudadanoAdmin())));
      }

      /**
       * tipoValidezDocumento.1=Còpia
       * tipoValidezDocumento.2=Còpia Compulsada
       * tipoValidezDocumento.3=Còpia Original
       * tipoValidezDocumento.4=Original
       */
      if (anexo.getValidezDocumento() != null && anexo.getValidezDocumento() != -1) {
        metadades.add(new Metadata("anexo.validezDocumento", 
            I18NLogicUtils.tradueix(loc, "tipoValidezDocumento." + anexo.getValidezDocumento())));
      }

      if (mimeFinal != null) {
        metadades.add(new Metadata("anexo.formato", mimeFinal));
      }

      if (anexo.getTipoDocumental() != null && 
          anexo.getTipoDocumental().getCodigoNTI() != null) {
        metadades.add(new Metadata("anexo.tipoDocumental.codigo", anexo.getTipoDocumental().getCodigoNTI()));
        try {
          metadades.add(new Metadata("anexo.tipoDocumental.descripcion", 
              ((TraduccionTipoDocumental) anexo.getTipoDocumental().getTraduccion(loc.getLanguage())).getNombre()));
        } catch(Throwable th) {
          log.error("Error en la traduccion de tipo documental: " + th.getMessage(), th);
        }
      }
      if (anexo.getObservaciones() != null) {
        metadades.add(new Metadata("anexo.observaciones", anexo.getObservaciones()));
      }

      custody.updateMetadata(custodyID, metadades.toArray(new Metadata[metadades.size()]));

    }

    
    
    protected static String checkFileName(String name, String defaultName) throws Exception  {
      if (name == null || name.trim().length() == 0) {
        return defaultName;
      } else {
        return StringUtils.recortarNombre(name, RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH); 
      }
    }
    
   
    
    public static class java_util_Date_PersistenceDelegate extends PersistenceDelegate {
      protected Expression instantiate(Object oldInstance, Encoder out) {
          Date date = (Date) oldInstance;
          return new Expression(date, date.getClass(), "new", new Object[] {date.getTime()});
      }
  }


    protected String getCustodyParameters(IRegistro registro, Anexo anexo) throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      XMLEncoder encoder = new XMLEncoder(baos);

      encoder.setPersistenceDelegate(java.sql.Date.class, new java_util_Date_PersistenceDelegate());
      encoder.setPersistenceDelegate(java.sql.Time.class, new java_util_Date_PersistenceDelegate());
      encoder.setPersistenceDelegate(java.sql.Timestamp.class, new java_util_Date_PersistenceDelegate());
      encoder.setPersistenceDelegate(java.util.Date.class, new java_util_Date_PersistenceDelegate()); 
      
      encoder.writeObject(registro);
      encoder.writeObject(new Anexo(anexo));
      encoder.flush();
      encoder.close();
      
/*

      try {
        java.beans.XMLDecoder xmlDec = new java.beans.XMLDecoder(
            new java.io.ByteArrayInputStream(baos.toByteArray()));
        IRegistro registroDec = (IRegistro) xmlDec.readObject();
  
        System.out.println(" DDDDDDDDDDDDDDD = ]" + registroDec.getNumeroRegistroFormateado()
            + "[");
        System.out.println(" zzzzzzzzzzzzzzz = ]"
            + registroDec.getRegistroDetalle().getFechaOrigen() + "[");
  
        Anexo anex = (Anexo) xmlDec.readObject();
  
        System.out.println(" KKKKKKKK = ]"
            + ((TraduccionTipoDocumental) anex.getTipoDocumental().getTraduccion("ca"))
                .getNombre() + "[");
  

  
      } catch (Throwable e) {
        log.error(" EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", e);
      }
  
      try {
  
        java.io.FileOutputStream fos = new java.io.FileOutputStream("c:\\tmp\\out.xml");
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
  
      } catch (Throwable e) {
        log.error(" FFFFFFFFFFFFFFFFF", e);
      }
*/


      

      return new String(baos.toByteArray());
    }
    
    
    

    @Override
    public Anexo findById(Long id) throws Exception {

        return em.find(Anexo.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Anexo> getAll() throws Exception {

        return  em.createQuery("Select anexo from Anexo as anexo order by anexo.id").getResultList();
    }
    
    
        
    

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(anexo.id) from Anexo as anexo");

        return (Long) q.getSingleResult();
    }

    @Override
    public List<Anexo> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select anexo from Anexo as anexo order by anexo.id");

        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public List<Anexo> getByRegistroEntrada(Long idRegistro) throws Exception{
        RegistroEntrada re = registroEntradaEjb.findById(idRegistro);
        Hibernate.initialize(re.getRegistroDetalle().getAnexos());
        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        return anexos;

    }

    @Override
    public List<Anexo> getByRegistroSalida(Long idRegistro) throws Exception{
        RegistroSalida re = registroSalidaEjb.findById(idRegistro);
        Hibernate.initialize(re.getRegistroDetalle().getAnexos());
        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        return anexos;

    }

    @Override
    public List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception{
        Query query = em.createQuery("Select anexo from Anexo as anexo where anexo.registroDetalle.id=:idRegistroDetalle order by anexo.id");
        query.setParameter("idRegistroDetalle", idRegistroDetalle);
        return query.getResultList();

    }


    /* METODOS DEL AnnexDocumentCustodyManager.java hecho por marilen del TODO DE TONI*/

 
    private static  IDocumentCustodyPlugin cacheDocumentCustodyPlugin = null;
    
    
    /**
     * Obtiene una instancia del plugin de custodia
     *
     * @return
     * @throws Exception
     */
    public IDocumentCustodyPlugin getInstance() throws Exception {

        if (cacheDocumentCustodyPlugin == null) {
          // Valor de la Clau
          final String propertyName = RegwebConstantes.REGWEB3_PROPERTY_BASE + "annex.documentcustodyplugin";
          String className = System.getProperty(propertyName);
          if (className == null || className.trim().length() <= 0) {
              throw new Exception("No hi ha cap propietat " + propertyName
                      + " definint la classe que gestiona el plugin de login");
          }
          // Carregant la classe
          Object obj;
          obj = PluginsManager.instancePluginByClassName(className, RegwebConstantes.REGWEB3_PROPERTY_BASE + "annex.");
          // TODO Falta mirar si retorna un null !!!!!
          
          cacheDocumentCustodyPlugin = (IDocumentCustodyPlugin) obj;
        }

        return cacheDocumentCustodyPlugin;
    }


    /**
     * Obtiene la info del fichero existente en el sistema de archivos
     * (No obtiene el array de bytes)
     * @param custodiaID
     * @return
     */
    public  DocumentCustody getArchivo(String custodiaID) throws Exception {

        if (custodiaID == null) {
            log.warn("getArchivo :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }

        return getInstance().getDocumentInfo(custodiaID);

    }
    
    
    
    /**
     * Obtiene la info del fichero existente en el sistema de archivos
     * (No obtiene el array de bytes)
     * @param custodiaID
     * @return
     */
    @Override
    public byte[] getArchivoContent(String custodiaID) throws Exception {

        if (custodiaID == null) {
            log.warn("getArchivo :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }

        return getInstance().getDocument(custodiaID);

    }
    

    /**
     * Obtiene la info de la firma existente en el sistema de archivos
     * (No obtiene el array de bytes)
     * @param custodiaID
     * @return
     */
    public  SignatureCustody getFirma(String custodiaID) throws Exception {

        if (custodiaID == null) {
            log.warn("getFirma :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }

        return getInstance().getSignatureInfo(custodiaID);
    }
    
    
    @Override
    public byte[] getFirmaContent(String custodiaID) throws Exception {

        if (custodiaID == null) {
            log.warn("getFirma :: CustodiaID vale null !!!!!", new Exception());
            return null;
        }

        return getInstance().getSignature(custodiaID);
    }
    
    
    

    /**
     * Elimina completamente una custodia ( = elimicion completa de Anexo)
     *
     * @param custodiaID
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     * casos.
     */
    public boolean eliminarCustodia(String custodiaID) throws Exception {

        if (custodiaID == null) {
            log.warn("eliminarCustodia :: CustodiaID vale null !!!!!", new Exception());
            return false;
        } else {
            getInstance().deleteCustody(custodiaID);
            return true;
        }

    }


    /**
     * Solo elimina el archivo asociado al documento.
     *
     * //TODO Revisar: parece que no se emplea
     *
     * @param custodiaID
     * @return
     * @throws Exception
     */
    public boolean eliminarDocumento(String custodiaID) throws Exception {

        if (custodiaID == null) {
            log.warn("eliminarDocumento :: CustodiaID vale null !!!!!", new Exception());
            return false;
        } else {
            getInstance().deleteDocument(custodiaID);
            getInstance().deleteSignature(custodiaID);
            return true;
        }

    }


    /**
     * Solo elimina la el archivo asociado a la firma
     *
     * //TODO Revisar: parece que no se emplea
     *
     * @param custodiaID
     * @return
     * @throws Exception
     */
    public boolean eliminarFirma(String custodiaID) throws Exception {

        if (custodiaID == null) {
            log.warn("eliminarFirma :: CustodiaID vale null !!!!!", new Exception());
            return false;
        } else {
            getInstance().deleteDocument(custodiaID);
            return true;
        }

    }




    /**
     * Crea o actualiza un anexos en el sistema de custodia
     * @param name
     * @param file
     * @param signatureName
     * @param signature
     * @param signatureMode
     * @param custodyID Si vale null significa que creamos el archivo. Otherwise actualizamos el fichero.
     * @param custodyParameters JSON del registre
     * @return Identificador de custodia
     * @throws Exception
     */
    // TODO mime de doc i firma
    public String crearArchivo(String name, byte[] file, String signatureName, 
        byte[] signature,  int signatureMode, String custodyID, String custodyParameters) throws Exception {

        IDocumentCustodyPlugin instance = getInstance();

        if (custodyID == null) {
          custodyID = instance.reserveCustodyID(custodyParameters);
        }


        if (signatureMode == RegwebConstantes.TIPO_FIRMA_CSV) {
          // CSV == Referencia a DocumentCustody
          throw new Exception("Modo de firma(signatureMode) RegwebConstantes.TIPO_FIRMA_CSV "
             + " no suportat. Es suportarà a partir de la posada en marxa de l'Arxiu Electrònic");
        }
        
        
        if (name != null && file != null) {
          DocumentCustody document = new DocumentCustody(name, file);
          instance.saveDocument(custodyID, custodyParameters, document);
        }



        if(signatureName != null && signature != null) {
          
          SignatureCustody docSignature = new SignatureCustody();
          docSignature.setName(signatureName);
          docSignature.setData(signature);
          
          final long signType = (long)signatureMode;
          
          // Cases en doc. doc/AnalisiGestioDocumentsSIRDocumentCustodyAPI2Regweb.odt
          
          if (signType == RegwebConstantes.TIPO_FIRMA_XADES_DETACHED_SIGNATURE) {
            // Cas 4
            docSignature.setSignatureType(SignatureCustody.XADES_SIGNATURE);
            docSignature.setAttachedDocument(false);
          } else if(signType == RegwebConstantes.TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE) {
            // CAS 3
            docSignature.setSignatureType(SignatureCustody.XADES_SIGNATURE);
            docSignature.setAttachedDocument(true);
          } else if(signType == RegwebConstantes.TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE) {
            // CAS 4
            docSignature.setSignatureType(SignatureCustody.CADES_SIGNATURE);
            docSignature.setAttachedDocument(false);
          } else if(signType == RegwebConstantes.TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE) {
            // CAS 3
            docSignature.setSignatureType(SignatureCustody.CADES_SIGNATURE);
            docSignature.setAttachedDocument(true);
          } else if(signType == RegwebConstantes.TIPO_FIRMA_PADES) {
            // CAS 5
            docSignature.setSignatureType(SignatureCustody.PADES_SIGNATURE);
            docSignature.setAttachedDocument(null);
          } else { //    default:
            String msg = "No es suporta signatureMode amb valor  " + signatureMode;
            log.error(msg, new Exception());
            throw new Exception(msg);
          }


          instance.saveSignature(custodyID, custodyParameters, docSignature);
        }
        
        //log.info("Creamos el file: " + getArchivosPath()+dstId.toString());

        return custodyID;
      }

    

    /* FIN METODOS DEL AnnexDocumentCustodyManager.java hecho por marilen del TODO DE TONI*/
}
