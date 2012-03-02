/*  
 * Funciones estandar JS
 *
*/

  /* Comprobamos si el navegador es IE */
     function CheckIsIE() { 
        if  (navigator.appName.toUpperCase() == 'MICROSOFT INTERNET EXPLORER')  { return true;}
        else { return false; }
     }

     /* Imprimir un iframe */
     function imprime(sello) {
        if (CheckIsIE() == true) {
            parent[sello].focus();
            parent[sello].print();
        } else {
            window.frames[sello].focus();
            window.frames[sello].print();
        }
      }

    /* trim(), devuelve un string */
    function trim(s) {
        while (s.length>0 && (s.charAt(0) == ' ' || s.charAt(0) == '\n' || s.charAt(0) == '\r')) {
            s = s.substring(1,s.length);
        }
        while (s.length>0 && (s.charAt(s.length-1) == ' ' || s.charAt(s.length-1) == '\n') || s.charAt(s.length-1) == '\r') {
            s = s.substring(0,s.length-1);
        }
        return s;
    }

    function getkey(e) {
        if (window.event)
            return window.event.keyCode;
        else if (e)
            return e.which;
        else
            return null;
    }
    
    /* Caracteres validos */
        function goodchars(e, goods) {
            var key, keychar;
            key = getkey(e);
            if (key == null) return true;

            // get character
            keychar = String.fromCharCode(key);
            keychar = keychar.toLowerCase();
            goods = goods.toLowerCase();

            // check goodkeys
            if (goods.indexOf(keychar) != -1)
                return true;

            // control keys
            if ( key==null || key==0 || key==8 || key==9 || key==13 || key==27 )
                return true;

            // else return false
            return false;
         }

    function check(e){
        var key, keychar;
        key = getkey(e);

        if (key == null) return true;
        if (key == 8) return true;
        if (key == 8364) return false; // No dejemos introducir el caracter euro
        if (key==13) return false;
    } 
    
    function checkComentario(e){     
        if (document.registroForm.comentario && document.registroForm.comentario.value.length>159) {
            alert("No es permeten més caràcters a l'extracte.");
            return false;
        } else {
            return check(e);
        }
    }

// Leemos todas las Cookies
    function leeAllCookies(nombre) {
        nombresCookies = new Array();
        cookie=document.cookie.split("; ");
        n=0;
        for (i=0; i < cookie.length; i++) {
            if (cookie[i].indexOf(nombre)==0) {
                nombresCookies[n++]=URLDecode(cookie[i].substring(0,cookie[i].indexOf("=")));
            }
        }
        return nombresCookies;
    }

    function getCookie(name){
        var cname=name + "=";
        var dc=document.cookie;  
        if(dc.length>0) { 
	          begin=dc.indexOf(cname);  
                  if(begin!=-1){
		    begin+=cname.length;
	            end=dc.indexOf(";",begin);
                    if(end==-1)
			     end=dc.length;
		    return(dc.substring(begin,end));
                  }
        }
     }


// Borramos una cookie
    function borraCookie(nombre) {
        if(getCookie(URLEncode(nombre))){
            document.cookie = URLEncode(nombre) + "=" + escape(getCookie(URLEncode(nombre))) + "; path=/ ; expires=Fri, 31 Dec 1999 23:59:59 GMT;";
        }
    }

// Cambiamos el texto de un id
    function cambiaTexto(value, id) {
        nodo = document.getElementById(id);
        if (nodo.childNodes.length!=0) {
            nodo.removeChild(nodo.childNodes.item(0));
        }
        nodo.appendChild(document.createTextNode(value));
    }
function URLEncode(plaintext)
{
	// The Javascript escape and unescape functions do not correspond
	// with what browsers actually do...
	var SAFECHARS = "0123456789" +					// Numeric
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +	// Alphabetic
					"abcdefghijklmnopqrstuvwxyz" +
					"-_.!~*'()";					// RFC2396 Mark characters
	var HEX = "0123456789ABCDEF";

	var encoded = "";
	for (var i = 0; i < plaintext.length; i++ ) {
		var ch = plaintext.charAt(i);
	    if (ch == " ") {
		    encoded += "+";				// x-www-urlencoded, rather than %20
		} else if (SAFECHARS.indexOf(ch) != -1) {
		    encoded += ch;
		} else {
		    var charCode = ch.charCodeAt(0);
			if (charCode > 255) {
			    alert( "Unicode Character '" 
                        + ch 
                        + "' cannot be encoded using standard URL encoding.\n" +
				          "(URL encoding only supports 8-bit characters.)\n" +
						  "A space (+) will be substituted." );
				encoded += "+";
			} else {
				encoded += "%";
				encoded += HEX.charAt((charCode >> 4) & 0xF);
				encoded += HEX.charAt(charCode & 0xF);
			}
		}
	} // for

    return encoded;
};

function URLDecode(encoded) {
   // Replace + with ' '
   // Replace %xx with equivalent character
   // Put [ERROR] in output if %xx is invalid.
   var HEXCHARS = "0123456789ABCDEFabcdef"; 
   var plaintext = "";
   var i = 0;
   while (i < encoded.length) {
       var ch = encoded.charAt(i);
	   if (ch == "+") {
	       plaintext += " ";
		   i++;
	   } else if (ch == "%") {
			if (i < (encoded.length-2) 
					&& HEXCHARS.indexOf(encoded.charAt(i+1)) != -1 
					&& HEXCHARS.indexOf(encoded.charAt(i+2)) != -1 ) {
				plaintext += unescape( encoded.substr(i,3) );
				i += 3;
			} else {
				alert( 'Bad escape combination near ...' + encoded.substr(i) );
				plaintext += "%[ERROR]";
				i++;
			}
		} else {
		   plaintext += ch;
		   i++;
		}
	} // while

   return plaintext;
};

function esEmail (obj) 
{
    if (obj.value.length == 0) return false;
	var er_email = /^([^@\s]+@[^@\.\s]+(\.[^@\.\s]+)+|^)$/
	if (!er_email.test(obj.value))
		return false;
	return true;
};

function verHistoricoEmails(ofi,num,ano,tipus)
{
	var url = "veureHistoric?oficina="+ofi+"&numero="+num+"&ano="+ano+"&tipus="+tipus;
	miVentana = window.open(url, "veureHistoric","scrollbars,resizable,width=600,height=500");
	miVentana.focus(); 		
};
