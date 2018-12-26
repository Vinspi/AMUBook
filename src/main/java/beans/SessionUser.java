package beans;

import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@Stateful
@ManagedBean(name = "sessionUser")
@SessionScoped
public class SessionUser implements Serializable {

    private String email;
    private String name;
    private String surname;
    private boolean validAccount;

    private String temporaryPass;

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

    public String getTemporaryPass() {
        return temporaryPass;
    }

    public void setTemporaryPass(String temporaryPass) {
        this.temporaryPass = temporaryPass;
    }

    public boolean isValidAccount() {
        return validAccount;
    }

    public void setValidAccount(boolean validAccount) {
        this.validAccount = validAccount;
    }

    @Override
    public String toString() {
        return "SessionUser{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
