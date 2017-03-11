package tdg.game.objects.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import tdg.game.Tales;
import tdg.game.attributes.Attribute;
import tdg.game.attributes.DependantAttribute;
import tdg.game.attributes.FinalBonus;
import tdg.game.graphics.Material;

public class Sword extends Equipment
{
	DependantAttribute attribute;
	Attribute finalAttribute;
	boolean added = false;

	public Sword(float x, float y, float width, float height)
	{
		super(new Material(Tales.assets.getTexture("knife.png")), x, y, width, height, 1);
		attribute = new DependantAttribute(10f);
		finalAttribute = new Attribute(0f);
	}

	@Override
	public void invInput()
	{
		super.invInput();
		// Skill CoolDown Effect: if skill activated <=> added => buff of 10f
		// strength; on timer end => back to strength without skill buff;
		// moreover if timer is ended, you can activate buff another time.
		if(Gdx.input.isKeyJustPressed(Input.Keys.U) && holder != null)
		{
			if(!added)
			{
				attribute.addAttribute("armor", finalAttribute.addFinalBonus(new FinalBonus(10f, 0, 100).start(finalAttribute)));
				added = true;
			}
		}
		if(attribute.getAttributes().get("armor") != null)
		{
			boolean temp = true;
			for(int i = 0; i < attribute.getAttributes().get("armor").getFinalBonuses().size(); i++)
			{
				temp = temp && attribute.getAttributes().get("armor").getFinalBonuses().get(i).getTimer().isOver();
			}
			if(temp)
			{
				added = false;
			}
		}
	}

	@Override
	public void invUpdate()
	{
		super.invUpdate();
		attribute.updateFinalBonus();
	}

	@Override
	public void onPickUp()
	{
		super.onPickUp();
		add("strength", attribute);
	}
}
