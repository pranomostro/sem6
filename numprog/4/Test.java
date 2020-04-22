import java.util.Arrays;

import ode.ExpliziterEuler;
import ode.Heun;
import ode.Konvergenzordnung;
import ode.ODE;
import ode.RungeKutta4;
import planeten.PlanetenGUI;
import freierfall.FastTransportGui;

public class Test {

    /**
     * Hier werden die GUIs fuer die Freie-Fall- und die
     * Planetensystemsimulation gestartet, und einzelne Testfaelle
     * durchgefuehrt.
     */
    public static void main(String[] args) {

        /**************************************/
        boolean startPlanetensystem = true;
        boolean startFreierFall = true;

        boolean testExpliziteVerfahren = true;
        boolean testKonvergenzordnung = true;
        /**************************************/

        if (startPlanetensystem) {
            new PlanetenGUI().setVisible(true);
        }

        if (startFreierFall) {
            new FastTransportGui().setVisible(true);
        }

        if (testExpliziteVerfahren)
            testExpliziteVerfahren();

        if(testKonvergenzordnung)
        {
            testKonvergenzordnung();
        }
    }

    private static void testKonvergenzordnung() {
        System.out.println("Teste Konvergenzordnung");

        double lambda = -0.5;
        double T = 1;

        ODE ode = new ODE() {

            @Override
            public double[] auswerten(double t, double[] y) {
                return new double[] { lambda*y[0] };
            }
        };

        Konvergenzordnung k = new Konvergenzordnung(ode, new double[] {1}, new double[] { Math.exp(lambda*T)  }, T);
        double h = 0.01;
        System.out.println("Konvergenzordnung mit Heun: " + k.order(new Heun(), h));
        System.out.println("Erwartetes Ergebnis: 2.0 (gerundet auf eine Nachkommastelle)");


        System.out.println("*************************************\n");
    }

    public static void testExpliziteVerfahren() {
        System.out.println("Teste Explizite Verfahren");
        ODE ode = new ODE() {

            @Override
            public double[] auswerten(double t, double[] y) {
                double[] v = new double[1];
                v[0] = t * y[0];
                return v;
            }
        };

        double delta_t = 1;
        double t0 = 0;
        double[] y0 = { 1 };

        /* Expl Euler */
        System.out.println("\nTeste Expliziten Euler.");
        ExpliziterEuler euler = new ExpliziterEuler();
        double[] y = Arrays.copyOf(y0, y0.length);
        double t = t0;
        for (int k = 1; k <= 4; k++) {
            y = euler.nextStep(y, t, delta_t, ode);
            System.out.println("y" + k + " = " + y[0]);
            t = t + delta_t;
        }
        System.out.println("Richtig waere: 1, 2, 6, 24" );


        /* Heun */
        System.out.println("\nTeste Heun.");
        Heun heun = new Heun();
        y = Arrays.copyOf(y0, y0.length);
        t = t0;
        for (int k = 1; k <= 4; k++) {
            y = heun.nextStep(y, t, delta_t, ode);
            System.out.println("y" + k + " = " + y[0]);
            t = t + delta_t;
        }
        System.out.println("Richtig waere: 1.5, 5.25, 34.125, 358.3..." );


        /* Runge Kutta4 */
        System.out.println("\nTeste Runge-Kutta4.");
        RungeKutta4 rk4 = new RungeKutta4();
        y = Arrays.copyOf(y0, y0.length);
        t = t0;
        for (int k = 1; k <= 4; k++) {
            y = rk4.nextStep(y, t, delta_t, ode);
            System.out.println("y" + k + " = " + y[0]);
            t = t + delta_t;
        }
        System.out.println("Richtig waeren gerundet: 1.65, 7.2, 77.7, 1857" );


        System.out.println("*************************************\n");
    }

}

