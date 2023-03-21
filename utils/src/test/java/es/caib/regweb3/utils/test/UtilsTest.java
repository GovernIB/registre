package es.caib.regweb3.utils.test;

import es.caib.regweb3.utils.StringUtils;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void capitalizeWords() throws Exception {

        System.out.println(StringUtils.capitailizeWord("TALLERES HNOS. J Y J. TORRES, CB", true));
        System.out.println(StringUtils.capitailizeWord("Dr. juan carLos De lA martínez", false));
    }


    @Test
    public void sustituirCaracteres(){
        String cadena = "taxa_bonif_50_Fam_Num_B1_SET21_Garippa.pdf";

        System.out.println("Cadena inicial: " + cadena);

        System.out.println("Cadena corregida: " + StringUtils.eliminarCaracteresProhibidosArxiu(cadena));
    }

    @Test
    public void sustituirCaracteresSIR(){
        String cadena = "IMP_Informe_159-20-CA_casa_Sa_Pa_sa_den_Xumeu_Pere_St_Josep_de_Sa_Talaia%09.002.pdf";

        System.out.println("Cadena inicial: " + cadena);

        System.out.println("Cadena corregida: " + StringUtils.sustituirCaracteresProhibidosSIR(cadena,'_'));
    }

    @Test
    public void eliminarCaracteresSIR(){
        String cadena = "IMP_Informe_159-20-CA_casa_Sa_Pa_sa_den_Xumeu_Pere_St_;Josep_de_Sa_Talaia%09.002.pdf";

        System.out.println("Cadena inicial: " + cadena);

        System.out.println("Cadena corregida: " + StringUtils.eliminarCaracteresProhibidosSIR(cadena));
    }

    @Test
    public void recortarCadena(){
        String cadena = "taxa_bonif_50_Fam_Num_B1_SET21_Garippa.pdf";

        System.out.println("Cadena inicial: " + cadena);

        System.out.println("Cadena corregida: " + StringUtils.recortarCadena(cadena,20));
    }
}
