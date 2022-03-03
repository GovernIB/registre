<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

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
                        <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                        <li><a href="<c:url value="/tipoAsunto/list"/>" ><i class="fa fa-institution"></i> <spring:message code="tipoAsunto.tipoAsunto"/></a></li>
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="tipoAsunto.editar"/> ${tipoAsunto.traduccion.nombre}</li>
                    </ol>
                </div>
           </div><!-- Fin miga de pan -->

            <div class="row">
                <div class="col-xs-12">
                    <form:form modelAttribute="tipoAsunto" method="post" cssClass="form-horizontal">
                    <div class="panel panel-warning">
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

                            <div class="form-group col-xs-12 senseMargeLat">
                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="codigo"><span class="text-danger">*</span> <spring:message code="codigoAsunto.codigo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="codigo" cssClass="form-control"/> <form:errors path="codigo" cssClass="help-block" element="span"/>
                                    </div>
                                </div>

                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb_left control-label">
                                     <form:label path="activo"><spring:message code="regweb.activo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                     <form:checkbox path="activo"/>
                                    </div>
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

                                        <div class="form-group col-xs-12">
                                            <div class="form-group col-xs-6 senseMargeLat">
                                                <div class="col-xs-4 pull-lef etiqueta_regweb control-label textEsq">
                                                    <form:label path="traducciones['${idioma_lang}'].nombre"><span class="text-danger">*</span>
                                                     <spring:message code="regweb.nombre"/></form:label>
                                                </div>
                                                <div class="col-xs-8">
                                                    <form:input path="traducciones['${idioma_lang}'].nombre" cssClass="form-control"/>
                                                    <form:errors path="traducciones['${idioma_lang}'].nombre" cssClass="help-block" element="span"/>
                                                </div>
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
                   <div class="panel panel-warning">
                       <div class="panel-heading">
                            <a href="#myModal" class="btn btn-warning btn-xs pull-right" role="button" data-toggle="modal"><i class="fa fa-plus"></i> <spring:message code="codigoAsunto.nuevo"/></a>
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="tipoAsunto.codigosAsunto"/>: ${tipoAsunto.traduccion.nombre}</strong></h3>
                       </div>
                       <div class="panel-body">

                           <c:import url="../modulos/mensajes.jsp"/>

                            <c:if test="${empty tipoAsunto.codigosAsunto}">
                                <div class="alert alert-grey alert-dismissable">
                                   <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                   <strong><spring:message code="regweb.listado.vacio"/> <fmt:message key="codigoAsunto.codigoAsunto"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty tipoAsunto.codigosAsunto}">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col>
                                        <col>
                                        <col width="100">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="regweb.nombre"/></th>
                                        <th><spring:message code="codigoAsunto.codigo"/></th>
                                        <th><spring:message code="regweb.activo"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:set var="index" value="1"/>
                                    <c:forEach var="codigoAsunto" items="${tipoAsunto.codigosAsunto}">
                                        <tr>
                                            <td>${codigoAsunto.traducciones[pageContext.response.locale.language].nombre}</td>
                                            <td>${codigoAsunto.codigo}</td>
                                            <c:if test="${codigoAsunto.activo}"><td><span class="label label-success"><spring:message code="regweb.si"/></span></td></c:if>
                                            <c:if test="${not codigoAsunto.activo}"><td><span class="label label-danger"><spring:message code="regweb.no"/></span></td></c:if>
                                            <td class="center">
                                               <!-- Definimos la variable scope request para que sea visible en el javascript-->
                                               <c:set var="codAsunto" value="${codigoAsunto}" scope="request"/>
                                               <a class="btn btn-warning btn-sm" href="javascript:void(0);" onclick="showModalEditar('${codigoAsunto.id}')" title="Editar"><span class="fa fa-pencil"></span></a>
                                               <a class="btn btn-danger btn-sm" title="Eliminar" onclick="confirm('<c:url value="/codigoAsunto/${codigoAsunto.id}/delete"/>', '<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>')" href="javascript:void(0);"><span class="fa fa-eraser"></span></a>
                                            </td>
                                        </tr>

                                        <%--Creo el modal para la edición de los codigos Asunto--%>
                                        <div id="myModal_${codigoAsunto.id}" class="modal fade" >
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="goTo('<c:url value="/tipoAsunto/${tipoAsunto.id}/edit/"/>')">×</button>
                                                        <h3><spring:message code="codigoAsunto.editar"/></h3>
                                                    </div>

                                                    <div class="modal-body" >
                                                        <c:url value="/codigoAsunto/edit" var="formAction"/>
                                                        <form:form id="modal-form_${codigoAsunto.id}" modelAttribute="codigoAsunto" method="post" action="${formAction}" cssClass="form-horizontal">
                                                            <form:hidden path="id" value="${codAsunto.id}"/>

                                                            <div class="panel panel-success">

                                                                <div class="panel-heading">
                                                                    <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                                                        <strong><spring:message code="codigoAsunto.datos"/></strong></h3>
                                                                </div>

                                                                <div class="panel-body">

                                                                    <div class="form-group col-xs-12 senseMargeLat">
                                                                        <div class="form-group col-xs-6">
                                                                            <div class="col-xs-4 pull-left etiqueta_regweb_left control-label textEsq">
                                                                                <form:label path="codigo"><span class="text-danger">*</span> <spring:message code="codigoAsunto.codigo"/></form:label>
                                                                            </div>
                                                                            <div class="col-xs-8" id="cod_${codigoAsunto.id}">
                                                                                <form:input path="codigo" cssClass="form-control" maxlength="16" value="${codAsunto.codigo}"/><span class="errors"></span>
                                                                            </div>
                                                                        </div>

                                                                        <div class="form-group col-xs-6">
                                                                            <div class="col-xs-4 pull-left etiqueta_regweb_left control-label textEsq">
                                                                                <form:label path="activo"><spring:message code="regweb.activo"/></form:label>
                                                                            </div>
                                                                            <div class="col-xs-8">
                                                                                <form:checkbox path="activo" id="check_${codigoAsunto.id}_${codigoAsunto.activo}"/>
                                                                            </div>
                                                                        </div>
                                                                    </div>

                                                                    <div class="form-group col-xs-12" id="myTab2_${codigoAsunto.id}">
                                                                        <ul class="nav nav-tabs">
                                                                            <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                                                                <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                                                                <li id="a_${codigoAsunto.id}"><a href="#${idioma_lang}_modal_${codigoAsunto.id}" data-toggle="tab"><spring:message code="idioma.${idioma}"/></a></li>
                                                                            </c:forEach>
                                                                        </ul>

                                                                        <div id="myTabContent2_${codigoAsunto.id}" class="tab-content">
                                                                            <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                                                                <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                                                                <div class="tab-pane" id="${idioma_lang}_modal_${codigoAsunto.id}">

                                                                                    <div class="form-group col-xs-12">
                                                                                        <div class="form-group col-xs-6 senseMargeLat">
                                                                                            <div class="col-xs-4 pull-lef etiqueta_regweb control-label textEsq">
                                                                                                <form:label path="traducciones['${idioma_lang}'].nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                                                                            </div>
                                                                                            <div class="col-xs-8" id="nom_${idioma_lang}${codigoAsunto.id}">
                                                                                                <form:input path="traducciones['${idioma_lang}'].nombre" cssClass="form-control" value="${fn:escapeXml(codAsunto.traducciones[idioma_lang].nombre)}"/><span class="errors"></span>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>

                                                                                </div>
                                                                            </c:forEach>
                                                                        </div>
                                                                    </div>
                                                                </div> <!-- /.panel body -->
                                                            </div> <!-- /.panel panel-info -->
                                                            <div class="form-actions">
                                                                <input type="submit" value="<spring:message code="regweb.guardar"/>" class="btn btn-warning btn-sm" onclick="return validateModalCodAsunto(${codigoAsunto.id})">
                                                                <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/tipoAsunto/${tipoAsunto.id}/edit/"/>')" class="btn btn-default btn-sm">
                                                            </div>
                                                        </form:form>

                                                    </div>
                                                    <div class="modal-footer">
                                                        <button class="btn" data-dismiss="modal" aria-hidden="true" onclick="goTo('<c:url value="/tipoAsunto/${tipoAsunto.id}/edit/"/>')"><spring:message code="regweb.cerrar"/></button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>




                                        <c:set var="index" value="${index}+1"/>
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
            <div id="myModal" class="modal fade" >
                <div class="modal-dialog">
                   <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarModal()">×</button>
                            <h3><spring:message code="codigoAsunto.nuevo"/></h3>
                        </div>

                        <div class="modal-body" >
                            <c:url value="/codigoAsunto/new" var="formAction"/>
                            <form:form id="modal-form" modelAttribute="codigoAsunto" method="post" action="${formAction}" cssClass="form-horizontal" >
                                <form:hidden path="id"/>

                                <div class="panel panel-warning">

                                   <div class="panel-heading">
                                       <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="codigoAsunto.datos"/></strong></h3>
                                   </div>

                                   <div class="panel-body">

                                        <div class="form-group col-xs-6">
                                           <div class="col-xs-4 pull-left etiqueta_regweb_left control-label">
                                               <form:label path="codigo"><span class="text-danger">*</span> <spring:message code="codigoAsunto.codigo"/></form:label>
                                           </div>
                                           <div class="col-xs-8" id="cod">
                                               <form:input path="codigo" cssClass="form-control" maxlength="16"/><span class="errors"></span>
                                           </div>
                                        </div>
                                       <div class="form-group col-xs-6">
                                           <div class="col-xs-2 pull-left etiqueta_regweb_left control-label">
                                               <form:label path="activo"><spring:message code="regweb.activo"/></form:label>
                                           </div>
                                           <div class="col-xs-10">
                                               <form:checkbox path="activo" value="true"/>
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
                                                            <form:label path="traducciones['${idioma_lang}'].nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                                        </div>
                                                        <div class="col-xs-8" id="nom_${idioma_lang}">
                                                            <form:input path="traducciones['${idioma_lang}'].nombre" cssClass="form-control"/><span class="errors"></span>
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
                            <button class="btn" data-dismiss="modal" aria-hidden="true" onclick="limpiarModal()"><spring:message code="regweb.cerrar"/></button>
                        </div>
                   </div>
                </div>
            </div>

            </c:if>


    </div>
