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
    int dataType;
    float sizeX;
    float sizeY;
    float sizeZ;
    float strainX;
    float strainY;
    float nx;
    float ny;
    float elecX;
    float elecY;
    float elecZ;
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

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public float getSizeX() {
        return sizeX;
    }

    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public void setSizeY(float sizeY) {
        this.sizeY = sizeY;
    }

    public float getSizeZ() {
        return sizeZ;
    }

    public void setSizeZ(float sizeZ) {
        this.sizeZ = sizeZ;
    }

    public float getStrainX() {
        return strainX;
    }

    public void setStrainX(float strainX) {
        this.strainX = strainX;
    }

    public float getStrainY() {
        return strainY;
    }

    public void setStrainY(float strainY) {
        this.strainY = strainY;
    }

    public float getNX() {
        return this.nx;
    }

    public void setNX(float nx) {
        this.nx = nx;
    }

    public float getNY() {
        return this.ny;
    }

    public void setNY(float ny) {
        this.ny = ny;
    }

    public float getElecX() {
        return elecX;
    }

    public void setElecX(float elecX) {
        this.elecX = elecX;
    }

    public float getElecY() {
        return elecY;
    }

    public void setElecY(float elecY) {
        this.elecY = elecY;
    }

    public float getElecZ() {
        return elecZ;
    }

    public void setElecZ(float elecZ) {
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
