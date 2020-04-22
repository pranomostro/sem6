package dft;

import java.util.Arrays;

/**
 * Schnelle inverse Fourier-Transformation
 *
 * @author Sebastian Rettenberger
 */
public class IFFT {
    /**
     * Schnelle inverse Fourier-Transformation (IFFT).
     *
     * Die Funktion nimmt an, dass die Laenge des Arrays c immer eine
     * Zweierpotenz ist. Es gilt also: c.length == 2^m fuer ein beliebiges m.
     */
    public static Complex[] ifft(Complex[] c) {
        if(c.length == 1) return new Complex[] {c[0]};
        int m = c.length / 2;
        Complex[] firstHalf = new Complex[m];
        Complex[] secondHalf = new Complex[m];
        for(int i = 0; i < m; i++) {
            firstHalf[i] = c[2 * i];
            secondHalf[i] = c[(2 * i) + 1];
        }
        Complex[] z1 = ifft(firstHalf);
        Complex[] z2 = ifft(secondHalf);

        Complex omega = Complex.fromPolar(1, (2 * Math.PI) / c.length);

        Complex[] v = new Complex[c.length];
        for(int j = 0; j < m; j++) {
            v[j] = z1[j].add(omega.power(j).mul(z2[j]));
            v[j + m] = z1[j].sub(omega.power(j).mul(z2[j]));
        }
        return v;
    }
}
