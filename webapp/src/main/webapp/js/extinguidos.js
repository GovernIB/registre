/**
 * Función que envia los datos del formulario de un organismo extinguido para procesarlo
 * @param extinguido id del organismo extinguido
 */
function procesarExtinguido(extinguido) {

    var url = $("#extinguidoForm"+extinguido).attr("action");
    var total =  $('#total'+extinguido).val(); // valor total de libros
    var array = [];

    // Montamos los pares libro-organismo en función del total de libros
    for( i = 1; i <= total; i++) {
        var libroOrganismo = { "libro": $("#libro"+i+"-"+extinguido).val(), "organismo": $("#organismoSustituye"+i+"-"+extinguido).val()};
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
            var idPanel = '#panel'+extinguido;
            if(respuesta.status == 'FAIL'){
                  mostrarMensaje('#pendientes', "S'ha produit un error processant l'organisme extingit " + extinguido);
                  $(idPanel).hide();

            }else if(respuesta.status == 'SUCCESS'){
                  mostrarMensaje('#pendientes', "S'ha processat correctament l'organisme extingit " + extinguido);
                  $(idPanel).hide();
                  mostrarProcesado(extinguido, respuesta.result.nombre,respuesta.result.libroOrganismos);
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
 *  Función que muestra como quedan asignados los libros una vez procesado el organismo
 * @param extinguidoId identificador del organismo extinguido
 * @param extinguidoNombre  nombre del Organismos extinguido
 * @param librosOrganismos  conjunto de relaciones de libro-organismo del organismo extinguido
 */
 function mostrarProcesado(extinguidoId, extinguidoNombre, librosOrganismos){

    // El html que se coge es el del resumen de los organismos procesados automaticamente
    // a este código se añadirá la nueva información a mostrar en una tabla
    var html=$("#resumen").html();
    html +=  "Organisme Extingit: <strong>"+extinguidoNombre+"</strong>"
    html += "<table class='table table-bordered table-hover table-striped'  id=\"procesado"+extinguidoId +"\">";
    html += '<colgroup>';
    html += '<col>';
    html += '<col>';
    html += '</colgroup>';
    html += '<thead>';
    html += '<tr>';
    html += '<th>Llibre</th>';
    html += '<th>Organisme Asignat </th>';
    html += '</tr>';
    html += '</thead>';
    html += '<tbody></tbody></table>';

    $('#resumen').html(html);
    // añadimos la información de libro-organismo
    var fila;
    for(i=0; i<librosOrganismos.length; i++){
        fila = "<tr><td>"+librosOrganismos[i].libro+"</td><td>"+librosOrganismos[i].organismo+"</td>"
        $('#procesado'+extinguidoId).append(fila);
    }

}


