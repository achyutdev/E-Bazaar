package presentation.gui;

import java.util.Arrays;
import java.util.List;

import presentation.control.CheckoutUIControl;
import presentation.data.CheckoutData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import business.externalinterfaces.*;

public class PaymentWindow extends Stage implements MessageableWindow {
	private TextField nameOnCardField = new TextField();
	private TextField cardNumberField  = new TextField();
	private ComboBox<String> cardTypeField = new ComboBox<String>();
	private TextField expirationField  = new TextField();
	Text messageBar = new Text();

	public CreditCard getCreditCardFromWindow() {
		return CheckoutData.INSTANCE.createCreditCard(nameOnCardField.getText(),
				expirationField.getText(), cardNumberField.getText(),
				cardTypeField.getValue());
	}
	public PaymentWindow() {
		this.nameOnCardField.setText(CheckoutUIControl.INSTANCE
				.getShippingBillingWindow().getBillName());
		setTitle("Payment");
		messageBar.setFill(Color.FIREBRICK);

		BorderPane topContainer = new BorderPane();

		//set up top label
		HBox labelHbox = setUpTopLabel();

		//set up credit card combo box
		setCreditCardTypes();

		loadDefaultData();

        //prepare grid
        List<? extends Node> displayValues
           = Arrays.asList(nameOnCardField, cardNumberField, cardTypeField, expirationField);
		FourByTwoGridPane dataTable
		   = new FourByTwoGridPane(CheckoutData.INSTANCE.getDisplayCredCardFields(),
				   displayValues, "gray", GuiConstants.PROD_DETAILS_GRID_WIDTH);

		//set up buttons
		HBox btnBox = setUpButtons();

		//set up central grid
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20);
		grid.setHgap(10);
		grid.add(dataTable, 0, 1);
		grid.add(messageBar, 0, 3);

		//set up outer BorderPane
		BorderPane.setMargin(labelHbox, new Insets(12,12,12,12));
		BorderPane.setMargin(btnBox, new Insets(12,12,12,12));
		topContainer.setTop(labelHbox);
		topContainer.setCenter(grid);
		topContainer.setBottom(btnBox);

		//set scene and stage
        Scene scene = new Scene(topContainer, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT-100);
		setScene(scene);
	}

	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Credit Card Information"));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private void setCreditCardTypes() {
		ObservableList<String> comboVals
		  = FXCollections.observableList(
			   CheckoutData.INSTANCE.getCredCardTypes());
        cardTypeField.setItems(comboVals);
        //set default value to display
        //cardTypeField.setValue(comboVals.get(0));
	}

	private void loadDefaultData() {
		List<String> defaultPaymentInfo = CheckoutUIControl.INSTANCE.getDefaultPaymentInfo();
		nameOnCardField.setText(defaultPaymentInfo.get(0));
        cardNumberField.setText(defaultPaymentInfo.get(1));
        cardTypeField.setValue(defaultPaymentInfo.get(2));
        expirationField.setText(defaultPaymentInfo.get(3));
	}
	private HBox setUpButtons() {
		Button proceedButton = new Button("Checkout");
		Button backButton = new Button("Previous Screen");
		Button backToCartButton = new Button("Back to Shopping Cart");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(proceedButton);
		btnBox.getChildren().add(backToCartButton);
		btnBox.getChildren().add(backButton);
		backButton.setOnAction(CheckoutUIControl.INSTANCE.getBackToShipBillWindow());
		backToCartButton.setOnAction(CheckoutUIControl.INSTANCE.getBackToCartHandler());
		proceedButton.setOnAction(CheckoutUIControl.INSTANCE.getProceedToTermsHandler());
		return btnBox;
	}
	@Override
	public Text getMessageBar() {
		return messageBar;
	}
}
