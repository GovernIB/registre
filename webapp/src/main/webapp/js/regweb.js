$(document).ready(function() {

    // Muestra los errores en los formularios
    $(".help-block").parents(".form-group").each(function(){
        $(this).addClass("has-error");
    });

    // Activa las Pestañas

    $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
        });
    $('#myTab a:first').tab('show');

    $('#myTab2 a').click(function (e) {
          e.preventDefault();
          $(this).tab('show');
          });
    
    $('#myTab2 a:first').tab('show');


    // Menú _Hover bootstrap-dropdown-on-hover-plugin.js 
    $('[data-toggle="dropdown"]').dropdownHover({delay: 50});

    // JS Code to add Submenus in BS 3:
    $("li.dropdown").mouseover(function (e) {
        if ( $(this).find(".dropdown-submenu-left") ) {
            var elm = $(this).find("a");
            var off = elm.offset();
            var l = off.left;
            var w = elm.width();
            var m = 30
            var docW = $(".container").width();

            var isEntirelyVisible = (l + w + m <= docW);

            if ( ! isEntirelyVisible ) {
                $(this).find(".dropdown-submenu-left").addClass('toggle-left');
                elm.find(".chevron-right").hide();
                elm.find(".chevron-left").show();
            } else {
                elm.find(".chevron-left").hide();
            }
        }
    });

    // Select autocomplete
    $(function () {

        var config = {
            '.chosen-select'           : {width:"100%"},
            '.chosen-select-deselect'  : {allow_single_deselect:true},
            '.chosen-select-no-single' : {disable_search_threshold:10},
            '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
            '.chosen-select-width'     : {width:"95%"}
        }
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }
    });

    $("[rel='ayuda']").popover({ trigger: "hover",placement: "bottom",container:"body", html:true});

});



function confirmRW(url, mensaje) {
    confirm(url, mensaje);
}

// Muestra el cuadro de confirmación para realizar una acción sobre un registro
function confirm(url, mensaje) {

    var confirmModal = 
      $("<div class=\"modal fade\">" +
          "<div class=\"modal-dialog\">" +
            "<div class=\"modal-content\">"+
              "<div class=\"modal-header\">" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
                "<h4 class=\"modal-title\">Confirmar</h4>" +
              "</div>" +

          "<div class=\"modal-body\">" +
            "<p>"+mensaje+"</p>" +
          "</div>" +

          "<div class=\"modal-footer\">" +
              "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>"+
              "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>"+
          "</div>" +
          "</div>" +
          "</div>" +
        "</div>");

    confirmModal.find("#okButton").click(function(event) {
      document.location.href=url;
	  confirmModal.modal("hide");
    });

    confirmModal.modal("show");
}

// Muestra el cuadro de confirmación para realizar una acción sobre un registro
function cerrar(mensaje) {

    var confirmModal =
        $("<div class=\"modal fade\">" +
        "<div class=\"modal-dialog\">" +
        "<div class=\"modal-content\">"+
        "<div class=\"modal-header\">" +
        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
        "<h4 class=\"modal-title\">Confirmar</h4>" +
        "</div>" +

        "<div class=\"modal-body\">" +
        "<p>"+mensaje+"</p>" +
        "</div>" +

        "<div class=\"modal-footer\">" +
        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>"+
        "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>"+
        "</div>" +
        "</div>" +
        "</div>" +
        "</div>");

    confirmModal.find("#okButton").click(function(event) {
        confirmModal.modal("hide");
    });

    confirmModal.modal("show");
}


function goTo(url) {
    document.location.href=url;
}

function imprimirRecibo(url) {

    var idModelo = $('#id').val();
    var url2=url.concat(idModelo);

    document.location.href=url2;
}

function registrarPreRegistro(url) {

    var idLibro = $('#id').val();
    var url2=url.concat(idLibro);

    document.location.href=url2;
}

/**
 * Añade un mensaje de error con el texto indicado en el div con el id indicado
 * @param id
 * @param texto
 */
function mensajeError(id,texto){
 var mensaje = "<div class=\"alert alert-danger alert-dismissable\">"+
    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>"+
    "<strong>"+texto+"</strong> </div>";

    $(id).append(mensaje);
}

/**
 * Añade un mensaje de info con el texto indicado en el div con el id indicado
 * @param id
 * @param texto
 */
