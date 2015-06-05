package es.caib.regweb.persistence.ejb;


import es.caib.regweb.model.Anexo;
import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.IRegistro;
import es.caib.regweb.model.RegistroDetalle;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.TraduccionTipoDocumental;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.persistence.utils.AnnexFileSystemManager;
import es.caib.regweb.persistence.utils.I18NLogicUtils;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.Configuracio;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.utils.Metadata;
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
import java.sql.Timestamp;
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

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @EJB(mappedName = "regweb/RegistroEntradaCambiarEstadoEJB/local")
    public RegistroEntradaCambiarEstadoLocal registroEntradaEjb;

    @EJB(mappedName = "regweb/RegistroSalidaCambiarEstadoEJB/local")
    public RegistroSalidaCambiarEstadoLocal registroSalidaEjb;

    @EJB(mappedName = "regweb/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;
    
    @EJB(mappedName = "regweb/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    
    
    final class java_util_Date_PersistenceDelegate extends PersistenceDelegate {
      protected Expression instantiate(Object oldInstance, Encoder out) {
          Date date = (Date) oldInstance;
          return new Expression(date, date.getClass(), "new", new Object[] {date.getTime()});
      }
  }
    
    
    @Override
    public AnexoFull getAnexoFull(Long anexoID) throws I18NException {
      
      try {
        Anexo anexo = em.find(Anexo.class, anexoID);
        
        String custodyID = anexo.getCustodiaID();
        
        AnexoFull anexoFull = new AnexoFull(anexo);

        IDocumentCustodyPlugin custody = AnnexFileSystemManager.getInstance();
        
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
    public AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
        Long registroID, String tipoRegistro) throws I18NException {
      
      IDocumentCustodyPlugin custody = null;
      boolean error = false;
      String custodyID = null;
      try {
        custody = AnnexFileSystemManager.getInstance();
        Anexo anexo = anexoFull.getAnexo();
        
        // TODO Validador
        
        anexo.setFechaCaptura((Date)new Timestamp(new Date().getTime()));

        
        final String custodyParameters = getCustodyParameters(registroID, tipoRegistro);
        
        custodyID = custody.reserveCustodyID(custodyParameters);
        anexo.setCustodiaID(custodyID);
  
        updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
            registroID, tipoRegistro);

        anexo = this.persist(anexo);
        
        final boolean isNew = true;
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

    
    @Override
    public AnexoFull actualizarAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
        Long registroID, String tipoRegistro) throws I18NException {
      
      try {
        
        Anexo anexo = anexoFull.getAnexo();
        
        // TODO Validador
        
        anexo.setFechaCaptura((Date)new Timestamp(new Date().getTime()));
        
        IDocumentCustodyPlugin custody = AnnexFileSystemManager.getInstance();
        
        final String custodyParameters = getCustodyParameters(registroID, tipoRegistro);
        
        
        final String custodyID = anexo.getCustodiaID();
        
        anexo = this.merge(anexo);
        anexoFull.setAnexo(anexo);
        
        final boolean isNew = false;
        // Crea historico y lo enlaza con el RegistroDetalle
        crearHistorico(anexoFull, usuarioEntidad, registroID, tipoRegistro, isNew);
  
        updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
            registroID, tipoRegistro);

        return anexoFull;
      
      } catch(I18NException i18n) {
        throw i18n;
      } catch(Exception e) {
        log.error("Error actualitzant un anexe: " + e.getMessage(), e);
        throw new I18NException(e, "anexo.error.guardando", new I18NArgumentString(e.getMessage()));
      }
      
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
               registroEntradaEjb.cambiarEstado(registroID, RegwebConstantes.ESTADO_PENDIENTE_VISAR);

               // Creamos el historico de registro de entrada
               historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);

            }

        } else {// MODIFICACION DE ANEXO

            if(dias >= entidadActiva.getDiasVisado()){ // Si han pasado más de los dias de visado cambiamos estado registro
                registroEntradaEjb.cambiarEstado(registroID, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
            }

            // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);
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
               registroSalidaEjb.cambiarEstado(registroID, RegwebConstantes.ESTADO_PENDIENTE_VISAR);

               // Creamos el historico de registro de entrada
               historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);
            }

        } else {// MODIFICACION DE ANEXO

            if(dias >= entidadActiva.getDiasVisado()){ // Si han pasado más de los dias de visado cambiamos estado registro
                registroSalidaEjb.cambiarEstado(registroID, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
            }
            // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
            historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);
        }
        anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
      }
    }
    



    protected void updateCustodyInfoOfAnexo(AnexoFull anexoFull, IDocumentCustodyPlugin custody,
        final String custodyParameters, final String custodyID, 
        final Long registroID, final String tipoRegistro) throws Exception {

      // TODO Falta Check DOC
      Anexo anexo = anexoFull.getAnexo();
      
      boolean updateDate = false;
      String mime = null;;

      DocumentCustody doc = null;
      if (anexoFull.isDocumentoFileDelete()) {
        custody.deleteDocument(custodyID);
        updateDate = true;
      } else {

        doc = anexoFull.getDocumentoCustody();

        if (doc != null && doc.getData() != null) {
          //byte[] data = doc.getData();

          if(doc.getMime() == null) {
            doc.setMime("application/octet-stream");
          }
          mime = doc.getMime();

          doc.setName(checkFileName(doc.getName() , "file.bin"));
          // TODO CHECK
          //anexo.setTamano(new Long(data.length));
          anexo.setFechaCaptura((Date)new Timestamp(new Date().getTime()));

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

          signature.setName(checkFileName(signature.getName(), "signature.bin"));

          if (signature.getMime() == null) {
            signature.setMime("application/octet-stream");
          }
          if (mime == null) {
            mime = signature.getMime();
          }
          
          signature.setSignatureType(SignatureCustody.OTHER_SIGNATURE);
          // TODO Fallarà en update
          signature.setAttachedDocument(doc == null? true : false);
          
          custody.saveSignature(custodyID, custodyParameters, signature);
          
          updateDate = true;
        }
      }

      // TODO Actualitzar Metadades
      List<Metadata> metadades = new ArrayList<Metadata>();
      
      // fechaDeEntradaEnElSistema 
      if (updateDate) {
        metadades.add(new Metadata("anexo.fechaCaptura", anexo.getFechaCaptura()));
      }
      

      IRegistro registro;
      if ("entrada".equals(tipoRegistro)) {
        registro = registroEntradaEjb.findById(registroID);
      } else {
        registro = registroSalidaEjb.findById(registroID);
      }
      
      // TODO String tipoDeDocumento; //  varchar(100)

      if (registro.getOficina() != null) {
        metadades.add(new Metadata("oficina",
            registro.getOficina().getNombreCompleto()));
      }

      final String lang = Configuracio.getDefaultLanguage(); 
      final Locale loc = new Locale(lang);
      //  varchar(80)
      
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
      if (anexo.getValidezDocumento() != null) {
        metadades.add(new Metadata("anexo.validezDocumento", 
            I18NLogicUtils.tradueix(loc, "tipoValidezDocumento." + anexo.getValidezDocumento())));
      }
      
      if (mime != null) {
        metadades.add(new Metadata("anexo.formato", mime));
      }

      if (anexo.getTipoDocumental() != null) {
        metadades.add(new Metadata("anexo.tipoDocumental", anexo.getTipoDocumental().getCodigoNTI()));
      }
      if (anexo.getObservaciones() != null) {
        metadades.add(new Metadata("anexo.observaciones", anexo.getObservaciones()));
      }
      
      for (Metadata metadata : metadades) {
        if (metadata.getValue() != null) {
          custody.deleteMetadata(custodyID, metadata.getKey());
          custody.addMetadata(custodyID, metadata);
        }
      }
      
    }

    
    
    protected static String checkFileName(String name, String defaultName) throws Exception  {
      if (name == null || name.trim().length() == 0) {
        return defaultName;
      } else {
        return StringUtils.recortarNombre(name, RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH); 
      }
    }
    
    
    
    protected String getCustodyParameters(Long registroID, String tipoRegistro) throws Exception {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      XMLEncoder encoder = new XMLEncoder(baos);
      
      encoder.setPersistenceDelegate(java.sql.Date.class, new java_util_Date_PersistenceDelegate());
      encoder.setPersistenceDelegate(java.sql.Time.class, new java_util_Date_PersistenceDelegate());
      
      //ObjectMapper mapper = new ObjectMapper();
      //mapper.configure(Fe Config.FAIL_ON_EMPTY_BEANS, false);
      log.info("Cercant registre amb id = '" + registroID + "' de tipus '" + tipoRegistro + "'");
      
      if ("entrada".equals(tipoRegistro)) {
        RegistroEntrada re = registroEntradaEjb.findById(registroID);
       
        encoder.writeObject(re);
        //mapper.writeValue(baos, re);
      } else {
        RegistroSalida rs = registroSalidaEjb.findById(registroID);
        encoder.writeObject(rs);
        //mapper.writeValue(baos, rs);
        
      }
      encoder.flush();
      encoder.close();
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
    public boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle) throws Exception {

        Anexo anexo = findById(idAnexo);
        RegistroDetalle registroDetalle = registroDetalleEjb.findById(idRegistroDetalle);

        if(anexo != null && registroDetalle != null){
            log.info("Eliminar Anexo: " + registroDetalle.getAnexos().remove(anexo));
            registroDetalleEjb.merge(registroDetalle);
            remove(anexo);
        }
        return AnnexFileSystemManager.eliminarCustodia(anexo.getCustodiaID());
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


}
