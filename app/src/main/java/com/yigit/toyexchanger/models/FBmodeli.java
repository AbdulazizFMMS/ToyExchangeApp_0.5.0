package com.yigit.toyexchanger.models;

public class FBmodeli {
    String resimlinki ;
    String ilanName ;
    String ilanDetay ;
    String telNum ;
    String konum ;
    String katagori ;



    public FBmodeli( String resimlinki, String ilanName ,String ilanDetay,String telNum, String konum, String katagori ){
        this.resimlinki = resimlinki;
        this.ilanName = ilanName;
        this.ilanDetay = ilanDetay;
        this.telNum = telNum;
        this.konum = konum;
        this.katagori = katagori;
    }
    public String getResimlinki() {
        return resimlinki;
    }

    public void setResimlinki(String resimlinki) {
        this.resimlinki = resimlinki;
    }

    public String getIlanName() {
        return ilanName;
    }

    public void setIlanName(String ilanName) {
        this.ilanName = ilanName;
    }

    public String getIlanDetay() {
        return ilanDetay;
    }

    public void setIlanDetay(String ilanDetay) {
        this.ilanDetay = ilanDetay;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getKatagori() {
        return katagori;
    }

    public void setKatagori(String katagori) {
        this.katagori = katagori;
    }





}
