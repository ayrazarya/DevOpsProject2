package org.AbsensiMahasiswa.Models;

import java.util.List;

public class Kelas {
    private int idKelas;
    private String namaKelas;

    // Navigation Property
    private List<Mahasiswa> mahasiswas;

    // Constructor
    public Kelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }


    public Kelas(int idKelas, String kelas) {
        this.idKelas = idKelas;
        this.namaKelas = kelas;
    }


    // Getter dan Setter
    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public List<Mahasiswa> getMahasiswas() {
        return mahasiswas;
    }

    public void setMahasiswas(List<Mahasiswa> mahasiswas) {
        this.mahasiswas = mahasiswas;
    }
}
