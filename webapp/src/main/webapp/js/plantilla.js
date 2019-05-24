/**
 *
 * Agrupa todas las funcionalidades relacionadas con las plantillas de un registro.
 *
 */

/**
 * Crea una Plantilla obtenido los valores de los campos del Registro
 */
function guardarPlantilla(){


    var url = $("#plantillaForm").attr("action");

    var json = {
        "idRegistro": $('#idRegistro').val(),
        "tipoRegistro": $('#tipoRegistro').val(),
        "nombrePlantilla": $('#nombrePlantilla').val()};

    $.ajax({
        url: url,
        data:  JSON.stringify(json) ,
        type: "POST",

        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function(result) {
            if (result != null) {
                mensajeSuccess("#mensajes", "S´ha creat la plantilla.");
            } else {
                mensajeError("#mensajes", "Ha ocorregut un error");
            }

        }

    });

    // Ocultamos el modal de Plantilla
    $('#modalNewPlantilla').modal('hide');
}

/**
 * Carga las Plantillas de un Usuario
 * @param url
 * @param idUsuario
 * @param tipoRegistro
 */
function cargarPlantillas(url,idUsuario,tipoRegistro){

    var html = '';
    $("#plantillaList").html(html);

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
                var idPlantilla = result[i].id;
                html += '<li><a href="javascript:void(0);" onclick="rellenarFormulario('+ idPlantilla +','+ tipoRegistro +')">' + result[i].nombre + '</a></li>';
            }
            if(len==0){
                html += '<li>No hi ha plantilles guardades</li>';
            }
            html += '</ul>';

            $("#plantillaList").html(html);
        }
    });

    // Ocultamos el modal de Select Plantilla
    $('#modalSelectPlantilla').modal('hide');
}

// Valida el formuario Nuevo si ha introducido un nombre
function validaFormulario(form) {

    var nombrePlantilla = $('#nombrePlantilla').val();
    var variable = "#nomPlantilla span.errors";

    if (nombrePlantilla != "") {
        var htmlNormal = "<span id='nomPlantilla.errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");

        // Si pasa todas las validaciones, creamos la nueva Plantilla.
        nuevaPlantilla();

    } else{
        var formatoHtml = "<span id='nomPlantilla.errors' class='help-block'>El camp és obligatori</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        return false;
    }
}

/**
 * Crea una Plantilla obtenido los valores de los campos del Registro
 */
function nuevaPlantilla(){


    var tipoRegistro = $('#tipoRegistro').val();
    var url = $("#plantillaForm").attr("action").concat('/' + tipoRegistro);

    // Interesados
    var interesado = $('#interesados tr:eq(1)').attr('id');
    if(interesado!=null && interesado.indexOf("organismo") >= 0){
        interesado = interesado.replace('organismo','');
        var interesadoDenominacion = $('#interesados tr:eq(1) td')[0].innerHTML;
        interesado = interesado + "+" + interesadoDenominacion;
    }else{
        interesado="";
    }

    var json = {
        "nombreRepro": $('#nombrePlantilla').val(),
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
        "fechaOrigen": $('#registroDetalle\\.fechaOrigen').val(),
        "codigoSia": $('#registroDetalle\\.codigoSia').val(),
        "interesado": interesado};

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
                mensajeSuccess("#mensajes", "S´ha creat la plantilla.");
            } else {
                mensajeError("#mensajes", "Ha ocorregut un error");
            }

        }

    });

    // Ocultamos el modal de Plantilla
    $('#modalNewPlantilla').modal('hide');
}

/**
 * Rellena los campos de un Registro a partir de los valores de una Plantilla
 * @param idPlantilla
 * @param tipoRegistro
 */
