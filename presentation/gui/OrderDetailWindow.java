package presentation.gui;

import presentation.control.ViewOrdersUIControl;
import presentation.data.OrderItemPres;
import presentation.data.OrderPres;
import presentation.data.ViewOrdersData;
import presentation.util.TableUtil;
import business.ordersubsystem.OrderImpl;
import business.ordersubsystem.OrderItemImpl;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrderDetailWindow extends Stage {
	private TableView<OrderItemPres> table = new TableView<OrderItemPres>();
	OrderPres selectedOrder;
	OrdersWindow orders;
	
	public void setData(ObservableList<OrderItemPres> orderItems) {
		table.setItems(orderItems);
	}
	@SuppressWarnings("unchecked")
	public OrderDetailWindow() {
		this.selectedOrder = ViewOrdersData.INSTANCE.getSelectedOrder();
		setTitle("Order Details");
		
		//set up top label
		HBox labelHbox = createTopLabel();
		
		//set up table
        TableColumn<OrderItemPres, String> productNameCol = 
			TableUtil.makeTableColumn(new OrderItemPres(), "Product Name",
				 "productNameProperty", 100);
        TableColumn<OrderItemPres, String> quantityCol = 
    		TableUtil.makeTableColumn(new OrderItemPres(), "Quantity",
    			"quantityProperty", 100);
        TableColumn<OrderItemPres, String> unitPriceCol = 
        		TableUtil.makeTableColumn(new OrderItemPres(), "Unit Price",
        			"unitPriceProperty", 100);
        TableColumn<OrderItemPres, String> totalPriceCol = 
        		TableUtil.makeTableColumn(new OrderItemPres(), "Total Price",
        			"totalPriceProperty", 100);
		table.getColumns().addAll(productNameCol, quantityCol, unitPriceCol, totalPriceCol);
		
		//set up row of buttons
		HBox btnBox = setUpButtons();
		
		//set up grid pane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		grid.add(labelHbox, 0, 1);
		grid.add(table, 0, 2);
		grid.add(btnBox,0,4);
		grid.add(new HBox(10), 0, 5);
		grid.setMinWidth(400);
		    
		//set in scene and stage
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
	}
	private HBox createTopLabel() {
		Label label = new Label("Order Details");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private HBox setUpButtons() {
		Button okButton = new Button("OK");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(okButton);
		okButton.setOnAction(
			ViewOrdersUIControl.INSTANCE.getOrderDetailsOkHandler());
		return btnBox;
	}
	
}
	
