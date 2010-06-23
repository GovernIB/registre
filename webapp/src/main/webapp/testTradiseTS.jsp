<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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

    
  //  Vector document = new Vector();
   // Vector documentName = new Vector();

  //  byte doc1 = Byte.valueOf("dfsdfsd"); //FileHash.makeHashing("/home/jo/xuleta_html.sxw","SHA");
//    byte doc2 = Byte.valueOf("ffsdfsd"); //FileHash.makeHashing("/home/jo/xpvlk.txt","SHA");
   
    //document.addElement("a");
    //document.addElement("b");
    //documentName.addElement("xuleta_html.sxw");
    //documentName.addElement("kpvlk.txt");
    
   // FileOutputStream xmlDocument = new FileOutputStream("/dev/null");
   // FileOutputStream smimeDocument = new FileOutputStream("/dev/null");
   // FileOutputStream signatureFile = new FileOutputStream("/dev/null");

    //Antes de llamar a grabarConFirma se deben pasar al EJB todos los parÃ¡metros necesarios para
    //crear un registro de entrada con los mÃ©todos set disponibles

//    registro.grabarConFirma("SHA",document,documentName,xmlDocument,smimeDocument,signatureFile);

	//out.println(registro.TestTS());
	
	//out.println(xmlDocument);
	%>
</body>
  
</html>
