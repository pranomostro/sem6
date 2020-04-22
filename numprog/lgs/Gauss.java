import java.util.Arrays;

public class Gauss {

    /**
     * Diese Methode soll die Loesung x des LGS R*x=b durch
     * Rueckwaertssubstitution ermitteln.
     * PARAMETER:
     * R: Eine obere Dreiecksmatrix der Groesse n x n
     * b: Ein Vektor der Laenge n
     */
    public static double[] backSubst(double[][] R, double[] b) {
        double[] result = new double[b.length];
        for(int i = b.length - 1; i >= 0; i--) {
            double extraWert = 0;
            for(int j = b.length - 1; j > i; j--) {
                extraWert += R[i][j] * result[j];
            }
            result[i] = (b[i] - extraWert) / R[i][i];
        }
        return  result;
    }

    /**
     * Diese Methode soll die Loesung x des LGS A*x=b durch Gauss-Elimination mit
     * Spaltenpivotisierung ermitteln. A und b sollen dabei nicht veraendert werden.
     * PARAMETER: A:
     * Eine regulaere Matrix der Groesse n x n
     * b: Ein Vektor der Laenge n
     */
    public static double[] solve(double[][] A, double[] b) {
        double[][] Acopy = new double[b.length][b.length];
        for(int i = 0; i < b.length; i++) {
            Acopy[i] = A[i].clone();
        }
        double[] bcopy = b.clone();
        for(int i = 0; i < b.length; i++) {
            int maxRow = i;
            for(int j = i; j < b.length; j++) {
                if(Math.abs(A[j][i]) > Math.abs(A[maxRow][i])) {
                    maxRow = j;
                }
            }
            double[] tempRow = Acopy[i];
            Acopy[i] = Acopy[maxRow].clone();
            Acopy[maxRow] = tempRow.clone();
            double btemp = bcopy[i];
            bcopy[i] = bcopy[maxRow];
            bcopy[maxRow] = btemp;
            for(int j = i + 1; j < b.length; j++) {
                double factor = (Acopy[j][i] / Acopy[i][i]) * (-1);
                for(int k = i; k < b.length; k++) {
                    Acopy[j][k] += factor * Acopy[i][k];
                }
                bcopy[j] += factor * bcopy[i];
            }
        }
        return backSubst(Acopy, bcopy);
    }

    /**
     * Diese Methode soll eine Loesung p!=0 des LGS A*p=0 ermitteln. A ist dabei
     * eine nicht invertierbare Matrix. A soll dabei nicht veraendert werden.
     *
     * Gehen Sie dazu folgendermassen vor (vgl.Aufgabenblatt):
     * -Fuehren Sie zunaechst den Gauss-Algorithmus mit Spaltenpivotisierung
     *  solange durch, bis in einem Schritt alle moeglichen Pivotelemente
     *  numerisch gleich 0 sind (d.h. <1E-10)
     * -Betrachten Sie die bis jetzt entstandene obere Dreiecksmatrix T und
     *  loesen Sie Tx = -v durch Rueckwaertssubstitution
     * -Geben Sie den Vektor (x,1,0,...,0) zurueck
     *
     * Sollte A doch invertierbar sein, kann immer ein Pivot-Element gefunden werden(>=1E-10).
     * In diesem Fall soll der 0-Vektor zurueckgegeben werden.
     * PARAMETER:
     * A: Eine singulaere Matrix der Groesse n x n
     */
    public static double[] solveSing(double[][] A) {
        double[][] Acopy = new double[A.length][A.length];
        for(int i = 0; i < A.length; i++) {
            Acopy[i] = A[i].clone();
        }
        for(int i = 0; i < A.length; i++) {
            int maxRow = i;
            for(int j = i; j < A.length; j++) {
                if(Math.abs(Acopy[j][i]) > Math.abs(Acopy[maxRow][i])) {
                    maxRow = j;
                }
            }
            double[] tempRow = Acopy[i];
            Acopy[i] = Acopy[maxRow].clone();
            Acopy[maxRow] = tempRow.clone();
            if(Math.abs(Acopy[i][i]) < 1E-10) {
                double[][] T = new double[i][i];
                double[] v = new double[i];
                for(int j = 0; j < i; j++) {
                    System.arraycopy(Acopy[j], 0, T[j], 0, i);
                    v[j] = (-1) * Acopy[j][i];
                }
                double[] x = backSubst(T, v);
                double[] result = new double[A.length];
                for(int j = 0; j < A.length; j++) {
                    result[j] = j < x.length ? x[j] : (j == x.length ? 1 : 0);
                }
                return result;
            }
            for(int j = i + 1; j < A.length; j++) {
                double factor = (Acopy[j][i] / Acopy[i][i]) * (-1);
                for(int k = i; k < A.length; k++) {
                    Acopy[j][k] += factor * Acopy[i][k];
                }
            }
        }
        double[] result = new double[A.length];
        Arrays.fill(result, 0);
        return result;
    }

    /**
     * Diese Methode berechnet das Matrix-Vektor-Produkt A*x mit A einer nxm
     * Matrix und x einem Vektor der Laenge m. Sie eignet sich zum Testen der
     * Gauss-Loesung
     */
    public static double[] matrixVectorMult(double[][] A, double[] x) {
        int n = A.length;
        int m = x.length;

        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            y[i] = 0;
            for (int j = 0; j < m; j++) {
                y[i] += A[i][j] * x[j];
            }
        }

        return y;
    }
}
