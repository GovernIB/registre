

/*
 * Created on 1-des-2009
 *
 */
package es.caib.regweb.webapp.servlet;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.caib.regweb.webapp.servlet.UtilWebServlet;
import es.caib.regweb.logic.interfaces.AdminFacade;
import es.caib.regweb.logic.interfaces.RegistroEntradaFacade;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.interfaces.HistoricoEmailsFacade;
import es.caib.regweb.logic.util.AdminFacadeUtil;
import es.caib.regweb.logic.util.RegistroEntradaFacadeUtil;
import es.caib.regweb.logic.util.ValoresFacadeUtil;
import es.caib.regweb.logic.util.HistoricoEmailsFacadeUtil;
import es.caib.regweb.logic.helper.ModeloDocumentoData;
import es.caib.regweb.logic.helper.ParametrosRegistroEntrada;
import es.caib.regweb.logic.helper.ParametrosHistoricoEmails;
import es.caib.regweb.logic.helper.Mail;

/**
 * Servlet Class
 *
 */
public class EmailServlet extends UtilWebServlet {


	private static Logger log = null;
	
	/**
	 * Constante para indicar que el correo va dirigido al ciudadano
	 */
	public static final String TIPUS_CIUTADA = "EX";
	/**
	 * Constante para indicar que el correo va dirigido a una unidad de gestión
	 */
	public static final String TIPUS_INTERN = "IN";
	/**
	 * Constante para indicar que el idioma del correo es catalan
	 */
	public static final String IDIOMA_CATALAN = "CT";
	/**
	 * Constante para indicar que el idioma del correo es castellano
	 */
	public static final String IDIOMA_CASTELLANO = "ES";
	/**
	 * Constante para indicar que el idioma del correo no esta determinado
	 */
	public static final String IDIOMA_NO_DETERMINADO = "NO";
	
	public EmailServlet() {
		super();
	}
	 
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean envioCorrecto = false;
		ServletContext context = this.getServletConfig().getServletContext();
		StringBuilder param = new StringBuilder("/popup/rtdoEnviament.jsp");
		String dir_destinatario = null;
		String contenido = null;
		String tipus = ((request.getParameter("tipus")!=null)?request.getParameter("tipus"):"BUIT");
		String idioma = ((request.getParameter("idioma")!=null)?request.getParameter("idioma"):IDIOMA_CATALAN);
		String asunto = "";
		AdminFacade autUsu = null;
		ModeloDocumentoData modeloEmail = null;	
		
		//Leemos el registro de base de datos
		ParametrosRegistroEntrada registro =leerRegistro(request);
		registro.fijaUsuario(request.getRemoteUser());
		
