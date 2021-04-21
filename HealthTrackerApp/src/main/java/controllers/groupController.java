package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.paint.Color;
import model.Group;
import model.GroupDBHelper;
import model.UserDBHelper;

public class groupController extends Controller implements Initializable{

    GroupDBHelper GDBH = new GroupDBHelper();
    UserDBHelper UDBH = new UserDBHelper();
    int currentUserID = 2;
    Group currentGroup;

    //Group creation form
    @FXML
    private Label errorLabel, isTitle, isInvCodeLabel,isInviteCodeLabel,isAddEmailLabel,isErrorLabel;
    @FXML
    private TextField groupNameInput,isEmailTextField;
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



    public void SetUpGroupHomepage(){
        ArrayList<Integer> usersGroupIDs = new ArrayList<Integer>();

        usersGroupIDs = GDBH.getUsersGroupIDs(currentUserID);

        Group[] usersGroups = new Group[usersGroupIDs.size()];

        for(int x= 0; x< usersGroupIDs.size(); x++){
            //Get object of groups
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
            currentGroup = usersGroups[0];
            changeGroup(currentGroup);
        }
    }

    public void changeGroup(Group currentGroup){

        groupNameLabel.setText(currentGroup.getName());
        groupDescLabel.setText(currentGroup.getDescription());
        System.out.println("Members: " + currentGroup.getSize());
        groupMemberCountLabel.setText("Members: " + currentGroup.getSize());
        groupRoleLabel.setText("Role: " + GDBH.getMembersRole(currentGroup.getiD(),currentUserID));
    }

    /**
     * Method used to let a member join the group. Saves them to the the database with MEMBER role
     */
    public void JoinGroup(){
        String invCode = invCodeTextBox.getText();
        int groupID= GDBH.GetGroupID(invCode) ;
        GDBH.AddMember(groupID, currentUserID);
        SetUpGroupHomepage();

    }

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //IF groups_creation:
        //toggleInviteSection(false);
        //IF groups:
        SetUpGroupHomepage();
    }
}
