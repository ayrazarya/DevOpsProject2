package org.AbsensiMahasiswa.Models;

import java.time.LocalDate;
import java.util.Date;

public class Absensi {
    private int idAbsensi;
    private int idMahasiswa;
    private int idAdmin;
    private Date tanggal;  // Use Date for storing the date
    private String status; // "Hadir", "Sakit", "Alfa"

    // Navigation Properties
    private Mahasiswa mahasiswa;
    private Admin admin;

    // Constructor
    public Absensi(int idMahasiswa, LocalDate date, String status, int idAdmin) {
        this.idMahasiswa = idMahasiswa;
        this.tanggal = java.sql.Date.valueOf(date);  // Convert LocalDate to java.sql.Date
        this.status = status;
        this.idAdmin = idAdmin;
    }
    public Absensi() {
        // No-arg constructor
    }

    // Getter and Setter m
    // ethods
    public int getIdAbsensi() {
        return idAbsensi;
    }

    public void setIdAbsensi(int idAbsensi) {
        this.idAbsensi = idAbsensi;
    }

    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
