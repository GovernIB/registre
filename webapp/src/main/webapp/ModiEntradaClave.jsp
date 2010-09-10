<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    String usuario=request.getRemoteUser();

    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    String numero="";
    String ano="";
    String ofi="";
%>
<%! 
    void escribeSelect(javax.servlet.jsp.JspWriter out, Vector valores, String referencia) throws java.io.IOException {
        
        for (int i=0;i<valores.size();i=i+2){
                String codigo=valores.get(i).toString();
                String descripcion=valores.get(i+1).toString();
                out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) ? "selected" : "")+">");
                out.write(codigo+"-"+descripcion);
                out.write("</option>\n");
        }
    }
%>

<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        
        
        
        <script language="javascript" src="jscripts/TAO.js"></script>

    </head>
    <body bgcolor="#FFFFFF">
    
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><fmt:message key='modificacio_dentrades'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<br/>
		<br/>	
		<br/>
        <!-- <center><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center>&nbsp;<p>&nbsp;<p> -->
        <!-- Mostramos Errores si los hubiera -->
<% if (request.getParameter("error")!=null && request.getParameter("error").equals("S")) {
              numero=(request.getParameter("numero")==null) ? "": request.getParameter("numero");
              ano=(request.getParameter("ano")==null) ? "": request.getParameter("ano");
              ofi=(request.getParameter("oficina")==null) ? "": request.getParameter("oficina");
%>
        <table class="recuadroErrors" width="400" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/></p>
                    <ul>
                        <li><fmt:message key='registre_no_existeix'/></li>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
        <p>&nbsp;<p>&nbsp;
   <%  } %>
   
   <%-- Fin de errores --%>
   	    <br/>
        <table align="center" border="1" class="bordeEntrada">
        <form id="busquedaForm" name="busquedaForm" action="ModiEntrada.jsp" method="post" onsubmit="return confirmaProceso()">
        <tr>
            <td style="border:0">
            &nbsp;
            </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;&nbsp;
                <fmt:message key='oficina'/>..............:
                &nbsp;&nbsp;
                <select name="oficina" id="oficina" > 
              <% escribeSelect(out, valores.buscarOficinas(usuario,"AE"), ofi);%>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0">
            &nbsp;<br/>
            </td>
        </tr>
        <tr>
        <td style="border:0">
            &nbsp;&nbsp;
            <fmt:message key='num_reg_any_entrada'/>..:
            &nbsp;&nbsp;
            <input onKeyPress="return goodchars(event,'0123456789')" type="text" name="numero" size="6" value="<%=numero%>">
            /
            <input onKeyPress="return goodchars(event,'0123456789')" type="text" name="any" size="4" value="<%=ano%>">
            &nbsp;
            <a href="javascript:abreRegistros()"><img border="0" src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar_entrades'/>"></a>
            &nbsp;&nbsp;
        </td>
        </tr>
        <tr>
            <td style="border:0">
            &nbsp;<br/>
            </td>
        </tr>
        <tr>
        <td style="border:0">
            <p align="center">
            <input type=submit value="<fmt:message key='cercar'/>">
            </p>
        </td>
        </tr>
        </form>
        </table>
<!--     </div> -->
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
 <script type="text/javascript">
          <!-- 
          function abreRegistros() {
                codOficina=document.getElementById('busquedaForm').oficina.options[document.getElementById('busquedaForm').oficina.selectedIndex].value;
                any=document.getElementById('busquedaForm').any.value;
                miRegistros=open("popup/listadoRegistrosEntradaAux.jsp?oficina="+codOficina+"&any="+any,"registros","scrollbars,resizable,width=600,height=400");
                miRegistros.focus();
           }
           function setRegistro(numeroEntrada, anoEntrada) {
                document.getElementById('busquedaForm').any.value=anoEntrada;
                document.getElementById('busquedaForm').numero.value=numeroEntrada;
           }

          function confirmaProceso() {
                 numeroRegistro=trim(document.getElementById('busquedaForm').numero.value);
                
                if (numeroRegistro=="" || numeroRegistro=="0") {
                  alert("Heu de introduir un nombre de registre ");
                  return false;
                }
                else {
                  return true;
                }
            } 
            -->
        </script>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
<script type="text/javascript">
	 var elFocus = document.getElementById('oficina');
	 elFocus.focus();
</script>
		
                 
		
    </body>
</html>
