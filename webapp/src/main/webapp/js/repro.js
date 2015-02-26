/**
 *
 * Agrupa todas las funcionalidades relacionadas con las repros de un registro.
 *
 */

// Valida el formuario Nuevo si ha introducido un nombre
function validaFormulario(form) {

    var nombreRepro = $('#nombreRepro').val();
    var variable = "#nomRepro span.errors";

    if (nombreRepro != "") {
        var htmlNormal = "<span id='nomRepro.errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");

        // Si pasa todas las validaciones, creamos la nueva Repro.
        nuevaRepro();

    } else{
        var formatoHtml = "<span id='nomRepro.errors' class='help-block'>El camp és obligatori</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        return false;
    }
}

// Agafa tots els camps del Registre d'Entrada per guardar a la Repro
function nuevaRepro(){

    var nombreRepro = $('#nombreRepro').val();
    var tipoRegistro = $('#tipoRegistro').val();
    var url = $("#reproForm").attr("action").concat('/'+nombreRepro).concat('/'+tipoRegistro);

    var json = { "idLibro": $('#libro\\.id').val(),
        "extracto": $('#registroDetalle\\.extracto').val(),
        "idTipoDocumentacionFisica": $('#registroDetalle\\.tipoDocumentacionFisica').val(),
        "idTipoAsunto": $('#registroDetalle\\.tipoAsunto\\.id').val(),
        "idIdioma": $('#registroDetalle\\.idioma\\.id').val(),
        "idCodigoAsunto": $('#registroDetalle\\.codigoAsunto\\.id').val(),
        "referenciaExterna": $('#registroDetalle\\.referenciaExterna').val(),
        "expediente": $('#registroDetalle\\.expediente').val(),
        "idTransporte": $('#registroDetalle\\.transporte').val(),
        "numeroTransporte": $('#registroDetalle\\.numeroTransporte').val(),
        "observaciones": $('#registroDetalle\\.observaciones').val(),
        "oficinaOrigen": $('#registroDetalle\\.oficinaOrigen\\.codigo').val(),
        "oficinaOrigenExterno": $('#registroDetalle\\.oficinaOrigenExterno').val(),
        "denominacionOfiOrigenExt": $('#registroDetalle\\.denominacionOfiOrigenExt').val(),
        "numeroRegistroOrigen": $('#registroDetalle\\.numeroRegistroOrigen').val(),
        "fechaOrigen": $('#registroDetalle\\.fechaOrigen').val()};

    if(tipoRegistro == 1){ // RegistroEntrada
        json['destino'] = $('#destino\\.codigo').val();
        json['destinoExternoCodigo'] = $('#destinoExternoCodigo').val();
        json['destinoExternoDenominacion'] = $('#destinoExternoDenominacion').val();

    }else  if(tipoRegistro == 2){ // Registro Salida
        json['origen'] = $('#origen\\.codigo').val();
        json['origenExternoCodigo'] = $('#origenExternoCodigo').val();
        json['origenExternoDenominacion'] = $('#origenExternoDenominacion').val();
    }

    $.ajax({
        url: url,
        data: JSON.stringify(json),
        type: "POST",

        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function(result) {

        }

    });

    // Ocultamos el modal de Repro
    $('#modalNewRepro').modal('hide');
}

// Obté tots els camps de la Repro per omplir el Registre d'Entrada
function rellenarFormulario(idRepro,tipoRegistro){

    //Obtenemos los datos de la Repro
    $.ajax({
        url: urlObtenerRepro,
        data: { idRepro: idRepro },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(repro) {

            // Rellenamos los campos del formulario utilizando los de la Repro
            $('#libro\\.id').val(repro.idLibro);
            $('#registroDetalle\\.extracto').val(repro.extracto);
            $('#registroDetalle\\.tipoDocumentacionFisica').val(repro.idTipoDocumentacionFisica);

            $('#registroDetalle\\.tipoAsunto\\.id').val(repro.idTipoAsunto);
            $('#registroDetalle\\.tipoAsunto\\.id').trigger("chosen:updated");
            actualizarCodigosAsunto();
            $('#registroDetalle\\.idioma\\.id').val(repro.idIdioma);
            $('#registroDetalle\\.codigoAsunto\\.id').val(repro.idCodigoAsunto);
            $('#registroDetalle\\.referenciaExterna').val(repro.referenciaExterna);
            $('#registroDetalle\\.expediente').val(repro.expediente);
            $('#registroDetalle\\.transporte').val(repro.idTransporte);
            $('#registroDetalle\\.numeroTransporte').val(repro.numeroTransporte);
            $('#registroDetalle\\.observaciones').val(repro.observaciones);
            //$('#registroDetalle\\.oficinaOrigen\\.codigo').val();
            //$('#registroDetalle\\.oficinaOrigenExterno').val();
            //$('#registroDetalle\\.denominacionOfiOrigenExt').val();
            $('#registroDetalle\\.numeroRegistroOrigen').val(repro.numeroRegistroOrigen);
            $('#registroDetalle\\.fechaOrigen').val(repro.fechaOrigen);

            $('#libro\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.tipoDocumentacionFisica').trigger("chosen:updated");
            $('#registroDetalle\\.idioma\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.transporte').trigger("chosen:updated");

            $('#registroDetalle\\.codigoAsunto\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.transporte').trigger("chosen:updated");

            if(tipoRegistro == 1){ // RegistroEntrada
                $('#destino\\.codigo').val(repro.destino);
                $('#destinoExternoCodigo').val();
                //$('#destinoExternoDenominacion').val();

            }else  if(tipoRegistro == 2){ // Registro Salida

            }

        }
    });

    // Ocultamos el modal de Select Repro
    $('#modalSelectRepro').modal('hide');
}

/**
 * Prepara el formulario de Repros para dar de alta una nueva.
 */
function preparaFormularioRepro(tipoRegistro){
    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarRepro();

    //Introducimos el tipo de Repro
    $('#tipoRegistro').val(tipoRegistro);
}

/**
 * Limpia el formulario y los posibles mensajes de error
 */
function limpiarRepro(){
    clearForm("#reproForm");
    quitarErroresRepro();
    $('#nombreRepro').val(null);
    $('#tipoRegistro').val(null);
}

function quitarErroresRepro(){
    var htmlNormal = "<span id='reproSelect.errors'></span>";
    var variable = "#nomRepro span.errors";
    $(variable).html(htmlNormal);
    $(variable).parents(".form-group").removeClass("has-error");
}



function cargarRepros(url,idUsuario,tipoRegistro){

    var html = '';
    $("#reproList").html(html);

    jQuery.ajax({
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: { idUsuario: idUsuario, tipoRegistro:tipoRegistro},
        success: function(result) {
            var len = result.length;
            html += '<ul>';
            for ( var i = 0; i < len; i++) {
                var idRepro = result[i].id;
                html += '<li><a href="javascript:void(0);" onclick="rellenarFormulario('+ idRepro +','+ tipoRegistro +')">' + result[i].nombre + '</a></li>';
            }
            if(len==0){
                html += '<li>No hi ha repros guardades</li>';
            }
            html += '</ul>';



            $("#reproList").html(html);
        }
    });

    // Ocultamos el modal de Select Repro
    $('#modalSelectRepro').modal('hide');
}



