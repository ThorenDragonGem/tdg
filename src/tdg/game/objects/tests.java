package tdg.game.objects;

import java.util.Random;

import tdg.game.utils.Mathf;

public class tests
{
	public static void main(String[] args)
	{
		System.out.println(Math.exp(500));
		return;
		// long first = System.currentTimeMillis();
		// float[] fs = new float[1000000];
		// float luck = 100;
		// float max = 1;
		// float min = 0;
		// float median = (luck / 200) + (max - min) / 2;
		// System.out.println(median);
		// for(int i = 0; i < fs.length; i++)
		// {
		// float f = (float)getBiasedRandom(median, min, max);
		// fs[i] = f;
		// }
		// float mean = 0;
		// for(Float f : fs)
		// {
		// mean += f;
		// }
		// mean /= fs.length;
		// Arrays.sort(fs);
		// long f = System.currentTimeMillis();
		// long time = f - first;
		// System.out.println(time);

		// float a = 50;
		// float t = a;
		// for(int i = 1; i < 501; i++)
		// {
		// System.out.println(a);
		// System.out.println(50 * Mathf.pow(1.05f, i));
		// a = a * 1.05f;
		// t += a;
		// }
		// System.err.println(t);
	}

	static Random r = new Random();

	public static double getBiasedRandom(double bias, double min, double max)
	{
		double bias_depth_perc = 0.1;
		double bias_depth_abs = (max - min) * bias_depth_perc;
		double min_bias = bias - bias_depth_abs;
		double max_bias = bias + bias_depth_abs;

		if(max_bias > max)
			max_bias = max;
		if(min_bias < min)
			min_bias = min;

		double variance = (max_bias - min_bias) / 2;

		double rndBiased = bias + r.nextGaussian() * variance;

		if(rndBiased > max)
			rndBiased = max - (rndBiased - max);

		if(rndBiased < min)
			rndBiased = min + (min - rndBiased);

		return rndBiased;
	}

	public static float randomBias(float min, float max, float bias)
	{
		float r = Mathf.random();
		r = Mathf.pow(r, bias);
		return min + (max - min) * r;
	}
}
