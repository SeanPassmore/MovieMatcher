package Movie_Matcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class Controller {
     @FXML
     private TextArea tags;
     @FXML private TextField tagSearch = new TextField();
     @FXML
     protected void test(ActionEvent event){
         System.out.println("You clicked me!");
         tags.setText("hey fag");
    }
}
