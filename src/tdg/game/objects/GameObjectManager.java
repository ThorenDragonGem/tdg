package tdg.game.objects;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import tdg.game.ObjectManager;
import tdg.game.Tales;

public class GameObjectManager extends ObjectManager<GameObject> implements IObject
{

	@Override
	public void input()
	{
		for(GameObject o : objects)
			o.input();
	}

	@Override
	public void update()
	{
		for(GameObject o : objects)
			o.update();
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		objects = sort(objects);
		int i = 0;
		for(GameObject o : objects)
		{
			if((o.x >= Tales.camera.position.x - Tales.camera.viewportWidth / 2 * Tales.camera.zoom - o.width && o.x <= Tales.camera.position.x + Tales.camera.viewportWidth / 2 * Tales.camera.zoom))
			{
				if((o.y >= Tales.camera.position.y - Tales.camera.viewportHeight / 2 * Tales.camera.zoom - o.height && o.y <= Tales.camera.position.y + Tales.camera.viewportHeight / 2 * Tales.camera.zoom))
				{
					if(o.material.getTexture() == null)
					{
						renderer.begin(ShapeType.Filled);
						o.render(batch, renderer);
						renderer.end();
					}
					else
					{
						batch.begin();
						o.render(batch, renderer);
						batch.end();
					}
					i++;
				}
			}
		}
		// System.out.println(i);
	}

	public List<GameObject> sort(List<GameObject> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			final GameObject temp = list.get(i);
			int j = i - 1;
			while(j >= 0 && list.get(j).drawOrder > temp.drawOrder)
			{
				list.set(j + 1, list.get(j));
				j--;
			}
			list.set(j + 1, temp);
		}
		return list;
	}

}
