<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    String usuario=request.getRemoteUser();
    
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    boolean autorizadoVisadoEntradas=valores.usuarioAutorizadoVisar(usuario,"AES60T01");
    boolean autorizadoVisadoSalidas=valores.usuarioAutorizadoVisar(usuario,"AES65T01");

//	response.setHeader("Cache-Control", "store");
//    response.setHeader("Pragma", "cache");
         %>

<html>
    <head>
        <title><fmt:message key='registre_entrades'/> / <fmt:message key='sortides'/></title>
        
        
        <meta http-equiv="Cache-Control" content="store">
        <meta http-equiv="Pragma" content="cache"> 
        <link rel="shortcut icon" href="favicon.ico"/>
        <script>
            function abre() {
            //ventana=window.open("http://suport.caib.es/sacmicrofront/contenido.do?mkey=M207&lang=CA&cont=9853");
            ventana=window.open("<fmt:message key='urlPinfos'/>");
            ventana.moveTo(0,0);
            ventana.focus();
            }
        </script>

    </head>
    <body bgcolor="#FFFFFF" text="#000000">
    
   	<!-- Molla pa --> 
	<ul id="mollaPa">
	<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
	<li><fmt:message key='documentacio'/></li>
	</ul>
	<!-- Fi Molla pa-->
	<br/>
 <!--       <center><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center>&nbsp;<p> -->
        <br/>
        <table align="center">
            <%-- Fila para los literales --%>
            <tr>
                <td align="center">
                    <font class="titulo">
                        <b><fmt:message key='documentacio'/></b>
                    </font>
                </td>
            </tr>
            <tr>
                <td>
                    <div align="center" id="menuDocAdm" style="width:250px">
                        <ul>
                                <li><a href="davallafitxer?nomfitxer=manualusuari" target="_blank"><font class="menu"><fmt:message key='manual_usuari'/></font></a></li>
                                <li><a href="davallafitxer?nomfitxer=requisits" target="_blank"><font class="menu"><fmt:message key='requisits_tecnologics'/></font></a></li>
                                <c:if test="${initParam['registro.solicituds.estat']}">
								<li><a href="javascript:abre()"><font class="menu"><fmt:message key='sollicituds_dautoritzacio_nous_usuaris'/></font></a></li>
								</c:if>
                    	</ul>
					</div>
            </td>
            </tr>
        </table>
		<p>&nbsp;</p>
    </body>
</html>
