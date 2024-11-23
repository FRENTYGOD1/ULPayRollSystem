public class Employee {
    private int employeeId;
    private String name;
    private String passWord;
    private String positionType;
    private int scalePoint;
    private int annualRate;

    public Employee(String name,String passWord, String positionType, int scalePoint, int annualRate){
        this.employeeId = employeeId;
        this.name = name;
        this.passWord = passWord;
        this.positionType = positionType;
        this.scalePoint = scalePoint;
        this.annualRate = annualRate;
    }

    public int getemployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getpassWord() {
        return passWord;
    }

    public String getpositionType() {
        return positionType;
    }

    public int getscalePoint() {
        return scalePoint;
    }

    public int getannualRate() {
        return annualRate;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setpassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setscalePoint(int scalePoint) {
        this.scalePoint = scalePoint;
    }

    public void setannualRate(int annualRate) {
        this.annualRate = annualRate;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }
}

