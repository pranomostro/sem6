import java.util.Arrays;

/**
 * Die Klasse Newton-Polynom beschreibt die Newton-Interpolation. Die Klasse
 * bietet Methoden zur Erstellung und Auswertung eines Newton-Polynoms, welches
 * uebergebene Stuetzpunkte interpoliert.
 *
 * @author braeckle
 *
 */
public class NewtonPolynom implements InterpolationMethod {

    /** Stuetzstellen xi */
    double[] x;

    /**
     * Koeffizienten/Gewichte des Newton Polynoms p(x) = a0 + a1*(x-x0) +
     * a2*(x-x0)*(x-x1)+...
     */
    double[] a;

    /**
     * die Diagonalen des Dreiecksschemas. Diese dividierten Differenzen werden
     * fuer die Erweiterung der Stuetzstellen benoetigt.
     */
    double[] f;

    /**
     * leerer Konstruktore
     */
    public NewtonPolynom() {
    };

    /**
     * Konstruktor
     *
     * @param x
     *            Stuetzstellen
     * @param y
     *            Stuetzwerte
     */
    public NewtonPolynom(double[] x, double[] y) {
        this.init(x, y);
    }

    /**
     * {@inheritDoc} Zusaetzlich werden die Koeffizienten fuer das
     * Newton-Polynom berechnet.
     */
    @Override
    public void init(double a, double b, int n, double[] y) {
        x = new double[n + 1];
        double h = (b - a) / n;

        for (int i = 0; i < n + 1; i++) {
            x[i] = a + i * h;
        }
        computeCoefficients(y);
    }

    /**
     * Initialisierung der Newtoninterpolation mit beliebigen Stuetzstellen. Die
     * Faelle "x und y sind unterschiedlich lang" oder "eines der beiden Arrays
     * ist leer" werden nicht beachtet.
     *
     * @param x
     *            Stuetzstellen
     * @param y
     *            Stuetzwerte
     */
    public void init(double[] x, double[] y) {
        this.x = Arrays.copyOf(x, x.length);
        computeCoefficients(y);
    }

    /**
     * computeCoefficients belegt die Membervariablen a und f. Sie berechnet zu
     * uebergebenen Stuetzwerten y, mit Hilfe des Dreiecksschemas der
     * Newtoninterpolation, die Koeffizienten a_i des Newton-Polynoms. Die
     * Berechnung des Dreiecksschemas soll dabei lokal in nur einem Array der
     * Laenge n erfolgen (z.B. spaltenweise Berechnung). Am Ende steht die
     * Diagonale des Dreiecksschemas in der Membervariable f, also f[0],f[1],
     * ...,f[n] = [x0...x_n]f,[x1...x_n]f,...,[x_n]f. Diese koennen spaeter bei
     * der Erweiterung der Stuetzstellen verwendet werden.
     *
     * Es gilt immer: x und y sind gleich lang.
     */
    private void computeCoefficients(double[] y) {
        a = new double[y.length];
        f = new double[y.length];

        boolean[][] haveCacheValue = new boolean[y.length][y.length];
        double[][] cacheValue = new double[y.length][y.length];

        for(int j = 0; j < y.length; j++) {
            cacheValue[j][0] = y[j];
            haveCacheValue[j][0] = true;
        }

        recComputeCoefficient(0, y.length - 1, cacheValue, haveCacheValue);
        updateAF(cacheValue, haveCacheValue);
    }

    private void updateAF(double[][] cacheValue, boolean[][] haveCacheValue) {
        for(int j = 0; j < x.length; j++) {
            if(haveCacheValue[0][j]) a[j] = cacheValue[0][j];
            if(haveCacheValue[x.length - j - 1][j]) f[j] = cacheValue[x.length - j - 1][j];
        }
    }

    private void recComputeCoefficient(int i, int k, double[][] cacheValue, boolean[][] haveCacheValue) {
        if(haveCacheValue[i][k]) return;
        recComputeCoefficient(i + 1, k - 1, cacheValue, haveCacheValue);
        recComputeCoefficient(i, k - 1, cacheValue, haveCacheValue);

        cacheValue[i][k] = (cacheValue[i + 1][k - 1] - cacheValue[i][k - 1]) / (x[i + k] - x[i]);
        haveCacheValue[i][k] = true;
    }

    /**
     * Gibt die Koeffizienten des Newton-Polynoms a zurueck
     */
    public double[] getCoefficients() {
        return a;
    }

    /**
     * Gibt die Dividierten Differenzen der Diagonalen des Dreiecksschemas f
     * zurueck
     */
    public double[] getDividedDifferences() {
        //Since we apparently save the f array exactly the wrong way round
        //We could also reverse it every time we save / load it from / into our cache
        //but doing it this way makes more sense to us since the runtime of the calculcations
        //is more important. Making the whole cache system work with the numbers going the other way
        //is possible, but we are happy that it is working as it is right now.
        //Of course: Were this code to be used somewhere important, this issues would need to be resolved.
        double[] reversed = new double[f.length];
        for(int i = 0; i < f.length; i++) {
            reversed[f.length - i - 1] = f[i];
        }
        return reversed;
    }

    /**
     * addSamplintPoint fuegt einen weiteren Stuetzpunkt (x_new, y_new) zu x
     * hinzu. Daher werden die Membervariablen x, a und f vergoessert und
     * aktualisiert . Das gesamte Dreiecksschema muss dazu nicht neu aufgebaut
     * werden, da man den neuen Punkt unten anhaengen und das alte
     * Dreiecksschema erweitern kann. Fuer diese Erweiterungen ist nur die
     * Kenntnis der Stuetzstellen und der Diagonalen des Schemas, bzw. der
     * Koeffizienten noetig. Ist x_new schon als Stuetzstelle vorhanden, werden
     * die Stuetzstellen nicht erweitert.
     *
     * @param x_new
     *            neue Stuetzstelle
     * @param y_new
     *            neuer Stuetzwert
     */
    public void addSamplingPoint(double x_new, double y_new) {
        x = Arrays.copyOf(x, x.length + 1);
        a = Arrays.copyOf(a, a.length + 1);
        f = Arrays.copyOf(f, f.length + 1);
        x[x.length - 1] = x_new;

        boolean[][] haveCacheValue = new boolean[x.length][x.length];
        double[][] cacheValue = new double[x.length][x.length];
        for(int j = 0; j < f.length - 1; j++) {
            cacheValue[f.length - j - 2][j] = f[j];
            haveCacheValue[f.length - j - 2][j] = true;
        }
        cacheValue[f.length - 1][0] = y_new;
        haveCacheValue[f.length - 1][0] = true;
        recComputeCoefficient(0, f.length - 1, cacheValue, haveCacheValue);
        updateAF(cacheValue, haveCacheValue);
    }

    /**
     * {@inheritDoc} Das Newton-Polynom soll effizient mit einer Vorgehensweise
     * aehnlich dem Horner-Schema ausgewertet werden. Es wird davon ausgegangen,
     * dass die Stuetzstellen nicht leer sind.
     */
    @Override
    public double evaluate(double z) {
        double result = 0;
        for(int j = 0; j < f.length; j++) {
            double multResult = 1;
            for(int k = 0; k < j; k++) multResult *= (z - x[k]);
            result += a[j] * multResult;
        }
        return result;
    }
}
