package com.gp.simpletodo;

import java.io.Serializable;

/**
 * This is a POJO for each item on the TODO List
 */
public class Item implements Serializable{
    private static final long serialVersionUID = 6177222050535318633L;
    private String text;//Name is also the description of the item for now.
    private String date;
    private int position;

    public Item(String txt, String dt){
        this.text = txt;
        this.date = dt;
    }

    public Item(String txt){
        this.text = txt;
        this.date = "";
    }

    public Item(){
        this.text = "";
        this.date = "";
    }

    public String getText(){
        return text;
    }
    public void setText(String str){
        text = str;
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int pos){
        this.position = pos;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String dt){
        date = dt;
    }
}
