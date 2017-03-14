package tdg.game.objects.entities;

import tdg.game.Manager;
import tdg.game.Tales;
import tdg.game.graphics.Material;
import tdg.game.stats.MonsterRank;

public class Slime extends Aggressive
{

	public Slime(float x, float y, float width, float height)
	{
		super(MonsterRank.BRONZE5, new Material(Tales.assets.getTexture("slime.png")), x, y, width, height, 1);
		solid = true;
		// stats.getStat("healthMax").setBaseValue(1000000f);
		// stats.heal(Float.MAX_VALUE);
		// stats.getStat("as").setBaseValue(1f);
		// stats.getStat("strength").setBaseValue(100f);
		// stats.getStat("defense").setBaseValue(10000);
	}

	@Override
	protected void death()
	{
		super.death();
		Slime slime;
		Manager.spawn(slime = new Slime(x, y, width, height));
	}
}
