var itervalProgres;
var writtenLines = 0;
$(document).ready(function() {
	
	$('#rangoFechasBusqueda').on("submit", function(){
		
		$('#modalImportacioRegistrosSir').modal('show');
		
		refreshProgres();
	});
});

function refreshProgres() {
	itervalProgres =  setInterval(getProgres, 350);
}

function getProgres() {
	console.log("getProgres");
	$('.close', parent.document).prop('disabled', true);
	$.ajax({
		type: 'GET',
		url: urlProgreso,
		success: function(data) {
			if (data) {
				writeInfo(data);
				if (data.progreso != undefined) {
					$('#bar').css('width', data.progreso + '%');
					$('#bar').attr('aria-valuenow', data.progreso);
					$('#bar').html(data.progreso + '%');
					if (data.progreso == 100) {
						clearInterval(itervalProgres);
					}
				}
			}
		},
		error: function() {
			console.log("error obtenint progrés...");
			$('.close', parent.document).prop('disabled', false);
		}
	});
}

function writeInfo(data) {
	let info = data.info;
	let index;
	if (info != undefined) {
		let scroll = writtenLines < info.length;
		console.log("Scrol?: ", writtenLines, info.length, scroll);
		for (index = writtenLines; index < info.length; index++) {
			$("#bcursor").before("<p class='info-" + info[index].tipo + "'>" + info[index].texto + "</p>");
		}
		writtenLines = index;
		if (data.error) {
			$("#bcursor").before("<p class='info-ERROR'>" + data.errorMsg + "</p>");
		}
		//scroll to the bottom of "#actualitzacioInfo"
		if (scroll) {
			var infoDiv = document.getElementById("actualitzacionInfo");
			infoDiv.scrollTop = infoDiv.scrollHeight;
		}
	}
}

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
 * Volver a enviar un intercmbio
 * @param idOficioRemision
 */
function reenviarIntercambio(idOficioRemision){

    // Reenviamos el mensaje de control
    $.ajax({
        url: urlReenviarIntercambio,
        data: { idOficioRemision: idOficioRemision },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            if(result === true){
                mensajeSuccess("#mensajes", tradsSir['intercambio.reenviado.ok']);
            }else{
                mensajeError("#mensajes", tradsSir['intercambio.reenviado.error']);
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

/**
 * Envia un mensaje ACK
 * @param id
 * @param url
 */
function actualizarRegistroSir(id, url){
    //Reiniciamos el contador de reintentos
    $.ajax({
        url: url,
        data: { id: id },
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            if(result === true){
                mensajeSuccess("#mensajes", tradsSir['registroSir.actualizar.ok']);
            }else{
                mensajeError("#mensajes", tradsSir['registroSir.actualizar.error']);
            }
        }
    });
}