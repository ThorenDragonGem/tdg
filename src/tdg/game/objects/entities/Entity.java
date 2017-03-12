package tdg.game.objects.entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import tdg.game.Manager;
import tdg.game.graphics.Material;
import tdg.game.objects.EntityStats;
import tdg.game.objects.GameObject;
import tdg.game.objects.inventory.Inventory;
import tdg.game.objects.inventory.InventoryGUI;
import tdg.game.objects.items.Equipment;
import tdg.game.objects.items.Item;
import tdg.game.objects.items.ItemStack;
import tdg.game.utils.CoolDown;
import tdg.game.utils.Mathf;
import tdg.game.utils.Physics;

public class Entity extends GameObject
{
	public Inventory inventory;
	public InventoryGUI inventoryGUI;
	public EntityStats stats;
	public CoolDown cd;
	public float speedX, speedY;
	public boolean collided = false;
	public boolean showInventory;
	public boolean attacking, defending, attacked;
	public int attackCD;
	// public AIManager aiManager;

	public Entity(Material material, float x, float y, float width, float height, int drawOrder)
	{
		super(material, x, y, width, height, (int)(1000 + Mathf.maximize(drawOrder, 9000)));
		// drawOrder between 1000 and 10 000
		// this.aiManager = new AIManager();
		this.stats = new EntityStats(this);
		attackCD = (int)(60f / stats.get("as"));
		this.cd = new CoolDown(attackCD);
		this.inventory = new Inventory(0);
		this.showInventory = true;
	}

	@Override
	public void input()
	{
		super.input();
		inventory.input();
	}

	int haflSecond = 0;
	/**
	 * Use {@link #postUpdate()};
	 */
	@Deprecated
	@Override
	public void update()
	{
		super.update();
		System.out.println(stats.getLevel() + "  " + stats.calculateNextLevelXp());
		attackCD = (int)(60f / stats.get("as"));
		cd.setTime((int)((1 - stats.get("cdr")) * attackCD));
		if(haflSecond % 30 == 0)
		{
			halfSeconddUpdate();
			haflSecond = 0;
		}
		haflSecond++;

		collided = false;
		if(!cd.isActive())
			cd.go();
		inventory.update();
		List<GameObject> colliders = Physics.getCollisionList(this);
		for(GameObject o : colliders)
		{
			if(!(o instanceof Item))
				continue;
			if(inventory.hasFreeSlot((Item)o) && ((Item)o).pickable)
				pickUp((Item)o);
		}
		for(ItemStack stack : inventory.getList())
		{
			if(stack == null)
				continue;
			for(Item i : stack.getItems())
			{
				if(i == null)
					continue;
				i.inInventory = true;
			}
		}
		float newX = x + speedX;
		float newY = y + speedY;
		speedX = 0;
		speedY = 0;

		boolean moveX = true;
		boolean moveY = true;

		for(GameObject collider : Manager.OBJECTS.getObjects())
		{
			if(collider == this || !collider.solid)
				continue;
			if(Intersector.intersectRectangles(new Rectangle(newX, y, width, height), collider.getBounds(), new Rectangle()))
			{
				moveY = false;
				collided = true;
			}

			if(Intersector.intersectRectangles(new Rectangle(x, newY, width, height), collider.getBounds(), new Rectangle()))
			{
				moveX = false;
				collided = true;
			}
		}
		postUpdate();
		if(!moveX && !moveY)
			return;
		if(!moveX)
		{
			x = newX;
			return;
		}
		if(!moveY)
		{
			y = newY;
			return;
		}
		x = newX;
		y = newY;
	}

	public void halfSeconddUpdate()
	{
		stats.heal(stats.get("regen") / 2f);
		stats.mana(stats.get("manaRegen") / 2f);
	}

	/**
	 * Must use this method => update after collision detection but before position return;
	 */
	public void postUpdate()
	{
		inventory.update();
		stats.health = Mathf.minimize(stats.health, 0f);
		if(!stats.isAlive())
			death();
		cd.update();
	}

	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		super.render(batch, renderer);
		// get all ItemStack in inventory
		// if stack different of null aka contains Item
		// render stack into hud.
		for(ItemStack stack : inventory.getList())
		{
			if(stack == null)
				continue;
			inventoryGUI.getCell(inventory.getIndex(stack)).setItemStack(stack);
		}

