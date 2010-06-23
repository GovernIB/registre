<%@ page import = "java.util.*, org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>


<html lang="es">
    <head>
        <title>Importar Repro</title>
        
        
        <link rel="shortcut icon" href="favicon.ico"/>
        <script language="javascript" src="jscripts/TAO.js"></script>
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
                alert("Advertiment: L'arxiu no sembla ser de text. L'extensió deu ser .txt");
            }
            }
            }
            function validar(f) {
            error="";
            resultado=true;

            if (f.fichero.value=="") {
            error+="El nom no pot ser buid.\n";
            }
            if (error != "") {
            alert(error);
            resultado=false;
            }
            return resultado;
            }
        </script>
    </head>
 <body bgcolor="#ECE9D8">
<%
	Logger log = Logger.getLogger(this.getClass());
    try {
   	   	String tipusCookies=request.getParameter("tipusCookie");
   	   	es.caib.regweb.webapp.servlet.FileUploader fileuploader=new es.caib.regweb.webapp.servlet.FileUploader(request, response, tipusCookies);
   	   
   	   	out.println("<div align=\"center\">");
        if (fileuploader.getBorradas()) { 
      		out.println("<br><br><b> S'han esborrat les Repros que hi havien.</b>");
      		}  
		if(fileuploader.getDescartades()>0){		    
	       	out.println("<br><br><b> No s'han importat "+fileuploader.getDescartades()+" Repros per superar el m\u00E0xim de repros a l'aplicació.</b>");        
	       	}
        if(fileuploader.getImportadas()>0){
        	out.println("<br><br><b>S'han importat correctament "+fileuploader.getImportadas()+" repros. </b>");
        	}
        if(fileuploader.getNoGravades()>0){
        	out.println("<br><br><b>Hi ha "+fileuploader.getNoGravades()+" Repros amb el nom repetit que no s'han importat. </b>");
        	}
        if((fileuploader.getImportadas()==0) && (fileuploader.getNoGravades()==0)&& (fileuploader.getDescartades()==0)){
        	out.println("<br><br><b>No s'ha importat res. El fitxer és v\u00E0lid?</b>");
        	}
        out.println("</div>");    
     
        //Excepción al insertar
       } catch (java.sql.SQLException e) {
    	   log.debug("Excepció tractada");
    	   log.debug(e);
    %> 
     &nbsp;<br>
        <div align="center">
        Error: Hi ha hagut un error en la importació de les repros.
        </div>
        <p>&nbsp;</p>
    <%
       } catch (Exception e) {
			log.debug("Excepció tractada");
			log.debug(e);
			//e.printStackTrace();
    %>

                <p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>
                <form method="post" onSubmit="return validar(this);" enctype="multipart/form-data">
                <input type="hidden" name="tipusCookie" value="<%=request.getParameter("tipusCookie")%>">
                <table width="95%">
                    <tr valign="middle"> 
                        <td>
                            <input type="file" name="fichero" size="44" onchange="checkImagen(this)">
                        </td>
                    </tr>
                    <tr valign="middle">
                        <td align="center">
                            <input type="checkbox" name="borra_repro" value="s"> 
                            &nbsp;Esborrar Repros desades actualment
                        </td>
                    </tr>
                    <tr valign="middle">
                        <td align="center">
                            <input type="SUBMIT" value="<fmt:message key='enviar'/>">
                        </td>
                    </tr>
                </table>
                </form> 
            </body>
        </html>

<% 
       }
       %>
    	   <p></p>
    		<center><a href="javascript:window.close();"><fmt:message key='tancar'/></a></center>
      