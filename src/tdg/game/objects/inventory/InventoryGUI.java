package tdg.game.objects.inventory;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import tdg.game.Tales;
import tdg.game.graphics.Material;

public class InventoryGUI
{
	private Inventory inventory;
	private Cell[] cells;
	private Material material;
	private boolean toRest;
	private int rows, cols, rest, posX, posY, cellSize;

	public InventoryGUI(Inventory inventory, Material material, int x, int y, int cellSize, int maxSlotperRow)
	{
		this.inventory = inventory;
		this.material = material;
		this.cellSize = cellSize;
		this.posX = x;
		this.posY = y;
		cells = createQuadInventory(inventory.getSlots(), maxSlotperRow);
	}

	public InventoryGUI(Inventory inventory, Material material, int x, int y, int cellSize)
	{
		this.inventory = inventory;
		this.material = material;
		this.cellSize = cellSize;
		this.posX = x;
		this.posY = y;
	}

	private Cell[] createQuadInventory(int slots, int max)
	{
		if(slots == 0)
			return new Cell[0];
		cells = new Cell[slots];
		int i = 0;
		int x = 0;
		int y = 0;
		while(slots > max)
		{
			rows++;
			slots -= max;
		}
		rest = slots % max;
		if(rest != 0)
			rows++;
		if(rest > 0)
		{
			for(x = 0; x < rows - 1; x++)
			{
				for(y = 0; y < max; y++)
				{
					cells[i] = new Cell(x * cellSize + posX + cellSize / 4, y * cellSize + posY + cellSize / 4, null);
					i++;
				}
			}
			for(int k = 0; k < rest; k++)
			{
				cells[i] = new Cell(x * cellSize + posX + cellSize / 4, k * cellSize + posY + cellSize / 4, null);
				i++;
			}
		}
		else
		{
			for(x = 0; x < rows + 1; x++)
			{
				for(y = 0; y < max; y++)
				{
					cells[i] = new Cell(x * cellSize + posX + cellSize / 4, y * cellSize + posY + cellSize / 4, null);
					i++;
				}
			}
			for(int k = 0; k < rest; k++)
			{
				cells[i] = new Cell(x * cellSize + posX + cellSize / 4, k * cellSize + posY + cellSize / 4, null);
				i++;
			}
		}
		return cells;
	}

	// TODO: addCell(Cell cell);
	public void setCells(Cell[] cells)
	{
		this.cells = cells;
	}

	public Cell[] getCells()
	{
		return cells;
	}

	public void render(SpriteBatch batch, ShapeRenderer renderer)
	{
		if(material.getTexture() != null)
		{
			batch.begin();
			batch.draw(material.getTexture(), posX, posY, material.getTexture().getWidth(), material.getTexture().getHeight());
			batch.end();
		}
		for(int i = 0; i < cells.length; i++)
		{
			if(cells[i].getItemStack() != null && cells[i].getItemStack().getItemType() != null)
			{
				if(cells[i].getItemStack().getItemType().material.getTexture() != null)
				{
					batch.begin();
					batch.draw(cells[i].getItemStack().getItemType().material.getTexture(), cells[i].getY(), cells[i].getX(), cellSize / 2, cellSize / 2);
					if(cells[i].getItemStack().getCurrentSize() > 1)
						Tales.font.draw(batch, Integer.toString(cells[i].getItemStack().getCurrentSize() + 1), cells[i].getY(), cells[i].getX());
					batch.end();
				}
				else
				{
					renderer.begin(ShapeType.Filled);
					renderer.setColor(cells[i].getItemStack().getItemType().material.getColor());
					renderer.rect(cells[i].getY(), cells[i].getX(), cellSize / 2, cellSize / 2);
					renderer.end();
					batch.begin();
					if(cells[i].getItemStack().getCurrentSize() > 1)
						Tales.font.draw(batch, Integer.toString(cells[i].getItemStack().getCurrentSize() + 1), cells[i].getY(), cells[i].getX());
					batch.end();
				}
			}
		}
	}

	@Override
	public String toString()
	{
		String string = "";
		for(int i = 0; i < cells.length; i++)
		{
			if(cells[i] != null)
				string += "[" + cells[i].getY() + " " + cells[i].getX() + " " + cells[i].getItemStack().toString() + "] ";
			else
			{
				string += "null ; ";
				continue;
			}
		}
		return string;
	}

	public Cell getCell(int index)
	{
		return cells[index];
	}

	public int getWidth()
	{
		return material.getTexture().getWidth();
	}

	public int getHeight()
	{
		return material.getTexture().getHeight();
	}

	public void setPosition(int x, int y)
	{
		this.posX = x;
		this.posY = y;
	}
}