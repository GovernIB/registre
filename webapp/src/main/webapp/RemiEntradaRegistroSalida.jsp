<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<!--
  Registro General CAIB - Registro de Salidas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficina");
String numeroEntrada=request.getParameter("numeroEntrada");
String ano=request.getParameter("ano");
%>

<%
RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
ParametrosRegistroSalida pregsal = new ParametrosRegistroSalida();
ParametrosRegistroSalida registro = new ParametrosRegistroSalida();

RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
ParametrosRegistroEntrada preg = new ParametrosRegistroEntrada();
ParametrosRegistroEntrada reg = new ParametrosRegistroEntrada();

ValoresFacade valores = ValoresFacadeUtil.getHome().create();
OficioRemisionFacade ofi = OficioRemisionFacadeUtil.getHome().create();
LineaOficioRemisionFacade lin = LineaOficioRemisionFacadeUtil.getHome().create();

ParametrosOficioRemision param = new ParametrosOficioRemision();
ParametrosOficioRemision oficio = new ParametrosOficioRemision();
ParametrosLineaOficioRemision paraml = new ParametrosLineaOficioRemision();

Integer intSerie=(Integer)session.getAttribute("serie");
if (intSerie==null) {
    intSerie=new Integer(0);
    session.setAttribute("serie", intSerie);
}
int serie=intSerie.intValue();
int serieForm = Integer.parseInt(request.getParameter("serie"));

if (serie>serieForm) {
    session.setAttribute("errorAtras","1");

%>
    <jsp:forward page="pedirdatosSalida.jsp" />
       <% }

serie++;
// intSerie++;
intSerie=Integer.valueOf(String.valueOf(serie));
session.setAttribute("serie", intSerie);
session.removeAttribute("errorAtras");

Vector modelosOficios = valores.buscarModelos("tots","totes");

%>

<%

param.setOficinaOficio(request.getParameter("oficina"));
param.setFechaOficio(request.getParameter("datasalida"));

ofi.grabar(param);
oficio = ofi.leer(param);

