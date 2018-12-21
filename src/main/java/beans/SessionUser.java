package beans;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@Stateful
@SessionScoped
@ManagedBean(name = "userSession")
public class SessionUser implements Serializable {

    private String email;
    private String name;
    private String surname;

    public SessionUser() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
