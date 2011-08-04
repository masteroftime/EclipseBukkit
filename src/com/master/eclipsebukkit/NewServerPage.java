package com.master.eclipsebukkit;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Spinner;

public class NewServerPage extends WizardPage implements IWizardPage
{
	private Text text_1;
	private Text txtFtp;
	private Text text_3;
	private Text text_4;
	public NewServerPage()
	{
		super("New Bukkit Server Page");
		
		setTitle("Create a new Bukkit Server");
		setDescription("");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Group group_1 = new Group(container, SWT.NONE);
		group_1.setBounds(10, 10, 554, 54);
		
		Label label = new Label(group_1, SWT.NONE);
		label.setText("Path:");
		label.setBounds(10, 25, 42, 15);
		
		text_1 = new Text(group_1, SWT.BORDER);
		text_1.setBounds(108, 22, 402, 21);
		
		Button button_1 = new Button(group_1, SWT.NONE);
		button_1.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/obj16/fldr_obj.gif"));
		button_1.setBounds(516, 20, 28, 25);
		
		Button btnAddAnExisting = new Button(group_1, SWT.RADIO);
		btnAddAnExisting.setSelection(true);
		btnAddAnExisting.setBounds(10, 0, 115, 16);
		btnAddAnExisting.setText("Add a local Server");
		
		Group group_2 = new Group(container, SWT.NONE);
		group_2.setBounds(10, 70, 554, 102);
		
		Button btnConnectToRemote = new Button(group_2, SWT.RADIO);
		btnConnectToRemote.setBounds(10, 0, 187, 16);
		btnConnectToRemote.setText("Connect to remote Server (FTP)");
		
		Label lblAddress = new Label(group_2, SWT.NONE);
		lblAddress.setBounds(10, 25, 55, 15);
		lblAddress.setText("Address:");
		
		txtFtp = new Text(group_2, SWT.BORDER);
		txtFtp.setText("ftp://");
		txtFtp.setBounds(109, 22, 435, 21);
		
		text_3 = new Text(group_2, SWT.BORDER);
		text_3.setBounds(109, 49, 173, 21);
		
		text_4 = new Text(group_2, SWT.BORDER);
		text_4.setBounds(371, 49, 173, 21);
		
		Label lblUsername = new Label(group_2, SWT.NONE);
		lblUsername.setBounds(10, 52, 55, 15);
		lblUsername.setText("Username:");
		
		Label lblPassword = new Label(group_2, SWT.NONE);
		lblPassword.setBounds(302, 52, 55, 15);
		lblPassword.setText("Password:");
		
		Button btnSavePassword = new Button(group_2, SWT.CHECK);
		btnSavePassword.setBounds(10, 78, 98, 16);
		btnSavePassword.setText("Save Password");
	}
}
