/**
 *
 */
function nuevaNotificacion(){

    var notificacion = { "destinatarios": $('#destinatarios').val(), "tipo": $('#tipo').val(), "asunto": $('#asuntoNotificacion').val(), "mensaje" : $('#mensajeNotificacion').val()};

    if($('#asuntoNotificacion').val() === "" || $('#mensajeNotificacion').val() === "" || $('#destinatarios').val() == null){
        $("#errores").html(tradsNotificacion['notificacion.campos.obligatorios']);
        $('#errores').show();
    }else{

        $.ajax({
            url: urlNuevaNotificacion,
            data: JSON.stringify(notificacion),
            type: "POST",
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
                waitingDialog.show(tradsNotificacion['notificacion.generando'], {dialogSize: 'm', progressType: 'warning'});
            },
            success: function(respuesta) {

                if(respuesta.status === 'FAIL'){
                    mensajeError("#mensajes", tradsNotificacion['notificacion.nueva.error']);
                }else if(respuesta.status === 'SUCCESS') { // Si no hay errores

                    limpiarNuevaNotificacion();
                    mensajeSuccess("#mensajes", tradsNotificacion['notificacion.nueva.ok']);
                }

                $("#modalCompose").modal('hide');
                waitingDialog.hide();
            }
        });
    }

    event.preventDefault();
}

/**
 *
 * @param idNotificacion
 */
function verNotificacion(idNotificacion){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarNotificacion();

    //Obtenemos los datos de la Integración
    $.ajax({
        url: urlObtenerNotificacion,
        data: { idNotificacion: idNotificacion},
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            $('#asunto').html(result.asunto);
            $('#fechaEnviado').html(result.fechaEnviadoFormateada);
            $('#mensaje').html('<pre>'+result.mensaje+'</pre>');
            $('#remitente').html(result.nombreRemitente);
            $('#botonEliminar').attr('onClick', 'confirm("/regweb3/notificacion/'+result.id+'/eliminar","'+tradsNotificacion['notificacion.eliminar']+'")');

            if(result.estado == 0){ // Pendiente de leer
                $('#botonLeer').attr('onClick', 'confirm("/regweb3/notificacion/'+result.id+'/leer","'+tradsNotificacion['notificacion.confirmar.leer']+'")');
                $('#botonLeer').show();
            } else if(result.estado == 1){
                $('#botonLeer').hide();
            }


            if(result.tipo === 0){
                $('#fechaEnviado').after('<span id="tipoNotificacion" class="label label-warning pull-right">'+tradsNotificacion['notificacion.tipo.0']+'</span>');
            }else if(result.tipo === 1){
                $('#fechaEnviado').after('<span id="tipoNotificacion" class="label label-success pull-right">'+tradsNotificacion['notificacion.tipo.1']+'</span>');
            }else if(result.tipo === 2){
                $('#fechaEnviado').after('<span id="tipoNotificacion" class="label label-danger pull-right">'+tradsNotificacion['notificacion.tipo.2']+'</span>');
            }

            $("#modalLeer").modal('show');
        }
    });
}

/**
 *
 */
function limpiarNuevaNotificacion(){
    $('#errores').html('');
    $('#errores').hide();
    $('#destinatarios').val('');
    $('#destinatarios').trigger("chosen:updated");
    $('#tipo').val('0');
    $('#tipo').trigger("chosen:updated");
    $('#asuntoNotificacion').val('');
    $('#mensajeNotificacion').val('');

}

/**
 *
 */
function limpiarNotificacion(){
    $('#mensaje').html('');
    $('#asunto').html('');
    $('#remitente').html('');
    $('#fechaEnviado').html('');
    $('#tipoNotificacion').html('');
}