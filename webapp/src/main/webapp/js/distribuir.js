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
 * Función que permite determinar el plugin de distribución
 */
function distribuir() {

    var html = '';

    jQuery.ajax({
        async: true,
        url: urlDeterminarPluginDistrib,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success:function(respuesta){

            if( respuesta.status === 'SUCCESS' || respuesta.status === 'ENVIADO_COLA'){
                goTo(urlDetalle);

            }else if(respuesta.status === 'FAIL') {//Si ha ocurrido un fallo en el envio
                mensajeError('#mensajes', respuesta.error);
                return false;

            } else if(respuesta.status ==='ENVIO_MAIL'){
                $('#distribuirModal').modal('show');
            }

        }

    });
}