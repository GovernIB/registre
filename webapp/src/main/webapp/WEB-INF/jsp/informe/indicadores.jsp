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


        <!-- BUSCADOR -->
        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><spring:message code="informe.indicadores"/> </h3>
                    </div>
                    <div class="panel-body">
                        <form:form modelAttribute="informeIndicadoresBusquedaForm" method="post" cssClass="form-horizontal" name="informeIndicadoresBusquedaForm" onsubmit="return validaFormulario(this)">
                            <div class="row">
                                <div class="form-group col-xs-6 pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="tipo"><spring:message code="informe.tipoLibro"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right">
                                        <form:select path="tipo" cssClass="chosen-select">
                                            <form:option value="0" default="default"><spring:message code="informe.ambosTipos"/></form:option>
                                            <form:option value="1"><spring:message code="informe.entrada"/></form:option>
                                            <form:option value="2"><spring:message code="informe.salida"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6  pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="formato"><spring:message code="regweb.formato"/></form:label>
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
                                        <form:label path="fechaInicio"><spring:message code="informe.fechaInicio"/></form:label>
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
                                        <form:label path="fechaFin"><spring:message code="informe.fechaFin"/></form:label>
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
                                <div class="form-group col-xs-6  pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="campoCalendario"><spring:message code="informe.mostrar"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right">
                                        <form:select path="campoCalendario" cssClass="chosen-select">
                                            <form:option value="0" default="default"><spring:message code="informe.ambosCalendario"/></form:option>
                                            <form:option value="1"><spring:message code="informe.anys"/></form:option>
                                            <form:option value="2"><spring:message code="informe.mesos"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning"><spring:message code="regweb.buscar"/></button>
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