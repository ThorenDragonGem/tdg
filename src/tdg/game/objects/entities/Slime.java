package tdg.game.objects.entities;

import tdg.game.Tales;
import tdg.game.graphics.Material;

public class Slime extends Aggressive
{

	public Slime(float x, float y, float width, float height)
	{
		super(new Material(Tales.assets.getTexture("slime.png")), x, y, width, height, 1);
		cd.setTime(10);
		solid = true;
		// stats.getStat("healthMax").setBaseValue(1000000f);
		stats.heal(Float.MAX_VALUE);
	}

	@Override
	protected void death()
	{
		super.death();
		Tales.spawn(new Slime(x, y, width, height));
	}
}
