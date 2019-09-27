package com.company;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.util.Iterator;

public class SearchCountReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
    //private MultipleOutputs<Text,Text> mos;
    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException {
        //mos=new MultipleOutputs(context);//初始化mos
    }
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
       // mos.close();
    }
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        try {
            int sum=0;
            for(IntWritable v:values){
                sum++;
            }
            context.write(key,new IntWritable(sum));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
