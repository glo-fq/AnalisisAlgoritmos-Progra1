/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Gloriana
 */
public class ControladorFunciones implements ActionListener {
    public PrincipalForm vista;
    public Funciones modelo;
    
    public ControladorFunciones(PrincipalForm pVista, Funciones pModelo){
        vista = pVista;
        modelo = pModelo;
        
        this.vista.btnIngresarDatos.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()) {
            case "Ingresar datos":
                aplicarAlgoritmo();
                break;
            case "Cancelar":
                cerrarVentanaPrincipal();
                break;
            default:
                break;
        }
    }
    
    public void aplicarAlgoritmo() {
        //Se verifica que todos los campos estén llenos
        if(vista.principalDatosCorrectos()) {
            System.out.println("Dentro Aplicar Algoritmo");
            
            //Se reciben los datos que se introdujeron en los campos
            String tamPoblacion = vista.txtTamPoblacion.getText();
            String probabilidadCruce = vista.txtProbabilidadCruce.getText();
            String porcentajeGenes = vista.txtPorcentajeGenes.getText();
            String porcentajeIndividuosMenosAptos = vista.txtPorcentajeIndividuosMenosAptos.getText();
            String rutaImagenMeta = vista.txtRutaImagenMeta.getText();
            String rutaImagenDestino = vista.txtRutaImagenDestino.getText();
            
            String algoritmoSeleccionado = (String) vista.comboBoxAlgoritmo.getSelectedItem();
            
            try {
                //try para convertir los Strings recibidos a números válidos
                int numTamPoblacion = Integer.parseInt(tamPoblacion);
                float numPorcentajeIndividuosMenosAptos = Float.valueOf(porcentajeIndividuosMenosAptos);
                int numProbabilidadCruce = Integer.parseInt(probabilidadCruce);
                float numPorcentajeGenes = Float.valueOf(porcentajeGenes);
                
                //Constructos del modelo
                modelo = new Funciones(numTamPoblacion, numPorcentajeIndividuosMenosAptos, numProbabilidadCruce, numPorcentajeGenes, rutaImagenMeta, rutaImagenDestino);
                
                if("Distancia euclideana".equals(algoritmoSeleccionado)) {
                    System.out.println("Aplicando dist euclideana");
                    modelo.aplicarDistanciaEuclideana();
                }
                
                if("Manhattan".equals(algoritmoSeleccionado)) {
                    System.out.println("Aplicando Manhattan");
                    modelo.aplicarManhattan();
                }
                if("Filtrado de imágenes".equals(algoritmoSeleccionado)) {
                    System.out.println("Aplicando filtrado de imágenes");
                    modelo.aplicarFiltradoImagenes();
                }
            
                
                JOptionPane.showMessageDialog(vista, "Proceso finalizado con éxito.");
                
            } catch(NumberFormatException excepcion) {
                JOptionPane.showMessageDialog(vista, "Hubo un problema leyendo los datos. Por favor intente de nuevo.");
            }
            
            
           
            
        } else {
            //Si falta algún campo, se abre una ventana que lo indique
            JOptionPane.showMessageDialog(vista, "Todos los datos son requeridos");
        }
    }
    
    public void cerrarVentanaPrincipal() {
        vista.cerrarVentana();
    }
    
}
