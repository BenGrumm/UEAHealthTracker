package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Group;
import model.GroupDBHelper;
import model.User;
import model.UserDBHelper;
import sample.GUI;

public class groupController extends Controller implements Initializable{

    GroupDBHelper GDBH = new GroupDBHelper();
    UserDBHelper UDBH = new UserDBHelper();
    int currentUserID = User.getLoggedIn().getID();
    Group currentGroup;
    Group[] usersGroups;
    String usersCurrentUI;


    //Group creation form
    @FXML
    private Label errorLabel, isTitle, isInvCodeLabel,isInviteCodeLabel,isAddEmailLabel,isErrorLabel;
    @FXML
    private TextField groupNameInput,isEmailTextField,groupNameInputDash;
    @FXML
    private TextArea groupDescriptionInput;
    @FXML
    private Button isInviteButton;

    //Group Homepage
    @FXML
    private Label groupNameLabel,groupMemberCountLabel, groupDescLabel,groupRoleLabel,groupGoal1Label,groupGoal2Label,groupGoal3Label;
    @FXML
    private Button groupJoinButton,groupCreateButton,manageGroupButton,inviteMethodButton,leaveGroupButton;
    @FXML
    private ComboBox usersGroupsComboBox;
    @FXML
    private TextField invCodeTextBox;



    @FXML
    /**
     * Method used on create button press. Used to check, validate and save new groups to database.
     */
    private void CreateGroup(ActionEvent event) {
        if(groupNameInput.getText().isEmpty()){
            errorLabel.setTextFill(Color.rgb(255,0,0));
            errorLabel.setText("Need to input a name");
        }
        else {

            String name = groupNameInput.getText();
            String desc = groupDescriptionInput.getText();
            int size = 1;
            String invCode = generateInvitecode();

            //Check group name doesnt already exist
            if(!GDBH.doesGroupNameExist(name)) {
                //Group toAdd = new Group(name, desc,generateInvitecode());
                //Call to save to database
                while(GDBH.doesGroupInvCodeExist(invCode)){
                    invCode = generateInvitecode();
                }
                GDBH.addGroup(name,desc,1,invCode,currentUserID);
                errorLabel.setTextFill(Color.rgb(0, 255, 0));
                errorLabel.setText("Group Created");
                isInviteCodeLabel.setText(invCode);
                toggleInviteSection(true);
            }
            else{
                errorLabel.setTextFill(Color.rgb(255,0,0));
                errorLabel.setText("Group name already exists, please try a different name");
                groupNameInput.clear();
            }
        }
        //SetUpGroupHomepage();
    }

    /**
     * Method used to show or hide the invite section UI to the user for the group they just created.
     * @param b True, to show, false to hide
     */
    public void toggleInviteSection(boolean b){
        isAddEmailLabel.setVisible(b);
        isEmailTextField.setVisible(b);
        isInviteButton.setVisible(b);
        isInvCodeLabel.setVisible(b);
        isInviteCodeLabel.setVisible(b);
        isTitle.setVisible(b);
    }

    /**
     * Method used to generate an invite code
     * @return String of 6 characters.
     */
    public String generateInvitecode(){
        Random rand = new Random();
        String code = "";

        for(int i=0; i<6; i++){
            int randomNum = rand.nextInt(90-65) +65;
            char letter = (char)randomNum;
            code = code + letter;
        }
        return code;
    }

    /**
     * Method used when invite members button is pressed. Checks email is registered and if it is, sends invite with
     * the code to the user.
     */
    public void InviteMember(){
        String email = isEmailTextField.getText();
        if(!UDBH.checkValidEmail(email)){

            //ADD EMAIL PART

            isErrorLabel.setTextFill(Color.rgb(0,255,0));
            isErrorLabel.setText("Invite Sent!");
        }
        else{
            isErrorLabel.setTextFill(Color.rgb(255,0,0));
            isErrorLabel.setText("Email is not associated with a registered account.");
        }
    }

    public void SelectDifferentGroup(ActionEvent event){
        for(int x= 0; x< usersGroups.length; x++){
            if(usersGroupsComboBox.getValue().equals(usersGroups[x].getName())) {
                changeGroup(usersGroups[x]);
            }
        }
    }

    /**
     * Method used to get a list of groups a member is apart of.
     */
    public void SetUpGroupHomepage(){
        ArrayList<Integer> usersGroupIDs = new ArrayList<Integer>();

        usersGroupIDs = GDBH.getUsersGroupIDs(currentUserID);

        usersGroups = new Group[usersGroupIDs.size()];

        for(int x= 0; x< usersGroupIDs.size(); x++){
            //Get object of groups
            System.out.println(usersGroupIDs.get(x));
            System.out.println("Users group IDs:" + usersGroupIDs.get(x));
            if(GDBH.getGroup(usersGroupIDs.get(x)) == null){
                System.out.println("ERROR IN GETTING GROUP");
            }
            else{
                usersGroups[x] = GDBH.getGroup(usersGroupIDs.get(x));
            }
        }

        String[] groupNames = new String[usersGroups.length];

        for(int x= 0; x< usersGroups.length; x++){
            groupNames[x] = usersGroups[x].getName();
            System.out.println(groupNames[x]);
        }

        //Set ComboBox
        usersGroupsComboBox.getItems().clear();
        usersGroupsComboBox.getItems().addAll(groupNames);

        if(!(usersGroups.length == 0)) {
            changeGroup(usersGroups[0]);
        }
        else{
            System.out.println("Error / change scene");
        }
    }

    /**
     * Method used to change all relevent labels to show a different groups information
     */
    public void changeGroup(Group newGroup){

        groupNameLabel.setText(newGroup.getName());
        groupDescLabel.setText(newGroup.getDescription());
        System.out.println("Members: " + newGroup.getSize());
        groupMemberCountLabel.setText("Members: " + newGroup.getSize());
        groupRoleLabel.setText("Role: " + GDBH.getMembersRole(newGroup.getiD(),currentUserID));
        currentGroup = newGroup;
    }

    /**
     * Method used to let a member join the group. Saves them to the the database with MEMBER role
     */
    public void JoinGroup(){
        String invCode = invCodeTextBox.getText();
        int groupID= GDBH.GetGroupID(invCode) ;
        if(GDBH.doesMemberAlreadyExistInGroup(groupID,currentUserID)){
            System.out.println("Already in group");
        }
        else {
            GDBH.AddMember(groupID, currentUserID);
            SetUpGroupHomepage();
        }
    }

    /**
     * Method used to let a member leave a group. Although wont let owners leave
     */
    public void LeaveGroup(){
        currentGroup.getiD();
        String role = GDBH.getMembersRole(currentGroup.getiD(),currentUserID);
        if(role.equals("OWNER")){
            System.out.println("OWNER - CANT LEAVE GROUP");
        }
        else{
            GDBH.LeaveGroup(currentGroup.getiD(), currentUserID);
        }
        SetUpGroupHomepage();
    }

    public void DashToCreateAGroupPage(){
        String name = groupNameInputDash.getText();
        LoadCreateAGroupPage();
        groupNameInput.setText(name);
    }




    public void LoadCreateAGroupPage(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/group_creation.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupController gc = loader.getController();
        gc.toggleInviteSection(false);

        GUI.changeScene(root);

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        System.out.println("INitialised");


        //SetUpGroupHomepage();
    }
}
