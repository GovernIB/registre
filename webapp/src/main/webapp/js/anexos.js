/**
 * Carga los datos de un Anexo para mostrarlos en un modal
 * @param idAnexo
 */
function obtenerAnexo(idAnexo){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarAnexoDetalle();

    //Obtenemos los datos del Anexo a editar
    $.ajax({
        url: urlCargarAnexo,
        data: { idAnexo: idAnexo},
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            $('#anexoTitulo').html(result.titulo);

            $('#titulo').html(result.titulo);
            $('#validezDocumento').html(tradsanexo['tipoValidezDocumento.'+result.validezDocumento]);

            $('#tipoDocumento').html(tradsanexo['tipoDocumento.0'+result.tipoDocumento]);
            $('#observacionesAnexo').html(result.observaciones);
            $('#origen').html(tradsanexo['anexo.origen.'+result.origenCiudadanoAdmin]);
            obtenerElementoTraducido(urlTipoDocumental, result.tipoDocumental.id, 'tipoDocumental');

        }
    });


}

/**
 * Limpia el formulario de anexo y los posibles mensajes de error
 */
function limpiarAnexoDetalle(){

    $('#anexoTitulo').html('');

    $('#titulo').html('');
    $('#validezDocumento').html('');
    $('#tipoDocumento').html('');
    $('#observacionesAnexo').html('');
    $('#origen').html('');
    $('#tipoDocumental').html('');

}