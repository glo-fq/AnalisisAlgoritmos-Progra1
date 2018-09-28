/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controlador.*;
import Vista.*;


/**
 *
 * @author Gloriana
 */
public class GestorPrincipalForm {
    public static void main(String[] args)  {
        
        
        PrincipalForm vista = new PrincipalForm();
        Funciones modelo = new Funciones();
        
        ControladorFunciones controladorFunciones = new ControladorFunciones(vista, modelo);
        
        controladorFunciones.vista.setVisible(true);
        controladorFunciones.vista.setLocationRelativeTo(null);
    }
    
}
