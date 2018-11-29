package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.DestinatarioWrapper;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.plugins.distribucion.ConfiguracionDistribucion;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

   @PersistenceContext(unitName = "regweb3")
   private EntityManager em;


   @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
   private RegistroEntradaLocal registroEntradaEjb;

   @EJB(mappedName = "regweb3/JustificanteEJB/local")
   private JustificanteLocal justificanteEjb;

   @EJB(mappedName = "regweb3/IntegracionEJB/local")
   private IntegracionLocal integracionEjb;

   @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
   private UsuarioEntidadLocal usuarioEntidadEjb;

   @EJB(mappedName = "regweb3/PluginEJB/local")
   private PluginLocal pluginEjb;

   @EJB(mappedName = "regweb3/ColaEJB/local")
   private ColaLocal colaEjb;


   @Override
   public RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException, I18NValidationException {

      log.info("------------------------------------------------------------");
      log.info("Distribuyendo el registro: " + re.getNumeroRegistroFormateado());
      log.info("");

      //Información a guardar de la integración
      StringBuilder peticion = new StringBuilder();
      long tiempo = System.currentTimeMillis();
      String descripcion = "Distribución Registro";
      String numRegFormat = "";

      RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();
      respuestaDistribucion.setHayPlugin(false);
      respuestaDistribucion.setDestinatarios(null);
      respuestaDistribucion.setEnviado(false);
      respuestaDistribucion.setEnviadoCola(false);

      //Obtenemos plugin
      try {
         IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(usuarioEntidad.getEntidad().getId(), RegwebConstantes.PLUGIN_DISTRIBUCION);

         //Si han especificado plug-in
         if (distribucionPlugin != null) {
            peticion.append("clase: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));
            peticion.append("numeroRegistro: ").append(re.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            numRegFormat = re.getNumeroRegistroFormateado();

            respuestaDistribucion.setHayPlugin(true);

            //Obtenemos la configuración de la distribución
            ConfiguracionDistribucion configuracionDistribucion = distribucionPlugin.configurarDistribucion();
            respuestaDistribucion.setListadoDestinatariosModificable(configuracionDistribucion.isListadoDestinatariosModificable());
            //Obtenemos los anexos en función de la configuración establecida
            //re = obtenerAnexosDistribucion(re, configuracionDistribucion.getConfiguracionAnexos());
            //Se gestionan los anexos a distribuir, en función de la aplicación SIR que los ha enviado
            re= gestionAnexosByAplicacionSIR(re);

            if (configuracionDistribucion.isListadoDestinatariosModificable()) {// Si es modificable, mostraremos pop-up
               respuestaDistribucion.setDestinatarios(distribucionPlugin.distribuir(re)); // isListado = true , puede escoger a quien lo distribuye de la listas propuestas.
            } else { // Si no es modificable, obtendra los destinatarios del propio registro y nos saltamos una llamada al plugin

               if(configuracionDistribucion.isEnvioCola()){ //Si esta configurado para enviarlo a la cola
                  enviarAColaDistribucion(re,usuarioEntidad, configuracionDistribucion.getMaxReintentos());
                  respuestaDistribucion.setEnviadoCola(true);
               }else {
                  //Generamos Justificante
                  AnexoFull justificante = null;
                  if(!re.getRegistroDetalle().getTieneJustificante()) {
                     justificante = justificanteEjb.crearJustificante(re.getUsuario(), re, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
                     re.getRegistroDetalle().getAnexosFull().add(justificante);
                  }

                  //Distribuimos directamente
                  Locale locale = new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO);
                  respuestaDistribucion.setEnviado(distribucionPlugin.enviarDestinatarios(re, null, "", locale));

                  // Si ya ha sido enviado, lo marcamos como tramitado.
                  if(respuestaDistribucion.getEnviado()){
                     //En tramitar entrada creamos la trazabilidad de distribución y con esa fecha trabajamos para obtener los anexos a purgar
                     registroEntradaEjb.tramitarRegistroEntrada(re,usuarioEntidad);

                     // Integración
                     integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(),numRegFormat);
                     log.info("");
                     log.info("Fin distribución del registro: " + re.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
                     log.info("------------------------------------------------------------");
                  }

               }

            }


         }else{ //No hay plugin, generamos justificante y marcamos el Registro como Tramitado
            //Validamos las firmas de los anexos
            // TODO (No se si hay que validar, porque aquí no distribuimos a ningun lado
               /* if(PropiedadGlobalUtil.validarFirmas()) {
                    for (AnexoFull anexoFull : re.getRegistroDetalle().getAnexosFull()) {
                        signatureServerEjb.checkDocument(anexoFull, usuarioEntidad.getEntidad().getId(), new Locale("ca"), false);
                    }
                }*/

            AnexoFull justificante = null;
            if(!re.getRegistroDetalle().getTieneJustificante()) {
               justificante = justificanteEjb.crearJustificante(re.getUsuario(), re, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
               re.getRegistroDetalle().getAnexosFull().add(justificante);
            }
            //En tramitar entrada creamos la trazabilidad de distribución y con esa fecha trabajamos para obtener los anexos a purgar
            registroEntradaEjb.tramitarRegistroEntrada(re,usuarioEntidad);
            //Integración
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, usuarioEntidad.getEntidad().getId(), numRegFormat);

            log.info("");
            log.info("Fin distribución del registro: " + re.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
            log.info("------------------------------------------------------------");
         }

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
   public void distribuirRegistrosEnCola(Long entidadId) throws Exception, I18NException, I18NValidationException{
      log.info("Entramos en distribuir registros en Cola " );
      //Obtenemos todos los administradores de la entidad
      List<UsuarioEntidad> administradores = usuarioEntidadEjb.findAdministradoresByEntidad(entidadId);
      //Obtenermos plugin distribución
      IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_DISTRIBUCION);
      //Iniciamos la distribución de la lista si existe el plugin
      if(distribucionPlugin != null) {
         log.info("Iniciamos la distribucion de la cola");
         iniciarDistribucionLista(entidadId, administradores,distribucionPlugin);
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
            ConfiguracionDistribucion configuracionDistribucion = distribucionPlugin.configurarDistribucion();
            Locale locale = new Locale(idioma);

            //Generamos el justificante porque antes no lo hemos hecho
            AnexoFull justificante = null;
            if(!re.getRegistroDetalle().getTieneJustificante()) {
               justificante = justificanteEjb.crearJustificante(re.getUsuario(), re, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
               re.getRegistroDetalle().getAnexosFull().add(justificante);
            }
            re= gestionAnexosByAplicacionSIR(re);

            distribucionOk = distribucionPlugin.enviarDestinatarios(re, wrapper.getDestinatarios(), wrapper.getObservaciones(), locale);
            //Integración
            if(distribucionOk){
               //Montamos la petición de la integración
               peticion.append("registro: ").append(re.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
               peticion.append("clase: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));
               //Tramitamos el registro de entrada
               registroEntradaEjb.tramitarRegistroEntrada(re, re.getUsuario());
               integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion,peticion.toString(),System.currentTimeMillis() - tiempo, entidadId, re.getNumeroRegistroFormateado());
               log.info("");
               log.info("Fin distribución del registro: " + re.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - tiempo));
               log.info("------------------------------------------------------------");
            }
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

   @Override
   public void enviarAColaDistribucion(RegistroEntrada re, UsuarioEntidad usuarioEntidad, int maxReintentos) throws Exception, I18NException, I18NValidationException {

      try {
         //Creamos un elemento nuevo de la cola de distribución
         Cola cola = new Cola();
         cola.setNumeroReintentos(0);
         cola.setIdObjeto(re.getId());
         cola.setDescripcionObjeto(re.getNumeroRegistroFormateado());
         cola.setTipo(RegwebConstantes.COLA_DISTRIBUCION);
         cola.setUsuarioEntidad(usuarioEntidad);
         cola.setDenominacionOficina(re.getOficina().getDenominacion());

         colaEjb.persist(cola);

         log.info("RegistroEntrada: " + re.getNumeroRegistroFormateado() + " enviado a la Cola de Distribución");
         registroEntradaEjb.cambiarEstadoHistorico(re,RegwebConstantes.REGISTRO_DISTRIBUYENDO, usuarioEntidad);

      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   /**
    * Inicia la distribución de X elementos de la cola
    * @param entidadId
    * @throws Exception
    * @throws I18NException
    * @throws I18NValidationException
    */
   @Override
   public void iniciarDistribucionLista(Long entidadId, List<UsuarioEntidad> administradores, IDistribucionPlugin plugin) throws Exception, I18NException, I18NValidationException{

      //Obtenemos plugin
      int maxReintentos=0;
      //Obtenemos el numero máximo de reintentos de la configuración del plugin
      if(plugin!= null) {
         maxReintentos = plugin.configurarDistribucion().getMaxReintentos();
      }
      //obtiene un numero de elementos (configurable) pendientes de distribuir que estan en la cola
      List<Cola> elementosADistribuir = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_DISTRIBUCION,entidadId,RegwebConstantes.NUMELEMENTOSDISTRIBUIR,maxReintentos);
      Cola elementoADistribuir1 = new Cola();

      //Construimos los mensajes para guardar la información de la integración
      StringBuilder peticion = new StringBuilder();
      long tiempo = System.currentTimeMillis();
      String descripcion = "Distribución desde Cola";
      String hora =  "<b>"+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "</b>&nbsp;&nbsp;&nbsp;";


      try {
         if (elementosADistribuir.size() > 0) {

            log.info(plugin.getClass());
            if (plugin != null) {
               for (Cola elementoADistribuir : elementosADistribuir) {
                  peticion= new StringBuilder();
                  try {
                     elementoADistribuir1 = elementoADistribuir;
                     //Obtenemos el registro de entrada que se debe distribuir
                     RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(elementoADistribuir1.getIdObjeto());

                     //Montamos la petición de la integración
                     peticion.append("usuario: ").append(registroEntrada.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
                     peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                     peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
                     peticion.append("clase: ").append(plugin.getClass().getName()).append(System.getProperty("line.separator"));
                        /*if (distribucionPlugin instanceof DistribucionRipeaPlugin){
                            for (AnexoFull anexoFull : registroEntrada.getRegistroDetalle().getAnexosFull()) {
                                signatureServerEjb.checkDocument(anexoFull, entidadId, new Locale("ca"), false);
                            }
                        }*/

                     log.info("DISTRIBUYENDO REGISTRO  " + registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir1.getIdObjeto());
                     //Si no tiene justificante lo generamos
                     AnexoFull justificante = null;
                     if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
                        justificante = justificanteEjb.crearJustificante(registroEntrada.getUsuario(), registroEntrada, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
                        registroEntrada.getRegistroDetalle().getAnexosFull().add(justificante);
                     }

                     //Gestionamos los anexos sir antes de distribuir
                     registroEntrada = gestionAnexosByAplicacionSIR(registroEntrada);
                     //Invocamos al plugin para distribuir el registro
                     Boolean distribuidoOK = plugin.enviarDestinatarios(registroEntrada, null, "", new Locale("ca"));

                     if (distribuidoOK) { //Si la distribución ha ido bien
                        //Eliminamos el elemento de la cola
                        colaEjb.remove(elementoADistribuir1);
                        log.info("distribucion OK REGISTRO " + registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir1.getIdObjeto());
                        //Tramitamos el registro de entrada
                        registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, registroEntrada.getUsuario());
                        //Añadimos la integración correcta.
                        integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_DISTRIBUCION,descripcion,peticion.toString(),System.currentTimeMillis() - tiempo,entidadId,registroEntrada.getNumeroRegistroFormateado());

                     } else { //No ha ido bien, el plugin nos dice que no ha ido bien
                        log.info( "Distribucion Error REGISTRO "+ registroEntrada.getNumeroRegistroFormateado() + "   IdObjeto: " + elementoADistribuir1.getIdObjeto());
                        try {
                           //Actualizamos los diferentes datos del elemento a distribuir(incremento de intentos, envio de mails, etc)
                           colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",null,administradores,maxReintentos);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                     }
                  } catch (Exception e) {
                     log.info("Primer Exception ");
                     try {
                        colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e, administradores,maxReintentos);
                     } catch (Exception ee) {
                        ee.printStackTrace();
                     }
                     e.printStackTrace();
                  } catch (I18NException e) {
                     log.info("Primer I18NException ");
                     try {
                        colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e, administradores,maxReintentos );
                     } catch (Exception ee) {
                        ee.printStackTrace();
                     }
                     e.printStackTrace();
                  } catch (I18NValidationException e) {
                     log.info("Primer I18NValidationException");
                     try {
                        colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e, administradores,maxReintentos);
                     } catch (Exception ee) {
                        ee.printStackTrace();
                     }
                     e.printStackTrace();
                  }catch(Throwable t){
                     log.info("Primer Throwable");
                     try {
                        colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",t ,administradores,maxReintentos);
                     } catch (Exception ee) {
                        ee.printStackTrace();
                     }
                     t.printStackTrace();
                  }
               }
            }
         }
      } catch (Exception e) {
         log.info("Error distribuyendo el registro Exception");
         try {
            colaEjb.actualizarElementoCola(elementoADistribuir1,descripcion,  peticion,tiempo,entidadId, hora,"ca",e,administradores,maxReintentos );
         } catch (Exception ee) {
            ee.printStackTrace();
         }
         e.printStackTrace();
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
