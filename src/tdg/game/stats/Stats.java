package tdg.game.stats;

import java.util.List;

import tdg.game.attributes.Attribute;
import tdg.game.utils.Mathf;

public class Stats
{
	private float baseValue, currentValue, tempUp, min, max;

	public Stats(float baseValue, float min, float max)
	{
		this.baseValue = baseValue;
		this.currentValue = baseValue;
		this.min = min;
		this.max = max;
		this.tempUp = 0;
	}

	public Stats(float baseValue, float min)
	{
		this(baseValue, min, Float.MAX_VALUE);
	}

	public Stats(float baseValue)
	{
		this(baseValue, -Float.MAX_VALUE, Float.MAX_VALUE);
	}

	public float calculateAttribute(List<Attribute> attributes)
	{
		float value = baseValue;
		for(Attribute attribute : attributes)
			value += attribute.getFinalValue();
		value = Mathf.minAndMax(value, min, max);
		currentValue = value;
		return value;
	}

	public float getBaseValue()
	{
		return baseValue;
	}

	public void setBaseValue(float baseValue)
	{
		baseValue = Mathf.minimize(baseValue, min);
		baseValue = Mathf.maximize(baseValue, max);
		this.baseValue = baseValue;
	}

	public void addBaseValue(float value)
	{
		setBaseValue(baseValue + value);
	}

	public float getMin()
	{
		return min;
	}

	public void setMin(float min)
	{
		this.min = min;
	}

	public float getMax()
	{
		return max;
	}

	public void setMax(float max)
	{
		this.max = max;
	}

	public float getCurrentValue()
	{
		return currentValue;
	}

	public void setCurrentValue(float curretnValue)
	{
		this.currentValue = curretnValue;
	}

	public float getTempUp()
	{
		return tempUp;
	}

	public void setTempUp(float tempUp)
	{
		this.tempUp = tempUp;
	}

	public void addTempUp(float value)
	{
		value = Mathf.minimize(value, 0);
		setTempUp(tempUp + value);
	}
}
