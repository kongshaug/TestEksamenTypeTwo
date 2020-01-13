/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author benja
 */
public class JokesDTO {
    
    private ArrayList<JokeDTO> Jokes;
    private String Reference = "api.chucknorris.io";

    public JokesDTO(ArrayList<JokeDTO> Jokes) {
        this.Jokes = Jokes;
    }

  
    
    
    
}
