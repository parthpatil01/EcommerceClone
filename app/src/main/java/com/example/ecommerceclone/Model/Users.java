package com.example.ecommerceclone.Model;

public class Users {
    private String username,phone,password,profilepic,email,address,gender;

    public Users(){

    }

    public Users(String username, String phone, String password, String profilepic, String email, String address, String gender) {
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.profilepic = profilepic;
        this.email = email;
        this.address = address;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
