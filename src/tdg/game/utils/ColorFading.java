package tdg.game.utils;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Thanks to :
 * @author MadProgrammer <link>http://stackoverflow.com/users/992484/madprogrammer</link>
 * @author Modified by ThorenDragonGem
 */
public class ColorFading
{
	public static com.badlogic.gdx.graphics.Color toGDXColor(Color color)
	{
		return new com.badlogic.gdx.graphics.Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
	}
	
	private static FCArrays order(FadeColor[] fadeColors)
	{
		//initializes arrays
		float[] fs_i = new float[fadeColors.length];
		Color[] cs_i = new Color[fadeColors.length];

		for(int w = 0; w < fadeColors.length; w++)
		{
			fs_i[w] = fadeColors[w].indice;
			cs_i[w] = fadeColors[w].color;
		}
		//sorted indices array
		float[] fs_s = fs_i.clone();
		Arrays.sort(fs_s);

		Color[] cs_a = new Color[cs_i.length];
		
		//sorts Color with his right index
		//browses cases of the indices array of index i
		for(int i = 0; i < fs_i.length; i++)
		{
			//browses cases of the sorted indices of index j
			for(int j = 0; j < fs_s.length; j++)
			{
				//checks if the value of the case of index i equals t the value of the case of index j
				if(fs_i[i] == fs_s[j])
				{
					//if true the color of index i in the non sorted array
					//is put at the case of index j in the color sorted array
					cs_a[j] = cs_i[i];
				}
			}
		}

		//checks if starts by 0.0f and finishes by 1.0f.
		float[] fs_f = null;
		Color[] cs_f = null;
		
		if(fs_s[0] != 0.0f)
		{
			if(fs_s[fadeColors.length - 1] != 1f)
			{
				fs_f = new float[fadeColors.length + 2];
				fs_f[0] = 0.0f;
				for(int a = 0; a < fs_s.length; a++)
					fs_f[a + 1] = fs_s[a];
				fs_f[fadeColors.length + 1] = 1f;
				
				cs_f = new Color[fadeColors.length + 2];
				cs_f[0] = cs_a[0];
				for(int d = 0; d < cs_a.length; d++)
					cs_f[d + 1] = cs_a[d];
				cs_f[fadeColors.length + 1] = cs_a[cs_a.length - 1];
			}
			else
			{
				fs_f = new float[fadeColors.length + 1];
				fs_f[0] = 0.0f;
				for(int b = 0; b < fs_s.length; b++)
					fs_f[b + 1] = fs_s[b];
				
				cs_f = new Color[fadeColors.length + 1];
				cs_f[0] = cs_a[0];
				for(int e = 0; e < cs_a.length; e++)
					cs_f[e + 1] = cs_a[e];
			}
		}
		else if(fs_s[fadeColors.length - 1] != 1f)
		{
			fs_f = new float[fadeColors.length + 1];
			for(int c = 0; c < fs_s.length; c++)
				fs_f[c] = fs_s[c];
			fs_f[fadeColors.length] = 1f;
			
			cs_f = new Color[fadeColors.length + 1];
			for(int f = 0; f < cs_a.length; f++)
				cs_f[f] = cs_a[f];
			cs_f[fadeColors.length] = cs_a[cs_a.length - 1];
		}
		else
		{
			fs_f = fs_s;
			cs_f = cs_a;
		}
			
		float[] fractions = new float[fs_f.length];
		Color[] colors = new Color[cs_f.length];
		fractions = fs_f.clone();
		colors = cs_f.clone();
		return new ColorFading.FCArrays(fractions, colors);
	}
	
	public static Color blendColors(FadeColor[] fadeColors, float progress)
	{
		return blendColors(order(fadeColors).floatArray, order(fadeColors).colorArray, progress);
	}
	
	private static Color blendColors(float[] fractions, Color[] colors, float progress)
	{
		Color color = null;
		if(fractions != null)
		{
			if(colors != null)
			{
				if(fractions.length == colors.length)
				{
					int[] indicies = getFractionIndicies(fractions, progress);
					float[] range = new float[]
					{ fractions[indicies[0]], fractions[indicies[1]] };
					Color[] colorRange = new Color[]
					{ colors[indicies[0]], colors[indicies[1]] };

					float max = range[1] - range[0];
					float value = progress - range[0];
					float weight = value / max;

					color = blend(colorRange[0], colorRange[1], 1f - weight);
				}
				else
				{
					throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
				}
			}
			else
			{
				throw new IllegalArgumentException("Colours can't be null");
			}
		}
		else
		{
			throw new IllegalArgumentException("Fractions can't be null");
		}
		return color;
	}

	public static int[] getFractionIndicies(float[] fractions, float progress)
	{
		int[] range = new int[2];

		int startPoint = 0;
		while(startPoint < fractions.length && fractions[startPoint] <= progress)
		{
			startPoint++;
		}

		if(startPoint >= fractions.length)
		{
			startPoint = fractions.length - 1;
		}

		range[0] = startPoint - 1;
		range[1] = startPoint;

		return range;
	}

	public static Color blend(Color color1, Color color2, double ratio)
	{
		float r = (float)ratio;
		float ir = (float)1.0 - r;

		float rgb1[] = new float[3];
		float rgb2[] = new float[3];

		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);

		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;

		if(red < 0)
		{
			red = 0;
		}
		else if(red > 255)
		{
			red = 255;
		}
		if(green < 0)
		{
			green = 0;
		}
		else if(green > 255)
		{
			green = 255;
		}
		if(blue < 0)
		{
			blue = 0;
		}
		else if(blue > 255)
		{
			blue = 255;
		}

		Color color = null;
		try
		{
			color = new Color(red, green, blue);
		}
		catch(IllegalArgumentException exp)
		{
			NumberFormat nf = NumberFormat.getNumberInstance();
			System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
			exp.printStackTrace();
		}
		return color;
	}
	
	public static FadeColor[] getFadeColorArray(float[] fractions, Color[] colors)
	{
		if(fractions.length != colors.length)
			throw new IllegalArgumentException();
		FadeColor[] fadeColors = new FadeColor[fractions.length];
		for(int i = 0; i < fractions.length; i++)
		{
			FadeColor fadeColor = new FadeColor(fractions[i], colors[i]);
			fadeColors[i] = fadeColor;
		}
		return fadeColors;
	}
	
	static class FCArrays
	{
		public float[] floatArray;
		public Color[] colorArray;
		
		public FCArrays(float[] floatArray, Color[] colorArray)
		{
			this.floatArray = floatArray;
			this.colorArray = colorArray;
		}
	}

	public static class FadeColor
	{
		public float indice;
		public Color color;

		public FadeColor(float indice, Color color)
		{
			this.indice = indice;
			this.color = color;
		}
	}
}
