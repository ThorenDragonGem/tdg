package tdg.game.attributes;

import java.util.HashMap;
import java.util.Map;

public class DependantAttribute extends Attribute
{
	protected Map<String, Attribute> otherAttributes;

	public DependantAttribute(float startingValue)
	{
		super(startingValue);
		otherAttributes = new HashMap<String, Attribute>();
	}

	public void addAttribute(String name, Attribute attribute)
	{
		otherAttributes.put(name, attribute);
	}

	public void removeAttribute(String name)
	{
		if(otherAttributes.containsKey(name))
			otherAttributes.remove(name);
	}

	public void removeAttribute(Attribute attribute)
	{
		for(String name : otherAttributes.keySet())
			if(otherAttributes.get(name) == attribute)
				otherAttributes.remove(name);
	}

	public void removeAll()
	{
		for(Attribute attribute : otherAttributes.values())
			removeAttribute(attribute);
	}

	@Override
	public void updateFinalBonus()
	{
		if(otherAttributes.values().size() == 0)
		{
			for(FinalBonus bonus : finalBonuses)
				bonus.update();
		}
		else
		{
			for(Attribute attribute : otherAttributes.values())
				attribute.updateFinalBonus();
		}
	}

	@Override
	public float calculateValue()
	{
		// specific attribute code goes here
		finalValue = getBaseValue();
		applyRawBonuses();
		applyFinalBonuses();
		for(Attribute attribute : otherAttributes.values())
		{
			finalValue += attribute.calculateValue();
		}
		return finalValue;
	}

	public Map<String, Attribute> getAttributes()
	{
		return otherAttributes;
	}
}