</div>


<c:import url="../modulos/pie.jsp"/>
<script>




function showModalEditar(codi){
    $('#a_'+codi).addClass("active");
    $('#ca_modal_'+codi).addClass("active");
    // Rutina per modificar el checkbox si el codAsunto esta desactivat (no funciona be al form)
    var input = "input[id*='check_"+codi+"']";
    var identificador = $(input).attr('id');
    var llevarString = "check_"+codi+"_";
    var activo = identificador.replace(llevarString,"");
    // Si no es activo posa el check a false
    if(activo=="false"){
        $('#check_'+codi+'_false').prop('checked',false);
    }
    // Mostra el modal
	$('#myModal_'+codi).modal('show');
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
    var nombreCorrecto = false;
    var traduccionesCorrecto = false;
    var noErrorTraducciones = true;
    var codigo = $("#modal-form input#codigo").val();
    // Comprueba si Codigo tiene algún valor e indica el posible error
    if (codigo == "") {
        var variable = "#cod span.errors";
        var formatoHtml = "<span id='cod.errors' class='help-block'>El camp és obligatori</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        nombreCorrecto = false;
    } else {
        var variable = "#cod span.errors";
        var htmlNormal = "<span id='cod.errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        nombreCorrecto = true;
    }
    // Comprueba si las Traducciones tienen algún valor e indica el posible error
    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
        <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
        if ($("#modal-form input#traducciones\\'${idioma_lang}\\'\\.nombre").val() == "") {
            var variable = "#nom_${idioma_lang} span.errors";
            var formatoHtml = "<span id='nom_${idioma_lang}.errors' class='help-block'>El camp és obligatori</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            traduccionesCorrecto = false;
            noErrorTraducciones = false;
        } else {
            var variable = "#nom_${idioma_lang} span.errors";
            var htmlNormal = "<span id='nom_${idioma_lang}.errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");
            traduccionesCorrecto = true;
        }
    </c:forEach>
    // Si no hay errores hace el submit
    if ((nombreCorrecto)&&(traduccionesCorrecto)&&(noErrorTraducciones)) {
        $('#modal-form').submit();
    }else{
        return false;
    }
}


