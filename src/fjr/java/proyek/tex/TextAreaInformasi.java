/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fjr.java.proyek.tex;

import javafx.scene.control.TextArea;

/**
 *
 * @author fajar
 */
public class TextAreaInformasi  extends TextArea{
    
    double width; 
    double height; 
    
    public TextAreaInformasi(double width, double height, String text){
        super(text);
        this.width = width; 
        this.height = height;
    }
    
    public void setTex(String text){
        super.setText(text);
    }
}
