package tdg.game.objects.items;

import com.badlogic.gdx.graphics.Color;

import tdg.game.graphics.Material;

public class BlueItem extends DustItem
{

	public BlueItem(float x, float y, float width, float height)
	{
		super(new Material(Color.BLUE), x, y, width, height, 3);
		maxStackSize = 16;
	}

	public BlueItem()
	{
		super();
	}

}
