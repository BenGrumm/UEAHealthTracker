package model;

/**
 * This class is to store the information associated with groups.
 * This will store an ID, Name and Description and Size.
 * @author Alexander Clifford
 **/

public class Group {

    private int iD, size;
    private String name, description;
    private final GroupDBHelper GDBH = new GroupDBHelper();
    /** Getters **/

    public int getiD() {
        return iD;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return GDBH.getGroupName(this.iD);
    }

    public String getDescription() {
        return description;
    }

    /** Setters **/

    //Need to make sure does not already exist
    private void setiD(int iD) {
        this.iD = iD;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** Additional Methods **/
    public void saveGroup(){
        if(!checkDuplicateName()) {
            this.iD = GDBH.addGroup(this.name, this.description, this.size);
        }
        if(this.iD == 0){
            System.out.println("Error: Saving Group");
        }

        else{
            System.out.println("Name already exists, enter a different name");
        }
    }


    /**Return true if the name exists, false if it doesn't **/
    public boolean checkDuplicateName(){
        if(GDBH.doesGroupNameExist(this.name)){
            return true;
        }
        else{
            return false;
        }
    }

    public void incrementSize() {
        this.size = size + 1;
    }




    /**Constructors**/

    //Without ID. (Preferred)
    public Group(String name, String description) {
        this.size = 1;
        this.name = name;
        this.description = description;
        saveGroup();
    }

    /** Test Harness **/

    public static void main(String[] args) {

        Group group2 = new Group("Test2","Desc");
        Group group3 = new Group("Test3","Desc");
        Group group4 = new Group("Test4","Desc");
        System.out.println(group2.iD);
        System.out.println(group3.iD);
        System.out.println(group4.iD);

        System.out.println(group2.getName());

    }


}
