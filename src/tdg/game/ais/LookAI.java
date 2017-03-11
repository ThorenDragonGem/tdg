package tdg.game.ais;

import tdg.game.objects.GameObject;
import tdg.game.objects.entities.Entity;

public class LookAI extends AI
{
	private Entity entity;
	private Class<? extends GameObject> targetType;

	public LookAI(Entity entity, Class<? extends GameObject> targetType)
	{
		this.entity = entity;
		this.targetType = targetType;
	}

	@Override
	public void updateAI()
	{
		// for(GameObject object : Physics.sphereCollide(new Circle(entity.x +
		// entity.width / 2, entity.y + entity.height / 2,
		// entity.stats.get(Stats.SIGHT_RANGE))))
		// {
		// if(object == entity || !(targetType.getClass().isInstance(object)))
		// continue;
		// entity.stats.target = (Entity)object;
		// }
	}
}
