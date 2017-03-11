package tdg.game.attributes;

public class BaseAttribute
{
	private float baseValue;
	private float baseMultiplier;

	public BaseAttribute(float value, float multiplier)
	{
		this.baseValue = value;
		this.baseMultiplier = multiplier;
	}

	public BaseAttribute(float value)
	{
		this(value, 0);
	}

	public float getBaseValue()
	{
		return baseValue;
	}

	public float getBaseMultiplier()
	{
		return baseMultiplier;
	}
}
