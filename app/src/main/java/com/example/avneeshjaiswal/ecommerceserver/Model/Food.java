package com.example.avneeshjaiswal.ecommerceserver.Model;

public class Food {

    private String description;
    private String food_name;
    private String image;
    private String price;
    private String res_id;

    public Food(String description, String food_name, String image, String price, String res_id) {
        this.description = description;
        this.food_name = food_name;
        this.image = image;
        this.price = price;
        this.res_id = res_id;
    }

    public Food() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRes_id() {
        return res_id;
    }

    public void setRes_id(String res_id) {
        this.res_id = res_id;
    }
}

