package presentation.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.control.BrowseSelectUIControl;
import presentation.control.CheckoutUIControl;
import presentation.control.DeleteCartItemHandler;
import presentation.data.CartItemPres;


public class ShoppingCartWindow extends Stage implements DefaultShoppingCartWindow {
	private final static String TITLE_STRING = "Shopping Cart";

	//DefaultShoppingCartWindow implementations
	public TableView<CartItemPres> getTable() {
		return table;
	}
	public Text getTotal() {
		return total;
	}
	public Label getTotalLabel() {
		return totalLabel;
	}
	//like "Shopping Cart"
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


	//Singleton
	public final static ShoppingCartWindow INSTANCE = new ShoppingCartWindow();
	private TableView<CartItemPres> table = new TableView<CartItemPres>();

	private Text messageBar = new Text();
	private HBox btnBox;

	private Stage primaryStage;//used when retrieved cart needs to navigate back
	public void setPrimaryStage(Stage stage) {
		this.primaryStage = stage;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	//special location to put total for the shopping cart
	private Text total = new Text();
	private Label totalLabel = new Label("Total:");

	//editable column
	private TableColumn<CartItemPres, String> quantityCol;

	public ShoppingCartWindow() {
		setTitle(TITLE_STRING);
		setScene(createScene());
	}

	public ObservableList<CartItemPres> getCartItems() {
		return table.getItems();
	}

	public HBox setUpButtons() {
		Button proceedButton = new Button("Proceed to Checkout");
		Button continueButton = new Button("Continue Shopping");
		Button saveButton = new Button("Save Cart");
		Button deleteButton = new Button("Delete Selected");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(proceedButton);
		btnBox.getChildren().add(continueButton);
		btnBox.getChildren().add(saveButton);
		btnBox.getChildren().add(deleteButton);
		//Shop cart deletions are managed by the GUI
		deleteButton.setOnAction(new DeleteCartItemHandler(this));
		continueButton.setOnAction(BrowseSelectUIControl.INSTANCE.getCartContinueHandler());
		saveButton.setOnAction(BrowseSelectUIControl.INSTANCE.getSaveCartHandler());
		proceedButton.setOnAction(CheckoutUIControl.INSTANCE.getProceedFromCartToShipBill());
		return btnBox;
	}



}
