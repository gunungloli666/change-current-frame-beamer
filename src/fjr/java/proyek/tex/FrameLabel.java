package fjr.java.proyek.tex;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FrameLabel {
    
    String content; 
    String label; 
    
    int framePosition; 
    
    private final BooleanProperty compileProperty; 
    
    private final StringProperty labelProperty; 
    
    private final StringProperty contentProperty; 
    
    private final IntegerProperty positionProperty;
    
    boolean haveLabel = false; 
    
    int labelPositon ;
    
    public FrameLabel(String content, String label, int labelPositon){
        this.content = content; 
        this.label = label; 
        if(label.matches("current")){
            compileProperty = new SimpleBooleanProperty(true); 
        }else{
            compileProperty = new SimpleBooleanProperty(false); 
        }
        labelProperty = new SimpleStringProperty(label); 
        contentProperty = new SimpleStringProperty(content); 
        positionProperty = new SimpleIntegerProperty(labelPositon);
        
        compileProperty.addListener(( ObservableValue<? extends Boolean> ov,  Boolean t,  Boolean t1) -> {
            if(t1){
                labelProperty.set("current");
            }else{
                labelProperty.set("%current");
            }
        });
        this.labelPositon = labelPositon; 
    }
    
    public void setFramePosition(int posisi){
        this.framePosition = posisi; 
    }
    
    public int getFramePosition(){
        return framePosition; 
    }
    
    public StringProperty labelProperty(){
        return labelProperty;
    }
    
    public StringProperty contentProperty(){
        return contentProperty;
    }
    
    public BooleanProperty  compileProperty(){
        return compileProperty; 
    }
    
    public IntegerProperty positionProperty(){
        return positionProperty; 
    }
    
    public void haveALabel(boolean haveLabel){
        this.haveLabel = haveLabel; 
    }
    
    public boolean isHaveALabel(){
        return haveLabel; 
    }
    
    public void changeLabel(String label){
        labelProperty.set(label);
    }
    
    public String getContent(){
        return content; 
    }
    
    public void setLabelPosition(int label){
        this.labelPositon = label; 
    }
    
    public int getLabelPositon(){
        return this.labelPositon; 
    }
    
    public String getLabel(){
        return label; 
    }
    
    public void setContent(String content){
        this.content = content; 
    }
    
    public void setLabel(String label){
        this.label = label; 
    }
}
