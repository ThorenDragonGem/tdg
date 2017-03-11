package tdg.game.objects.items;

import com.badlogic.gdx.graphics.Color;

import tdg.game.graphics.Material;

public class RedItem extends DustItem
{

	public RedItem(float x, float y, float width, float height)
	{
		super(new Material(Color.RED), x, y, width, height, 4);
	}

	public RedItem()
	{
		super();
	}

}
