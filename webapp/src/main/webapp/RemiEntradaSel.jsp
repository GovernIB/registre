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
    
    Vector oficinasfisicas = valores.buscarOficinasFisicas(usuario, "AS");

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


void escribeSelect2(javax.servlet.jsp.JspWriter out, String tipo, Vector valores, String referencia, String referencia2) throws java.io.IOException {

    for (int i=0;i<valores.size();i=i+3){
    	String ofi=valores.get(i).toString();
    	String codigo=valores.get(i+1).toString();
        String descripcion=valores.get(i+2).toString();
        out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) && ofi.equals(referencia2) ? "selected" : "")+">");
        if (tipo.equals("N")) {
            out.write(descripcion);
        } else {
            out.write(codigo+" - "+descripcion);
        }
        out.write("</option>\n");
    }
}
%>

<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        
        <script>
        var oficinasfisicasarray = new Array();
        <% for (int ii=0; oficinasfisicas!=null && ii<oficinasfisicas.size(); ii=ii+3) { %>
        	oficinasfisicasarray.push([<%= oficinasfisicas.get(ii).toString() %>, <%= oficinasfisicas.get(ii+1).toString() %>,"<%= oficinasfisicas.get(ii+2).toString() %>"]);
        <% } %>

        
        function refrescaFisica(){
           	fis=document.getElementById('oficinafisica').value;
           	oficina=document.getElementById('oficina').value;
           	options=document.getElementById('oficinafisica').options;
           	ofifis=document.getElementById('oficinafisica');
	   	
        	var seleccionado = ofifis.selectedIndex;

           	while(ofifis.hasChildNodes()){
           		ofifis.removeChild(ofifis.childNodes[0]);
           	}

       			opcion= new Option('00 - <fmt:message key='registro.todas_autorizadas'/>','');
       			opcion.value='';
       			opcion.selected=false;
       			options[options.length]=opcion;	
        	
            for(i=0; i<oficinasfisicasarray.length; i++) {
           		of = oficinasfisicasarray[i];
           		if(of[0] == oficina){
           			opcion= new Option(of[2],of[1]);
           			opcion.value=of[1];
           			opcion.selected=(fis==of[1]);
           			if (fis==of[1]) seleccionado = options.length;
           			options[options.length]=opcion;	
           		}
           	}
           	document.getElementById('oficinafisica').selectedIndex=seleccionado;
           }
        </script>
    </head>

    <body bgcolor="#FFFFFF">

    
  	<!-- Molla pa --> 
	<ul id="mollaPa">
	<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
	<li><fmt:message key='seleccionar_oficina_entrada'/></li>
	</ul>
	<!-- Fi Molla pa-->
		<br/>
		<br/>	
		<br/>
		<br/>
        <!-- Mostramos Errores si los hubiera -->
<% 
    if (request.getParameter("error")!=null) {
%>
        <table class="recuadroErrors" width="400" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/></p>
                    <ul>
                        <li><%=request.getParameter("error")%></li>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
        <p>&nbsp;</p><p>&nbsp;</p>
   <%  } %>
   
   <%-- Fin de errores --%>
   		<br/>
        <p>&nbsp;</p>
<!--   		<div id="RecuadreCentrat"> --> 
        <table  class="recuadroEntradas" align="center">
        <form name="busquedaForm" action="RemiEntradaLis.jsp" method="post">
        <tr>
            <td style="border:0">
            &nbsp;
            </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;&nbsp;
                <fmt:message key='oficina'/>:
                &nbsp;&nbsp;
                <select name="oficina" id="oficina" onchange="refrescaFisica()">
                    <option value="00">00 - <fmt:message key='registro.todas_autorizadas'/> </option>
              <% escribeSelect(out, valores.buscarOficinas(usuario,"AS"), "");%>
                </select>
                &nbsp;&nbsp;
            </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;&nbsp;
                <fmt:message key="oficina_fisica"/>:
                &nbsp;&nbsp;
                <select name="oficinafisica" id="oficinafisica">
                    <option value="">00 - <fmt:message key='registro.todas_autorizadas'/> </option>
                </select>
                &nbsp;&nbsp;
            </td>
        </tr>
        <tr>
            <td style="border:0">
            &nbsp;<br>
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
<!--        </div> -->
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
<script type="text/javascript">
	 var elFocus = document.getElementById('oficina');
	 elFocus.focus();
</script>
		
                 
		
    </body>
</html>
