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
    String dataType;
    String sizeX;
    String sizeY;
    String sizeZ;
    String strainX;
    String strainY;
    String nx;
    String ny;
    String elecX;
    String elecY;
    String elecZ;
    String xy_Fig;
    String xz_Fig;
    String xyz_Fig;
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
        return nx;
    }

    public void setNX(String nx) {
        this.nx = nx;
    }

    public String getNY() {
        return ny;
    }

    public void setNY(String ny) {
        this.ny = ny;
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

    public String getXY_Fig() { return xy_Fig; }

    public void setXY_Fig(String xy_Fig) { this.xy_Fig = xy_Fig; }

    public String getXZ_Fig() { return xz_Fig; }

    public void setXZ_Fig(String xz_Fig) { this.xz_Fig = xz_Fig; }

    public String getXYZ_Fig() { return xyz_Fig; }

    public void setXYZ_Fig(String xyz_Fig) { this.xyz_Fig = xyz_Fig; }

    public String getData_File() { return data_File; }

    public void setData_File(String data_File) { this.data_File = data_File; }

}
