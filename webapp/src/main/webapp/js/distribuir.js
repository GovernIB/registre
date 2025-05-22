/**
 * javascript que implementa las funciones necesarias para distribuir un registro a una lista de destinatarios
 * obtenidos del plugin
 */


/**
 * Función para redistribuir un Registro ya distribuido previamente.
 * Solo funcionará para instalaciones donde no sea necesario escoger Destinatarios
 * @param url
 */
function redistribuir(url){

    jQuery.ajax({
        async: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(objeto){
            waitingDialog.show(traddistribuir['distribuir.distribuyendo'], {dialogSize: 'm', progressType: 'success'});
        },
        success:function(respuesta){

            if( respuesta.status == 'SUCCESS' || respuesta.status == 'ENVIADO_COLA'){
                mensajeSuccess('#mensajes', 'Registro redistribuido correctamente');
                waitingDialog.hide();
                return false;
            }else{
                //Si ha ocurrido un fallo en el envio
                if(respuesta.status == 'FAIL'){
                    mensajeError('#mensajes', respuesta.error);
                    waitingDialog.hide();
                    return false;
                }

            }
            waitingDialog.hide();
        }

    });
}

/*
 * Función que permite distribuir el registro a los destinatarios que se le indiquen.
 * Realiza una llamada ajax para obtener los destinatarios
 * si son modificables muestra el pop up para poder modificarlos
 * si no lo son redirecciona directamente a los destinatarios devueltos.
 * si no hay destinatarios se marca el registro como tramitado y listo.
 */
function distribuir() {

    var html = '';

    jQuery.ajax({
        async: true,
        url: urlDistribuir,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(objeto){
            waitingDialog.show(traddistribuir['distribuir.distribuyendo'], {dialogSize: 'm', progressType: 'success'});
        },
        success:function(respuesta){

            if( respuesta.status === 'SUCCESS' || respuesta.status === 'ENVIADO_COLA'){
                goTo(urlDetalle);

            }else if(respuesta.status === 'FAIL') {//Si ha ocurrido un fallo en el envio
                mensajeError('#mensajes', respuesta.error);
                waitingDialog.hide();
                return false;

            }

            waitingDialog.hide();
        }

    });
}

function mostrarFormulario(urlDescarga) {
	var loaded = $('.resumen-documento').length > 0;
	var $formularioContainer = $('.formulario_container');
	var $procedimientoContainer = $('.procedimiento_container');
	
	if (! loaded) {
	    jQuery.ajax({
	        async: true,
	        url: urlDescarga,
	        type: 'GET',
	        dataType: 'json',
	        contentType: 'application/json',
			beforeSend: function(objeto){
				$formularioContainer.addClass('revocar_loader');
	        },
	        success:function(revocacionDto) {
				var documento = revocacionDto.documento;
				var base64 = documento.contenido;
	                		
	    	    // Convierte el contenido base64 a un objeto Blob
				const binary = atob(base64);
	    	    const len = binary.length;
	    	    const buffer = new Uint8Array(len);
	    	    for (let i = 0; i < len; i++) {
	    	    	buffer[i] = binary.charCodeAt(i);
		        }
	    		const blob = new Blob([buffer], { type: documento.mimeType });
	    	
				// Crea una URL para el blob y carga el PDF en el iframe
	    	    const url = URL.createObjectURL(blob);
	    	                
	    	    const iframe = document.createElement("iframe");
	    	    iframe.id = "pdfPreview";
	    	    iframe.height = "400";
	    	    iframe.width = "100%";
	    	    iframe.src = url;
	    	                
	    	    let resumenDocumento = $('<div>', { class: 'resumen-documento' });
				resumenDocumento.append(iframe);
	    	                
				$('.wrapper').append(resumenDocumento);
	
				$formularioContainer.removeClass('revocar_loader');
				
				$formularioContainer.parent().removeClass('formulario_container_loading');
				$formularioContainer.parent().addClass('formulario_container_loaded');
				
				$procedimientoContainer.show();
	        }
	
	    });
	}
}