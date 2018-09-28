/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import java.awt.Desktop;
import java.awt.Graphics;
import java.util.*;
import java.io.IOException;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


/**
 *
 * @author Dereck
 */

public class Funciones {
    
   
    private ArrayList imagenDestino;
    public static int largo;
    public static int ancho;
    
    //Datos recibidos desde la vista
    public int poblacion;
    public float porcentajeMalos;
    public int probabilidadCruce;
    public float probabilidadMutacion;
    public String rutaImagenMeta;
    public String rutaImagenDestino;
    
    public Funciones(){
        this.imagenDestino = new ArrayList(10000);
        
    }
    
    public Funciones(int pPoblacion, float pPorcentajeMalos, int pProbabilidadCruce, float pProbabilidadMutacion, String pRutaImagenMeta, String pRutaImagenDestino){
        this.imagenDestino = new ArrayList(10000);
        poblacion = pPoblacion;
        porcentajeMalos = pPorcentajeMalos;
        probabilidadCruce = pProbabilidadCruce;
        rutaImagenMeta = pRutaImagenMeta;
        rutaImagenDestino = pRutaImagenDestino;
        
    }
    
    public void leerImagen(){
        BufferedImage img = null;
        File f = null;

        //read image
        try{
          f = new File(rutaImagenMeta);
          img = ImageIO.read(f);
        }catch(IOException e){
          System.out.println(e);
        }
        int width = img.getWidth();
        int height = img.getHeight();
        largo = width;
        ancho = height;

        //get pixel value
        for(int i=0; i < width; i++){
            for(int j=0; j < height; j++){
                int pixel = img.getRGB(i, j);
                imagenDestino.add(pixel);
                
            }
        }
       
    }
    
    public BufferedImage conversorArrtoBuffer(ArrayList arr){//Auxiliar que convierte un array en BufferImage
        BufferedImage img = new BufferedImage(largo, ancho, BufferedImage.TYPE_INT_ARGB);
     
        int cont1 = 0;
        int cont2 = 0;
        int cont3= 0;
        while(cont1 < largo){
            
            
            while(cont2 < ancho){
                int pixel = (int)(arr.get(cont3));
                cont3++;
                img.setRGB(cont1, cont2, pixel);
                cont2++;
                
            }
            cont2 = 0;
            cont1++;
        }
        return img;
    }
    
