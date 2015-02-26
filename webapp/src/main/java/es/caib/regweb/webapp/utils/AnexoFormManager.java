package es.caib.regweb.webapp.utils;

import es.caib.regweb.model.Anexo;
import es.caib.regweb.persistence.ejb.AnexoLocal;
import es.caib.regweb.utils.AnnexFileSystemManager;

import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created 6/06/14 8:57
 *
 * @author mgonzalez
 */
public class AnexoFormManager {

      protected final Logger log = Logger.getLogger(getClass());

      private final AnexoLocal anexoEjb;
      private final MultipartFile anexoSubido;
      private final MultipartFile firmaAnexoSubido;
      private Anexo anexoActual;
      private String ruta;

      public AnexoFormManager(AnexoLocal anexoEjb, MultipartFile anexoSubido, MultipartFile firmaAnexoSubido, String ruta) {
          this.anexoEjb = anexoEjb;
          this.anexoSubido = anexoSubido;
          this.firmaAnexoSubido = firmaAnexoSubido;
          this.ruta = ruta;
      }

      /**
       * Metodo que gestiona y guarda los archivos asociados a un anexo y
       * actualiza el anexo con el resto de campos derivados de los archivos(doc y firma)
       * @param anexo anexo que hay que actualizar
       * @return
       * @throws Exception
       */
      public Anexo prePersist(Anexo anexo) throws Exception {

          //anexoActual = new Anexo();
          log.info("Anexo id" + anexo.getId());

          // recortamos el nombre del fichero anexado al maximo que permite sicres3
          String nombreFicheroAnexado= StringUtils.recortarNombre(anexoSubido.getOriginalFilename(), RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH);

          anexoActual = anexo;
          anexoActual.setTipoMIME(anexoSubido.getContentType());
          anexoActual.setNombreFicheroAnexado(nombreFicheroAnexado);
          anexoActual.setTamano(Long.valueOf(anexoSubido.getBytes().length));
          anexoActual.setFechaCaptura(new Date());


          // Si hay firma detached
          if(firmaAnexoSubido != null){
            log.info("Entramos en firmaAnexoSubido");
            log.info(firmaAnexoSubido.getOriginalFilename());
            // recortamos el nombre del fichero anexado al maximo que permite sicres3
             String nombreFirmaAnexada= StringUtils.recortarNombre(firmaAnexoSubido.getOriginalFilename(), RegwebConstantes.ANEXO_NOMBREARCHIVO_MAXLENGTH);
             anexoActual.setNombreFirmaAnexada(nombreFirmaAnexada);
             anexoEjb.actualizarAnexo(anexoActual);
            // Creamos el archivo
            AnnexFileSystemManager.crearArchivo(nombreFicheroAnexado, anexoSubido.getBytes(), nombreFirmaAnexada, firmaAnexoSubido.getBytes() , anexoActual.getId(), anexo.getModoFirma());
          }else{ // Si no hay se guarda el archivo solo sin firma
            anexoActual.setNombreFirmaAnexada("");
            anexoEjb.actualizarAnexo(anexoActual);
            AnnexFileSystemManager.crearArchivo(nombreFicheroAnexado, anexoSubido.getBytes(),null, null , anexoActual.getId(), anexo.getModoFirma());
          }
          return anexoActual;
      }

      /**
       * Si ha habido un error, elimina el Anexo de la bbdd y del sistema de archivos
       * @throws Exception
       */
      public void processError() throws Exception {

          anexoEjb.remove(this.anexoActual);
          AnnexFileSystemManager.eliminarArchivo(this.anexoActual.getId());
      }

      public void processErrorAnexosWithoutThrowException() {

          try {
              processError();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

}
