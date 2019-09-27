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

public class BigSmallClassSearchMapper extends Mapper<LongWritable,Text,Text,CheckValue> {


    private List<String> srcTotal=new ArrayList<>();
    public final static String searchFile="conf/18BigSmallClass.txt";
   // private final static IntWritable one=new IntWritable(1);
   // private List<Integer> states=new ArrayList<>();
    public void setup(Context context) throws IOException
    {
        InputStreamReader in=null;
        BufferedReader br=null;
        FileInputStream f=null;
        String lineInfo="";
        try {
            f=new FileInputStream(searchFile);
            in=new InputStreamReader(f,"GBK");
            br=new BufferedReader(in);

            while((lineInfo=br.readLine())!=null){
                String[] ss=lineInfo.split(" ");
                String bigClassName=ss[0];
                for(int i=1;i<ss.length;i++){
                    srcTotal.add(bigClassName+";;"+ss[i]);
                }
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
        }
    }
    protected void map(LongWritable key, Text value,Mapper<LongWritable,Text,Text,CheckValue>.Context context)
            throws IOException, InterruptedException {
        try {
            String line = new String(value.getBytes(),0,value.getLength(),"GBK");
            Document dom = DocumentHelper.parseText(line);
            Element root = dom.getRootElement();

            String srcType=root.element("SRC_TYPE").getText();
            if(srcTotal.indexOf(srcType)>=0){
                String srcName=root.element("SRC_NAME").getText();
                String srcAddress=root.element("SRC_ADDRESS_CHN").getText();
                String src_x=root.element("SRC_X").getText();
                String src_y=root.element("SRC_Y").getText();
                String keyWords=root.element("KEYWORDS").getText();
                CheckValue ckv=new CheckValue(srcName,srcAddress,src_x,src_y,keyWords);
                context.write(new Text(srcType),ckv);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

