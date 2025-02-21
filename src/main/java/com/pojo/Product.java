package com.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    String name;
    String dataType;
    String sizeX;
    String sizeY;
    String sizeZ;
    String strainX;
    String strainY;
    String nX;
    String nY;
    String elecX;
    String elecY;
    String elecZ;
    String xY_Fig;
    String xZ_Fig;
    String xYZ_Fig;
    String data_File;

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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSizeX() {
        return sizeX;
    }

    public void setSizeX(String sizeX) {
        this.sizeX = sizeX;
    }

    public String getSizeY() {
        return sizeY;
    }

    public void setSizeY(String sizeY) {
        this.sizeY = sizeY;
    }

    public String getSizeZ() {
        return sizeZ;
    }

    public void setSizeZ(String sizeZ) {
        this.sizeZ = sizeZ;
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

    public String getNX() {
        return nX;
    }

    public void setNX(String nX) {
        this.nX = nX;
    }

    public String getNY() {
        return nY;
    }

    public void setNY(String nY) {
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

    public String getXY_Fig() { return xY_Fig; }

    public void setXY_Fig(String xY_Fig) { this.xY_Fig = xY_Fig; }

    public String getXZ_Fig() { return xZ_Fig; }

    public void setXZ_Fig(String xZ_Fig) { this.xZ_Fig = xZ_Fig; }

    public String getXYZ_Fig() { return xYZ_Fig; }

    public void setXYZ_Fig(String xYZ_Fig) { this.xYZ_Fig = xYZ_Fig; }

    public String getData_File() { return data_File; }

    public void setData_File(String data_File) { this.data_File = data_File; }

}
