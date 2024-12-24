package org.AbsensiMahasiswa.Models;

import java.util.List;

public class Mahasiswa {
    private int idMahasiswa;
    private String nama;
    private String nim;
    private int idKelas;
    private String status;


    // Navigation Property
    private Kelas kelas;
    private List<Absensi> absensi;

    public Mahasiswa(String nama, String nim, String status, Kelas kelas) {
        this.nama = nama;
        this.nim = nim;
        this.status = status;
        this.kelas = kelas;
    }

    public Mahasiswa() {
        // No-arg constructor
    }
    // Getter dan Setter
    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public List<Absensi> getAbsensi() {
        return absensi;
    }

    public void setAbsensi(List<Absensi> absensi) {
        this.absensi = absensi;
    }

   
    
}