		// TODO: if config.drawHealthBars
		// if(renderer.isDrawing())
		// {
		// drawHealthBar(renderer);
		// }
		// else
		// {
		// batch.end();
		// renderer.begin(ShapeType.Filled);
		// drawHealthBar(renderer);
		// renderer.end();
		// batch.begin();
		// }
	}

	public void renderHUD(SpriteBatch batch, ShapeRenderer renderer)
	{
		if(showInventory)
			inventoryGUI.render(batch, renderer);
	}

	protected void idle()
	{

	}

	protected void look()
	{
		List<GameObject> objects = Physics.sphereCollide(new Circle(x + width / 2, y + height / 2, stats.get("sgr")));
		for(GameObject o : objects)
		{
			if(!(o instanceof Player))
				continue;
			stats.target = (Entity)o;
		}
	}

	protected void chase()
	{

	}

	protected void attack(Entity entity)
	{
		cd.restart();
		stats.target.attacked = true;
		// TODO: different cases from passive and aggressive ?
		// the target of this = entity (mostly stats.target);
		stats.target = entity;
		// magicalDamages(1f, 11, null);
		physicalDamages(1f, null);
	}

	protected void defend()
	{

	}

	int length, p = 0;
	Vector2 nextPos;
	protected void move()
	{
		if(length % 100 == 0)
		{
			// recreate move pattern
			nextPos = new Vector2(x + width / 2 + Mathf.random(-5 * width, 5 * width), y + height / 2 + Mathf.random(-5 * height, 5 * height));
			length = 0;
		}
		float pathX = nextPos.x - x + width / 2;
		float pathY = nextPos.y - y + height / 2;
		float dist = nextPos.len();
		float dirX = pathX / dist;
		float dirY = pathY / dist;
		length++;
		if(collided)
		{
			nextPos = getCenterPosition2();
			dirX = 0;
			dirY = 0;
			length = 100;
		}
		move(dirX, dirY);
	}

	protected void death()
	{
		dropAll();
		// the attacker's target become null;
		// TODO: Bug => Resolved/WIP
		if(stats.target != null)
			stats.target.stats.target = null;
		Manager.OBJECTS.remove(this);
	}

	protected void pickUp(Item i)
	{
		if(inventory.getFreeItemStack(i) != null)
		{
			inventory.getFreeItemStack(i).addItem(i);
		}
		else
		{
			if(inventory.hasFreeSlot())
			{
				ItemStack stack = new ItemStack(i, i.maxStackSize);
				inventory.add(stack);
			}
		}
		i.holder = this;
		if(i instanceof Equipment)
		{
			((Equipment)i).onPickUp();
			((Equipment)i).addAttributesToHolder();
		}
		Manager.OBJECTS.remove(i);
	}

	public void dropAll()
	{
		for(ItemStack stack : inventory.getList())
			if(stack != null)
				drop(stack);
	}

	// TODO: set drop to not pickable during a certain time
	protected void drop(ItemStack stack)
	{
		// browse stack from last to first Item => the item type won't be null
		// before every other Item were dropped
		for(int j = stack.getItems().length - 1; j >= 0; j--)
		{
			if(stack.getItems()[j] == null)
				continue;
			drop(stack, stack.getItems()[j]);
		}
		inventory.remove(stack);
	}

	protected void drop(ItemStack stack, Item item)
	{
		if(item != null)
		{
			item.x = x + width / 2 + Mathf.random(-2 * width, 2 * width);
			item.y = y + height / 2 + Mathf.random(-2 * height, 2 * height);
			item.pickable = true;
			item.droppable = true;
			item.inInventory = false;
			if(item instanceof Equipment)
			{
				((Equipment)item).removeAllAttrbiutesFromHolder();
				((Equipment)item).removeAttributesToHolder();
			}
			item.holder = null;
			Manager.OBJECTS.add(item);
			stack.removeItem(item);
		}
	}

	protected Ray getRay(Vector3 start, Vector3 end)
	{
		Ray ray = new Ray(start, new Vector3(end.x - start.x, end.y - start.y, 0));
		return ray;
	}

	protected void move(float offsetX, float offsetY)
	{
		float velocity = stats.get("velocity");
		speedX += 500 * offsetX * velocity * Gdx.graphics.getDeltaTime();
		speedY += 500 * offsetY * velocity * Gdx.graphics.getDeltaTime();
	}

	public void physicalDamages(float factor, Circle aoe)
	{
		if(aoe == null)
		{
			stats.physicalDamages(factor);
		}
		else
		{
			
		}
	}

	public void magicalDamages(float factor, float manaCost, Circle aoe)
	{
		if(aoe == null)
		{
			stats.magicalDamages(factor, manaCost);
		}
		else
		{

		}
	}
}
