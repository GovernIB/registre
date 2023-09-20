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
 * Reenviar mensaje de control
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
 * Volver a enviar un intercambio con la libreria LIBSIR
 * @param idOficioRemision
 */
function reencolarIntercambio(oficina, idIntercambio){

         // Reenviamos intercambio
         $.ajax({
             url: urlReencolarIntercambio,
             data: {oficina: oficina, idIntercambio: idIntercambio},
             type: "GET",
             dataType: 'json',
             contentType: 'application/json',

             success: function (result) {

                 if (result === true) {
                     mensajeSuccess("#mensajes", tradsSir['intercambio.reenviado.ok']);
                 } else {
                     mensajeError("#mensajes", tradsSir['intercambio.reenviado.error']);
                 }
             }
         });

}

/**
 * Marcar como Error Técnico intercambio con la libreria LIBSIR
 * @param idOficioRemision
 */
function marcarErrorTecnicoIntercambio(oficina, idIntercambio){

    // Marcamos como error técnico

        $.ajax({
            url: urlMarcarErrorTecnicoIntercambio,
            data: {idIntercambio: idIntercambio, oficina: oficina},
            type: "GET",
            dataType: 'json',
            contentType: 'application/json',

            success: function (result) {

                if (result === true) {
                    mensajeSuccess("#mensajes", tradsSir['intercambio.marcado.ok']);
                } else {
                    mensajeError("#mensajes", tradsSir['intercambio.marcado.error']);
                }
            }
        });

}


/**
 * Desmarcar como Error Técnico intercambio con la libreria LIBSIR
 * @param idOficioRemision
 */
function desmarcarErrorTecnicoIntercambio(oficina, idIntercambio){

    // Marcamos como error técnico
    if(confirm("", tradsSir['intercambio.confirmar.desmarcar'])) {
        $.ajax({
            url: urlDesmarcarErrorTecnicoIntercambio,
            data: {idIntercambio: idIntercambio, oficina: oficina},
            type: "GET",
            dataType: 'json',
            contentType: 'application/json',

            success: function (result) {

                if (result === true) {
                    mensajeSuccess("#mensajes", tradsSir['intercambio.desmarcado.ok']);
                } else {
                    mensajeError("#mensajes", tradsSir['intercambio.desmarcado.error']);
                }
            }
        });
    }
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


//Funciones modales para SIR
function reencolarIntercambioModal(oficina, idIntercambio,confirmModal){

    confirmModal.modal("show");

    confirmModal.find("#cancelButton").click(function (event) {
        confirmModal.modal("hide");
    });


    confirmModal.find("#okButton").click(function (event) {
        confirmModal.modal("hide");
        reencolarIntercambio(oficina,idIntercambio);

    });
}

function marcarErrorTecnicoIntercambioModal(oficina, idIntercambio, confirmModal){

    confirmModal.modal("show");

    confirmModal.find("#cancelButton").click(function (event) {
        confirmModal.modal("hide");
    });


    confirmModal.find("#okButton").click(function (event) {
        confirmModal.modal("hide");
        marcarErrorTecnicoIntercambio(oficina,idIntercambio);

    });
}


function desmarcarErrorTecnicoIntercambioModal(oficina, idIntercambio, confirmModal){

    confirmModal.modal("show");

    confirmModal.find("#cancelButton").click(function (event) {
        confirmModal.modal("hide");
    });


    confirmModal.find("#okButton").click(function (event) {
        confirmModal.modal("hide");
        reencolarIntercambio(oficina,idIntercambio);

    });
}