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
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="informe.indicadoresOficina"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->


        <!-- BUSCADOR -->
        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="informe.indicadoresOficina"/></strong> </h3>
                    </div>
                    <div class="panel-body">
                        <form:form modelAttribute="informeIndicadoresOficinaBusquedaForm" method="post" cssClass="form-horizontal" name="informeIndicadoresOficinaBusquedaForm" onsubmit="return validaFormulario(this)">
                            <div class="row">
                                <div class="form-group col-xs-6 pad-left libros1">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="oficina"><span class="text-danger">*</span> <spring:message code="registroEntrada.oficina"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right" id="ofic">
                                        <form:select path="oficina" items="${oficinasInforme}" itemValue="id" itemLabel="denominacion" cssClass="chosen-select" multiple="false"/>
                                        <span id="oficinaErrors"></span>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6  pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="formato"><span class="text-danger">*</span> <spring:message code="regweb.formato"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right">
                                        <form:select path="formato" cssClass="chosen-select">
                                            <form:option value="pdf" default="default"><spring:message code="regweb.formato.pdf" /></form:option>
                                            <form:option value="excel"><spring:message code="regweb.formato.excel"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-xs-6  pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="fechaInicio"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaInicio" maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6  pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="fechaFin"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
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

                        </form:form>
                    </div>
                </div>
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
        // Si todos los campos son correctos, hace el submit
        return (fechaInicio) && (fechaFin) && (fechas);
    }

</script>

</body>
</html>