package tdg.game.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import tdg.game.Manager;
import tdg.game.objects.GameObject;

public class Physics
{
	public static float dist(float x1, float y1, float x2, float y2)
	{
		float dx = x2 - x1;
		float dy = y2 - y1;
		return Mathf.sqrt(dx * dx + dy * dy);
	}

	public static List<GameObject> getCollisionList(GameObject object)
	{
		List<GameObject> res = new ArrayList<GameObject>();
		for(GameObject o : Manager.OBJECTS.getObjects())
		{
			if(Intersector.intersectRectangles(object.getBounds(), o.getBounds(), new Rectangle()))
				res.add(o);
		}
		return res;
	}

	public static List<GameObject> sphereCollide(Circle circle)
	{
		List<GameObject> res = new ArrayList<GameObject>();
		for(GameObject o : Manager.OBJECTS.getObjects())
		{
			if(Intersector.overlaps(circle, o.getBounds()))
				res.add(o);
		}
		return res;
	}

}
