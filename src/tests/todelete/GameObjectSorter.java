package tests.todelete;

import java.util.ArrayList;
import java.util.List;

import tdg.game.objects.GameObject;

public class GameObjectSorter
{

	public static List<GameObject> sortDrawOrder(List<GameObject> objects)
	{
		GameObject[] nonSortedArray = listToArray(objects);
		GameObject[] sortedArray = new GameObject[nonSortedArray.length];
		GameObject temp;
		for(int i = 0; i < nonSortedArray.length - 1; i++)
		{
			for(int j = 0; j < nonSortedArray.length; j++)
			{
				// if(nonSortedArray[j].drawOrder > nonSortedArray[i].drawOrder)
				{
					temp = nonSortedArray[i];
					nonSortedArray[i] = nonSortedArray[j];
					nonSortedArray[j] = temp;
					sortedArray = nonSortedArray;
				}
			}
		}
		return arrayToList(sortedArray);
	}

	public static GameObject[] listToArray(List<GameObject> objects)
	{
		GameObject[] res = new GameObject[objects.size()];
		for(int i = 0; i < objects.size(); i++)
		{
			res[i] = objects.get(i);
		}
		return res;
	}

	public static List<GameObject> arrayToList(GameObject[] objects)
	{
		List<GameObject> res = new ArrayList<GameObject>();
		for(int i = 0; i < objects.length; i++)
		{
			res.add(i, objects[i]);
		}
		return res;
	}

	private static int[] sortArray(int[] nonSortedArray)
	{
		int[] sortedArray = new int[nonSortedArray.length];
		int temp;
		for(int i = 0; i < nonSortedArray.length - 1; i++)
		{
			for(int j = i + 1; j < nonSortedArray.length; j++)
			{
				if(nonSortedArray[i] > nonSortedArray[j])
				{
					temp = nonSortedArray[i];
					nonSortedArray[i] = nonSortedArray[j];
					nonSortedArray[j] = temp;
					sortedArray = nonSortedArray;
				}
			}
		}
		return sortedArray;
	}
}
