package org.fundaciobit.plugins.distribucion.distribucionripea.test;

import org.fundaciobit.plugins.distribucion.Destinatarios;
import org.fundaciobit.plugins.distribucion.distribucionripea.DistribucionRipeaPlugin;


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

            DistribucionRipeaPlugin distribucionPlugin = new DistribucionRipeaPlugin();

            Destinatarios destinatarios = distribucionPlugin.distribuir(null);
            if (destinatarios != null) {
                System.out.println(" ------- Destinatarios ------- ");
                System.out.println("DESTINATARIOS POSIBLES: " + destinatarios.getPosibles().size());
                System.out.println();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}
