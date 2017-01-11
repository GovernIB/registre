/**
 * javascript que implementa las funciones necesarias para distribuir un registro a una lista de destinatarios
 * obtenidos del plugin
 */


/*
 * Función que permite distribuir el registro a los destinatarios que se le indiquen.
 * Realiza una llamada ajax para obtener los destinatarios
 * si son modificables muestra el pop up para poder modificarlos
 * si no lo son redirecciona directamente a los destinatarios devueltos.
 * si no hay destinatarios se marca el registro como tramitado y listo.
 */
function distribuir() {

    var html = '';
    limpiarDistribuir();

    jQuery.ajax({
        async: true,
        url: urlDistribuir,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {

            // Si no hay plugin definido, o el plugin ya envió directamente el registro,
            // se marca como Tramitado y recargamos página.

            if (!result.hayPlugin || result.enviado){
                goTo(urlDetalle);
            }else{

                // Si se debia enviar directamente sin seleccionar destinatarios y falló el envío directo
                if (!result.listadoDestinatariosModificable && !result.enviado) {
                    // Error en el envio
                    mensajeError('#mensajesdetalle', traddistribuir['distribuir.noenviado']);
                }


                // Si hay destinatarios mostramos pop-up, solo hay este caso.
                if (result.destinatarios != null) {
                    if ((result.destinatarios.posibles != null && result.destinatarios.posibles.length > 0) || (result.destinatarios.propuestos != null && result.destinatarios.propuestos.length > 0)) { // Si hay destinatarios, mostramos pop-up

                        // Pintamos el select con las opciones propuestas seleccionadas y las posibles sin seleccionar
                        html += '<select data-placeholder="" id="destinatarios"  name="destinatarios"  class="chosen-select" multiple="true">';
                        var lenposibles = result.destinatarios.posibles.length;
                        for (var i = 0; i < lenposibles; i++)
                            html += '<option value="' + result.destinatarios.posibles[i].id + '">'
                                + result.destinatarios.posibles[i].name + '</option>';
                        var lenpropuestos = result.destinatarios.propuestos.length;
                        for (var j = 0; j < lenpropuestos; j++)
                            html += '<option value="' + result.destinatarios.propuestos[j].id + '" selected="selected">'
                                + result.destinatarios.propuestos[j].name + '</option>';

                        html += '</select>';
                        html += '<span id="destinatariosError"></span>';
                        $('#divdestinatarios').html(html);

                        $('#destinatarios').chosen({width: "100%"});
                        $('#destinatarios').trigger("chosen:update");

                        $('#distribuirModal').modal('show');


                    } else { // No hay destinatarios
                        mensajeError('#mensajesdetalle', traddistribuir['distribuir.nodestinatarios']);

                    }
                } else { // No hay destinatarios
                    mensajeError('#mensajesdetalle', traddistribuir['distribuir.nodestinatarios']);

                }

            }


        }


    });
}


/**
 * Función que recoge la lista de destinatarios propuestos y modificados por el usuario y los distribuye.
 */
function enviarDestinatarios() {


    var html = "";
    var destinatarios = [];
    var destinatariosarray = "";

    // Coegemos los destinatarios que han seleccionado en el select "destinatarios"
    if ($('#destinatarios :selected').length > 0) {
        $('#distribuirModal').modal('hide');
        //build an array of selected values
        destinatariosarray = "[";
        $('#destinatarios :selected').each(function (i, selected) {
            html += "<li>" + $(selected).text() + "</li>";
            destinatarios[i] = '{"id":"' + $(selected).val() + '","name":"' + $(selected).text() + '"}';
            // Colocamos la coma de separación
            if (i != 0 && i < $('#destinatarios :selected').length) {
                destinatariosarray += ",";
            }
            destinatariosarray += destinatarios[i];
        });
        destinatariosarray += "]";
        //Pintamos los destinatarios escogidos
        $('#listadestin').html(html);
        $('#modalDistribDestinatarios').modal('show');

        // var destinatarios = [{"id":"a","name":"shail1"}, {"id":"b","name":"shail2"}];
        //var destinatario = {"id":"a","name":"shail1"};

        var observ = $('#observtramit').val();


        /* HAY que montar el string manual(no se porque), pero funciona */
        var json = '{"destinatarios":' + destinatariosarray + ',"observaciones":"' + observ + '"}';


        jQuery.ajax({
            url: urlEnviarDestinatarios,
            type: 'POST',
            dataType: 'json',
            data: json,
            contentType: 'application/json',
            success: function (result) {

                if(result){
                    goTo(urlDetalle);
                }else{
                    $('#modalDistribDestinatarios').modal('hide');
                    mensajeError('#mensajesdetalle', traddistribuir['distribuir.noenviado']);
                }

            }
        });
    } else {
        var variable = "#destinatarios";
        variable = variable + "Error"; // #destinatariosError

        // Mostramos los errores de validación encontrados

        var htmlError = "<span id=\"destinatariosError\" class=\"help-block\"> " + traddistribuir['campo.obligatorio'] + "</span>";

        $(variable).html(htmlError);
        $(variable).parents(".form-group").addClass("has-error");

    }

}

function limpiarDistribuir() {
    quitarError('destinatarios');
    $('#observtramit').val('');
}