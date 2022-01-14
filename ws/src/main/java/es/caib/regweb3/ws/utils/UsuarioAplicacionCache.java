package es.caib.regweb3.ws.utils;



/**
 * 
 * @author anadal
 * 
 */
public class UsuarioAplicacionCache {


  private static final ThreadLocal<UsuarioInfo> appInfo = new ThreadLocal<UsuarioInfo>();

  public static final void put(UsuarioInfo appname) {
    appInfo.set(appname);
  }

  public static UsuarioInfo get() {
    return appInfo.get();
  }

  public static void remove() {
    appInfo.remove();
  }

}
