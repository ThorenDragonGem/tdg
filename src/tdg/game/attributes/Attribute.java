package tdg.game.attributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Attribute extends BaseAttribute
{
	protected List<RawBonus> rawBonuses;
	protected List<FinalBonus> finalBonuses;
	protected float finalValue;

	public Attribute(float startingValue)
	{
		super(startingValue);
		rawBonuses = new CopyOnWriteArrayList<RawBonus>();
		finalBonuses = new CopyOnWriteArrayList<FinalBonus>();
		finalValue = getBaseValue();
	}

	public void addRawBonus(RawBonus bonus)
	{
		rawBonuses.add(bonus);
	}

	public Attribute addFinalBonus(FinalBonus bonus)
	{
		finalBonuses.add(bonus);
		return this;
	}

	public void removeRawBonus(RawBonus bonus)
	{
		if(rawBonuses.contains(bonus))
			rawBonuses.remove(bonus);
	}

	public void removeFinalBonus(FinalBonus bonus)
	{
		if(finalBonuses.contains(bonus))
		{
			finalBonuses.remove(bonus);
		}
	}

	protected void applyRawBonuses()
	{
		float rawBonusValue = 0;
		float rawBonusMultiplier = 0;
		for(RawBonus bonus : rawBonuses)
		{
			rawBonusValue += bonus.getBaseValue();
			rawBonusMultiplier += bonus.getBaseMultiplier();
		}

		finalValue += rawBonusValue;
		finalValue *= (1 + rawBonusMultiplier);
	}

	protected void applyFinalBonuses()
	{
		float finalBonusValue = 0;
		float finalBonusMultiplier = 0;
		for(FinalBonus bonus : finalBonuses)
		{
			finalBonusValue += bonus.getBaseValue();
			finalBonusMultiplier += bonus.getBaseMultiplier();
		}

		finalValue += finalBonusValue;
		finalValue *= (1 + finalBonusMultiplier);
	}

	public float calculateValue()
	{
		// TODO: add getBaseMultiplier()
		finalValue = getBaseValue();
		applyRawBonuses();
		applyFinalBonuses();
		return finalValue;
	}

	public void updateFinalBonus()
	{
		for(FinalBonus bonus : finalBonuses)
			if(bonus.getTimer() != null)
				bonus.update();
	}

	public List<RawBonus> getRawBonuses()
	{
		return rawBonuses;
	}

	public List<FinalBonus> getFinalBonuses()
	{
		return finalBonuses;
	}

	public float getFinalValue()
	{
		return calculateValue();
	}
}
