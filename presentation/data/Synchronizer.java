package presentation.data;

import javafx.collections.ObservableList;

public interface Synchronizer {
	@SuppressWarnings("rawtypes")
	public void refresh(ObservableList list);
}
