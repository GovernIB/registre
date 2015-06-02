package es.caib.regweb.persistence.ejb;


import es.caib.regweb.model.Anexo;
import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.RegistroDetalle;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.persistence.utils.AnnexFileSystemManager;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

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
import java.util.Date;
import java.util.List;

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
  
  
    public javax.ejb.SessionContext ejbContext;

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

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
  
        updateCustodyInfoOfAnexo(anexoFull, custody, custodyParameters, custodyID,
            registroID, tipoRegistro);

        anexo = this.merge(anexo);
        
        final boolean isNew = false;
        // Crea historico y lo enlaza con el RegistroDetalle
        crearHistorico(anexoFull, usuarioEntidad, registroID, tipoRegistro, isNew);
        
        anexoFull.setAnexo(anexo);
        
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

      DocumentCustody doc = null;
      if (anexoFull.isDocumentoFileDelete()) {
        custody.deleteDocument(custodyID);
      } else {

        doc = anexoFull.getDocumentoCustody();

        if (doc != null && doc.getData() != null) {
          byte[] data = doc.getData();

          anexo.setTipoMIME(doc.getMime() == null? "application/octet-stream" : doc.getMime());

          anexo.setNombreFicheroAnexado(checkFileName(doc.getName() , "file.bin"));
          // TODO CHECK
          anexo.setTamano(new Long(data.length));
          anexo.setFechaCaptura((Date)new Timestamp(new Date().getTime()));

          custody.saveDocument(custodyID, custodyParameters, doc);
        }
      }

      if (anexoFull.isSignatureFileDelete()) {
        custody.deleteSignature(custodyID);
      } else {
        SignatureCustody signature = anexoFull.getSignatureCustody();
        if (signature != null && signature.getData() != null) {

          signature.setName(checkFileName(signature.getName(), "signature.bin"));

          if (signature.getMime() == null) {
            signature.setMime("application/octet-stream");
          }
          
          signature.setSignatureType(SignatureCustody.OTHER_SIGNATURE);
          // TODO Fallarà en update
          signature.setAttachedDocument(doc == null? true : false);
          
          custody.saveSignature(custodyID, custodyParameters, signature);
          
          
        }
      }

      // TODO Actualitzar Metadades
      /*
      String tipoDeDocumento; //  varchar(100)
      Date fechaDeEntradaEnElSistema; //             date
      String oficinaQueLoAnexa; //  varchar(80)
      String origen; //   varchar(25)
      String validez; //  varchar25)
      String formato; //  varchar(40)
      String tipoDocumental; // varchar(20)
      String  observacionRegistro; // varchar(100)
      
      
      Anexo anexo = anexoFull.getAnexo();
      if ("entrada".equals(tipoRegistro)) {
        RegistroEntrada re = registroEntradaEjb.findById(registroID);
        
        origen = String anexo.getOrigenCiudadanoAdmin();
        observacionRegistro = anexo.getObservaciones()
        
      } else {
        RegistroSalida rs = registroSalidaEjb.findById(registroID);
      }
      */
      
      
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
      log.info("XYZ Cercant registre amb id = '" + registroID + "' de tipus '" + tipoRegistro + "'");
      
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

    
    // ----------------------------------------------------------------------
    // TODO REVISAR SI AQUESTS METODE S'UTILITZEN
    // ----------------------------------------------------------------------
    
     /**
      * Actualiza un anexo de la bbdd y del sistema de anexos
      * @param anexo
      * @return
      */
    /*
     @Override
     public boolean actualizarAnexo(Anexo anexo) throws Exception{
         if (anexo != null) {
             try {
                 if (findById(anexo.getId()) != null) {
                     merge(anexo);
                 }
             } catch (Exception e) {
                 log.error("Error actualitzant anexo amb id=" + anexo.getId()
                         + "(" + anexo.getNombreFicheroAnexado() + ")");
             }
         }
         return true;
     }
     */

     /**
      * XYZ Eliminar !!!!!
      * 
      * Actualiza un anexo de la bbdd y le asocia los archivos
      * @param idAnexo
      * @return
      */
    /*
     @Override
     public Anexo actualizarAnexoConArchivos(Long idAnexo, byte[] ficheroAnexado,
         String nombreFicheroAnexado, String tipoMIMEFicheroAnexado, Long tamanoFicheroAnexado,
         byte[] firmaAnexada, String nombreFirmaAnexada, String tipoMIMEFirmaAnexada,
         Long tamanoFirmaAnexada, Integer modoFirma, Date fechaCaptura ) throws Exception{

          log.info("Entramos en actualizarAnexoConArchivos");
          Anexo anexoActual = findById(idAnexo);



          if(ficheroAnexado!= null && ficheroAnexado.length>0 && !StringUtils.isEmpty(nombreFicheroAnexado)){
              log.info("Entramos en if fichero anexado");
              // recortamos el nombre del fichero anexado al maximo que permite sicres3
              String nombreFicheroAnexadoRec= StringUtils.recortarNombre(nombreFicheroAnexado, RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH);

              log.info("RECORTADO " + nombreFicheroAnexadoRec);
              anexoActual.setTipoMIME(tipoMIMEFicheroAnexado);
              anexoActual.setNombreFicheroAnexado(nombreFicheroAnexadoRec);
              anexoActual.setTamano(tamanoFicheroAnexado);
              anexoActual.setFechaCaptura(fechaCaptura);

              String custodiaID;
              // Si hay firma detached
              if(firmaAnexada != null && firmaAnexada.length>0 && !StringUtils.isEmpty(nombreFirmaAnexada)){
                log.info("Entramos en firmaAnexada");

                // recortamos el nombre del fichero anexado al maximo que permite sicres3
                 String nombreFirmaAnexadaRec= StringUtils.recortarNombre(nombreFirmaAnexada, RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH);
                 log.info("RECORTADA " + nombreFirmaAnexadaRec);
                 anexoActual.setNombreFirmaAnexada(nombreFirmaAnexadaRec);

                // actualizarAnexo(anexoActual);
                anexoActual = merge(anexoActual);
                
                // TODO Juntar mètode actualizarAnexo i crearArchivo
                
                // TODO  S'ha d'afegir registre en format JSON dins custodyParameters
                final String custodyParameters = null;
                
                // Creamos el archivo
                custodiaID = AnnexFileSystemManager.crearArchivo(nombreFicheroAnexadoRec, ficheroAnexado,
                    nombreFirmaAnexadaRec, firmaAnexada , modoFirma, anexoActual.getCustodiaID(), custodyParameters);
              }else{ // Si no hay se guarda el archivo solo sin firma
                log.info("Entramos en else archivo");
                anexoActual.setNombreFirmaAnexada("");
                anexoActual = merge(anexoActual);
                
                // TODO Juntar mètode actualizarAnexo i crearArchivo
                
                // TODO  S'ha d'afegir registre en format JSON dins custodyParameters
                final String custodyParameters = null;
                
                custodiaID = AnnexFileSystemManager.crearArchivo(nombreFicheroAnexadoRec,
                    ficheroAnexado,null, null , modoFirma, anexoActual.getCustodiaID(), custodyParameters);
              }
              
              
              log.info("XYZ 111 CREAR ARCHIVO ANEXO: " + custodiaID);
              
              anexoActual.setCustodiaID(custodiaID);
              anexoActual = merge(anexoActual);
              
          }

          return anexoActual;
     }
     */

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
