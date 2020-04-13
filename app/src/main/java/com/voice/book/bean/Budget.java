package com.voice.book.bean;

public class Budget {
    String BudegetId;
    String date;
    String type;
    String BudegetTypeId;
    String note;
    String num;

    public String getBudegetId() {
        return BudegetId;
    }

    public void setBudegetId(String budegetId) {
        BudegetId = budegetId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBudegetTypeId() {
        return BudegetTypeId;
    }

    public void setBudegetTypeId(String budegetTypeId) {
        BudegetTypeId = budegetTypeId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
