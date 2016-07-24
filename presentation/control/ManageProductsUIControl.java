package presentation.control;

import presentation.data.CatalogPres;
import presentation.data.DefaultData;
import presentation.data.ManageProductsData;
import presentation.data.ProductPres;
import presentation.gui.AddCatalogPopup;
import presentation.gui.AddProductPopup;
import presentation.gui.MaintainCatalogsWindow;
import presentation.gui.MaintainProductsWindow;
import presentation.util.TableUtil;
import business.exceptions.BackendException;
import business.exceptions.UnauthorizedException;
import business.externalinterfaces.Catalog;
import business.productsubsystem.ProductSubsystemFacade;
import business.usecasecontrol.ManageProductsController;
import business.util.DataUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import middleware.exceptions.DatabaseException;

import java.util.logging.*;


public enum ManageProductsUIControl {
	INSTANCE;
	private static final Logger LOG 
		= Logger.getLogger(ManageProductsUIControl.class.getSimpleName());
	private Stage primaryStage;
	private Callback startScreenCallback;

	public void setPrimaryStage(Stage ps, Callback returnMessage) {
		primaryStage = ps;
		startScreenCallback = returnMessage;
	}

	// windows managed by this class
	MaintainCatalogsWindow maintainCatalogsWindow;
	MaintainProductsWindow maintainProductsWindow;
	AddCatalogPopup addCatalogPopup;
	AddProductPopup addProductPopup;

	// Manage catalogs
	private class MaintainCatalogsHandler implements EventHandler<ActionEvent>,Callback {
		@Override
		public void handle(ActionEvent e) {
			//LOG.warning("Login is not being checked in MaintainCatalogsHandler.");
			boolean isLoggedIn = DataUtil.isLoggedIn();
			if (!isLoggedIn) {
				LoginUIControl loginControl = new LoginUIControl(maintainCatalogsWindow,
						primaryStage, this);
				loginControl.startLogin();
			} else {
				doUpdate();
			}

		}

		@Override
		public Text getMessageBar() {
			return startScreenCallback.getMessageBar();
		}

		@Override
		public void doUpdate() {
			maintainCatalogsWindow = new MaintainCatalogsWindow(primaryStage);
			ObservableList<CatalogPres> list = ManageProductsData.INSTANCE.getCatalogList();
			maintainCatalogsWindow.setData(list);
			primaryStage.hide();
			try {
				Authorization.checkAuthorization(maintainCatalogsWindow, DataUtil.custIsAdmin());
				//show this screen if user is authorized
				maintainCatalogsWindow.show();
			} catch(UnauthorizedException ex) {   
            	startScreenCallback.displayError(ex.getMessage());
            	maintainCatalogsWindow.hide();
            	primaryStage.show();   
            	
            }
			
		}
	}
	
	public MaintainCatalogsHandler getMaintainCatalogsHandler() {
		return new MaintainCatalogsHandler();
	}
	
	private class MaintainProductsHandler implements EventHandler<ActionEvent>,Callback {
		@Override
		public void handle(ActionEvent e) {
			boolean isLoggedIn = DataUtil.isLoggedIn();
			if (!isLoggedIn) {
				LoginUIControl loginControl = new LoginUIControl(maintainProductsWindow,
						primaryStage, this);
				loginControl.startLogin();
			} else {
				doUpdate();
			}
	      
		}

		@Override
		public Text getMessageBar() {
			return startScreenCallback.getMessageBar();
		}

		@Override
		public void doUpdate() {
			try {
				maintainProductsWindow=new MaintainProductsWindow(primaryStage);
				Authorization.checkAuthorization(maintainProductsWindow, DataUtil.custIsAdmin());
				maintainProductsWindow = new MaintainProductsWindow(primaryStage);
				CatalogPres selectedCatalog = ManageProductsData.INSTANCE.getSelectedCatalog();
				if(selectedCatalog != null) {
					ObservableList<ProductPres> list = ManageProductsData.INSTANCE.getProductsList(selectedCatalog);
					maintainProductsWindow.setData(ManageProductsData.INSTANCE.getCatalogList(), list);
				}
				maintainProductsWindow.show();  
		        primaryStage.hide();
			} catch (UnauthorizedException e1) {
				startScreenCallback.displayError(e1.getMessage());
				maintainProductsWindow.hide(); 
				primaryStage.show(); 
			}
			
		}
	}
	public MaintainProductsHandler getMaintainProductsHandler() {
		return new MaintainProductsHandler();
	}
	
