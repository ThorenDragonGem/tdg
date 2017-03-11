package tdg.game.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets
{
	// public Map<String, Texture> textures;
	private AssetManager manager;

	public Assets()
	{
		// textures = new HashMap<String, Texture>();
		manager = new AssetManager();
		loadTexture("badlogic.jpg");
		loadTexture("chest.png");
		loadTexture("helm.png");
		loadTexture("inventory10x2.png");
		loadTexture("inventory10x4.png");
		loadTexture("knife.png");
		loadTexture("shield.png");
		loadTexture("slime.png");
		manager.finishLoading();
	}

	public void loadTexture(String path)
	{
		manager.load(path, Texture.class);
	}

	public Texture getTexture(String name)
	{
		return (Texture)manager.get(name);
	}

	public List<File> listFiles(String directoryName, String... extensions)
	{
		File directory = new File(directoryName);

		List<File> resultList = new ArrayList<File>();

		File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for(File file : fList)
		{
			if(file.isFile())
			{
				for(String s : extensions)
				{
					if(file.getName().substring(file.getName().lastIndexOf(".") + 1).contains(s))
					{
						// textures.put(file.getName(), new
						// Texture(file.getAbsolutePath()));
					}
				}
			}
			else if(file.isDirectory())
			{
				resultList.addAll(listFiles(file.getAbsolutePath()));
			}
		}
		return resultList;
	}
}
