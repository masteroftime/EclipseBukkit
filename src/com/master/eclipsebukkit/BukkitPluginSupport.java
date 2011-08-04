package com.master.eclipsebukkit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class BukkitPluginSupport 
{
	public static void createBukkitProject(IJavaProject project, int bukkitVers, int craftVers, String main, IProgressMonitor monitor)
	{
		try {
			if(bukkitVers == -1 && craftVers > 0)
			{
				int bukkit = getMatchingBukkitNr(craftVers);
				if(bukkit != -1)
				{
					createBukkitProject(project, bukkit, craftVers, main, monitor);
				}
				else createBukkitProject(project, -2, craftVers, main, monitor);
			}
			else
			{
				createBukkitProject(project, bukkitVers == -2?null:getBukkit(bukkitVers), getCraftBukkit(craftVers), main, monitor);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createBukkitProject(IJavaProject project, String bukkitPath, String craftPath, String main, IProgressMonitor monitor)
	{
		try {
			createBukkitProject(project, bukkitPath == null?null:new FileInputStream(bukkitPath), new FileInputStream(craftPath), main, monitor);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void createBukkitProject(IJavaProject project, InputStream bukkit, InputStream craftBukkit, String main, IProgressMonitor monitor)
	{
		try {
			Bundle bundle = Platform.getBundle("com.master.eclipsebukkit");
			project.getProject().getFile("src/plugin.yml").create(FileLocator.openStream(bundle, new Path("template.yml"), false), false, monitor);
			//project.getProject().getFolder("srv").create(IResource.HIDDEN, true, monitor);
			project.getProject().getFile("bin/craftbukkit.jar").create(craftBukkit, false, monitor);
			
			String p = main.substring(0, main.lastIndexOf("."));
			String mainClass = main.substring(main.lastIndexOf(".")+1);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(FileLocator.openStream(bundle, new Path("mainClass.java"), false)));
			String line;
			String classContent = "";
			while((line = reader.readLine()) != null)
			{
				line = line.replace("###packagename###", p);
				line = line.replace("###classname###", mainClass);
				classContent += line+"\n";
			}
			
			IPackageFragmentRoot packageRoot = (IPackageFragmentRoot) JavaCore.create(project.getProject().getFolder("src"));
			IPackageFragment pack = packageRoot.createPackageFragment(p, false, monitor);
			ICompilationUnit mainUnit = pack.createCompilationUnit(mainClass+".java", classContent, false, monitor);
			
			if(bukkit != null)
			{
				project.getProject().getFile("bukkit.jar").create(bukkit, false, monitor);
				
				IClasspathEntry[] oldEntries = project.getRawClasspath();
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length+1];
				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
				newEntries[oldEntries.length] = JavaCore.newLibraryEntry(project.getPath().append("bukkit.jar"), null, null);
				project.setRawClasspath(newEntries, monitor);
			}
			else
			{
				IClasspathEntry[] oldEntries = project.getRawClasspath();
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length+1];
				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
				newEntries[oldEntries.length] = JavaCore.newLibraryEntry(project.getPath().append("bin/craftbukkit.jar"), null, null);
				project.setRawClasspath(newEntries, monitor);
			}
			
			addBukkitNature(project.getProject());
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
	
	public static int getMatchingBukkitNr(int buildNr)
	{
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(new URL("http://ci.bukkit.org/job/dev-craftbukkit/"+buildNr).openStream()));
			
			String line;
			
			while((line = r.readLine()) != null)
			{
				if(line.startsWith("Bukkit: #"))
				{
					int i = line.indexOf("Bukkit: #") +9;
					return Integer.parseInt(line.substring(i, line.indexOf('<', i)));
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
}
