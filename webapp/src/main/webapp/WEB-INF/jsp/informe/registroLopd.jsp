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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="informe.registroLopd"/></li>
                </ol>
            </div>
        </div><!-- /.row -->


        <!-- BUSCADOR -->
        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><spring:message code="informe.registroLopd"/> </h3>
                    </div>
                    <div class="panel-body">
                        <form:form modelAttribute="registroLopdBusquedaForm" method="post" cssClass="form-horizontal" name="registroLopdBusquedaForm" onsubmit="return validaFormulario(this)">
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
                                <div class="form-group col-xs-6 pad-left">
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
                            <div class="row">
                                <div class="form-group col-xs-6 pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="libro"><span class="text-danger">*</span> <spring:message code="libro.libro"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right">
                                        <form:select path="libro" items="${libros}" itemValue="id" itemLabel="nombre" cssClass="chosen-select"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="tipoRegistro"><span class="text-danger">*</span> <spring:message code="regweb.tipoRegistro"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right">
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
                            <div class="row">
                                <div class="form-group col-xs-6 pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="numeroRegistro"><spring:message code="registroEntrada.numeroRegistro"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right">
                                        <form:input path="numeroRegistro" maxlength="256" cssClass="form-control"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning"><spring:message code="regweb.buscar"/></button>
                            </div>
                        </form:form>

                    </div>
                </div>

                <c:if test="${(not empty entradas) || (not empty salidas)}">

                    <div class="row">
                        <div class="col-xs-12">

                            <!-- REGISTROS DE ENTRADA -->
                            <c:if test="${entradas != null}">

                                <!-- PARÁMETROS DE BÚSQUEDA -->
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                            <strong>
                                                <spring:message code="informe.registroLopd"/>
                                            </strong>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="col-xs-12"><strong><spring:message code="libro.libro"/>: ${libro.nombreCompleto}</strong></div>
                                        <div class="col-xs-12"><strong><spring:message code="informe.fechaInicio"/>: <fmt:formatDate value="${registroLopdBusquedaForm.fechaInicio}" pattern="dd/MM/yyyy"/></strong></div>
                                        <div class="col-xs-12"><strong><spring:message code="informe.fechaFin"/>: <fmt:formatDate value="${registroLopdBusquedaForm.fechaFin}" pattern="dd/MM/yyyy"/></strong></div>
                                        <div class="col-xs-12"><strong><spring:message code="regweb.tipoRegistro"/>: <spring:message code="registroEntrada.registroEntrada"/></strong></div>
                                        <c:if test="${registroLopdBusquedaForm.numeroRegistro != null}">
                                            <div class="col-xs-12"><strong><spring:message code="registroEntrada.numeroRegistro"/>: ${registroLopdBusquedaForm.numeroRegistro}</strong></div>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="panel panel-info">
                                    <div class="panel-heading">
                                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                            <strong>
                                                <spring:message code="registroEntrada.registroEntradas"/>
                                            </strong>
                                        </h3>
                                    </div>
                                    <div class="panel-body">

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
                                                <c:forEach var="entrada" items="${entradas}" varStatus="status">
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

                                        </div>

                                    </div>
                                </div>
                            </c:if>

                            <!-- REGISTROS DE SALIDA -->
                            <c:if test="${salidas != null}">

                                <!-- PARÁMETROS DE BÚSQUEDA -->
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                            <strong>
                                                <spring:message code="informe.registroLopd"/>
                                            </strong>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="col-xs-12"><strong><spring:message code="libro.libro"/>: ${libro.nombreCompleto}</strong></div>
                                        <div class="col-xs-12"><strong><spring:message code="informe.fechaInicio"/>: <fmt:formatDate value="${registroLopdBusquedaForm.fechaInicio}" pattern="dd/MM/yyyy"/></strong></div>
                                        <div class="col-xs-12"><strong><spring:message code="informe.fechaFin"/>: <fmt:formatDate value="${registroLopdBusquedaForm.fechaFin}" pattern="dd/MM/yyyy"/></strong></div>
                                        <div class="col-xs-12"><strong><spring:message code="regweb.tipoRegistro"/>: <spring:message code="registroSalida.registroSalida"/></strong></div>
                                        <c:if test="${registroLopdBusquedaForm.numeroRegistro != null}">
                                            <div class="col-xs-12"><strong><spring:message code="registroEntrada.numeroRegistro"/>: ${registroLopdBusquedaForm.numeroRegistro}</strong></div>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="panel panel-danger">
                                    <div class="panel-heading">
                                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                            <strong>
                                                <spring:message code="registroSalida.registroSalidas"/>
                                            </strong>
                                        </h3>
                                    </div>
                                    <div class="panel-body">

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
                                                <c:forEach var="salida" items="${salidas}" varStatus="status">
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

                                        </div>

                                    </div>
                                </div>
                            </c:if>

                        </div>
                    </div>

                </c:if>

                <c:if test="${(empty entradas) && (empty salidas)}">
                    <div class="alert alert-warning alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <spring:message code="regweb.busqueda.vacio"/> <strong>
                        <c:if test="${registroLopdBusquedaForm.tipoRegistro == 1}">
                            <spring:message code="registroEntrada.registroEntrada"/>
                        </c:if>
                        <c:if test="${registroLopdBusquedaForm.tipoRegistro == 2}">
                            <spring:message code="registroSalida.registroSalida"/>
                        </c:if>
                        </strong>
                    </div>
                </c:if>

            </div>
        </div>
        <!-- FIN BUSCADOR-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


