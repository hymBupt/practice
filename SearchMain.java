package com.company;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import javax.security.auth.login.AppConfigurationEntry;
import java.io.IOException;
import java.util.HashMap;

public class SearchMain {


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        for(String arg:args){
            System.out.println("arg:"+arg);
        }
        if(args==null||args.length<4){
            System.out.println("请输入参数！！");
            System.exit(1);
        }

        Configuration conf=new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        System.out.println("参数信息：");
        for (String str:otherArgs) {
            System.out.println("参数："+str);
        }
        if(otherArgs==null||otherArgs.length<1){
            System.exit(2);
        }
        String input=args[3];
        String output=args[4];
        conf.setInt("mapred.job.reuse.jvm.num.tasks", -1);
        conf.setBoolean("mapred.map.tasks.speculative.execution", true);    //推测执行参数，防止某一任务过大
        conf.setBoolean("mapred.reduce.tasks.speculative.execution", true); //推测执行参数，防止某一任务过大
        Job job=Job.getInstance(conf,"ForSearchProject");
        job.setJarByClass(SearchMain.class);
        job.setMapperClass(SearchCountMapper.class);
        job.setReducerClass(SearchCountReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CateValue.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

       // MultipleOutputs.addNamedOutput(job,"origin", TextOutputFormat.class,Text.class,Text.class);
       // MultipleOutputs.addNamedOutput(job,"end", TextOutputFormat.class,Text.class,Text.class);

        job.setNumReduceTasks(1);
        job.setInputFormatClass(TextInputFormat.class);
        LazyOutputFormat.setOutputFormatClass(job,GBKTextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(input));
        // 判断output文件夹是否存在，如果存在则删除
        Path path = new Path(output);// 取第1个表示输出目录参数（第0个参数是输入目录）
        FileSystem fileSystem =FileSystem.get(job.getConfiguration());// 根据path找到这个文件
        if (fileSystem.exists(path)) {
            fileSystem.delete(path, true);// true的意思是，就算output有东西，也一带删除
        }

        FileOutputFormat.setOutputPath(job,path);
        System.exit(job.waitForCompletion(true)?0:1);

    }
}
