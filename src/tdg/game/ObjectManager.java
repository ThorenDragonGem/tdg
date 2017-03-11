package tdg.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObjectManager<E>
{
	protected List<E> objects;

	public ObjectManager()
	{
		objects = new CopyOnWriteArrayList<E>();
	}

	public void add(E e)
	{
		if(objects.contains(e))
			return;
		objects.add(e);
	}

	public void add(E e, int index)
	{
		if(objects.contains(e))
			return;
		objects.add(index, e);
	}

	public void remove(E e)
	{
		if(!objects.contains(e))
			return;
		objects.remove(e);
	}

	public void remove(int index)
	{
		if(objects.get(index) == null)
			return;
		objects.remove(index);
	}

	public List<E> getObjects()
	{
		return objects;
	}

	public void clear()
	{
		objects.clear();
	}

	@Override
	public String toString()
	{
		String s = "[";
		for(E e : objects)
			s += e + " ; ";
		s = s.substring(0, s.length() - 3);
		return s;
	}
}