function mensajeSuccess(id,texto){
    var mensaje = "<div class=\"alert alert-success alert-dismissable\">"+
        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>"+
        "<strong>"+texto+"</strong> </div>";

    $(id).append(mensaje);
}


/**
 * Carga de valores un Select
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param idEntidad id del campo select donde cargar los datos obtenidos
 * @param valorSelected Valor seleccionado en el select, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function cargarSelect(url,/*idEntidad,*/idSelect, valorSelected, todos){
    //alert( "cargarSelect " + idSelect);
    var html = '';
    jQuery.ajax({
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
       /* data: { codigo: idEntidad },*/
        success: function(result) {
            if(todos){html = '<option value="-1">...</option>';}
            var len = result.length;
            var selected='';
            for ( var i = 0; i < len; i++) {
                selected='';
                if(valorSelected != null && result[i].codigo == valorSelected){
                    selected = 'selected="selected"';
                }
                html += '<option '+selected+' value="' + result[i].codigo + '">'
                    + result[i].denominacion + '</option>';
            }
            html += '</option>';

            if(len != 0){
                $(idSelect).html(html);
            }else if(len==0){
                $(idSelect).attr("disabled","disabled");
                var html='';
                $(idSelect).html(html);
            }
            $(idSelect).trigger("chosen:updated");
        }
    });


}


/**
 * Carga de valores un Select dependiente de otro
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param seleccion valor seleccionado en el Select principal
 * @param valorSelected Valor seleccionado en el select dependiente, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function actualizarSelect(url, idSelect, seleccion, valorSelected, todos){
    var html = '';
    if(seleccion != '-1'){
        jQuery.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '<option value="-1">...</option>';}
                var len = result.length;
                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(valorSelected != null && result[i].id == valorSelected){
                        selected = 'selected="selected"';
                        }
                    html += '<option '+selected+' value="' + result[i].id + '">'
                        + result[i].nombre + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    var html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }
            }
        });

    }else{
        var html='';
        $(idSelect).html(html);
        $(idSelect).attr("disabled",true).trigger("chosen:updated");
    }

}

/**
 * Carga de valores un Select dependiente de otro pero muestra los campos codigo y denominacion.
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param seleccion valor seleccionado en el Select principal
 * @param valorSelected Valor seleccionado en el select dependiente, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function actualizarSelect2(url, idSelect, seleccion, valorSelected, todos){
    var html = '';
    if(seleccion != '-1'){
        jQuery.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '<option value="-1">...</option>';}
                var len = result.length;

                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(valorSelected != null && result[i].codigo == valorSelected){
                        selected = 'selected="selected"';
                        }
                    html += '<option '+selected+' value="' + result[i].codigo + '">'
                        + result[i].denominacion + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    var html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }
            }
        });

    }else{
        var html='';
        $(idSelect).html(html);
        $(idSelect).attr("disabled",true).trigger("chosen:updated");
    }

}



/**
 * Carga de valores un Select dependiente de otro incluyendo la opción todos (...)
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param seleccion valor seleccionado en el Select principal
 * @param valorSelected Valor seleccionado en el select dependiente, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function actualizarSelectTodos(url, idSelect, seleccion, valorSelected, todos){
    var html = '';
    if(seleccion != '-1'){
        jQuery.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '<option value="-1">...</option>';}
                var len = result.length;
                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(valorSelected != null && result[i].id == valorSelected){
                        selected = 'selected="selected"';
                    }
                    html += '<option '+selected+' value="' + result[i].id + '">'
                        + result[i].nombre + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    var html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }


            }
        });

    }else{
        var html='<option value="-1">...</option>';
        $(idSelect).html(html);
        $(idSelect).attr("disabled",true).trigger("chosen:updated");
    }

}

/**
 * Carga de valores un Select dependiente de otro
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param seleccion valor seleccionado en el Select principal
 * @param valorSelected Valor seleccionado en el select dependiente, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function actualizarSelectTraduccion(url, idSelect, seleccion, valorSelected, todos){
    var html = '';
    $(idSelect).attr("disabled",true).trigger("chosen:updated");
    if(seleccion != '-1'){
        jQuery.ajax({
            async: false,
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '<option value="-1">...</option>';}
                var len = result.length;
                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(valorSelected != null && result[i].id == valorSelected){
                        selected = 'selected="selected"';
                    }
                    html += '<option '+selected+' value="' + result[i].id + '">'
                        + result[i].traduccion.nombre + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }
            }
        });

    }else{
        html='';
        $(idSelect).html(html);
        $(idSelect).attr("disabled",true).trigger("chosen:updated");
    }

}

/**
 * Carga de valores una tabla
 * @param url donde hacer la petición ajax
 * @param idTabla id de la tabla  donde cargar los datos obtenidos
 * @param denominacion campo de la denominacion del organismo
 */
