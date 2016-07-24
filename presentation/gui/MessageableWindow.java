package presentation.gui;

import javafx.scene.text.Text;

/**
 * Implementers of this interface support display of 
 * messages in a messageBar -- including error messages,
 * info messages, and also clearing of messages
 * 
 * @author pcorazza
 *
 */
public interface MessageableWindow {
	public Text getMessageBar();
	public default void displayError(String msg) {
		getMessageBar().setFill(GuiConstants.ERROR_MESSAGE_COLOR);
		getMessageBar().setText(msg);
	}
	public default void displayInfo(String msg) {
		getMessageBar().setFill(GuiConstants.INFO_MESSAGE_COLOR);
		getMessageBar().setText(msg);
	}
	public default void clearMessages() {
		getMessageBar().setText("");
	}
}
