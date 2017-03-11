package tdg.game.attributes;

import tdg.game.utils.CoolDown;

public class FinalBonus extends BaseAttribute
{
	private CoolDown cd;
	private Attribute parent;

	public FinalBonus(float value, float multiplier)
	{
		super(value, multiplier);
		cd = null;
	}

	public FinalBonus(float value, float multiplier, int time)
	{
		super(value, multiplier);
		cd = new CoolDown(time);
	}

	public FinalBonus start(Attribute parent)
	{
		this.parent = parent;
		cd.go();
		return this;
	}

	public void update()
	{
		if(cd.isOver())
		{
			cd.terminate();
			parent.removeFinalBonus(this);
		}
		else
		{
			cd.update();
		}
	}

	public CoolDown getTimer()
	{
		return cd;
	}
}
