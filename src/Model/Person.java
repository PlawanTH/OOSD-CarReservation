/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author chalinyasutrat
 */
class Person {
    
    private String citizenID;
    private String firstname;
    private String lastname;
    private String dateOfBirth;
    private String address;
    private String subdistrict;
    private String district;
    private String city;
    private String country;
    private String zipCode;
    private String email;
    private String phoneNumber;

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * This is a method to set all personal data from HashMap that received from database.
     * 
     * @param dataSet This is a set of data that receive from database
     */
    public void setPersonalData(java.util.HashMap dataSet){
        citizenID = ((String)dataSet.get("CitizenID"));
        firstname = ((String)dataSet.get("Firstname"));
        lastname = ((String)dataSet.get("Lastname"));
        dateOfBirth = ((String)dataSet.get("DateOfBirth"));
        address = ((String)dataSet.get("Address"));
        subdistrict = ((String)dataSet.get("Subdistrict"));
        district = ((String)dataSet.get("District"));
        city = ((String)dataSet.get("City"));
        country = ((String)dataSet.get("Country"));
        zipCode = ((String)dataSet.get("ZipCode"));
        email = ((String)dataSet.get("Email"));
        phoneNumber = ((String)dataSet.get("PhoneNumber")); 
    }
    
}
