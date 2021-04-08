package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import model.Group;

public class groupController extends Controller implements Initializable{
    @FXML
    private Label errorLabel, isTitle, isInvCodeLabel,isInviteCodeLabel,isAddEmailLabel;
    @FXML
    private TextField groupNameInput,isEmailTextField;
    @FXML
    private TextArea groupDescriptionInput;
    @FXML
    private Button isInviteButton;

    @FXML
    private void CreateGroup(ActionEvent event) {

        if(groupNameInput.getText().isEmpty()){
            errorLabel.setTextFill(Color.rgb(255,0,0));
            errorLabel.setText("Need to input a name");
        }
        else {

            String name = groupNameInput.getText();
            String desc = groupDescriptionInput.getText();

            //Check group name doesnt already exist

            if(checkGroupNameUnique(name) == true) {
                Group toAdd = new Group(name, desc);
                //Call to save to database
                errorLabel.setTextFill(Color.rgb(0, 255, 0));
                errorLabel.setText("Group Created");
                isInviteCodeLabel.setText(generateInvitecode());
                toggleInviteSection(true);

            }
            else{
                errorLabel.setTextFill(Color.rgb(255,0,0));
                errorLabel.setText("Group name already exists, please try a different name");
                groupNameInput.clear();
            }
        }
    }


    private boolean checkGroupNameUnique(String name){
        //If in db return false
        return true;
    }

    public void toggleInviteSection(boolean b){
        isAddEmailLabel.setVisible(b);
        isEmailTextField.setVisible(b);
        isInviteButton.setVisible(b);
        isInvCodeLabel.setVisible(b);
        isInviteCodeLabel.setVisible(b);
        isTitle.setVisible(b);
    }

    public String generateInvitecode(){
        return "X09X08";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleInviteSection(false);
    }
}
