package tdg.game;

import tdg.game.objects.GameObjectManager;
import tdg.game.objects.blocks.Block;
import tdg.game.objects.entities.Entity;
import tdg.game.objects.items.Item;
import tdg.game.utils.Mathf;

public class Manager
{
	public static final GameObjectManager OBJECTS = new GameObjectManager();

	public static void spawn(Entity type)
	{
		// monster rank + level repartition
		type.stats.setLevel((int)Mathf.random(1, 500));
		OBJECTS.add(type);
	}

	public static void place(Block block)
	{
		OBJECTS.add(block);
	}

	public static void loot(Item item)
	{
		OBJECTS.add(item);
	}
}
