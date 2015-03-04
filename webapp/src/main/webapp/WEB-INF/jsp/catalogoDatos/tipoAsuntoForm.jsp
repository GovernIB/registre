<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb.utils.RegwebConstantes"/>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

            <!-- Miga de pan -->
           <div class="row">
                <div class="col-xs-12">
                    <ol class="breadcrumb">
                        <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${entidadActiva.nombre}</a></li>
                        <li><a href="<c:url value="/tipoAsunto/list"/>" ><i class="fa fa-globe"></i> <spring:message code="tipoAsunto.tipoAsunto"/></a></li>
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="tipoAsunto.editar"/> ${tipoAsunto.traduccion.nombre}</li>
                    </ol>
                </div>
           </div><!-- Fin miga de pan -->

            <div class="row">
                <div class="col-xs-12">
                    <form:form modelAttribute="tipoAsunto" method="post" cssClass="form-horizontal">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                           <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                              <strong>
                                  <c:if test="${not empty tipoAsunto.id}"><spring:message code="tipoAsunto.editar"/></c:if>
                                  <c:if test="${empty tipoAsunto.id}"><spring:message code="tipoAsunto.nuevo"/></c:if>
                              </strong>
                           </h3>
                        </div>

                        <!-- Formulario -->
                        <div class="panel-body">
                            
                                <form:hidden path="entidad.id"/>
                                <form:errors path="traducciones['es'].nombre" cssClass="has-error help-block" element="span"><span class="help-block-red"><spring:message code="regweb.traduccion.obligatorio"/></span></form:errors>

                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                        <form:label path="codigo"><span class="text-danger">*</span> <spring:message code="codigoAsunto.codigo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="codigo" cssClass="form-control"/> <form:errors path="codigo" cssClass="help-block" element="span"/>
                                    </div>
                                </div>

                                <div class="form-group col-xs-12">
                                    <div class="col-xs-2 pull-left etiqueta_regweb_left control-label">
                                     <form:label path="activo"><spring:message code="regweb.activo"/></form:label>
                                    </div>
                                    <div class="col-xs-10">
                                     <form:checkbox path="activo" value="true"/>
                                    </div>
                                </div>


                                <ul class="nav nav-tabs" id="myTab">
                                    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                        <li><a href="#${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" data-toggle="tab"><spring:message code="idioma.${idioma}"/></a></li>
                                    </c:forEach>
                                </ul>
                                <div id='content' class="tab-content">
                                    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                        <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                        <div class="tab-pane" id="${idioma_lang}">
                                            <div class="form-group col-xs-6">
                                                <div class="col-xs-4 pull-lef etiqueta_regweb control-label">
                                                    <form:label path="traducciones['${idioma_lang}'].nombre"><span class="text-danger">*</span>
                                                     <spring:message code="regweb.nombre"/></form:label>
                                                </div>
                                                <div class="col-xs-8">
                                                    <form:input path="traducciones['${idioma_lang}'].nombre" cssClass="form-control"/>
                                                    <form:errors path="traducciones['${idioma_lang}'].nombre" cssClass="help-block" element="span"/>
                                                </div>
                                            </div>
                                         </div>
                                    </c:forEach>
                                </div>



                        </div>
                    </div>
                    <!-- Botonera -->

                        <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>

                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/tipoAsunto/list"/>')" class="btn btn-sm">
                        <c:if test="${not empty tipoAsunto.id}">
                            <input type="button" value="<spring:message code="regweb.eliminar"/>" onclick='javascript:confirm("<c:url value="/tipoAsunto/${tipoAsunto.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' class="btn btn-danger btn-sm">
                        </c:if>

                    </form:form>
                </div>
            </div>

             <%--CodigosAsunto del TipoAsunto--%>
             <br/>
             <c:if test="${not empty tipoAsunto.id}">
             <div class="row">
               <div class="col-xs-12">
                   <div class="panel panel-success">
                       <div class="panel-heading">
                            <a href="#myModal" class="btn btn-success btn-xs pull-right" role="button" data-toggle="modal"><i class="fa fa-plus"></i> <spring:message code="codigoAsunto.nuevo"/></a>
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="tipoAsunto.codigosAsunto"/>: ${tipoAsunto.traduccion.nombre}</strong></h3>
                       </div>
                       <div class="panel-body">

                           <c:import url="../modulos/mensajes.jsp"/>

                            <c:if test="${empty tipoAsunto.codigosAsunto}">
                                <div class="alert alert-warning alert-dismissable">
                                   <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                   <strong><spring:message code="regweb.listado.vacio"/> <fmt:message key="codigoAsunto.codigoAsunto"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty tipoAsunto.codigosAsunto}">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col width="101">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><fmt:message key="regweb.nombre"/></th>
                                        <th><fmt:message key="regweb.acciones"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="codigoAsunto" items="${tipoAsunto.codigosAsunto}">
                                        <tr>
                                            <td><i:trad value="${codigoAsunto}" property="nombre"/></td>
                                            <td class="center">

                                               <%-- <a class="btn btn-warning" href="javascript:void(0);" onclick='showModalEditar("${codigoAsunto.id}", "<c:out value="${fn:escapeXml(codigoAsunto.traducciones['ca'].nombre)}" />", "<c:out value="${fn:escapeXml(codigoAsunto.traducciones['es'].nombre)}" />", "${codigoAsunto.codigo}")' title="Editar"><span class="fa fa-pencil"></span></a> --%>
                                                <a class="btn btn-warning" href="javascript:void(0);" onclick='showModalEditar("${codigoAsunto.id}", "<c:out value="${codigoAsunto.traducciones['ca'].nombre}" escapeXml="true"/>", "<c:out value="${codigoAsunto.traducciones['es'].nombre}" escapeXml="true"/>", "${codigoAsunto.codigo}")' title="Editar"><span class="fa fa-pencil"></span></a>
                                               <%-- <a class="btn btn-warning" href="javascript:void(0);" onclick='showModalEditar("${codigoAsunto.id}", "${fn:escapeXml(codigoAsunto.traducciones['ca'].nombre)}", "${fn:escapeXml(codigoAsunto.traducciones['es'].nombre)}", "${codigoAsunto.codigo}")' title="Editar"><span class="fa fa-pencil"></span></a>--%>
                                                <a class="btn btn-danger" title="Eliminar" onclick="confirm('<c:url value="/codigoAsunto/${codigoAsunto.id}/delete"/>', '<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>')" href="javascript:void(0);"><span class="fa fa-eraser"></span></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            </c:if>
                       </div>
                   </div><!--.panel panel-success-->
               </div> <!--.col-xs-12-->
             </div><!--row-->

            <%--Modal añadir CodigoAsunto--%>
            <div id="myModal" class="modal fade bs-example-modal-lg" >
                <div class="modal-dialog">
                   <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h3><spring:message code="codigoAsunto.nuevo"/></h3>
                        </div>

                        <div class="modal-body" >
                            <c:url value="/codigoAsunto/edit" var="formAction"/>
                            <form:form id="modal-form" modelAttribute="codigoAsunto" method="post" action="${formAction}" cssClass="form-horizontal" >
                                <form:hidden path="id"/>

                                <div class="panel panel-success">

                                   <div class="panel-heading">
                                       <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong>Dades codi Assumpte</strong></h3>
                                   </div>

                                   <div class="panel-body">

                                        <div class="form-group col-xs-6">
                                           <div class="col-xs-4 pull-left etiqueta_regweb_left control-label">
                                               <form:label path="codigo"><spring:message code="codigoAsunto.codigo"/></form:label>
                                           </div>
                                           <div class="col-xs-8">
                                               <form:input path="codigo" cssClass="form-control" maxlength="16"/> <form:errors path="codigo" cssClass="help-block" element="span"/>
                                           </div>
                                        </div>
                                        <div class="form-group col-xs-12">
                                        <ul class="nav nav-tabs" id="myTab2">
                                            <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                                <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                                <li><a href="#${idioma_lang}_modal" data-toggle="tab"><spring:message code="idioma.${idioma}"/></a></li>
                                            </c:forEach>
                                        </ul>


                                        <div id="myTabContent2" class="tab-content">
                                            <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                                <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                                <div class="tab-pane" id="${idioma_lang}_modal">
                                                    <br>
                                                    <div class="form-group col-xs-8">
                                                        <div class="col-xs-4 pull-lef etiqueta_regweb control-label">
                                                            <form:label path="traducciones['${idioma_lang}'].nombre"><spring:message code="regweb.nombre"/></form:label>
                                                        </div>
                                                        <div class="col-xs-8">
                                                            <form:input path="traducciones['${idioma_lang}'].nombre" cssClass="form-control"/>
                                                             <form:errors path="traducciones['${idioma_lang}'].nombre" cssClass="help-block" element="span"/>
                                                        </div>
                                                    </div>

                                                </div>
                                            </c:forEach>
                                        </div>
                                     </div>
                                   </div> <!-- /.panel body -->
                                </div> <!-- /.panel panel-info -->
                                <div class="form-actions">
                                   <input type="submit" value="<spring:message code="regweb.guardar"/>" class="btn btn-warning btn-sm" onclick="return validateModal()">
                                   <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/tipoAsunto/${tipoAsunto.id}/edit/"/>')" class="btn btn-default btn-sm">
                                </div>
                            </form:form>

                        </div>
                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="regweb.cerrar"/></button>
                        </div>
                   </div>
                </div>
            </div>

            </c:if>



    </div>
