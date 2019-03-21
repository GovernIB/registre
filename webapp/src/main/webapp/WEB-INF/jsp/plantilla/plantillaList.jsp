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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li><i class="fa fa-user"></i> ${loginInfo.usuarioAutenticado.nombreCompleto}</li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="plantilla.listado"/></li>
                </ol>
            </div>
        </div><!-- /.row -->
        <div id="mensajes"></div>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="plantilla.listado"/></strong></h3>
                    </div>

                    <div class="panel-body">
                        <c:import url="../modulos/mensajes.jsp"/>

                        <c:if test="${empty listado}">
                            <div class="alert alert-grey alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <strong><spring:message code="regweb.listado.vacio"/> <spring:message code="plantilla.plantilla"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty listado}">
                            <div class="alert-grey">
                                <c:if test="${paginacion.totalResults == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="plantilla.plantilla"/>
                                </c:if>
                                <c:if test="${paginacion.totalResults > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="plantilla.plantillas"/>
                                </c:if>

                                <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped tablesorter sorted_table">
                                    <colgroup>
                                        <col>
                                        <col>
                                        <col width="100">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="regweb.nombre"/></th>
                                        <th><spring:message code="plantilla.tipo"/></th>
                                        <th><spring:message code="regweb.activo"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:set var="orden" value="1"></c:set>
                                    <form id="cambiarOrdenForm" class="form-horizontal" action="${pageContext.request.contextPath}/plantilla" method="get" enctype="multipart/form-data">
                                      <c:forEach var="plantilla" items="${listado}">
                                        <tr id="fila${orden}">
                                            <td>${plantilla.nombre}</td>
                                            <td>
                                                <c:if test="${plantilla.tipoRegistro == 1}"><span class="label label-info"><spring:message code="registroEntrada.registroEntrada"/></span></c:if>
                                                <c:if test="${plantilla.tipoRegistro == 2}"><span class="label label-danger"><spring:message code="registroSalida.registroSalida"/></span></c:if>
                                            </td>
                                            <td>
                                                <input id="canviarEstat" type="hidden" value="${pageContext.request.contextPath}/plantilla"/>
                                                <div id="actiu${plantilla.id}">
                                                    <c:if test="${plantilla.activo}">
                                                        <button type="button" class="label label-success" onclick="canviarEstatPlantilla(${plantilla.id},${plantilla.activo})" title="<spring:message code="plantilla.cambiar.estado"/>"><spring:message code="regweb.si"/></button>
                                                    </c:if>
                                                    <c:if test="${!plantilla.activo}">
                                                        <button type="button" class="label label-danger" onclick="canviarEstatPlantilla(${plantilla.id},${plantilla.activo})" title="<spring:message code="plantilla.cambiar.estado"/>"><spring:message code="regweb.no"/></button>
                                                    </c:if>
                                                </div>
                                            </td>
                                            <td class="center" width="210">
                                              <a class="btn btn-warning btn-sm" href="<c:url value="/plantilla/${plantilla.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>

                                              <c:if test="${plantilla.orden != 1}">
                                                <button id="pujar${plantilla.id}" type="button" class="btn btn-success btn-sm" onclick="cambiarOrdenPlantilla(${plantilla.id},${orden},1,${fn:length(listado)})" title="<spring:message code="regweb.subir"/>"><span class="fa fa-arrow-up"></span></button>
                                              </c:if>
                                              <c:if test="${plantilla.orden == 1}">
                                                <button id="pujar${plantilla.id}" type="button" class="btn btn-success disabled btn-sm" onclick="cambiarOrdenPlantilla(${plantilla.id},${orden},1,${fn:length(listado)})" title="<spring:message code="regweb.subir"/>"><span class="fa fa-arrow-up"></span></button>
                                              </c:if>
                                              <c:if test="${plantilla.orden != fn:length(listado)}">
                                                <button id="baixar${plantilla.id}" type="button"  class="btn btn-info btn-sm" onclick="cambiarOrdenPlantilla(${plantilla.id},${orden},2,${fn:length(listado)})" title="<spring:message code="regweb.bajar"/>"><span class="fa fa-arrow-down"></span></button>
                                              </c:if>
                                              <c:if test="${plantilla.orden == fn:length(listado)}">
                                                <button id="baixar${plantilla.id}" type="button"  class="btn btn-info disabled btn-sm" onclick="cambiarOrdenPlantilla(${plantilla.id},${orden},2,${fn:length(listado)})" title="<spring:message code="regweb.bajar"/>" ><span class="fa fa-arrow-down"></span></button>
                                              </c:if>

                                              <a data-toggle="modal" role="button" href="#modalEnviarPlantilla" class="btn btn-warning btn-default btn-sm" title=" <spring:message code="regweb.enviar"/>" onclick="carregaPlantilla(${plantilla.id})"><span class="fa fa-external-link"></span></a>
                                              <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/plantilla/${plantilla.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                            </td>
                                        </tr>
                                        <c:set var="orden" value="${orden+1}"></c:set>
                                      </c:forEach>
                                    </form>

                                    </tbody>
                                </table>

                                <!-- Paginacion -->
                                <c:import url="../modulos/paginacion.jsp">
                                    <c:param name="entidad" value="plantilla"/>
                                </c:import>


                            </div>
                        </c:if>
                    </div>

                </div>
            </div>
        </div>

        <!-- MODAL ENVIAR PLANTILLA -->
        <div id="modalEnviarPlantilla" class="modal fade" >
            <div class="modal-dialog modal-medio">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarModal()">x</button>
                        <h3 class="modal-title" id="myModalLabel2"><spring:message code="plantilla.enviar"/></h3>
                    </div>
                    <div class="modal-body" >
                        <form id="plantillaSelectForm" class="form-horizontal" action="${pageContext.request.contextPath}/plantilla/enviar" method="post" enctype="multipart/form-data">
                            <input type="hidden" id="idPlantilla" name="idPlantilla" value=""/>

                            <div class="form-group col-xs-12">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <label for="usuario"><span class="text-danger">*</span> <spring:message code="usuario.identificador"/></label>
                                </div>
                                <div class="col-xs-8" id="plantillaEnviar">
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
    function carregaPlantilla(idPlantilla) {

        $('#usuario').val(-1);
        $('#idPlantilla').val(idPlantilla);

        var variable = "#plantillaEnviar span.errors";
        var htmlNormal = "<span id='plantillaEnviar.errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");

    }
</script>


<script type="text/javascript">

    // Valida el formuario Enviar si ha elegido un usuario
    function validaFormularioEnviar(form) {

        var idUsuario = $('#usuario').val();
        var variable = "#plantillaEnviar span.errors";
        var idPlantilla = $('#idPlantilla').val();

        if (idUsuario != -1) {

            var htmlNormal = "<span id='plantillaEnviar.errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");

            enviarPlantilla(idUsuario,idPlantilla);

        } else{

            var formatoHtml = "<span id='plantillaEnviar.errors' class='help-block'>S'ha de triar un dels usuaris</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            return false;
        }
    }
