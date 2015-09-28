/**
 * Función que envia los datos del formulario de un organismo para procesarlo
 * @param organismoAProcesar id del organismo a procesar
 */
function procesarOrganismo(organismoAProcesar) {

    var url = $("#organismoAProcesarForm"+organismoAProcesar).attr("action");
    var total =  $('#total'+organismoAProcesar).val(); // valor total de libros
    var array = [];


    // Montamos los pares libro-organismo en función del total de libros
    for( i = 1; i <= total; i++) {
        var libroOrganismo = { "libro": $("#libro"+i+"-"+organismoAProcesar).val(), "organismo": $("#organismoSustituye"+i+"-"+organismoAProcesar).val()};
        array[i-1] = libroOrganismo;

    }

     $.ajax({
        url: url,
        data: JSON.stringify(array),
        type: "POST",

        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        success: function(respuesta) {
            var idPanel = '#panel'+organismoAProcesar;
            if(respuesta.status == 'FAIL'){
                  mostrarMensaje('#pendientes', trads['mensajeprocesadoerror'] + " " + organismoAProcesar);
                  $(idPanel).hide();

            }else if(respuesta.status == 'SUCCESS'){
                  //mostrarMensaje('#pendientes', trads['mensajeprocesadook'] + " " + organismoAProcesar);
                  mostrarMensaje('#pendientes', trads['mensajeprocesadook'] + " " +respuesta.result.nombre);
                  $(idPanel).hide();
                  mostrarProcesado(organismoAProcesar, respuesta.result.nombre,respuesta.result.libroOrganismos);
            }
        }
     });
}

/**
 * Función que permite mostrar un mensaje en un panel
 * @param idPanel
 * @param mensaje
 */
function mostrarMensaje(idPanel, mensaje){

    $(idPanel).append('<div class="alert alert-success alert-dismissable"><strong>'+mensaje+'</strong><br></div>');


}


/**
 * Función que muestra como quedan asignados los libros una vez procesado el organismo
 * @param organismoAProcesarId identificador del organismo a procesar
 * @param organismoAProcesarNombre  nombre del Organismos a procesar
 * @param librosOrganismos  conjunto de relaciones de libro-organismo del organismo a procesar
 */
 function mostrarProcesado(organismoAProcesarId, organismoAProcesarNombre, librosOrganismos){

    // El html que se coge es el del resumen de los organismos procesados automaticamente
    // a este código se añadirá la nueva información a mostrar en una tabla
    var html=$("#resumen").html();
   /* html +=  trads['organismo.extinguido']+" <strong>"+organismoAProcesarNombre+"</strong>"*/
   // html +=  "Organisme Extingit: <strong>"+extinguidoNombre+"</strong>"
    html += "<table class='table table-bordered table-hover table-striped'  id=\"procesado"+organismoAProcesarId +"\">";
    html += '<colgroup>';
    html += '<col>';
    html += '<col>';
    html += '</colgroup>';
    html += '<thead>';
    html += '<tr>';
    html += '<th>'+trads['organismo.extinguido']+' </th>';
    html += '<th>'+trads['organismo.asignado']+' </th>';
    html += '<th>'+trads['libro.libro']+'</th>';
    html += '</tr>';
    html += '</thead>';
    html += '<tbody></tbody></table>';

    $('#resumen').html(html);
    // añadimos la información de libro-organismo
    var fila;
    for(i=0; i<librosOrganismos.length; i++){
        fila = "<tr><td>"+organismoAProcesarNombre +"</td><td>"+librosOrganismos[i].organismo+"</td><td>"+librosOrganismos[i].libro+"</td>"
        $('#procesado'+organismoAProcesarId).append(fila);
    }

}


