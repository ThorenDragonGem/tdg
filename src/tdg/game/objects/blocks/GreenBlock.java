package tdg.game.objects.blocks;

import com.badlogic.gdx.graphics.Color;

import tdg.game.graphics.Material;

public class GreenBlock extends Block
{

	public GreenBlock(float x, float y, float width, float height)
	{
		super(new Material(Color.GREEN), x, y, width, height, 1);
	}

}
