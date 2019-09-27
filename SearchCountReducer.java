package com.company;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;

public class SearchCountReducer extends Reducer<Text,CateValue,Text,Text> {
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
    protected void reduce(Text key, Iterable<CateValue> cateValues, Context context) throws IOException, InterruptedException {
        try {

            Text valueNum=new Text();
            int sumTotal = 0;
            int sumSearch = 0;

            for (CateValue cateValue : cateValues) {
                sumTotal++;
                if (FiledProcess.tagProcess(cateValue.getTag()))
                    sumSearch++;
            }
            int sumNotSearch=sumTotal-sumSearch;
            ReduceValue reduceValue = new ReduceValue(sumTotal, sumNotSearch);
            valueNum.set(reduceValue.toString());
            context.write(key,valueNum);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
