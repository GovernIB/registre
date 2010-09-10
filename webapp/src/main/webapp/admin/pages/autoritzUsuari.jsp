<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
        var listaExtractos=new Array();
        var i=0;
        var copiado=false;

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
		
    function abreError() {
      miError=open("<c:url value='/error.jsp'/>","Error","scrollbars,resizable,width=500,height=380");
      miError.focus();
    }

    function getdescMissatge() {
      return '<c:out escapeXml="false" value="${descMissatge}"/>';
    }

    function getmesInfoMissatge() {
      return '<c:out escapeXml="false" value="${mesInfoMissatge}"/>';
    }

    <c:if test="${not empty missatge}"> 
    addLoadEvent(function() {
      abreError();;
    });				
    </c:if>
    </script>
</head>
<body>

     	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="autoritzacio_usuaris"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>

    <c:choose>
    <c:when test="${not hayUsuario}">
<br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>  
        <table align="center" >
            <tr>
            <td>
            	 <div  id="menuDocAdm" style="width:250px">
            	 <ul style="margin-right: 5px">
					<li><p><fmt:message key='usuari_a_autoritzar_q'/></p></li>
                	<li>	<form id="cercaUsuari" name="cercaUsuari" action="<c:url value='/admin/controller.do?accion=autoritzUsuari'/>" method="post">
							<input style="width:70px;" type="text" name="usuariAutoritzar" id="usuariAutoritzar" size="9" maxlength="9"/>
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


	<div class="RecuadreCentrat" style="width: 600px;">
	<p><fmt:message key='usuari_a_autoritzar'/> <b><c:out escapeXml="false" value='${usuariAutoritzar}'/></b></p>

	<c:if test="${not tieneAutorizaciones}">
		<center><b><fmt:message key='usuari_no_aut'/></b></center>	
	</c:if>
	<br/>
<form name="altaAutorUsu" id="altaAutorUsu" action="<c:url value='/UtilAdmin'/>" method="post">	
<input type="hidden" name="accion" id="accion" value="altaAutUsu"/>
<input type="hidden" name="usuariAut" id="usuariAut" value="<c:out escapeXml="false" value='${usuariAutoritzar}'/>"/>
<table>
<tr>
	<th><fmt:message key='oficina'/></th>
	<th><fmt:message key='alta_entrades'/></th>
	<th><fmt:message key='consulta_entrades'/></th>
	<th><fmt:message key='alta_sortides'/></th>
	<th><fmt:message key='consulta_sortides'/></th>
	<th><fmt:message key='visat_entrades'/></th>
	<th><fmt:message key='visat_sortides'/></th>
</tr>
<tr>
	<td></td>
	<td><input type="checkbox" name="AEtots" id="AEtots" value="AE" style="width: 60px;" onclick="MarcaTot(event);"/></td>
	<td><input type="checkbox" name="CEtots" id="CEtots" value="CE" style="width: 60px;" onclick="MarcaTot(event);"/></td>
	<td><input type="checkbox" name="AStots" id="AStots" value="AS" style="width: 60px;" onclick="MarcaTot(event);"/></td>
	<td><input type="checkbox" name="CStots" id="CStots" value="CS" style="width: 60px;" onclick="MarcaTot(event);"/></td>
	<td><input type="checkbox" name="VEtots" id="VEtots" value="VE" style="width: 60px;" onclick="MarcaTot(event);"/></td>
	<td><input type="checkbox" name="VStots" id="VStots" value="VS" style="width: 60px;" onclick="MarcaTot(event);"/></td>
</tr>
	<c:out escapeXml="false" value='${ofiInput}'/>
<tr>
	<td align="center" colspan="6"><input type="submit" value="<fmt:message key='desa'/>"/></td>
</tr>

</form>
</table>	

</div>

</c:otherwise>
</c:choose>

<script type="text/javascript">
	 var elFocus = document.getElementById('<c:out escapeXml="false" value='${elementFocus}'/>');
	 if (elFocus!=null) elFocus.focus();
</script>

	</body>
</html>   