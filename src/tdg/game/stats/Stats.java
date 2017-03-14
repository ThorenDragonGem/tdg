package tdg.game.stats;

import java.util.List;

import tdg.game.attributes.Attribute;
import tdg.game.utils.Mathf;

public class Stats
{
	protected MonsterRank rank;
	private double baseValue, currentValue, tempUp, min, max;

	public Stats(double baseValue, double min, double max)
	{
		this.baseValue = baseValue;
		this.currentValue = baseValue;
		this.min = min;
		this.max = max;
		this.tempUp = 0;
	}

	public Stats(double baseValue, double min)
	{
		this(baseValue, min, Double.MAX_VALUE);
	}

	public Stats(double baseValue)
	{
		this(baseValue, -Double.MAX_VALUE, Double.MAX_VALUE);
	}

	public double calculateAttribute(List<Attribute> attributes)
	{
		double value = baseValue;
		for(Attribute attribute : attributes)
			value += attribute.getFinalValue();
		value = Mathf.minAndMax(value, min, max);
		currentValue = value;
		return value;
	}

	public double getBaseValue()
	{
		return baseValue;
	}

	public void setBaseValue(double baseValue)
	{
		baseValue = Mathf.minimize(baseValue, min);
		baseValue = Mathf.maximize(baseValue, max);
		this.baseValue = baseValue;
	}

	public void addBaseValue(double value)
	{
		setBaseValue(baseValue + value);
	}

	public double getMin()
	{
		return min;
	}

	public void setMin(double min)
	{
		this.min = min;
	}

	public double getMax()
	{
		return max;
	}

	public void setMax(double max)
	{
		this.max = max;
	}

	public double getCurrentValue()
	{
		return currentValue;
	}

	public void setCurrentValue(double currentValue)
	{
		this.currentValue = currentValue;
	}

	public double getTempUp()
	{
		return tempUp;
	}

	public void setTempUp(double tempUp)
	{
		this.tempUp = tempUp;
	}

	public void addTempUp(double value)
	{
		value = Mathf.minimize((float)value, 0);
		setTempUp(tempUp + value);
	}

	public MonsterRank getRank()
	{
		return rank;
	}
}
