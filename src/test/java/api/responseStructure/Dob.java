package api.responseStructure;

import java.util.Date;

public class Dob {
    private Date date;
    private Integer age;

    public Dob() {
    }

    public Dob(Date date, int age) {
        this.date = date;
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public int getAge() {
        return age;
    }
}
