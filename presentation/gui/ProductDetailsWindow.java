package presentation.gui;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import presentation.control.BrowseSelectUIControl;
import presentation.data.BrowseSelectData;
import presentation.data.ProductPres;

public class ProductDetailsWindow extends Stage {
	private ProductPres selectedProduct;
	
	public ProductDetailsWindow(ProductPres selectedProduct) {
		this.selectedProduct = selectedProduct;
		setTitle("Product Details");
		BorderPane topContainer = new BorderPane();
		
		//set up top label
		HBox labelHbox = setUpTopLabel();
		
		//prepare display grid
        List<String> displayValues = BrowseSelectData.INSTANCE.getProductDisplayValues(selectedProduct);
		FourByTwoGridPane dataTable 
		   = new FourByTwoGridPane(BrowseSelectData.INSTANCE.getProductFieldNamesForDisplay(),
				   displayValues, "gray", GuiConstants.PROD_DETAILS_GRID_WIDTH);
						        
		//set up button row
		HBox btnBox = setUpButtons();
		
		//set up grid pane (for center area of scene)
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20); 
		grid.setHgap(10);
		grid.add(dataTable, 0, 1);
		
		//place label, grid and buttons in the top container
		BorderPane.setMargin(labelHbox, new Insets(12,12,12,12));
		BorderPane.setMargin(btnBox, new Insets(12,12,12,12));
		topContainer.setTop(labelHbox);
		topContainer.setCenter(grid);
		topContainer.setBottom(btnBox);
		
		//set into scene and stage
        Scene scene = new Scene(topContainer, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT-80);  
		setScene(scene);
	}
	
	public HBox setUpTopLabel() {
		final Label label = new Label(
				String.format("%s: Product Details", selectedProduct.nameProperty().get()));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	
	private HBox setUpButtons() {
		Button addToCartButton = new Button("Add to Cart");
		Button backButton = new Button("Back to Product List");
		backButton.setOnAction(BrowseSelectUIControl.INSTANCE.getBackToProductListHandler());
		addToCartButton.setOnAction(BrowseSelectUIControl.INSTANCE.getAddToCartHandler());
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addToCartButton);
		btnBox.getChildren().add(backButton);
		return btnBox;
	}
	
}
