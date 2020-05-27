package com.flloyd.mymoviememoir.M3Model;

public class PersonID {

        private String personid;
        private String personfname;
        private String personlname;
        private String gender;
        private String dob;
        private String streetaddress;
        private String statecode;
        private String postcode;

        public PersonID( String personid, String personfname, String personlname, String gender, String dob, String streetaddress, String statecode, String postcode) {
            this.personid = personid;
            this.personfname = personfname;
            this.personlname = personlname;
            this.gender = gender;
            this.dob = dob + "T00:00:00+11:00";
            this.streetaddress = streetaddress;
            this.statecode = statecode;
            this.postcode = postcode;
        }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getPersonfname() {
            return personfname;
        }

        public void setPersonfname(String personfname) {
            this.personfname = personfname;
        }

        public String getPersonlname() {
            return personlname;
        }

        public void setPersonlname(String personlname) {
            this.personlname = personlname;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getStreetaddress() {
            return streetaddress;
        }

        public void setStreetaddress(String streetaddress) {
            this.streetaddress = streetaddress;
        }

        public String getStatecode() {
            return statecode;
        }

        public void setStatecode(String statecode) {
            this.statecode = statecode;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }



}
