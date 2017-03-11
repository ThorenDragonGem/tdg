package tdg.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import tdg.game.graphics.Material;

public class GameObject implements IObject
{
	public Material material;
	public float x, y, width, height;
	public boolean solid;
	public int drawOrder = 0;

	public GameObject(Material material, float x, float y, float width, float height, int drawOrder)
	{
		this.material = material;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.drawOrder = drawOrder;
	}

	@Override
	public void input()
	{

	}

	@Override
	public void update()
	{

	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		if(material.getTexture() != null)
		{
			batch.draw(material.getTexture(), x, y, width, height, 0, 0, material.getTexture().getWidth(), material.getTexture().getHeight(), false, false);
		}
		else
		{
			renderer.setColor(material.getColor());
			// TODO render with 4 colors
			renderer.rect(x, y, width, height);
		}
	}

	public boolean isSolid()
	{
		return solid;
	}

	public void setSolid(boolean solid)
	{
		this.solid = solid;
	}

	public Rectangle getBounds()
	{
		return new Rectangle(x, y, width, height);
	}

	public Vector3 getPosition()
	{
		return new Vector3(x, y, 0);
	}

	public Vector3 getCenterPosition3()
	{
		return new Vector3(x + width / 2, y + height / 2, 0);
	}

	public Vector2 getCenterPosition2()
	{
		return new Vector2(x + width / 2, y + height / 2);
	}
}