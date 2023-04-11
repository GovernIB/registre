// Muestra el cuadro de confirmación para distribuir un registro
function confirmDistribuir(mensaje, distribucionEmail) {

    var confirmModal =
        $("<div class=\"modal fade\">" +
            "<div class=\"modal-dialog\">" +
            "<div class=\"modal-content\">" +
            "<div class=\"modal-header\">" +
            "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
            "<h4 class=\"modal-title\">Confirmar</h4>" +
            "</div>" +

            "<div class=\"modal-body\">" +
            "<p>" + mensaje + "</p>" +
            "</div>" +

            "<div class=\"modal-footer\">" +
            "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>" +
            "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>");

    confirmModal.find("#okButton").click(function (event) {
        if(distribucionEmail){
            $('#distribuirModal').modal('show');
        }else{
            distribuir();
        }

        confirmModal.modal("hide");
    });

    confirmModal.modal("show");
}


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

    var json = { "emails": $("#distribuirForm textarea[name=emails]").val(), "motivo": $("#distribuirForm textarea[name=motivo]").val()};

    jQuery.ajax({
        async: true,
        url: urlDistribuir,
        type: 'POST',
        data: JSON.stringify(json),
        dataType: 'json',
        contentType: 'application/json',
        beforeSend: function(objeto){
            waitingDialog.show(traddistribuir['distribuir.distribuyendo'], {dialogSize: 'm', progressType: 'success'});
        },
        success:function(respuesta){

            if( respuesta.status === 'SUCCESS' || respuesta.status === 'ENVIADO_COLA'){
                waitingDialog.hide();
                goTo(urlDetalle);

            }else if(respuesta.status === 'FAIL') {//Si ha ocurrido un fallo en el envio
                waitingDialog.hide();
                mensajeError('#mensajes', respuesta.error);
                return false;
            }
        }
    });
}


/**
 * Valida los campos del formulario y distribuye el registro
 * @param aceptarRegistroSir Si se trata de la aceptación de un registro SIR
 */

function validarFormEmail(aceptarRegistroSir) {

    //Obtenemos los valores del formulario
    var emails = $("#distribuirForm textarea[name=emails]").val();
    var motivo = $("#distribuirForm textarea[name=motivo]").val();

    // Validamos los campos
    if(validaCampo(emails,'idEmails') && validaCampo(motivo,'idMotivo')) {

        if(aceptarRegistroSir){ // Si se trata de la aceptación de un RegistroSir, procesaremos el formulario de registroSirDetalle.jsp
            $("#registrarForm input[name=emails]").val(emails);
            $("#registrarForm input[name=motivo]").val(motivo);
            $('#distribuirModal').modal('hide');
            waitingDialog.show(traddistribuir['distribuir.distribuyendo'], {dialogSize: 'm', progressType: 'success'});
            doForm('#registrarForm');
        }else{ // Distribuimos normalmente
            distribuir();
            $('#distribuirModal').modal('hide');
        }

    }else{
        return false;
    }
}