package fjr.java.proyek.tex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.regex.*;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class FileHandler {

    HashMap<Integer, FrameLabel> map;

    String path = "E:/presentasi tesis final .tex";

    ArrayList<String> listLabel;

    BufferedReader reader;

    RandomAccessFile randomAccessFile;

    int position = 0;

    File file;

    ArrayList<String> listResult;

    String stringHasil = "";

    Pattern framePattern = Pattern.compile("%?\\s*(\\\\begin\\{frame\\})?\\s*\\[.*label\\s*=\\s*([A-Za-z]+)\\]");
    
    Pattern second = Pattern.compile("\\s*(\\\\begin\\{frame\\})\\s*"); 
    
    Pattern third = Pattern.compile("\\s*(\\\\begin\\{frame\\})\\s*"); 
    
    Pattern labelPattern = Pattern.compile(""); 
    
    enum ReadType {
        USING_BUFFER, USING_FILE_UTILS
    }

    ReadType readType = ReadType.USING_FILE_UTILS;

    String encoding;

    File mainFile;

    public FileHandler() {
        file = new File(path);
        listLabel = new ArrayList<>();
//        listResult = new ArrayList<>(); 
        encoding = "UTF-8";
    }

    public void randomAcces() throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(file, "r");
    }

    public String getLine() throws IOException {
        if (randomAccessFile == null) {
            System.out.println("random acces == 0");
            return "";
        }
//        randomAccessFile.seek(0);
//        byte[] b = new byte[10] ; 
//        String res  = randomAccessFile.read(b); 
        String s = randomAccessFile.readLine();
//        System.out.println(labelName); 
//        System.out.println(randomAccessFile.length()); 
//        return randomAccessFile.readUTF();
//        byte[] resul = 
        return s;
    }

    public void getLineBetweenTag() {
        String hasil = StringUtils.substringBetween("\\begin", "\\end");
        System.out.println(hasil);
    }

    public void openFile() {
        try {
            read();
        } catch (FileNotFoundException fileEx) {
            System.out.println("kesalahan dalam meload file: " + fileEx);
        } catch (IOException ioex) {
            System.out.println("kesalahan dalam IO: " + ioex);
        }
    }

    Pattern p;
    
    Matcher m;

    HashMap<Integer, FrameLabel> hashMap;

    ArrayList<FrameLabel> listFrameLabel = new ArrayList<>();

    ArrayList<String> listOutput = new ArrayList<>();
    
    String regexForBeginnginOfFrame = "^\\s*\\\\begin\\{frame\\}\\s*(\\[\\s*label\\s*=\\s*[A-Za-z]+\\s*\\])?\\s*$"; 
    String regexForEndOfFrame = "^\\s*\\\\end\\{frame\\}\\s*$"; 

    public void read(File f) throws FileNotFoundException, IOException {
        listFrameLabel.clear();
        this.mainFile = f;
        stringHasil = FileUtils.readFileToString(f);
        listResult = (ArrayList<String>) FileUtils.readLines(f, "UTF-8");
        int line = 0;
        int i;
        Matcher match;
        for (i = 0; i < listResult.size(); i++) {
            String str = listResult.get(i);
            if (Pattern.matches(regexForBeginnginOfFrame, str)) {
                int j = i;
                String s = "";
                StringBuilder builder = new StringBuilder();
                String label = "empty";
                int labelPosition = 0;
                boolean haveALabel = false;
                boolean run = true; 
                while(run && j< listResult.size()){
                    s = listResult.get(j);
                    if (!Pattern.matches(regexForEndOfFrame, path)) {
                        builder.append(s);
                        match = framePattern.matcher(s);
                        if (match.find()) {
                            labelPosition = j;
                            label = match.group(2);
                            haveALabel = true;
                        }
                        j++; 
                    } else {
                       run = false;
                    }
                }
                if (!haveALabel) { 
                    labelPosition = i+1;
                }
                String result = builder.toString();
                FrameLabel frameLabel = new FrameLabel(result, label, labelPosition);
                frameLabel.haveALabel(haveALabel);
                frameLabel.setFramePosition(i);
                listFrameLabel.add(frameLabel);
                i = j;
            }
        }
    }

    public void writeFile(ObservableList<FrameLabel> listLabel) throws IOException {
        ArrayList<String> listOutout = new ArrayList<>(listResult);
        for (FrameLabel frame : listLabel) {
            int posisi = frame.positionProperty().get();
            int posisiframe = frame.getFramePosition();
            boolean havelabel = frame.compileProperty().get();
            String labelName = frame.labelProperty().get();
            if (posisi == posisiframe) {
                String hasil = labelName.replaceAll("\\[\\s*label\\s*=\\s*[A-Za-z\\d]+\\s*\\]", "");
                if (havelabel) {
                    listOutout.add(posisi + 1, "[label=current]");
                } else {
                    listOutout.add(posisi + 1, "%[label=current]");
                }
            } else {
                if (labelName.equalsIgnoreCase("current")) {
                    listOutout.set(posisi, "%[label=current]");
                } else {
                    listOutout.set(posisi, "[label=current]");
                }
            }
        }
        String file = this.mainFile.getParent().replaceAll("\\\\", "/");
        file = file + "fjr_backup.tex";
        File f = new File(file);
        FileUtils.writeLines(f, listResult);
        FileUtils.writeLines(mainFile, listOutout);
    }

    public ArrayList getListFrameLabel() {
        return listFrameLabel;
    }

    public void reload() throws IOException{
       if(mainFile != null){
             read(mainFile);
       }
    }
    
    public HashMap getMap() {
        return hashMap;
    }

    public void next() {
        if (m.find()) {
            System.out.println(m.group());
        }
    }

    public void read() throws FileNotFoundException, IOException {
        read(new File(path));
    }

    public void readBuffer() {
    }
}
