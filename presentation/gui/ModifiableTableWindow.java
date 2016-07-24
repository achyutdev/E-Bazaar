package presentation.gui;

import javafx.scene.control.TableView;
import presentation.util.TableUtil;

/**
 * Implementers of this interface support callback
 * calls to their embedded tableview, permitting
 * toggling between selection by row and selection by cell.
 * Typically used by handlers in a Control class to
 * begin or end handling of a button click.
 * 
 * @author pcorazza
 */
public interface ModifiableTableWindow {
	@SuppressWarnings("rawtypes")
	public TableView getTable();
	@SuppressWarnings("unchecked")
	default public void setTableAccessByRow() {
		TableUtil.selectByRow(getTable());
	}
	@SuppressWarnings("unchecked")
	default public void setTableAccessByCell() {
		TableUtil.selectByCell(getTable());
	}
}
