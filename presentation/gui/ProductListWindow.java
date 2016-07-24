package presentation.gui;

import presentation.control.BrowseSelectUIControl;
import presentation.data.CatalogPres;
import presentation.data.ProductPres;
import presentation.util.TableUtil;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProductListWindow extends Stage implements MessageableWindow {
	private TableView<ProductPres> table = new TableView<ProductPres>();
	Stage primaryStage;
	CatalogPres selectedCatalog;
	ProductPres selectedProduct;
	Text messageBar = new Text();
	public Text getMessageBar() {
		return messageBar;
	}
	public TableView<ProductPres> getTable() {
		return table;
	}
	
	public void setData(ObservableList<ProductPres> prods) {
		table.setItems(prods);
	}
	private HBox createTopLabel() {
		Label label = new Label(String.format("Available %s", selectedCatalog.nameProperty().get()));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private HBox setUpButtons() {
		Button viewButton = new Button("Select Product");
		Button backButton = new Button("Go Back to Catalogs");
		backButton.setOnAction(BrowseSelectUIControl.INSTANCE.getBackToCatalogListHandler());
		viewButton.setOnAction(BrowseSelectUIControl.INSTANCE.getViewProductDetailsHandler());
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(viewButton);
		btnBox.getChildren().add(backButton);
		return btnBox;
	}
	@SuppressWarnings("unchecked")
	public ProductListWindow(CatalogPres catalog) {
		this.selectedCatalog = catalog;
		setTitle("Product List");
		messageBar.setFill(Color.FIREBRICK);
		
		//set up top label
		HBox labelHbox = createTopLabel();
		
		//set up table
        TableColumn<ProductPres, String> productNameCol = 
			TableUtil.makeTableColumn(new ProductPres(), 
				String.format(catalog.nameProperty().get()), 
				   "nameProperty", GuiConstants.GRID_PANE_WIDTH);
		table.getColumns().addAll(productNameCol);
		
		//set up row of buttons
		HBox btnBox = setUpButtons();
		
		//set up grid pane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		grid.add(labelHbox, 0, 1);
		grid.add(table, 0, 2);
		grid.add(messageBar, 0, 3);
		grid.add(btnBox,0,5);
		grid.add(new HBox(10), 0, 6);
		    
		//set in scene and stage
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
	}
	
}
