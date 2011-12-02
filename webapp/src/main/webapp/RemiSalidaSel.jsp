<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    String usuario=request.getRemoteUser();

    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    String numero="";
    String ano="";
%>
<%! 
    void escribeSelect(javax.servlet.jsp.JspWriter out, Vector valores, String referencia) throws java.io.IOException {
        
        for (int i=0;i<valores.size();i=i+2){
                String codigo=valores.get(i).toString();
                String descripcion=valores.get(i+1).toString();
                out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) ? "selected" : "")+">");
                out.write(codigo+"-"+descripcion);
                out.write("</option>\n");
        }
    }
%>

<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>
        
        
        
    </head>

    <body bgcolor="#FFFFFF">

    
  	<!-- Molla pa --> 
	<ul id="mollaPa">
	<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
	<li><fmt:message key='seleccionar_oficina_entrada'/></li>
	</ul>
	<!-- Fi Molla pa-->
		<br/>
		<br/>	
		<br/>
		<br/>
        <!-- Mostramos Errores si los hubiera -->
<% 
    if (request.getParameter("error")!=null) {
%>
        <table class="recuadroErrors" width="400" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/></p>
                    <ul>
                        <li><%=request.getParameter("error")%></li>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
        <p>&nbsp;</p><p>&nbsp;</p>
   <%  } %>
   
   <%-- Fin de errores --%>
   		<br/>
        <p>&nbsp;</p>
<!--   		<div id="RecuadreCentrat"> --> 
        <table  class="recuadroSalidasRegistro" align="center">
        <form name="busquedaForm" action="RemiSalidaLis.jsp" method="post">
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;<fmt:message key='oficina'/>:&nbsp;</td>
            <td><select name="oficina" id="oficina">
                    <option value="00">00 - <fmt:message key='registro.todas_autorizadas'/> </option>
              <% escribeSelect(out, valores.buscarOficinas(usuario,"AE"), "");%>
             </select>
            </td>
        </tr>
        <% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){ %>
        <tr>
           <td style="align:left" colspan="2">
                &nbsp;<fmt:message key='mostrarRegistrosConDocElectronicos'/>:<input type="checkbox" name="mostrarRegistrosConDocElectronicos" id="mostrarRegistrosConDocElectronicos" />
           </td>
        </tr>
        <%} %>
        <tr>
        <td style="align:center" colspan="2">
            <p align="center">
            <input type=submit value="<fmt:message key='cercar'/>">
            </p>
        </td>
        </tr>
        </form>
        </table>
<!--        </div> -->
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
<script type="text/javascript">
	 var elFocus = document.getElementById('oficina');
	 elFocus.focus();
</script>
		
                 
		
    </body>
</html>
