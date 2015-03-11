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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="informe.libroRegistro"/></li>
                </ol>
            </div>
        </div><!-- /.row -->


        <!-- BUSCADOR -->
            <div class="row">

                <div class="col-xs-12">

                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-search"></i><spring:message code="informe.libroRegistro"/> </h3>
                        </div>
                        <div class="panel-body">
                            <form:form modelAttribute="informeLibroBusquedaForm" method="post" cssClass="form-horizontal" name="informeLibroBusquedaForm" onsubmit="return validaFormulario(this)">
                                <div class="row">
                                    <div class="form-group col-xs-6 pad-left">
                                        <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                            <form:label path="tipo"><span class="text-danger">*</span> <spring:message code="informe.tipoLibro"/></form:label>
                                        </div>
                                        <div class="col-xs-9 no-pad-right">
                                            <form:select path="tipo" cssClass="chosen-select">
                                                <form:option value="0" default="default"><spring:message code="informe.entrada"/></form:option>
                                                <form:option value="1"><spring:message code="informe.salida"/></form:option>
                                            </form:select>
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
                                    <div class="form-group col-xs-6 pad-left libros1">
                                        <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                            <form:label path="libros"><span class="text-danger">*</span> <spring:message code="registroEntrada.libro"/></form:label>
                                        </div>
                                        <div class="col-xs-9 no-pad-right" id="libros">
                                            <c:if test="${fn:length(libros) eq 1}">
                                                <form:select path="libros" items="${libros}" itemValue="id" itemLabel="libroOrganismo" cssClass="chosen-select" multiple="false"/>
                                            </c:if>
                                                <c:if test="${fn:length(libros) gt 1}">
                                                <spring:message code="informe.libros" var="varLibros"/>
                                                <form:select data-placeholder="${varLibros}" path="libros" items="${libros}" itemValue="id" itemLabel="libroOrganismo" cssClass="chosen-select" multiple="true"/>
                                                </c:if>
                                            <span id="librosErrors"></span>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 pad-left campos1">
                                        <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                            <form:label path="campos"><span class="text-danger">*</span> <spring:message code="regweb.campos"/></form:label>
                                        </div>
                                        <div class="col-xs-9 no-pad-right" id="campos">
                                            <spring:message code="informe.campos" var="varCampos"/>
                                            <form:select data-placeholder="${varCampos}" multiple="true" cssClass="chosen-select" id="campos" path="campos" name="campos">
                                                <form:option value="llibr" selected="selected"><spring:message code="registroEntrada.libro"/></form:option>
                                                <form:option value="ofici" selected="selected"><spring:message code="registroEntrada.oficina"/></form:option>
                                                <form:option value="anyRe" selected="selected"><spring:message code="registroEntrada.anyRegistro"/></form:option>
                                                <form:option value="data" selected="selected"><spring:message code="registroEntrada.dataRegistre"/></form:option>
                                                <form:option value="numRe" selected="selected"><spring:message code="registroEntrada.numeroRegistro"/></form:option>
                                                <form:option value="extra" selected="selected"><spring:message code="registroEntrada.extracto"/></form:option>
                                                <form:option value="tipAs" selected="selected"><spring:message code="registroEntrada.tipoAsunto"/></form:option>
                                                <form:option value="nomIn" selected="selected"><spring:message code="registroEntrada.interesados"/></form:option>
                                                <form:option value="orgOr" selected="selected"><spring:message code="registroEntrada.oficinaOrigen"/></form:option>
                                                <form:option value="numOr" selected="selected"><spring:message code="registroEntrada.numeroRegistroOrigen"/></form:option>
                                                <form:option value="datOr" selected="selected"><spring:message code="registroEntrada.dataOrigen"/></form:option>
                                                <form:option value="orgDe" selected="selected"><spring:message code="registroEntrada.destinoOrigen"/></form:option>
                                                <form:option value="docFi" selected="selected"><spring:message code="registroEntrada.documentacionFisica"/></form:option>
                                                <form:option value="idiom" selected="selected"><spring:message code="registroEntrada.idioma"/></form:option>
                                                <form:option value="obser" selected="selected"><spring:message code="registroEntrada.observaciones"/></form:option>
                                                <form:option value="estat" selected="selected"><spring:message code="registroEntrada.estado"/></form:option>
                                                <form:option value="exped" selected="selected"><spring:message code="registroEntrada.expediente"/></form:option>
                                                <form:option value="codAs"><spring:message code="registroEntrada.codigoAsunto"/></form:option>
                                                <form:option value="refEx"><spring:message code="registroEntrada.referenciaExterna"/></form:option>
                                                <form:option value="trans"><spring:message code="registroEntrada.transporte"/></form:option>
                                                <form:option value="numTr"><spring:message code="registroEntrada.numTransporte"/></form:option>
                                            </form:select>
                                            <span id="camposErrors"></span>
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