function cargarTabla(url, idTabla, denominacion){

    var html = '';
    html += '<table class="table table-bordered table-hover table-striped tablesorter">';
    html += '<colgroup>';
    html += '<col>';
    html += '<col width="100">';
    html += '</colgroup>';
    html += '<thead>';
    html += '<tr>';
    html += '<th>NOMBRE</th>';
    html += '<th>ACCIONES</th>';
    html += '</tr>';
    html += '</thead>';
    html += ' <tbody>';
    html += ' <tr>';

    jQuery.ajax({
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: { denominacion: denominacion },
        success: function(result) {
            var len = result.length;
            for ( var i = 0; i < len; i++) {

              html += '<td> FILA </td>';


            }
            html += '</tr>';
            html += '</tbody>';
            html += '</table>';


            if(len != 0){
                $(idTabla).attr("display:block");
                $(idTabla).html(html);
            }else if(len==0){
                $(idTabla).attr("display:none");
                var html='';
                $(idTabla).html(html);
            }

        }
  }) ;

}

/**
*Función que añade el externo al select indicado y además lo marca como seleccionado
*(solo se emplea para organismoDestino
* @param idSelect identificador del select al que añadirle la opción
* @param codigo identificador a añadir
* @param denominacion denominación del objeto a añadir
*/
function addExterno(idSelect, codigo, denominacion){
    var html =  $(idSelect).html();
    html += '<option value="'+codigo+'" selected="selected">'+ denominacion+'</option>';
    $(idSelect).html(html);
    $(idSelect).attr("disabled",false).trigger("chosen:updated");

}

/** Funcion que carga un select con los valores que obtiene de la url y luego añade al final la oficina externo
 * que le pasamos en valorSelected y denominacionSelected
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param idEntidad id del campo select donde cargar los datos obtenidos
 * @param valorSelected Valor seleccionado en el select, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 * @param denominacionSelected valor de la denominación seleccionada. sirve solo para las modificaciones
*/

function cargarSelectOficinaExterno(url,idEntidad,idSelect, valorSelected, todos, denominacionSelected){
    var html = '';
    jQuery.ajax({
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: { codigo: idEntidad },
        success: function(result) {
            if(todos){html = '<option value="-1">...</option>';}
            var len = result.length;
            var selected='';
            for ( var i = 0; i < len; i++) {
                selected='';
                if(valorSelected != null && result[i].codigo == valorSelected){
                    selected = 'selected="selected"';
                }
                html += '<option '+selected+' value="' + result[i].codigo + '">'
                    + result[i].denominacion + '</option>';
            }
            html += '</option>';

            //añadimos el externo
            html += '<option value="'+valorSelected+'" selected="selected">'+ denominacionSelected+'</option>';
            if(len != 0){
                $(idSelect).html(html);
            }else if(len==0){
                $(idSelect).attr("disabled","disabled");
                var html='';
                $(idSelect).html(html);
            }
            $(idSelect).trigger("chosen:updated");
        }
    });
}

/* Función que hace el reset de un formulario con distintos tipos de inputs.*/
function clearForm(form) {
  // iterate over all of the inputs for the given form element
  $(':input', form).each(function() {
    var type = this.type;
    var tag = this.tagName.toLowerCase(); // normalize case
    // it's ok to reset the value attr of text inputs,
    // password inputs, and textareas
    if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea' || type=='file')
      this.value = "";
    // checkboxes and radios need to have their checked state cleared
    // but should *not* have their 'value' changed
    else if (type == 'checkbox' || type == 'radio')
      this.checked = false;
    // select elements need to have their 'selectedIndex' property set to -1
    // (this works for both single and multiple select elements)
    else if (tag == 'select')
      this.selectedIndex = 0;
      $(this).trigger("chosen:updated");
  });
}

