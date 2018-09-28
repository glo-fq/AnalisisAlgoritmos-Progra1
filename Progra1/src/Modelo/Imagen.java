/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import java.util.*;

/**
 *
 * @author Dereck
 */
public class Imagen {
    private ArrayList array;
    private int apto;
    
    public Imagen(ArrayList arr){
        this.array = arr;
        this.apto = 0;
    }
    
    public ArrayList getArray(){
        return array;
    }
    public void setApto(int n){
        apto = n;
    }
    public int getApto(){
        return apto;
    }
    
    
}
