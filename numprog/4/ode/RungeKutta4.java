package ode;

/**
 * Der klassische Runge-Kutta der Ordnung 4
 *
 * @author braeckle
 *
 */
public class RungeKutta4 implements Einschrittverfahren {

    @Override
    /**
     * {@inheritDoc}
     * Bei der Umsetzung koennen die Methoden addVectors und multScalar benutzt werden.
     */
    public double[] nextStep(double[] y_k, double t, double delta_t, ODE ode) {
        // TODO: diese Methode ist zu implementieren
        return null;
    }

    /**
     * addiert die zwei Vektoren a und b
     */
    private double[] addVectors(double[] a, double[] b) {
        double[] erg = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            erg[i] = a[i] + b[i];
        }
        return erg;
    }

    /**
     * multipliziert den Skalar scalar auf den Vektor a
     */
    private double[] multScalar(double[] a, double scalar) {
        double[] erg = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            erg[i] = scalar * a[i];
        }
        return erg;
    }

}