//Valida los libros seleccionados (libros, nombre del libro)
function librosSeleccionados(select, camp) {
    var variable = '';
    var htmlBuit = '';
    // Valor de los libros
    var value = $(select).val();
    var numLibros = 0;
    if (value!=null && value!=""){
        // Número de los libros en el select
        numLibros = value.length;
    }
    // Si hay menos de un libro seleccionado, retorna error
    if (numLibros<1){
        variable = "#" + camp + " span#librosErrors";
        htmlBuit = "<span id='librosErrors' class='help-block'>És obligatori elegir almanco 1 llibre</span>";
        $(variable).html(htmlBuit);
        $(variable).parents(".libros1").addClass("has-error");
        $('ul.chosen-choices').css('border-color','#a94442');
        return false;
    }else{
        variable = "#" + camp + " span:contains('elegir')";
        $(variable).removeClass("help-block");
        $(variable).parents(".libros1").removeClass("has-error");
        htmlBuit = "<span id='librosErrors'></span>";
        $(variable).html(htmlBuit);
        $('ul.chosen-choices').css('border-color','#aaa');
        return true;
    }
}

//Valida los campos seleccionados (campos, nombre del campo)
function camposSeleccionados(select, camp) {
    var variable = '';
    var htmlBuit = '';
    // Valor de los campos
    var value = $(select).val();
    var numCampos = 0;
    if (value!=null && value!=""){
        // Número de los campos en el select
        numCampos = value.length;
    }
    // Si hay menos de dos campos seleccionados, retorna error
    if (numCampos<2){
        variable = "#" + camp + " span#camposErrors";
        htmlBuit = "<span id='camposErrors' class='help-block'>És obligatori elegir almanco 2 camps</span>";
        $(variable).html(htmlBuit);
        $(variable).parents(".campos1").addClass("has-error");
        $('ul.chosen-choices').css('border-color','#a94442');
        return false;
    }else{
        variable = "#" + camp + " span:contains('elegir')";
        $(variable).removeClass("help-block");
        $(variable).parents(".campos1").removeClass("has-error");
        htmlBuit = "<span id='camposErrors'></span>";
        $(variable).html(htmlBuit);
        $('ul.chosen-choices').css('border-color','#aaa');
        return true;
    }
}

// Valida el formuario si las fechas Inicio y Fin son correctas, hay almenos 2 campos seleccionados, hay un Libro seleccionado
function validaFormulario(form) {
    var fechaInicio = true;
    var fechaFin = true;
    var libros = true;
    var campos = true;
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
    // Valida los libros seleccionados
    if (!librosSeleccionados(form.libros, 'libros')){
        libros = false;
    }
    // Valida los campos seleccionados
    if (!camposSeleccionados(form.campos, 'campos')){
        campos = false;
    }
    // Si todos los campos son correctos, hace el submit
    if((fechaInicio)&&(fechaFin)&&(libros)&&(campos)&&(fechas)){
        return true;
    } else{
        return false;
    }
}

</script>

</body>
</html>