package tdg.game.spells;

public enum Elements
{
	FIRE("Crimson Red"), WIND("Ash Gray"), EARTH("Ocher Brown"), WATER("Irridiescent Blue"), 
	ELECTRIC("GOLD Yellow"), ICE("Azure Sky"), STEEL("Adamantium Dark Gray"), NATURE("Green Forest"), 
	LIGHT("Ivory White"), SHADOW("Ebony Black"), TIME("Bronze Orange"), SPACE("Amethyst Purple");

	private String name;

	Elements(String name)
	{
		this.name = name;
	}
}