/**  CALENDAR **/
$(function() {
    $('#fechaInicio').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY',
        pickTime: false,
        useMinutes: false,
        useSeconds: false,
        useCurrent: false,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});

$(function() {
    $('#fechaFin').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY',
        pickTime: false,
        useMinutes: false,
        useSeconds: false,
        useCurrent: false,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});

$(function() {
    $('#fechaOrigen').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY HH:mm:ss',
        pickTime: true,
        useMinutes: true,
        useSeconds: true,
        useCurrent: false,
        use24hours: true,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});


/**
 * Elimina la etiqueta de Error al campo pasado por parámetro
 * @param campo
 */
function quitarError(campo){
    var variable = "#"+campo+"Error";

    $(variable).parents(".form-group").removeClass("has-error");
    var htmlNormal = "<span id=\""+campo+"Error\"></span>";
    $(variable).html(htmlNormal);
}

/**
 * Realiza el submit del formulario pasado por parámetro.
 * @param formulario
 */

function doForm(formulario){

    window.onbeforeunload = null;
    $(formulario).submit();
}

/**
 * Obtiene el valor traducido de un elemento, para comprar las modificaciones de un Registro
 * @param url donde hacer la petición ajax
 * @param id
 * @param elemento
 * @return Texto con la traducción del elmento solicitado
 */
function obtenerElementoTraducido(url, id,elemento){
    jQuery.ajax({
        url: url,
        data: { id: id },
        type: 'GET',
        success: function(result) {
            // Escribimos el resultado en el elmento indicado
            $('#'+elemento).html(result);

        }
    }) ;
}

/* Función que obtiene el contextpath en js */
function getContextPath() {
    var ctx = window.location.pathname,
        path = '/' !== ctx ? ctx.substring(0, ctx.indexOf('/', 1) + 1) : ctx;
    return path + (/\/$/.test(path) ? '' : '/');
}


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
                return diaFin >= diaInicio;
            else
                return false;
        }else
            return false;
    }
}


//Valida las fechas combinadas de un formulario (fechaInicio, fechaFin, nombre del campo Inicio, nombre del campo Fin)
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
        var formatoHtml = "<span id='"+ campFin +".errors' class='help-block'>" + $('#error2').val() + "</span>";
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
        var formatoHtml = "<span id='"+ campInicio +".errors' class='help-block'>" + $('#error1').val() + "</span>";
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
            var formatoHtml = "<span id='"+ campInicio +".errors' class='help-block'>" + $('#error3').val() + "</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            var variable2 = "#" + campFin + " span.errors";
            var formatoHtml2 = "<span id='"+ campFin +".errors' class='help-block'>" + $('#error3').val() + "</span>";
            $(variable2).html(formatoHtml2);
            $(variable2).parents(".form-group").addClass("has-error");
            posterior = false;
        }
    }

    // Si las fechas son anteriores o iguales a hoy, y la Inicio es menor o igual a la de Fin, retiorna true
    return !!((posterior) && (inicioCorrecta) && (finCorrecta));
}

// Valida que el valor introducido en el campo numeroRegistro sea un número entero
function validaEntero(numeroRegistro, campNumeroRegistro){

    //Compruebo si es un valor numérico
    if(numeroRegistro.value.length>0) {

        var contienePunto = numeroRegistro.value.indexOf('.');

        if ((!isNaN(numeroRegistro.value))&&(contienePunto==-1)) {
            var variable = "#" + campNumeroRegistro + " span.errors";
            var htmlNormal = "<span id='" + campNumeroRegistro + ".errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");
        } else {
            var variable = "#" + campNumeroRegistro + " span.errors";
            var formatoHtml = "<span id='" + campNumeroRegistro + ".errors' class='help-block'>" + $('#error4').val() + "</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
        }
    }

    return !isNaN(numeroRegistro.value);
}

// Valida que esté selecionado un libro
function validaLibro(libro, campoLibro){
    //Comprueba si hay un libro seleccionado
    if (libro.value!='') {
        var variable = "#" + campoLibro + " span.errors";
        var htmlNormal = "<span id='" + campoLibro + ".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
    } else {
        var variable = "#" + campoLibro + " span.errors";
        var formatoHtml = "<span id='" + campoLibro + ".errors' class='help-block'>" + $('#error5').val() + "</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
    }

    return libro.value!='';
}