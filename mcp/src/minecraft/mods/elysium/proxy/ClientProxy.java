package mods.elysium.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import mods.elysium.DefaultProps;
import mods.elysium.Elysium;
import mods.elysium.client.gui.menu.*;
import mods.elysium.dimension.portal.ElysianTileEntityPortal;
import mods.elysium.dimension.portal.ElysianTileEntityPortalRenderer;
import mods.elysium.entity.EntityDrachma;
import mods.elysium.entity.EntityCatorPillar;
import mods.elysium.entity.EntityGerbil;
import mods.elysium.entity.tileentity.TileEntityElysianWorkbench;
import mods.elysium.entity.tileentity.TileEntityShrinePillar;
import mods.elysium.entity.tileentity.TileFancyWorkbench;
import mods.elysium.render.*;
import net.aetherteam.mainmenu_api.MainMenuAPI;
import net.minecraft.block.Block;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public Object getClient()
	{
		return FMLClientHandler.instance().getClient();
	}
	
	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
	@Override
	public void registerRenderers()
	{
		MainMenuAPI.registerMenu("Elysian Menu", ElysianMenu.class);
		//MainMenuAPI.registerMenu("Misc Elysian Menu", MiscElysianMenu.class);
		
		Elysium.fancyWorkbenchRenderID = RenderingRegistry.getNextAvailableRenderId();

		
		ClientRegistry.bindTileEntitySpecialRenderer(ElysianTileEntityPortal.class, new ElysianTileEntityPortalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShrinePillar.class, new RenderTileEntityShrinePillar());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFancyWorkbench.class, new RenderFancyWorkbench());
		
		RenderingRegistry.registerBlockHandler(new RenderFancyWorkbench());

		
		RenderingRegistry.registerEntityRenderingHandler(EntityDrachma.class, new RenderDrachmaOBJ());
		RenderingRegistry.registerEntityRenderingHandler(EntityCatorPillar.class, new RenderCaterPillar());
		RenderingRegistry.registerEntityRenderingHandler(EntityGerbil.class, new RenderGerbil());
	}
	
	@Override
	public void installSounds()
	{
		this.installSound("resources/streaming/FluteTrack.wav");
		this.installSound("resources/streaming/Uranus Paradise Short.ogg");
	}
	
	private void installSound(String filename)
	{
		File soundFile = new File(ModLoader.getMinecraftInstance().mcDataDir, filename);

		if (!soundFile.exists())
		{
			try
			{
				String srcPath = filename;
				InputStream inStream = Elysium.class.getResourceAsStream(srcPath);
				if (inStream == null)
				{
					throw new IOException();
				}
				
				if (!soundFile.getParentFile().exists())
				{
					soundFile.getParentFile().mkdirs();
				}
				
				BufferedInputStream fileIn = new BufferedInputStream(inStream);
				BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(soundFile));
				byte[] buffer = new byte[1024];
				int n = 0;
				while (-1 != (n = fileIn.read(buffer)))
				{
					fileOut.write(buffer, 0, n);
				}
				fileIn.close();
				fileOut.close();
			}
			catch (IOException ex)
			{
				
			}
		}
		
		if (soundFile.canRead() && soundFile.isFile())
		{
			ModLoader.getMinecraftInstance().installResource(filename, soundFile);
			System.out.println(Elysium.consolePrefix+"Loaded soundfile: " + soundFile);
		}
		else
		{
			System.err.println(Elysium.consolePrefix+"Could not load soundfile: " + soundFile);
		}
	}
}
