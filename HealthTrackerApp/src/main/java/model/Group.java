package model;

/**
 * This class is to store the information associated with groups.
 * This will store an ID, Name and Description and Size.
 * @author Alexander Clifford
 **/

public class Group {

    private int iD, size;
    private String name, description, role;

    /** Getters **/

    public String getRole() {
        return role;
    }
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

    public void setRole(String role) {
        this.role = role;
    }

    /** Additional Methods **/
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
    public Group(int size, String name, String description, String role) {
        generateiD();
        this.size = size;
        this.name = name;
        this.description = description;
        this.role = role;
    }

    //With ID
    public Group(int iD, int size, String name, String description,String role) {
        setiD(iD);
        this.size = size;
        this.name = name;
        this.description = description;
        this.role = role;
    }

    /** Test Harness **/

    public static void main(String[] args) {
        Group group1 = new Group(1,1,"Test1","");
        Group group2 = new Group(1,"Test2","Desc");

        System.out.println(group1.getiD());
        System.out.println(group1.getName());
        System.out.println(group1.getDescription());
        System.out.println(group1.getSize());

        System.out.println(group2.getiD());
        group2.setiD(10);
        System.out.println(group2.getiD());

        group1.setName("Changed");
        group1.setDescription("ChangedDesc");
        group1.setSize(2);

        System.out.println(group1.getiD());
        System.out.println(group1.getName());
        System.out.println(group1.getDescription());
        System.out.println(group1.getSize());

        group1.incrementSize();

        System.out.println(group1.size);

    }


}
