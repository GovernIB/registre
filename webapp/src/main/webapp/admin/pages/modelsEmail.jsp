<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*, es.caib.regweb.webapp.servlet.EmailServlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<% 
String titulo = ""; 
String cuerpo = ""; 
ModeloDocumentoData modelGestionar = (ModeloDocumentoData) request.getAttribute("modelGestionar");

if(modelGestionar!=null){
	 titulo = ((modelGestionar.getTitulo()==null)?"":modelGestionar.getTitulo().trim()); 
	 cuerpo = ((modelGestionar.getCuerpo()==null)?"":modelGestionar.getCuerpo().trim()); 
	}
%>
<html>
<head>
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


    function confirmaProceso(){     
        if (checkTitulo() && checkComentario()) {
            return true;
        } else {
            return false;
        }
    }
    function checkTitulo(){     
        if (document.gestionaModelEmail.titulo && document.gestionaModelEmail.titulo.value.length>99) {
            alert("No es pot afegir més text al títol del email.");
            return false;
        } else {
            return true;
        }
    }

    function checkComentario(){     
        if (document.gestionaModelEmail.cuerpo && document.gestionaModelEmail.cuerpo.value.length>999) {
            alert("No es pot afegir més text al cos del email.");
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
			<li><a href="<c:url value='/admin/controller.do?accion=gestioDocuments'/>"><fmt:message key="admin.gestio_documents"/></a></li>	
			<li><fmt:message key="admin.models_email"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>

    <c:choose>
    <c:when test="${not hayModeloEmail}">
<br/>
		<p>&nbsp;</p>  
        <table align="center" >
            <tr>
            <td>
            	 <div id="menuDocAdm" style="width:350px">
            	 <ul style="margin-right: 5px">
					<li><p><fmt:message key='admin.modelo_email_a_gestionar_q'/> <a href="javascript:obriEmail()"></a></p></li>
                	<li>	<form id="cercaModelEmail" name="cercaModelEmail" action="<c:url value='/admin/controller.do?accion=modelsEmail'/>" method="post" onSubmit="return validaCerca();">

								<select name="tipo" id="tipo" text="tipo email">
                                   <option value="<%=EmailServlet.TIPUS_CIUTADA%>"><fmt:message key='admin.email_extern'/></option>
                                   <option value="<%=EmailServlet.TIPUS_INTERN %>"><fmt:message key='admin.email_intern'/></option>
                                </select>&nbsp;
                                <select name="idioma" id="idioma" text="idioma email">
                                   <option value="<%=EmailServlet.IDIOMA_CASTELLANO%>"><fmt:message key='literal.castella'/></option>
                                   <option value="<%=EmailServlet.IDIOMA_CATALAN%>"><fmt:message key='literal.catala'/></option>
                                </select>
                                <br/><br/>
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

	<div class="RecuadreCentrat" style="width:700px;">
    <c:choose>
    <c:when test="${existeModel}">
		<p><fmt:message key='admin.modelo_email_a_gestionar'/> <b><c:out escapeXml="false" value='${tipo} - ${idioma}'/></b></p>
    </c:when>
    <c:otherwise>
		<p><fmt:message key='admin.modelo_email_a_gestionar_no_existeix'></fmt:message></p>
    </c:otherwise>
    </c:choose>
	
	<br/>
	<form name="gestionaModelEmail" id="gestionaModelEmail" action="<c:url value='/UtilAdmin'/>" method="post" onsubmit="return confirmaProceso()">	
		<input type="hidden" name="accion" id="accion" value="<c:out escapeXml="true" value='${valorAccio}'/>"/>
		
		<fmt:message key='admin.tipo_email'/>: <input type="text" name="tipo" readonly="readonly"  id="tipo" size="3" maxlength="3" value="<c:out escapeXml="false" value='${tipo}'/>" style="width: 23px;"/>
		<fmt:message key='admin.idioma_email'/>: <input type="text" name="idioma" readonly="readonly"  id="idioma" size="3" maxlength="3" value="<c:out escapeXml="false" value='${idioma}'/>" style="width: 23px;"/>
		<br/>
		<fmt:message key='admin.email_subject'/>: <input type="text" name="titulo"   id="titulo" size="30" maxlength="100" value="<%=titulo%>" onkeypress="return checkTitulo()" />
		<br/>
		<fmt:message key='admin.email_cos'/>:<br/>
		<textarea cols="75" onkeypress="return checkComentario()" rows="15" name="cuerpo"><%=cuerpo%></textarea>
		<p><input type="submit" value="<fmt:message key='desa'/>"/></p>
	
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