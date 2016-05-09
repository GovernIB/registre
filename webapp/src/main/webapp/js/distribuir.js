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
function distribuir(url, urlEnviarDestinatarios, urlDetalle, urlTramitar) {
    var html = '';
    limpiarDistribuir();

    jQuery.ajax({
        async: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
            if (result != null && result.propuestos != null) { // Si hay destinatarios, mostramos pop-up
                //Rellenamos el select de posibles
                var lenposibles = result.posibles.length;
                for (var i = 0; i < lenposibles; i++)
                    html += '<option value="' + result.posibles[i].id + '">'
                        + result.posibles[i].name + '</option>';
                $('#posibles').trigger("chosen:updated");
                $('#posibles').html(html);


                //Rellenamos el select de propuestos
                html = '';
                var lenpropuestos = result.propuestos.length;
                for (var j = 0; j < lenpropuestos; j++)
                    html += '<option value="' + result.propuestos[j].id + '">'
                        + result.propuestos[j].name + '</option>';
                $('#propuestos').html(html);
                $('#distribuirModal').modal('show');

            } else { // No hay destinatarios, se marca como tramitado
                goTo(urlTramitar);
            }
        }
    });
}

/**
 * Función que recoge la lista de destinatarios propuestos y modificados por el usuario y los distribuye.
 */
function enviarDestinatarios(url, urlDetalle) {


    var html = "";
    var destinatarios = [];
    var destinatariosarray = "";

    //Seleccionamos todos por defecto y así se enviaran todos, que es el comportamiento normal.
    $('#propuestos option').prop('selected', true);
    // Coegemos los destinatarios que han seleccionado en el combo "propuestos"
    if ($('#propuestos :selected').length > 0) {
        $('#distribuirModal').modal('hide');
        //build an array of selected values
        destinatariosarray = "[";
        $('#propuestos :selected').each(function (i, selected) {
            html += "<li>" + $(selected).text() + "</li>";
            destinatarios[i] = '{"id":"' + $(selected).val() + '","name":"' + $(selected).text() + '"}';
            // Colocamos la coma de separación
            if (i != 0 && i < $('#propuestos :selected').length) {
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
            url: url,
            type: 'POST',
            dataType: 'json',
            data: json,
            contentType: 'application/json',
            success: function (result) {
                goTo(urlDetalle);

            }
        });
    } else {
        var variable = "#propuestos";
        variable = variable + "Error"; // #propuestosError

        // Mostramos los errores de validación encontrados

        var htmlError = "<span id=\"propuestosError\" class=\"help-block\"> " + traddistribuir['campo.obligatorio'] + "</span>";

        $(variable).html(htmlError);
        $(variable).parents(".form-group").addClass("has-error");

    }

}

function limpiarDistribuir() {
    quitarError('propuestos');
    $('#observtramit').val('');
}