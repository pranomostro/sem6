import java.util.Arrays;

/**
 * Die Klasse CubicSpline bietet eine Implementierung der kubischen Splines. Sie
 * dient uns zur effizienten Interpolation von aequidistanten Stuetzpunkten.
 *
 * @author braeckle
 *
 */
public class CubicSpline implements InterpolationMethod {

    /** linke und rechte Intervallgrenze x[0] bzw. x[n] */
    double a, b;

    /** Anzahl an Intervallen */
    int n;

    /** Intervallbreite */
    double h;

    /** Stuetzwerte an den aequidistanten Stuetzstellen */
    double[] y;

    /** zu berechnende Ableitunge an den Stuetzstellen */
    double[] yprime;

    /**
     * {@inheritDoc} Zusaetzlich werden die Ableitungen der stueckweisen
     * Polynome an den Stuetzstellen berechnet. Als Randbedingungen setzten wir
     * die Ableitungen an den Stellen x[0] und x[n] = 0.
     */
    @Override
    public void init(double a, double b, int n, double[] y) {
        this.a = a;
        this.b = b;
        this.n = n;
        h = ((double) b - a) / (n);

        this.y = Arrays.copyOf(y, n + 1);

        /* Randbedingungen setzten */
        yprime = new double[n + 1];
        yprime[0] = 0;
        yprime[n] = 0;

        /* Ableitungen berechnen. Nur noetig, wenn n > 1 */
        if (n > 1) {
            computeDerivatives();
        }
    }

    /**
     * getDerivatives gibt die Ableitungen yprime zurueck
     */
    public double[] getDerivatives() {
        return yprime;
    }

    /**
     * Setzt die Ableitungen an den Raendern x[0] und x[n] neu auf yprime0 bzw.
     * yprimen. Anschliessend werden alle Ableitungen aktualisiert.
     */
    public void setBoundaryConditions(double yprime0, double yprimen) {
        yprime[0] = yprime0;
        yprime[n] = yprimen;
        if (n > 1) {
            computeDerivatives();
        }
    }

    /**
     * Berechnet die Ableitungen der stueckweisen kubischen Polynome an den
     * einzelnen Stuetzstellen. Dazu wird ein lineares System Ax=c mit einer
     * Tridiagonalen Matrix A und der rechten Seite c aufgebaut und geloest.
     * Anschliessend sind die berechneten Ableitungen y1' bis yn-1' in der
     * Membervariable yprime gespeichert.
     *
     * Zum Zeitpunkt des Aufrufs stehen die Randbedingungen in yprime[0] und yprime[n].
     * Speziell bei den "kleinen" Faellen mit Intervallzahlen n = 2
     * oder 3 muss auf die Struktur des Gleichungssystems geachtet werden. Der
     * Fall n = 1 wird hier nicht beachtet, da dann keine weiteren Ableitungen
     * berechnet werden muessen.
     */
    public void computeDerivatives() {
        double[] sub = new double[n - 1];
        double[] diag = new double[n - 1];
        double[] sup = new double[n - 1];
        double[] v = new double[n - 1];

        for(int i = 0; i < n - 1; i++) {
            sub[i] = 1;
            sup[i] = 1;
            diag[i] = 4;
        }
        if(n == 2) {
            v[0] = (3 / h) * (y[2] - y[0] - ((h / 3) * yprime[0]) - ((h / 3) * yprime[n]));
        } else {
            for (int i = 1; i < n; i++) {
                if (i == 1) {
                    v[i - 1] = (3 / h) * (y[i + 1] - y[i - 1] - ((h / 3) * yprime[0]));
                } else if (i == n - 1) {
                    v[i - 1] = (3 / h) * (y[i + 1] - y[i - 1] - ((h / 3) * yprime[n]));
                } else {
                    v[i - 1] = (3 / h) * (y[i + 1] - y[i - 1]);
                }
            }
        }
        thomas(sub, sup, diag, v);
        System.arraycopy(v, 0, yprime, 1, v.length);
    }

    private void thomas(double[] sub, double[] sup, double[] diag, double[] v) {
        //Nach Aufgabenstellung Thomas-Algorithmus verwendet und im Internet nachgeschlagen
        //Quelle: https://en.wikibooks.org/wiki/Algorithm_Implementation/Linear_Algebra/Tridiagonal_matrix_algorithm
        sup[0] /= diag[0];
        v[0] /= diag[0];

        for(int i = 1; i < v.length; i++) {
            double m = 1 / (diag[i] - (sub[i] * sup[i - 1]));
            sup[i] *= m;
            v[i] = (v[i] - sub[i] * v[i - 1]) * m;
        }

        for(int i = v.length - 2; i >= 0; i--) v[i] -= sup[i] * v[i + 1];
    }

    /**
     * {@inheritDoc} Liegt z ausserhalb der Stuetzgrenzen, werden die
     * aeussersten Werte y[0] bzw. y[n] zurueckgegeben. Liegt z zwischen den
     * Stuetzstellen x_i und x_i+1, wird z in das Intervall [0,1] transformiert
     * und das entsprechende kubische Hermite-Polynom ausgewertet.
     */
    @Override
    public double evaluate(double z) {
        if(z <= a) return y[0];
        if(z >= b) return y[n];

        double leftValue = a + Math.floor((z - a) / h) * h;
        int leftIndex = (int) ((leftValue - a) / h);

        double t = (z - leftValue) / h;
        double t2 = Math.pow(t, 2);
        double t3 = Math.pow(t, 3);
        double h0 = 1 - (3 * t2) + (2 * t3);
        double h1 = (3 * t2) - (2 * t3);
        double h2 = t - (2 * t2) + t3;
        double h3 = (-1 * t2) + t3;

        return (y[leftIndex] * h0) + (y[leftIndex + 1] * h1) + (h * yprime[leftIndex] * h2) + (h * yprime[leftIndex + 1] * h3);
    }
}
