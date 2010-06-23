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
		
		function obriMunicipis() {
            	miMunicipi=open("<c:url value='/admin/popup.do?accion=totsMunicipis060'/>","TipusDocuments","scrollbars,resizable,width=300,height=400");
            	miMunicipi.focus();
        }
        
        function setMunicipis060(codMunicipi) {
            	var formulari = document.getElementById("cercaMunicipis060");
            	document.getElementById("mun060Gestionar").value=codMunicipi;
            	formulari.submit();
        }

        function validaCerca() {
            	var formulari = document.getElementById("cercaMunicipis060");
            	if (document.getElementById("mun060Gestionar").value=="" ) {
            		alert("<fmt:message key='error_camp_municipi_012'/>");
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
			<li><fmt:message key="gestio_municipis_012"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>

    <c:choose>
    <c:when test="${not hayMunicipio}">
<br/>
		<p>&nbsp;</p>  
        <table align="center" >
            <tr>
            <td>
            	 <div id="menuDocAdm" style="width:300px">
            	 <ul style="margin-right: 5px">
					<li><p><fmt:message key='municipi_012_a_gestionar_q'/> <a href="javascript:obriMunicipis()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align=middle alt="<fmt:message key='cercar'/>"></a></p></li>
                	<li>	<form id="cercaMunicipis060" name="cercaMunicipis060" action="<c:url value='/admin/controller.do?accion=municipis060'/>" method="post" onSubmit="return validaCerca();">
								<input onKeyPress="return goodchars(event,'0123456789')" style="width:20px;" type="text" text="codi municipi 012" name="mun060Gestionar" id="mun060Gestionar" size="3" maxlength="3"/>&nbsp;
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

  </c:when>
  <c:otherwise>

	<div class="RecuadreCentrat" style="width:400px;">
    <c:choose>
    <c:when test="${existeMunicipio}">
		<p><fmt:message key='municipi_012_a_gestionar'/> <b><c:out escapeXml="false" value='${municipi060.codiMunicipi060} - ${municipi060.descMunicipi060}'/></b></p>
    </c:when>
    <c:otherwise>
		<p><fmt:message key='municipi_012_no_existeix'><fmt:param value='${mun060Gestionar}'/></fmt:message></p>
    </c:otherwise>
    </c:choose>
	
	<br/>
<form name="gestionaOficines" id="gestionaOficines" action="<c:url value='/UtilAdmin'/>" method="post">	
<input type="hidden" name="accion" id="accion" value="<c:out escapeXml="true" value='${valorAccio}'/>"/>
<input type="hidden" name="mun060Gestionar" id="mun060Gestionar" value="<c:out escapeXml="true" value='${mun060Gestionar}'/>"/>
<table>
<tr>
	<th><fmt:message key='codi_municipi_012'/></th>
	<th><fmt:message key='nom_municipi_012'/></th>
  <!-- <th><fmt:message key='data_baixa_2'/></th> -->
</tr>
<tr>
	<td><input type="text" name="codMunicipi" readonly="true"  id="codMunicipi" size="3" maxlength="3" value="<c:out escapeXml="true" value='${municipi060.codiMunicipi060}'/>" style="width: 23px;"/></td>
	<td>
	  <input type="text" name="descMunicipi" id="descMunicipi" value="<c:out escapeXml="true" value='${municipi060.descMunicipi060}'/>" size="30" maxlength="30" style="width: 225px;"/>
    <input type="hidden" name="dataBaixa" id="dataBaixa" value="<c:out escapeXml="true" value='${municipi060.dataBaixa}'/>" />
	</td>
  <!-- <td><input type="text" name="dataBaixa" id="dataBaixa" <c:out escapeXml="true" value='${readonly}'/> value="<c:out escapeXml="true" value='${municipi060.dataBaixa}'/>" size="10" maxlength="10" style="width: 70px;"/></td> -->
</tr>
<tr>
	<td align="center" colspan="3"><input type="submit" value="<fmt:message key='desa'/>"/></td>
</tr>

</table>
</form>
</div>
</c:otherwise>
</c:choose>

<script type="text/javascript">
	 var elFocus = document.getElementById('<c:out escapeXml="true" value='${elementFocus}'/>');
	 if (elFocus!=null) elFocus.focus();
</script>
		<p>&nbsp;</p> 		
		<p>&nbsp;</p>
		<p>&nbsp;</p> 

	</body>
</html>   