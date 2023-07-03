package com.example.restaurantuser.Domain;

public class UserDomain {
    private String userId;
    private String alamat;
    private String tanggalLahir;

    public UserDomain() {
        // Diperlukan konstruktor kosong untuk deserialisasi Firebase
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}
