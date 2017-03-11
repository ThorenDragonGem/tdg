package tdg.game.objects.inventory;

import tdg.game.objects.items.ItemStack;

public class Cell
{
	private ItemStack stack;
	private int x;
	private int y;

	public Cell(int x, int y, ItemStack stack)
	{
		this.x = x;
		this.y = y;
		this.stack = stack;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public ItemStack getItemStack()
	{
		return stack;
	}

	public void setItemStack(ItemStack stack)
	{
		this.stack = stack;
	}
}