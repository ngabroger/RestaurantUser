package com.example.restaurantuser.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {
    private String nama;
    private String deskripsi;
    private String foto;
    private String harga;
    private String kode;
    private String kategori;
    private int times;
    private double score;
    private int numericCart;

    public FoodDomain() {
        // Diperlukan oleh Firebase untuk membuat objek dari data yang diambil dari database
    }


    public FoodDomain(String nama, String deskripsi, String foto, String harga, String kode, String kategori) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.foto = foto;
        this.harga = harga;
        this.kode = kode;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumericCart() {
        return numericCart;
    }

    public void setNumericCart(int numericCart) {
        this.numericCart = numericCart;
    }
}