registro.fijaUsuario(usuario);
registro.setCorreo(request.getParameter("correo"));
registro.setdatasalida(request.getParameter("datasalida"));
registro.sethora(request.getParameter("hora"));
registro.setoficina(request.getParameter("oficina"));
registro.setoficinafisica(request.getParameter("oficinafisica"));
registro.setdata(request.getParameter("data"));
registro.settipo(request.getParameter("tipo"));
registro.setidioma(request.getParameter("idioma"));
registro.setentidad1(request.getParameter("entidad1"));
registro.setentidad2(request.getParameter("entidad2"));
registro.setaltres(request.getParameter("altres"));
registro.setbalears(request.getParameter("balears"));
registro.setfora(request.getParameter("fora"));
registro.setentrada1(request.getParameter("entrada1"));
registro.setentrada2(request.getParameter("entrada2"));
registro.setremitent(request.getParameter("remitent"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
registro.setcomentario("Ofici de sortida de documentació amb núm. d'ofici " + oficio.getNumeroOficio() + "/" + oficio.getAnoOficio());
%>

<% boolean ok=regsal.validar(registro);
if (!ok){
    request.setAttribute("registroSalida",registro);
    
    request.setAttribute( "oficina", codOficina);
    request.setAttribute( "numeroEntrada", numeroEntrada);
    request.setAttribute( "ano", ano);


%>
        <jsp:forward page="RemiEntradaPaso.jsp" />
<% } else { 
    
    boolean grabado=regsal.grabar(registro);
    if (!grabado) {
        request.setAttribute("registroSalida",registro);

        
        request.setAttribute( "oficina", codOficina);
    	request.setAttribute( "numeroEntrada", numeroEntrada);
		request.setAttribute( "ano", ano);

%>
                <jsp:forward page="RemiEntradaPaso.jsp" />

<%            } else {
		registro = regsal.leer(registro);
		
		oficio.setAnoSalida(registro.getAnoSalida());
		oficio.setOficinaSalida(registro.getOficina());
		oficio.setNumeroSalida(registro.getNumeroSalida());
		oficio.setNulo("N");
		oficio.setMotivosNulo("");
		oficio.setUsuarioNulo("");
		oficio.setDescartadoEntrada(null);
		oficio.setMotivosDescarteEntrada(null);
		oficio.setUsuarioEntrada(null);
		oficio.setAnoEntrada(null);
		oficio.setOficinaEntrada(null);
		oficio.setNumeroEntrada(null);
		oficio.setFechaEntrada(null);
		
		String descripcion = "";
		String listareg = request.getParameter("registre");
		String [] registros = listareg.split("\\|");
		for (int i=0; registros!=null && i<registros.length;i++) {
			String [] campos = registros[i].split("-");
			
		    preg.fijaUsuario(usuario);
		    preg.setoficina(campos[0]);
		    preg.setNumeroEntrada(campos[1]);
		    preg.setAnoEntrada(campos[2]);
		    reg=regent.leer(preg);

		    descripcion += "("+ campos[0] + " - " + valores.recuperaDescripcionOficina(reg.getOficina().toString()).trim() + ") " + campos[1] + "/" + campos[2] + " - " + reg.getDescripcionRemitente().trim() + ";";
		  
		  paraml = new ParametrosLineaOficioRemision();
			paraml.setOficinaEntrada(campos[0]);
			paraml.setNumeroEntrada(campos[1]);
			paraml.setAnoEntrada(campos[2]);
			paraml.setDescartadoEntrada("");
			paraml.setMotivosDescarteEntrada("");
			paraml.setUsuarioEntrada(usuario);
			paraml.setAnoOficio(oficio.getAnoOficio());
			paraml.setOficinaOficio(oficio.getOficinaOficio());
			paraml.setNumeroOficio(oficio.getNumeroOficio());
			lin.grabar(paraml);
		    
		}
		oficio.setDescripcion(descripcion);
		ofi.actualizar(oficio);
		
        String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
        String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
        String bloqueoAno=(session.getAttribute("bloqueoAno")==null) ? "" : (String)session.getAttribute("bloqueoAno");
        
        if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
            valores.liberarDisquete(bloqueoOficina,bloqueoTipo,bloqueoAno,usuario);
            session.removeAttribute("bloqueoOficina");
            session.removeAttribute("bloqueoTipo");
            session.removeAttribute("bloqueoAno");
            session.removeAttribute("bloqueoUsuario");
            session.removeAttribute("bloqueoDisquete");
        }
%>

<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>
        
        <link rel="shortcut icon" href="favicon.ico"/>
        
        <script src="jscripts/TAO.js"></script>
        <script>
        function imprimeOficio() {
        	var url = "imprimeixOfici?";
            url += "oficina="+encodeURIComponent("<%=oficio.getOficinaOficio()%>");
        	url += "&oficio="+encodeURIComponent("<%=oficio.getNumeroOficio()%>");
        	url += "&any="+encodeURIComponent("<%=oficio.getAnoOficio()%>");
			url += '&mode=S&modelo='+encodeURIComponent(document.getElementById('model').value);
        	window.open(url, "oficio");  		
        }
        </script>
    </head>
    <body>
        
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="RemiEntradaSel.jsp"><fmt:message key='consulta_registres_pendents_remisio'/></a></li>
		<li><fmt:message key='registre_ofici_remisio'/> creat</li>
		</ul>
		<!-- Fi Molla pa-->
        <!-- <p>&nbsp;
        <center><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center>&nbsp;<p>
        -->
		<p>&nbsp;</p>
        <table class="recuadroSalidas" width="400" align="center">
            <tr>
                <td style="border:0" >
                    &nbsp;<br><center><b><fmt:message key='ofici'/> <%=oficio.getNumeroOficio()%>/<%=oficio.getAnoOficio()%> <fmt:message key='desat_correctament'/></B></center></p>
                </td>
            </tr>   
            <tr>
                <td style="border:0" >
                    &nbsp;<br><center><b><fmt:message key='registre_de_sortida'/> <%=oficio.getNumeroSalida()%>/<%=oficio.getAnoSalida()%> <fmt:message key='desat_correctament'/></B></center></p>
                </td>
            </tr>   
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p><center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p>
                    <center>
                      <% if (modelosOficios.size()>0) { %>
                    	<form name="ofici" id="ofici">
                    	<select name="model" id="model">
                    	<%     for (int i=0;i<modelosOficios.size();i++) {
                            String codiModel=modelosOficios.get(i).toString();%>
                            <option value="<%=codiModel %>"><%=codiModel %></option>
                        <% } %>

                    	</select>
                    	<a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="imprimeOficio();">
                        &nbsp;Imprimir ofici&nbsp;</a>
                        
                    	</form> 
                      <% } else { %>
                        <p align="center">
                        <fmt:message key="no_hi_ha_models_ofici"/>
                        </p>
                      <% } %>
                		
                        <br/>
                
                	    <a style="text-decoration: none;" type="button" class="botonFormulario" href="RemiEntradaLis.jsp">
                        &nbsp;<fmt:message key='tornar'/>&nbsp;</a>
                    </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
        </table>

        <%
}
}

        %>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
        
                 
    </body>
</html>