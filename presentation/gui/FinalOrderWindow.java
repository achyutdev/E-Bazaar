package presentation.gui;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

import presentation.control.CheckoutUIControl;
import presentation.data.CartItemData;
import presentation.data.CartItemPres;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FinalOrderWindow extends Stage implements
		DefaultShoppingCartWindow {
	private final static String TITLE_STRING = "Final Order";

	// DefaultShoppingCartWindow implementations
	public TableView<CartItemPres> getTable() {
		return table;
	}

	public Text getTotal() {
		return total;
	}

	public Label getTotalLabel() {
		return totalLabel;
	}

	public String getTitleString() {
		return TITLE_STRING;
	}

	public void setBtnBox(HBox btnBox) {
		this.btnBox = btnBox;
	}

	@Override
	public Text getMessageBar() {
		return messageBar;
	}

	@Override
	public TableColumn<CartItemPres, String> getQuantityCol() {
		return quantityCol;
	}

	@Override
	public void setQuantityCol(TableColumn<CartItemPres, String> quantCol) {
		quantityCol = quantCol;

	}

	@Override
	public HBox getBtnBox() {
		return btnBox;
	}

	private TableView<CartItemPres> table = new TableView<>();
	private Text messageBar = new Text();
	private HBox btnBox;
	// special location to put total for the shopping cart
	private Text total = new Text();
	private Label totalLabel = new Label("Total:");

	// editable column
	private TableColumn<CartItemPres, String> quantityCol;
	
	public FinalOrderWindow() {
		setTitle(TITLE_STRING);
		setScene(createScene());
	}
	public ObservableList<CartItemPres> getCartItems() {
		return table.getItems();
	}

	public HBox setUpButtons() {
		Button submitButton = new Button("Submit Order");
		Button cancelButton = new Button("Cancel");
		Button shopCartButton = new Button("Back to Shopping Cart");

		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(submitButton);
		btnBox.getChildren().add(cancelButton);
		btnBox.getChildren().add(shopCartButton);

		submitButton.setOnAction(CheckoutUIControl.INSTANCE.getSubmitHandler());
		cancelButton.setOnAction(CheckoutUIControl.INSTANCE.getCancelOrderHandler());
		shopCartButton.setOnAction(
			CheckoutUIControl.INSTANCE.getToShoppingCartFromFinalOrderHandler());
		return btnBox;
	}
}
