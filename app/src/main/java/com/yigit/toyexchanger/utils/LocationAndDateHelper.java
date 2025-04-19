package com.yigit.toyexchanger.utils;


public class LocationAndDateHelper {
    String location, date,txttehlikename;
    String hastaname ,hastayas,hastacinsi,hastauzunlugu,hastaagirligi,hastabelirti,hastagecmisi,hastaalerjisi;
    LocationAndDateHelper(){

    }
    public LocationAndDateHelper(String location, String date) {
        this.location = location;
        this.date = date;
    }

    public LocationAndDateHelper(String location, String date, String txttehlikename) {
        this.txttehlikename=txttehlikename;
        this.location = location;
        this.date = date;
    }
    public LocationAndDateHelper(String hastaname , String hastayas, String hastacinsi, String hastauzunlugu, String hastaagirligi, String hastabelirti, String hastagecmisi, String hastaalerjisi) {
        this.hastaname=hastaname;
        this.hastayas = hastayas;
        this.hastauzunlugu = hastauzunlugu;
        this.hastaagirligi = hastaagirligi;
        this.hastacinsi = hastacinsi;
        this.hastabelirti=hastabelirti;
        this.hastagecmisi = hastagecmisi;
        this.hastaalerjisi = hastaalerjisi;
    }

    public String getHastauzunlugu() {
        return hastauzunlugu;
    }

    public void setHastauzunlugu(String hastauzunlugu) {
        this.hastauzunlugu = hastauzunlugu;
    }

    public String getHastaagirligi() {
        return hastaagirligi;
    }

    public void setHastaagirligi(String hastaagirligi) {
        this.hastaagirligi = hastaagirligi;
    }

    public String getHastaname() {
        return hastaname;
    }

    public void setHastaname(String hastaname) {
        this.hastaname = hastaname;
    }

    public String getHastayas() {
        return hastayas;
    }

    public void setHastayas(String hastayas) {
        this.hastayas = hastayas;
    }

    public String getHastacinsi() {
        return hastacinsi;
    }

    public void setHastacinsi(String hastacinsi) {
        this.hastacinsi = hastacinsi;
    }

    public String getHastabelirti() {
        return hastabelirti;
    }

    public void setHastabelirti(String hastabelirti) {
        this.hastabelirti = hastabelirti;
    }

    public String getHastagecmisi() {
        return hastagecmisi;
    }

    public void setHastagecmisi(String hastagecmisi) {
        this.hastagecmisi = hastagecmisi;
    }

    public String getHastaalerjisi() {
        return hastaalerjisi;
    }

    public void setHastaalerjisi(String hastaalerjisi) {
        this.hastaalerjisi = hastaalerjisi;
    }

    public String getTxttehlikename() {
        return txttehlikename;
    }

    public void setTxttehlikename(String txttehlikename) {
        this.txttehlikename = txttehlikename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
