package tdg.game.objects.inventory;

import tdg.game.objects.items.Item;
import tdg.game.objects.items.ItemStack;

public class Inventory
{
	// TODO: replace Integer by List<Item> or Item[]
	private ItemStack[] items;
	private int slots;
	// TODO: Work with ItemStack and no longer with Item
	public Inventory(int slots)
	{
		items = new ItemStack[slots];
		this.slots = slots;
	}

	public void add(ItemStack stack)
	{
		items[getNextFreeSlot()] = stack;
	}

	public void remove(ItemStack stack)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == stack)
				items[i] = null;
		}
	}

	public void clear()
	{
		for(ItemStack stack : items)
			stack = null;
	}

	public int getIndex(ItemStack stack)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == stack)
				return i;
		}
		return -1;
	}

	public int getIndex(Item item)
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i].getSameInstance(item))
				return i;
		}
		return -1;
	}

	public ItemStack getFreeItemStack(Item i)
	{
		for(ItemStack stack : items)
			if(stack != null && stack.getSameInstance(i) && stack.hasFreeSlot())
				return stack;
		return null;
	}

	public Item getItemType(int index)
	{
		return items[index].getItemType();
	}

	public ItemStack[] getList()
	{
		return items;
	}

	public int getNextFreeSlot()
	{
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == null)
				return i;
		}
		return -1;
	}

	public boolean hasFreeSlot()
	{
		if(getNextFreeSlot() != -1)
			return true;
		else
			return false;
	}

	public boolean hasFreeSlot(Item i)
	{
		for(ItemStack stack : items)
		{
			if(stack == null)
				return true;
			if(stack.getSameInstance(i) && stack.hasFreeSlot())
				return true;
		}
		return false;
	}

	public int getSlots()
	{
		return slots;
	}

	@Override
	public String toString()
	{
		String string = "";
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == null)
				string += "(null = 0) ; ";
			else
				string += "(" + items[i].getItemType() + " = " + items[i].getCurrentSize() + "/" + items[i].getMaxSize() + ") ; ";
		}
		return string.substring(0, string.length() - 3);
	}

	public void input()
	{
		for(ItemStack stack : items)
			if(stack != null)
				stack.getItemType().invInput();
		// Thus, stack of one for equipment.
	}

	public void update()
	{
		for(ItemStack stack : items)
			if(stack != null)
				stack.getItemType().invUpdate();
		// Thus, stack of one for equipment.
	}

	public ItemStack getStack(Item i)
	{
		for(ItemStack stack : items)
		{
			if(stack.getSameInstance(i))
				return stack;
		}
		return null;
	}
}

// public class Inventory
// {
// private Item[] items;// TODO Item[][1]; or stacks
// private int slots;
//
// public Inventory(int slots)
// {
// items = new Item[slots];
// this.slots = slots;
// }
//
// public void add(Item i)
// {
// if(getNextFreeSlot() != -1)
// items[getNextFreeSlot()] = i;
// }
//
// public void add(Item i, int index)
// {
// if(items[index] == i)
// return;
// items[index] = i;
// }
//
// public void remove(int index)
// {
// items[index] = null;
// }
//
// public void clear()
// {
// for(int i = 0; i < items.length; i++)
// items[i] = null;
// }
//
// public int getNextFreeSlot()
// {
// for(int i = 0; i < items.length; i++)
// {
// if(items[i] == null)
// return i;
// }
// return -1;
// }
//
// public boolean hasFreeSlot()
// {
// if(getNextFreeSlot() != -1)
// return true;
// else
// return false;
// }
//
// public int slots()
// {
// return slots;
// }
//
// /**
// * Modifies the slot of the inventory.
// * If newSlots < lastSlots => items with index > newSlots disapears.
// * else, creates new empty slots;
// * @param slots
// */
// public void setSlots(int slots)
// {
// Item[] items2 = items;
// items = new Item[slots];
// for(int i = 0; i < items.length; i++)
// items[i] = items2[i];
// this.slots = slots;
// items2 = null;
// }
//
// public Item get(int index)
// {
// return items[index];
// }
//
// public int get(Item item)
// {
// for(int i = 0; i < items.length; i++)
// if(items[i] == item)
// return i;
// return -1;
// }
//
// public Item[] getList()
// {
// return items;
// }
//
// @Override
// public String toString()
// {
// String string = "";
// for(int i = 0; i < items.length; i++)
// string += items[i] + "(" + i + ") ; ";
// string = string.substring(0, string.length() - 3);
// return string;
// }
//
// public void input()
// {
// for(Item item : items)
// if(item != null)
// item.invInput();
// }
//
// public void update()
// {
// for(Item item : items)
// if(item != null)
// item.invUpdate();
// }
// }