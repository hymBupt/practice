package com.company;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BigSmallClassResult {
    private static String curVersion;// 当前版本
    private static String historyVersions;// 所有历史版本
    private List<Area> bigSmallClassStatics=new ArrayList<>();
    private  String resultDir="./result/";
    private  static List<String> totalWord=new ArrayList<>();
    private final static String searchFile="./conf/18BigSmallClass.txt";
    // 输出文件名称
    // 片区
    private final static String bigSmallClassStaticFileName = "大小类抽查统计.xls";

    public static void main(String[] args) {
        BigSmallClassResult acr = new BigSmallClassResult();
        acr.initVersion("v19.3test", "v18.12,v19.1,v19.2");
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
                    totalWord.add(bigClassName+";;"+ss[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                System.out.println("大小："+totalWord.size());
                System.out.println("第二："+totalWord.get(1));
                System.out.println("第二："+totalWord.indexOf("餐饮服务;;一般中餐"));
                br.close();;
                in.close();;
                f.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(String arg:args){
            System.out.println("arg:"+arg);
        }
        if(args==null||args.length<1){
            System.out.println("请输入参数！！");
            System.exit(1);
        }

        acr.doResultWithCmd("hadoop fs -text " + args[0]);

    }

    // 初始化现版和历史版本
    public void initVersion(String curVer, String hisVers) {
        curVersion = curVer;
        historyVersions = hisVers;
    }

    public void doResultWithCmd(String bigSmallClassStaticCmd) {
        initBigSmallClassStaticCmd(bigSmallClassStaticCmd);
        // 导出excel
        exportExcel();
        //清空list里的数据
        clearData();
    }
    public static final String emptyToNull(String s) {
        return s == null ? s : s.trim().length()==0 ? null : s.trim();
    }
    // 初始化片区没有挂接poi的数据
    private void initBigSmallClassStaticCmd(String cmd) {
        // 初始化成员属性: distanceMore200PoiList
        try {
            Process process = getProcessWithCmd(cmd);
            BufferedReader bufferedReader = getISBufferReaderWithProcess(process);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = emptyToNull(line);
                if (line == null ) {
                    continue;
                }
                Area area = Area.polygonWithNoPoiArea(line);
                this.bigSmallClassStatics.add(area);
            }

            bufferedReader.close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成质检报表
    private void exportExcel() {
        // 初始化sheet
        HSSFSheet sheet1 = singleExcelSheetWithName("sheet");

        initBigSmallClassStaticSheet(sheet1);

        exportExcelWithSheet(sheet1,  resultDir+ bigSmallClassStaticFileName);

    }

    private void clearData() {
        bigSmallClassStatics.clear();
    }

    private void initBigSmallClassStaticSheet(HSSFSheet sheet) {
        // 初始化标题行,内容行
        int titleRowN = 0;
        int curContentRow = 1;
        // 写入标题
        String[] titles = { "大类",  "小类","名称" ,"地址","x坐标","y坐标","keyword"};
        Row titleRow = sheet.createRow(titleRowN);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        for (Area area : bigSmallClassStatics) {
            // 初始化数据
            String[] contentArr = new String[titles.length];
            contentArr[0] = area.bigClassName;
            //contentArr[1] = area.smallClassName;
            contentArr[1] = area.smallClassName;
            contentArr[2]=area.srcName;
            contentArr[3] = area.srcAddress;
            contentArr[4] = area.src_x;
            contentArr[5]=area.src_y;
            contentArr[6] = area.keyWords;
           // float percents=(float)area.notSearchNum/area.totalNum;
            //NumberFormat nf=NumberFormat.getPercentInstance();
            //nf.setMaximumFractionDigits(4);

            // 写入数据
            HSSFRow contentRow = sheet.createRow(curContentRow++);
            for (int cellColumn = 0; cellColumn < titles.length; cellColumn++) {
                HSSFCell cell = contentRow.createCell(cellColumn);
                cell.setCellValue(contentArr[cellColumn]);
            }
        }
    }
   /* int getNum(String word){
        for(int i=0;i<bigSmallClassStatics.size();i++){
            if(word.equals(bigSmallClassStatics.get(i).keyWord))
                return bigSmallClassStatics.get(i).totalNum;
            else
                continue;
        }
        return -1;
    }*/

    // 用名字创建sheet
    private HSSFSheet singleExcelSheetWithName(String fileName) {
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象 (后面会关闭它)
        HSSFSheet sheet0 = workbook.createSheet(fileName); // 创建工作表
        return sheet0;
    }

    // 用sheet导出excel
    private void exportExcelWithSheet(Sheet sheet, String fileName) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            Workbook workbook = sheet.getWorkbook();
            workbook.write(out);
            workbook.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Process getProcessWithCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            return process;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static BufferedReader getISBufferReaderWithProcess(Process process) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferedReader;
    }

    // 片区// 片区
    public static class Area {
        public String bigClassName;// 大类
        public String smallClassName;// 小类
        public String srcName;
        public String srcAddress;
        public String src_x;
        public String src_y;
        public String keyWords;
        //public String keyWord;
        //public int totalNum; // 总量
        //public int notSearchNum; // 无search数量

        // 初始化片区没有挂接poi的area对象
        public static Area polygonWithNoPoiArea(String line) {
            String[] arr = line.split("\t");
            String[] classArr=arr[0].split(";;");
            String bigClassName = classArr[0];
            String smallClassName = classArr[1];
            //String bigClassName=arr[0];
            //int totalNum=Integer.parseInt(arr[1]);
            //int notSearchNum=Integer.parseInt(arr[2]);
            String srcName=arr[1];
            String srcAddress=arr[2];
            String src_x=arr[3];
            String src_y=arr[4];
            String keyWords;
            if(arr.length<6)
                keyWords="null";
            else keyWords=arr[5];
            Area area = new Area();
            //area.bigClassName=bigClassName;
            area.smallClassName=smallClassName;
            area.bigClassName=bigClassName;
            area.keyWords=keyWords;
            area.src_x=src_x;
            area.src_y=src_y;
            area.srcName=srcName;
            area.srcAddress=srcAddress;
            return area;
        }
    }


}
