package tdg.game.objects.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tdg.game.attributes.Attribute;
import tdg.game.graphics.Material;
import tdg.game.stats.Stats;
import tdg.game.utils.Mathf;

public class Equipment extends Item
{
	protected Map<Stats, List<Attribute>> attributes;
	protected Map<Stats, List<Attribute>> toRemoveInHolder;

	protected boolean modified;

	public Equipment(Material material, float x, float y, float width, float height, int drawOrder)
	{
		super(material, x, y, width, height, (int)(100 + Mathf.maximize(drawOrder, 200)));
		// draw order between 300 and 500
		attributes = new HashMap<Stats, List<Attribute>>();
		toRemoveInHolder = new HashMap<Stats, List<Attribute>>();
		modified = false;
		maxStackSize = 1;
	}

	public Equipment()
	{
		super();
	}

	@Override
	public void invInput()
	{
		super.invInput();
	}

	@Override
	public void invUpdate()
	{
		super.invUpdate();
		if(modified && inInventory)
		{
			removeAttributesToHolder();
			addAttributesToHolder();
			modified = false;
		}
	}

	// TODO: add constructor BitmapFont
	// GlyphLayout layout = new GlyphLayout(Tales.font, "Equip.");
	@Override
	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		super.render(batch, renderer);
		// Tales.font.draw(batch, layout, x - layout.width / 2 + width / 2, y +
		// height);
	}

	public void add(String s, Attribute attribute)
	{
		if(holder == null)
			return;
		if(attributes.get(holder.stats.getStat(s)) == null)
			attributes.put(holder.stats.getStat(s), new CopyOnWriteArrayList<Attribute>());
		attributes.get(holder.stats.getStat(s)).add(attribute);
		modified = true;
	}

	public void remove(String s, Attribute attribute)
	{
		if(holder == null)
			return;
		if(attributes.get(holder.stats.getStat(s)) == null)
			return;
		if(attributes.get(holder.stats.getStat(s)).contains(attribute))
		{
			if(toRemoveInHolder.get(holder.stats.getStat(s)) == null)
				toRemoveInHolder.put(holder.stats.getStat(s), new CopyOnWriteArrayList<Attribute>());
			toRemoveInHolder.get(holder.stats.getStat(s)).add(attribute);
			attributes.get(holder.stats.getStat(s)).remove(attribute);
		}
		modified = true;
	}

	public void remove(String s, int index)
	{
		if(holder == null)
			return;
		if(attributes.get(holder.stats.getStat(s)) == null)
			return;
		if(attributes.get(holder.stats.getStat(s)).get(index) != null)
		{
			toRemoveInHolder.get(holder.stats.getStat(s)).add(attributes.get(holder.stats.getStat(s)).get(index));
			attributes.get(holder.stats.getStat(s)).remove(index);
		}
		modified = true;
	}

	public void addAttributesToHolder()
	{
		for(Stats s : attributes.keySet())
		{
			for(Attribute attribute : attributes.get(s))
			{
				if(!holder.stats.getAttributes().get(s).contains(attribute))
					holder.stats.addAttribute(s, attribute);
			}
		}
	}

	/**
	 * Remove all attributes from this Equipment from holder's attributes
	 * <strong>Does not remove all Attribute of the Equipment</strong>
	 */
	public void removeAllAttrbiutesFromHolder()
	{
		for(Stats s : attributes.keySet())
		{
			for(Attribute attribute : attributes.get(s))
			{
				if(toRemoveInHolder.get(s) == null)
					toRemoveInHolder.put(s, new CopyOnWriteArrayList<Attribute>());
				toRemoveInHolder.get(s).add(attribute);
			}
		}
	}

	public void removeAttributesToHolder()
	{
		for(Stats s : toRemoveInHolder.keySet())
		{
			for(Attribute attribute : toRemoveInHolder.get(s))
			{
				holder.stats.removeAttribute(s, attribute);
			}
			toRemoveInHolder.get(s).clear();
		}
	}

	public void onPickUp()
	{

	}
}
