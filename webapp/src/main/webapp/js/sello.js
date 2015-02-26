$(document).ready(function() {

    var offset;
    var pageX;
    var pageY;
    var pageCoords;
    var w;
    var h;
    var orientacion;

    //posicionaSegellXY(155,14,"( 155, 14)");
    posicionaSegell('V1');
    orientacion = "V";
    $("#orientacion").val('V');

    $("#sello").mousemove(function( event ) {
        offset = $( this ).offset();
        pageX=(event.pageX - offset.left);
        pageY=(event.pageY - offset.top);
        pageCoords = "( " + pageX+ ", " + pageY + " )";

        //$( "#paginaX" ).text( "( event.pageX ) : " + pageX );
        //$( "#paginaY" ).text( "( event.pageY ) : " + pageY );
        //$( "#offsetTop" ).text( "offsetTop: " + offset.top );
        //$( "#offsetLeft" ).text( "offsetLeft: " + offset.left );
    });

    $("#sello").click(function( event ) {
        offset = $( this ).offset();
        pageX=(event.pageX - offset.left);
        pageY=(event.pageY - offset.top);
        var texto = "( " + pageX+ ", " + pageY + " )";
        posicionaSegellXY(pageX,pageY,texto);
    });
});

function posicionaSegellXY(x ,y, texto) {

    var pdfX=Math.floor(x/0.352777777777778);
    if(orientacion=="V"){
        var pdfY=Math.floor((297 - y)/0.352777777777778);
    }
    if(orientacion=="H"){
        var pdfY=Math.floor((210 - y)/0.352777777777778);
    }
    var ampleSegell=45;
    var altSegell=23;
    var minborde =10;

    if (x > (w - minborde - ampleSegell)) {
        x = w - minborde - ampleSegell;
    } else {
        if (x < minborde) {
            x = minborde;
        }
    }
    if (y > (h - minborde - altSegell)) {
        y = h - minborde - altSegell;

    } else {
        if (y < minborde) {
            y = minborde;
        }
    }

    //$( "#pdfX" ).text(pdfX);
    //$( "#pdfY" ).text(pdfY);

    $( '#x' ).val(pdfX);
    $( '#y' ).val(pdfY);

    $("#marca").css("left",x+altSegell-2);
    $("#marca").css("top",y+ampleSegell+85);
    $("#marca").html("("+x.toFixed() +","+y.toFixed() +")");
}

function posicionaSegell(pos) {

    var borde=19;
    var ampleSegell=45;
    var altSegell=23;
    var alineacio = pos[0];
    var num = pos[1];

    if (alineacio == 'V') {
        w = 210;
        h = 297;
        orientacion = "V";
    } else {
        w = 297;
        h = 210;
        orientacion = "H";
    }

    $("#orientacion").val(alineacio);
    $("#sello").css("height",h + 'px');
    $("#sello").css("width",w + 'px');

    switch(parseInt(num)){

        case 1:
        var texto = "( " + borde+ ", " + borde + " )";
        posicionaSegellXY(borde, borde, texto);
        break;

        case 2:
        var texto = "( " + Math.floor(w/2 - ampleSegell/2)+ ", " + borde + " )";
        posicionaSegellXY(Math.floor(w/2 - ampleSegell/2), borde, texto);
        break;

        case 3:
        var texto = "( " + w - ampleSegell - borde+ ", " + borde + " )";
        posicionaSegellXY(w - ampleSegell - borde, borde, texto);
        break;

        case 4:
        var texto = "( " + borde+ ", " + Math.floor(h/2 - altSegell/2) + " )";
        posicionaSegellXY(borde, Math.floor(h/2 - altSegell/2), texto);
        break;

        case 5:
        var texto = "( " + Math.floor(w/2 - ampleSegell/2)+ ", " + Math.floor(h/2 - altSegell/2) + " )";
        posicionaSegellXY(Math.floor(w/2 - ampleSegell/2), Math.floor(h/2 - altSegell/2), texto);
        break;

        case 6:
        var texto = "( " + w - ampleSegell - borde+ ", " + Math.floor(h/2 - altSegell/2) + " )";
        posicionaSegellXY(w - ampleSegell - borde, Math.floor(h/2 - altSegell/2), texto);
        break;

        case 7:
        var texto = "( " + borde+ ", " + h - borde - altSegell + " )";
        posicionaSegellXY(borde, h - borde - altSegell, texto);
        break;

        case 8:
        var texto = "( " + Math.floor(w/2 - ampleSegell/2)+ ", " + h - borde - altSegell + " )";
        posicionaSegellXY(Math.floor(w/2 - ampleSegell/2), h - borde - altSegell, texto);
        break;

        case 9:
        var texto = "( " + w - ampleSegell - borde+ ", " + h - borde - altSegell + " )";
        posicionaSegellXY(w - ampleSegell - borde, h - borde - altSegell, texto);
        break;
    }
}