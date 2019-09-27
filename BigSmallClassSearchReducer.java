package com.company;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;

public class BigSmallClassSearchReducer extends Reducer<Text,CheckValue,Text,CheckValue> {
    public static String itemName="BigSmallClassSpecificItem";
    public static String staticName="18BigClassStatistic";
    private MultipleOutputs<Text,CheckValue> mos;
    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException {
        mos=new MultipleOutputs(context);//初始化mos
    }
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
         mos.close();
    }
    protected void reduce(Text key, Iterable<CheckValue> checkValues, Context context) throws IOException, InterruptedException {
        try {
            int limit = 0;

            for (CheckValue checkValue : checkValues) {
                limit++;
                mos.write(staticName,key,checkValue);
                if(limit==100)
                    break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

