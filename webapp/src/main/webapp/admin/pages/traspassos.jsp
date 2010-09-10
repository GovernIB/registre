<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

        <html>
            <head>
                <title><fmt:message key="traspassos"/></title>

				<script language="javascript" src="<c:url value='/jscripts/TAO.js'/>"></script>
                <script>
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
                    
					function validar(f) {
                    	error="";
	                    resultado=true;

	                    if (f.fitxer.value=="") {
		                    error+="<fmt:message key="error_nom_buit"/>\n";
    	                }
	                    if (error != "") {
    		                alert(error);
            		        resultado=false;
                    	}
	                    return resultado;
                    }
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
		<p>&nbsp;</p>  
		<p>&nbsp;</p>
        <table align="center" >
            <tr>
            <td>
            	 <div id="menuDocAdm" style="width:400px;padding:3px;">
            	<form method="post" action="<c:url value='/PujaFitxerTraspas'/>" onSubmit="return validar(this);" enctype="multipart/form-data">
					<input type="hidden" name="accion" id="accion" value="traspas"/>
						<p>&nbsp;</p>
						<p><fmt:message key="selecciona_fitxer_traspas"/></p>
						<p>&nbsp;</p>
						<p style="text-align: center;"><input type="file" name="fitxer" id="fitxer" size="44" onchange="checkImagen(this)"/></p>
						<p style="text-align: center;"><input type="submit" name="submitButton" id="submitButton" value="<fmt:message key='enviar'/>"/></p>
						<p>&nbsp;</p>
                </form> 
            	</div>
            	</td>
			</tr>			
        </table>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p> 
<script type="text/javascript">
	 var elFocus = document.getElementById("fitxer");
	 if (elFocus!=null) elFocus.focus();
</script>
 		<!-- PEU -->
   		<!-- Fi PEU -->
            </body>
        </html>