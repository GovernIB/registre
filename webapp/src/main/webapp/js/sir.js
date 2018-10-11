/**
 * Envia un mensaje ACK
 * @param idRegistroSir
 */
function enviarACK(idRegistroSir){

    //Enviamos el mensaje ACK
    $.ajax({
        url: urlEnviarACK,
        data: { idRegistroSir: idRegistroSir },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            if(result === true){
                mensajeSuccess("#mensajes", tradsMensajeControl['mensajeControl.ACK.enviado.ok']);
            }else{
                mensajeError("#mensajes", tradsMensajeControl['mensajeControl.ACK.enviado.error']);
            }
        }
    });
}

/**
 * Envia un mensaje de Confirmación
 * @param idRegistroSir
 */
function enviarConfirmacion(idRegistroSir){

    //Enviamos el mensaje de Confirmación
    $.ajax({
        url: urlEnviarConfirmacion,
        data: { idRegistroSir: idRegistroSir },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            if(result === true){
                mensajeSuccess("#mensajes", tradsMensajeControl['mensajeControl.confirmacion.enviado.ok']);
            }else{
                mensajeError("#mensajes", tradsMensajeControl['mensajeControl.confirmacion.enviado.error']);
            }
        }
    });
}

/**
 * Reenviar mensajde de control
 * @param idMensaje
 */
function reenviarMensaje(idMensaje){

    // Reenviamos el mensaje de control
    $.ajax({
        url: urlReenviarMensaje,
        data: { idMensaje: idMensaje },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            if(result === true){
                mensajeSuccess("#mensajes", tradsMensajesControl['mensajeControl.reenviado.ok']);
            }else{
                mensajeError("#mensajes", tradsMensajesControl['mensajeControl.reenviado.error']);
            }
        }
    });
}

/**
 * Envia un mensaje ACK
 * @param id
 * @param url
 */
function reiniciarContador(id, url){
    //Reiniciamos el contador de reintentos
    $.ajax({
        url: url,
        data: { id: id },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            if(result === true){
                mensajeSuccess("#mensajes", tradsSir['registroSir.reiniciar.ok']);
            }else{
                mensajeError("#mensajes", tradsSir['registroSir.reiniciar.error']);
            }
        }
    });
}