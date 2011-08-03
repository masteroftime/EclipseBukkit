package com.master.eclipsebukkit;

import javax.swing.ImageIcon;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.ResourceManager;

public class ServerView extends ViewPart {

	public ServerView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Tree tree = new Tree(parent, SWT.BORDER);
		
		TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem.setImage(ResourceManager.getPluginImage("com.master.eclipsebukkit", "icons/favicon.ico"));
		trtmNewTreeitem.setText("New TreeItem");
		
		TreeItem trtmNewTreeitem_1 = new TreeItem(trtmNewTreeitem, SWT.NONE);
		trtmNewTreeitem_1.setImage(ResourceManager.getPluginImage("org.eclipse.php.server.ui", "/icons/full/obj16/server.gif"));
		trtmNewTreeitem_1.setText("New TreeItem");
		trtmNewTreeitem.setExpanded(true);
		
		TreeItem trtmNewTreeitem_2 = new TreeItem(tree, SWT.NONE);
		trtmNewTreeitem_2.setText("New TreeItem");
		
		TreeItem trtmNewTreeitem_4 = new TreeItem(trtmNewTreeitem_2, SWT.NONE);
		trtmNewTreeitem_4.setText("New TreeItem");
		
		TreeItem trtmNewTreeitem_3 = new TreeItem(trtmNewTreeitem_2, SWT.NONE);
		trtmNewTreeitem_3.setText("New TreeItem");
		
		TreeItem trtmNewTreeitem_5 = new TreeItem(trtmNewTreeitem_3, SWT.NONE);
		trtmNewTreeitem_5.setText("New TreeItem");
		trtmNewTreeitem_3.setExpanded(true);
		trtmNewTreeitem_2.setExpanded(true);
		
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
