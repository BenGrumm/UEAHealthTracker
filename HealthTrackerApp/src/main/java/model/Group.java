package model;

/**
 * This class is to store the information associated with groups.
 * This will store an ID, Name and Description and Size.
 * @author Alexander Clifford
 **/

public class Group {

    private int iD, size;
    private String name, description,invCode;
    private final GroupDBHelper GDBH = new GroupDBHelper();
    /** Getters **/

    public int getiD() {
        return iD;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
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
            GDBH.addGroup(this.name, this.description, this.size,this.invCode,1);
        }
        else if(this.iD == 0){
            System.out.println("Error: Saving Group");
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
    public Group(int id, String name, String description,int size, String invCode) {
        this.iD = id;
        this.name = name;
        this.description = description;
        this.size = size;
        this.invCode = invCode;
        //saveGroup();
    }

    /** Test Harness **/

    public static void main(String[] args) {
/*
        Group group2 = new Group("Test2","Desc","XOPPOX");
        Group group3 = new Group("Test3","Desc","KMLLMK");
        Group group4 = new Group("Test4","Desc","TESTED");
        System.out.println(group2.iD);
        System.out.println(group3.iD);
        System.out.println(group4.iD);

        System.out.println(group2.getName());
*/
    }


}
