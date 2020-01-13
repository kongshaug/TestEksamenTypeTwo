/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author benja
 */
public class JokeDTO {
    
    private String value;
    private String[] categories;

    public JokeDTO(String value, String[] categories) {
        this.value = value;
        this.categories = categories;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    
}
