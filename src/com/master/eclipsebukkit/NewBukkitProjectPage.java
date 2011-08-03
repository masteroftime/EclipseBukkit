package com.master.eclipsebukkit;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.text.java.SWTTemplateCompletionProposalComputer;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.ResourceManager;

public class NewBukkitProjectPage extends WizardPage {
	private Text txtVers;
	private Text txtFile;
	private Combo combo;
	private Button btnNewButton;
	
	private int bukkitVers;
	private int craftVers;
	private Label lblNewestLabel;

	public int getBukkitVers() {
		return bukkitVers;
	}
	
	public int getCraftVers() {
		return craftVers;
	}

	protected NewBukkitProjectPage() {
		super("Bukkit Config Page");
		
		setTitle("Create a Bukkit Plugin");
		setDescription("Configure Bukkit specific options");
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		
		Group grpBukkitVersion = new Group(container, SWT.NONE);
		grpBukkitVersion.setText("Bukkit Version");
		grpBukkitVersion.setBounds(10, 10, 554, 153);
		
		Button btnNewest = new Button(grpBukkitVersion, SWT.RADIO);
		btnNewest.setSelection(true);
		btnNewest.setBounds(10, 20, 177, 16);
		btnNewest.setText("Newest Recommended Build:");
		
		lblNewestLabel = new Label(grpBukkitVersion, SWT.NONE);
		lblNewestLabel.setBounds(241, 21, 55, 15);
		lblNewestLabel.setText("Loading...");
		
		Button btnSpecNr = new Button(grpBukkitVersion, SWT.RADIO);
		btnSpecNr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtVers.setEnabled(!txtVers.isEnabled());
			}
		});
		btnSpecNr.setBounds(10, 44, 177, 16);
		btnSpecNr.setText("Specify Build Number:");
		
		Button btnSpecFile = new Button(grpBukkitVersion, SWT.RADIO);
		btnSpecFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtFile.setEnabled(!txtFile.isEnabled());
			}
		});
		btnSpecFile.setBounds(10, 92, 177, 16);
		btnSpecFile.setText("Specify Jar File");
		
		txtVers = new Text(grpBukkitVersion, SWT.BORDER);
		txtVers.setEnabled(false);
		txtVers.setBounds(241, 42, 303, 21);
		
		txtFile = new Text(grpBukkitVersion, SWT.BORDER);
		txtFile.setEnabled(false);
		txtFile.setBounds(241, 90, 303, 21);
		
		Button btnServer = new Button(grpBukkitVersion, SWT.RADIO);
		btnServer.setBounds(10, 68, 177, 16);
		btnServer.setText("User Server Version:");
		
		Label lblNewLabel_1 = new Label(grpBukkitVersion, SWT.NONE);
		lblNewLabel_1.setBounds(241, 69, 55, 15);
		lblNewLabel_1.setText("No Server");
		
		Button btnCheckButton_1 = new Button(grpBukkitVersion, SWT.CHECK);
		btnCheckButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton_1.setBounds(10, 127, 102, 16);
		btnCheckButton_1.setText("Use CraftBukkit");
		
		combo = new Combo(container, SWT.NONE);
		combo.setEnabled(false);
		combo.setBounds(182, 179, 348, 23);
		
		btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setEnabled(false);
		btnNewButton.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/obj16/add_obj.gif"));
		btnNewButton.setBounds(536, 177, 28, 25);
		
		Button btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo.setEnabled(!combo.getEnabled());
				btnNewButton.setEnabled(!btnNewButton.getEnabled());
			}
		});
		btnCheckButton.setBounds(20, 181, 135, 16);
		btnCheckButton.setText("Use Server for Testing");
		
		int[] version = BukkitPluginSupport.getLastBuildNr();
		lblNewestLabel.setText(""+version[0]);
		bukkitVers = version[1];
		craftVers = version[0];
		
		/*
		IRunnableWithProgress run = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				int newest = BukkitPluginSupport.getLastRecommendedBuild();
				lblNewestLabel.setText(""+newest);
				if(buildNr == -1) buildNr = newest;
			}
		};
		
		try {
			getContainer().run(true, false, run);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
}
