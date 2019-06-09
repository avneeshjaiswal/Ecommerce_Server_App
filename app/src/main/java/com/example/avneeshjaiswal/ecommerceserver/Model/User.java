package com.example.avneeshjaiswal.ecommerceserver.Model;

/**
 * Created by avneesh jaiswal on 09-Feb-18.
 */

public class User {

    private String Name;
    private String Password;
    private String Phone;
    private String Res_id;

    public User(String name, String password, String phone, String res_id) {
        Name = name;
        Password = password;
        Phone = phone;
        Res_id = res_id;

    }

    public User() {
    }

    public User(String name,String password){
        Name = name;
        Password = password;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRes_id() {
        return Res_id;
    }

    public void setRes_id(String res_id) {
        Res_id = res_id;
    }
}