    public void verImagen(ArrayList arr, String nombre){
        BufferedImage img = conversorArrtoBuffer(arr);
        //file object
        File f = null;
        
        try{
            f = new File("C:\\Users\\Gloriana\\Documents\\TEC\\Semestre II - 2018\\Análisis de Algoritmos\\Archivos_Progra_1" + nombre + ".jpg"); //cambiar ruta a tu compu
            ImageIO.write(img, "png", f);
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }
    
    public void unirImagenes(ArrayList arr1){ //Esta funcion une las 10 imagenes como una sola
        int cont = 0;
        BufferedImage b1 = conversorArrtoBuffer((ArrayList)arr1.get(0));
        BufferedImage result;
        result = new BufferedImage (b1.getWidth() * 10, b1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics ();
        g.drawImage (b1, 0, 0, null);
        int acu = b1.getWidth();
        cont++;
        while (cont < arr1.size()){
            BufferedImage b2 = conversorArrtoBuffer((ArrayList)arr1.get(cont));
            g.drawImage (b2, acu, 0, null);
            acu += b2.getWidth();
            cont++;    
        }
        
        g.dispose ();
        
        File f = null;
        try{
            f = new File(rutaImagenDestino + "\\unido.jpg"); 
            ImageIO.write(result, "png", f);
            //Abrir en photo viewer
            Desktop dt = Desktop.getDesktop();
            dt.open(f);
    System.out.println("Done.");
        } catch(IOException e){
            System.out.println("Error: " + e);
        }
         
    }
    
    public Imagen crearImagen(){
        ArrayList arr = new ArrayList(largo*ancho);
        BufferedImage img = new BufferedImage(largo, ancho, BufferedImage.TYPE_INT_ARGB);
        
        //file object
        File f = null;
        
        //Crear pixeles aleatorios pixel por pixel
        for(int y = 0; y < largo; y++){
            for(int x = 0; x < ancho; x++){
                int a = (int)(Math.random()*256); //alpha
                int r = (int)(Math.random()*256); //red
                int g = (int)(Math.random()*256); //green
                int b = (int)(Math.random()*256); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b;
                arr.add(p);
                }
        
        }
        
        Imagen im = new Imagen(arr);
        return im;
        
    }
    
    public ArrayList crearPoblacion(int n){ //Cada grupo de imagenes
        int cont = 0;
        ArrayList arr = new ArrayList(largo*ancho);
        while (cont < n){
            Imagen ima = crearImagen();
            arr.add(ima);
            cont++;
        }
        return arr;
        
    }
    
    public ArrayList seleccionEuclideana(ArrayList poblacion){ //toma la poblacion y la ordena segun su semejanza al mas apto
        ArrayList resul = new ArrayList();
        int cont = 0;    
        while(cont < poblacion.size()){
            Imagen imagenPrueba = (Imagen)poblacion.get(cont);           
            ArrayList listaPixeles = imagenPrueba.getArray();
            int cont2 = 0;        
            int acuR = 0;
            int acuG = 0;
            int acuB = 0;
            int acuA = 0;
            double acuRF = 0;
            double acuGF = 0;
            double acuBF = 0;
            double acuAF = 0;
            int ponderadoFin = 0;
            
            while(cont2 < listaPixeles.size()){
                int r1 = ((int)listaPixeles.get(cont) >> 16) & 0xff;
                int g1 = ((int)listaPixeles.get(cont) >> 8) & 0xff;
                int b1 = (int)listaPixeles.get(cont) & 0xff;
                int a1 = ((int)listaPixeles.get(cont) >> 24) & 0xff;
                
                int r2 = ((int)imagenDestino.get(cont) >> 16) & 0xff;
                int g2 = ((int)imagenDestino.get(cont) >> 8) & 0xff;
                int b2 = (int)imagenDestino.get(cont) & 0xff;
                int a2 = ((int)imagenDestino.get(cont) >> 24) & 0xff;
                
                acuR += (int)Math.pow((r1 - r2), 2);
                acuG += (int)Math.pow((g1 - g2), 2);
                acuB += (int)Math.pow((b1 - b2), 2);
                acuA += (int)Math.pow((a1 - a2), 2);
                
                acuRF = Math.pow((r1 - r2), 2);
                acuGF = Math.pow((g1 - g2), 2);
                acuBF = Math.pow((b1 - b2), 2);
                acuAF = Math.pow((a1 - a2), 2);
                
                cont2++;
            }
            acuR = (int)Math.sqrt(acuR);
            acuG = (int)Math.sqrt(acuG);   
            acuB = (int)Math.sqrt(acuB);
            acuA = (int)Math.sqrt(acuA);
            
            acuRF = Math.sqrt(acuRF);
            acuGF = Math.sqrt(acuGF);   
            acuBF = Math.sqrt(acuBF);
            acuAF = Math.sqrt(acuAF);
            
            ponderadoFin = (((acuR + acuG + acuB + acuA) / 4) / 10);
            //System.out.println("ponderadoFin: " + ponderadoFin);
            
            double normalizado = (((acuRF + acuGF + acuBF + acuAF) / 4) / 255 * 100);
            //System.out.println("Escalado: " + normalizado);
            
            imagenPrueba.setApto(ponderadoFin);
            poblacion.set(cont, imagenPrueba);
            
            resul.add(cont, normalizado);
            
            cont++;
            
        }
        quicksort(poblacion, 0, poblacion.size() - 1);  //un ordenamiento modificado de imagenes
        
        double resultMin = 100 - (double)Collections.min(resul);
        System.out.println("Minimo: " + resultMin);
        
        return poblacion;
    }
    
    public ArrayList seleccionManhattan(ArrayList poblacion){ //Basado en distancia manhattan
        ArrayList resul = new ArrayList();
        int cont = 0;    
        while(cont < poblacion.size()){
            Imagen imagenPrueba = (Imagen)poblacion.get(cont);           
            ArrayList listaPixeles = imagenPrueba.getArray();
            int cont2 = 0;        
            int acuR = 0;
            int acuG = 0;
            int acuB = 0;
            int acuA = 0;
            
            float acuRF = 0;
            float acuGF = 0;
            float acuBF = 0;
            float acuAF = 0;
            
            int ponderadoFin = 0;
            
            while(cont2 < listaPixeles.size()){
                int r1 = ((int)listaPixeles.get(cont) >> 16) & 0xff;
                int g1 = ((int)listaPixeles.get(cont) >> 8) & 0xff;
                int b1 = (int)listaPixeles.get(cont) & 0xff;
                int a1 = ((int)listaPixeles.get(cont) >> 24) & 0xff;
                
                int r2 = ((int)imagenDestino.get(cont) >> 16) & 0xff;
                int g2 = ((int)imagenDestino.get(cont) >> 8) & 0xff;
                int b2 = (int)imagenDestino.get(cont) & 0xff;
                int a2 = ((int)imagenDestino.get(cont) >> 24) & 0xff;
                
                acuR += (int)Math.abs(r1 - r2);
                acuG += (int)Math.abs(g1 - g2);
                acuB += (int)Math.abs(b1 - b2);
                acuA += (int)Math.abs(a1 - a2);
                
                
                //Acumula los datos en float para mayor precision
                acuRF += Math.abs(r1 - r2);
                acuGF += Math.abs(g1 - g2);
                acuBF += Math.abs(b1 - b2);
                acuAF += Math.abs(a1 - a2);
                
                cont2++;
            }
            
            
            ponderadoFin = (((acuR+acuG+acuB+acuA) / 4));
            imagenPrueba.setApto(ponderadoFin);
            //System.out.println("ponderadoFin: " + ponderadoFin);
            poblacion.set(cont, imagenPrueba);
            
            //Normaliza
            double normalizado = (((acuRF+acuGF+acuBF+acuAF) / 4)/ 25500);
            //System.out.println("Normalizado: " + normalizado);
            //Agrega el dato normalizado al array de resultados
            resul.add(cont, normalizado);
            
            cont++;
            
        }
        quicksort(poblacion, 0, poblacion.size() - 1);  //Un ordenamiento modificado de imagens
        
        //Le resta el menor de los resultados a 100 para saber el porcentaje de similitud
        double resultMin = 100 - (double)Collections.min(resul);
        System.out.println("Minimo: " + resultMin);
        
        return poblacion;
        
    }
    
    public ArrayList promedioVentana(ArrayList listaPixeles,int pivote){    //calcula promedio de ventana
        //Se recibe la lista de pixeles y el pivote es el pixel de la parte superior izquierda de lo que se quiere calcular
        ArrayList resul = new ArrayList();
        //Acumulados de cada color
        int acuR = 0;
        int acuG = 0;
        int acuB = 0;
        int acuA = 0;
        
        //Pixeles de ventana
        
        //Superior izquierdo
        acuR += ((int)listaPixeles.get(pivote) >> 16) & 0xff;      //Cada grupo de 4 instrucciones representa un pixel de la ventana
        acuG += ((int)listaPixeles.get(pivote) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote) & 0xff;
        acuA += ((int)listaPixeles.get(pivote) >> 24) & 0xff;

        //Superior centro
        acuR += ((int)listaPixeles.get(pivote + 1) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + 1) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + 1) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + 1) >> 24) & 0xff;

