<%@ page import = "java.util.*, java.io.*, org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

        <html>
            <head>
        			<title><fmt:message key="traspassos"/></title>
				<script language="javascript" src="<c:url value='/jscripts/TAO.js'/>"></script>
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

				function checkImagen(campo) {
                    nombre = campo.value;
                    //Se comprueba que no este vacio
                    if (nombre!=""){
                    //Se comprueba su extension
                    sepExtension=".";
                    //el ultimo valor del while es la extension
                    while (nombre.indexOf(sepExtension)!=-1){
                    	nombre = nombre.slice(nombre.indexOf(sepExtension)+1);
                    }
                    nombre=nombre.toLowerCase();
                    if ((nombre != 'txt') ||
                    	nombre == campo.value) {
                        //alert("Advertiment: L'arxiu no sembla ser de text. L'extensió deu ser .txt");
                    }
                    }
                    }
                    function validarAltaTraspas(f) {
                    	error="";
                    	resultado=true;
						f = document.getElementById('altaTraspas');
                    	if (f.oficinaOnRegistrar.value=="") {
                    		error+="<fmt:message key='error_oficina_buida'/>\n";
                    	}
						if (f.organismeEmissor.value=="") {
                    		error+="<fmt:message key='error_organisme_buit'/>\n";
                    	}
                    	if (error != "") {
                    		alert(error);
                    		resultado=false;
                    	} else {
							//Eliminam el botó de "submit" per a que no el tornin a apretar per error!.
							//alert('Deshabilitam el botó de submit');
							var botoEnviar = document.getElementById('botoEnviar');
							var divAnchor = document.getElementById('anchorSubmit');
							divAnchor.style.width='100px;';
							botoEnviar.href='./admin/controller.do?accion=index';
							botoEnviar.name='<fmt:message key="tornar_menu_administracio"/>';
							botoEnviar.innerHTML='<fmt:message key="tornar_menu_administracio"/>';
							botoEnviar.onclick = '';
							
							//alert('Deshabilitat el botó de submit');
						}
                    	return resultado;
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
                      abreError();
                    });				
                    </c:if>
		</script>
            </head>
         <body>

     	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="traspassos"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>

		<p>&nbsp;</p>  
        <table align="center" >
            <tr>
            <td>
            	 <div id="menuDocAdm" style="width:400px;">
            	<form name="altaTraspas" id="altaTraspas" method="post" action="<c:url value='/UtilAdmin'/>" method="post">
					<input type="hidden" name="accion" id="accion" value="altaTraspas"/>
					<input type="hidden" name="fitxer" id="fitxer" value="<c:out escapeXml="true" value='${fitxer}'/>"/>
					<input type="hidden" name="nomFitxer" id="nomFitxer" value="<c:out value='${nomFitxer}'/>"/>
					<p>&nbsp;</p>
                   	<p><strong><fmt:message key='registres_traspas'><fmt:param value='${nombreLinies}'/></fmt:message></strong></p>
					<ol>
					<%
   	try{
   		BufferedReader buffReader = new BufferedReader( new StringReader(fitxer) );
   		String tmp = "";
   		int j=0;
   		while ( (tmp = buffReader.readLine())!= null ) {
			j++;
   			out.println("<li>"+tmp+"</li>");   		
   		}
   	}catch(Exception ex){
   		ex.printStackTrace();
    }
						%>
						</ol>
						<p><strong><fmt:message key='parametres_traspas'/></strong></p>
          		        <ul>
          		        	<li><fmt:message key='codi_oficina_registrar'/>&nbsp;<input type="text" name="oficinaOnRegistrar" id="oficinaOnRegistrar" size="2" maxlength="2" value="95" style="width:20px;"/></li>
           	         		<li><fmt:message key='codi_organisme_destinatari'/>&nbsp;<input type="text" name="organismeEmissor" id="organismeEmissor" size="4" maxlength="4" value="9520" style="width:30px;"/></li>
						</ul>
						<div style="margin-left: 37.5%;margin-right: 37.5%;padding: 8px;text-align: center;">
	             	    	<p id="anchorSubmit" name="anchorSubmit" class="anchorAsSubmitButton" >
	             	    		<a id="botoEnviar" name="botoEnviar" style="cursor: pointer;" onclick='if (validarAltaTraspas()){document.forms.altaTraspas.submit();alert("<fmt:message key='un_cop_baixat'/>");} return false;'><fmt:message key='registrar'/></a>
	             	    	</p>
	             	    </div>
						<p>&nbsp;</p>
                </form> 
            	</div>
            	</td>
			</tr>			
        </table>
		<p>&nbsp;</p> 

<script type="text/javascript">
	 var elFocus = document.getElementById('oficinaOnRegistrar');
	 if (elFocus!=null) elFocus.focus();
</script>

            </body>
        </html>