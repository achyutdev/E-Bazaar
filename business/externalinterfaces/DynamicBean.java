package business.externalinterfaces;

import java.beans.PropertyChangeListener;

public interface DynamicBean {
	public void addPropertyChangeListener(PropertyChangeListener pcl);
	public void removePropertyChangeListener(PropertyChangeListener pcl);
}
