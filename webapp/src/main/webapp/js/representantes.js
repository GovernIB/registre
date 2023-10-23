/**
 * Gestiona la creación o modificación de un Representante
 * @param idRepresentado
 * @param idRepresentante
 * @param urlEditar
 */
function gestionarRepresentante(idRepresentante,idRepresentado,urlEditar){

    $('#modalBuscadorPersonasTodas').modal('hide');

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarInteresado();

    // Activamos o deshabilitamos campos según el TipoPersona escogido
    tipoInteresadoRepresentante();

    // Rellenamos los campos necesarios para identificar al respresentante
    $('#isRepresentante').val('true');
    $('#representado\\.id').val(idRepresentado);

    // Comprbamos si se trata de una Creación o una modificación
    if(idRepresentante == null || idRepresentante.length == 0){ // Nuevo Representante
        $('#accion').val('nuevo');
        $('#interesadoTitulo').html(tradsinteresado['interesado.representante.nuevo']);
        $('#tipoInteresadoSelect').show();


    }else{ // Editar Representante
        $('#eliminarRepresentante').show();
        $('#accion').val('editar');
        $('#interesadoTitulo').html(tradsinteresado['interesado.representante.editar']);

        var json = { "id": $('#id').val(), "tipo": $('#tipo').val(), "nombre" : $('#nombre').val(), "apellido1" : $('#apellido1').val(), "apellido2" : $('#apellido2').val(),"tipoDocumentoIdentificacion": $('#tipoDocumentoIdentificacion').val(), "documento" : $('#documento').val(),
            "pais" : $('#pais\\.id').val(),"provincia" : $('#provincia\\.id').val(), "localidad" : $('#localidad\\.id').val(), "direccion" : $('#direccion').val(), "razonSocial": $('#razonSocial').val(), "email" : $('#email').val(), "cp" : $('#cp').val(), "telefono" : $('#telefono').val(),
            "direccionElectronica":$('#direccionElectronica').val(),"canal":$('#canal').val(), "observaciones":$('#observaciones').val(), "guardarInteresado":$('#guardarInteresado').prop('checked')};

        //Obtenemos los datos del Representante a editar
        $.ajax({
            url: urlEditar,
            data: { id: idRepresentante },
            type: "GET",
            dataType: 'json',
            contentType: 'application/json',


            success: function(representante) {

                // Rellenamos los campos del formulario

                $('#id').val(representante.id);
                $('#tipo').val(representante.tipo);
                $('#nombre').val(representante.nombre);
                $('#apellido1').val(representante.apellido1);
                $('#apellido2').val(representante.apellido2);
                if(representante.tipoDocumentoIdentificacion != null && representante.tipoDocumentoIdentificacion != '-1'){
                    $('#documento').removeAttr("disabled","disabled");
                    $('#tipoDocumentoIdentificacion').val(representante.tipoDocumentoIdentificacion);
                }
                $('#documento').val(representante.documento);
                if(representante.pais != null){$("#pais\\.id").val(representante.pais.id);}
                if(representante.provincia != null){
                    $("#provincia\\.id").val(representante.provincia.id);
                    $('#provincia\\.id').removeAttr("disabled","disabled");
                    actualizarLocalidad();
                }
                if(representante.localidad != null){
                    $("#localidad\\.id").val(representante.localidad.id);
                    $('#localidad\\.id').removeAttr("disabled","disabled");
                }
                $('#direccion').val(representante.direccion);
                $('#razonSocial').val(representante.razonSocial);
                $('#direccionElectronica').val(representante.direccionElectronica);
                $('#email').val(representante.email);
                $('#cp').val(representante.cp);
                $('#telefono').val(representante.telefono);
                if(representante.canal != null){$("#canal").val(representante.canal);}
                $('#observaciones').val(representante.observaciones);

                // Actualizamos los select Chosen
                $('#tipoDocumentoIdentificacion').trigger("chosen:updated");
                $('#canal').trigger("chosen:updated");
                $('#tipoPersona').trigger("chosen:updated");
                $('#pais\\.id').trigger("chosen:updated");
                $('#provincia\\.id').trigger("chosen:updated");
                $('#localidad\\.id').trigger("chosen:updated");

                // Activamos o deshabilitamos campos según el TipoPersona escogido
                tipoInteresadoRepresentante();
                // Gestión de los cambios del Canal de Notificación
                actualizarCanalNotificacion();

            }
        });

    }

    // Mostramos el formulario
    $('#modalInteresado').modal('show');
}


/**
 * Vincula un Representante con un Interesado
 * @param idRepresentante
 * @param idRepresentado
 * @param nombreRepresentante
 * @param idRegistroDetalle
 */
function addRepresentanteHtml(idRepresentante, idRepresentado,nombreRepresentante,idRegistroDetalle){
    var elemento = "#persona"+idRepresentado;

    // Botonera de acciones de un representante
    var representanteButton = "<div class=\"btn-group\">"+
        "<button type=\"button\" class=\"btn btn-success btn-xs dropdown-toggle\" data-toggle=\"dropdown\">"+nombreRepresentante+" <span class=\"caret\"></span></button>"+
        "<ul class=\"dropdown-menu\" role=\"menu\">"+
        "<li><a href=\"#modalInteresado\" onclick=\"gestionarRepresentante("+idRepresentante+","+idRepresentado+",'"+urlObtenerInteresado+"')\">"+tradsinteresado['interesado.representante.editar']+"</a></li>"+
        "<li><a href=\"javascript:void(0);\" onclick=\"eliminarRepresentante("+idRepresentante+","+idRepresentado+",'"+idRegistroDetalle+"')\">"+tradsinteresado['interesado.representante.eliminar']+"</a></li></ul></div>";

    // Añadimos a la celda la nueva información
    $(elemento + ' td:nth-child(3)').html(representanteButton);

    $('#modalBuscadorPersonasTodas').modal('hide');

    if (idRegistroDetalle.length > 0) {
        mensajeSuccess("#mensajes", tradsinteresado['representante.añadido']);
    }
}


