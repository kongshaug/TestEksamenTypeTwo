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
    
    private String joke;
    private String category;

    public JokeDTO(Joke joke) {
        this.joke = joke.getValue();
        this.category = joke.getCategories()[0];
    }



  

    
}
