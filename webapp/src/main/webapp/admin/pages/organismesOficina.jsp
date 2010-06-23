<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<% 

%>

<html>
<head>
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
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

		function MarcaTot(evnt) {
			/* Marcam tots els checkboxs de la columna */
			var valor = false;

			var elementClickat = null;
			if (window.event) {
				elementClickat = window.event.srcElement;
			} else {
				elementClickat = evnt.target;
			}
			if ( elementClickat.checked == true) {
				valor=true;
			} else {
				valor=false;
			}
			//alert('Valor='+valor + ' elementClickat.name='+elementClickat.name+ ' elementClickat.value='+elementClickat.value);
			var llista = document.getElementById("altaAutorUsu").getElementsByTagName(elementClickat.value);
			var llistaElementsFormAU = document.getElementById("altaAutorUsu").elements;

			for (var i=0; i<llistaElementsFormAU.length ;i++) {
				//window.alert('Element '+i+' '+llistaElementsFormAU[i].name); 
				if ( llistaElementsFormAU[i].name == elementClickat.value ){
					llistaElementsFormAU[i].checked=valor;
				}
			}
		}
		
		function abreOficinas() {
            	miOficinas=open("<c:url value='/admin/popup.do?accion=totesOficines'/>","Oficines","scrollbars,resizable,width=400,height=400");
            	miOficinas.focus();
        }
        
        function setOficina(codOficina) {
            	var formulari = document.getElementById("cercaOficina");
            	document.getElementById("oficinaGestionar").value=codOficina;
            	formulari.submit();
        }
    </script>
</head>
<body>

     	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="assignacio_organismes"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>


    <c:choose>
    <c:when test="${not hayOficina}">
    
<br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>  
        <table align="center" >
            <tr>
            <td>
            	 <div  id="menuDocAdm" style="width:250px">
            	 <ul style="margin-right: 5px">
					<li><p><fmt:message key='oficina_a_gestionar_q'/><a href="javascript:abreOficinas()"><img border="0" src="/regweb/imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>"></a></p></li>
                	<li>	<form id="cercaOficina" name="cercaOficina" action="<c:url value='/admin/controller.do?accion=organismesOficina'/>" method="post">
								<input onKeyPress="return goodchars(event,'0123456789')" style="width:20px;" type="text" name="oficinaGestionar" id="oficinaGestionar" size="2" maxlength="2"/>
								<input type="submit" value="<fmt:message key='cercar'/>">
							</form>
					</li>
            	</ul>
            	</div>
            	</td>
			</tr>			
        </table>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>


  </c:when>
  <c:otherwise>

	<div class="RecuadreCentrat" style="width:500px;">
	<p><fmt:message key='oficina_a_gestionar'/><b><c:out value="${oficines[0]}"/> - <c:out value="${oficines[1]}"/></b></p>
	<br/>
<form name="altaOrgsOfi" id="altaOrgsOfi" action="/regweb/UtilAdmin" method="post">	
<input type="hidden" name="accion" id="accion" value="altaOrganismesOficina"/>
<input type="hidden" name="oficinaGestionar" id="usuariAut" value="<c:out escapeXml="true" value='${oficinaGestionar}'/>"/>
<table>
<tr>
	<th><fmt:message key='assignar'/></th>
	<th><fmt:message key='no_remetre'/></th>
	<th><fmt:message key='organisme'/></th>
</tr>
<tr>
	<td></td>
	<td></td>
	<td></td>
</tr>
	<c:out escapeXml="false" value='${ofiInput}'/>
<tr>
	<td align="center" colspan="2"><input type="submit" value="<fmt:message key='desa'/>"/></td>
</tr>

</form>
</table>	

</div>

</c:otherwise>
</c:choose>

<script type="text/javascript">
	 var elFocus = document.getElementById('<c:out escapeXml="true" value='${elementFocus}'/>');
	 if (elFocus!=null) elFocus.focus();
</script>

	</body>
</html>   