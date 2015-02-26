package es.caib.regweb.persistence.ejb;


import es.caib.regweb.model.Anexo;
import es.caib.regweb.model.RegistroDetalle;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.utils.AnnexFileSystemManager;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Stateless(name = "AnexoEJB")
@SecurityDomain("seycon")
public class AnexoBean extends BaseEjbJPA<Anexo, Long> implements AnexoLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;

    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;

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
    public boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle) throws Exception{

        Anexo anexo = findById(idAnexo);
        RegistroDetalle registroDetalle = registroDetalleEjb.findById(idRegistroDetalle);

        if(anexo != null && registroDetalle != null){
            log.info("Eliminar Anexo: " + registroDetalle.getAnexos().remove(anexo));
            registroDetalleEjb.merge(registroDetalle);
            remove(anexo);
        }
        return AnnexFileSystemManager.eliminarArchivo(anexo.getId());
    }

     /**
      * Actualiza un anexo de la bbdd y del sistema de anexos
      * @param anexo
      * @return
      */
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

     /**
      * Actualiza un anexo de la bbdd y le asocia los archivos
      * @param idAnexo
      * @return
      */
     @Override
     public Anexo actualizarAnexoConArchivos(Long idAnexo, byte[] ficheroAnexado,String nombreFicheroAnexado, String tipoMIMEFicheroAnexado, Long tamanoFicheroAnexado,
                                    byte[] firmaAnexada, String nombreFirmaAnexada, String tipoMIMEFirmaAnexada, Long tamanoFirmaAnexada, Integer modoFirma, Date fechaCaptura ) throws Exception{

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


              // Si hay firma detached
              if(firmaAnexada != null && firmaAnexada.length>0 && !StringUtils.isEmpty(nombreFirmaAnexada)){
                log.info("Entramos en firmaAnexada");

                // recortamos el nombre del fichero anexado al maximo que permite sicres3
                 String nombreFirmaAnexadaRec= StringUtils.recortarNombre(nombreFirmaAnexada, RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH);
                 log.info("RECORTADA " + nombreFirmaAnexadaRec);
                 anexoActual.setNombreFirmaAnexada(nombreFirmaAnexadaRec);

                // actualizarAnexo(anexoActual);
                anexoActual = merge(anexoActual);
                // Creamos el archivo
                AnnexFileSystemManager.crearArchivo(nombreFicheroAnexadoRec, ficheroAnexado, nombreFirmaAnexadaRec, firmaAnexada , anexoActual.getId(), modoFirma);
              }else{ // Si no hay se guarda el archivo solo sin firma
                log.info("Entramos en else archivo");
                anexoActual.setNombreFirmaAnexada("");
                anexoActual = merge(anexoActual);
                AnnexFileSystemManager.crearArchivo(nombreFicheroAnexadoRec, ficheroAnexado,null, null , anexoActual.getId(), modoFirma);
              }
          }

          return anexoActual;
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
