package es.caib.regweb3.plugins.distribucion.email.test;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.plugins.distribucion.email.DistribucionEmailPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * @author anadal
 * @author mgonzalez
 */
public class Test {

    @org.junit.Test
    public void test() {

        System.out.println();
    }


    public static void main(String[] args) {

        try {

            DistribucionEmailPlugin distribucionPlugin = new DistribucionEmailPlugin();
            List<String> emails= new ArrayList<>();
            emails.add("mgonzalez@fundaciobit.org");
            RegistroEntrada re = new RegistroEntrada();
            distribucionPlugin.distribuir( re,new Locale("ca"));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}
