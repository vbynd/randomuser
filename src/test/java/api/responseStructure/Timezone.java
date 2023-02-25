package api.responseStructure;

public class Timezone {
    private String offset;
    private String description;

    public Timezone() {
    }

    public Timezone(String offset, String description) {
        this.offset = offset;
        this.description = description;
    }

    public String getOffset() {
        return offset;
    }

    public String getDescription() {
        return description;
    }
}
