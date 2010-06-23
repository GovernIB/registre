<%@page import="java.util.*, javax.naming.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%
    InitialContext ictx = new InitialContext();
    Context myenv = (Context)ictx.lookup("java:comp/env");
    String urlBDP = (String)myenv.lookup("BDP.url");
    String urlForward = (String)myenv.lookup("BDP.forward");
%>

<html>
<head><title>Selecció de persones</title>
<SCRIPT>
function selecciona(persona) {
    top.opener.setPersona(persona);
    close();
}
</SCRIPT>
<LINK TYPE="text/CSS" rel="stylesheet" HREF="estilos.css">


</head>

<body>
    <table width="100%" border="0">
        <form name="form1" method="post" action="<%=urlBDP%>">
        <INPUT TYPE="hidden" NAME="CAMPO_1" value="SI">
        <INPUT TYPE="hidden" NAME="CON_1" value="0">

        <INPUT TYPE="hidden" NAME="CAMPO_3" value="SI">
        <INPUT TYPE="hidden" NAME="CON_3" value="0">

        <INPUT TYPE="hidden" NAME="CAMPO_5" value="SI">
        <INPUT TYPE="hidden" NAME="CON_5" value="0">
            
        <INPUT TYPE="hidden" NAME="ID_USU" value="AD_ADM">
        <INPUT TYPE="hidden" NAME="URL_FORWARD" value="<%=urlForward%>">
        <tr>
            <td>Recerca per :</td>
            <td>
                <INPUT TYPE="radio" NAME="TYPE_ID" VALUE="1" checked> D.N.I.
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>        
            <td>
                <INPUT TYPE="radio" NAME="TYPE_ID" VALUE="2"> Passaport
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>        
            <td>
                <INPUT TYPE="radio" NAME="TYPE_ID" VALUE="5"> NIE
            </td>
        </tr>
	<tr>
            <td>&nbsp;</td>
            <td>
                <INPUT TYPE="radio" NAME="TYPE_ID" VALUE="9"> CIF
            </td>
	</tr>
        <tr>
            <td>Número :&nbsp;</td>                
            <td>
		<INPUT TYPE="text" NAME="ID" SIZE=30 MAXLENGTH=30 value="99999999Z">
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td align="center" colspan="2">
                <input type=submit value="Recerca">
            </td>
        </tr>
        </form>
    </table>
    &nbsp;<p></p>
    <table width="100%" border="0">
    <%
        String nombre = (request.getAttribute("CAMPO_1")==null) ? "" : (String)request.getAttribute("CAMPO_1");
        String apellido1 = (request.getAttribute("CAMPO_3")==null) ? "" : (String)request.getAttribute("CAMPO_3");
        String apellido2 = (request.getAttribute("CAMPO_5")==null) ? "" : (String)request.getAttribute("CAMPO_5");
        String persona=nombre.trim()+" "+apellido1.trim()+" "+apellido2.trim();
        
       // persona="Jose Pepe pepito";
    %>
        <tr>
            <td>Nom:</td>
            <td><a href="javascript:selecciona('<%=persona%>')"><%=persona%></a></td>
    </table>
</body>
</html>
