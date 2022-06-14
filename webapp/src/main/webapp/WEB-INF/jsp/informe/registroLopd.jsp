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

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="informe.registroLopd"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->
        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="informe.registroLopd"/> </strong></h3>
                    </div>
                    <div class="panel-body">
                        <form:form modelAttribute="registroLopdBusqueda" method="post" cssClass="form-horizontal" name="registroLopdBusqueda" onsubmit="return validaFormulario(this)">

                            <form:hidden path="pageNumber"/>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicio.registroLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaInicio" maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fin.registroLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="libro" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.libro.registroLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="libro.libro"/></label>
                                    </div>
                                    <div class="col-xs-8" id="libro">
                                        <form:select path="libro" items="${libros}" itemValue="id" itemLabel="libroOrganismo" cssClass="chosen-select"/>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="tipoRegistro" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoRegistro.registroLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="regweb.tipoRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <div>
                                            <form:select path="tipoRegistro" cssClass="chosen-select" multiple="false">
                                                <form:option path="tipoRegistro" value="1"><spring:message code="registroEntrada.registroEntrada"/></form:option>
                                                <form:option path="tipoRegistro" value="2"><spring:message code="registroSalida.registroSalida"/></form:option>
                                            </form:select>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="numeroRegistro" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numero.registroLopd"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8 no-pad-right" id="numeroRegistro">
                                        <form:input path="numeroRegistro" maxlength="256" cssClass="form-control"/>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning  btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>

                            <c:set var="errorInicio"><spring:message code="error.fechaInicio.posterior"/></c:set>
                            <input id="error1" type="hidden" value="${errorInicio}"/>
                            <c:set var="errorFin"><spring:message code="error.fechaFin.posterior"/></c:set>
                            <input id="error2" type="hidden" value="${errorFin}"/>
                            <c:set var="errorInicioFin"><spring:message code="error.fechaInicioFin.posterior"/></c:set>
                            <input id="error3" type="hidden" value="${errorInicioFin}"/>
                            <c:set var="errorNumeroRegistro"><spring:message code="error.numeroRegistro.noNumerico"/></c:set>
                            <input id="error4" type="hidden" value="${errorNumeroRegistro}"/>
                            <c:set var="errorLibro"><spring:message code="error.libro.seleccionado"/></c:set>
                            <input id="error5" type="hidden" value="${errorLibro}"/>

                        </form:form>

                    </div>
                </div>

                <c:if test="${registroLopdBusqueda.fechaInicio != null}">

                    <c:if test="${not empty paginacion.listado}">

                        <div class="row">
                            <div class="col-xs-12">

                                <!-- REGISTROS DE ENTRADA -->
                                <c:if test="${entradas}">

                                    <div class="panel panel-info">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                                <strong>
                                                    <spring:message code="registroEntrada.registroEntradas"/>
                                                </strong>
                                            </h3>
                                        </div>
                                        <div class="panel-body">

                                            <div class="col-xs-12">
                                                <div class="alert-grey">
                                                    <c:if test="${paginacion.totalResults == 1}">
                                                        <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                    </c:if>
                                                    <c:if test="${paginacion.totalResults > 1}">
                                                        <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                    </c:if>

                                                    <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                </div>

                                                <div class="table-responsive">
                                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="40">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                            <th><spring:message code="registroEntrada.anyRegistro"/></th>
                                                            <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                                            <th><spring:message code="registroEntrada.oficina"/></th>
                                                            <th></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="entrada" items="${paginacion.listado}" varStatus="status">
                                                            <tr>
                                                                <td>${entrada.numeroRegistro}</td>
                                                                <td><fmt:formatDate value="${entrada.fecha}" pattern="yyyy"/></td>
                                                                <td>${entrada.libro.nombreCompleto}</td>
                                                                <td><fmt:formatDate value="${entrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${entrada.oficina.denominacion}</td>
                                                                <td><a class="btn btn-info btn-sm" href="<c:url value="/informe/${entrada.id}/${idTipoRegistro}/informeRegistroLopd"/>" title="<spring:message code="regweb.ver"/>"><span class="fa fa-check"></span></a></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>

                                                    <!-- Paginacion -->
                                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                                        <c:param name="entidad" value="registroLopd"/>
                                                    </c:import>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- REGISTROS DE SALIDA -->
                                <c:if test="${salidas}">

                                    <div class="panel panel-danger">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                                <strong>
                                                    <spring:message code="registroSalida.registroSalidas"/>
                                                </strong>
                                            </h3>
                                        </div>
                                        <div class="panel-body">

                                            <div class="col-xs-12">
                                                <div class="alert-grey">
                                                    <c:if test="${paginacion.totalResults == 1}">
                                                        <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                                    </c:if>
                                                    <c:if test="${paginacion.totalResults > 1}"><spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalidas"/>
                                                    </c:if>

                                                    <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                </div>

                                                <div class="table-responsive">
                                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="40">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                                            <th><spring:message code="registroSalida.anyRegistro"/></th>
                                                            <th><spring:message code="registroSalida.libro.corto"/></th>
                                                            <th><spring:message code="registroSalida.fecha"/></th>
                                                            <th><spring:message code="registroSalida.oficina"/></th>
                                                            <th></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="salida" items="${paginacion.listado}" varStatus="status">
                                                            <tr>
                                                                <td>${salida.numeroRegistro}</td>
                                                                <td><fmt:formatDate value="${salida.fecha}" pattern="yyyy"/></td>
                                                                <td>${salida.libro.nombreCompleto}</td>
                                                                <td><fmt:formatDate value="${salida.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${salida.oficina.denominacion}</td>
                                                                <td><a class="btn btn-danger btn-sm" href="<c:url value="/informe/${salida.id}/${idTipoRegistro}/informeRegistroLopd"/>" title="<spring:message code="regweb.seleccionar"/>"><span class="fa fa-check"></span></a></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>

                                                    <!-- Paginacion -->
                                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                                        <c:param name="entidad" value="registroLopd"/>
                                                    </c:import>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                            </div>
                        </div>

                    </c:if>

                    <c:if test="${empty paginacion.listado}">
                        <div class="alert alert-grey alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            <spring:message code="regweb.busqueda.vacio"/> <strong>
                            <c:if test="${registroLopdBusqueda.tipoRegistro == 1}">
                                <spring:message code="registroEntrada.registroEntrada"/>
                            </c:if>
                            <c:if test="${registroLopdBusqueda.tipoRegistro == 2}">
                                <spring:message code="registroSalida.registroSalida"/>
                            </c:if>
                            </strong>
                        </div>
                    </c:if>

                </c:if>

            </div>
        </div>
        <!-- FIN BUSCADOR-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


