package presentation.gui;

import java.util.function.Consumer;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.StageStyle;
import presentation.data.CheckoutData;
import presentation.data.CustomerPres;
import presentation.data.Synchronizer;
import presentation.util.TableUtil;

public class SelectAddress extends Stage implements MessageableWindow {
	ShippingBillingWindow shipBillWindow;
	TableView<CustomerPres> table = new TableView<CustomerPres>();
	String labelStr;
	boolean isShipping;
	/* This keeps data in this table in synch with the data model */
	Synchronizer synch;
	private Text messageBar = new Text();

	public void setData(ObservableList<CustomerPres> addrData) {
		table.setItems(addrData);
	}
	@SuppressWarnings("unchecked")
	public SelectAddress(ShippingBillingWindow w, CheckoutData.ShipBill shipBill,
			Consumer<CustomerPres> addressSetter) {
		System.out.println("called here selectshipadressform.....................................");
		initStyle(StageStyle.UTILITY);
		shipBillWindow = w;
		isShipping = shipBill.isShipping;
		this.labelStr = shipBill.label;
		synch = shipBill.synch;

		Label label = new Label(String.format(labelStr));
        label.setFont(new Font("Arial", 13));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);

        table = new TableView<CustomerPres>();
        table.setEditable(true);

        //create columns
        TableColumn<CustomerPres, String> nameCol
        	= TableUtil.makeTableColumn(new CustomerPres(), "Name", "fullNameProperty", 100);

        TableColumn<CustomerPres, String> streetCol
    	    = TableUtil.makeEditableTableColumn(table, new CustomerPres(), "Street", "streetProperty", 100);

        streetCol.setOnEditCommit(t -> {
 		   CustomerPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
 		   String newStreet = t.getNewValue();
 		   instance.setStreet(new SimpleStringProperty(newStreet));
 		   TableUtil.refreshTable(table, synch);
 	     });

        TableColumn<CustomerPres, String> cityCol
	       = TableUtil.makeEditableTableColumn(table, new CustomerPres(), "City", "cityProperty", 100);

        cityCol.setOnEditCommit(t -> {
		   CustomerPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String newCity = t.getNewValue();
		   instance.setCity(new SimpleStringProperty(newCity));
		   TableUtil.refreshTable(table, synch);
	     });

        TableColumn<CustomerPres, String> stateCol
	       = TableUtil.makeEditableTableColumn(table, new CustomerPres(), "State", "stateProperty", 100);

        stateCol.setOnEditCommit(t -> {
		   CustomerPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String newState = t.getNewValue();
		   instance.setState(new SimpleStringProperty(newState));
		   TableUtil.refreshTable(table, synch);
	     });

        TableColumn<CustomerPres, String> zipCol
	       = TableUtil.makeEditableTableColumn(table, new CustomerPres(), "Zipcode", "zipProperty", 100);

        zipCol.setOnEditCommit(t -> {
		   CustomerPres instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String newzip = t.getNewValue();
		   instance.setZip(new SimpleStringProperty(newzip));
		   TableUtil.refreshTable(table, synch);
	     });
        table.getColumns().addAll(nameCol, streetCol, cityCol, stateCol, zipCol);

      //add buttons
  		Button selectButton = new Button("Select");
  		Button deleteButton = new Button("Delete Selected");
  		Button closeButton = new Button("Close Window");

  		//assemble all in a GridPane
  		GridPane grid = new GridPane();
  		grid.setMinWidth(500);
  		grid.setAlignment(Pos.CENTER);
  		grid.setVgap(10);
  		grid.setHgap(10);
  		grid.add(labelHbox, 0, 1);
  		grid.add(table, 0, 2);
  		grid.add(messageBar, 0, 4);
  		HBox btnBox = new HBox(10);
  		btnBox.setAlignment(Pos.CENTER);
  		btnBox.getChildren().add(selectButton);
  		btnBox.getChildren().add(deleteButton);
  		btnBox.getChildren().add(closeButton);
  		grid.add(btnBox,0,6);
  		grid.add(new HBox(10), 0, 7);


  		deleteButton.setOnAction(evt -> {
  			TableUtil.selectByRow(table);
  			ObservableList<CustomerPres> tableItems =
  					isShipping ? CheckoutData.INSTANCE.getCustomerShipAddresses() :
  						CheckoutData.INSTANCE.getCustomerBillAddresses();
		    ObservableList<CustomerPres> selectedItems = table.getSelectionModel().getSelectedItems();
		    ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();


		    if(tableItems.isEmpty()) {
		    	displayError("Nothing to delete!");
		    } else if (selectedIndices == null || selectedIndices.isEmpty()) {
		    	displayError("Please select a row.");
		    } else {
		    	
		    	
		    	boolean result = tableItems.remove(selectedItems.get(0));
			    if(result) {
			    	table.setItems(tableItems);
			    	clearMessages();
			    } else {
			    	displayInfo("Zero addresses deleted.");
			    }
		    }
  		});
  		closeButton.setOnAction(evt -> hide());
  		selectButton.setOnAction(evt -> {
			CustomerPres c = table.getSelectionModel().getSelectedItem();
			if(c == null) {
				messageBar.setText("Please select a row");
			} else {
				addressSetter.accept(c);
				hide();
			}
  		});

  		Scene scene = new Scene(grid,650, 300);
  		setScene(scene);
	}
	public Text getMessageBar() {
		return messageBar;
	}
}
