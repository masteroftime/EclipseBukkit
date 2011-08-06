package com.master.eclipsebukkit;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class BukkitProjectWizard extends Wizard implements INewWizard {
	
	private NewJavaProjectWizardPageOne jPage1;
	private NewJavaProjectWizardPageTwo jPage2;
	private NewBukkitProjectPage bPage;

	public BukkitProjectWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New Project");
		
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		
		IJavaProject project = jPage2.getJavaProject();
		if(project == null)
		{
				IRunnableWithProgress run = new IRunnableWithProgress() {
					
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						try {
							jPage2.performFinish(monitor);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				};
				
				try {
					getContainer().run(true, false, run);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		
		IRunnableWithProgress run = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				if(!bPage.doUseFile())
				{
					BukkitPluginSupport.createBukkitProject(jPage2.getJavaProject(), bPage.noBukkit()?-1:bPage.getBukkitVers(), bPage.noCraft()?bPage.getCraftVers()*-1:bPage.getCraftVers(), bPage.getMain(), monitor);
				}
				else
				{
					BukkitPluginSupport.createBukkitProject(jPage2.getJavaProject(), bPage.noBukkit()?null:bPage.getBukkitFile(), bPage.noCraft()?null:bPage.getCraftFile(), bPage.getMain(), monitor);
				}
			}
		};
		
		try {
			getContainer().run(true, true, run);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	@Override
	public boolean performCancel() {
		if(jPage2.getJavaProject() != null)
		{
			jPage2.performCancel();
		}
		return super.performCancel();
	}
	
	@Override
	public void addPages() {
		jPage1 = new NewJavaProjectWizardPageOne();
		jPage2 = new NewJavaProjectWizardPageTwo(jPage1);
		bPage = new NewBukkitProjectPage(jPage1);
		
		jPage1.setTitle("Create a Bukkit Plugin");
		jPage2.setTitle("Create a Bukkit Plugin");
		
		addPage(jPage1);
		addPage(bPage);
		addPage(jPage2);
	}
}
