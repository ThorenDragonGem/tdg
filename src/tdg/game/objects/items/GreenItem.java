package tdg.game.objects.items;

import com.badlogic.gdx.graphics.Color;

import tdg.game.graphics.Material;

public class GreenItem extends DustItem
{

	public GreenItem(float x, float y, float width, float height)
	{
		super(new Material(Color.GREEN), x, y, width, height, 2);
		maxStackSize = 32;
	}

	public GreenItem()
	{
		super();
	}
}
