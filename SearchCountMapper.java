package com.company;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

public class SearchCountMapper extends Mapper<LongWritable,Text,Text,CateValue> {


    private HashMap<String,String> srcTotal=new HashMap<>();
    public final static String searchFile="conf/testBigSmallClass.txt";
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
                String[] ss = lineInfo.split(";;");
                if (ss.length < 2)
                    continue;
                srcTotal.put(ss[0], ss[1]);
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
        //srcTotal.put("旅游景点","*");
        //srcTotal.put("购物场所","*");
        //srcTotal.put("宾馆饭店","*");
    }
    protected void map(LongWritable key, Text value,Mapper<LongWritable,Text,Text,CateValue>.Context context)
            throws IOException, InterruptedException {
        try {
            String line = new String(value.getBytes(),0,value.getLength(),"GBK");
            Document dom = DocumentHelper.parseText(line);
            Element root = dom.getRootElement();
            String srcType = root.element("SRC_TYPE").getText();
            String[] ss = srcType.split(";;");
            if (ss.length >= 2){
                if (srcTotal.containsKey(ss[0])) {
                String dataID = root.element("DATAID").getText();
                String srcNmae = root.element("SRC_NAME").getText();
                String srcCity = root.element("SRC_CITY").getText();
                String srcAddress = root.element("SRC_ADDRESS_CHN").getText();
                String src_x = root.element("SRC_X").getText();
                String src_y = root.element("SRC_Y").getText();
                String tag = root.element("TAG").getText();
                String laiyuan = root.element("LAI_YUAN").getText();
                context.write(new Text(srcType), new CateValue(dataID,
                        srcNmae, srcCity, srcAddress, src_x, src_y, tag, laiyuan));}
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
