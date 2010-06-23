package es.caib.regweb.webapp.servlet;

import java.util.*;
import javax.naming.*;

import es.caib.regweb.logic.helper.ParametrosRegistroEntrada;
import es.caib.regweb.logic.interfaces.RegistroEntradaFacade;
import es.caib.regweb.logic.util.RegistroEntradaFacadeUtil;
import org.apache.log4j.Logger;

public class RegistroEntradaClientThread  extends Thread{
  private int idThread = 0;
  private Logger log = Logger.getLogger(this.getClass());
  
  public RegistroEntradaClientThread(int i){
    idThread = i;
  }
  
  private InitialContext getInitialContext() throws NamingException {
    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY, 
            "org.jnp.interfaces.NamingContextFactory");
    env.put(Context.SECURITY_PRINCIPAL, "rwedes");
    env.put(Context.SECURITY_CREDENTIALS, "rwedes");
    env.put(Context.PROVIDER_URL, "localhost:8080/regweb");

    return new InitialContext(env);
  }

  private RegistroEntradaFacade getRegistroEntradaFacade(){
    try{
      return RegistroEntradaFacadeUtil.getHome().create();
    }
    catch(Exception e){
      e.printStackTrace();
      return null;
    }
  }

  private String getMinuto(int i){
    String s = ""+i;
    if (s.length() == 1) s = "0" + s;
    
    return s;
  }
 
  public void run(){
    RegistroEntradaFacade regent = getRegistroEntradaFacade();
    ParametrosRegistroEntrada ren = new ParametrosRegistroEntrada();
    if (regent == null){
      log.error("No se puede obtener instancia de RegistroEntrada. Saliendo...");
      return;
    }

    try{
    	log.info("[Thread " + idThread + "]: Iniciant Registre d'entrada!");
      // Usuario que está accediendo
      ren.fijaUsuario("rwedes");

      // Fecha de la entrada.
      ren.setdataentrada("02/03/2006");
      
      // Hora de la entrada: Se concatena en el campo "minuto" el identificador del 
      // Thread
      ren.sethora("14:" + getMinuto(idThread%60) + ":12");

      // El usuario de pruebas U83541 tiene permisos para registrar entradas desde
      // las oficinas 11 y 12. Tablas BAGECOM y BZAUTOR / BZAUTOR01
      ren.setoficina("11");
      ren.setdata("15/06/2006");

      // El tipo de documento es una carta. (CA = "Carta". Tabla BZTDOCU01)
      ren.settipo("CA");

      // El idioma del documento es catalán. (2 = "Catalán". Tabla BZIDIOM)
      ren.setidioma("2");

      // Entidad remitente: Juzgado de instrucción nº 10. Tabla BZENTID
      ren.setentidad1("JINS");
      ren.setentidad2("10");

      // En esta prueba, el remitente es de balears: select * from bagruge 
      // where fabctagg = 90
      // En particular, usaremos Palma de Mallorca (40)
      ren.setbalears("40");

      // Destinatario del documento de entrada. Tabla BZOFIOR / BZOFIOR01
      // Filtramos por el idoficina = 11 para determinar a qué organismos se puede 
      // remitir.
      // Usamos 1110 (President)
      ren.setdestinatari("1110");

      // Indicamos el idioma del extracto (Catalán = CA)
      ren.setidioex("2");

      //Pasamos del nº de disquete de momento.
      //ren.setdisquet("3");
      

      // Extracto o comentario del registro explicando de qué se trata el doc.
      ren.setcomentario("Segona bateria de proves SMR. Registre de entrada. Thread: " + idThread + " timestamp: " + System.currentTimeMillis());


      // Validamos.
      if (!regent.validar(ren)){
        log.error("Error en la consistencia de los datos");
        Hashtable ht = ren.getErrores();
        log.error(ht.toString());
        return;
      }

      // Grabamos
      regent.grabar(ren);

      // Mostramos los errores si los hubo.
      Hashtable ht = ren.getErrores();
      if ((ht != null) && !ht.isEmpty()){
        log.error("Registro de entrada con errores:\n" + ht);
      }
      
      else log.info("[Thread " + idThread + "]: Registro de entrada efectuado con éxito!");

    }
    catch(Exception e){
      log.error("RegistroEntradaClientThread: Capturada Excepci\u00f3n! "+e.getMessage());
      e.printStackTrace();
      return;
    }
  }
}
