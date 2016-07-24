package presentation.gui;

import presentation.control.ManageProductsUIControl;
import presentation.data.CatalogPres;
import presentation.data.DefaultData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import business.externalinterfaces.*;
import business.productsubsystem.*;

public class AddCatalogPopup extends Popup implements MessageableWindow {
	private TextField name;
	public TextField getNameField() {
		return name;
	}
	HBox sceneTitle;
	HBox topLevel;
	Text messageBar = new Text();
	private HBox setUpTopLabel() {
		Label label = new Label("Add Catalog");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private void setBorder() {
		final String cssDefault = "-fx-border-color: gray;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 3;\n";
                //+ "-fx-border-style: dashed;\n";
        topLevel.setStyle(cssDefault);
	}
	public AddCatalogPopup() {
		setX(50);
        setY(50);
        topLevel = new HBox(10);
        topLevel.setOpacity(1);
        setBackground(Color.KHAKI);
        setBorder();
        
		HBox sceneTitle = setUpTopLabel();
		Label idLabel = new Label("Catalog Id:");
		Label nameLabel = new Label("Catalog Name:");
		name = new TextField();
		HBox btnbox = setUpButtons();
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.add(sceneTitle, 0, 0, 2, 1);
		grid.add(nameLabel, 0, 1);
		grid.add(name, 1, 1);
		grid.add(messageBar, 0, 3, 2, 1);
		grid.add(btnbox, 0, 5, 2, 1);
		topLevel.getChildren().add(grid);
		getContent().addAll(topLevel);	
	}
	private HBox setUpButtons() {
		Button addButton = new Button("Add Catalog");
		Button cancelButton = new Button("Cancel");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(cancelButton);
		
		addButton.setOnAction(ManageProductsUIControl.INSTANCE.getAddNewCatalogHandler());
		cancelButton.setOnAction(evt -> {
			clearMessages();
			hide();
		});
		
		return btnBox;
	}
	void setBackground(Color color) {
		topLevel.backgroundProperty().set(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	@Override
	public Text getMessageBar() {
		return messageBar;
	}
}
