package com.master.eclipsebukkit;

import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

public class NewBukkitProjectPage extends WizardPage {
	private Text txtVers;
	private Text txtBukkitFile;
	//private Combo combo;
	//private Button btnNewButton;
	private Label lblNewestLabel;
	private Text txtCraftFile;
	private Button btnOpenBukkit;
	private Button btnOpenCraft;
	
	private int bukkitVers;
	private int craftVers;
	private boolean useFile;
	private boolean noBukkit;
	private boolean noCraft;
	private int specifiedVersion;
	private String bukkitFile;
	private String craftFile;
	private String main;
	
	private boolean canFlipPage;
	private Text txtMain;
	
	private NewJavaProjectWizardPageOne previous;
	private Button btnUseCraft;

	public int getBukkitVers() {
		if(specifiedVersion != -1)
		{
			return 0;
		}
		else return bukkitVers;
	}
	
	public int getCraftVers() {
		if(specifiedVersion != -1)
		{
			return specifiedVersion;
		}
		else return craftVers;
	}
	
	public String getBukkitFile() {
		return bukkitFile;
	}
	
	public String getCraftFile() {
		return craftFile;
	}
	
	public boolean doUseFile() {
		return useFile;
	}
	
	public boolean noBukkit() {
		return noBukkit;
	}
	
	public boolean noCraft() {
		return noCraft;
	}
	
	public String getMain() {
		return main;
	}

