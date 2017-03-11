package tdg.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface IObject
{
	void input();

	void update();

	void render(SpriteBatch batch, ShapeRenderer renderer);
}