	private class BackButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {		
			maintainCatalogsWindow.clearMessages();		
			maintainCatalogsWindow.hide();
			startScreenCallback.clearMessages();
			primaryStage.show();
		}
			
	}
	public BackButtonHandler getBackButtonHandler() {
		return new BackButtonHandler();
	}
	
	private class BackFromProdsButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {		
			maintainProductsWindow.clearMessages();		
			maintainProductsWindow.hide();
			startScreenCallback.clearMessages();
			primaryStage.show();
		}			
	}
	
	public BackFromProdsButtonHandler getBackFromProdsButtonHandler() {
		return new BackFromProdsButtonHandler();
	}
	
	//////new
	/* Handles the request to delete selected row in catalogs table */
	private class DeleteCatalogHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			TableView<CatalogPres> table = maintainCatalogsWindow.getTable();
			ObservableList<CatalogPres> tableItems = table.getItems();
		    ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();
		    ObservableList<CatalogPres> selectedItems = table.getSelectionModel()
					.getSelectedItems();
		    if(tableItems.isEmpty()) {
		    	maintainCatalogsWindow.displayError("Nothing to delete!");
		    } else if (selectedIndices == null || selectedIndices.isEmpty()) {
		    	maintainCatalogsWindow.displayError("Please select a row.");
		    } else {
		    	boolean result = false;
				try {
					ManageProductsController.INSTANCE.deleteCatalog(selectedItems);
					result=true;
				} catch (BackendException e) {
					maintainCatalogsWindow.displayError(
				    		"Selected catalog could not be deleted from the database!");  
					result=false;
				}
			    if(result) {
			    	table.setItems(ManageProductsData.INSTANCE.getCatalogList());	
			    } else {
			    	maintainCatalogsWindow.displayInfo("No items deleted.");
			    }
		    }
		}			
	}
	
	public DeleteCatalogHandler getDeleteCatalogHandler() {
		return new DeleteCatalogHandler();
	}
	
	/* Produces an AddCatalog popup in which user can add new catalog data */
	private class AddCatalogHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			addCatalogPopup = new AddCatalogPopup();
			addCatalogPopup.show(maintainCatalogsWindow);	
		}
	}
	public AddCatalogHandler getAddCatalogHandler() {
		return new AddCatalogHandler();
	} 
	
	/* Invoked by AddCatalogPopup - reads user input for a new catalog to be added to db */
	private class AddNewCatalogHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			//validate input
			TextField nameField = addCatalogPopup.getNameField();
			String catName = nameField.getText();
			if(catName.equals("")) 
				addCatalogPopup.displayError("Name field must be nonempty!");
			else {
				try {
					int newCatId = ManageProductsController.INSTANCE.saveNewCatalog(catName);
					CatalogPres catPres = ManageProductsData.INSTANCE.catalogPresFromData(newCatId, catName);
					ManageProductsData.INSTANCE.addToCatalogList(catPres);
					maintainCatalogsWindow.setData(ManageProductsData.INSTANCE.getCatalogList());
					TableUtil.refreshTable(maintainCatalogsWindow.getTable(), 
							ManageProductsData.INSTANCE.getManageCatalogsSynchronizer());
					addCatalogPopup.clearMessages();
					addCatalogPopup.hide();
				} catch(BackendException e) {
					addCatalogPopup.displayError("A database error has occurred. Check logs and try again later.");
				}
			}	
		}
		
	}
	public AddNewCatalogHandler getAddNewCatalogHandler() {
		return new AddNewCatalogHandler();
	} 

	
