package com.company;

import org.apache.hadoop.io.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
    public static  final List<Integer> tagProcess1(String tagDemo, List<String> l){
        String[] ss=tagDemo.split("\\$\\$,");
        List<Integer> k=new ArrayList<Integer>();
        for(int i=0;i<ss.length;i++){
            String[] ss1=ss[i].split(":");
            int state=-1;
            if(ss1.length<2)
                continue;;
            if(ss1[0].equals("$$SEARCH")){
                String[] ss2=ss1[1].split("-");
                for(int j=0;j<ss2.length;j++){
                    state=l.indexOf(ss2[j]);
                    if(state>=0)
                        k.add(state);
                }
            }

        }
        return k;
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
