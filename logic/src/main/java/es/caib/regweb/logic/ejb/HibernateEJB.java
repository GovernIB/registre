package es.caib.regweb.logic.ejb;

import java.net.URL;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.apache.log4j.Logger;

/**
 * Bean con la funcionalidad básica para interactuar con HIBERNATE.
 *
 * @ejb.bean view-type="remote"
 * generate="false"
 * @ejb.resource-ref res-ref-name="hibernate/SessionFactory"
 * res-type="org.hibernate.SessionFactory"
 * res-auth="Container"
 * @jboss.resource-ref res-ref-name="hibernate/SessionFactory"
 * jndi-name="es.caib.regweb.hibernate.SessionFactory"
 * @ejb.resource-ref res-ref-name="jdbc/RegwebDS"
 *     res-type="javax.sql.DataSource"
 *     res-auth="Container"
 * @jboss.resource-ref res-ref-name="jdbc/RegwebDS"
 *     jndi-name="es.caib.regweb.db"
 * @ejb.permission role-name="RWE_USUARI"
 * @ejb.permission role-name="RWE_ADMIN"
 * @ejb.permission role-name="RWE_LOPD"
 */

public abstract class HibernateEJB implements SessionBean {

	private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(this.getClass());

    protected SessionFactory sf = null;
    protected SessionContext ctx = null;

   public void setSessionContext(SessionContext ctx) {
      this.ctx = ctx;
   }

   private static SessionFactory initSessionFactory() throws EJBException {
      System.setProperty(Environment.USE_STREAMS_FOR_BINARY, "true");
      try {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         URL url = cl.getResource("hibernate.cfg.xml");
         AnnotationConfiguration cfg = new AnnotationConfiguration().configure(url);
         return cfg.buildSessionFactory();
      } catch (HibernateException e) {
         throw new EJBException(e);
      }
   }

   public void ejbCreate() throws CreateException {
      log.debug("ejbCreate: " + this.getClass());
      try {
         Context ctx = new InitialContext();
         sf = (SessionFactory) ctx.lookup("java:comp/env/hibernate/SessionFactory");
         ctx.close();
         if (sf == null) {
            log.warn("Obtener SessionFactory por JNDI ha devuelto null.");
            sf = initSessionFactory();
         }
      } catch (NamingException e) {
         log.error("Error buscando SessionFactory en JNDI, inicializando ...");
         sf = initSessionFactory();
      }

   }

   public void ejbRemove() {
      log.debug("ejbRemove: " + this.getClass());
      sf = null;
   }

   protected Session getSession() {
      try {
    	  log.debug("GetSession");
          return sf.openSession();
      } catch (HibernateException e) {
         throw new EJBException(e);
      }
   }

   protected void close(Session sessio) {
	   log.debug("Close Session");
      if (sessio != null && sessio.isOpen()) {
         try {
            if (sessio.isDirty()) {
               log.warn("Se ha cerrado la sessi\u00f3n sin hacer un flush.");
               sessio.flush();
            }
            sessio.close();
         } catch (HibernateException e) {
            throw new EJBException(e);
         }
      }
   }

}
