/**
 *
 * Agrupa todas las funcionalidades relacionadas con las repros de un registro.
 *
 */

/**
 * Carga las Repros de un Usuario
 * @param url
 * @param idUsuario
 * @param tipoRegistro
 */
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

/**
 * Crea una Repro obtenido los valores de los campos del Registro
 */
function nuevaRepro(){


    var tipoRegistro = $('#tipoRegistro').val();
    var url = $("#reproForm").attr("action").concat('/' + tipoRegistro);

    var json = {
        "nombreRepro": $('#nombreRepro').val(),
        "idLibro": $('#libro\\.id').val(),
        "extracto": $('#registroDetalle\\.extracto').val(),
        "idTipoDocumentacionFisica": $('#registroDetalle\\.tipoDocumentacionFisica').val(),
        "idTipoAsunto": $('#registroDetalle\\.tipoAsunto\\.id').val(),
        "idIdioma": $('#registroDetalle\\.idioma').val(),
        "idCodigoAsunto": $('#registroDetalle\\.codigoAsunto\\.id').val(),
        "referenciaExterna": $('#registroDetalle\\.referenciaExterna').val(),
        "expediente": $('#registroDetalle\\.expediente').val(),
        "idTransporte": $('#registroDetalle\\.transporte').val(),
        "numeroTransporte": $('#registroDetalle\\.numeroTransporte').val(),
        "observaciones": $('#registroDetalle\\.observaciones').val(),
        "oficinaCodigo": $('#registroDetalle\\.oficinaOrigen\\.codigo option:selected').val(),
        "oficinaDenominacion": $('#registroDetalle\\.oficinaOrigen\\.codigo option:selected').text(),
        "numeroRegistroOrigen": $('#registroDetalle\\.numeroRegistroOrigen').val(),
        "fechaOrigen": $('#registroDetalle\\.fechaOrigen').val()};

    if(tipoRegistro == 1){ // RegistroEntrada
        json['destinoCodigo'] = $('#destino\\.codigo option:selected').val();
        json['destinoDenominacion'] = $('#destino\\.codigo option:selected').text();

    }else  if(tipoRegistro == 2){ // Registro Salida
        json['origenCodigo'] = $('#origen\\.codigo option:selected').val();
        json['origenDenominacion'] = $('#origen\\.codigo option:selected').text();
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
            if (result != null) {
                mensajeSuccess("#mensajes", "S´ha creat la repro.");
            } else {
                mensajeError("#mensajes", "Ha ocorregut un error");
            }

        }

    });

    // Ocultamos el modal de Repro
    $('#modalNewRepro').modal('hide');
}

/**
 * Rellena los campos de un Registro a partir de los valores de una Repro
 * @param idRepro
 * @param tipoRegistro
 */
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
            $('#registroDetalle\\.idioma').val(repro.idIdioma);
            $('#registroDetalle\\.idioma').trigger("chosen:updated");
            $('#registroDetalle\\.codigoAsunto\\.id').val(repro.idCodigoAsunto);
            $('#registroDetalle\\.referenciaExterna').val(repro.referenciaExterna);
            $('#registroDetalle\\.expediente').val(repro.expediente);
            $('#registroDetalle\\.transporte').val(repro.idTransporte);
            $('#registroDetalle\\.numeroTransporte').val(repro.numeroTransporte);
            $('#registroDetalle\\.observaciones').val(repro.observaciones);
            $('#registroDetalle\\.numeroRegistroOrigen').val(repro.numeroRegistroOrigen);
            $('#registroDetalle\\.fechaOrigen').val(repro.fechaOrigen);

            $('#libro\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.tipoDocumentacionFisica').trigger("chosen:updated");
            $('#registroDetalle\\.idioma\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.transporte').trigger("chosen:updated");

            $('#registroDetalle\\.codigoAsunto\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.transporte').trigger("chosen:updated");

            //Oficina origen
            if (repro.oficinaCodigo != null) {
                var oficina= '';
                oficina += '<option value="' + repro.oficinaCodigo + '" selected="selected">'
                + repro.oficinaDenominacion + '</option>';

                $('#registroDetalle\\.oficinaOrigen\\.codigo').append(oficina);
                $('#registroDetalle\\.oficinaOrigen\\.codigo').trigger("chosen:updated");
                $('#registroDetalle\\.oficinaOrigen\\.denominacion').val(repro.oficinaDenominacion);
            }

            // Destino u Origen
            if(tipoRegistro == 1){ // RegistroEntrada
                if(repro.destinoCodigo == null){
                    mensajeError("#mensajes","La unitat destino seleccionada ja no està vigent, i s'ha eliminat de la seva Repro.");
                }else{
                    //alert("Destino codigo: " + repro.destinoCodigo);
                    //alert("Destino denominacion: " + repro.destinoDenominacion);
                    /*var unidad = '';
                    unidad = "#destino\\\\.codigo option[value="+repro.destinoCodigo+"]";
                    alert($("#destino\\.codigo option[value="+repro.destinoCodigo+"]").text());
                    alert($(unidad).text());
                    if($(unidad).text().length > 0){
                        alert("encontrado: " + $(unidad).text());
                    }else{
                        alert("No encontrado");
                    }*/
                    var destino= '';
                    destino += '<option value="' + repro.destinoCodigo + '" selected="selected">' + repro.destinoDenominacion + '</option>';

                    $('#destino\\.codigo').append(destino);
                    $('#destino\\.codigo').trigger("chosen:updated");
                    $('#destino\\.denominacion').val(repro.destinoDenominacion);
                }

            }else  if(tipoRegistro == 2){ // Registro Salida

                if (repro.origenCodigo != null) {

                    var origen= '';
                    origen += '<option value="' + repro.origenCodigo + '" selected="selected">' + repro.origenDenominacion + '</option>';

                    $('#origen\\.codigo').append(origen);
                    $('#origen\\.codigo').trigger("chosen:updated");
                    $('#origen\\.denominacion').val(repro.origenDenominacion);

                }
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