package tdg.game.objects.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tdg.game.graphics.Material;
import tdg.game.utils.Mathf;

public class Passive extends Entity
{

	public Passive(Material material, float x, float y, float width, float height, int drawOrder)
	{
		super(material, x, y, width, height, (int)(500 + Mathf.maximize(drawOrder, 500)));
		// draw order between 1500 and 2000;
	}

	@Override
	public void postUpdate()
	{
		super.postUpdate();
		stats.target = null;
		move();
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		super.render(batch, renderer);
	}
}
