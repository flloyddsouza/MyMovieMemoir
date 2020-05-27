package com.flloyd.mymoviememoir.M3Model;

public class CredentialsID {
    private String credentialid;
    private String username;
    private String password;
    private String signupdate;
    private PersonID personid;


    public CredentialsID( String credentialid, String username, String password, String signupdate) {
        this.credentialid = credentialid;
        this.username = username;
        this.password = password;
        this.signupdate = signupdate + "T00:00:00+11:00";
    }

    public String getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(String credentialid) {
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

    public PersonID getPersonid() {
        return personid;
    }

    public void setPersonid(PersonID personid) {
        this.personid = personid;
    }
}