</script>

<script type="text/javascript">

    function enviarPlantilla(idUsuario,idPlantilla) {

        var url = $("#plantillaSelectForm").attr("action").concat('/'+idPlantilla+'/'+idUsuario);

        $.ajax({
            url: url,
            type: "POST",

            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function(result) {
            mensajeSuccess("#mensajes","S´ha realitzat correctament l'enviament de la plantilla.");
            }

        });

        // Ocultamos el modal de Enviar Plantilla
        clearForm("#plantillaSelectForm");
        $('#modalEnviarPlantilla').modal('hide');
    }
</script>

<script type="text/javascript">
    /**
     * Canvia l'estat de la Plantilla
     */
    function canviarEstatPlantilla(idPlantilla,actiu) {

        var url = $("#canviarEstat").val().concat('/'+idPlantilla+'/cambiarEstado');

        $.ajax({
            url: url,
            type: "GET",

            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function(result) {

                if(result == true) {

                    var botoActiu = "<button type=\"button\" class=\"label label-success\" onclick=\"canviarEstatPlantilla(" + idPlantilla + ",true)\" title=\"Si\"><spring:message code="regweb.si"/></button>";
                    var botoNoActiu = "<button type=\"button\" class=\"label label-danger\" onclick=\"canviarEstatPlantilla(" + idPlantilla + ",false)\" title=\"No\"><spring:message code="regweb.no"/></button>";

                    if (actiu) {
                        $("#actiu" + idPlantilla).html(botoNoActiu);
                    } else {
                        $("#actiu" + idPlantilla).html(botoActiu);
                    }
                    $("#mensajes").empty();
                    mensajeSuccess("#mensajes", "<spring:message code="aviso.plantilla.canviEstatOk"/>");

                } else if (result == false){
                    $("#mensajes").empty();
                    mensajeError("#mensajes", "<spring:message code="aviso.plantilla.canviEstatNok"/>");
                }

            }

        });

    }
