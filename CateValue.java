package com.company;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CateValue implements Writable {
    private String dataID;
    private String src_name;
    private String src_city;
    private String src_address;
    private String src_x;
    private String src_y;
    private String tag;
    private String laiyuan;

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getSrc_name() {
        return src_name;
    }

    public void setSrc_name(String src_name) {
        this.src_name = src_name;
    }

    public String getSrc_city() {
        return src_city;
    }

    public void setSrc_city(String src_city) {
        this.src_city = src_city;
    }

    public String getSrc_address() {
        return src_address;
    }

    public void setSrc_address(String src_address) {
        this.src_address = src_address;
    }


    public String getSrc_x() {
        return src_x;
    }

    public void setSrc_x(String src_x) {
        this.src_x = src_x;
    }

    public String getSrc_y() {
        return src_y;
    }

    public void setSrc_y(String src_y) {
        this.src_y = src_y;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLaiyuan() {
        return laiyuan;
    }

    public void setLaiyuan(String laiyuan) {
        this.laiyuan = laiyuan;
    }

    public CateValue(){
        super();
    }

    public CateValue(String dataID, String src_name, String src_city, String src_address,
                      String src_x, String src_y, String tag, String laiyuan) {
        this.dataID = dataID;
        this.src_name = src_name;
        this.src_city = src_city;
        this.src_address = src_address;
        this.src_x = src_x;
        this.src_y = src_y;
        this.tag = tag;
        this.laiyuan = laiyuan;
    }

    public void set(String dataID, String src_name, String src_city, String src_address,
                     String src_x, String src_y, String tag, String laiyuan) {
        this.dataID = dataID;
        this.src_name = src_name;
        this.src_city = src_city;
        this.src_address = src_address;
        this.src_x = src_x;
        this.src_y = src_y;
        this.tag = tag;
        this.laiyuan = laiyuan;
    }
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(dataID);
        dataOutput.writeUTF(src_name);
        dataOutput.writeUTF(src_city);
        dataOutput.writeUTF(src_address);
        dataOutput.writeUTF(src_x);
        dataOutput.writeUTF(src_y);
        dataOutput.writeUTF(tag);
        dataOutput.writeUTF(laiyuan);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.dataID=dataInput.readUTF();
        this.src_name=dataInput.readUTF();
        this.src_city=dataInput.readUTF();
        this.src_address=dataInput.readUTF();
        this.src_x=dataInput.readUTF();
        this.src_y=dataInput.readUTF();
        this.tag=dataInput.readUTF();
        this.laiyuan=dataInput.readUTF();
    }
    @Override
    public String toString(){
        return dataID+"\t"+src_name+"\t"+src_city+"\t"+src_address+"\t"+
                src_x+"\t"+src_y+"\t"+tag+"\t"+laiyuan;
    }
}
