package com.company;

import org.apache.hadoop.io.Text;

import java.io.UnsupportedEncodingException;

public class FiledProcess {
    public FiledProcess() {
    }
    public static  final boolean tagProcess(String tagDemo){
        String[] ss=tagDemo.split("\\$\\$,");
        for(int i=0;i<ss.length;i++){
            String[] ss1=ss[i].split(":");
            if(ss1[0].equals("$$SEARCH"))
                return true;
        }
        return false;
    }
    public static Text transformTextToUTF8(Text text, String encoding) {
        String value = null;
        try {
            value = new String(text.getBytes(), 0, text.getLength(), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new Text(value);
    }

}
