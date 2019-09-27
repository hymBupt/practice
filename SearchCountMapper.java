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

public class SearchCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {

    private List<String> keys = new ArrayList<String>();
    private final static IntWritable one=new IntWritable(1);
    public final static String searchFile="conf/keyWord.txt";
    private List<Integer> states=new ArrayList<>();
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
                System.out.println("shuju"+lineInfo.trim());
                keys.add(lineInfo.trim());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                //keys.add("中餐");
                System.out.println("大小："+keys.size());
                br.close();;
                in.close();;
                f.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //srcTotal.put("旅游景点","*");
        //srcTotal.put("购物场所","*");
        //srcTotal.put("宾馆饭店","*");
    }
    protected void map(LongWritable key, Text value,Mapper<LongWritable,Text,Text, IntWritable>.Context context)
            throws IOException, InterruptedException {
        try {
            String line = new String(value.getBytes(),0,value.getLength(),"GBK");
            Document dom = DocumentHelper.parseText(line);
            Element root = dom.getRootElement();
            String tag = root.element("TAG").getText();
            states=FiledProcess.tagProcess(tag,keys);
            for(int i=0;i<states.size();i++)
                context.write(new Text(keys.get(states.get(i))), one);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
