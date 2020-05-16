package com.flloyd.mymoviememoir.M3Model;

public class Credentials {

    private Integer credentialid;
    private String username;
    private String password;
    private String signupdate;
    private Person personid;


    public Credentials(Integer credentialid, String username, String password, String signupdate) {
        this.credentialid = credentialid;
        this.username = username;
        this.password = password;
        this.signupdate = signupdate + "T00:00:00+11:00";
    }

    public Integer getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(String signupdate) {
        this.signupdate = signupdate;
    }

    public Person getPersonid() {
        return personid;
    }

    public void setPersonid(Person personid) {
        this.personid = personid;
    }

}
