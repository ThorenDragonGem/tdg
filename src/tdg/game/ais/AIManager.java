package tdg.game.ais;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AIManager
{
	private Map<String, AI> ais;
	
	public AIManager()
	{
		ais = new ConcurrentHashMap<String, AI>();
	}

	public void addAI(String name, AI ai)
	{
		ais.put(name, ai);
	}

	public void removeAI(String name)
	{
		if(ais.get(name) != null)
			ais.remove(name);
	}

	public AI get(String name)
	{
		return ais.get(name);
	}

	// public void updateAIs()
	// {
	// for(AI ai : ais.values())
	// ai.updateAI();
	// }
}