</script>

<script type="text/javascript">
    /**
     * Ordena la Plantilla, sube o baja (accion=1 sube; accion=2 baja)
     */
    function cambiarOrdenPlantilla(idPlantilla,ordenActual,accion,filas) {

        var url = "";
        if(accion == 1) { // Sube orden
            url = $("#cambiarOrdenForm").attr("action").concat('/' + idPlantilla + '/subir');
        } else if(accion == 2){ // Baja orden
            url = $("#cambiarOrdenForm").attr("action").concat('/' + idPlantilla + '/bajar');
        }
        // Obtiene el orden de la fila anterior y la postarior de la actual
        var ordenSuperior = ordenActual - 1;
        var ordenInferior = ordenActual + 1;

        $.ajax({
            url: url,
            type: "GET",

            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            success: function(result) {

                if(result == true) { // Si la acción retorna éxito

                    if (accion == 1) {  // Sube orden
                        // Guardamos la fila que bajará y cambiamos los valores de sus javascript
                        var filaSuperior = $("#fila" + ordenSuperior).html();
                        var idPlantillaSuperior = filaSuperior.substring(filaSuperior.indexOf("cambiarOrdenPlantilla(") + 18, filaSuperior.indexOf("," + ordenSuperior + ",1," + filas));
                        filaSuperior = filaSuperior.replace("cambiarOrdenPlantilla(" + idPlantillaSuperior + "," + ordenSuperior + ",1," + filas + ")", "cambiarOrdenPlantilla(" + idPlantillaSuperior + "," + ordenActual + ",1," + filas + ")");
                        filaSuperior = filaSuperior.replace("cambiarOrdenPlantilla(" + idPlantillaSuperior + "," + ordenSuperior + ",2," + filas + ")", "cambiarOrdenPlantilla(" + idPlantillaSuperior + "," + ordenActual + ",2," + filas + ")");

                        // Cambiamos los valores del javascript que tendrá la fila que subimos
                        var filaActual = $("#fila" + ordenActual).html();
                        filaActual = filaActual.replace("cambiarOrdenPlantilla(" + idPlantilla + "," + ordenActual + ",1," + filas + ")", "cambiarOrdenPlantilla(" + idPlantilla + "," + ordenSuperior + ",1," + filas + ")");
                        filaActual = filaActual.replace("cambiarOrdenPlantilla(" + idPlantilla + "," + ordenActual + ",2," + filas + ")", "cambiarOrdenPlantilla(" + idPlantilla + "," + ordenSuperior + ",2," + filas + ")");

                        // Cambiamos las filas afectadas
                        $("#fila" + ordenSuperior).html(filaActual);
                        $("#fila" + ordenActual).html(filaSuperior);

                        if (ordenSuperior == 1) {  // Si al subir la fila queda la primera, desactivamos opción de subir y activamos la de la fila que baja
                            $("#pujar" + idPlantilla).addClass("disabled");
                            $("#fila" + ordenActual).find("button[id^='pujar']").removeClass("disabled");
                        }
                        if (ordenActual == filas) { // Si al subir la fila otra queda la última, activamos la opción de bajar y desactivamos la opción de bajar a la que baja
                            $("#baixar" + idPlantilla).removeClass("disabled");
                            $("#fila" + ordenActual).find("button[id^='baixar']").addClass("disabled");
                        }
                        if (ordenActual!=1 && ordenActual!=filas){ // Si és una fila central, tiene que tener las opciones activas
                            $("#fila" + ordenActual).find("button[id^='pujar']").removeClass("disabled");
                            $("#fila" + ordenActual).find("button[id^='baixar']").removeClass("disabled");
                        }

                    } else if (accion == 2) { // Baja orden
                        // Guardamos la fila que subirá y cambiamos los valores de sus javascript
                        var filaInferior = $("#fila" + ordenInferior).html();
                        var idPlantillaInferior = filaInferior.substring(filaInferior.indexOf("cambiarOrdenPlantilla(") + 18, filaInferior.indexOf("," + ordenInferior + ",1," + filas));
                        filaInferior = filaInferior.replace("cambiarOrdenPlantilla(" + idPlantillaInferior + "," + ordenInferior + ",1," + filas + ")", "cambiarOrdenPlantilla(" + idPlantillaInferior + "," + ordenActual + ",1," + filas + ")");
                        filaInferior = filaInferior.replace("cambiarOrdenPlantilla(" + idPlantillaInferior + "," + ordenInferior + ",2," + filas + ")", "cambiarOrdenPlantilla(" + idPlantillaInferior + "," + ordenActual + ",2," + filas + ")");

                        // Cambiamos los valores del javascript que tendrá la fila que bajamos
                        var filaActual = $("#fila" + ordenActual).html();
                        filaActual = filaActual.replace("cambiarOrdenPlantilla(" + idPlantilla + "," + ordenActual + ",1," + filas + ")", "cambiarOrdenPlantilla(" + idPlantilla + "," + ordenInferior + ",1," + filas + ")");
                        filaActual = filaActual.replace("cambiarOrdenPlantilla(" + idPlantilla + "," + ordenActual + ",2," + filas + ")", "cambiarOrdenPlantilla(" + idPlantilla + "," + ordenInferior + ",2," + filas + ")");

                        // Cambiamos las filas afectadas
                        $("#fila" + ordenInferior).html(filaActual);
                        $("#fila" + ordenActual).html(filaInferior);

                        if (ordenActual == 1) { // Si al bajar la fila otra queda la primera, desactivamos opción de subir y activamos la de la fila que baja
                            $("#pujar" + idPlantilla).removeClass("disabled");
                            $("#fila" + ordenActual).find("button[id^='pujar']").addClass("disabled");
                        }
                        if (ordenInferior == filas) { // Si al bajar la fila queda la última, activamos la opción de bajar y desactivamos la opción de bajar a la que baja
                            $("#baixar" + idPlantilla).addClass("disabled");
                            $("#fila" + ordenActual).find("button[id^='baixar']").removeClass("disabled");
                        }
                        if (ordenActual!=1 && ordenActual!=filas){ // Si és una fila central, tiene que tener las opciones activas
                            $("#fila" + ordenActual).find("button[id^='pujar']").removeClass("disabled");
                            $("#fila" + ordenActual).find("button[id^='baixar']").removeClass("disabled");
                        }
                    }

                    $("#mensajes").empty();
                    mensajeSuccess("#mensajes", "<spring:message code="aviso.plantilla.canviOrdreOk"/>");

                } else if (result == false){ // Si la acción retorna error
                    $("#mensajes").empty();
                    mensajeError("#mensajes", "<spring:message code="aviso.plantilla.canviOrdreNok"/>");
                }

            }

        });

    }
</script>

<script>
/**
* Limpia el formulario del Modal y los posibles mensajes de error
*/
function limpiarModal(){
    clearForm("#plantillaSelectForm");
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