function rellenarFormulario(idPlantilla,tipoRegistro){

    //Eliminamos datos previos de interesados
    quitarErroresPlantilla();
    eliminarInteresados();
    $('#interesados').hide();

    $.ajax({
        url: urlObtenerPlantilla,
        data: { idPlantilla: idPlantilla },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(plantilla) {

            // Rellenamos los campos del formulario utilizando los de la Plantilla
            $('#libro\\.id').val(plantilla.idLibro);
            $('#registroDetalle\\.extracto').val(plantilla.extracto);
            $('#registroDetalle\\.tipoDocumentacionFisica').val(plantilla.idTipoDocumentacionFisica);
            actualizarColorTipoDocumentacion();

            $('#registroDetalle\\.tipoAsunto\\.id').val(plantilla.idTipoAsunto);
            $('#registroDetalle\\.tipoAsunto\\.id').trigger("chosen:updated");
            actualizarCodigosAsunto();
            $('#registroDetalle\\.idioma').val(plantilla.idIdioma);
            $('#registroDetalle\\.idioma').trigger("chosen:updated");
            $('#registroDetalle\\.codigoAsunto\\.id').val(plantilla.idCodigoAsunto);
            $('#registroDetalle\\.referenciaExterna').val(plantilla.referenciaExterna);
            $('#registroDetalle\\.expediente').val(plantilla.expediente);
            $('#registroDetalle\\.transporte').val(plantilla.idTransporte);
            $('#registroDetalle\\.numeroTransporte').val(plantilla.numeroTransporte);
            $('#registroDetalle\\.observaciones').val(plantilla.observaciones);
            $('#registroDetalle\\.numeroRegistroOrigen').val(plantilla.numeroRegistroOrigen);
            $('#registroDetalle\\.fechaOrigen').val(plantilla.fechaOrigen);
            $('#registroDetalle\\.codigoSia').val(plantilla.codigoSia);

            $('#libro\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.tipoDocumentacionFisica').trigger("chosen:updated");
            $('#registroDetalle\\.idioma\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.transporte').trigger("chosen:updated");

            $('#registroDetalle\\.codigoAsunto\\.id').trigger("chosen:updated");
            $('#registroDetalle\\.transporte').trigger("chosen:updated");

            // Interesado
            if (plantilla.interesado != null) {
                if (plantilla.interesado.length > 0) {
                    var interesadoCodigo = plantilla.interesado.substr(0, plantilla.interesado.indexOf('+'));
                    var interesadoDenominacion = plantilla.interesado.substr(plantilla.interesado.indexOf('+') + 1);
                    addOrganismoInteresadoPlantilla(tradorganismo['interesado.administracion'], '', interesadoCodigo, interesadoDenominacion);
                }
            }

            //Oficina origen
            if (plantilla.oficinaCodigo != null) {
                var oficina= '';
                oficina += '<option value="' + plantilla.oficinaCodigo + '" selected="selected">'
                + plantilla.oficinaDenominacion + '</option>';

                $('#registroDetalle\\.oficinaOrigen\\.codigo').append(oficina);
                $('#registroDetalle\\.oficinaOrigen\\.codigo').trigger("chosen:updated");
                $('#registroDetalle\\.oficinaOrigen\\.denominacion').val(plantilla.oficinaDenominacion);
            }

            // Destino u Origen
            if(tipoRegistro == 1){ // RegistroEntrada
                if(plantilla.destinoCodigo == null){
                    mensajeError("#mensajes","La unitat destino seleccionada ja no està vigent, i s'ha eliminat de la seva Plantilla.");
                }else{

                    var destino= '';
                    destino += '<option value="' + plantilla.destinoCodigo + '" selected="selected">' + plantilla.destinoDenominacion + '</option>';

                    $('#destino\\.codigo').append(destino);
                    $('#destino\\.codigo').trigger("chosen:updated");
                    $('#destino\\.denominacion').val(plantilla.destinoDenominacion);
                }

            }else  if(tipoRegistro == 2){ // Registro Salida

                if (plantilla.origenCodigo != null) {

                    var origen= '';
                    origen += '<option value="' + plantilla.origenCodigo + '" selected="selected">' + plantilla.origenDenominacion + '</option>';

                    $('#origen\\.codigo').append(origen);
                    $('#origen\\.codigo').trigger("chosen:updated");
                    $('#origen\\.denominacion').val(plantilla.origenDenominacion);

                }
            }

        }
    });

    // Ocultamos el modal de Select Plantilla
    $('#modalSelectPlantilla').modal('hide');
}

/**
 * Prepara el formulario de Plantillas para dar de alta una nueva.
 */
function preparaFormularioPlantilla(tipoRegistro){
    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarPlantilla();

    //Introducimos el tipo de Plantilla
    $('#tipoRegistro').val(tipoRegistro);
}

/**
 * Limpia el formulario y los posibles mensajes de error
 */
function limpiarPlantilla(){
    clearForm("#plantillaForm");
    quitarErroresPlantilla();
    $('#nombrePlantilla').val(null);
    $('#tipoRegistro').val(null);
}

function quitarErroresPlantilla(){
    var htmlNormal = "<span id='plantillaSelect.errors'></span>";
    var variable = "#nomPlantilla span.errors";
    $(variable).html(htmlNormal);
    $(variable).parents(".form-group").removeClass("has-error");
}