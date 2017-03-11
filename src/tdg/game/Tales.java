package tdg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

import tdg.game.graphics.Material;
import tdg.game.objects.GameObject;
import tdg.game.objects.blocks.Block;
import tdg.game.objects.blocks.GreenBlock;
import tdg.game.objects.entities.Player;
import tdg.game.objects.entities.RedCow;
import tdg.game.objects.entities.Slime;
import tdg.game.objects.items.BlueItem;
import tdg.game.objects.items.GreenItem;
import tdg.game.objects.items.RedItem;
import tdg.game.objects.items.Shield;
import tdg.game.objects.items.Sword;
import tdg.game.utils.Assets;
import tdg.game.utils.Mathf;

public class Tales extends ApplicationAdapter
{
	public static Assets assets;
	public static OrthographicCamera camera;
	public static BitmapFont font;
	private SpriteBatch masterBatch;
	private ShapeRenderer masterRenderer;
	private Player player;
	private Block block;
	private float camX, camY;
	private boolean paused;

	@Override
	public void create()
	{
		assets = new Assets();
		camera = new OrthographicCamera(Config.width, Config.height);
		camera.far = 300f;
		camera.near = 0f;
		camera.update();
		font = new BitmapFont();
		masterBatch = new SpriteBatch();
		masterRenderer = new ShapeRenderer();
		masterRenderer.setAutoShapeType(true);
		player = new Player(new Material(assets.getTexture("badlogic.jpg")), 0, 0, 64, 64);
		// TODO: GameObjectFactory.java
		spawn(new GreenBlock(100, 100, 64, 64));
		spawn(new GreenBlock(200, 100, 64, 64));
		spawn(new GreenBlock(300, 100, 64, 64));
		spawn(new GreenBlock(100, 200, 64, 64));
		spawn(new GreenBlock(100, 300, 64, 64));
		spawn(new GreenBlock(200, 200, 64, 64));
		spawn(new GreenBlock(200, 300, 64, 64));
		spawn(new GreenBlock(300, 200, 64, 64));
		spawn(new GreenBlock(300, 300, 64, 64));
		// spawn(new Slime(-200, -200, 64, 64));
		// spawn(new Slime(-200, -300, 64, 64));
		spawn(new Slime(-200, -400, 64, 64));
		spawn(new RedCow(-300, 300, 32, 32));
		for(int i = 0; i < 1; i++)
		{
			spawn(new Sword(Mathf.random(-1 * Config.width, 1 * Config.width), Mathf.random(-1 * Config.height, 1 * Config.height), 32, 32));
			spawn(new Shield(Mathf.random(-1 * Config.width, 1 * Config.width), Mathf.random(-1 * Config.height, 1 * Config.height), 32, 32));
			spawn(new RedItem(Mathf.random(-1 * Config.width, 1 * Config.width), Mathf.random(-1 * Config.height, 1 * Config.height), 32, 32));
			spawn(new BlueItem(Mathf.random(-1 * Config.width, 1 * Config.width), Mathf.random(-1 * Config.height, 1 * Config.height), 32, 32));
			spawn(new GreenItem(Mathf.random(-1 * Config.width, 1 * Config.width), Mathf.random(-1 * Config.height, 1 * Config.height), 32, 32));
		}
		spawn(new Sword(Mathf.random(-1 * Config.width, 1 * Config.width), Mathf.random(-1 * Config.height, 1 * Config.height), 32, 32));
		spawn(player);
	}

	public void input()
	{
		Gdx.graphics.setTitle("TDG, FPS: " + Integer.toString(Gdx.graphics.getFramesPerSecond()));
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();
		if(Gdx.input.isKeyJustPressed(Input.Keys.P))
			paused = !paused;
		if(Gdx.input.isKeyJustPressed(Input.Keys.M))
		{
			if(!Gdx.graphics.isFullscreen())
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			else
				Gdx.graphics.setWindowedMode(1280, 720);
		}
		Gdx.input.setInputProcessor(new InputProcessor()
		{

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button)
			{
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer)
			{
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button)
			{
				return false;
			}

			@Override
			public boolean scrolled(int amount)
			{
				if(amount == 1)
					camera.zoom += 0.2f;
				else if(amount == -1)
					camera.zoom -= 0.2f;
				camera.zoom = Mathf.minimize(camera.zoom, 0.4f);
				camera.zoom = Mathf.maximize(camera.zoom, 5f);
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY)
			{
				return false;
			}

			@Override
			public boolean keyUp(int keycode)
			{
				return false;
			}

			@Override
			public boolean keyTyped(char character)
			{
				return false;
			}

			@Override
			public boolean keyDown(int keycode)
			{
				return false;
			}
		});
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0))
			spawn(new Slime(Mathf.random(-1 * Config.width, 1 * Config.width), Mathf.random(-1 * Config.height, 1 * Config.height), 32, 32));
		if(!paused)
			Manager.OBJECTS.input();
	}

	public void update()
	{
		int w = (int)(Config.width / 6 * camera.zoom);
		if(this.player.x > this.camX + w)
			this.camX = this.player.x - w;
		if(this.player.x < this.camX - w)
			this.camX = this.player.x + w;
		int h = (int)(Config.height / 6 * camera.zoom);
		if(this.player.y > this.camY + h)
			this.camY = this.player.y - h;
		if(this.player.y < this.camY - h)
			this.camY = this.player.y + h;
		camera.viewportWidth = Config.width;
		camera.viewportHeight = Config.height;
		camera.position.set((int)(camX + player.width / 2), (int)(camY + player.height / 2), 0);
		camera.update();
		masterBatch.setProjectionMatrix(camera.combined);
		masterRenderer.setProjectionMatrix(camera.combined);
		if(!paused)
			Manager.OBJECTS.update();
	}

	@Override
	public void render()
	{
		input();
		update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Manager.OBJECTS.render(masterBatch, masterRenderer);

		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, camera.viewportWidth, camera.viewportHeight);
		masterBatch.setProjectionMatrix(uiMatrix);
		masterRenderer.setProjectionMatrix(uiMatrix);

		player.renderHUD(masterBatch, masterRenderer);
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
		Config.width = width;
		Config.height = height;
	}

	public static void spawn(GameObject object)
	{
		Manager.OBJECTS.add(object);
	}
}
