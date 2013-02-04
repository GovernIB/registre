package es.caib.regweb.ws;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

public class EjbUtil
{
	
	//private final static String JNP_PROTOCOL = "jnp://";
	private final static String HTTP_PROTOCOL = "http";	
	private final static String JNP_INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
	private final static String HTTP_INITIAL_CONTEXT_FACTORY = "org.jboss.naming.HttpNamingContextFactory";
	private final static String URL_PKG_PREFIXES = "org.jboss.naming:org.jnp.interfaces";
		
    public static InitialContext getInitialContext(boolean local,String url) throws Exception{
    	Properties environment = null;
		javax.naming.InitialContext initialContext = null;
		if (local){						
			initialContext = new javax.naming.InitialContext(environment);
			return initialContext;
		}		
		environment = new Properties();
		environment.put( Context.PROVIDER_URL,  url);
		environment.put( Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory( url ));
		environment.put (Context.URL_PKG_PREFIXES,  URL_PKG_PREFIXES );
		initialContext = new javax.naming.InitialContext(environment);
		return initialContext;    
    }
                        
	
    public static Object lookupHome( String jndi,boolean local,String url, Class narrowTo) throws Exception {
	      // Obtain initial context
	      InitialContext initialContext = getInitialContext(local,url);
	      try {
	         Object objRef = initialContext.lookup(jndi);
	         // only narrow if necessary
	         if (narrowTo.isInstance(java.rmi.Remote.class))
	            return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
	         else
	            return objRef;
	      } finally {
	         initialContext.close();
	      }
	   }
	
	private static String getInitialContextFactory( String url )
	{
		return url.startsWith( HTTP_PROTOCOL ) ? HTTP_INITIAL_CONTEXT_FACTORY : JNP_INITIAL_CONTEXT_FACTORY;
	}
    
    
}
