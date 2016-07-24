
package presentation.gui;

import presentation.control.LoginUIControl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginWindow extends Stage implements MessageableWindow {
	LoginUIControl control;
	final Text messageBar = new Text();
	TextField userTextField = new TextField();
	PasswordField pwBox = new PasswordField();
    public LoginWindow(LoginUIControl control) {
        this.control = control;
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("ID");
        grid.add(userName, 0, 1);

        
        //userTextField.setPrefColumnCount(10);
        //userTextField.setPrefWidth(30);
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password");
        grid.add(pw, 0, 2);
        //grid.setGridLinesVisible(true) ;

       
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        Button cancel = new Button("Exit Login");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BASELINE_CENTER);
        hbBtn.getChildren().add(btn);
        btn.setOnAction(control.getSubmitHandler(this));
        hbBtn.getChildren().add(cancel);
        cancel.setOnAction(control.getCancelHandler(this));
        grid.add(hbBtn, 1, 4);

        HBox messageBarBox = createMessageBarBox();
        grid.add(messageBarBox, 0, 6, 2, 1);

        //Scene scene = new Scene(grid, 300, 200);
        Scene scene = new Scene(grid);
        setScene(scene);
    }
    private HBox createMessageBarBox() {
		HBox box = new HBox(10);
		box.setAlignment(Pos.BASELINE_LEFT);
		box.getChildren().add(messageBar);
		return box;
		
	}

	@Override
	public Text getMessageBar() {
		return messageBar;
	}
	public String getId() {
		return userTextField.getText();
	}
	public String getPassword() {
		return pwBox.getText();
	}
}