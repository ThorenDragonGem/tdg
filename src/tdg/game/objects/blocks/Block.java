package tdg.game.objects.blocks;

import tdg.game.graphics.Material;
import tdg.game.objects.GameObject;
import tdg.game.utils.Mathf;

public class Block extends GameObject
{

	public Block(Material material, float x, float y, float width, float height, int drawOrder)
	{
		super(material, x, y, width, height, (int)(10 + Mathf.maximize(drawOrder, 190)));
		// draw order between 10 and 200
		solid = true;
	}

}
