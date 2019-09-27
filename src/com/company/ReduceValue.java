package com.company;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ReduceValue implements Writable {
    private int total;
    private int notSearch;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNotSearch() {
        return notSearch;
    }

    public void setNotSearch(int notSearch) {
        this.notSearch = notSearch;
    }

    public ReduceValue() {
        super();
    }

    public ReduceValue(int total, int notSearch) {
        this.total = total;
        this.notSearch = notSearch;
    }
    public void set(int total, int notSearch) {
        this.total = total;
        this.notSearch = notSearch;
    }
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(total);
        dataOutput.writeInt(notSearch);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.total=dataInput.readInt();
        this.notSearch=dataInput.readInt();
    }
    @Override
    public String toString(){
        return total+"\t"+notSearch;
    }
}
