package com.yigit.toyexchanger.models;

public class dataholder {
    String tel;
    String mesaj;
    String name;
    private boolean selected; // Seçili durumu takip eder

    dataholder()
    {

    }

    public dataholder(String name, String tel, String mesaj) {
        this.name = name;
        this.tel = tel;
        this.mesaj = mesaj;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter ve Setter
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
