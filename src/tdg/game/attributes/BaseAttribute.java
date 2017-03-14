package tdg.game.attributes;

public class BaseAttribute
{
	private double baseValue;
	private double baseMultiplier;

	public BaseAttribute(double value, double multiplier)
	{
		this.baseValue = value;
		this.baseMultiplier = multiplier;
	}

	public BaseAttribute(double value)
	{
		this(value, 0);
	}

	public double getBaseValue()
	{
		return baseValue;
	}

	public double getBaseMultiplier()
	{
		return baseMultiplier;
	}
}
