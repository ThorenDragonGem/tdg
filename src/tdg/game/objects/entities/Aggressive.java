package tdg.game.objects.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.Ray;

import tdg.game.graphics.Material;
import tdg.game.utils.Mathf;

public class Aggressive extends Entity
{
	public Aggressive(Material material, float x, float y, float width, float height, int drawOrder)
	{
		super(material, x, y, width, height, (int)Mathf.maximize(drawOrder, 500));
		// draw order between 100 and 1500;
		// aiManager.addAI("look", new LookAI(this, Player.class));
	}

	@Override
	public void postUpdate()
	{
		super.postUpdate();
		if(stats.target == null)
		{
			// TODO ?
			// aiManager.get("look").updateAI();
			look();
			if(cd.isOver())
				cd.stop();
		}
		else
		{
			if(!cd.isActive())
				cd.go();
			if(Intersector.overlaps(new Circle(x + width / 2, y + width / 2, stats.get("atr")), stats.target.getBounds()))
			{
				if(cd.isOver())
					attack(stats.target);
			}
			else if(Intersector.overlaps(new Circle(x + width / 2, y + width / 2, stats.get("sgr")), stats.target.getBounds()))
			{
				Ray ray = getRay(getCenterPosition3(), stats.target.getCenterPosition3());
				// chase();
				move(ray.direction.x, ray.direction.y);
			}
			else
			{
				stats.target = null;
			}
		}
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		super.render(batch, renderer);
	}

	@Override
	protected void defend()
	{
		super.defend();
		// TODO: if stats.target is attacking and cd is over, defend
	}
}