<!-- VALIDADOR DE FORMULARI -->
<script type="text/javascript">

// Valida el formuario si las fechas Inicio y Fin son correctas
function validaFormulario(form) {
    var fechaInicio = true;
    var fechaFin = true;
    var fechas = true;
    var numeroRegistro = true;
    var libro = true;
    // Valida el formato de Fecha de Inicio
    if (!validaFecha(form.fechaInicio, 'fechaInicio')) {
        fechaInicio = false;
    }
    // Valida el formato de Fecha de Fin
    if (!validaFecha(form.fechaFin, 'fechaFin')) {
        fechaFin = false;
    }
    // Si las Fechas son correctas, Valida el Fecha Inicio y Fecha Fin menor o igual que fecha actual, Fecha Inicio menor o igual que Fecha Fin
    if((fechaInicio)&&(fechaFin)){
        if (!validaFechasConjuntas(form.fechaInicio, form.fechaFin, 'fechaInicio', 'fechaFin')) {
            fechas = false;
        }
    }
    // Valida que el valor de Número sea un entero
    if(!validaLibro(form.libro, 'libro')){
        libro = false;
    }
    // Valida que el valor de Número sea un entero
    if(!validaEntero(form.numeroRegistro.value, 'numeroRegistro')){
        numeroRegistro = false;
    }
    // Si todos los campos son correctos, hace el submit
    return (fechaInicio) && (fechaFin) && (fechas) && (numeroRegistro) && (libro);
}

</script>

</body>
</html>