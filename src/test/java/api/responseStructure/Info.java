package api.responseStructure;

public class Info {
    private String seed;
    private Integer results;
    private Integer page;
    private String version;

    public Info() {
    }

    public Info(String seed, Integer results, Integer page, String version) {
        this.seed = seed;
        this.results = results;
        this.page = page;
        this.version = version;
    }

    public String getSeed() {
        return seed;
    }

    public Integer getResults() {
        return results;
    }

    public Integer getPage() {
        return page;
    }

    public String getVersion() {
        return version;
    }
}
