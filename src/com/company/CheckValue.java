package com.company;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CheckValue implements Writable {
    private String src_name;
    private String src_address;
    private String src_x;
    private String src_y;
    private String keyWords;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }


    public String getSrc_name() {
        return src_name;
    }

    public void setSrc_name(String src_name) {
        this.src_name = src_name;
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


    public CheckValue(){
        super();
    }

    public CheckValue( String src_name, String src_address,
                     String src_x, String src_y, String keyWords) {
        this.src_name = src_name;
        this.src_address = src_address;
        this.src_x = src_x;
        this.src_y = src_y;
        this.keyWords=keyWords;
    }

    public void set( String src_name, String src_address,
                     String src_x, String src_y, String keyWords) {
        this.src_name = src_name;
        this.src_address = src_address;
        this.src_x = src_x;
        this.src_y = src_y;
        this.keyWords=keyWords;
    }
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(src_name);
        dataOutput.writeUTF(src_address);
        dataOutput.writeUTF(src_x);
        dataOutput.writeUTF(src_y);
        dataOutput.writeUTF(keyWords);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.src_name=dataInput.readUTF();
        this.src_address=dataInput.readUTF();
        this.src_x=dataInput.readUTF();
        this.src_y=dataInput.readUTF();
        this.keyWords=dataInput.readUTF();
    }
    @Override
    public String toString(){
        return src_name+"\t"+src_address+"\t"+
                src_x+"\t"+src_y+"\t"+keyWords;
    }
}