<!-- VALIDADOR DE FORMULARI -->
<script type="text/javascript">

//Valida las fechas (fecha, nombre del campo)
function validaFecha(inputText, camp){
    var formatoFecha = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
    var variable = "#" + camp + " span.errors";
    var formatoHtml = "<span id='"+ camp +".errors' class='help-block'>El format no és correcte</span>";

    // Comprueba que la fecha tiene el formato adecuado (el día como máximo 31 y el mes 12)
    if(inputText.value.match(formatoFecha)){
        //Comprueba que los campos esten separados por una barra
        var opera1 = inputText.value.split('/');
        lopera1 = opera1.length;
        // Separa la fecha en día, mes y año
        if (lopera1>1){
            var pdate = inputText.value.split('/');
        }
        var dd  = parseInt(pdate[0]);
        var mm = parseInt(pdate[1]);
        var yy = parseInt(pdate[2]);
        // Crea lista de días máximos por cada mes
        var ListofDays = [31,28,31,30,31,30,31,31,30,31,30,31];
        // Comprueba si el mes es 2 (febrero)
        if (mm==1 || mm>2){
            // Si el valor del dia es mayor que el que marca la tabla de días, devuelve error
            if (dd>ListofDays[mm-1]){
                $(variable).html(formatoHtml);
                $(variable).parents(".form-group").addClass("has-error");
                return false;
            }
        }
        // Entra si el mes es febrero
        if (mm==2){
            var lyear = false;
            // Comprueba si el año es bisiesto
            if ( (!(yy % 4) && yy % 100) || !(yy % 400)){
                lyear = true;
            }
            // Retorna error si el año no es bisiesto y el día es mayor que 28
            if ((lyear==false) && (dd>=29)){
                $(variable).html(formatoHtml);
                $(variable).parents(".form-group").addClass("has-error");
                return false;
            }
            // Retorna error si el año es bisiesto y el día es mayor que 29
            if ((lyear==true) && (dd>29)){
                $(variable).html(formatoHtml);
                $(variable).parents(".form-group").addClass("has-error");
                return false;
            }
        }
        // Si el formato es correcto y el día concuerda con el mes elegido y el año, elimina campo de error
        var variable = "#" + camp + " span.errors";
        var htmlNormal = "<span id='"+ camp +".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        return true;
    } // Si el formato de la fecha no ese correcto, retorna error
    else{
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        return false;
    }
}

