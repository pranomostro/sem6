/**
 * @author Christoph Riesinger (riesinge@in.tum.de)
 * @author Jürgen Bräckle (braeckle@in.tum.de)
 * @author Sebastian Rettenberger (rettenbs@in.tum.de)
 * @since Oktober 22, 2014
 * @version 1.2
 *
 *          This class contains methods for rapidly calculating basic
 *          mathematical operations.
 */
public class FastMath {
	/**
	 * The "magic" constant which is used in the fast inverse square roo
	 * algorithm.
	 *
	 * The given initial value is just a test value for 8 mantissa bits and 4
	 * exponent bits, and has to be optimized by the students.
	 *
	 * In literature, several of those constants for floats or doubles can be
	 * found. There's no optimal constant for all cases.
	 */
	private static int MAGIC_NUMBER = 0x5f3759df;
	/**
	 * belegt die MAGIC_NUMBER mit dem Wert magic
	 */
	public static void setMagic(int magic) {
		FastMath.MAGIC_NUMBER = magic;
	}

	/**
	 * This method contains the code for the fast inverse square root algorithm
	 * which can e.g. be found in "Fast Inverse Square Root" from Lomont, Chris
	 * (February, 2003).
	 *
	 * It approximately calculates the value 1 / sqrt(x).
	 *
	 * No Newton steps to improve the result has to be implemented in this
	 * exercise.
	 *
	 * @param x
	 *            Input value of which the inverse square root should be
	 *            computed.
	 * @return Approximation for 1 / sqrt(x).
	 */
	public static Gleitpunktzahl invSqrt(Gleitpunktzahl x) {
		int f = gleitpunktzahlToIEEE(x);
		f >>= 1;
		f = MAGIC_NUMBER - f;
		return iEEEToGleitpunktzahl(f);

		/*

		>:(

		int i = x.exponent;

		i <<= Gleitpunktzahl.getSizeMantisse();
		i += x.mantisse;

		i /= 2;
		i = MAGIC_NUMBER - i;

		Gleitpunktzahl result = new Gleitpunktzahl();
		result.mantisse = (i & (int)(Math.pow(2, Gleitpunktzahl.getSizeMantisse()) - 1));
		i >>>= Gleitpunktzahl.getSizeMantisse();
		result.exponent = i;

		return result;
		*/
	}

	/**
	 * Calculates the absolute error between the result of the fast inverse
	 * square root algorithm and the "exact" IEEE-conform result.
	 *
	 * @param x
	 *            Position where the absolute error should be determined.
	 * @return Absolute error between invSqrt(x) and 1 / Math.sqrt(x).
	 */
	public static double absInvSqrtErr(Gleitpunktzahl x) {
		double exact = 1 / Math.sqrt(x.toDouble());
		double approx = invSqrt(x).toDouble();
		double absErr = Math.abs(exact - approx);

		return absErr;
	}

	/**
	 * Calculates the relative error between the result of the fast inverse
	 * square root algorithm and the "exact" IEEE-conform result.
	 *
	 * @param x
	 *            Position where the relative error should be determined.
	 * @return Relative error between invSqrt(x) and 1 / Math.sqrt(x).
	 */
	public static double relInvSqrtErr(Gleitpunktzahl x) {
		double absErr = absInvSqrtErr(x);
		double relErr = Math.abs(absErr * Math.sqrt(x.toDouble()));

		return relErr;
	}

	/**
	 * Uebersetzt die Gleitpunktzahl in eine Bitfolge (int) aehnlich dem IEEE
	 * Standard, d.h. in die Form [Vorzeichen, Exponent, Mantisse], wobei die
	 * führende 1 der Mantisse nicht gespeichert wird. Dieser Wechsel ist noetig
	 * 	 * für ein Funktionieren des Fast Inverse Sqrt Algorithmus
	 */
	public static int gleitpunktzahlToIEEE(Gleitpunktzahl x) {
		int sizeExponent = Gleitpunktzahl.getSizeExponent();
		int sizeMantisse = Gleitpunktzahl.getSizeMantisse();

		int result;

		/* mantisse ohne fuehrende 1 einfuegen */
		int mask = (int) Math.pow(2, sizeMantisse-1) - 1;
		result = (x.mantisse & mask);

		/* exponent vorne anhaengen */
		result |= (x.exponent << sizeMantisse-1);

		/* vorzeichen setzen */
		if (x.vorzeichen)
			result |= (1 << sizeExponent + sizeMantisse-1);

		return result;
	}

	/**
	 * Liefert aus einer Bitfolge (int) in IEEE Darstellung, d.h. [Vorzeichen,
	 * Exponent, Mantisse] mit Mantisse ohne führende Null, die entsprechende
	 * Gleitpunktdarstellung
	 */
	public static Gleitpunktzahl iEEEToGleitpunktzahl(int b) {
		Gleitpunktzahl g = new Gleitpunktzahl();
		int sizeExponent = Gleitpunktzahl.getSizeExponent();
		int sizeMantisse = Gleitpunktzahl.getSizeMantisse();

		/* fuehrende 1 fuer mantisse eintragen */
		g.mantisse = 1;
		g.mantisse <<= sizeMantisse-1;
		/* mantisse ohne fuehrende 1 einfuegen */
		int mask = (int) Math.pow(2, sizeMantisse-1) - 1;
		g.mantisse |= (b & mask);

		/* exponent eintrage */
		b >>= sizeMantisse-1;
		mask = (int) Math.pow(2, sizeExponent) - 1;
		g.exponent = (b & mask);

		/* vorzeichen setzen */
		b >>= sizeExponent;
		g.vorzeichen = (b & 1) > 0;

		return g;
	}
}
