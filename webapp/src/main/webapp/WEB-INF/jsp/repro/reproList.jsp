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

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${entidadActiva.nombre}</a></li>
                    <li><i class="fa fa-user"></i> ${usuarioAutenticado.nombreCompleto}</li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="repro.listado"/></li>
                </ol>
            </div>
        </div><!-- /.row -->
        <div id="mensajes"></div>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="repro.listado"/></strong></h3>
                    </div>

                    <div class="panel-body">
                        <c:import url="../modulos/mensajes.jsp"/>

                        <c:if test="${empty listado}">
                            <div class="alert alert-warning alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="repro.repro"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty listado}">
                            <div class="alert-grey">
                                <c:if test="${paginacion.totalResults == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="repro.repro"/>
                                </c:if>
                                <c:if test="${paginacion.totalResults > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="repro.repros"/>
                                </c:if>

                                <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col>
                                        <col width="100">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="regweb.nombre"/></th>
                                        <th><spring:message code="repro.tipo"/></th>
                                        <th><spring:message code="regweb.activo"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="repro" items="${listado}">
                                        <tr>
                                            <td>${repro.nombre}</td>
                                            <td>
                                                <c:if test="${repro.tipoRegistro == 1}"><span class="label label-info"><spring:message code="registroEntrada.registroEntrada"/></span></c:if>
                                                <c:if test="${repro.tipoRegistro == 2}"><span class="label label-danger"><spring:message code="registroSalida.registroSalida"/></span></c:if>
                                            </td>
                                            <td>
                                                <c:if test="${repro.activo}">
                                                    <a class="label label-success" href="<c:url value="/repro/${repro.id}/cambiarEstado"/>" title="<spring:message code="repro.cambiar.estado"/>"><spring:message code="regweb.si"/></a>
                                                </c:if>
                                                <c:if test="${!repro.activo}">
                                                    <a class="label label-danger" href="<c:url value="/repro/${repro.id}/cambiarEstado"/>" title="<spring:message code="repro.cambiar.estado"/>"><spring:message code="regweb.no"/></a>
                                                </c:if>
                                            </td>
                                            <td class="center" width="210">
                                                <a class="btn btn-warning btn-sm"
                                                   href="<c:url value="/repro/${repro.id}/edit"/>"
                                                   title="<spring:message code="regweb.editar"/>"><span
                                                        class="fa fa-pencil"></span></a>
                                              <c:if test="${repro.orden != 1}">
                                                <a class="btn btn-success btn-sm" href="<c:url value="/repro/${repro.id}/subir"/>" title="<spring:message code="regweb.subir"/>"><span class="fa fa-arrow-up"></span></a>
                                              </c:if>
                                              <c:if test="${repro.orden == 1}">
                                                <a class="btn btn-success disabled btn-sm"  title="<spring:message code="regweb.subir"/>"><span class="fa fa-arrow-up"></span></a>
                                              </c:if>
                                              <c:if test="${repro.orden != fn:length(listado)}">
                                                <a class="btn btn-info btn-sm" href="<c:url value="/repro/${repro.id}/bajar"/>" title="<spring:message code="regweb.bajar"/>"><span class="fa fa-arrow-down"></span></a>
                                              </c:if>
                                              <c:if test="${repro.orden == fn:length(listado)}">
                                                <a class="btn btn-info disabled btn-sm" title="<spring:message code="regweb.bajar"/>" ><span class="fa fa-arrow-down"></span></a>
                                              </c:if>
                                                <a data-toggle="modal" role="button" href="#modalEnviarRepro" class="btn btn-warning btn-default btn-sm" title=" <spring:message code="regweb.enviar"/>" onclick="carregaRepro(${repro.id})"><span class="fa fa-external-link"></span></a>
                                                <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/repro/${repro.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                            </td>
                                        </tr>
                                    </c:forEach>


                                    </tbody>
                                </table>

                                <!-- Paginacion -->
                                <c:import url="../modulos/paginacion.jsp">
                                    <c:param name="entidad" value="repro"/>
                                </c:import>


                            </div>
                        </c:if>
                    </div>

                </div>
            </div>
        </div>

        <!-- MODAL ENVIAR REPRO -->
        <div id="modalEnviarRepro" class="modal fade bs-example-modal-lg" >
            <div class="modal-dialog modal-medio">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarModal()">x</button>
                        <h3 class="modal-title" id="myModalLabel2"><spring:message code="repro.enviar"/></h3>
                    </div>
                    <div class="modal-body" >
                        <form id="reproSelectForm" class="form-horizontal" action="${pageContext.request.contextPath}/repro/enviar" method="post" enctype="multipart/form-data">
                            <input type="hidden" id="idRepro" name="idRepro" value=""/>

                            <div class="form-group col-xs-12">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <label for="usuario"><span class="text-danger">*</span> <spring:message code="usuario.identificador"/></label>
                                </div>
                                <div class="col-xs-8" id="reproEnviar">
                                    <select id="usuario" name="usuario" class="chosen-select">
                                        <option value="-1">...</option>
                                        <c:forEach items="${usuariosEntidadList}" var="usuarioEntidad">
                                            <option value="${usuarioEntidad.id}">${usuarioEntidad.usuario.identificador}</option>
                                        </c:forEach>
                                    </select>
                                    <span class="errors"></span>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <input type="button" onclick="return validaFormularioEnviar(this)" title="<spring:message code="regweb.seleccionar"/>" value="<spring:message code="regweb.enviar"/>" class="btn btn-warning btn-sm">
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    //
    function carregaRepro(idRepro) {

        $('#usuario').val(-1);
        $('#idRepro').val(idRepro);

        var variable = "#reproEnviar span.errors";
        var htmlNormal = "<span id='reproEnviar.errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");

    }
</script>


<script type="text/javascript">

    // Valida el formuario Enviar si ha elegido un usuario
    function validaFormularioEnviar(form) {

        var idUsuario = $('#usuario').val();
        var variable = "#reproEnviar span.errors";
        var idRepro = $('#idRepro').val();

        if (idUsuario != -1) {

            var htmlNormal = "<span id='reproEnviar.errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");

            enviarRepro(idUsuario,idRepro);

        } else{

            var formatoHtml = "<span id='reproEnviar.errors' class='help-block'>S'ha de triar un dels usuaris</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            return false;
        }
    }
</script>

<script type="text/javascript">

    function enviarRepro(idUsuario,idRepro) {

        var url = $("#reproSelectForm").attr("action").concat('/'+idRepro+'/'+idUsuario);

        $.ajax({
            url: url,
            type: "POST",

            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function(result) {
            mensajeSuccess("#mensajes","S´ha realitzat correctament l'enviament de la repro.");
            }

        });

        // Ocultamos el modal de Enviar Repro
        clearForm("#reproSelectForm");
        $('#modalEnviarRepro').modal('hide');
    }
</script>

<script>
/**
* Limpia el formulario del Modal y los posibles mensajes de error
*/
function limpiarModal(){
    clearForm("#reproSelectForm");
    quitarErroresModal();
    $('#usuario').val(-1);
}

function quitarErroresModal(){
    var variable = "#usuario span.errors";
    var htmlNormal = "<span id='usuario.errors'></span>";
    $(variable).html(htmlNormal);
    $(variable).parents(".form-group").removeClass("has-error");
}

</script>

</body>
</html>