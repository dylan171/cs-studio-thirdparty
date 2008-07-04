package org.csstudio.nams.configurator.views;

import org.csstudio.nams.configurator.beans.IConfigurationBean;
import org.csstudio.nams.configurator.composite.FilterableBeanList;
import org.csstudio.nams.configurator.service.AbstractConfigurationBeanServiceListener;
import org.csstudio.nams.configurator.service.ConfigurationBeanService;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

public abstract class AbstractNamsView extends ViewPart {

	protected static ConfigurationBeanService configurationBeanService;
	private FilterableBeanList filterableBeanList;

	public AbstractNamsView() {
		configurationBeanService.addConfigurationBeanServiceListener(new AbstractConfigurationBeanServiceListener() {
			//TODO updateView() is in most cases overkill
			@Override
			public void onBeanInsert(IConfigurationBean bean) {
				if (filterableBeanList != null) {
					filterableBeanList.updateView();
				}
			}
			@Override
			public void onBeanUpdate(IConfigurationBean bean) {
				if (filterableBeanList != null) {
					filterableBeanList.updateView();
				}
			}
			@Override
			public void onBeanDeleted(IConfigurationBean bean) {
				if (filterableBeanList != null) {
					filterableBeanList.updateView();
				}
			}
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		filterableBeanList = new FilterableBeanList(parent, SWT.None) {
			@Override
			protected IConfigurationBean[] getTableInput() {
				return getTableContent();
			}
		};
		MenuManager menuManager = new MenuManager();
		TableViewer table = filterableBeanList.getTable();
		Menu menu = menuManager.createContextMenu(table.getTable());
		table.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, table);
		getSite().setSelectionProvider(table);
		initDragAndDrop(filterableBeanList);
	}

	protected abstract void initDragAndDrop(FilterableBeanList filterableBeanList);

	@Override
	public void setFocus() {
		filterableBeanList.getTable().getTable().setFocus();
	}
	protected abstract IConfigurationBean[] getTableContent();

	public static void staticInject(ConfigurationBeanService beanService) {
		configurationBeanService = beanService;
	}
	
}
