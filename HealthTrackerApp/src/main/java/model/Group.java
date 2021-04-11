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
        return GDBH.getGroupName(getiD());
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
       boolean noDuplicateName = false;

        if(noDuplicateName = false) {
            this.iD = GDBH.addGroup(this.name, this.description, this.size);
        }
        else{
            /* HANDLE PROPERLY */
            System.out.println("Name not unique");
        }
    }


    public void generateiD(){
        int generatedID = 0;

        //Use database to work out unique ID

        this.iD = generatedID;
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

        System.out.println(group2.iD);


    }


}
