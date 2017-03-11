package tdg.game.objects.items;

import tdg.game.Tales;
import tdg.game.graphics.Material;

public class Shield extends Equipment
{

	public Shield(float x, float y, float width, float height)
	{
		super(new Material(Tales.assets.getTexture("shield.png")), x, y, width, height, 2);
	}
}
