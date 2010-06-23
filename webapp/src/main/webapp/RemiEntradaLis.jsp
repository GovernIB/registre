<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<% 
ListadoRegOficiosEntradaFacade listado=ListadoRegOficiosEntradaFacadeUtil.getHome().create();

String usuario=request.getRemoteUser().toUpperCase();
String oficinaSel=request.getParameter("oficina");
String oficinaFisSel=request.getParameter("oficinafisica");
String parametros[] = {oficinaSel, oficinaFisSel};

session.setAttribute("listadoOficiosEntrada",parametros);
%>


<html>
<head><title><fmt:message key='registre_entrades'/></title>
    
    
    
    <script src="jscripts/TAO.js"></script>
        <script>
            function confirmaDescarta() {
                var cadena=prompt("<fmt:message key='esta_segur_anular_oficis_motius'/>","");
                if (cadena==null || cadena=="") {
                return false;
                }

        		var llistaElementsFormAU = document.getElementById("ofimul").elements;


        		for (var i=0; i<llistaElementsFormAU.length ;i++) {
        			//window.alert('Element '+i+' '+llistaElementsFormAU[i].name); 
        			if ( llistaElementsFormAU[i].name == "registre" ){
        				if (llistaElementsFormAU[i].checked) {
        	            	if (oficioDescartaForm.registre.value!="") oficioDescartaForm.registre.value += "|";
        	            	oficioDescartaForm.registre.value += llistaElementsFormAU[i].value;
        				}
        			}
        		}
                        
            	oficioDescartaForm.motius.value=cadena;
                
                oficioDescartaForm.submit();
            }
		</script>
    <script>

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
		var llista = document.getElementById("ofimul").getElementsByTagName(elementClickat.value);
		var llistaElementsFormAU = document.getElementById("ofimul").elements;

		for (var i=0; i<llistaElementsFormAU.length ;i++) {
			//window.alert('Element '+i+' '+llistaElementsFormAU[i].name); 
			if ( llistaElementsFormAU[i].name == elementClickat.value ){
				llistaElementsFormAU[i].checked=valor;
			}
		}
	}

	function validar() {
		/* Marcam tots els checkboxs de la columna */
		var valor = false;

		var llistaElementsFormAU = document.getElementById("ofimul").elements;

		var origen = "";
		var origenfisico = "";
		var destinatario = "";
		var mismoorigen = true;
		var mismoorigenfisico = true;
		var mismodestinatario = true;

		for (var i=0; i<llistaElementsFormAU.length ;i++) {
			//window.alert('Element '+i+' '+llistaElementsFormAU[i].name); 
			if ( llistaElementsFormAU[i].name == "registre" ){
				valor = valor || llistaElementsFormAU[i].checked;
				if (llistaElementsFormAU[i].checked) {
					if (origen == "") origen = document.getElementById("ori-"+llistaElementsFormAU[i].value).value;
					mismoorigen = mismoorigen && (origen == document.getElementById("ori-"+llistaElementsFormAU[i].value).value);
					if (origenfisico == "") origenfisico = document.getElementById("ofis-"+llistaElementsFormAU[i].value).value;
					mismoorigenfisico = mismoorigenfisico && (origenfisico == document.getElementById("ofis-"+llistaElementsFormAU[i].value).value);
					if (destinatario == "") destinatario = document.getElementById("dest-"+llistaElementsFormAU[i].value).value;
					mismodestinatario = mismodestinatario && (destinatario == document.getElementById("dest-"+llistaElementsFormAU[i].value).value);
				}
			}
		}
		if (!valor) {
			alert("Ha de seleccionar al menys un registre.");
			return false;
		}
		if (!mismodestinatario) {
			alert("Els registres han de tenir el mateix destinatari.");
			return false;
		}
		if (!mismoorigen) {
			alert("Els registres han de ser de la mateixa oficina.");
			return false;
		}
		if (!mismoorigenfisico) {
			alert("Els registres han de ser de la mateixa oficina fisica.");
			return false;
		}
		document.getElementById("ofimul").submit();
		return true;
	}
	
        var listaExtractos=new Array();
        var i=0;
        var copiado=false;
     
        function cargarDatos(cadena) {
        listaExtractos[i++]=cadena.toUpperCase();
        }
     
        function buscar() {
        var resultados=false;
        var cadena=prompt("Text a cercar en l'extracte","");
        if (cadena==null || trim(cadena)=="") {
        return;
        }
        
        enfocado=false;
        encontrados=0;
        for (var n=0;n < i; n++) {
        var elemento=listaExtractos[n];
        trId = "fila" + n;
        refId= "ref" + n;
        var fila=document.getElementById(trId);
        if (elemento.lastIndexOf(trim(cadena.toUpperCase()))>-1) {
        fila.style.background="#fff8a7"; 
        if (!enfocado) {
        document.getElementById(refId).focus();
        enfocado=true;
        }
        resultados=true;
        encontrados++;
        } else {
        if (n%2==0) {
        fila.style.background="";
        } else {
        fila.style.background="#DDDDFF";
        }
        }
        }
        if (!resultados) {
        alert("Text "+'"'+cadena+'"'+" no trobat dins l'extracte");
        } else {
        alert("Trobats "+encontrados+" registres amb el text "+'"'+cadena+'"'+" a l'extracte");
        }
        }
    </script>
