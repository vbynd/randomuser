package api.responseStructure;

import java.util.Date;

public class Registered {
    private Date date;
    private Integer age;

    public Registered() {
    }

    public Registered(Date date, Integer age) {
        this.date = date;
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public Integer getAge() {
        return age;
    }
}
