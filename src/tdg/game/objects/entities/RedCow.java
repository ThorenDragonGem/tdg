package tdg.game.objects.entities;

import com.badlogic.gdx.graphics.Color;

import tdg.game.graphics.Material;

public class RedCow extends Passive
{

	public RedCow(float x, float y, float width, float height)
	{
		super(new Material(Color.RED), x, y, width, height, 1);
		solid = true;
	}

	@Override
	public void postUpdate()
	{
		super.postUpdate();
	}

}
