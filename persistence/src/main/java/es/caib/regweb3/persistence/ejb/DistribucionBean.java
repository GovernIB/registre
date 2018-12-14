package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.DestinatarioWrapper;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.plugins.distribucion.ConfiguracionDistribucion;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mgonzalez on 27/11/2018.
 */
@Stateless(name = "DistribucionEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DistribucionBean implements DistribucionLocal{

   protected final Logger log = Logger.getLogger(getClass());

   @EJB private RegistroEntradaLocal registroEntradaEjb;
   @EJB private JustificanteLocal justificanteEjb;
   @EJB private IntegracionLocal integracionEjb;
   @EJB private PluginLocal pluginEjb;
   @EJB private ColaLocal colaEjb;
   @EJB private UsuarioEntidadLocal usuarioEntidadEjb;


   @Override
   public RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException, I18NValidationException {

      RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();

      log.info("------------------------------------------------------------");
      log.info("Distribuyendo el registro: " + re.getNumeroRegistroFormateado());
      log.info("");

      //Información a guardar de la integración
      StringBuilder peticion = new StringBuilder();
      long tiempo = System.currentTimeMillis();
      String descripcion = "Distribución Registro";
      String numRegFormat = "";

      String hora = "<b>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "</b>&nbsp;&nbsp;&nbsp;";

      //Montamos la petición de la integración
      peticion.append("usuario: ").append(re.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
      peticion.append("registro: ").append(re.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
      peticion.append("oficina: ").append(re.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
      peticion.append("clase: ").append(re.getClass().getName()).append(System.getProperty("line.separator"));

      //Obtenemos el numero máximo de reintentos de una propiedad global
      Integer maxReintentos = PropiedadGlobalUtil.getMaxReintentosCola(usuarioEntidad.getEntidad().getId());

      //Obtenemos plugin
      try {
         IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(usuarioEntidad.getEntidad().getId(), RegwebConstantes.PLUGIN_DISTRIBUCION);

         //Si han especificado plug-in
         if (distribucionPlugin != null) {

            numRegFormat = re.getNumeroRegistroFormateado();

            respuestaDistribucion.setHayPlugin(true);

            //Obtenemos la configuración de la distribución
            ConfiguracionDistribucion configuracionDistribucion = distribucionPlugin.configurarDistribucion();
            respuestaDistribucion.setListadoDestinatariosModificable(configuracionDistribucion.isListadoDestinatariosModificable());

            if (configuracionDistribucion.isListadoDestinatariosModificable()) {// Si es modificable, mostraremos pop-up
               respuestaDistribucion.setDestinatarios(distribucionPlugin.distribuir(re)); // isListado = true , puede escoger a quien lo distribuye de la listas propuestas.
            } else { // Si no es modificable, obtendra los destinatarios del propio registro y nos saltamos una llamada al plugin

               if(configuracionDistribucion.isEnvioCola()){ //Si esta configurado para enviarlo a la cola
                  colaEjb.enviarAColaDistribucion(re,usuarioEntidad, maxReintentos);
                  respuestaDistribucion.setEnviadoCola(true);

                  return respuestaDistribucion;

               }
            }
         }

         // Distribuimos el registro
         Boolean distribuido = distribuirRegistroEntrada(re,distribucionPlugin, descripcion, tiempo, peticion);

         respuestaDistribucion.setEnviado(distribuido);


      } catch (I18NException i18ne) {
         try {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18ne, null,System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
         } catch (Exception e) {
            e.printStackTrace();
         }
         throw i18ne;
      } catch (Exception e) {
         try {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), e, null,System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
         } catch (Exception ex) {
            ex.printStackTrace();
         }
         throw e;
      } catch (I18NValidationException i18vn) {
         try {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18vn, null,System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);
         } catch (Exception ex) {
            ex.printStackTrace();
         }
         throw i18vn;
      }

      return respuestaDistribucion;
   }

   @Override
   public Boolean distribuirRegistroEntrada(RegistroEntrada registroEntrada, IDistribucionPlugin distribucionPlugin, String descripcion, long tiempo, StringBuilder peticion) throws Exception, I18NValidationException, I18NException {

      log.info("DISTRIBUYENDO REGISTRO: " + registroEntrada.getNumeroRegistroFormateado());

      AnexoFull justificante = null;
      if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
         justificante = justificanteEjb.crearJustificante(registroEntrada.getUsuario(), registroEntrada, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
         registroEntrada.getRegistroDetalle().getAnexosFull().add(justificante);
      }

      if(distribucionPlugin == null){
         //Tramitamos el registro de entrada directamente
         registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, registroEntrada.getUsuario());
         integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroEntrada.getUsuario().getEntidad().getId(), registroEntrada.getNumeroRegistroFormateado());
         return true;
      }else{
         //Gestionamos los anexos sir antes de distribuir
         registroEntrada = gestionFicherosTecnicos(registroEntrada);
         boolean distribuido = distribucionPlugin.enviarDestinatarios(registroEntrada, null, "", new Locale("ca"));
         if (distribuido) { //Si ha ido bien lo marcamos como distribuido
            registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, registroEntrada.getUsuario());
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroEntrada.getUsuario().getEntidad().getId(), registroEntrada.getNumeroRegistroFormateado());
         }

         return distribuido;
      }

   }

   @Override
   @TransactionTimeout(value = 1200)  // 20 minutos
   public void distribuirRegistrosEnCola(Long idEntidad) throws Exception{

      try {

         //Obtenemos todos los administradores de la entidad
         List<UsuarioEntidad> administradores = usuarioEntidadEjb.findAdministradoresByEntidad(idEntidad);

         //Petición: Construimos los mensajes para guardar la información de la integración
         long tiempo = System.currentTimeMillis();
         String descripcion = "Distribución desde Cola";
         String hora = "<b>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "</b>&nbsp;&nbsp;&nbsp;";

         //Obtenermos plugin distribución
         IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_DISTRIBUCION);

         log.info("Iniciamos la distribucion de la cola");

         //Obtenemos el numero máximo de reintentos de una propiedad global
         Integer maxReintentos = PropiedadGlobalUtil.getMaxReintentosCola(idEntidad);

         //obtiene un numero de elementos (configurable) pendientes de distribuir que estan en la cola
         List<Cola> elementosADistribuir = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_DISTRIBUCION, idEntidad, RegwebConstantes.NUMELEMENTOSDISTRIBUIR, maxReintentos);

         for (Cola elementoADistribuir : elementosADistribuir) {
            StringBuilder peticion = new StringBuilder();
            try {

               //Obtenemos el registro de entrada que se debe distribuir
               RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(elementoADistribuir.getIdObjeto());

               //Montamos la petición de la integración
               peticion.append("usuario: ").append(registroEntrada.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
               peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
               peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
               if(distribucionPlugin != null){
                  peticion.append("clase: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));
               }

               //Distribuimos el registro de entrada.
               Boolean distribuido = distribuirRegistroEntrada(registroEntrada, distribucionPlugin, descripcion, tiempo, peticion);

               if (distribuido) { //Si la distribución ha ido bien

                  colaEjb.remove(elementoADistribuir); //Eliminamos el elemento de la cola
                  log.info("distribucion OK REGISTRO " + registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir.getIdObjeto());

               } else { //No ha ido bien, el plugin nos dice que no ha ido bien
                  log.info("Distribucion Error REGISTRO " + registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir.getIdObjeto());
                  //Actualizamos los diferentes datos del elemento a distribuir(incremento de intentos, envio de mails, etc)
                  colaEjb.actualizarElementoCola(elementoADistribuir, descripcion, peticion, tiempo, idEntidad, hora, "ca", null, administradores, maxReintentos);
               }

            } catch (Exception e) {
               log.info("Primer Exception ");
               e.printStackTrace();
               colaEjb.actualizarElementoCola(elementoADistribuir, descripcion, peticion, tiempo, idEntidad, hora, "ca", e, administradores, maxReintentos);
            } catch (I18NException e) {
               log.info("Primer I18NException ");
               e.printStackTrace();
               colaEjb.actualizarElementoCola(elementoADistribuir, descripcion, peticion, tiempo, idEntidad, hora, "ca", e, administradores, maxReintentos);
            } catch (I18NValidationException e) {
               log.info("Primer I18NValidationException");
               e.printStackTrace();
               colaEjb.actualizarElementoCola(elementoADistribuir, descripcion, peticion, tiempo, idEntidad, hora, "ca", e, administradores, maxReintentos);
            } catch (Throwable t) {
               log.info("Primer Throwable");
               t.printStackTrace();
               colaEjb.actualizarElementoCola(elementoADistribuir, descripcion, peticion, tiempo, idEntidad, hora, "ca", t, administradores, maxReintentos);
            }
         }

      } catch (I18NException e) {
         e.printStackTrace();
      }

   }



   @Override
   public Boolean enviar(RegistroEntrada re, DestinatarioWrapper wrapper,
                         Long entidadId, String idioma) throws Exception, I18NException, I18NValidationException {

      // Información de la integración
      StringBuilder peticion = new StringBuilder();
      long tiempo = System.currentTimeMillis();
      String descripcion = "Distribución Registro Modificable";
      Boolean distribucionOk = false; //Estado de la distribución

      //Obtenemos plugin
      try {
         IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_DISTRIBUCION);
         if (distribucionPlugin != null) {

            distribucionOk = distribuirRegistroEntrada(re,distribucionPlugin, descripcion, tiempo, peticion);

         }
         return distribucionOk;
      } catch (I18NException i18ne) {
         try {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18ne, null,System.currentTimeMillis() - tiempo, entidadId,re.getNumeroRegistroFormateado());
         } catch (Exception e) {
            e.printStackTrace();
         }
         throw i18ne;
      } catch (Exception e) {
         try {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadId,re.getNumeroRegistroFormateado());
         } catch (Exception ex) {
            e.printStackTrace();
         }
         throw e;
      } catch (I18NValidationException i18vn) {
         try {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(), i18vn, null,System.currentTimeMillis() - tiempo, entidadId,re.getNumeroRegistroFormateado());
         } catch (Exception ex) {
            ex.printStackTrace();
         }
         throw i18vn;
      }

   }



   /**
    * Método que prepara el registro de entrada para distribuirlo.
    * La variable confAnexos indica que datos se envian en el segmento de anexo del registro de entrada.
    * <p/>
    * 1 = custodiaId + metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
    * 2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
    * 3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
    *
    * @param original
    * @param confAnexos
    * @return
    * @throws Exception
    * @throws I18NException
    */
   private RegistroEntrada obtenerAnexosDistribucion(RegistroEntrada original, int confAnexos) throws Exception, I18NException, I18NValidationException {


      // Miramos si debemos generar el justificante
      AnexoFull justificante = null;
      if(!original.getRegistroDetalle().getTieneJustificante()) {
         justificante = justificanteEjb.crearJustificante(original.getUsuario(), original, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
      }

      switch (confAnexos) {
         case 1: {//1.  Fitxer + firma + metadades + custodiaId
            registroEntradaEjb.cargarAnexosFull(original);
            if(justificante!= null){
               original.getRegistroDetalle().getAnexosFull().add(justificante);
            }
            break;
         }
         case 2: {//2. custodiaId

            // Montamos una nueva lista de anexos solo con el custodiaID, sin metadatos ni nada
            List<Anexo> anexos = original.getRegistroDetalle().getAnexos();
            List<Anexo> nuevosAnexos = new ArrayList<Anexo>();
            for (Anexo anexo : anexos) {
               Anexo nuevoAnexo = new Anexo();
               nuevoAnexo.setId(anexo.getId());
               nuevoAnexo.setJustificante(anexo.isJustificante());
               nuevoAnexo.setCustodiaID(anexo.getCustodiaID());
               nuevosAnexos.add(nuevoAnexo);
            }
            //Añadimos el justificante si lo acabamos de crear
            if(justificante != null){
               Anexo anexoJust = new Anexo();
               anexoJust.setId(justificante.getAnexo().getId());
               anexoJust.setJustificante(justificante.getAnexo().isJustificante());
               anexoJust.setCustodiaID(justificante.getAnexo().getCustodiaID());
               nuevosAnexos.add(justificante.getAnexo());
            }
            original.getRegistroDetalle().setAnexos(nuevosAnexos);
            break;
         }
         case 3: {// 3. custodiaId + metadades (no se hace nada, es el caso por defecto)

            //añadimos el justificante si lo acabamos de crear
            if(justificante != null){
               original.getRegistroDetalle().getAnexos().add(justificante.getAnexo());
            }
         }

      }
      return original;
   }


   /**
    *  Este método elimina los anexos que no se pueden enviar a Arxiu porque no estan soportados.
    *  Son ficheros xml de los cuales no puede hacer el upgrade de la firma y se ha decidido que no se distribuyan.
    *
    * @param original
    * @return
    * @throws Exception
    * @throws I18NException
    * @throws I18NValidationException
    */
   private RegistroEntrada gestionAnexosByAplicacionSIR(RegistroEntrada original) throws Exception, I18NException, I18NValidationException {

      List<AnexoFull> anexosFullADistribuir = new ArrayList<AnexoFull>();
      //Obtenemos los anexos del registro para tratarlos
      List<AnexoFull> anexosFull =original.getRegistroDetalle().getAnexosFull();
      //Lista de anexos para el procesamiento intermedio
      List<AnexoFull> anexosFullIntermedio = new ArrayList<AnexoFull>();


      gestionarByAplicacionByNombreFichero( RegwebConstantes.FICHERO_REGISTROELECTRONICO, anexosFull, anexosFullIntermedio);
      gestionarByAplicacionByNombreFichero(RegwebConstantes.FICHERO_DEFENSORPUEBLO, anexosFullIntermedio, anexosFullADistribuir);

      original.getRegistroDetalle().setAnexosFull(anexosFullADistribuir);

      return original;
   }


   /**
    *  Este método elimina los anexos que no se pueden enviar a Arxiu porque no estan soportados.
    *  Son ficheros xml de los cuales no puede hacer el upgrade de la firma y se ha decidido que no se distribuyan.
    *
    * @param original
    * @return
    * @throws Exception
    * @throws I18NException
    * @throws I18NValidationException
    */
   private RegistroEntrada gestionFicherosTecnicos(RegistroEntrada original) throws Exception, I18NException, I18NValidationException {

      List<AnexoFull> anexosFullADistribuir = new ArrayList<AnexoFull>();
      //Obtenemos los anexos del registro para tratarlos
      List<AnexoFull> anexosFull =original.getRegistroDetalle().getAnexosFull();

      for(AnexoFull anexoFull: anexosFull){
         if(!RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO.equals(anexoFull.getAnexo().getTipoDocumento())){
            anexosFullADistribuir.add(anexoFull);
         }
      }

      original.getRegistroDetalle().setAnexosFull(anexosFullADistribuir);

      return original;
   }


   /**
    * Este método lo que hace es eliminar de la lista de anexos a distribuir, aquellos que el Arxiu no soporta que son
    * de formato xml y que no puede hacer el upgrade de la firma.
    * Lo que hace es comparar el nombre del Fichero y la aplicación de la que procede, para determinar si lo elimina o no.
    * @param nombreFichero
    * @param anexosFull
    * @param anexosFullADistribuir
    * @return
    * @throws Exception
    */
   private List<AnexoFull> gestionarByAplicacionByNombreFichero(String nombreFichero, List<AnexoFull> anexosFull, List<AnexoFull> anexosFullADistribuir) throws Exception {
      //para cada uno de los anexos miramos si es uno de los conflictivos.
      //Estan tipificados en Regweb3Constantes.
      for (AnexoFull anexoFull: anexosFull) {
         String nombreFicheroTemp = "";
         //Si es un documento sin firma, el nombre està en DocumentCustody
         if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA){
            nombreFicheroTemp = anexoFull.getDocumentoCustody().getName();
         }else { // Si tiene firma, el nombre está en SignatureCustody.
            nombreFicheroTemp = anexoFull.getSignatureCustody().getName();
         }

         //Si el nombre del fichero es distinto al que nos han indicado, se puede distribuir
         if(!nombreFichero.equals(nombreFicheroTemp)){
            anexosFullADistribuir.add(anexoFull);
         }
      }
      return anexosFullADistribuir;
   }


}
