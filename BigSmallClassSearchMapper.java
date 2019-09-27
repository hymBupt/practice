package com.company;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BigSmallClassSearchMapper extends Mapper<LongWritable,Text,Text,Text> {


    private List<String> srcTotal=new ArrayList<>();
    public final static String searchFile="conf/topKeyWord.txt";
    private final static IntWritable one=new IntWritable(1);
    private List<Integer> states=new ArrayList<>();
    public void setup(Context context) throws IOException
    {
       /* InputStreamReader in=null;
        BufferedReader br=null;
        FileInputStream f=null;
        String lineInfo="";
        try {
            f=new FileInputStream(searchFile);
            in=new InputStreamReader(f,"GBK");
            br=new BufferedReader(in);

            while((lineInfo=br.readLine())!=null){
                srcTotal.add(lineInfo.trim());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                System.out.println("大小："+srcTotal.size());
                br.close();;
                in.close();;
                f.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
    }
    protected void map(LongWritable key, Text value,Mapper<LongWritable,Text,Text,Text>.Context context)
            throws IOException, InterruptedException {
        try {
            String line = new String(value.getBytes(),0,value.getLength(),"GBK");
            Document dom = DocumentHelper.parseText(line);
            Element root = dom.getRootElement();

            String hitCount=root.element("HITCOUNT").getText();
            int hitCount1=Integer.parseInt(hitCount);
            if(hitCount1>0){
                String srcType = root.element("SRC_TYPE").getText();
                String ss[]=srcType.split(";;");
                String bigClass=ss[0];
                String tag=root.element("TAG").getText();
                context.write(new Text(bigClass), new Text(tag));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

