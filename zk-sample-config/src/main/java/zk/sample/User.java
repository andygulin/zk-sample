package zk.sample;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private int id;
    private String name;
    private int age;
    private String address;
    private Date createAt;

    public User(int id, String name, int age, String address, Date createAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