</head>
<body>

     	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="RemiEntradaSel.jsp"><fmt:message key='consulta_registres_pendents_remisio'/></a></li>
		<li><fmt:message key='llistat_de_registres_pendents'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
<%
Vector registros=listado.recuperarRegistros(usuario,oficinaSel,oficinaFisSel);

if (registros.size()==0) { 
/* No hi ha cap element al llistat, eliminam el llistat de la sessió.*/
        	session.removeAttribute("listadoOficiosEntrada");
        	%>
<p><p>
<center><b><fmt:message key='no_shan_trobat_registres_que_compleixin_els_criteris_seleccionats'/></B></center>
     <% } else { %>
     
<table border="0" width="95%" align="center">
    <tr>
        <td align="left" width="33%">
        </td>
        <td align="center" width="33%">
            [&nbsp;<a href="RemiEntradaSel.jsp"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]
        </td>
        <td align="right" width="33%">
        </td>
    </tr>
</table>


<table width="100%" border=0>
    <tr>
        <td align="left">
            
        </td>
        <td align="right">
            <font color="green"><fmt:message key='recerca_paraules'/></font>
        </td>
    </tr>
</table>

<form name="ofimul" id="ofimul" action="RemiEntradaPaso.jsp">
<table width="100%" border="1">
    <tr>
    <td></td>
    <td><input type="checkbox" name="AEtots" id="Initots" value="registre" onclick="MarcaTot(event);"/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='data_ent'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='num_reg'/></TD>
    <td align="center" class="cabeceraTabla"><fmt:message key='oficina'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='data_doc'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='t_document'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='remitent'/>/<fmt:message key='procedencia_geografica'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='destinatari'/></td>
    <td width="25%" >
       <table width="100%" border="0">
       <tr>
           <td width="10%" class="cabeceraTabla">&nbsp;&nbsp;<fmt:message key='extracte'/></td>
           <td align="left">
               <a href="javascript: buscar()"> 
                   <img src="imagenes/buscar.gif" border=0  title="<fmt:message key='cercar_extracte'/>">
               </a>
           </td>
       </tr>
       </table>
    </td>
    </tr>
    <%   
    int hasta=registros.size();
    for (int i=0;i<hasta;i++) {
        RegistroSeleccionado reg=(RegistroSeleccionado)registros.get(i);
        
        String anoEntrada=reg.getAnoEntrada();
        String fechaEntrada=reg.getFechaES();
        String numeroEntrada=reg.getNumeroEntrada();
        String oficina=reg.getOficina();
        String textoOficina=reg.getDescripcionOficina();
        String oficinaFisica=reg.getOficinaFisica();
        String textoOficinaFisica=reg.getDescripcionOficinaFisica();
        String fechaDocumento=reg.getData();
        String remitente=reg.getDescripcionRemitente();
        String procedencia=reg.getDescripcionGeografico();
        String destinatario=reg.getDescripcionOrganismoDestinatario();
        String tipoDocumento=reg.getDescripcionDocumento();
        String claveRegistro=anoEntrada+"-"+numeroEntrada;
        String extracto=reg.getExtracto();
        boolean anulado=(reg.getRegistroAnulado().equals("") || reg.getRegistroAnulado().equals(" ")) ? false : true;
    %>
    <tr id="<%="fila"+i%>" class="<%=((i%2)==0)? "par":"impar"%>"> 
     
        <td>
        <a id="<%="ref"+i%>" href="RemiEntradaFicha.jsp?oficina=<%=oficina%>&numeroEntrada=<%=numeroEntrada%>&anoEntrada=<%=anoEntrada%>">
            <img src="imagenes/open24.gif" border=0  title="Veure document">
        </a>
        </td> 
        <td>
        <input type="checkbox" name="registre" value="<%=oficina%>-<%=numeroEntrada%>-<%=anoEntrada%>-<%=oficinaFisica %>">
        <input type="hidden" disabled name="destinatario" id="dest-<%=oficina%>-<%=numeroEntrada%>-<%=anoEntrada%>-<%=oficinaFisica %>" value="<%=destinatario %>">
        <input type="hidden" disabled name="origen" id="ori-<%=oficina%>-<%=numeroEntrada%>-<%=anoEntrada%>-<%=oficinaFisica %>" value="<%=oficina %>">
        <input type="hidden" disabled name="origenfisico" id="ofis-<%=oficina%>-<%=numeroEntrada%>-<%=anoEntrada%>-<%=oficinaFisica %>" value="<%=oficinaFisica %>">
        </td> 
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=fechaEntrada%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>" align="center"><%=numeroEntrada%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=oficina%>-<%=textoOficina%> (<%=textoOficinaFisica %>)</td>
        <td style="<%= (anulado) ? "color:red;" : "" %>" align="center"><%=fechaDocumento%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=tipoDocumento%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=remitente%> &nbsp;/&nbsp; <%=procedencia%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=destinatario%></td>
        <c:set var="texto" scope="page"><%=extracto%></c:set>
        <td style="<%= (anulado) ? "color:red;" : "" %>" width="25%"><c:out escapeXml="false" value="${texto}"/><script>cargarDatos("<c:out escapeXml="true" value="${texto}"/>");</script></td>
    </tr>
    <% }%>
</table>
</form>

        <%-- formulario --%>
    <div align="center">        
            <table border=0 width="50%">
                    <tr>
                        <td>
				            <!-- Boton de enviar -->          
                            <p align="center">
                                <input type="button" name="crearSortida" value="<fmt:message key='crear_ofici_remisio'/>"  onclick="return validar()">
                            </p>
                        </td>
                        <td>
                           <form name="oficioDescartaForm" action="RemiEntradaDescartarMultiple.jsp" method="post" >
				            <input type="hidden" name="registre" value="">
            				<input type="hidden" name="motius" value=""/>
                            <!-- Boton de enviar -->          
                            <p align="center">
                                <input type="button" name="descartar" value="<fmt:message key='descartar'/>" onclick="return confirmaDescarta()">
                            </P>
                            </form>
                        </td>

                    </tr>

            </table>
    </div>

<%
	/* Per maquetar quan només apareixen pocs
	   registres al llistat */
	if ( registros.size()<10 && registros.size()>0) {
		for (int i=6-registros.size();i>0;i--) {
            out.println("<p>&nbsp;</p>");
		}
	}
%>
&nbsp;<br>
<table border="0" width="95%" align="center">
    <tr>
        <td align="left" width="33%">
        </td>
        <td align="right" width="33%">
        </td>
    </tr>
</table>

<% } %>
 		
                 
   		
	</body>
</html>   