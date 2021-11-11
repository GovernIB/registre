/**
 * Carga los datos de un Anexo para mostrarlos en un modal
 * @param idAnexo
 */
function obtenerAnexo(idAnexo, idEntidad){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarAnexoDetalle();

    //Obtenemos los datos del Anexo a editar
    $.ajax({
        url: urlCargarAnexo,
        data: { idAnexo: idAnexo, idEntidad: idEntidad},
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            $('#anexoTitulo').html(result.anexo.titulo);
            $('#titulo').html(result.anexo.titulo);
            $('#validezDocumento').html(tradsanexo['tipoValidezDocumento.'+result.anexo.validezDocumento]);
            $('#tipoDocumento').html(tradsanexo['tipoDocumento.0'+result.anexo.tipoDocumento]);
            $('#observacionesAnexo').html(result.anexo.observaciones);
            if(result.anexo.scan == true){
                $('#escaneado').html("Si");
            }else{
                $('#escaneado').html("No");
            }
            $('#origen').html(tradsanexo['anexo.origen.'+result.anexo.origenCiudadanoAdmin]);
            obtenerElementoTraducido(urlTipoDocumental, result.anexo.tipoDocumental.id, 'tipoDocumental');
            if(result.anexo.modoFirma == 0 || result.anexo.modoFirma == 2 ){
                $('#mime').html(result.docMime);
                $('#nombreFichero').html(result.docFileName);
            }else {
                $('#mime').html(result.signMime);
                $('#nombreFichero').html(result.signFileName);
            }

            if(result.anexo.modoFirma !== 0){ // Firma Attached o detached
                $('#tipoFirma').html(result.anexo.signType);
                $('#perfilFirma').html(result.anexo.signProfile);
                $('#formatoFirma').html(result.anexo.signFormat);
                $('#firmaInformacion').show();
            }else{
                $('#firmaInformacion').hide();
            }
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
    $('#escaneado').html('');
    $('#origen').html('');
    $('#mime').html('');
    $('#tipoFirma').html('');
    $('#perfilFirma').html('');
    $('#formatoFirma').html('');
    $('#nombreFichero').html('');

}

function quitarErrorAnexo(campo){
    $(campo).parents(".form-group").removeClass("has-error");
    $(campo).remove();
}

function limpiarAnexoForm(){
    //$('#reload').show();
    quitarErrorAnexo('#documentoFile\\.errors');
    quitarErrorAnexo('#firmaFile\\.errors');
}

function quitarMensajeError(){
    $('#mensajeError').remove();
}