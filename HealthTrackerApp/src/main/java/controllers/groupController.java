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

    private GroupDBHelper GDBH = new GroupDBHelper();
    private UserDBHelper UDBH = new UserDBHelper();
    private int currentUserID = User.getLoggedIn().getID();
    private Group currentGroup;
    private Group[] usersGroups;
    private int mgGroupID;
    private String usersCurrentUI;
    private String groupName;

    //Group creation form
    @FXML
    private Label errorLabel, isTitle, isInvCodeLabel,isInviteCodeLabel,isAddEmailLabel,isErrorLabel,createTitle,createGroupName,createGroupDesc;
    @FXML
    private TextField groupNameInput,isEmailTextField,groupNameInputDash;
    @FXML
    private TextArea groupDescriptionInput;
    @FXML
    private Button isInviteButton,createGroupButton;

    //Group Homepage
    @FXML
    private Label groupNameLabel,groupMemberCountLabel, groupDescLabel,groupRoleLabel,groupGoal1Label,groupGoal2Label,groupGoal3Label;
    @FXML
    private Button groupJoinButton,groupCreateButton,manageGroupButton,inviteMethodButton,leaveGroupButton,subButton;
    @FXML
    private ComboBox usersGroupsComboBox;
    @FXML
    private TextField invCodeTextBox;

    //Manage Group Page
    @FXML
    private Label mgErrLabel, mgGroupName, mgGroupDesc,mgGroupInvCode, mgID;
    @FXML
    private Button mgUpdateName,mgUpdateDesc, mgRegenerateCode, mgDelete;
    @FXML
    private TextField mgNameTextBox, mgDescTextBox;








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
     * Method used to show or hide the create group section of the UI to the user for the group they are on.
     * @param b True, to show, false to hide
     */
    public void toggleCreateSection(boolean b){
        createTitle.setVisible(b);
        createGroupName.setVisible(b);
        createGroupDesc.setVisible(b);
        groupNameInput.setVisible(b);
        groupDescriptionInput.setVisible(b);
        createGroupButton.setVisible(b);
    }
    /**
     * Method used to set up group creating UI for invite
     */
    public void SetUpInvite(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/group_creation.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupController gc = loader.getController();
        gc.toggleInviteSection(true);
        gc.toggleCreateSection(false);
        gc.isInviteCodeLabel.setText(currentGroup.getInvCode());

        GUI.changeScene(root);
        /*
        toggleInviteSection(true);
        toggleCreateSection(true);
        isInviteCodeLabel.setText(currentGroup.getInvCode());

         */
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

    /**
     * Method used to swap the group displayed on the group dashboard to the group selected in the combobox
     */
    public void SelectDifferentGroup(ActionEvent event){
        for(int x= 0; x< usersGroups.length; x++){
            if(usersGroupsComboBox.getValue().equals(usersGroups[x].getName())) {
                changeGroup(usersGroups[x]);
            }
        }
    }

    /**
     * Method used to setup the group dashboard, and get upto date list of the correct groups a user is in.
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
            LoadCreateAGroupPage();
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
        if(GDBH.getMembersRole(newGroup.getiD(),currentUserID).equals("OWNER")){
            manageGroupButton.setVisible(true);
        }
        else{
            manageGroupButton.setVisible(false);
        }
        if(GDBH.getMembersSubStatus(newGroup.getiD(), currentUserID).equals("YES")){
            subButton.setText("Unsubscribe");
        }
        else{
            subButton.setText("Subscribe");
        }


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
            goToGroupPage();
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
        goToGroupPage();
    }

    /**
     * Method used to move to create a group scene and carry the name they enter across
     */
    public void DashToCreateAGroupPage(){
        String name = groupNameInputDash.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/group_creation.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupController gc = loader.getController();
        gc.toggleInviteSection(false);
        gc.groupNameInput.setText(name);

        GUI.changeScene(root);
    }

    /**
     * Method used to move to create a group scene.
     */
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

    public void LoadManageGroup(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage_group.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupController gc = loader.getController();
        gc.mgGroupName.setText(currentGroup.getName());
        gc.mgGroupDesc.setText(currentGroup.getDescription());
        gc.mgGroupInvCode.setText(currentGroup.getInvCode());
        gc.mgID.setText(currentGroup.getiD() +"");
        GUI.changeScene(root);

    }

    public void RegenInvCode(){
        String invCode = generateInvitecode();
        while(GDBH.doesGroupInvCodeExist(invCode)){
            invCode = generateInvitecode();
        }
        GDBH.UpdateInvCode(Integer.parseInt(mgID.getText()),invCode);
        mgGroupInvCode.setText(invCode);
        mgErrLabel.setTextFill(Color.rgb(0, 255, 0));
        mgErrLabel.setText("Invite Code Updated");
    }

    public void UpdateDesc(){
        GDBH.UpdateDesc(Integer.parseInt(mgID.getText()),mgDescTextBox.getText());
        mgErrLabel.setTextFill(Color.rgb(0, 255, 0));
        mgErrLabel.setText("Description Updated");
        mgGroupDesc.setText(mgDescTextBox.getText());
    }

    public void UpdateName(){
        String name = mgNameTextBox.getText();

        if(!GDBH.doesGroupNameExist(name)) {
            GDBH.UpdateName(Integer.parseInt(mgID.getText()), name);
            mgErrLabel.setTextFill(Color.rgb(0, 255, 0));
            mgErrLabel.setText("Name Updated");
            mgGroupName.setText(mgNameTextBox.getText());
        }
        else{
            mgErrLabel.setTextFill(Color.rgb(255, 0, 0));
            mgErrLabel.setText("Name Unavailable");
            mgNameTextBox.setText("");
        }
    }

    public void DeleteGroup(){
        GDBH.DeleteGroup(Integer.parseInt(mgID.getText()));
        goToGroupPage();
    }

    public void Subscribe(){
        if(subButton.getText().equals("Subscribe")){
            //MAke column yes
            GDBH.ChangeSubscription(currentGroup.getiD(),currentUserID,"YES");
            subButton.setText("Unsubscribe");
        }
        else{
            GDBH.ChangeSubscription(currentGroup.getiD(),currentUserID,"NO");
            subButton.setText("Subscribe");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        System.out.println("INitialised");


        //SetUpGroupHomepage();
    }
}
