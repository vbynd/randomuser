package api.responseStructure;

public class Street {
    private Integer number;
    private String name;

    public Street() {
    }

    public Street(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
