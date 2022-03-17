/**
 * Carga los datos de un Interesado para mostrarlos en un modal
 * @param idIntegracion
 */
function infoIntegracion(idIntegracion){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarIntegracion();

    //Obtenemos los datos de la Integración
    $.ajax({
        url: urlobtenerIntegracion,
        data: { idIntegracion: idIntegracion},
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            $('#fecha').html(result.fechaFormateada);
            $('#descripcionIntegracion').html(result.descripcion);
            $('#tiempo').html(result.tiempoFormateado);
            $("#tipo").html(tradsIntegracion['integracion.tipo.'+result.tipo]);

            if(result.estado === 0){
                $('#estadoIntegracion').html('<span class="label label-success"><span class="fa fa-check"></span>  Ok</span>');
            }else if(result.estado === 1){
                $('#estadoIntegracion').html('<span class="label label-danger"><span class="fa fa-warning"></span> Error</span>');
            }

            var peticion = result.peticion;
            $('#peticion').html(peticion.replace(/\n/g, "<br />"));

            if(result.error != null){
                $('#error').text(result.error);
                $('#errorBox').show();
            }else{
                $('#errorBox').hide();
            }

            if(result.excepcion != null){
                $('#excepcion').html(result.excepcion);
                $('#excepcionBox').show();
            }else{
                $('#excepcionBox').hide();
            }

        }
    });
}

/**
 * Limpia los campos de la integración
 */
function limpiarIntegracion(){

    $('#fecha').html('');
    $('#descripcionIntegracion').html('');
    $('#tiempo').html('');
    $('#tipo').html('');
    $('#estadoIntegracion').html('');
    $('#peticion').html('');
    $('#error').html('');
    $('#excepcion').html('');
}

/**
 * Invoca al formulario de búsqueda de integraciones por número de registro
 * @param numeroRegistro
 */
function buscarIntegraciones(numeroRegistro){

    $('#texto').val(numeroRegistro);

    $('#integracion').submit();
}