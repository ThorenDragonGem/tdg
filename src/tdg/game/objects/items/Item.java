package tdg.game.objects.items;

import tdg.game.graphics.Material;
import tdg.game.objects.GameObject;
import tdg.game.objects.entities.Entity;
import tdg.game.utils.Mathf;

public class Item extends GameObject
{
	public boolean pickable;
	public boolean droppable;
	public boolean inInventory;
	public Entity holder;
	public int maxStackSize;

	public Item(Material material, float x, float y, float width, float height, int drawOrder)
	{
		super(material, x, y, width, height, (int)(200 + Mathf.maximize(drawOrder, 500)));
		// draw order between 200 and 500
		holder = null;
		pickable = true;
		droppable = true;
		inInventory = false;
		maxStackSize = 64;
	}

	public Item(Item i)
	{
		super(i.material, i.x, i.y, i.width, i.height, i.drawOrder);
	}

	public Item()
	{
		super(new Material(), 0, 0, 0, 0, 0);
	}

	public void invInput()
	{

	}

	/**
	 * Update method when Item is in Inventory and so not updated by Manager.OBJECTS.update() method;
	 */
	public void invUpdate()
	{

	}

	public void set(Item i)
	{
		this.material = i.material;
		this.x = i.x;
		this.y = i.y;
		this.width = i.width;
		this.height = i.height;
		this.drawOrder = i.drawOrder;
	}
}
