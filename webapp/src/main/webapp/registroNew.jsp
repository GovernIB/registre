<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<!--
  Registro General CAIB - Registro de Entradas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%
    RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
    ParametrosRegistroEntrada param = new ParametrosRegistroEntrada();
    ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();
    
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    String usuario=request.getRemoteUser().toUpperCase();
    
    Integer intSerie=(Integer)session.getAttribute("serie");
    if (intSerie==null) {
        intSerie=new Integer(0);
        session.setAttribute("serie", intSerie);
    }
    int serie=intSerie.intValue();
    int serieForm = Integer.parseInt(request.getParameter("serie"));
    
    if (serie>serieForm) { 
        session.setAttribute("errorAtras","1");
%>
    <jsp:forward page="pedirdatos.jsp" />
       <% }
   
    serie++;
   // intSerie++;
    intSerie=Integer.valueOf(String.valueOf(serie));
    session.setAttribute("serie", intSerie);
    session.removeAttribute("errorAtras");
%>

<%
registro.fijaUsuario(usuario);
registro.setCorreo(request.getParameter("correo"));
registro.setdataentrada(request.getParameter("dataentrada"));
registro.sethora(request.getParameter("hora"));
registro.setoficina(request.getParameter("oficina"));
registro.setdata(request.getParameter("data"));
registro.settipo(request.getParameter("tipo"));
registro.setidioma(request.getParameter("idioma"));
registro.setentidad1(request.getParameter("entidad1"));
registro.setentidad2(request.getParameter("entidad2"));
registro.setaltres(request.getParameter("altres"));
registro.setbalears(request.getParameter("balears"));
registro.setfora(request.getParameter("fora"));
registro.setsalida1(request.getParameter("salida1"));
registro.setsalida2(request.getParameter("salida2"));
registro.setdestinatari(request.getParameter("destinatari"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
registro.setcomentario(request.getParameter("comentario"));
%>

<%
     registro=regent.validar(registro);
     boolean ok=registro.getValidado();


   if (!ok){
          request.setAttribute("registroEntrada",registro);

%>
        <jsp:forward page="pedirdatos.jsp" />
<% } else { 
    


    registro=regent.grabar(registro);

    boolean grabado=registro.getGrabado();



            if (!grabado) {
                request.setAttribute("registroEntrada",registro);

%>
                <jsp:forward page="pedirdatos.jsp" />

<%            } else {
                String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
                String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
                String bloqueoAno=(session.getAttribute("bloqueoAno")==null) ? "" : (String)session.getAttribute("bloqueoAno");

                 if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
                    valores.liberarDisquete(bloqueoOficina,bloqueoTipo,bloqueoAno,usuario);
                    session.removeAttribute("bloqueoOficina");
                    session.removeAttribute("bloqueoTipo");
                    session.removeAttribute("bloqueoAno");
                    session.removeAttribute("bloqueoUsuario");
                    session.removeAttribute("bloqueoDisquete");
                 }
%>

<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        <LINK TYPE="text/CSS" rel="stylesheet" HREF="estilos.css">
        
        
    </head>
    <body>
        <p>

        

        <p>&nbsp;
<CENTER><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center>&nbsp;<p>
        <table class="recuadro" width="400" align="center">
            <tr>
            <td>
                &nbsp;<br><CENTER><b><fmt:message key='registre'/> <%=registro.getNumeroEntrada()%>/<%=registro.getAnoEntrada()%> <fmt:message key='desat_correctament'/></B></CENTER>
            </td>
            </tr>   
            <tr><td>&nbsp;</td></tr>
            <tr>
            <td>
                <p><CENTER><b><fmt:message key='oficina'/> :<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>
            </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                    <CENTER><a style="text-decoration: none;" type="button" class="botonFormulario" href="pedirdatos.jsp">
                                    &nbsp;<fmt:message key='registro.nuevo_registro'/>&nbsp;
                                </a>
                        </CENTER>
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
        </table>
        <!-- Nueva tabla -->
        &nbsp;<p>
        &nbsp;<p>
        <%--
        <table class="recuadro" align="center">
            <tr>
            <td>
            <a target="_blank" href="imprimeSello?tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E">
            <img border="0" src="imagenes/impsello.gif" align=middle alt="Marge superior esquerra">
            </a>
            </TD>
            <td>&nbsp;</TD>
            <td>
            <a target="_blank" href="imprimeSello?tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E">
            <img border="0" src="imagenes/impsello.gif" align=middle alt="Marge superior dreta">
            </a>
            </TD>
            </TR>
            <tr>
            <td>&nbsp;</TD>
            <td align="center"><fmt:message key='imprimir_segell'/></TD>
            <td>&nbsp;</TD>
            </TR>
            <tr>
            <td>
            <a target="_blank" href="imprimeSello?tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E">
            <img border="0" src="imagenes/impsello.gif" align=middle alt="Marge inferior esquerra">
            </a>
            </TD>
            <td>&nbsp;</TD>
            <td>
            --%>
            <script>
              function imprime1() {
                pdf1.print();
              }
              function imprime2() {
                pdf2.print();
              }
              function imprime3() {
                pdf3.print();
              }
              function imprime4() {
                pdf4.print();
              }              
            </script>
            <%--<a target="_blank" href="imprimeSello?tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E">
            <a href="javascript:imprime()">
            <img border="0" src="imagenes/impsello.gif" align=middle alt="Marge inferior dreta">
            </a>
            </TD>
            </TR>
        </table>
        --%>
            <!-- Fin de la nueva tabla -->
            <table align="center">
            <tr>
            <td>
              <iframe height="120" width="80" name="pdf1" src="imprimeSello?tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E&hora=<%=registro.getHora()%>"></iframe>
            </td>
            <td>&nbsp;&nbsp;</td>
            <td>
                <iframe height="120" width="80" name="pdf2" src="imprimeSello?tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E&hora=<%=registro.getHora()%>"></iframe>
            </td>
            <td>&nbsp;&nbsp;</td>
            <td>
                <iframe height="120" width="80" name="pdf3" src="imprimeSello?tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E&hora=<%=registro.getHora()%>"></iframe>
            </td>
            <td>&nbsp;&nbsp;</td>
            <td>                       
                <iframe height="120" width="80" name="pdf4" src="imprimeSello?tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&data=<%=registro.getDataEntrada()%>&ES=E&hora=<%=registro.getHora()%>"></iframe>
            </td>
            </tr>
            <tr>
            <td align="center">
              <a href="javascript:imprime1()">[ Imprimir ]</a>
            </td>
            <td>&nbsp;&nbsp;</td>
            <td align="center">
              <a href="javascript:imprime2()">[ Imprimir ]</a>
            </td>
            <td>&nbsp;&nbsp;</td>
            <td align="center">
              <a href="javascript:imprime3()">[ Imprimir ]</a>
            </td>
            <td>&nbsp;&nbsp;</td>
            <td align="center">
              <a href="javascript:imprime4()">[ Imprimir ]</a>
            </td>
            </tr>
            </table>
                
                
<%
            }
   } 


%>

    </body>
</html>