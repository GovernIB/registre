/**
 * Obtiene los datos de una Sincronización
 * @param idSincronizacion
 */
function infoSincronizacion(idSincronizacion){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarSincronizacion();

    //Obtenemos los datos de la Sincronizacion
    $.ajax({
        url: urlObtenerSincronizacion,
        data: { idSincronizacion: idSincronizacion},
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            $('#fecha').html(result.fechaFormateada);
            $('#tipo').html('<span class="label label-warning">'+tradsSincronizacion['sincronizacion.tipo.'+result.tipo]+'</span>');

            if(result.elementos != null){
                $('#elementos').html(result.elementos);
                $('#elementosBox').show();
            }else{
                $('#elementosBox').hide();
            }
        }
    });
}

/**
 * Limpia los campos de la sincronización
 */
function limpiarSincronizacion(){

    $('#fecha').html('');
    $('#tipo').html('');
    $('#elementos').html('');
}