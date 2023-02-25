package api.responseStructure;

import java.util.ArrayList;

public class Root {
    private ArrayList<Result> results;
    private Info info;

    public Root() {
    }

    public Root(ArrayList<Result> results, Info info) {
        this.results = results;
        this.info = info;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public Info getInfo() {
        return info;
    }
}
