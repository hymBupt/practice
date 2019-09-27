package com.company;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;

public class BigSmallClassSearchReducer extends Reducer<Text,Text,Text,ReduceValue> {
    public static String itemName="BigSmallClassSpecificItem";
    public static String staticName="BigClassHitStatistic";
    private MultipleOutputs<Text,ReduceValue> mos;
    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException {
        mos=new MultipleOutputs(context);//初始化mos
    }
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
         mos.close();
    }
    protected void reduce(Text key, Iterable<Text> textValues, Context context) throws IOException, InterruptedException {
        try {
            int sumTotal=0;
            int sumSearch = 0;

            for (Text textValue : textValues) {
                sumTotal++;
                if(FiledProcess.tagProcess(textValue.toString()))
                    sumSearch++;
            }
            int sumNotSearch=sumTotal-sumSearch;
            ReduceValue rv=new ReduceValue(sumTotal,sumNotSearch);
            mos.write(staticName,key,rv);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

