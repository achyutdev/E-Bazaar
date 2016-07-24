package presentation.gui;

import java.util.List;

import business.exceptions.BackendException;
import presentation.control.BrowseSelectUIControl;
import presentation.data.BrowseSelectData;
import presentation.data.CatalogPres;
import presentation.util.TableUtil;
import javafx.collections.FXCollections;
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

public class CatalogListWindow extends Stage implements MessageableWindow, ModifiableTableWindow {
	//These lines of code make CatalogListWindow a singleton -- singleton implementation
	//is not thread-safe, but doesn't need to be.
	private CatalogListWindow(Stage pStage, ObservableList<CatalogPres> list) {
		table = new TableView<CatalogPres>();
		setStage(pStage);
		setData(list);

	}
	private CatalogListWindow(ObservableList<CatalogPres> list) {
		table = new TableView<CatalogPres>();
		setStage(null);
		setData(list);

	}
	private static CatalogListWindow instance = null;
	public static CatalogListWindow getInstance(Stage primary, ObservableList<CatalogPres> list) {
		if(instance == null) {
			instance = new CatalogListWindow(primary, list);
		}
		return instance;
	}
	//useful if window is known to be populated
	public static CatalogListWindow getInstance() {
		if(instance == null) {
			try {
				instance= new CatalogListWindow(FXCollections.observableList(BrowseSelectData.INSTANCE.getCatalogList()));
				instance.setData(FXCollections.observableList(BrowseSelectData.INSTANCE.getCatalogList()));
			} catch (BackendException e) {
				instance= new CatalogListWindow();
				instance.displayError("Database is unavailable. Please try again later.");
			}
		}
		return instance;
	}

	private TableView<CatalogPres> table;
	public TableView<CatalogPres> getTable() {
		return table;
	}
	private Text messageBar = new Text();
	@Override
	public Text getMessageBar() {
		return messageBar;
	}

	/**
	 * Data is set into table using this method.
	 */
	public void setData(ObservableList<CatalogPres> cats) {
		table.setItems(cats);
	}

	private CatalogListWindow() {
		//empty because of the technique used for making it a singleton
		//the only necessary parameter is passed in by setStage
	}
	@SuppressWarnings("unchecked")
	public void setStage(Stage ps) {
		messageBar.setFill(Color.FIREBRICK);
		setTitle("Catalog List");

		//create label
		HBox browseCatalogsLabel = createTopLabel();

		//set up table
		TableColumn<CatalogPres, String> catalogNameCol =
			TableUtil.makeTableColumn(
				new CatalogPres(), "Catalog", "nameProperty", GuiConstants.GRID_PANE_WIDTH);

		table.getColumns().addAll(catalogNameCol);

		//set up button box at bottom
		HBox btnBox = setUpButtons();

		//set up main grid and add label and table
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.add(browseCatalogsLabel, 0, 1);
		grid.add(table, 0, 2);
		grid.add(messageBar, 0, 3);
		grid.add(btnBox,0,5);
		grid.add(new HBox(10), 0, 6);

        //set in scene and this stage
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);
		setScene(scene);
	}
	private HBox createTopLabel() {
		final Label label = new Label("Browse Catalogs");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private HBox setUpButtons() {
		Button viewButton = new Button("View Catalog");
		Button cartButton = new Button("View Shopping Cart");
		Button backButton = new Button("Back to Start");
		backButton.setOnAction(BrowseSelectUIControl.INSTANCE.getBackToPrimaryHandler());
		viewButton.setOnAction(BrowseSelectUIControl.INSTANCE.getViewProductsHandler());
		cartButton.setOnAction(BrowseSelectUIControl.INSTANCE.getCatalogsToCartHandler());
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(viewButton);
		btnBox.getChildren().add(cartButton);
		btnBox.getChildren().add(backButton);
        return btnBox;
	}



}