function validateModalCodAsunto(idCodAsunto) {
    var nombreCorrecto = false;
    var traduccionesCorrecto = false;
    var noErrorTraducciones = true;
    var codigo = $('#cod_'+idCodAsunto+' input#codigo').val();
    // Comprueba si Codigo tiene algún valor e indica el posible error
    if (codigo == "") {
        var variable = "#cod_"+idCodAsunto+" span.errors";
        var formatoHtml = "<span id='cod.errors' class='help-block'>El camp és obligatori</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        nombreCorrecto = false;
    } else {
        var variable = "#cod_"+idCodAsunto+" span.errors";
        var htmlNormal = "<span id='cod.errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        nombreCorrecto = true;
    }
    // Comprueba si las Traducciones tienen algún valor e indica el posible error
    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
        <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
        var id_lang = "<c:out value='${idioma_lang}'/>";
        var tradId = "#nom_"+id_lang+idCodAsunto+" input";
        var traduccioIdioma = $(tradId).val();
        if (traduccioIdioma == "") {
            var variable = "#nom_"+id_lang+idCodAsunto+" span.errors";
            var formatoHtml = "<span id='cod.errors' class='help-block'>El camp és obligatori</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            traduccionesCorrecto = false;
            noErrorTraducciones = false;
        } else {
            var variable = "#nom_"+id_lang+idCodAsunto+" span.errors";
            var htmlNormal = "<span id='nom_${idioma_lang}.errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");
            traduccionesCorrecto = true;
        }
    </c:forEach>
    // Si no hay errores hace el submit
    if ((nombreCorrecto)&&(traduccionesCorrecto)&&(noErrorTraducciones)) {
        $('#modal-form_'+idCodAsunto).submit();
    }else{
        return false;
    }
}