/**
 * Elimina un Representante y su vinculación con el Interesado
 * @param idRepresentante
 * @param idRepresentado
 */
function eliminarRepresentanteHtml(idRepresentado){
    var elemento = "#persona"+idRepresentado;
    var vacio = "";
    // Botonera de acciones de un representante
    var representanteButton = "<div class=\"btn-group\">"+
        "<button type=\"button\" class=\"btn btn-danger btn-xs dropdown-toggle\" data-toggle=\"dropdown\">No <span class=\"caret\"></span></button>"+
        "<ul class=\"dropdown-menu\" role=\"menu\">"+
        "<li><a href=\"#modalInteresado\" onclick=\"gestionarRepresentante('"+vacio+"',"+idRepresentado+",'"+urlObtenerInteresado+"')\">"+tradsinteresado['interesado.representante.nuevo']+"</a></li>"+
        "<li><a data-toggle=\"modal\" href=\"#modalBuscadorPersonasTodas\" onclick=\"busquedaRepresentantes("+idRepresentado+")\">"+tradsinteresado['interesado.representante.buscar']+"</a></li></ul></div>";

    // Añadimos a la celda la nueva información
    $(elemento + ' td:nth-child(3)').html(representanteButton);

    mensajeSuccess("#mensajes", tradsinteresado['representante.eliminado']);
}

/**
 * Añadimos el representante
 */
function addRepresentante(idRepresentante,idRepresentado,idRegistroDetalle){

    $.ajax({
        url: urlAddRepresentante,
        type: 'GET',
        dataType: 'json',
        data: { idRepresentante: idRepresentante, idRepresentado: idRepresentado, idRegistroDetalle:idRegistroDetalle },
        contentType: 'application/json',

        success: function(respuesta) {

            if(respuesta != null){
                addRepresentanteHtml(respuesta.result.id,idRepresentado,respuesta.result.nombre,idRegistroDetalle);
            }else{
                mensajeError("#mensajes", tradsinteresado['interesado.añadir.error']);
            }


        }
    });
}

/**
 * Eliminamos el representante de la sesion y lo desvinculamos del Interesado
 */
function eliminarRepresentante(idRepresentante,idRepresentado,idRegistroDetalle){

    if(idRegistroDetalle.length == 0){
        idRegistroDetalle = 'null';
    }

    var confirmModal =
        $("<div class=\"modal fade\">" +
        "<div class=\"modal-dialog\">" +
        "<div class=\"modal-content\">"+
        "<div class=\"modal-header\">" +
        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
        "<h4 class=\"modal-title\">"+tradsinteresado['regweb3.confirmar']+"</h4>" +
        "</div>" +

        "<div class=\"modal-body\">" +
        "<p>"+tradsinteresado['representante.eliminar']+"</p>" +
        "</div>" +

        "<div class=\"modal-footer\">" +
        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>"+
        "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>"+
        "</div>" +
        "</div>" +
        "</div>" +
        "</div>");

    confirmModal.find("#okButton").click(function(event) {

        $.ajax({
            url: urlEliminarRepresentante,
            type: 'GET',
            dataType: 'json',
            data: { idRepresentante: idRepresentante, idRepresentado: idRepresentado,idRegistroDetalle:idRegistroDetalle },
            contentType: 'application/json',

            success: function(result) {
                if(result==true){
                    eliminarRepresentanteHtml(idRepresentado);
                }
            }
        });
        confirmModal.modal("hide");
    });

    confirmModal.modal("show");

}


/**
 *
 * @param idRepresentado
 */
function busquedaRepresentantes(idRepresentado){

    limpiarBusquedaPersona('Todas');
    $('#representado').val(idRepresentado);
}
/**
 * Según el tipo persona seleccionado, habilita o deshabilita una serie de campos
 */
function tipoInteresadoRepresentante(){
    var tipoInteresado = $('#tipo option:selected').val();

    if (tipoInteresado == 2) { //Persona fisica
        $('#razonSocial').val('');
        $('#razonSocial').attr("disabled", "disabled");
        $('#nombre').removeAttr("disabled", "disabled");
        $('#apellido1').removeAttr("disabled", "disabled");
        $('#apellido2').removeAttr("disabled", "disabled");

        tiposDocumentoPersonaFisica();
    }

    if (tipoInteresado == 3) { //Persona juridica
        $('#razonSocial').removeAttr("disabled", "disabled");
        $('#nombre').val('');
        $('#apellido1').val('');
        $('#apellido2').val('');
        $('#nombre').attr("disabled", "disabled");
        $('#apellido1').attr("disabled", "disabled");
        $('#apellido2').attr("disabled", "disabled");

        tiposDocumentoPersonaJuridica();
    }

    quitarErroresInteresado();
}