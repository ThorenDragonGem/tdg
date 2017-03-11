package tdg.game.utils;

import java.util.Random;

public class Mathf
{
	public static final float PI = 3.14159265358979323846264338327950288419716939937510582f;
	public static final float PHI = (1 + sqrt(5)) / 2;

	public static boolean randomBoolean()
	{
		float random = random(0, 100);
		if(random > 50)
		{
			return true;
		}
		return false;
	}

	public static float min(float a, float b)
	{
		if(a < b)
			return a;
		return b;
	}

	public static float max(float a, float b)
	{
		if(a > b)
			return a;
		return b;
	}

	public static float clamp(float min, float max, float value)
	{
		if(value < min)
			value = min;
		if(value > max)
			value = max;

		return value;
	}

	public static float cos(float angle)
	{
		return (float)Math.cos(angle);
	}

	public static float sin(float angle)
	{
		return (float)Math.sin(angle);
	}

	public static float tan(float angle)
	{
		return (float)Math.tan(angle);
	}

	public static float acos(float angle)
	{
		return (float)Math.acos(angle);
	}

	public static float asin(float angle)
	{
		return (float)Math.asin(angle);
	}

	public static float atan(float angle)
	{
		return (float)Math.atan(angle);
	}

	public static float atan2(float x, float y)
	{
		return (float)Math.atan2(x, y);
	}

	public static float toRadians(float angle)
	{
		return (float)Math.toRadians(angle);
	}

	public static float toDegrees(float angle)
	{
		return (float)Math.toDegrees(angle);
	}

	public static float sqrt(float a)
	{
		return (float)Math.sqrt(a);
	}

	public static float floor(float a)
	{
		return (float)Math.floor(a);
	}

	public static float ceil(float a)
	{
		return (float)Math.ceil(a);
	}

	public static float round(float a)
	{
		return Math.round(a);
	}

	public static float random()
	{
		return (float)Math.random();
	}

	public static float random(float a, float b)
	{
		return a + random() * (b - a);
	}

	public static float abs(float a)
	{
		return Math.abs(a);
	}

	public static float pow(float f, float p)
	{
		return (float)Math.pow(f, p);
	}

	public static float lerp(float s, float e, float t)
	{
		return s + (e - s) * t;
	}

	public static float maximize(float value, float max)
	{
		if(value > max)
			value = max;
		return value;
	}

	public static float minimize(float value, float min)
	{
		if(value < min)
			value = min;
		return value;
	}

	public static float minAndMax(float value, float min, float max)
	{
		if(value < min)
			value = min;
		if(value > max)
			value = max;
		return value;
	}

	public static float exp(float exponent)
	{
		return (float)Math.exp(exponent);
	}

	public static float sigm(float numerator, float exponent)
	{
		return numerator / (1 + exp(-exponent));
	}

	public static float getBiasedRandom(float bias, float min, float max)
	{
		float bias_depth_perc = 0.1f;
		float bias_depth_abs = (max - min) * bias_depth_perc;
		float min_bias = bias - bias_depth_abs;
		float max_bias = bias + bias_depth_abs;
		Random r = new Random();
		if(max_bias > max)
			max_bias = max;
		if(min_bias < min)
			min_bias = min;
		float variance = (max_bias - min_bias) / 2;
		float rndBiased = bias + (float)r.nextGaussian() * variance;
		if(rndBiased > max)
			rndBiased = max - (rndBiased - max);
		if(rndBiased < min)
			rndBiased = min + (min - rndBiased);
		return rndBiased;
	}

}