package tdg.game.objects.items;

public class ItemStack
{
	private Item[] itemsInStack;
	private int maxSize, currentSize;

	public ItemStack(Item itemType, int maxSize)
	{
		this.itemsInStack = new Item[maxSize];
		this.itemsInStack[0] = itemType;
		this.maxSize = maxSize;
		this.currentSize = 0;
	}

	public void addItem(Item item)
	{
		if(!getSameInstance(item))
			return;
		if(hasFreeSlot())
		{
			itemsInStack[getNextFreeSlot()] = item;
			// increment currentSize to have the number of elements in
			// ItemStack;
			currentSize++;
		}
	}

	public void removeItem(Item item)
	{
		if(!getSameInstance(item))
			return;
		if(getIndex(item) != -1)
		{
			itemsInStack[getIndex(item)] = null;
			currentSize--;
		}
	}

	public int getCurrentSize()
	{
		return currentSize;
	}

	public int getMaxSize()
	{
		return maxSize;
	}

	public Item getItemType()
	{
		return itemsInStack[0];
	}

	public boolean getSameInstance(Item i)
	{
		return itemsInStack[0].getClass() == i.getClass();
	}

	public boolean hasFreeSlot()
	{
		return getNextFreeSlot() != -1;
	}

	public int getNextFreeSlot()
	{
		for(int i = 0; i < itemsInStack.length; i++)
		{
			if(itemsInStack[i] == null)
				return i;
		}
		return -1;
	}

	public int getIndex(Item item)
	{
		for(int i = 0; i < itemsInStack.length; i++)
		{
			if(itemsInStack[i] == item)
				return i;
		}
		return -1;
	}

	@Override
	public String toString()
	{
		String string = "";
		for(int i = 0; i < itemsInStack.length; i++)
		{
			if(itemsInStack[i] == null)
				string += "null ; ";
			else
				string += itemsInStack[i].toString() + " ; ";
		}
		return string.substring(0, string.length() - 3);
	}

	public Item[] getItems()
	{
		return itemsInStack;
	}
}
