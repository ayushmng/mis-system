
package com.example.ayush.missystem;

public class Cell {

    private String mId;
    private Object mData;

    private String mFilterKeyword;

    public Cell(String id) {
        this.mId = id;
    }

    public Cell(String id, Object data) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
    }

    public String getId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Object getContent(){
       return mData;
    }

    public Object getData() {
        return mData;
    }

    public void setData(String mData) {
        this.mData = mData;
    }

    public String getFilterableKeyword() {
        return mFilterKeyword;
    }

    public void setFilterKeyword(String mFilterKeyword) {
        this.mFilterKeyword = mFilterKeyword;
    }

}

