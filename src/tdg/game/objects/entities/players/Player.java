package tdg.game.objects.entities.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import tdg.game.Config;
import tdg.game.Manager;
import tdg.game.Tales;
import tdg.game.attributes.Attribute;
import tdg.game.graphics.Material;
import tdg.game.objects.GameObject;
import tdg.game.objects.entities.Entity;
import tdg.game.objects.inventory.Cell;
import tdg.game.objects.inventory.Inventory;
import tdg.game.objects.inventory.InventoryGUI;
import tdg.game.objects.items.Item;
import tdg.game.stats.MonsterRank;
import tdg.game.stats.Stats;
import tdg.game.utils.ColorFading;
import tdg.game.utils.Mathf;
import tdg.game.utils.Physics;

public class Player extends Entity
{
	protected boolean moveKeys, moveMouse;

	public Player(Material material, float x, float y, float width, float height)
	{
		super(MonsterRank.BRONZE5, material, x, y, width, height, Integer.MAX_VALUE);
		solid = true;
		inventory = new Inventory(7);
		// inventoryGUI = new InventoryGUI(inventory, new
		// Material(Tales.assets.getTexture("inventory10x2.png")), 100, 100, 64,
		// 10);
		inventoryGUI = new InventoryGUI(inventory, new Material(Tales.assets.getTexture("shield.png")), 0, 0, 64);
		inventoryGUI.setCells(new Cell[]
		{ new Cell(0, 0, null), new Cell(64, 0, null), new Cell(128, 0, null), new Cell(192, 0, null), new Cell(256, 0, null), new Cell(320, 0, null), new Cell(384, 0, null), });
		stats.getStat("healthMax").setBaseValue(1000000);
		stats.addAttribute("defense", new Attribute(100000));
		stats.addAttribute("strength", new Attribute(10000));
		stats.addAttribute("atr", new Attribute(100f));
		stats.getStat("regen").setBaseValue(0f);
		stats.getStat("critChance").setBaseValue(0f);
		stats.getStat("critDmg").setBaseValue(2f);
		stats.getStat("cdr").setBaseValue(0);
		stats.getStat("as").setBaseValue(10f);
		stats.getStat("vampChance").setBaseValue(50f);
		stats.getStat("spellVamp").setBaseValue(100f);
		//
		stats.getStat("power").setBaseValue(100);
		//
		stats.getStat("manaRegen").setBaseValue(2f);
		stats.setShield(100);
		stats.initAll();
	}