</div>


<c:import url="../modulos/pie.jsp"/>
<script>




function showModalEditar(id, nombreca, nombrees, codigo) {

    $('#id').val(id);
    
    <%--
    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
    <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
    </c:forEach>
    -->
    
    <%--  TODO  NO ES CORRECTE QUAN HI HAGI MES IDIOMES QUE ???? --%> 

    nombreca = nombreca.replace(/\"/g,'&quot;');
    nombreca = nombreca.replace(/'/g, "\\'");
    nombrees = nombrees.replace(/\"/g,'&quot;');
    nombrees = nombrees.replace(/'/g, "\\'");
    $('#modal-form #id').val(id);
    $("#modal-form #traducciones\\'ca\\'\\.nombre").val(nombreca);
    $("#modal-form #traducciones\\'es\\'\\.nombre").val(nombrees);
   // $('[name="traducciones[\'ca\'].nombre"]').val(unescapeHtml(nombreca));
   // $('[name="traducciones[\'es\'].nombre"]').val(nombrees);
    //$('[name="codigo"]').val(codigo);
    $("#modal-form #codigo").val(codigo);



    $('#myModal .modal-header h3').html("<spring:message code="codigoAsunto.editar"/>");
	$('#myModal').modal('show');
}

function showModalNuevo() {


    $('#modal-form #id').val("");
    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
    <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
    $("#modal-form #traducciones\\'${idioma_lang}\\'\\.nombre").val("");
    </c:forEach>

    $("#modal-form #codigo").val("");
    $('#myModal .modal-header h3').html("<spring:message code="codigoAsunto.nuevo"/>");
	$('#myModal').modal('show');
}

function validateModal() {
    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
    <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
    if ($("#modal-form input#traducciones\\'${idioma_lang}\\'\\.nombre").val() == '') {
        alert("<spring:message code="codigoAsunto.nombre.obligatorio"/>");
        return false;
    }
    </c:forEach>
    
    $('#modal-form').submit();
    return true;

}

function escapeHtml(unsafe) {
    return unsafe
         .replace(/&/g, "&amp;")
         .replace(/</g, "&lt;")
         .replace(/>/g, "&gt;")
         .replace(/"/g, "&quot;")
         .replace(/'/g, "&#039;");
 }

function unescapeHtml(safe) {
    return safe
         .replace("&amp;", /&/g)
         .replace("&lt;", /</g)
         .replace( "&gt;", />/g)
         .replace("&quot;", /"/g)
         .replace("&#039;", /'/g);
 }

</script>


</body>
</html>