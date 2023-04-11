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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="informe.indicadores"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->
        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="informe.indicadores"/></strong> </h3>
                    </div>
                    <div class="panel-body">
                        <form:form modelAttribute="informeIndicadoresBusquedaForm" method="post" cssClass="form-horizontal" name="informeIndicadoresBusquedaForm" onsubmit="return validaFormulario(this)">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="tipo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoLibro"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="regweb.tipoRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8 no-pad-right">
                                        <form:select path="tipo" cssClass="chosen-select">
                                            <form:option value="0" default="default"><spring:message code="informe.ambosTipos"/></form:option>
                                            <form:option value="1"><spring:message code="informe.entrada"/></form:option>
                                            <form:option value="2"><spring:message code="informe.salida"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="formato" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.formato"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="regweb.formato"/></label>
                                    </div>
                                    <div class="col-xs-8 no-pad-right">
                                        <form:select path="formato" cssClass="chosen-select">
                                            <form:option value="pdf" default="default"><spring:message code="regweb.formato.pdf" /></form:option>
                                            <form:option value="excel"><spring:message code="regweb.formato.excel"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicioInforme"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></label>
                                    </div>
                                    <div class="col-xs-8 no-pad-right" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaInicio" maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.finInforme"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></label>
                                    </div>
                                    <div class="col-xs-8 no-pad-right" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="campoCalendario" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.anyoMes"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.mostrar"/></label>
                                    </div>
                                    <div class="col-xs-8 no-pad-right">
                                        <form:select path="campoCalendario" cssClass="chosen-select">
                                            <form:option value="0" default="default"><spring:message code="informe.ambosCalendario"/></form:option>
                                            <form:option value="1"><spring:message code="informe.anys"/></form:option>
                                            <form:option value="2"><spring:message code="informe.mesos"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning  btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>

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
    if((fechaInicio)&&(fechaFin)&&(fechas)){
        return true;
    } else{
        return false;
    }
}

</script>

</body>
</html>