		try{
			//Comprobamos que ha sido leido correctamente
			if(registro.getLeido()){
				//Buscamos el destinatario
				if(tipus.equalsIgnoreCase(TIPUS_CIUTADA)){
					dir_destinatario = registro.getEmailRemitent();
				}else{
					dir_destinatario = request.getParameter("email");
				}

				if(dir_destinatario!=null){

					autUsu = AdminFacadeUtil.getHome().create();
					modeloEmail = autUsu.getModelEmail( idioma,tipus );
					asunto = substituirVariables( registro,  modeloEmail.getTitulo() );
					contenido = substituirVariables( registro, modeloEmail.getCuerpo());
					envioCorrecto = Mail.enviarCorreu(dir_destinatario,asunto,contenido);
				}else{ //Fin if(dir_destinatario!=null)
					log.error("No se puede enviar un correo interno al no poder leer el parámetro de entrada 'email'. Registro:"+registro.getReferenciaRegistro());
				}
			}// Fin if(registro.getLeido()){
		}catch(Exception ex){
			log.error("No se puede enviar un correo interno. Registro:"+registro.getReferenciaRegistro()+". Se ha generado una Excepción", ex);
		}
		if(envioCorrecto){
			param.append("?correcto=true");
			guardarHistorico( registro,  dir_destinatario,  tipus);
		}else{
			param.append("?correcto=false");
		}
		param.append("&email="+registro.getEmailRemitent()+"&oficina="+registro.getOficina()+"&numero="+registro.getNumeroEntrada()+"&ano="+registro.getAnoEntrada()+"&tipus="+tipus);

		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String url = response.encodeURL(param.toString());
		context.getRequestDispatcher(url).forward(request, response);
	}
	
	/**
	 * Método para grabar los datos del email enviado en el registro histórico de emails de la aplicación
	 * 
	 * @param registro
	 * @param dir_destinatario
	 * @param tipus
	 * @throws ServletException
	 */
	private void guardarHistorico(ParametrosRegistroEntrada registro, String dir_destinatario, String tipus)throws ServletException{
		HistoricoEmailsFacade histEmails = null;
		ParametrosHistoricoEmails param = new ParametrosHistoricoEmails();
		ValoresFacade valores;
		
		try{
			 valores = ValoresFacadeUtil.getHome().create();
		}catch(Exception ex){
			throw new ServletException("Error al buscar el EJB ValoresFacade.",ex);
		}
		
		try{
			param.setAnoRegistro(Integer.parseInt(registro.getAnoEntrada()));
			param.setCodigoOficina(Integer.parseInt(registro.getOficina()));
			param.setCodigoUsuario(registro.getUsuario());
			param.setEmailDestinatario(dir_destinatario);
			param.setFecha(valores.getFecha());
			param.setHora(valores.getHorasMinutosSegundos());
			param.setNumeroRegistro(Integer.parseInt(registro.getNumeroEntrada()));
			param.setTipoEmail((tipus.equalsIgnoreCase(TIPUS_CIUTADA)?"C":"I"));
			param.setTipoRegistro("E");
			// Este campo no hay que rellenarlo
			//param.setNumeroEmail(numeroEmail);
		}catch(Exception ex){
			throw new ServletException("Error al convertir parametros.",ex);
		}
		
		try{
			//
			histEmails = HistoricoEmailsFacadeUtil.getHome().create();
			histEmails.grabar(param);
			}catch(Exception ex){
				throw new ServletException("Error al cridar al EJB HistoricoEmailsFacade.",ex);
		}
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		log = Logger.getLogger(this.getClass());
	}
	
	/**
	 * Busca el registro de entrada que le indican los parametros de la llamada, oficina, numero y ano. 
	 * 
	 * @param request
	 * @return Devuelve los datos del registro o indica en el objeto que no ha podido ser leido
	 */
	private ParametrosRegistroEntrada leerRegistro(HttpServletRequest request) {
		ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();
		
		String codOficina = request.getParameter("oficina");
		String numeroEntrada = request.getParameter("numero");
		String ano = request.getParameter("ano");
		String usuario=request.getRemoteUser();
		
		try {
			RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
			registro.fijaUsuario(usuario);
			registro.setoficina(codOficina);
			registro.setNumeroEntrada(numeroEntrada);
			registro.setAnoEntrada(ano);
		    registro = regent.leer(registro);
		} catch (CreateException e) {
			registro.setLeido(false);
		} catch (NamingException e) {
			registro.setLeido(false);
		} catch (RemoteException e) {
			registro.setLeido(false);
		}
		
		return registro;
	}

	/**
	 * Genera el asunto que deberá llevar el correo electrónico a enviar
	 * 
	 * @param registro Objeto con los datos del registro
	 * @param modeloAsunto Modelo de como debe ser el asunto
	 * @return
	 */
	private String substituirVariables(ParametrosRegistroEntrada registro, String modelo){
		String asunto = modelo;
		String localizadoresStr = "";
		
		String localitzadorsDocs[] = registro.getArrayLocalitzadorsDocs();
		localizadoresStr = "<ul>";
        for(int i=0; i<localitzadorsDocs.length; i++){ 
        	localizadoresStr+=("<li><a href="+localitzadorsDocs[i]+" target='_blank'>"+localitzadorsDocs[i]+"</a></li>");
        } 
        localizadoresStr+="</ul>";

        String nomEntitat = System.getProperty("entitat.correu.nom");
        if (nomEntitat == null) {
           nomEntitat = "El Govern de les Illes Balears";
        }
        asunto=asunto.replace("$(NomEnt)",  nomEntitat);
		asunto=asunto.replace("$(AnoReg)",   registro.getAnoEntrada());
		asunto=asunto.replace("$(DataReg)",  registro.getDataEntrada());
		asunto=asunto.replace("$(DesOfi)",   registro.getDescripcionOficina());
		asunto=asunto.replace("$(DesRem)",   registro.getDescripcionRemitente());
		asunto=asunto.replace("$(ExtReg)",   registro.getComentario());
		asunto=asunto.replace("$(LliDoc)",   localizadoresStr);
		asunto=asunto.replace("$(NumOfi)",   registro.getOficina());
		asunto=asunto.replace("$(NumOfiFi)", registro.getOficinafisica());
		asunto=asunto.replace("$(NumReg)",   registro.getNumeroEntrada());
		asunto=asunto.replace("$(RefReg)",   registro.getReferenciaRegistro());

		return asunto;
	}

}
