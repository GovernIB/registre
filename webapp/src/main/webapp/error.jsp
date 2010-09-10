<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
%>

<html>
<head><title><fmt:message key='error'/></title>
	
	<script>
function addLoadEvent(func) {
    	var oldonload = window.onload;
	    if (typeof window.onload != 'function') {
	        window.onload = func;
	    } else {
	        window.onload = function() {
	            if (oldonload) {
	                oldonload();
	            }
	            func();
	        }
	    }
	}

	addLoadEvent(function() {
 	   document.getElementById("descError").innerHTML=top.opener.getdescMissatge();
	});

	function mostraMesInfoError() {
		document.getElementById('mesInfoError').innerHTML=top.opener.getmesInfoMissatge();
	}

	</script>
	
	
</head>
<body>
<p></p>
 <table class="recuadroErrors" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='error.sha_produit_error'/></p>
                    <ul>
                        <li><fmt:message key='error.descripcio'/> <p id="descError"></p> </li>
                        <li><a href="javascript:mostraMesInfoError();"><fmt:message key='error.veure_info_adicional'/></a>
                        <p id="mesInfoError"></p></li>
                    </ul>
                </td>
            </tr>
        </table>
<p></p>
<table align="center">
		<tr><td>		<a href="javascript:window.close();" ><fmt:message key='tancar'/></a> </td></tr>
</table>
<p></p>
 		

</body>
</html>