	@Override
	public void input()
	{
		super.input();
		// if(Gdx.input.isKeyJustPressed(Input.Keys.COMMA))
		// stats.setShield(250);
		if(Gdx.input.isKeyJustPressed(Input.Keys.COMMA))
			stats.addLevel(99);
		attacked = false;
		attacking = false;
		defending = false;
		if(Gdx.input.isKeyPressed(Input.Keys.H))
			stats.getStat("healthMax").addBaseValue(1);
		if(Gdx.input.isKeyJustPressed(Input.Keys.J))
			stats.applyTempUp();
		if(Gdx.input.isKeyJustPressed(Input.Keys.N))
			stats.heal(Float.MAX_VALUE);
		if(Gdx.input.isKeyJustPressed(Input.Keys.I))
			showInventory = !showInventory;
		if(Gdx.input.isKeyJustPressed(Input.Keys.Y))
			dropAll();
		if(!moveMouse)
		{
			if(Gdx.input.isKeyPressed(Input.Keys.Z))
			{
				moveKeys = true;
				move(0, 1);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.S))
			{
				moveKeys = true;
				move(0, -1);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.Q))
			{
				moveKeys = true;
				move(-1, 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.D))
			{
				moveKeys = true;
				move(1, 0);
			}
		}
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !moveKeys)
		{
			moveMouse = true;
			stats.target = null;
			float pathX = Tales.camera.getPickRay(Gdx.input.getX(), Gdx.input.getY()).origin.x - (x + width / 2);
			float pathY = Tales.camera.getPickRay(Gdx.input.getX(), Gdx.input.getY()).origin.y - (y + height / 2);
			float distance = Mathf.sqrt(pathX * pathX + pathY * pathY);
			distance = Mathf.minimize(distance, 10f);
			float dirX = pathX / distance;
			float dirY = pathY / distance;
			move(dirX, dirY);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.B))
			for(Stats s : stats.getAttributes().keySet())
				for(Attribute attribute : stats.getAttributes().get(s))
					stats.removeAttribute(s, attribute);
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
			defend();
		if(!defending && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
			if(cd.isOver() && stats.target != null && Intersector.overlaps(new Circle(x + width / 2, y + height / 2, (float)stats.get("atr")), stats.target.getBounds()))
				attack(stats.target);
	}

	@Override
	public void postUpdate()
	{
		super.postUpdate();
		for(GameObject collider : Manager.OBJECTS.getObjects())
		{
			if(collider == this)
				continue;
			// target set stuff => replaced by Ray picking.
			// target stay picked if not dead
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Intersector.intersectRayBoundsFast(Tales.camera.getPickRay(Gdx.input.getX(), Gdx.input.getY()), new Vector3(collider.x + collider.width / 2, collider.y + collider.height / 2, 0), new Vector3(collider.width, collider.height, 0)))
			{
				if(collider instanceof Entity)
				{
					stats.target = (Entity)collider;
				}
				else if(collider instanceof Item && ((Item)collider).pickable && inventory.hasFreeSlot((Item)collider))
				{
					if(Physics.dist(x + width / 2, y + height / 2, collider.x + collider.width / 2, collider.y + collider.height / 2) < Mathf.sqrt(width * width + height * height) / 2 * 5f)
						pickUp((Item)collider);
				}
			}
		}
		moveKeys = false;
		moveMouse = false;
	}

	private boolean getTargetInList()
	{
		for(GameObject object : Manager.OBJECTS.getObjects())
		{
			if(object == this)
				continue;
			if(stats.target == object)
				return true;
		}
		return false;
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		super.render(batch, renderer);
		batch.end();
		renderer.begin(ShapeType.Line);
		renderer.circle(x + width / 2, y + height / 2, (float)stats.get("atr"));
		if(getTargetInList())
		{
			renderer.set(ShapeType.Filled);
			renderer.setColor(Color.BLUE);
			renderer.circle(stats.target.x + stats.target.width / 2, stats.target.y + stats.target.height / 2, 10);
		}
		renderer.end();
		batch.begin();
	}

	@Override
	public void renderHUD(SpriteBatch batch, ShapeRenderer renderer)
	{
		super.renderHUD(batch, renderer);
		if(getTargetInList())
			drawTargetHUD(batch, renderer);
		drawPlayerHUD(batch, renderer);
	}

	@Override
	protected void attack(Entity entity)
	{
		super.attack(entity);
		attacking = true;
		stats.addTempUp("strength", 0, 0.1f);
		magicalDamages(1, 1, null);
	}

	@Override
	protected void defend()
	{
		super.defend();
		defending = true;
		if(attacked)
			stats.addTempUp("defense", 0, 0.01f);
	}

	public void drawTargetHUD(SpriteBatch batch, ShapeRenderer renderer)
	{
		Rectangle targetHUD = new Rectangle(0, Config.height / 2, 300, Config.height / 2);
		Rectangle entityHUD = new Rectangle(targetHUD.x + 10, targetHUD.y + targetHUD.height - 10 - 100, 100, 100);
		Rectangle healthHUD = new Rectangle(entityHUD.x + entityHUD.width + 10, entityHUD.y + entityHUD.height - 20, targetHUD.width - entityHUD.width - 30, 20);
		if(renderer.isDrawing())
			renderer.end();
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		renderer.rect(targetHUD.x, targetHUD.y, targetHUD.width, targetHUD.height);
		renderer.setColor(Color.BLACK);
		renderer.rect(entityHUD.x, entityHUD.y, entityHUD.width, entityHUD.height);
		renderer.setColor(ColorFading.toGDXColor(ColorFading.blendColors(healthFadeColors, stats.target.stats.health / stats.target.stats.get("healthMax"))));
		renderer.rect(healthHUD.x, healthHUD.y, (float)(stats.target.stats.health / stats.target.stats.get("healthMax") * healthHUD.width), healthHUD.height);
		renderer.end();
		// TODO: improve
		if(stats.target.material.getTexture() != null)
		{
			batch.begin();
			batch.draw(stats.target.material.getTexture(), entityHUD.x + 10, entityHUD.y + 10, entityHUD.width - 20, entityHUD.height - 20);
			batch.end();
		}
		else
		{
			renderer.begin(ShapeType.Filled);
			renderer.setColor(stats.target.material.getColor());
			renderer.rect(entityHUD.x + 10, entityHUD.y + 10, entityHUD.width - 20, entityHUD.height - 20);
			renderer.end();
		}
		batch.begin();
		String s = stats.getThousandsString(stats.target.stats.health) + "/" + stats.getThousandsString(stats.target.stats.get("healthMax"));
		GlyphLayout layout = new GlyphLayout(Tales.font, s);
		Tales.font.draw(batch, s, healthHUD.x + healthHUD.width / 2 - layout.width / 2, healthHUD.y, layout.width, 0, false);
		batch.end();
	}

	private ColorFading.FadeColor[] healthFadeColors = new ColorFading.FadeColor[]
	{ new ColorFading.FadeColor(0.0f, new java.awt.Color(200, 25, 28)), new ColorFading.FadeColor(1.0f, new java.awt.Color(28, 200, 25)), new ColorFading.FadeColor(0.5f, new java.awt.Color(255, 165, 0)) };
	private ColorFading.FadeColor[] manaFadeColors = new ColorFading.FadeColor[]
	{ new ColorFading.FadeColor(0.0f, new java.awt.Color(25, 28, 200)), new ColorFading.FadeColor(1.0f, new java.awt.Color(30, 144, 255)) };

	protected void drawHealthBar(ShapeRenderer renderer)
	{
		renderer.rect(x, y + height + 10, (float)(stats.health / stats.get("healthMax") * width), 10);
	}

	protected void drawPlayerHUD(SpriteBatch batch, ShapeRenderer renderer)
	{
		Rectangle hud = new Rectangle(Config.width / 2 - Config.hudWidth / 2, 0, Config.hudWidth, Config.hudHeight);
		Tales.beginAlpha();
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.DARK_GRAY);
		renderer.rect(hud.x, hud.y, hud.width, hud.height);
		stats.health = Mathf.minimize(stats.health, 0f);
		renderer.setColor(ColorFading.toGDXColor(ColorFading.blendColors(healthFadeColors, stats.health / stats.get("healthMax"))));
		renderer.rect(Config.width / 2 - Config.hudWidth / 2 + 10, 40, (Config.hudWidth - 20) * (float)(stats.health / stats.get("healthMax")), 20);
		renderer.setColor(new Color(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g, Color.LIGHT_GRAY.b, 0.9f));
		renderer.rect(Config.width / 2 - Config.hudWidth / 2 + 10, 40, (Config.hudWidth - 20) * (float)(stats.shield / stats.get("shieldMax")), 20);
		renderer.setColor(ColorFading.toGDXColor(ColorFading.blendColors(manaFadeColors, stats.mana / stats.get("manaMax"))));
		renderer.rect(Config.width / 2 - Config.hudWidth / 2 + 10, 10, (float)((Config.hudWidth - 20) * stats.mana / stats.get("manaMax")), 20);
		renderer.end();
		batch.begin();
		String s = stats.getThousandsString(stats.health) + "/" + stats.getThousandsString(stats.get("healthMax"));
		GlyphLayout layout = new GlyphLayout(Tales.font, s);
		Tales.font.draw(batch, layout, hud.x + hud.width / 2 - layout.width / 2, hud.y + layout.height + 40 + 10 / 2);
		layout.setText(Tales.font, "+" + stats.getThousandsString(stats.get("regen")));
		Tales.font.draw(batch, layout, hud.x + hud.width - layout.width - 10, hud.y + layout.height + 40 + 10 / 2);
		layout.setText(Tales.font, stats.getThousandsString(stats.mana) + "/" + stats.getThousandsString(stats.get("manaMax")));
		Tales.font.draw(batch, layout, hud.x + hud.width / 2 - layout.width / 2, hud.y + layout.height + 10 + 10 / 2);
		layout.setText(Tales.font, "+" + stats.getThousandsString(stats.get("manaRegen")));
		Tales.font.draw(batch, layout, hud.x + hud.width - layout.width - 10, hud.y + layout.height + 10 + 10 / 2);
		batch.end();
		Tales.endAlpha();
	}

}
