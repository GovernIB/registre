<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
    <script LANGUAGE="JavaScript">


      function confirmaProceso() {
          var respuesta = true;

          descCurtaOrganisme=document.gestionaOrganismes.descCurtaOrganisme.value;
		      descLlargaOrganisme=document.gestionaOrganismes.descLlargaOrganisme.value;

          if ( (descCurtaOrganisme == null) || (descCurtaOrganisme.length == 0)) {
            		alert("<fmt:message key='error_desc_curta_org_buit'/>");
            		respuesta = false;
            } else {   
             if ( (descLlargaOrganisme == null)|| (descLlargaOrganisme.length == 0)) {
           		  alert("<fmt:message key='error_desc_llarg_org_buit'/>");
            		respuesta = false;
               } else {   
                    respuesta = true;
               }
           } 
			return respuesta;
        }
    </script>
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

		function abreModelos() {
            	miDestinatarios=open("<c:url value='/admin/popup.do?accion=totsModelsRebuts'/>","models","scrollbars,resizable,width=400,height=400");
            	miDestinatarios.focus();
        }
        
        function setModel(codModelo) {
            	var formulari = document.getElementById("cercaModel");
            	document.getElementById("modelGestionar").value=codModelo;
            	formulari.submit();
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
			<li><fmt:message key="gestio_models_rebut"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>

    <c:choose>
    <c:when test="${not hayModel}">
    <br/>
    		<p>&nbsp;</p>
    		<p>&nbsp;</p>  
                	 <div  id="menuDocAdm" style="width: 250px;padding-left:10px;">
                	 <ul style="margin-right: 5px">
    					<li><p><fmt:message key='model_a_gestionar_q'/> <a href="javascript:abreModelos()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align=middle alt="<fmt:message key='cercar_alta'/>"></a></p></li>
                    	<li>	<form id="cercaModel" name="cercaModel" action="<c:url value='/admin/controller.do?accion=modelsRebuts'/>" method="post">
    								<input style="width:135px;" type="text" name="modelGestionar" id="modelGestionar" size="10" maxlength="20"/>
								    <input type="submit" value="<fmt:message key='cercar_alta'/>">
    							</form>
    					</li>
                	</ul>
                	</div>
    		<p>&nbsp;</p>
    		<p>&nbsp;</p>
    		<p>&nbsp;</p>
    		<p>&nbsp;</p>


    </c:when>
    <c:otherwise>


<p>&nbsp;</p>
<p>&nbsp;</p>
	<div class="RecuadreCentrat" style="width:500px">
    <c:choose>
    <c:when test="${existeModel}">
		<p><fmt:message key='model_a_gestionar'/> <br/> <b><c:out escapeXml="false" value='${modelGestionar}'/></b></p>
			
    <br/>
    <form name="gestionaModels" id="gestionaModels" action="<c:url value='/admin/controller.do?accion=altaModelRebut'/>" method="post"  enctype="multipart/form-data">	
    <input type="hidden" name="accion" id="accion" value="<c:out escapeXml="true" value='${valorAccio}'/>"/>
    <input type="hidden" name="modelGestionar" id="modelGestionar" value="<c:out escapeXml="true" value='${modelGestionar}'/>"/>
    <input type="hidden" name="nombre" id="nombre" value="<c:out escapeXml="true" value='${modelGestionar}'/>"/>
    <div  id="menuDocAdm" style="width: 450px;padding-left:10px;">
    <table>
    <tr>
    	<th><fmt:message key='nom_model_rebut'/></th>
    	<th><fmt:message key='tipus_dades'/></th>
    	<th></th>
    	<th><fmt:message key='esborrar'/></th>
    </tr>
    <c:forEach var="item" begin='0' end='${descModelSize-1}' step='2'>
  	<c:set var="nom" value="${descModel[item]}" />
  	<c:set var="content" value="${descModel[item+1]}" />
  	  <tr>
  	    <td><c:out escapeXml="false" value='${nom}'/></td>
  	    <td><c:out escapeXml="false" value='${content}'/></td>
			  <td><a href="<c:url value='/mostrafitxerrebut?nom=${nom}'/>" target="_blank"><fmt:message key='veure'/></a></td>
			  <td><input type="checkbox" value="s" name="borra_model" id="borra_model"/></td>
		  </tr>
		
    </c:forEach>

  	<tr>
    	<td align="center" colspan="2"><input type="submit" value="<fmt:message key='esborra'/>"/></td>
    </tr>

    </table>
    </form>
    </div>
	  </c:when>
	  <c:otherwise>
	  <p><fmt:message key="model_rebut_no_existeix"><fmt:param value="${modelGestionar}"/></fmt:message></p>
		
			<br/>
    <div  id="menuDocAdm" style="width: 450px;padding-left:10px;">
    <form name="gestionaModels" id="gestionaModels" action="<c:url value='/admin/controller.do?accion=altaModelRebut'/>" method="post"  enctype="multipart/form-data">	
    <input type="hidden" name="accion" id="accion" value="<c:out escapeXml="true" value='${valorAccio}'/>"/>
    <input type="hidden" name="nombre" id="nombre" value="<c:out escapeXml="true" value='${modelGestionar}'/>"/>
    <input type="hidden" name="modelGestionar" id="modelGestionar" value="<c:out escapeXml="true" value='${modelGestionar}'/>"/>
    <table>
    <tr>
    	<th><fmt:message key='nom_model_rebut'/></th>
    	<th><fmt:message key='fitxer'/></th>
    </tr>
    <tr>
    	<td><c:out escapeXml="false" value='${modelGestionar}'/></td>
    	<td><input type="file" name="fitxer" id="fitxer" value="" style="width:200px;"/></td>
    </tr>
    <tr>
    	<td align="center" colspan="2"><input type="submit" value="<fmt:message key='desa'/>"/></td>
    </tr>

    </table>
    </form>

		</div>
	  </c:otherwise>
	  </c:choose>

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