//////new
	
	
	/* Handles the request to delete selected row in catalogs table */
	private class DeleteProductHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			CatalogPres selectedCatalog = ManageProductsData.INSTANCE.getSelectedCatalog();
		    ObservableList<ProductPres> tableItems = ManageProductsData.INSTANCE.getProductsList(selectedCatalog);
			TableView<ProductPres> table = maintainProductsWindow.getTable();
			ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();
		    ObservableList<ProductPres> selectedItems = table.getSelectionModel().getSelectedItems();
		    if(tableItems.isEmpty()) {
		    	maintainProductsWindow.displayError("Nothing to delete!");
		    } else if (selectedIndices == null || selectedIndices.isEmpty()) {
		    	maintainProductsWindow.displayError("Please select a row.");
		    } else {
		    	boolean result=false;
				try {
					ManageProductsController.INSTANCE.deleteProduct(selectedCatalog,selectedItems);
					ManageProductsData.INSTANCE.removeFromProductList(selectedCatalog, selectedItems);
					result=true;
				} catch (BackendException e) {
					result=false;
					maintainProductsWindow.displayError(
				    		"Product cannot to be deleted from db!");
				}
			    if(result) {
			    	table.setItems(ManageProductsData.INSTANCE.getProductsList(selectedCatalog));
			    } else {
			    	maintainProductsWindow.displayInfo("No items deleted.");
			    }
				
		    }			
		}			
	}
	
	public DeleteProductHandler getDeleteProductHandler() {
		return new DeleteProductHandler();
	}
	
	/* Produces an AddProduct popup in which user can add new product data */
	private class AddProductHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			addProductPopup = new AddProductPopup();
			String catNameSelected 
			   = ManageProductsData.INSTANCE.getSelectedCatalog().getCatalog().getName();
			addProductPopup.setCatalog(catNameSelected);
			addProductPopup.show(maintainProductsWindow);
		}
	}
	public AddProductHandler getAddProductHandler() {
		return new AddProductHandler();
	} 
	
	/* Invoked by AddCatalogPopup - reads user input for a new catalog to be added to db */
	private class AddNewProductHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent evt) {
			
			TextField nameField = addProductPopup.getName();
			TextField manufactureField = addProductPopup.getManufactureDate();
			TextField numAvailField = addProductPopup.getNumAvail();
			TextField unitPriceField = addProductPopup.getUnitPrice();
			TextField descriptionField = addProductPopup.getDescription();
			
			//validate input (better to implement in rules engine
			if(nameField.getText().trim().equals("")) 
				addProductPopup.displayError("Product Name field must be nonempty!");
			else if(manufactureField.getText().trim().equals("")) 
				addProductPopup.displayError("Manufacture Date field must be nonempty!");
			else if(numAvailField.getText().trim().equals("")) 
				addProductPopup.displayError("Number in Stock field must be nonempty!");
			else if(unitPriceField.getText().trim().equals("")) 
				addProductPopup.displayError("Unit Price field must be nonempty!");
			else if(descriptionField.getText().trim().equals("")) 
				addProductPopup.displayError("Description field must be nonempty!");
			else {
				//code this as in AddNewCatalogHandler (above)
				//addProductPopup.displayInfo("You need to implement this!");
				//need to implement
			
						ProductPres prod = ManageProductsData.INSTANCE.productPresFromDataWithDesc(ManageProductsData.INSTANCE.getSelectedCatalog().getCatalog()
								, nameField.getText(),manufactureField.getText(), Integer.parseInt(numAvailField.getText()), Double.parseDouble(unitPriceField.getText()),descriptionField.getText());
						
						CatalogPres catPres=ManageProductsData.INSTANCE.getSelectedCatalog();
						//ManageProductsData.INSTANCE.addToProdList(catPres, prod);
						try {
							ManageProductsController.INSTANCE.saveProduct(prod.getProduct());
							ManageProductsData.INSTANCE.addToProdList(catPres, prod);
						} catch (BackendException e) {
							addProductPopup.displayError("Cannot add product.");
						}
						maintainProductsWindow.setData(ManageProductsData.INSTANCE.getProductsList(catPres));
						
						//maintainCatalogsWindow.setData(ManageProductsData.INSTANCE.getCatalogList());
						TableUtil.refreshTable(maintainProductsWindow.getTable(), 
								ManageProductsData.INSTANCE.getManageProductsSynchronizer());
						addProductPopup.clearMessages();
						addProductPopup.hide();
					
						addProductPopup.displayError("A database error has occurred. Check logs and try again later.");
				
				}	
			}	
	}
	
	public AddNewProductHandler getAddNewProductHandler() {
		return new AddNewProductHandler();
	} 

}
