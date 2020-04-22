public class BestMagic {
	public static void main(String[] args)
	{
		int bestmagic=0, magic=0, c;
		float besterr=65536, err, x;
		Gleitpunktzahl g;
		int numOfSamplingPts = 101;
		float[] xData = new float[numOfSamplingPts];
		float[] yData = new float[numOfSamplingPts];

		x=0.10f;

		Gleitpunktzahl.setSizeMantisse(24);
		Gleitpunktzahl.setSizeExponent(8);

		for(magic=1069500000; magic<1069600000; magic+=1)
		{
			FastMath.setMagic(magic);
			err=0;

			for (int i = 0; i < numOfSamplingPts; i++) {
				xData[i] = x;
				Gleitpunktzahl y = new Gleitpunktzahl(x);
				err += (float) FastMath.absInvSqrtErr(y);

				x *= Math.pow(100.0d, 1.0d / numOfSamplingPts);
			}
			if(err<besterr)
			{
				besterr=err;
				bestmagic=magic;
				System.out.println(besterr + " " + bestmagic);
			}
		}
		System.out.println(besterr + " " + bestmagic);
	}
}
