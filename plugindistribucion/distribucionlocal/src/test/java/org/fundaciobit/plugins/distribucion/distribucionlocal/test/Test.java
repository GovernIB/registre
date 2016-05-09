package org.fundaciobit.plugins.distribucion.distribucionlocal.test;

import org.fundaciobit.plugins.distribucion.Destinatarios;
import org.fundaciobit.plugins.distribucion.distribucionlocal.DistribucionLocalDistribucionPlugin;

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

            DistribucionLocalDistribucionPlugin distribucionPlugin = new DistribucionLocalDistribucionPlugin();

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
