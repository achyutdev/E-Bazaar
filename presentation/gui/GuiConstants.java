package presentation.gui;

import java.util.Arrays;
import java.util.List;

import presentation.data.Synchronizer;
import javafx.scene.paint.Color;

public class GuiConstants {
	public static final int SCENE_WIDTH = 480;
	public static final int SCENE_HEIGHT = 400;
	public static final int GRID_PANE_WIDTH = 320;
	public static final int PROD_DETAILS_GRID_WIDTH = 400;
	
	//colors
	public static final Color ERROR_MESSAGE_COLOR = Color.FIREBRICK;
	public static final Color INFO_MESSAGE_COLOR = Color.DARKBLUE;
	
	//strings
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	//field names
	public static final List<String> DISPLAY_PRODUCT_FIELDS 
	   = Arrays.asList("Item Name", "Price", "Quantity Available", "Review");
	public static final List<String> DISPLAY_ADDRESS_FIELDS
	   = Arrays.asList("Name", "Street", "City", "State", "Zip");
	public static final List<String> DISPLAY_CREDIT_CARD_FIELDS
	   = Arrays.asList("Name", "Card Number", "Card Type", "Expiration Date");
	//Belongs in the database
	public static final List<String> CREDIT_CARD_TYPES
	   = Arrays.asList("Visa", "Master Card", "Discover");
	
	
	//text
	public static final String TERMS_MESSAGE 
	   = "Any Items purchased from this site adhere to the terms and "+
	     "conditions depicted in this document. You will need to accecpt "+
		 "the Terms and Conditions depicted here inorder to purchase " +
		 "anything from this site.";
	
	public static final String SUCCESS_MESSAGE 
	    = "Your order has been successfully submitted. Your products will arrive "
		   		+ "in 3-5 business days. Thank you for shopping at E-Bazaar!";
}
