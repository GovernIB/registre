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

        System.out.println("Cadena corregida: " + StringUtils.sustituirCaracteresProhibidosArxiu(cadena,'_'));
    }

    @Test
    public void recortarCadena(){
        String cadena = "taxa_bonif_50_Fam_Num_B1_SET21_Garippa.pdf";

        System.out.println("Cadena inicial: " + cadena);

        System.out.println("Cadena corregida: " + StringUtils.recortarCadena(cadena,20));
    }
}
