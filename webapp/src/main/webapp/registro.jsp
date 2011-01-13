<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!--
  Registro General CAIB - Registro de Entradas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%

RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();

session.setAttribute("oficinaSesion", request.getParameter("oficina"));
session.setAttribute("oficinaFisicaSesion", request.getParameter("oficinafisica"));

ValoresFacade valores = ValoresFacadeUtil.getHome().create();
String usuario=request.getRemoteUser();

Vector modelosRecibos = valores.buscarModelosRecibos("tots","totes");

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
    <jsp:forward page="pedirdatos.jsp" />
       <% }
    
    serie++;
    // intSerie++;
    intSerie=Integer.valueOf(String.valueOf(serie));
    session.setAttribute("serie", intSerie);
    session.removeAttribute("errorAtras");
%>

<%
registro.fijaUsuario(usuario);
registro.setCorreo(request.getParameter("correo"));
registro.setdataentrada(request.getParameter("dataentrada"));
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
registro.setsalida1(request.getParameter("salida1"));
registro.setsalida2(request.getParameter("salida2"));
registro.setdestinatari(request.getParameter("destinatari"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
if(request.getParameter("mun_060")!=null)
	registro.setMunicipi060(request.getParameter("mun_060"));

registro.setcomentario(request.getParameter("comentario"));
%>

<%

     registro=regent.validar(registro);
     boolean ok=registro.getValidado();


if (!ok){
    request.setAttribute("registroEntrada",registro);

%>
        <jsp:forward page="pedirdatos.jsp" />
<% } else {
    
    


    registro=regent.grabar(registro);

    boolean grabado=registro.getGrabado();
    
    if (!grabado) {
        request.setAttribute("registroEntrada",registro);

%>
                <jsp:forward page="pedirdatos.jsp" />

<%            } else {
        registro = regent.leer(registro);
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
    <head><title><fmt:message key='registre_entrades'/></title>
        
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Cache-Control" content="no-cache">
        <script src="jscripts/TAO.js"></script>
        <script>
        function imprimeRecibo() {
        	var url = "imprimeixRebut?oficina="+encodeURIComponent("<%=registro.getOficina().toString()%>");
        	url += "&numeroentrada="+encodeURIComponent("<%=registro.getNumeroEntrada()%>")+"&anoentrada="+encodeURIComponent("<%=registro.getAnoEntrada()%>");
        	url += '&mode=N&modelo='+encodeURIComponent(document.getElementById('model').value);
        	window.open(url, "recibo");  		
        }
        </script>
    
    </head>
    <body>
        
     	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="pedirdatos.jsp"><fmt:message key='registre_entrades'/></a></li>
		<li><fmt:message key='registre_entrada_creat'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
        <table class="recuadroEntradas" width="400" align="center">
            <tr>
                <td style="border:0" >
                    &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro.getNumeroEntrada()%>/<%=registro.getAnoEntrada()%> <fmt:message key='desat_correctament'/></b></center></p>
                </td>
            </tr>   
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p><center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center></p>
                    <p><center><b><fmt:message key='oficina_fisica'/>:&nbsp;<%=registro.getOficinafisica()%>-<%=valores.recuperaDescripcionOficinaFisica(registro.getOficina().toString(),registro.getOficinafisica().toString())%></b></center></p>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p><center>
                    
                    <% if (modelosRecibos.size()>0) { %>
                    <form name="rebut" id="rebut">
                    	<select name="model" id="model">
                    	<%     for (int i=0;i<modelosRecibos.size();i++) {
                            String codiModel=modelosRecibos.get(i).toString();%>
                            <option value="<%=codiModel %>"><%=codiModel %></option>
                        <% } %>

                    	</select>
                    	<a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="imprimeRecibo();">
                        &nbsp;<fmt:message key='imprimir_rebut'/>&nbsp;</a>
                        
                    	</form> 
                		<% }  else { %>
                      <p align="center">
                		  <fmt:message key="no_hi_ha_models_rebut"/>
                		  </p>
                		<% } %>
                        <br/>
                    
                    <a style="text-decoration: none;" type="button" class="botonFormulario" href="pedirdatos.jsp">
                        &nbsp;<fmt:message key='registro.nuevo_registro'/>&nbsp;
                    </a>
                    </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
        </table>
        <!-- Nueva tabla -->
       <br/>
       <br/>
        <table align="center">
            <tr>
                <td align="center"> 
                   <iframe name="pdf1" HEIGHT="87" WIDTH="63" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <iframe height="87" width="63" name="pdf2" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <iframe height="87" width="63" name="pdf3" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                    <iframe height="87" width="63" name="pdf4" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
            </tr>
            <tr>
                <td align="center"> 
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_1'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_2'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_3'/></a>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_4'/></a>
                </td>
            </tr>
            <tr>
                <td> 
                    <iframe name="pdf5" HEIGHT="70" WIDTH="87" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=5&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>
                    <iframe height="70" width="87" name="pdf6" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=6&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>
                    <iframe height="70" width="87" name="pdf7" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=7&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>                       
                    <iframe height="70" width="87" name="pdf8" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=8&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
            </tr>
           <tr>
                <td align="center"> 
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=5&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_5'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                     <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=6&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_6'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                     <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=7&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_7'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                     <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=8&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_8'/></a> 
                </td>
            </tr>
            <%-- Imagenes --%>
        </table>
        <!-- Fin de la nueva tabla -->
        <%
}
}


        %>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
        
                 
    </body>
</html>