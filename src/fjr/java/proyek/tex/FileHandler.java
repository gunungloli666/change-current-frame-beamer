/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fjr.java.proyek.tex;

/**
 *
 * @author fajar
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.regex.*;
import javafx.collections.ObservableList;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
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
//        System.out.println(s); 
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

    public void read(File f) throws FileNotFoundException, IOException {
        hashMap = new HashMap<>();
        listFrameLabel.clear();
        this.mainFile = f;
        System.out.println("FILE OPEN");
        p = Pattern.compile("\\\\end\\{frame\\}");
        Pattern secondPAttern = Pattern.compile("\\\\end\\{frame\\}");
        stringHasil = FileUtils.readFileToString(f);
        listResult = (ArrayList<String>) FileUtils.readLines(f, "UTF-8");
        int line = 0;
        int i;
        Pattern patern1 = Pattern.compile("%?\\s*(\\\\begin\\{frame\\})?\\s*\\[.*label\\s*=\\s*(.*)\\]");
        Matcher match;
        for (i = 0; i < listResult.size(); i++) {
            String str = listResult.get(i);
            if (str.contains("\\begin{frame}")) {
                int j = i;
                String s;
                StringBuilder builder = new StringBuilder();
                String label = "empty";
                int labelPosition = 0;
                boolean haveALabel = false;
                do {
                    s = listResult.get(j);
                    builder.append(s);
                    match = patern1.matcher(s);
                    if (match.find()) {
                        labelPosition = j;
                        label = match.group(2);
                        haveALabel = true;
                    }
                    j++;
                } while (!s.contains("\\end{frame}"));
                if (!haveALabel) {
                    labelPosition = i;
                }
//                       if (haveALabel) {
                String result = builder.toString();
                FrameLabel frameLabel = new FrameLabel(result, label, labelPosition);
                frameLabel.haveALabel(haveALabel);
                hashMap.put(i, frameLabel);
                listFrameLabel.add(frameLabel);
//                       }
                i = j + 1;
            }
        }
    }

    public void writeFile(ObservableList<FrameLabel> listLabel) throws IOException {
        ArrayList<String> listOutout = new ArrayList<>(listResult);
        for (FrameLabel frame : listLabel) {
            int posisi = frame.positionProperty().get();
            String s = frame.labelProperty().get();
            listOutout.set(posisi, s);
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