	protected NewBukkitProjectPage(NewJavaProjectWizardPageOne previous) {
		super("Bukkit Config Page");
		
		setTitle("Create a Bukkit Plugin");
		setDescription("Configure Bukkit specific options");
		
		canFlipPage = true;
		this.previous = previous;
		specifiedVersion = -1;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if(visible == true)
		{
			String packageName = previous.getProjectName().replaceAll(" ", "").toLowerCase();
			String mainClassName = "";
			for(String s : previous.getProjectName().split(" "))
			{
				if(s.length() >= 1)
					mainClassName += Character.toUpperCase(s.charAt(0))+s.substring(1);
			}
			
			txtMain.setText("myname."+packageName+"."+mainClassName);
		}
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		
		Group grpBukkitVersion = new Group(container, SWT.NONE);
		grpBukkitVersion.setText("Bukkit Version");
		grpBukkitVersion.setBounds(10, 10, 554, 192);
		
		Button btnNewest = new Button(grpBukkitVersion, SWT.RADIO);
		btnNewest.setSelection(true);
		btnNewest.setBounds(10, 20, 177, 16);
		btnNewest.setText("Newest Recommended Build:");
		
		lblNewestLabel = new Label(grpBukkitVersion, SWT.NONE);
		lblNewestLabel.setBounds(241, 21, 55, 15);
		lblNewestLabel.setText("Loading...");
		
		final Button btnSpecNr = new Button(grpBukkitVersion, SWT.RADIO);
		btnSpecNr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtVers.setEnabled(btnSpecNr.getSelection());
				if(btnSpecNr.getSelection())
				{
					if(txtVers.getText().length() != 0)
					{
						specifiedVersion = Integer.parseInt(txtVers.getText());
						canFlipPage = true;
					}
					else canFlipPage = false;
				}
				else specifiedVersion = -1;
			}
		});
		btnSpecNr.setBounds(10, 44, 177, 16);
		btnSpecNr.setText("Specify Build Number:");
		
		txtVers = new Text(grpBukkitVersion, SWT.BORDER);
		txtVers.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if(txtVers.getText().matches("[^0-9]"))
				{
					txtVers.setText(txtVers.getText().replaceAll("[^0-9]", ""));
				}
				else
				{
					if(txtVers.getText().length() != 0)
					{
						specifiedVersion = Integer.parseInt(txtVers.getText());
						canFlipPage = true;
					}
					else canFlipPage = false;
				}
			}
		});
		txtVers.setEnabled(false);
		txtVers.setBounds(241, 42, 55, 21);
		
		/*combo = new Combo(container, SWT.NONE);
		combo.setEnabled(false);
		combo.setBounds(160, 266, 370, 23);
		
		btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setEnabled(false);
		btnNewButton.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/obj16/add_obj.gif"));
		btnNewButton.setBounds(536, 264, 28, 25);*/
		
		/*
		Button btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				combo.setEnabled(!combo.getEnabled());
				btnNewButton.setEnabled(!btnNewButton.getEnabled());
			}
		});
		btnCheckButton.setBounds(20, 268, 134, 16);
		btnCheckButton.setText("Use Server for Testing");*/
		
		int[] version = BukkitPluginSupport.getLastBuildNr();
		lblNewestLabel.setText(""+version[0]);
		
		final Button btnNoBukkit = new Button(grpBukkitVersion, SWT.CHECK);
		btnNoBukkit.setBounds(10, 146, 163, 16);
		btnNoBukkit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				noBukkit = btnNoBukkit.getSelection();
				
				btnUseCraft.setEnabled(!noBukkit);
				
				if(noBukkit)
				{
					btnUseCraft.setSelection(true);
					noCraft = false;
				}
				
				if(useFile)
				{
					txtBukkitFile.setEnabled(!txtBukkitFile.getEnabled());
					btnOpenBukkit.setEnabled(!btnOpenBukkit.getEnabled());
					txtCraftFile.setEnabled(btnUseCraft.getSelection());
					btnOpenCraft.setEnabled(btnUseCraft.getSelection());
				}
			}
		});
		btnNoBukkit.setText("Bind directly to CraftBukkit");
		
		final Button btnSpecFile = new Button(grpBukkitVersion, SWT.RADIO);
		btnSpecFile.setBounds(10, 66, 100, 16);
		btnSpecFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!noBukkit) txtBukkitFile.setEnabled(btnSpecFile.getSelection());
				if(!noCraft)  txtCraftFile.setEnabled(btnSpecFile.getSelection());
				if(!noBukkit) btnOpenBukkit.setEnabled(btnSpecFile.getSelection());
				if(!noCraft)  btnOpenCraft.setEnabled(btnSpecFile.getSelection());
				
				useFile = btnSpecFile.getSelection();
				
				if(useFile)
				{
					if(txtBukkitFile.getText().length() == 0 || txtCraftFile.getText().length() == 0)
					{
						canFlipPage = false;
					}
					else canFlipPage = true;
				}
				else canFlipPage = true;
			}
		});
		btnSpecFile.setText("Specify Jar File:");
		
		txtBukkitFile = new Text(grpBukkitVersion, SWT.BORDER);
		txtBukkitFile.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				bukkitFile = txtBukkitFile.getText();
			}
		});
		txtBukkitFile.setBounds(149, 88, 360, 21);
		txtBukkitFile.setEnabled(false);
		txtBukkitFile.setText(System.getProperty("user.home")+"\\");
		
		txtCraftFile = new Text(grpBukkitVersion, SWT.BORDER);
		txtCraftFile.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				craftFile = txtCraftFile.getText();
			}
		});
		txtCraftFile.setEnabled(false);
		txtCraftFile.setBounds(149, 115, 360, 21);
		txtCraftFile.setText(System.getProperty("user.home")+"\\");
		
		Label lblBukkit = new Label(grpBukkitVersion, SWT.NONE);
		lblBukkit.setBounds(50, 91, 62, 15);
		lblBukkit.setText("Bukkit:");
		
		Label lblCraftbukkit = new Label(grpBukkitVersion, SWT.NONE);
		lblCraftbukkit.setBounds(50, 118, 62, 15);
		lblCraftbukkit.setText("CraftBukkit:");
		
		btnOpenBukkit = new Button(grpBukkitVersion, SWT.NONE);
		btnOpenBukkit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
				fileDialog.setFileName(txtBukkitFile.getText());
				String file = fileDialog.open();
				txtBukkitFile.setText(file);
			}
		});
		btnOpenBukkit.setEnabled(false);
		btnOpenBukkit.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/obj16/fldr_obj.gif"));
		btnOpenBukkit.setBounds(515, 86, 29, 25);
		
		btnOpenCraft = new Button(grpBukkitVersion, SWT.NONE);
		btnOpenCraft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN);
				fileDialog.setFileName(txtCraftFile.getText());
				String file = fileDialog.open();
				txtCraftFile.setText(file);
			}
		});
		btnOpenCraft.setEnabled(false);
		btnOpenCraft.setImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/obj16/fldr_obj.gif"));
		btnOpenCraft.setBounds(515, 113, 29, 25);
		
		btnUseCraft = new Button(grpBukkitVersion, SWT.CHECK);
		btnUseCraft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				noCraft = !btnUseCraft.getSelection();
				
				if(useFile)
				{
					txtCraftFile.setEnabled(!txtCraftFile.getEnabled());
					btnOpenCraft.setEnabled(!btnOpenCraft.getEnabled());
				}
			}
		});
		btnUseCraft.setSelection(true);
		btnUseCraft.setBounds(10, 168, 249, 16);
		btnUseCraft.setText("Download CraftBukkit (required for testing)");
		
		txtMain = new Text(container, SWT.BORDER);
		txtMain.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				main = txtMain.getText();
			}
		});
		txtMain.setBounds(96, 208, 468, 21);
		
		Label lblMainClass = new Label(container, SWT.NONE);
		lblMainClass.setBounds(20, 211, 60, 15);
		lblMainClass.setText("Main Class:");
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
	
	@Override
	public boolean canFlipToNextPage() {
		return canFlipPage;
	}
}
