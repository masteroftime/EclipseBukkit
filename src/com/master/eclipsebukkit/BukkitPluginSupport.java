package com.master.eclipsebukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.osgi.framework.adaptor.FilePath;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.ui.internal.activities.Persistence;
import org.osgi.framework.Bundle;

public class BukkitPluginSupport 
{
	public static void createBukkitProject(IJavaProject project, int bukkitVers, int craftVers, IProgressMonitor monitor)
	{
		try {
			Bundle bundle = Platform.getBundle("com.master.eclipsebukkit");
			project.getProject().getFile("src/plugin.yml").create(FileLocator.openStream(bundle, new Path("template.yml"), false), true, monitor);
			//project.getProject().getFolder("srv").create(false, true, null);
			project.getProject().getFile("bukkit.jar").create(getBukkit(bukkitVers), true, monitor);
			
			IClasspathEntry[] oldEntries = project.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length+1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newLibraryEntry(project.getPath().append("bukkit.jar"), null, null);
			project.setRawClasspath(newEntries, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addBukkitNature(IProject p)
	{
		try {
			IProjectDescription desc = p.getDescription();
			String[] oldIds = desc.getNatureIds();
			String[] newIds = new String[oldIds.length+1];
			System.arraycopy(oldIds, 0, newIds, 0, oldIds.length);
			newIds[oldIds.length] = "com.master.eclipsebukkit.BukkitNature";
			desc.setNatureIds(newIds);
			
			p.setDescription(desc, null);
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}
	
	private static InputStream getBukkit(int buildNr) throws MalformedURLException, IOException
	{
		if(buildNr == -1)
		{
			return new URL("http://ci.bukkit.org/job/dev-Bukkit/Recommended/artifact/target/bukkit-0.0.1-SNAPSHOT.jar").openStream();
		}
		else
		{
			return new URL("http://ci.bukkit.org/job/dev-Bukkit/"+buildNr+"/artifact/target/bukkit-0.0.1-SNAPSHOT.jar").openStream();
		}
	}
	
	private static InputStream getCraftBukkit(int buildNr) throws MalformedURLException, IOException
	{
		if(buildNr == -1)
		{
			return new URL("http://ci.bukkit.org/job/dev-CraftBukkit/Recommended/artifact/target/craftbukkit-0.0.1-SNAPSHOT.jar").openStream();
		}
		else
		{
			return new URL("http://ci.bukkit.org/job/dev-CraftBukkit/"+buildNr+"/artifact/target/craftbukkit-0.0.1-SNAPSHOT.jar").openStream();
		}
	}
	
	public static int[] getLastBuildNr()
	{
		int[] res = new int[] {-1,-1};
		
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(new URL("http://ci.bukkit.org/job/dev-craftbukkit/Recommended/").openStream()));
			
			String line;
			
			while((line = r.readLine()) != null)
			{
				if(line.startsWith("CraftBukkit: #"))
				{
					int i = line.indexOf("CraftBukkit: #") +14;
					res[0] = Integer.parseInt(line.substring(i, line.indexOf('<', i)));
				}
				if(line.startsWith("Bukkit: #"))
				{
					int i = line.indexOf("Bukkit: #") +9;
					res[1] = Integer.parseInt(line.substring(i, line.indexOf('<', i)));
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
}
