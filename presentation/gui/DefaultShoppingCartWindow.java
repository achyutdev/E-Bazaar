package presentation.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import presentation.control.BrowseSelectUIControl;
import presentation.data.BrowseSelectData;
import presentation.data.CartItemPres;
import presentation.data.ErrorMessages;
import presentation.util.GuiUtils;
import presentation.util.TableUtil;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Product;

/**
 * This interface provides support for ShoppingCartWindow and
 * FinalOrderWindow. The screens contain exactly the same table,
 * so the table is set up for both of these screens using a default
 * interface method setUpTable().
 */
public interface DefaultShoppingCartWindow extends MessageableWindow, ModifiableTableWindow {
	
	//public GridPane setUpGridPane();
	public TableView<CartItemPres> getTable();
	public Text getTotal();
	public Label getTotalLabel();
	public TableColumn<CartItemPres, String> getQuantityCol();
	public void setQuantityCol(TableColumn<CartItemPres, String> quantCol);
	public HBox setUpButtons();
	public void setBtnBox(HBox box);
	public HBox getBtnBox();
	public String getTitleString();
	
	@SuppressWarnings("unchecked")
	default public void setUpTable() {
		getTable().setEditable(true);
        
        //create columns
        TableColumn<CartItemPres, String> itemNameCol 
        	= TableUtil.makeTableColumn(new CartItemPres(), "Item Name", "itemNameProperty", 200);
        
        //quantity column is trickier
        
	    setQuantityCol(TableUtil.makeEditableTableColumn(getTable(), new CartItemPres(), 
	    		"Quantity", "quantityProperty", 80));  
        getQuantityCol().setOnEditCommit(t -> {
		   CartItemPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String quantRequested = t.getNewValue();
		   boolean rulesOk = true;
		   try {
			   Product product 
			   	  = BrowseSelectData.INSTANCE.getProductForProductName(instance.getCartItem().getItemName());
			   BrowseSelectUIControl.INSTANCE.runQuantityRules(product, quantRequested);
		   } catch(RuleException e) {
			   getQuantityCol().getCellFactory().call(getQuantityCol()).cancelEdit();
			   rulesOk = false;
			   displayError(e.getMessage());
		   } catch(BusinessException e) {
			   getQuantityCol().getCellFactory().call(getQuantityCol()).cancelEdit();
			   rulesOk = false;
			   displayError(ErrorMessages.GENERAL_ERR_MSG + ": Message: " + e.getMessage());
		   }
		   if(rulesOk) {
			   //double price = Double.parseDouble(instance.priceProperty().get());
			   //int quant = Integer.parseInt(quantRequested);
			   //String newTotal = GuiUtils.formatPrice(quant * price);
			   clearMessages();
			   instance.setQuantity(new SimpleStringProperty(t.getNewValue()));
			   
			   double sumTotal = GuiUtils.computeTotalInTable(getTable());
			   setTotalInCart(GuiUtils.formatPrice(sumTotal));
		   }   
			   
		   TableUtil.refreshTable(getTable(), BrowseSelectData.INSTANCE.getShoppingCartSynchronizer());
		   
	     }); 

        TableColumn<CartItemPres, String> unitPriceCol 
	        = TableUtil.makeTableColumn(new CartItemPres(), "Unit Price", "priceProperty", 80);
        TableColumn<CartItemPres, String> totalPriceCol 
        	= TableUtil.makeTableColumn(new CartItemPres(), "Total Price", "totalPriceProperty", 80);

		getTable().getColumns().addAll(itemNameCol, getQuantityCol(), unitPriceCol, totalPriceCol);
		
		//make sure row selection is enabled after any mouse click
		getTable().setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            TableUtil.selectByRow(getTable());
	        }
	    });
	}
	
	default public HBox setUpTopLabel() {
		Label label = new Label(getTitleString());
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	
	default public Scene createScene() {
		
		//set up top label
		HBox labelHbox = setUpTopLabel();
		
		//set up table
		setUpTable();
        
		//set up buttons
		setBtnBox(setUpButtons());
		
		//assemble all in a GridPane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		
		grid.add(labelHbox, 0, 1);
		grid.add(getTable(), 0, 2);
		HBox totalBox = new HBox(10);
		totalBox.setAlignment(Pos.BOTTOM_RIGHT);
		totalBox.getChildren().add(getTotalLabel());
		totalBox.getChildren().add(getTotal());
		grid.add(totalBox, 0,3);
		grid.add(getMessageBar(), 0, 5);	
		grid.add(getBtnBox(),0,6);
		grid.add(new HBox(10), 0, 7);
		
		//set in scene
        Scene scene = new Scene(grid, 
        	GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		
		
		//Make sure that mouse click outside of table will also restore row selection
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	clearMessages();
	        	setTableAccessByRow();
	        }
	    });
		return scene;
	}
	
	//sets data from outside into shopping cart
	default public void setData(ObservableList<CartItemPres> items) {
		
		getTable().setItems(items);
		setTotalInCart(GuiUtils.formatPrice(GuiUtils.computeTotalInTable(getTable())));
		
		//turns on cell selection 
		TableView.TableViewSelectionModel<CartItemPres> selModel 
			= TableUtil.selectByCell(getTable());
		
		//selects the quantity cell so that it can be edited easily
		selModel.select(0, getQuantityCol());
	}
	
	
	
	default public void setTotalInCart(String amt) {
		getTotal().setText(amt);
	}
	
	
}
