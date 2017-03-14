package tdg.game.objects.entities;

import com.badlogic.gdx.graphics.Color;

import tdg.game.graphics.Material;
import tdg.game.stats.MonsterRank;

public class RedCow extends Passive
{

	public RedCow(float x, float y, float width, float height)
	{
		super(MonsterRank.BRONZE5, new Material(Color.RED), x, y, width, height, 1);
		solid = true;
	}

	@Override
	public void postUpdate()
	{
		super.postUpdate();
	}

}