// Valida si una fecha es anterior a otra
function esAnterior(fechaInicio, fechaFin){
    var anoInicio = parseInt(fechaInicio.substring(6,10));
    var mesInicio = fechaInicio.substring(3,5);
    var diaInicio = fechaInicio.substring(0,2);
    var anoFin = parseInt(fechaFin.substring(6,10));
    var mesFin = fechaFin.substring(3,5);
    var diaFin = fechaFin.substring(0,2);

    if(anoFin > anoInicio){
        return true;
    }else{
        if (anoFin == anoInicio){
            if(mesFin > mesInicio)
                return true;
            if(mesFin == mesInicio)
                if(diaFin >= diaInicio)
                    return true;
                else
                    return false;
            else
                return false;
        }else
            return false;
    }
}

//Valida las fechas combinadas (fechaInicio, fechaFin, nombre del campo Inicio, nombre del campo Fin)
function validaFechasConjuntas(fechaInicio, fechaFin, campInicio, campFin){
    var posterior = false;
    var inicioCorrecta = false;
    var finCorrecta = false;

    // Dia actual
    var d = new Date();
    var diaActual = d.getDate();
    var mesActual = d.getMonth();
    var anoActual = d.getFullYear();
    mesActual = mesActual + 1;
    if(mesActual < 10){
        mesActual = "0" + mesActual;
    }
    if(diaActual< 10){
        diaActual = "0" + diaActual;
    }
    var fechaActual = diaActual + "/" + mesActual + "/" + anoActual;

    // Mira si la fecha Fin es posterior a la actual
    if(!esAnterior(fechaFin.value,fechaActual)){
        var variable = "#" + campFin + " span.errors";
        var formatoHtml = "<span id='"+ campFin +".errors' class='help-block'>La Data Fi no pot ser posterior l'actual</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        finCorrecta = false;
    }else{
        var variable = "#" + campFin + " span.errors";
        var htmlNormal = "<span id='"+ campFin +".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        finCorrecta = true;
    }

    // Mira si la fecha Inicio es posterior a la actual
    if(!esAnterior(fechaInicio.value,fechaActual)){
        var variable = "#" + campInicio + " span.errors";
        var formatoHtml = "<span id='"+ campInicio +".errors' class='help-block'>La Data <spring:message code="regweb.inicio"/> no pot ser posterior a l'actual</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        inicioCorrecta = false;
    }else{
        var variable = "#" + campInicio + " span.errors";
        var htmlNormal = "<span id='"+ campInicio +".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        inicioCorrecta = true;
    }

    // Comprueba si la fecha Inicio es anterior a la de Fin
    if(inicioCorrecta && finCorrecta){
        if(esAnterior(fechaInicio.value, fechaFin.value)){
            var variable = "#" + campInicio + " span.errors";
            var htmlNormal = "<span id='"+ campInicio +".errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");
            var variable2 = "#" + campFin + " span.errors";
            var htmlNormal2 = "<span id='"+ campFin +".errors'></span>";
            $(variable2).html(htmlNormal2);
            $(variable2).parents(".form-group").removeClass("has-error");
            posterior = true;
        }else{
            var variable = "#" + campInicio + " span.errors";
            var formatoHtml = "<span id='"+ campInicio +".errors' class='help-block'>La Data <spring:message code="regweb.inicio"/> no pot ser posterior a la Data Fi</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            var variable2 = "#" + campFin + " span.errors";
            var formatoHtml2 = "<span id='"+ campFin +".errors' class='help-block'>La Data <spring:message code="regweb.inicio"/> no pot ser posterior a la Data Fi</span>";
            $(variable2).html(formatoHtml2);
            $(variable2).parents(".form-group").addClass("has-error");
            posterior = false;
        }
    }

    // Si las fechas son anteriores o iguales a hoy, y la Inicio es menor o igual a la de Fin, retiorna true
    if((posterior) && (inicioCorrecta) && (finCorrecta)){
        return true;
    } else{
        return false;
    }
}


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