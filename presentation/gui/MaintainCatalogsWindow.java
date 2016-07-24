package presentation.gui;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import business.usecasecontrol.ManageProductsController;
import presentation.control.ManageProductsUIControl;
import presentation.data.CatalogPres;
import presentation.data.ManageProductsData;
import presentation.data.ProductPres;
import presentation.util.TableUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class MaintainCatalogsWindow extends Stage implements MessageableWindow {
	private Stage primaryStage;
	private TableView<CatalogPres> table = new TableView<>();
	public TableView<CatalogPres> getTable() {
		return table;
	}
	
	private Text messageBar = new Text();
	private ComboBox<String> catalogCombo = new ComboBox<String>();
	
	//editable column
	private TableColumn<CatalogPres, String> idCol;
	private TableColumn<CatalogPres, String> nameCol;

	
	public MaintainCatalogsWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;
		setTitle("Maintain Catalogs");
		
		//set up top label
		HBox labelHbox = setUpTopLabel();
		
		//set up table
		setUpTable();
        
		//set up buttons
		HBox btnBox = setUpButtons();
		
		//assemble all in a GridPane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		
		grid.add(labelHbox, 0, 1);
		grid.add(table, 0, 2);
		grid.add(messageBar, 0, 3);	
		grid.add(btnBox,0,4);
		grid.add(new HBox(10), 0, 5);
		
		//set in scene
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
		
		//Make sure that mouse click outside of table will also restore row selection
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	messageBar.setText("");
	            TableUtil.selectByRow(table);
	        }
	    });
	}
	
	
	public void setData(ObservableList<CatalogPres> items) {		
		table.setItems(items);
	}
	
	
	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Maintain Catalogs"));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
		
	@SuppressWarnings("unchecked")
	private void setUpTable() {
		table.setEditable(true);       
        idCol 
	       = TableUtil.makeEditableTableColumn(table, new CatalogPres(), "ID", "idProperty", 100);  
        idCol.setVisible(false);
        idCol.setOnEditCommit(t -> {
           CatalogPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   instance.setId(new SimpleStringProperty(t.getNewValue()));	
		  
		   TableUtil.refreshTable(table, ManageProductsData.INSTANCE.getManageCatalogsSynchronizer());		   
	     }); 
        
        nameCol= TableUtil.makeEditableTableColumn(table, new CatalogPres(), "Name", "nameProperty", 360);  
        nameCol.setOnEditCommit(t -> {
        	CatalogPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   instance.setName(new SimpleStringProperty(t.getNewValue()));		
		   //edit in DB
		   try {
			ManageProductsController.INSTANCE.updateCatalog(instance);
			} catch (Exception e) {
				messageBar.setText("Unable to edit Catalog");
			}
		   TableUtil.refreshTable(table, ManageProductsData.INSTANCE.getManageCatalogsSynchronizer());		   
	     }); 
    	
		table.getColumns().add(nameCol);
		
		//make sure row selection is enabled after any mouse click
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            TableUtil.selectByRow(table);
	        }
	    });
	}
	
	private HBox setUpButtons() {
		Button addButton = new Button("Add New Catalog");
		Button deleteButton = new Button("Delete Catalog");
		Button backButton = new Button("Back to Main");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(deleteButton);
		btnBox.getChildren().add(backButton);
		
		deleteButton.setOnAction(ManageProductsUIControl.INSTANCE.getDeleteCatalogHandler());
		backButton.setOnAction(ManageProductsUIControl.INSTANCE.getBackButtonHandler());			
		addButton.setOnAction(ManageProductsUIControl.INSTANCE.getAddCatalogHandler());
	
		return btnBox;
	}


	@Override
	public Text getMessageBar() {
		return messageBar;
	}
}
