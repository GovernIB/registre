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