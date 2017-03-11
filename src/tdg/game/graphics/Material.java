package tdg.game.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Material
{
	private Texture texture;
	private Color color;

	public Material(Texture texture, Color color)
	{
		this.texture = texture;
		this.color = color;
	}

	public Material(Texture texture)
	{
		this(texture, Color.WHITE);
	}

	public Material(Color color)
	{
		// TODO: replace null by default texture
		this(null, color);
	}

	public Material()
	{
		// TODO: replace null by default texture
		this(null, Color.WHITE);
	}

	public Texture getTexture()
	{
		return texture;
	}

	public Color getColor()
	{
		return color;
	}
}
