package presentation.gui;

import presentation.control.CheckoutUIControl;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class OrderCompleteWindow extends Stage {
	
	public OrderCompleteWindow() {
		setTitle("Thank You");
		BorderPane topContainer = new BorderPane();
		
		HBox labelHbox = setUpTopLabel();

		//set up text area in a grid pane, in the center area
		TextArea ta = new TextArea(GuiConstants.SUCCESS_MESSAGE);
		ta.setWrapText(true);
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.add(ta, 0, 1);
		
		
		//set up buttons
		HBox btnBox = setUpButtons();
		
		//set up outer BorderPane
		BorderPane.setMargin(labelHbox, new Insets(12,12,12,12));
		BorderPane.setMargin(btnBox, new Insets(12,12,12,12));
		topContainer.setTop(labelHbox);
		topContainer.setCenter(grid);
		topContainer.setBottom(btnBox);
		
		//set scene and stage
		Scene scene = new Scene(topContainer, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT-200);  
		setScene(scene);
	}
	private HBox setUpTopLabel() {
		 Label label = new Label("Successful Submission!");
	     label.setFont(new Font("Arial", 16));
	     HBox labelHbox = new HBox(10);
	     labelHbox.setAlignment(Pos.CENTER);
	     labelHbox.getChildren().add(label);
	     return labelHbox;
	}
	private HBox setUpButtons() {
		Button continueButton = new Button("Continue Shopping");
		Button exitButton = new Button("Exit E-Bazaar");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);		
		btnBox.getChildren().add(continueButton);
		btnBox.getChildren().add(exitButton);
		   
		continueButton.setOnAction(
			CheckoutUIControl.INSTANCE.getContinueFromOrderCompleteHandler());
			
		exitButton.setOnAction(evt -> {
			Platform.exit();
		});
		return btnBox;
	}
}