        //Superior derecho
        acuR += ((int)listaPixeles.get(pivote + 2) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + 2) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + 2) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + 2) >> 24) & 0xff;

        //Izquierdo
        acuR += ((int)listaPixeles.get(pivote + largo) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + largo) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + largo) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + largo) >> 24) & 0xff;

        //Derecho
        acuR += ((int)listaPixeles.get((pivote + largo) + 2) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get((pivote + largo) + 2) >> 8) & 0xff;
        acuB += (int)listaPixeles.get((pivote + largo) + 2) & 0xff;
        acuA += ((int)listaPixeles.get((pivote + largo) + 2) >> 24) & 0xff;

        //Inferior izquierdo
        acuR += ((int)listaPixeles.get(pivote + (2 * largo)) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + (2 * largo)) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + (2 * largo)) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + (2 * largo)) >> 24) & 0xff;

        //Inferior centro
        acuR += ((int)listaPixeles.get((pivote + (2 * largo)) + 1) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get((pivote + (2 * largo)) + 1) >> 8) & 0xff;
        acuB += (int)listaPixeles.get((pivote + (2 * largo)) + 1) & 0xff;
        acuA += ((int)listaPixeles.get((pivote + (2 * largo)) + 1) >> 24) & 0xff;

        //Inferior derecho
        acuR += ((int)listaPixeles.get((pivote + (2 * largo)) + 2) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get((pivote + (2 * largo)) + 2) >> 8) & 0xff;
        acuB += (int)listaPixeles.get((pivote + (2 * largo)) + 2) & 0xff;
        acuA += ((int)listaPixeles.get((pivote + (2 * largo)) + 2) >> 24) & 0xff;
        
        //Se promedia cada color de los 8 pixeles que forman la ventana
        acuR = acuR / 8;      
        acuG = acuG / 8;
        acuB = acuB / 8;
        acuA = acuA / 8;
        
        //Se agregan los acumulados al resultado
        resul.add(acuR);
        resul.add(acuG);
        resul.add(acuB);
        resul.add(acuA);
        
        return resul;
        
        
    }
    
    public ArrayList promedioVentanaNorm(ArrayList listaPixeles,int pivote){    //calcula promedio de ventana
        //Se recibe la lista de pixeles y el pivote es el pixel de la parte superior izquierda de lo que se quiere calcular
        ArrayList resul = new ArrayList();
        //Acumulados de cada color
        double acuR = 0;
        double acuG = 0;
        double acuB = 0;
        double acuA = 0;
        
        //Pixeles de ventana
        
        //Superior izquierdo
        acuR += ((int)listaPixeles.get(pivote) >> 16) & 0xff;      //Cada grupo de 4 instrucciones representa un pixel de la ventana
        acuG += ((int)listaPixeles.get(pivote) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote) & 0xff;
        acuA += ((int)listaPixeles.get(pivote) >> 24) & 0xff;

        //Superior centro
        acuR += ((int)listaPixeles.get(pivote + 1) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + 1) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + 1) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + 1) >> 24) & 0xff;

        //Superior derecho
        acuR += ((int)listaPixeles.get(pivote + 2) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + 2) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + 2) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + 2) >> 24) & 0xff;

        //Izquierdo
        acuR += ((int)listaPixeles.get(pivote + largo) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + largo) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + largo) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + largo) >> 24) & 0xff;

        //Derecho
        acuR += ((int)listaPixeles.get((pivote + largo) + 2) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get((pivote + largo) + 2) >> 8) & 0xff;
        acuB += (int)listaPixeles.get((pivote + largo) + 2) & 0xff;
        acuA += ((int)listaPixeles.get((pivote + largo) + 2) >> 24) & 0xff;

        //Inferior izquierdo
        acuR += ((int)listaPixeles.get(pivote + (2 * largo)) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get(pivote + (2 * largo)) >> 8) & 0xff;
        acuB += (int)listaPixeles.get(pivote + (2 * largo)) & 0xff;
        acuA += ((int)listaPixeles.get(pivote + (2 * largo)) >> 24) & 0xff;

        //Inferior centro
        acuR += ((int)listaPixeles.get((pivote + (2 * largo)) + 1) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get((pivote + (2 * largo)) + 1) >> 8) & 0xff;
        acuB += (int)listaPixeles.get((pivote + (2 * largo)) + 1) & 0xff;
        acuA += ((int)listaPixeles.get((pivote + (2 * largo)) + 1) >> 24) & 0xff;

        //Inferior derecho
        acuR += ((int)listaPixeles.get((pivote + (2 * largo)) + 2) >> 16) & 0xff;
        acuG += ((int)listaPixeles.get((pivote + (2 * largo)) + 2) >> 8) & 0xff;
        acuB += (int)listaPixeles.get((pivote + (2 * largo)) + 2) & 0xff;
        acuA += ((int)listaPixeles.get((pivote + (2 * largo)) + 2) >> 24) & 0xff;
        
        //Se promedia cada color de los 8 pixeles que forman la ventana
        acuR = acuR / 8;      
        acuG = acuG / 8;
        acuB = acuB / 8;
        acuA = acuA / 8;
        
        //Se agregan los acumulados al resultado
        resul.add(acuR);
        resul.add(acuG);
        resul.add(acuB);
        resul.add(acuA);
        
        return resul;
        
        
    }
    
    public ArrayList seleccionVecindario(ArrayList poblacion){ //Basado en ventanas 3x3
        ArrayList resul = new ArrayList();
        int cont = 0;    
        while(cont < poblacion.size()){
            Imagen imagenPrueba = (Imagen)poblacion.get(cont);           
            ArrayList listaPixeles = imagenPrueba.getArray();
            int cont2 = 0;        
            int acuR = 0;
            int acuG = 0;
            int acuB = 0;
            int acuA = 0;
            
            double acuRF = 0;
            double acuGF = 0;
            double acuBF = 0;
            double acuAF = 0;
            
            
            int limiteImagen = largo;
            int ponderadoFin = 0;
            int pivote = 0;         //Este pivote se coloca en la esquina superior izquierda de la imagen y controla el desplazamiento de la ventana
            while(pivote + (2 * largo) < (listaPixeles.size())){
                while(pivote < (limiteImagen - 2)){
                    ArrayList arr1 = promedioVentana(listaPixeles,pivote);
                    ArrayList arr2 = promedioVentana(imagenDestino,pivote);
                    
                    ArrayList arr3 = promedioVentanaNorm(listaPixeles, pivote);
                    ArrayList arr4 = promedioVentanaNorm(imagenDestino, pivote);
                    
                    //Aqui realiza las diferencia absolutas entre pixeles destino y prueba
                    acuR += (int)Math.abs((int)arr1.get(0) - (int)arr2.get(0));
                    acuG += (int)Math.abs((int)arr1.get(1) - (int)arr2.get(1));
                    acuB += (int)Math.abs((int)arr1.get(2) - (int)arr2.get(2));
                    acuA += (int)Math.abs((int)arr1.get(3) - (int)arr2.get(3));
                    
                    acuRF += Math.abs((double)arr3.get(0) - (double)arr4.get(0));
                    acuGF += Math.abs((double)arr3.get(1) - (double)arr4.get(1));
                    acuBF += Math.abs((double)arr3.get(2) - (double)arr4.get(2));
                    acuAF += Math.abs((double)arr3.get(3) - (double)arr4.get(3));
//                    
                    pivote++;
                
                }
                pivote = pivote + 2;
                limiteImagen += largo;
            }
            ponderadoFin = (((acuR + acuG + acuB + acuA)/ 4));
            imagenPrueba.setApto(ponderadoFin);
            //System.out.println("ponderadoFin: " + ponderadoFin);
            poblacion.set(cont, imagenPrueba);
            
            
            double normalizado = (((acuRF + acuGF + acuBF + acuAF)/ 4) / 25500);
            //System.out.println("Normalizado: " + normalizado);
            
            resul.add(cont, normalizado);
            
            cont++;                                         //Probar todo el algoritmo!!
                
        }
        //Un ordenamiento modificado de imagenes
        quicksort(poblacion, 0, poblacion.size() - 1);
        
        double resultMin = 100 - (double)Collections.min(resul);
        System.out.println("Minimo: " + resultMin);
        
        return poblacion;
                
    }
    
    public static void quicksort(ArrayList poblacion, int izq, int der) {
        Imagen piv =(Imagen)poblacion.get(izq);
        
        // tomamos primer elemento como pivote
        int pivote =((Imagen)poblacion.get(izq)).getApto();
        
        // i realiza la búsqueda de izquierda a derecha
        int i = izq;
        
        // j realiza la búsqueda de derecha a izquierda
        int j = der;
        
        Imagen aux;
        
        // mientras no se crucen las búsquedas
        while(i < j){
            //buca elemento mayor que pivote
             while(((Imagen)poblacion.get(i)).getApto()<= pivote && i < j) i++;
             // busca elemento menor que pivote
             while(((Imagen)poblacion.get(j)).getApto() > pivote) j--;         
             // si no se han cruzado 
             if (i < j) {
                // los intercambia
                aux = (Imagen)poblacion.get(i);                  
                poblacion.set(i, (Imagen)poblacion.get(j));
                poblacion.set(j, aux);
           }
         }
         
        // se coloca el pivote en su lugar de forma que tendremos
        poblacion.set(izq, (Imagen)poblacion.get(j));
        // los menores a su izquierda y los mayores a su derecha
        poblacion.set(j, piv);
        if(izq < j-1)
            // ordenamos subarray izquierdo
            quicksort(poblacion, izq, j - 1); 
        if(j+1 < der)
            // ordenamos subarray derecho
            quicksort(poblacion, j + 1, der); 
    }
    
    public ArrayList cruzar(ArrayList imagen1, ArrayList imagen2){
        
        int pixelOne = 0;
        int pixelTwo = 0;
        int pixelDestino = 0;
        
        ArrayList imagenCruzada = new ArrayList(largo*ancho);
        for(int i = 0; i < largo*ancho; i++){
            int rFin = 0;
            int gFin = 0;
            int bFin = 0;
            int aFin = 0;
            pixelDestino = (int) imagenDestino.get(i);
            pixelOne = (int) imagen1.get(i);
            pixelTwo = (int) imagen2.get(i);
            
            int r1 = (pixelOne >> 16) & 0xff;
            int g1 = (pixelOne >> 8) & 0xff;
            int b1 = pixelOne & 0xff;
            int a1 = (pixelOne >> 24) & 0xff;
                
            int r2 = (pixelTwo >> 16) & 0xff;
            int g2 = (pixelTwo >> 8) & 0xff;
            int b2 = pixelTwo & 0xff;
            int a2 = (pixelTwo >> 24) & 0xff;
            
            int rDes = (pixelDestino >> 16) & 0xff;
            int gDes = (pixelDestino >> 8) & 0xff;
            int bDes = pixelDestino & 0xff;
            int aDes = (pixelDestino >> 24) & 0xff;
            
            
            //Aqui compara los pixeles padre con el pixel destino y elige el mas parecido al destino.
            if(Math.abs(rDes - r1) < Math.abs(rDes - r2))       
                rFin = r1;
            else
                rFin = r2;
            
            if(Math.abs(gDes - g1) < Math.abs(gDes - g2))
                gFin = g1;
            else
                gFin = g2;
            if(Math.abs(bDes - b1) < Math.abs(bDes - b2))
                bFin = b1;
            else
                bFin = b2;
            if(Math.abs(aDes - a1) < Math.abs(aDes - a2))
                aFin = a1;
            else
                aFin = a2;
            
            int p = (aFin << 24) | (rFin << 16) | (gFin << 8) | bFin;
            imagenCruzada.add(p);
            
            
        }
        return imagenCruzada;
    }
    
    public ArrayList mutar(ArrayList imagen,float porcentaje){
        int pixelOne = 0;   
        int pixelDestino = 0;
        int numGenes = imagenDestino.size();
        int mutaMax = (int)(numGenes *(porcentaje / 100));
        
        int cont = 0;
        while(cont < mutaMax){
            //Elige un gen al azar
            int numRandom = (int)(Math.random()* numGenes);        
            
            pixelDestino = (int) imagenDestino.get(numRandom);
            pixelOne = (int) imagen.get(numRandom);
                
            int rDes = (pixelDestino >> 16) & 0xff;
            int gDes = (pixelDestino >> 8) & 0xff;
            int bDes = pixelDestino & 0xff;
            int aDes = (pixelDestino >> 24) & 0xff;
          
            //mutacion controlada: genera numero aleatorio entre pixel destino y pixelDestino-50
            int r1 = (int)(Math.random() * (((rDes-15) - (rDes)) + 1) + rDes);  
            int g1 = (int)(Math.random() * (((gDes-15) - (gDes)) + 1) + gDes);                 
            int b1 = (int)(Math.random() * (((bDes-15) - (bDes)) + 1) + bDes);   
            int a1 = (int)(Math.random() * (((aDes-15) - (aDes)) + 1) + aDes);    
                      
            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
            imagen.set(numRandom, p);
            cont++;
        }
        return imagen;
        
    }
    public ArrayList emparejar(ArrayList poblacion, String tipoAlgoritmo){
        int cont1 = 0;
        ArrayList bonitos = new ArrayList(poblacion.size());
        ArrayList feos = new ArrayList(poblacion.size());
        ArrayList resultado = new ArrayList(poblacion.size());
        
        Imagen primerApto = (Imagen) poblacion.get(0);
        Imagen ultimoApto = (Imagen) poblacion.get(poblacion.size()-1);
        //System.out.println("PRIMERO " + primerApto.getApto());
        //System.out.println("ULTIMO " + ultimoApto.getApto());
        
        //divide bonitos y feos bajo criterio 50 y 50
        while(cont1 < poblacion.size()){      
            if(cont1 < (poblacion.size()/2)){
                bonitos.add(poblacion.get(cont1));
                cont1++;                   
            }
            else{
                feos.add(poblacion.get(cont1));
                cont1++;
            }     
        }
        int numHijos = 0;
        int nMalos = (int) (poblacion.size()*(porcentajeMalos/100));
        
        //aqui elegimos cruzar la cantidad de malos con buenos requerida
        while(numHijos < nMalos){     
            int rand1 = (int)(Math.random()*(bonitos.size()));
            int rand2 = (int)(Math.random()*(feos.size()));
            Imagen im1 = (Imagen) bonitos.get(rand1);
            Imagen im2 = (Imagen) feos.get(rand2);
            //se cruzan
            ArrayList r1 = cruzar(im1.getArray(), im2.getArray());   
            resultado.add(r1);
            numHijos++;
            
        }
        //Una vez garantizado un minimo variable, se elegira aleatoriamente las parejas
        int tipo1 ;   
        int tipo2 ;
        int rand1 ;
        int rand2 ;
        Imagen im1;
        Imagen im2;
        while(numHijos < poblacion.size()){
            int probabilidad = (int)(Math.random()*100);
            
            tipo1 = (int)(Math.random()*2);
            if (tipo1 == 0){
                rand1 = (int)(Math.random()*(bonitos.size())); 
                im1 = (Imagen) bonitos.get(rand1);
            }
            else {
                rand1 = (int)(Math.random()*(feos.size()));
                im1 = (Imagen) feos.get(rand1);
            }
            tipo2 = (int)(Math.random()*2);
            if (tipo2 == 0){
                rand2 = (int)(Math.random()*(bonitos.size()));
                im2 = (Imagen) bonitos.get(rand2);
            }
            else {
                rand2 = (int)(Math.random()*(feos.size()));
                im2 = (Imagen) feos.get(rand2);
            }
           
            if(probabilidad < probabilidadCruce){
                ArrayList r1 = cruzar(im1.getArray(), im2.getArray());   //se cruzan
                resultado.add(r1);
                numHijos++;
            }
        }
        ArrayList resulFin = new ArrayList(poblacion.size());
        
        for(int i=0; i < poblacion.size(); i++){
             //aqui se mutan de acuerdo al porcentaje dado
            Imagen im = new Imagen(mutar((ArrayList)resultado.get(i), probabilidadMutacion));
            resulFin.add(im);
        }
        
        if (tipoAlgoritmo.equals("1")){
            resulFin = seleccionEuclideana(resulFin);
            
        }
        if (tipoAlgoritmo.equals("2")){
            resulFin = seleccionManhattan(resulFin);
            
        }
        if (tipoAlgoritmo.equals("3")){
            resulFin = seleccionVecindario(resulFin);
            
        }
        
        return resulFin;
    }
    
    public ArrayList crearFamilias(String tipoAlgoritmo){
        leerImagen();
        ArrayList concatenacion = new ArrayList(50);
        ArrayList poblacionInicial = crearPoblacion(poblacion);
        //verImagen(((Imagen)poblacionInicial.get(0)).getArray(), "salida0" + Integer.toString(0));
        ArrayList resulSeleccion = new ArrayList(largo * ancho);
        concatenacion.add(((Imagen)poblacionInicial.get(0)).getArray());
        
        if (tipoAlgoritmo.equals("1")){
            resulSeleccion = seleccionEuclideana(poblacionInicial);
            
        }
        if (tipoAlgoritmo.equals("2")){
            resulSeleccion = seleccionManhattan(poblacionInicial);
            
        }
        if (tipoAlgoritmo.equals("3")){
            resulSeleccion = seleccionVecindario(poblacionInicial);
            
        }
        
        int cont = 0;
        int cota = ((Imagen)resulSeleccion.get(0)).getApto();
        ArrayList arr = new ArrayList();
        arr = resulSeleccion;
        
        //Se concatenan las 10 imagenes resultado
        while(cont < 10){
            arr = emparejar(arr, tipoAlgoritmo);
            if (((Imagen)arr.get(0)).getApto() <= cota){
                concatenacion.add(((Imagen)arr.get(0)).getArray());
                cota = ((Imagen)arr.get(0)).getApto();
                cont++;
                
            }
            else
                cont++;
            
        }
        unirImagenes(concatenacion);
        return arr;
        
    }
    
    public void aplicarDistanciaEuclideana() {
        crearFamilias("1");
    }
    
    public void aplicarManhattan() {
        crearFamilias("2");
    }
    
    public void aplicarFiltradoImagenes(){
        crearFamilias("3");
    }   
    
}
