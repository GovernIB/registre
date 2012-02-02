<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%! private Logger log = Logger.getLogger(this.getClass());%>

<html>
<head>
<script src="<c:url value='/jscripts/TAO.js'/>"></script>
<script language='javascript'>
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

		function abreUnidades() {
           	unidades=open("<c:url value='/admin/popup.do?accion=totesUnitatsDeGestio'/>","Unitat_de_gestio","scrollbars,resizable,width=400,height=400");
           	unidades.focus();
        }  
        function setUnitat(codOficina,codUnitat) {
            	var formulari = document.getElementById("cercaUnitatForm");
            	document.getElementById("codiOficina").value=codOficina;
            	document.getElementById("codiUnitat").value=codUnitat;
            	formulari.submit();
        }

        function validaGestioUnitat() {
            	if (document.getElementById("nomUnitat").value=="" || document.getElementById("email").value=="" ) {
            		alert("<fmt:message key='error_desc_data_alta_oficina'/>");
            		return false;
            	} else {
            		return true;
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
          abreError()
        });				
        </c:if>
    </script>
</head>
<body>

     	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="gestio_unitat_de_gestio"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
    <c:choose>
	    <c:when test="${not mostrarDatosUnidad}">
			<br/>
			<p>&nbsp;</p>
			<p>&nbsp;</p>  
	        <table align="center" >
	            <tr>
	            <td>
	            	 <div  id="menuDocAdm" style="width:250px">
	            	 <ul style="margin-right: 5px">
						<li>
						<p><fmt:message key='unitat_de_gestio_a_gestionar'/> 
						  <a href="javascript:abreUnidades()">
						    <img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align="middle" alt="<fmt:message key='cercar'/>">
						  </a>
						</p>				
						</li>
	                	<li>	<form id="cercaUnitatForm" name="cercaUnitatForm" action="<c:url value='/admin/controller.do?accion=unitatsDeGestio'/>" method="post">
									<input onKeyPress="return goodchars(event,'0123456789')" style="width:20px;" type="text" name="codiOficina" id="codiOficina" size="2" maxlength="2"/>
									<input onKeyPress="return goodchars(event,'0123456789')" style="width:40px;" type="text" name="codiUnitat" id="codiUnitat" size="4" maxlength="4"/>
									<input type="submit" value="<fmt:message key='cercar_alta'/>">
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
	<div class="RecuadreCentrat" style="width:900px;">
		<br/>
		<form name="gestionaOficinesForm" id="gestionaOficinesForm" action="<c:url value='/UtilAdmin'/>" method="post" onSubmit="return validaGestioUnitat();">	
			<input type="hidden" name="accion" id="accion" value="<c:out escapeXml="true" value='${accion}'/>"/>
			<input type="hidden" name="codiOficina" id="codiOficina" value="<c:out escapeXml="true" value='${codiOficina}'/>"/>
			<input type="hidden" name="codiUnitat" id="codiUnitat" value="<c:out escapeXml="true" value='${codiUnitat}'/>"/>
			<c:out escapeXml="false" value='${cabecera}'/>
			<table>
				<c:out escapeXml="false" value='${ofiInput}'/>
			</table>	
		</form>
	</div>
	<p>&nbsp;</p>
	<p>&nbsp;</p> 
	<p>&nbsp;</p>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	 var elFocus = document.getElementById('<c:out escapeXml="true" value='${elementFocus}'/>');
	 if (elFocus!=null) elFocus.focus();
</script>

	</body>
</html>   