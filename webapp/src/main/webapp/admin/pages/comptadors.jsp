<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>


<html>
<head>
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
    <script>
        var listaExtractos=new Array();
        var i=0;
        var copiado=false;

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
			var llista = document.getElementById("altaComptadorOficina").getElementsByTagName(elementClickat.value);
			var llistaElementsFormAU = document.getElementById("altaComptadorOficina").elements;

			for (var i=0; i<llistaElementsFormAU.length ;i++) {
				//window.alert('Element '+i+' '+llistaElementsFormAU[i].name); 
				if ( llistaElementsFormAU[i].name == elementClickat.value ){
					llistaElementsFormAU[i].checked=valor;
				}
			}
		}
		
		function validaAltaComptadors() {
            	input_box=confirm("<fmt:message key='alerta_comptador'/>");
				if (input_box==true) { 
					// Output when OK is clicked
					//alert ("You clicked OK"); 
	         		return true;
				} else {
					// Output when Cancel is clicked
					//alert ("You clicked cancel");
	          		return false;
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
			<li><fmt:message key="inicialitzacio_comptadors"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
		
    <c:choose>
    <c:when test="${not hayAny}">

<br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>  
        <table align="center" >
            <tr>
            <td>
            	 <div  id="menuDocAdm" style="width:250px">
            	 <ul style="margin-right: 5px">
					<li><p><fmt:message key='any_inicialitzar_q'/></p></li>
                	<li>	<form id="cercaAny" name="cercaAny" action="<c:url value='/admin/controller.do?accion=comptadors'/>" method="post">
							<input onKeyPress="return goodchars(event,'0123456789')" style="width:35px;" type="text" name="anyGestionar" id="anyGestionar" size="4" maxlength="4"/>
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

	<div class="RecuadreCentrat">
	<p><fmt:message key='any_inicialitzar'/> <b><c:out escapeXml="false" value='${anyGestionar}'/></b></p>

	<br/>
<form name="altaComptadorOficina" id="altaComptadorOficina" action="<c:url value='/UtilAdmin'/>" method="post" onSubmit="return validaAltaComptadors();">	
<input type="hidden" name="accion" id="accion" value="altaComptadorOficina"/>
<input type="hidden" name="anyGestionar" id="anyGestionar" value="<c:out escapeXml="true" value='${anyGestionar}'/>"/>
<table>
<tr>
	<th><fmt:message key='oficina'/></th>
	<th><fmt:message key='comptador'/> <br/><fmt:message key='entrades'/></th>
	<th><fmt:message key='comptador'/> <br/><fmt:message key='sortides'/></th>
	<th><fmt:message key='comptador'/> <br/><fmt:message key='oficis'/></th>
	<th><fmt:message key='inicialitzar'/></th>
</tr>
<tr>
	<td colspan="4"></td>
	<td><input type="checkbox" name="AEtots" id="Initots" value="Ini" style="width: 60px;" onclick="MarcaTot(event);"/></td>
</tr>
	<c:out escapeXml="false" value='${ofiInput}'/>
<tr>
	<td align="center" colspan="4"><input type="submit" value="<fmt:message key='desa'/>"/></td>
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