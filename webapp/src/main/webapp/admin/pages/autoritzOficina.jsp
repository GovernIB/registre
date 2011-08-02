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

	  function abreOficinas() {
	        miOficinas=open("<c:url value='/admin/popup.do?accion=totesOficines'/>","Oficines","scrollbars,resizable,width=400,height=400");
	        miOficinas.focus();
	      }
      
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

    function setOficina(codOficina) {
        var formulari = document.getElementById("cercaOficina");
        document.getElementById("oficinaGestionar").value=codOficina;
        formulari.submit();
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
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>" id='enlaceAdmin'><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="consulta_permisos_per_oficina"/></li>
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
          <div  id="menuDocAdm" style="width:300px">
            <form id="cercaOficina" name="cercaOficina" action="<c:url value='/admin/controller.do?accion=autoritzOficina'/>" method="post">
            <ul style="margin-right: 5px">
			    <li><p><fmt:message key="oficina_a_consultar_q"/> <a href="javascript:abreOficinas()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align="middle" alt="<fmt:message key='cercar'/>"></a></p></li>
            	<li>
            	  <input  onKeyPress="return goodchars(event,'0123456789')" style="width:20px;" type="text" name="oficinaGestionar" id="oficinaGestionar" size="2" maxlength="2"/>
            	  <input type="submit" value="&nbsp;<fmt:message key='cercar'/>&nbsp;">
			     </li>
			     <li><input type="checkbox" name=verPermisosConsulta id="verPermisosConsulta" /> - <fmt:message key='mostrar_usuarios_solo_consulta'/></li>
	      	  </ul>
            </form>
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
	<p><fmt:message key='usuarios_de_la_oficina'/> <b><c:out escapeXml="false" value='${oficinaConsultar}'/></b></p>

	<c:if test="${not tieneAutorizaciones}">
		<center><b><fmt:message key='usuari_no_aut'/></b></center>	
	</c:if>
	<br/>

<table>
<tr>
	<th><fmt:message key='usuari'/></th>
	<th><fmt:message key='alta_entrades'/></th>
	<th><fmt:message key='consulta_entrades'/></th>
	<th><fmt:message key='alta_sortides'/></th>
	<th><fmt:message key='consulta_sortides'/></th>
	<th><fmt:message key='visat_entrades'/></th>
	<th><fmt:message key='visat_sortides'/></th>
</tr>
	<c:out escapeXml="false" value='${ofiInput}'/>

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