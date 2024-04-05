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

        success: function(anexoFull) {

            $('#anexoTitulo').html(tradsanexo['anexo.detalle']+": "+anexoFull.anexo.titulo);
            $('#titulo').html(anexoFull.anexo.titulo);
            $('#validezDocumento').html(tradsanexo['tipoValidezDocumento.'+anexoFull.anexo.validezDocumento]);
            $('#tipoDocumento').html(tradsanexo['tipoDocumento.0'+anexoFull.anexo.tipoDocumento]);
            $('#observacionesAnexo').html(anexoFull.anexo.observaciones);
            $('#hash').html(anexoFull.hash);
            if(anexoFull.anexo.scan == true){
                $('#escaneado').html("Si");
            }else{
                $('#escaneado').html("No");
            }
            $('#origen').html(tradsanexo['anexo.origen.'+anexoFull.anexo.origenCiudadanoAdmin]);
            obtenerElementoTraducido(urlTipoDocumental, anexoFull.anexo.tipoDocumental.id, 'tipoDocumental');
            if(anexoFull.anexo.modoFirma == 0 || anexoFull.anexo.modoFirma == 2 ){
                $('#mime').html(anexoFull.docMime);
                $('#nombreFichero').html(anexoFull.docFileName);
            }else {
                $('#mime').html(anexoFull.signMime);
                $('#nombreFichero').html(anexoFull.signFileName);
            }

            if(anexoFull.anexo.modoFirma !== 0){ // Firma Attached o detached
                $('#tipoFirma').html(anexoFull.anexo.signType);
                $('#perfilFirma').html(anexoFull.anexo.signProfile);
                $('#formatoFirma').html(anexoFull.anexo.signFormat);
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
    $('#hash').html('');
    $('#tipoDocumental').html('');

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