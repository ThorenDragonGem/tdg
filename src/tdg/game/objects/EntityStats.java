package tdg.game.objects;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import tdg.game.attributes.Attribute;
import tdg.game.objects.entities.Entity;
import tdg.game.stats.Stats;
import tdg.game.utils.Mathf;

public class EntityStats
{
	public static Entity entity;
	public Entity target;
	public float health;
	private int level;
	private float totalXP;
	// TODO: change to Map<Stats, Map<String, Attribute>> ?
	private Map<Stats, List<Attribute>> attributes;
	private Map<String, Stats> stats;

	public EntityStats(Entity e)
	{
		entity = e;
		target = null;
		stats = new HashMap<String, Stats>();
		attributes = new HashMap<Stats, List<Attribute>>();
		// TODO: ATTACK_SPEED => CoolDown length;
		stats.put("defense", new Stats(0f, 0f));
		stats.put("strength", new Stats(1f, 0f));
		stats.put("velocity", new Stats(1f, 0f));
		stats.put("atr", new Stats(Mathf.sqrt(entity.width * entity.width + entity.height * entity.height) / 2, Mathf.sqrt(entity.width * entity.width + entity.height * entity.height) / 2));
		stats.put("sgr", new Stats(500f, Mathf.sqrt(entity.width * entity.width + entity.height * entity.height) / 2));
		stats.put("luck", new Stats(0f, 0f, 100f));
		stats.put("healthMax", new Stats(100f, 1f));
		stats.put("pen", new Stats(0f, 0f, 100f));
		stats.put("regen", new Stats(0f, 0f));
		stats.put("critChance", new Stats(0f, 0f, 100f));
		stats.put("critDmg", new Stats(2f, 0f));
		stats.put("cdr", new Stats(0f, 0f, 100f));
		stats.put("as", new Stats(1f, 0f));
		for(Stats s : stats.values())
			attributes.put(s, new CopyOnWriteArrayList<Attribute>());
		health = get("healthMax");
	}

	public void heal(float amount)
	{
		amount = Mathf.abs(amount);
		this.health += amount;
		this.health = Mathf.maximize(health, get("healthMax"));
	}

	public void damage(float factor)
	{
		if(target.defending)
			target.stats.health -= calculateDamages(factor) * 0.15f;
		else
			target.stats.health -= calculateDamages(factor);
		health = Mathf.minimize(health, 0);
	}

	// Entity damages its target => entity's strength and target's defense
	private float calculatePenetration()
	{
		float strength = get("strength");
		float penetrationFactor = get("pen");
		return (Mathf.sigm(2 * strength, penetrationFactor / 100) - strength);
	}

	private float calculateDamageReduction()
	{
		float defense = target.stats.get("defense");
		float strength = get("strength");
		return ((defense / 4) * (defense / 8)) / (strength + 1);
	}

	private float calculateDamages(float factor)
	{
		float critRoulette = (int)Mathf.random(0f, 100f);
		float strength = get("strength");
		if(critRoulette <= get("critChance") && critRoulette != 0)
			return ((strength * factor) / ((calculateDamageReduction() / 4) / (calculatePenetration() + 1) + 1)) * get("critDmg");
		return (strength * factor) / ((calculateDamageReduction() / 4) / (calculatePenetration() + 1) + 1);
	}

	public void calculateAll()
	{
		for(Stats s : attributes.keySet())
			s.calculateAttribute(attributes.get(s));
	}

	public void addAttribute(String s, Attribute attribute)
	{
		attributes.get(stats.get(s)).add(attribute);
	}

	public void addAttribute(Stats s, Attribute attribute)
	{
		attributes.get(s).add(attribute);
	}

	public void removeAttribute(String s, Attribute attribute)
	{
		if(attributes.get(stats.get(s)).contains(attribute))
			attributes.get(stats.get(s)).remove(attribute);
	}

	public void removeAttribute(Stats s, Attribute attribute)
	{
		if(attributes.get(s).contains(attribute))
			attributes.get(s).remove(attribute);
	}

	public void removeAttribute(String s, int index)
	{
		if(attributes.get(stats.get(s)).get(index) != null)
			attributes.get(stats.get(s)).remove(index);
	}

	public void removeAttribute(Stats s, int index)
	{
		if(attributes.get(s).get(index) != null)
			attributes.get(s).remove(index);
	}

	public float get(String s)
	{
		return stats.get(s).calculateAttribute(attributes.get(stats.get(s)));
	}

	public Stats getStat(String s)
	{
		return stats.get(s);
	}

	public Map<Stats, List<Attribute>> getAttributes()
	{
		return attributes;
	}

	public List<Attribute> getAttributes(Stats s)
	{
		return attributes.get(s);
	}

	public boolean isAlive()
	{
		return health > 0;
	}

	// TODO ???
	// public int getIndex(Stats s, Attribute attribute)
	// {
	// for(int i = 0; i < attributes.get(s).size(); i++)
	// if(attributes.get(s).get(i) == attribute)
	// return i;
	// return -1;
	// }

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public float getTempUp(String s)
	{
		return stats.get(s).getTempUp();
	}

	/**
	 * Uses Stats.LUCK to give a random value between valueMin and valueMax. More luck means value will be nearer than valueMax, and vice-versa.
	 * LUCK is a bias for the random number generation
	 */
	public void addTempUp(String s, float valueMin, float valueMax)
	{
		if(valueMax < valueMin)
		{
			float temp;
			temp = valueMin;
			valueMin = valueMax;
			valueMax = temp;
		}
		float value = Mathf.getBiasedRandom(get("luck") / 200 - (valueMax - valueMin) / 2, valueMin, valueMax);
		value = Mathf.minimize(value, 0);
		stats.get(s).addTempUp(value);
		// s.tempUp += value;
	}

	public void applyTempUp()
	{
		for(Stats s : stats.values())
		{
			s.addBaseValue(s.getTempUp());
			s.setTempUp(0);
		}
	}

	@Override
	public String toString()
	{
		String string = "";
		for(String s : stats.keySet())
		{
			if(s == null)
				continue;
			string += s + ": " + Float.toString(stats.get(s).getBaseValue()) + " | " + Float.toString(get(s)) + ", " + Float.toString(stats.get(s).getTempUp()) + " ; ";
		}
		return string.substring(0, string.length() - 3);
	}

	public String getThousandsString(float value)
	{
		if(value == 0)
			return "0,00";
		int thousands = 0;
		float res = value;
		while(res >= 1000)
		{
			res /= 1000;
			thousands++;
		}
		switch(thousands)
		{
			case 0:
				return getTwoDigits(value);
			case 1:
				return getTwoDigits(value / 1000f) + "k";
			case 2:
				return getTwoDigits(value / 1000000f) + "M";
			case 3:
				return getTwoDigits(value / 1000000000f) + "G";
			case 4:
				return getTwoDigits(value / 1000000000000f) + "T";
		}
		return getTwoDigits(value / 1000000000000f) + "T";
	}

	public String getTwoDigits(float value)
	{
		return new DecimalFormat("##.00").format(value);
	}
}
