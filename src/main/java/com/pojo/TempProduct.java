package com.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "temp_product")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class TempProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    String name;
    String sizex;
    String sizey;
    String sizez;
    String strainX;
    String strainY;
    String nX;
    String nY;
    String elecX;
    String elecY;
    String elecZ;
    String dataType;
    String Doi;
    String SupplmentaryInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSizex() {
        return sizex;
    }

    public void setSizex(String sizex) {
        this.sizex = sizex;
    }

    public String getSizey() {
        return sizey;
    }

    public void setSizey(String sizey) {
        this.sizey = sizey;
    }

    public String getSizez() {
        return sizez;
    }

    public void setSizez(String sizez) {
        this.sizez = sizez;
    }

    public String getStrainX() {
        return strainX;
    }

    public void setStrainX(String strainX) {
        this.strainX = strainX;
    }

    public String getStrainY() {
        return strainY;
    }

    public void setStrainY(String strainY) {
        this.strainY = strainY;
    }

    public String getnX() {
        return nX;
    }

    public void setnX(String nX) {
        this.nX = nX;
    }

    public String getnY() {
        return nY;
    }

    public void setnY(String nY) {
        this.nY = nY;
    }

    public String getElecX() {
        return elecX;
    }

    public void setElecX(String elecX) {
        this.elecX = elecX;
    }

    public String getElecY() {
        return elecY;
    }

    public void setElecY(String elecY) {
        this.elecY = elecY;
    }

    public String getElecZ() {
        return elecZ;
    }

    public void setElecZ(String elecZ) {
        this.elecZ = elecZ;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDoi() {
        return Doi;
    }

    public void setDoi(String doi) {
        Doi = doi;
    }

    public String getSupplmentaryInfo() {
        return SupplmentaryInfo;
    }

    public void setSupplmentaryInfo(String supplmentaryInfo) {
        SupplmentaryInfo = supplmentaryInfo;
    }

}
