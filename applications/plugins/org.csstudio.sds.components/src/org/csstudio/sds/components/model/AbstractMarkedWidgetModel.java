package org.csstudio.sds.components.model;

import org.csstudio.sds.model.WidgetPropertyCategory;
import org.csstudio.sds.model.properties.BooleanProperty;
import org.csstudio.sds.model.properties.DoubleProperty;


/**
 * This class defines a common widget model for any widget 
 * which has one scale and standard markers. 
 * Standard markers are comprised of LOLO, LO, HI, HIHI. 
 * @author Xihui Chen
 */
public abstract class AbstractMarkedWidgetModel extends AbstractScaledWidgetModel {
	
	/** The ID of the show minor ticks property. */
	public static final String PROP_SHOW_MARKERS = "showMarkers"; //$NON-NLS-1$	

	/** The ID of the lolo level property.*/
	public static final String PROP_LOLO_LEVEL = "loloLevel"; //$NON-NLS-1$
	
	/** The ID of the lo level property. */
	public static final String PROP_LO_LEVEL = "loLevel"; //$NON-NLS-1$
	
	/** The ID of the hi level property. */
	public static final String PROP_HI_LEVEL = "hiLevel"; //$NON-NLS-1$
	
	/** The ID of the hihi level property. */
	public static final String PROP_HIHI_LEVEL = "hihiLevel"; //$NON-NLS-1$		
	
	
	/** The ID of the show lolo property.*/
	public static final String PROP_SHOW_LOLO = "showLOLO"; //$NON-NLS-1$
	
	/** The ID of the show lo property. */
	public static final String PROP_SHOW_LO = "showLO"; //$NON-NLS-1$
	
	/** The ID of the show hi property. */
	public static final String PROP_SHOW_HI = "showHI"; //$NON-NLS-1$
	
	/** The ID of the show hihi property. */
	public static final String PROP_SHOW_HIHI = "showHIHI"; //$NON-NLS-1$		
	
	/** The ID of the lolo color property.*/
	public static final String PROP_LOLO_COLOR = "loloColor"; //$NON-NLS-1$
	
	/** The ID of the lo color property. */
	public static final String PROP_LO_COLOR = "loColor"; //$NON-NLS-1$
	
	/** The ID of the hi color property. */
	public static final String PROP_HI_COLOR = "hiColor"; //$NON-NLS-1$
	
	/** The ID of the hihi color property. */
	public static final String PROP_HIHI_COLOR = "hihiColor"; //$NON-NLS-1$		

	
	/** The default value of the levels property. */
	private static final double[] DEFAULT_LEVELS = new double[]{10, 20, 80, 90};	
	
	/** The default color of the lolo color property. */
	private static final String DEFAULT_LOLO_COLOR = "#ff0000";
	/** The default color of the lo color property. */
	private static final String DEFAULT_LO_COLOR = "#ffff00";
	/** The default color of the hi color property. */
	private static final String DEFAULT_HI_COLOR = "#ffff00";
	/** The default color of the hihi color property. */
	private static final String DEFAULT_HIHI_COLOR = "#ff0000";
	

	@Override
	protected void configureProperties() {	
		
		super.configureProperties();
		addProperty(PROP_SHOW_MARKERS, new BooleanProperty("Show Markers", 
				WidgetPropertyCategory.Display, true));			
		
		addProperty(PROP_LOLO_LEVEL, new DoubleProperty("Level LOLO", 
				WidgetPropertyCategory.Behaviour,DEFAULT_LEVELS[0]));
		addProperty(PROP_LO_LEVEL, new DoubleProperty("Level LO", 
				WidgetPropertyCategory.Behaviour,DEFAULT_LEVELS[1]));
		addProperty(PROP_HI_LEVEL, new DoubleProperty("Level HI", 
				WidgetPropertyCategory.Behaviour,DEFAULT_LEVELS[2]));
		addProperty(PROP_HIHI_LEVEL, new DoubleProperty("Level HIHI", 
				WidgetPropertyCategory.Behaviour,DEFAULT_LEVELS[3]));
		
		addProperty(PROP_SHOW_LOLO, new BooleanProperty("Show LOLO", 
				WidgetPropertyCategory.Display, true));		
		addProperty(PROP_SHOW_LO, new BooleanProperty("Show LO", 
				WidgetPropertyCategory.Display, true));	
		addProperty(PROP_SHOW_HI, new BooleanProperty("Show HI", 
				WidgetPropertyCategory.Display, true));	
		addProperty(PROP_SHOW_HIHI, new BooleanProperty("Show HIHI", 
				WidgetPropertyCategory.Display, true));
		
		addColorProperty(PROP_LOLO_COLOR, "Color LOLO ",
				WidgetPropertyCategory.Display, DEFAULT_LOLO_COLOR);
				addColorProperty(PROP_LO_COLOR, "Color LO",
				WidgetPropertyCategory.Display, DEFAULT_LO_COLOR);
				addColorProperty(PROP_HI_COLOR, "Color HI",
				WidgetPropertyCategory.Display, DEFAULT_HI_COLOR);
				addColorProperty(PROP_HIHI_COLOR, "Color HIHI",
				WidgetPropertyCategory.Display, DEFAULT_HIHI_COLOR);		
		
	}
	
	/**
	 * Gets the lolo level for this model.
	 * @return double
	 * 				The lolo level
	 */
	public double getLoloLevel() {
		return getDoubleProperty(PROP_LOLO_LEVEL).getPropertyValue();
	}
	
	/**
	 * Gets the lo level for this model.
	 * @return double
	 * 				The lo level
	 */
	public double getLoLevel() {
		return getDoubleProperty(PROP_LO_LEVEL).getPropertyValue();
	}
	
	/**
	 * Gets the hi level for this model.
	 * @return double
	 * 				The hi level
	 */
	public double getHiLevel() {
		return getDoubleProperty(PROP_HI_LEVEL).getPropertyValue();
	}
	
	/**
	 * Gets the hihi level of this model.
	 * @return double
	 * 				The hihi level 
	 */
	public double getHihiLevel() {
		return getDoubleProperty(PROP_HIHI_LEVEL).getPropertyValue();
	}
	
	/**
	 * @return true if the minor ticks should be shown, false otherwise
	 */
	public boolean isShowMarkers() {
		return getBooleanProperty(PROP_SHOW_MARKERS).getPropertyValue();
	}

	/**
	 * @return true if the lolo marker should be shown, false otherwise
	 */
	public boolean isShowLolo() {
		return getBooleanProperty(PROP_SHOW_LOLO).getPropertyValue();
	}
	
	/**
	 * @return true if the lo marker should be shown, false otherwise
	 */
	public boolean isShowLo() {
		return getBooleanProperty(PROP_SHOW_LO).getPropertyValue();
	}
	
	/**
	 * @return true if the hi marker should be shown, false otherwise
	 */
	public boolean isShowHi() {
		return getBooleanProperty(PROP_SHOW_HI).getPropertyValue();
	}
	
	/**
	 * @return true if the hihi marker should be shown, false otherwise
	 */
	public boolean isShowHihi() {
		return getBooleanProperty(PROP_SHOW_HIHI).getPropertyValue();
	}
	
	
}
