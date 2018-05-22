/**
 * Created by mgonzalez on 14/05/2018.
 */
/**
 * Carga los datos de un elemento de la cola para mostrarlos en un modal
 * @param idCola
 */
function infoCola(idCola){

    // Eliminamos el contenido del formulario y los mensajes de error
    limpiarCola();

    //Obtenemos los datos de la Integración
    $.ajax({
        url: urlobtenerCola,
        data: { idCola: idCola},
        type: "GET",
        dataType: 'json',
        contentType: 'application/json',

        success: function(result) {

            if(result.error != null){
                $('#error').html(result.error);
                $('#errorBox').show();
            }else{
                $('#errorBox').hide();
            }


        }
    });
}

/**
 * Limpia los campos de la integración
 */
function limpiarCola(){

    $('#error').html('');

}
