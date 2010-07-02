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
		
		function obriTipusDoc() {
            	miTipusDoc=open("<c:url value='/admin/popup.do?accion=totsTipusDoc'/>","TipusDocuments","scrollbars,resizable,width=300,height=400");
            	miTipusDoc.focus();
        }
        
        function setTipDoc(codTipDoc) {
            	var formulari = document.getElementById("cercaTipusDoc");
            	document.getElementById("tipDocGestionar").value=codTipDoc;
            	formulari.submit();
        }

        function validaCerca() {
            	var formulari = document.getElementById("cercaTipusDoc");
            	if (document.getElementById("tipDocGestionar").value=="" ) {
            		alert("<fmt:message key='error_tipus_document'/>");
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
			<li><fmt:message key="gestio_tipus_documents"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
		
		<c:choose>
    <c:when test="${not hayTipDoc}">
   
    <br/>
    		<p>&nbsp;</p>  
            <table align="center" >
                <tr>
                <td>
                	 <div id="menuDocAdm" style="width:300px">
                	 <ul style="margin-right: 5px">
    					<li><p><fmt:message key='tipus_document_a_gestionar_q'/> <a href="javascript:obriTipusDoc()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align=middle alt="<fmt:message key='cercar'/>"></a></p></li>
                    	<li>	<form id="cercaTipusDoc" name="cercaTipusDoc" action="<c:url value='/admin/controller.do?accion=tipusDocuments'/>" method="post" onSubmit="return validaCerca();">
    								<input style="width:20px;" type="text" text="codi tipus document" name="tipDocGestionar" id="tipDocGestionar" size="2" maxlength="2"/>&nbsp;<input type="submit" value="<fmt:message key='cercar_alta'/>">
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
	    <c:when test="${existeTipDoc}">
	  	<p><fmt:message key='tipus_document_a_gestionar'/> <b><c:out value='${tipusDocuments.codiTipusDoc} - ${tipusDocuments.descTipusDoc}'/></b></p>
	    </c:when>
		  <c:otherwise>
  		<p><fmt:message key='tipus_document_no_existeix'><fmt:param value='${tipDocGestionar}'/></fmt:message></p>
		  </c:otherwise>
		  </c:choose>
		

	<br/>
<form name="gestionaOficines" id="gestionaOficines" action="<c:url value='/UtilAdmin'/>" method="post">	
<input type="hidden" name="accion" id="accion" value="<c:out escapeXml="true" value='${valorAccio}'/>"/>
<input type="hidden" name="tipDocGestionar" id="tipDocGestionar" value="<c:out escapeXml="true" value='${tipDocGestionar}'/>"/>
<table>
<tr>
	<th><fmt:message key='codi_tipus_document'/></th>
	<th><fmt:message key='descripcio_tipus_document'/></th>
    <th><fmt:message key='data_baixa_2'/></th>
</tr>
<tr>
	<td><input type="text" name="codTipusDoc" readonly="true"  id="codTipusDoc" size="2" maxlength="2" value="<c:out escapeXml="true" value='${tipusDocuments.codiTipusDoc}'/>" style="width: 23px;"/></td>
	<td>
	  <input type="text" name="descTipusDoc" id="descTipusDoc" value="<c:out escapeXml="true" value='${tipusDocuments.descTipusDoc}'/>" size="30" maxlength="30" style="width: 225px;"/>
    <%--<input type="hidden" name="dataBaixa" id="dataBaixa" value="<c:out escapeXml="true" value='${tipusDocuments.dataBaixa}'/>" />--%>
	</td>
    <td><input type="text" name="dataBaixa" id="dataBaixa" <c:out escapeXml="true" value='${readonly}'/> value="<c:out escapeXml="true" value='${tipusDocuments.dataBaixa}'/>" size="10" maxlength="10" style="width: 70px;"/></td>
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