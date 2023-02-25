package api.responseStructure;

public class Id {
    private String name;
    private String value;

    public Id() {
    }

    public Id(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