/**
 * Sustituye caracteres por su simbolo
 */
function unescapeHtml(safe) {
    return safe
        .replace("&amp;","&")
        .replace("&#034;","\"")
        .replace("&#039;", "\'")
        .replace("&lt;", "<")
        .replace("&quot;","\"")
        .replace("&gt;", ">");
 }

/**
 * Limpia el formulario del Modal y los posibles mensajes de error
 */
function limpiarModal(){
    clearForm("#modal-form");
    quitarErroresModal();
    $('#cod').find("input").val(null);
    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
        <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
        $("#modal-form input#traducciones\\'${idioma_lang}\\'\\.nombre").val(null);
    </c:forEach>
}

<%--/**--%>
 <%--* Limpia el formulario del Modal de Edición de Codigo Asunto y los posibles mensajes de error--%>
 <%--*/--%>
<%--function limpiarModalCodAsunto(idCodAsunto){--%>
    <%--alert("limpiar: " + idCodAsunto);--%>
    <%--clearForm("#modal-form_"+idCodAsunto);--%>
    <%--quitarErroresModalCodAsunto(idCodAsunto);--%>
    <%--var codigo = "#cod_"+idCodAsunto+" input#codigo";--%>
    <%--var codigoOrigen = $(codigo).val();--%>
    <%--alert("ponemos valor origen: " + codigoOrigen);--%>
    <%--$(codigo).val(codigoOrigen);--%>
    <%--<c:forEach items="${idiomas}" var="idioma" varStatus="index">--%>
        <%--<c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />--%>
        <%--var id_lang = "<c:out value='${idioma_lang}'/>";--%>
        <%--var tradId = "#nom_"+id_lang+idCodAsunto+" input";--%>
        <%--$(tradId).val($(tradId).val());--%>
    <%--</c:forEach>--%>
<%--}--%>

function quitarErroresModal(){
    var variable = "#cod span.errors";
    var htmlNormal = "<span id='cod.errors'></span>";
    $(variable).html(htmlNormal);
    $(variable).parents(".form-group").removeClass("has-error");
    <c:forEach items="${idiomas}" var="idioma" varStatus="index">
        <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
        variable = "#nom_${idioma_lang} span.errors";
        htmlNormal = "<span id='nom_${idioma_lang}.errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
    </c:forEach>
}

<%--function quitarErroresModalCodAsunto(idCodAsunto){--%>
    <%--alert("quitar errores: " + idCodAsunto);--%>
    <%--var variable = "#cod_"+idCodAsunto+" span.errors";--%>
    <%--var htmlNormal = "<span id='cod.errors'></span>";--%>
    <%--$(variable).html(htmlNormal);--%>
    <%--$(variable).parents(".form-group").removeClass("has-error");--%>
    <%--<c:forEach items="${idiomas}" var="idioma" varStatus="index">--%>
        <%--<c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />--%>
        <%--var id_lang = "<c:out value='${idioma_lang}'/>";--%>
        <%--alert("quitar errores: " + id_lang);--%>
        <%--variable = "#nom_"+id_lang+idCodAsunto+" span.errors";--%>
        <%--htmlNormal = "<span id='nom_"+id_lang+".errors'></span>";--%>
        <%--$(variable).html(htmlNormal);--%>
        <%--$(variable).parents(".form-group").removeClass("has-error");--%>
    <%--</c:forEach>--%>
<%--}--%>

</script>


</body>
</html>