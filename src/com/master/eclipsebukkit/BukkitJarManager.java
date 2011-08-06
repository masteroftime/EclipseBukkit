package com.master.eclipsebukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IProgressMonitor;

public class BukkitJarManager 
{
	private static Set<Integer> bukkitFiles;
	private static Set<Integer> craftBukkitFiles;
	
	private static int[] lastBuild;
	
	private static void init()
	{		
		if(!new File("plugins/com.master.eclipsebukkit").exists())
		{
			new File("plugins/com.master.eclipsebukkit/builds/bukkit").mkdirs();
			new File("plugins/com.master.eclipsebukkit/builds/craftbukkit").mkdirs();
			
			bukkitFiles = new HashSet<Integer>();
			craftBukkitFiles = new HashSet<Integer>();
		}
		else
		{
			bukkitFiles = new HashSet<Integer>();
			craftBukkitFiles = new HashSet<Integer>();
			
			File bukkitDir = new File("plugins/com.master.eclipsebukkit/builds/bukkit");
			for(File f : bukkitDir.listFiles())
			{
				int version = Integer.parseInt(f.getName().substring(0, f.getName().lastIndexOf(".")));
				bukkitFiles.add(version);
			}
			
			File craftBukkitDir = new File("plugins/com.master.eclipsebukkit/builds/craftbukkit");
			for(File f : craftBukkitDir.listFiles())
			{
				int version = Integer.parseInt(f.getName().substring(0, f.getName().lastIndexOf(".")));
				craftBukkitFiles.add(version);
			}
		}
	}
	
	public static InputStream getBukkitFile(int version, IProgressMonitor monitor)
	{
		if(bukkitFiles == null)
			init();
		
		if(version < 0)
		{
			return null;
		}
		if(version == 0)
		{
			if(lastBuild == null) lastBuild = BukkitPluginSupport.getLastBuildNr();
			version = lastBuild[1];
		}
		
		if(bukkitFiles.contains(version))
		{
			try {
				return new FileInputStream("plugins/com.master.eclipsebukkit/builds/bukkit/"+version+".jar");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				URLConnection conn = new URL("http://ci.bukkit.org/job/dev-Bukkit/"+(version==-1?"recommended":version)+"/artifact/target/bukkit-0.0.1-SNAPSHOT.jar").openConnection();
				InputStream in = conn.getInputStream();
				OutputStream out = new FileOutputStream("plugins/com.master.eclipsebukkit/builds/bukkit/"+version+".jar");
				
				byte[] buffer = new byte[1024];
				int size = conn.getContentLength();
				if(size == 0) size = 3000000;
				monitor.beginTask("Downloading Bukkit", size);
				conn.connect();
				int read;
				while((read = in.read(buffer)) != -1)
				{
					out.write(buffer, 0, read);
					monitor.worked(read);
				}
				in.close();
				out.close();
				
				bukkitFiles.add(version);
				
				monitor.done();
				
				return new FileInputStream("plugins/com.master.eclipsebukkit/builds/bukkit/"+version+".jar");
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static InputStream getCraftBukkitFile(int version, IProgressMonitor monitor)
	{
		if(craftBukkitFiles == null)
			init();
		
		if(version < 0)
		{
			return null;
		}
		if(version == 0)
		{
			if(lastBuild == null) lastBuild = BukkitPluginSupport.getLastBuildNr();
			version = lastBuild[0];
		}
		
		if(craftBukkitFiles.contains(version))
		{
			try {
				return new FileInputStream("plugins/com.master.eclipsebukkit/builds/craftbukkit/"+version+".jar");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				URLConnection conn = new URL("http://ci.bukkit.org/job/dev-CraftBukkit/"+(version==-1?"recommended":version)+"/artifact/target/craftbukkit-0.0.1-SNAPSHOT.jar").openConnection();
				InputStream in = conn.getInputStream();
				OutputStream out = new FileOutputStream("plugins/com.master.eclipsebukkit/builds/craftbukkit/"+version+".jar");
				
				byte[] buffer = new byte[1024];
				int size = conn.getContentLength();
				if(size == 0) size = 8000000;
				monitor.beginTask("Downloading CraftBukkit", size);
				conn.connect();
				int read;
				while((read = in.read(buffer)) != -1)
				{
					out.write(buffer, 0, read);
					monitor.worked(read);
				}
				in.close();
				out.close();
				
				craftBukkitFiles.add(version);
				
				monitor.done();
				
				return new FileInputStream("plugins/com.master.eclipsebukkit/builds/craftbukkit/"+version+".jar");
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
