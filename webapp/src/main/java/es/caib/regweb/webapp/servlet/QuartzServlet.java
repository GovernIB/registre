/**
 * 
 */
package es.caib.regweb.webapp.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.jobs.ee.ejb.EJBInvokerJob;

/**
 * @author u83511
 *
 */

public class QuartzServlet extends GenericServlet {
	
	private Logger log = null;
	
	public void init(ServletConfig config) throws ServletException {
		String _fullDeployedEventEjbName="";
		log = Logger.getLogger(this.getClass());
		super.init(config);
		log.debug("Programant el job nocturn que esborra els registres antics del log de la LOPD..");

		JobDetail jd = new JobDetail("Test Quartz","My Test Job",EJBInvokerJob.class);
		jd.getJobDataMap().put("ejb", "es.caib.regweb.logic.EsborraRegAnticsLopdFacade");
		jd.getJobDataMap().put("method", "esborraDadesAntiguesLOPD");
		Object[] jdArgs = new Object[0];
		jd.getJobDataMap().put("args", jdArgs);
		CronTrigger cronTrigger = new CronTrigger("Test Quartz", "Test Quartz");
		
		try {
			String cronExpr = null;
//			Get the cron Expression as an Init parameter
			cronExpr = getInitParameter("cronExpr");
			log.debug(cronExpr);
			cronTrigger.setCronExpression(cronExpr);
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			sched.scheduleJob(jd, cronTrigger);
			log.debug("Job -agendat-..");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void service(ServletRequest arg0, ServletResponse arg1)
	throws ServletException, IOException {
	}
	public String getServletInfo() {
		return null;
	}
}

