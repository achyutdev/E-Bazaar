package presentation.control;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import presentation.data.BrowseSelectData;
import presentation.data.CartItemPres;
import presentation.gui.DefaultShoppingCartWindow;
import presentation.util.GuiUtils;

/**
 * This handler is needed both for ShoppingCartWindow and 
 * FinalOrderWindow. These windows are managed by different
 * controllers. Therefore, this handler has been placed in
 * the control package, but outside of both BrowseSelectUIControl
 * and CheckoutUIControl
 * 
 * @author pcorazza
 *
 */
public class DeleteCartItemHandler implements EventHandler<ActionEvent> {
	private DefaultShoppingCartWindow cartWindow;

	public DeleteCartItemHandler(DefaultShoppingCartWindow shopCartWindow) {
		cartWindow = shopCartWindow;
	}

	public void handle(ActionEvent evt) {
		TableView<CartItemPres> table = cartWindow.getTable();
		cartWindow.setTableAccessByRow();
		ObservableList<CartItemPres> tableItems = BrowseSelectData.INSTANCE
				.getCartData();
		ObservableList<CartItemPres> selectedItems = table.getSelectionModel()
				.getSelectedItems();
		ObservableList<Integer> selectedIndices = table.getSelectionModel()
				.getSelectedIndices();

		if (tableItems.isEmpty()) {
			cartWindow.displayError("Nothing to delete!");
		} else if (selectedIndices == null || selectedIndices.isEmpty()) {
			cartWindow.displayError("Please select a row.");
		} else {
			boolean result = BrowseSelectData.INSTANCE
					.removeFromCart(selectedItems);
			if (result) {
				table.setItems(BrowseSelectData.INSTANCE.getCartData());
				cartWindow.setTotalInCart(GuiUtils.formatPrice(GuiUtils
						.computeTotalInTable(table)));
				cartWindow.clearMessages();
			} else {
				cartWindow.displayInfo("Zero items removed from cart.");
			}
		}

	}
}
