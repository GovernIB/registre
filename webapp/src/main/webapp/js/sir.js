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
                mensajeSuccess("#mensajes", tradsRegistroSir['registroSir.ACK.enviado.ok']);
            }else{
                mensajeError("#mensajes", tradsRegistroSir['registroSir.ACK.enviado.error']);
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