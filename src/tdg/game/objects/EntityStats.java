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
	private Map<String, Stats> stats;
	// TODO: change to Map<Stats, Map<String, Attribute>> ?
	private Map<Stats, List<Attribute>> attributes;
	public static Entity entity;
	public Entity target;
	public double health, mana, shield;
	private int level, maxLevel;
	private double nextLevelXp, totalXP;
	private boolean levelModified;

	public EntityStats(Entity e)
	{
		entity = e;
		target = null;
		stats = new HashMap<String, Stats>();
		attributes = new HashMap<Stats, List<Attribute>>();
		/** From here */
		// defense stat => damage reduction
		stats.put("defense", new LevelStats(entity.rank, 0f, 0f));
		// strength stat => damages
		stats.put("strength", new LevelStats(entity.rank, 1f, 0f));
		// speed stat
		stats.put("velocity", new Stats(1f, 0f));
		// attack range stat
		stats.put("atr", new Stats(Mathf.sqrt(entity.width * entity.width + entity.height * entity.height) / 2, Mathf.sqrt(entity.width * entity.width + entity.height * entity.height) / 2));
		// sight range stat => max distance an entity can look
		stats.put("sgr", new Stats(500f, Mathf.sqrt(entity.width * entity.width + entity.height * entity.height) / 2));
		// luck stat => influences stats growth and drop loots
		stats.put("luck", new Stats(0f, 0f, 100f));
		// max health stat => the maximum amount of health of the entity
		stats.put("healthMax", new LevelStats(entity.rank, 100f, 1f));
		// physic penetration stat => influences damages reduction by
		// penetrating the defense (in %)
		stats.put("pen", new Stats(0f, 0f, 100f));
		// regen stat => the amount of health regenerated each second (each 60
		// ticks)
		stats.put("regen", new LevelStats(entity.rank, 0f, 0f));
		// critical chance stat => the probability of performing a critical hit
		// (in %)
		stats.put("critChance", new Stats(0f, 0f, 100f));
		// critical damages => damages done while performing a critical hit:
		// normal_damages * critical_damages
		stats.put("critDmg", new Stats(2f, 0f));
		// attack speed stat => the base length (before reduction, of CoolDown
		stats.put("as", new Stats(1f, 0f));
		// CoolDownReduction stat => reduction of the length of CoolDown (in %)
		stats.put("cdr", new Stats(0f, 0f, 100f));
		// SpellVamp chance stat => probability of healing entity when damaging
		// target (in %)
		stats.put("vampChance", new Stats(0f, 0f, 100f));
		// SpellVamp stat => amount of damages performed transformed in health
		// (in %)
		stats.put("spellVamp", new Stats(0f, 0f, 100f));
		// shield stat => add a "non health" shield with damages priority:
		// damages reduces first shield and if damages > shield, the rest of
		// damages reduces health; non affected by heal(); and does't affect
		// spellVamp; shield works like another healthBar, with reduction...
		stats.put("shieldMax", new Stats(0f, 0f));
		// magic
		stats.put("power", new LevelStats(entity.rank, 0f, 0f));
		stats.put("resistance", new LevelStats(entity.rank, 0f, 0f));
		stats.put("mpen", new Stats(0f, 0f, 100f));
		stats.put("manaMax", new LevelStats(entity.rank, 100f, 0f));
		stats.put("manaRegen", new LevelStats(entity.rank, 0f, 0f));
		for(Stats s : stats.values())
			attributes.put(s, new CopyOnWriteArrayList<Attribute>());
		health = get("healthMax");
		shield = get("shieldMax");
		mana = get("manaMax");
		maxLevel = 500;
		totalXP = 0;
		setLevel(1);
		/** to here => global for each Entity stats*/
	}

	/**
	 *  <strong>Only applicable for NPC</strong> else it means nothing
	 */
	public void applyStatsForLevel()
	{
		for(Stats s : stats.values())
		{
			if(!(s instanceof LevelStats))
				continue;
			if(!getAttributes(s).isEmpty())
				continue;
			addAttribute(getStat(getString(s)), new Attribute(0), 0);
			// addAttribute(getStat(getString(s)), new
			// Attribute(s.getRank().getMaxLevelConstant()), 0);
		}
		if(levelModified)
		{
			// TODO: add random parameter
			attributes.get(getStat("strength")).set(0, new Attribute(((LevelStats)getStat("strength")).calculateSqrtLevelValue(level, maxLevel) / 2d));
			// defense value level function is a square function
			attributes.get(getStat("defense")).set(0, new Attribute(((LevelStats)getStat("defense")).calculateSquareLevelValue(level, maxLevel) / 2.5d));
			attributes.get(getStat("healthMax")).set(0, new Attribute(((LevelStats)getStat("healthMax")).calculateLinearLevelValue(level, maxLevel)));
			heal(Double.MAX_VALUE);
			attributes.get(getStat("regen")).set(0, new Attribute(((LevelStats)getStat("regen")).calculateLinearLevelValue(level, maxLevel) / 10000d));
			attributes.get(getStat("power")).set(0, new Attribute(((LevelStats)getStat("power")).calculateSqrtLevelValue(level, maxLevel) / 2d));
			attributes.get(getStat("resistance")).set(0, new Attribute(((LevelStats)getStat("resistance")).calculateSquareLevelValue(level, maxLevel) / 2.5d));
			attributes.get(getStat("manaMax")).set(0, new Attribute(((LevelStats)getStat("manaMax")).calculateLinearLevelValue(level, maxLevel)));
			attributes.get(getStat("manaRegen")).set(0, new Attribute(((LevelStats)getStat("manaRegen")).calculateLinearLevelValue(level, maxLevel) / 10000d));
			levelModified = false;
		}
	}

	public void setShield(double amount)
	{
		amount = Math.abs(amount);
		this.shield = amount;
		getStat("shieldMax").setBaseValue(shield);
	}

	public void heal(double amount)
	{
		amount = Math.abs(amount);
		this.health += amount;
		this.health = Mathf.maximize(health, get("healthMax"));
	}

	public void mana(double amount)
	{
		amount = Math.abs(amount);
		this.mana += amount;
		this.mana = Mathf.maximize(mana, get("manaMax"));
	}

	public void physicalDamages(double factor)
	{
		if(target.defending)
		{
			double damages = calculatePhysicalDamages(factor) * 0.15f;
			// shield damages
			if(target.stats.shield > 0)
			{
				if(damages > target.stats.shield)
				{
					damages -= target.stats.shield;
					target.stats.shield = 0;
				}
				else
				{
					target.stats.shield -= damages;
					damages = 0;
				}
			}
			// health damages
			target.stats.health -= damages;
			// spellVamp stuff
			double vampRoulette = Mathf.random();
			if(vampRoulette <= get("vampChance") / 100f && vampRoulette != 0)
			{
				double vampHeal = Mathf.random(0.75f, 1.25f) * damages * get("spellVamp") / 100;
				heal(vampHeal);
			}
		}
		else
		{
			double damages = calculatePhysicalDamages(factor);
			// shield damages
			if(target.stats.shield > 0)
			{
				if(damages > target.stats.shield)
				{
					damages -= target.stats.shield;
					target.stats.shield = 0;
				}
				else
				{
					target.stats.shield -= damages;
					damages = 0;
				}
			}
			// health damages
			target.stats.health -= damages;
			// spellVamp stuff
			double vampRoulette = Mathf.random();
			if(vampRoulette <= get("vampChance") / 100f && vampRoulette != 0)
			{
				double vampHeal = Mathf.random(0.75f, 1.25f) * damages * get("spellVamp");
				heal(vampHeal);
			}
		}
		health = Mathf.minimize(health, 0);
	}

	// Entity damages its target => entity's strength and target's defense
	private double calculatePhysicalPenetration()
	{
		// TODO: if pen = 100
		// => like 0 defense ? or huge reduction ? or as here N
		double strength = get("strength");
		double pen = get("pen");
		return (Mathf.sigm(2 * strength, pen / 100) - strength);
	}

	private double calculatePhysicalDamageReduction()
	{
		double defense = target.stats.get("defense");
		double strength = get("strength");
		return ((defense / 4) * (defense / 8)) / (strength + 1);
	}

	private double calculatePhysicalDamages(double factor)
	{
		double critRoulette = (int)Mathf.random(0f, 100f);
		double strength = get("strength");
		if(critRoulette <= get("critChance") && critRoulette != 0)
			return ((strength * factor) / ((calculatePhysicalDamageReduction() / 4) / (calculatePhysicalPenetration() + 1) + 1)) * get("critDmg");
		return (strength * factor) / ((calculatePhysicalDamageReduction() / 4) / (calculatePhysicalPenetration() + 1) + 1);
	}

	public void magicalDamages(double factor, double manaCost)
	{
		if(manaCost > mana)
			return;
		if(target.defending)
		{
			double damages = calculateMagicalDamages(factor) * 0.15f;
			// shield damages
			if(target.stats.shield > 0)
			{
				if(damages > target.stats.shield)
				{
					damages -= target.stats.shield;
					target.stats.shield = 0;
				}
				else
				{
					target.stats.shield -= damages;
					damages = 0;
				}
			}
			// health damages
			target.stats.health -= damages;
			// spellVamp stuff
			double vampRoulette = Mathf.random();
			if(vampRoulette <= get("vampChance") / 100f && vampRoulette != 0)
			{
				double vampHeal = Mathf.random(0.75f, 1.25f) * damages * get("spellVamp") / 100;
				heal(vampHeal);
			}
		}
		else
		{
			double damages = calculateMagicalDamages(factor);
			// shield damages
			if(target.stats.shield > 0)
			{
				if(damages > target.stats.shield)
				{
					damages -= target.stats.shield;
					target.stats.shield = 0;
				}
				else
				{
					target.stats.shield -= damages;
					damages = 0;
				}
			}
			// health damages
			target.stats.health -= damages;
			// spellVamp stuff
			double vampRoulette = Mathf.random();
			if(vampRoulette <= get("vampChance") / 100f && vampRoulette != 0)
			{
				double vampHeal = Mathf.random(0.75f, 1.25f) * damages * get("spellVamp");
				heal(vampHeal);
			}
		}
		health = Mathf.minimize(health, 0);
		mana -= manaCost;
	}

	private double calculateMagicalPenetration()
	{
		double power = get("power");
		double mpen = get("mpen");
		return (Mathf.sigm(2 * power, mpen / 100) - power);
	}

	private double calculateMagicalDamageReduction()
	{
		double resistance = target.stats.get("resistance");
		double power = get("power");
		return ((resistance / 8) * (resistance / 16)) / (power + 1);
	}

	private double calculateMagicalDamages(double factor)
	{
		double critRoulette = (int)Mathf.random(0f, 100f);
		double power = get("power");
		if(critRoulette <= get("critChance") && critRoulette != 0)
			return ((power * factor) / ((calculateMagicalDamageReduction() / 4) / (calculateMagicalPenetration() + 1) + 1)) * get("critDmg");
		return ((power * factor) / ((calculateMagicalDamageReduction() / 4) / (calculateMagicalPenetration() + 1) + 1));
	}

	public void setXp(double amount)
	{
		// xp available to level up
		double xp = amount;
		// while xp amount greater than next level xp needed
		while(xp > nextLevelXp && level <= maxLevel)
		{
			double next = nextLevelXp;
			// increment level by one
			addLevel(1);
			// subtract total xp available by the amount one xp needed for last
			// level up
			xp -= next;
			nextLevelXp = calculateNextLevelXp();
		}
		totalXP = amount;
	}

	public void addXp(double amount)
	{
		setXp(totalXP + amount);
	}

	public void addLevel(int amount)
	{
		setLevel(level + amount);
	}

	public void setLevel(int level)
	{
		this.level = (int)Mathf.maximize(level, 500);
		nextLevelXp = calculateNextLevelXp();
		levelModified = true;
	}

	public int getLevel()
	{
		return level;
	}

	public double calculateNextLevelXp()
	{
		// sequence: a(level) = 50 * 1.15^level
		// starts to 1 and not 0 thus xp to level 2 is 50
		return (50 * Mathf.pow(1.05f, level - 1));
	}

	public void initAll()
	{
		calculateAll();
		health = get("healthMax");
		shield = get("shieldMax");
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

	public void addAttribute(String s, Attribute attribute, int index)
	{
		attributes.get(stats.get(s)).add(index, attribute);
	}

	public void addAttribute(Stats s, Attribute attribute)
	{
		attributes.get(s).add(attribute);
	}

	public void addAttribute(Stats s, Attribute attribute, int index)
	{
		attributes.get(s).add(index, attribute);
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

	public double get(String s)
	{
		return stats.get(s).calculateAttribute(attributes.get(stats.get(s)));
	}

	public Stats getStat(String s)
	{
		return stats.get(s);
	}

	public String getString(Stats s)
	{
		for(String string : stats.keySet())
		{
			if(stats.get(string).equals(s))
				return string;
		}
		return null;
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

	public double getTempUp(String s)
	{
		return stats.get(s).getTempUp();
	}

	/**
	 * Uses Stats.LUCK to give a random value between valueMin and valueMax. More luck means value will be nearer than valueMax, and vice-versa.
	 * LUCK is a bias for the random number generation
	 */
	public void addTempUp(String s, double valueMin, double valueMax)
	{
		if(valueMax < valueMin)
		{
			double temp;
			temp = valueMin;
			valueMin = valueMax;
			valueMax = temp;
		}
		double value = Mathf.getBiasedRandom(get("luck") / 200 - (valueMax - valueMin) / 2, valueMin, valueMax);
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
			string += s + ": " + Double.toString(stats.get(s).getBaseValue()) + " | " + Double.toString(get(s)) + ", " + Double.toString(stats.get(s).getTempUp()) + " ; ";
		}
		return string.substring(0, string.length() - 3);
	}

	public String getThousandsString(double value)
	{
		if(value == 0)
			return "0,00";
		int thousands = 0;
		double res = value;
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
				return getTwoDigits(value / 1000d) + "k";
			case 2:
				return getTwoDigits(value / 1000000d) + "M";
			case 3:
				return getTwoDigits(value / 1000000000d) + "G";
			case 4:
				return getTwoDigits(value / 1000000000000d) + "T";
		}
		return getTwoDigits(value / 1000000000000d) + "T";
	}

	public String getTwoDigits(double value)
	{
		return new DecimalFormat("##.00").format(value);
	}

}
