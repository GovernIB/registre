<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*,java.io.*" %>

<html>
<head>
    <LINK TYPE="text/CSS" rel="stylesheet" HREF="estilos.css">
    
	
    <title><fmt:message key='prova_del_registre_amb_signatura'/></title>
  </head>
  <body>
  <%
  
  /*Página que prueba la funcionalidad que permite obtener un acuse de recibo firmado de un
  registro de entrada
  
  */
  
    RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
    ParametrosRegistroEntrada pregent = new ParametrosRegistroEntrada();
    ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();

	
	
	ValoresFacade valores = ValoresFacadeUtil.getHome().create();
	String fitxer_a_firmar=".bash_history";
    String usuario=request.getRemoteUser().toUpperCase();
    
    Vector document = new Vector();
    Vector documentName = new Vector();

    byte[] doc1 = FileHash.makeHashing(System.getProperty("user.home")+fitxer_a_firmar,"SHA-1");
//    byte[] doc2 = FileHash.makeHashing("c:/service.txt","SHA-1");
        
    document.addElement(doc1);
//    document.addElement(doc2);
    documentName.addElement(fitxer_a_firmar);
//    documentName.addElement("service.txt");
    FileOutputStream xmlDocument = new FileOutputStream(System.getProperty("user.home")+System.getProperty("file.separator")+"xmlDocument.xml");
    FileOutputStream smimeDocument = new FileOutputStream(System.getProperty("user.home")+System.getProperty("file.separator")+"smime.txt");
    FileOutputStream signatureFile = new FileOutputStream(System.getProperty("user.home")+System.getProperty("file.separator")+"firma.signature");
	out.println("<p>"+System.getProperty("user.home")+System.getProperty("file.separator")+"firma.signature"+"</p>");
    //Antes de llamar a grabarConFirma se deben pasar al EJB todos los parÃ¡metros necesarios para
    //crear un registro de entrada con los mÃ©todos set disponibles

//    registro.grabarConFirma("SHA",document,documentName,xmlDocument,smimeDocument,signatureFile);

registro.fijaUsuario(usuario);
registro.setdataentrada(valores.getFecha());
registro.sethora(valores.getHorasMinutos());

registro.setoficina("2");
registro.setdata("01/06/2006");
registro.settipo("CA");
registro.setidioma("2");
registro.setentidad1("AUTORIT");
registro.setentidad2("0");
registro.setaltres("");
registro.setbalears("1");
registro.setfora("");
registro.setsalida1("");
registro.setsalida2("");
registro.setdestinatari("1800");

registro.setidioex("1");
registro.setdisquet("");

out.println("<p>Fitxer de prova a firmar:"+System.getProperty("user.home")+".bash_history</p>");
out.println("<p>Carpeta on es desaran el resultat de firmar:"+System.getProperty("user.home")+"</p>");
registro.setcomentario("Prova de signatura digital!");
registro = regent.validar(registro);
boolean ok=registro.getValidado();
if (!ok){

        Hashtable errores = (registro==null)?new Hashtable():registro.getErrores();
        if (errores.size() > 0) {%>
        <table class="recuadro" width="599" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/></p>
                    <ul>
                        <%      for (Enumeration e=errores.elements();e.hasMoreElements();) { %>
                        <li><%= e.nextElement()%></li>
                        <%}%>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
        <%  } %>
<p>LA VALIDACIÓ DEL REGISTRE HA FALLAT!!!</p>
<% } else {
%>	
<p>Registre validat!!!</p>
 <% 
	registro.setdataentrada(valores.getFecha());
	registro.sethora(valores.getHorasMinutos());
	//regent.grabarConFirma("SHA-1",document,documentName,xmlDocument,smimeDocument,signatureFile);
	%>
	<p>Registre signat!!!</p>
<%
} 	
%>
</body>
  
</html>
