package org.AbsensiMahasiswa.Views;



import org.AbsensiMahasiswa.Controllers.AbsensiController;
import org.AbsensiMahasiswa.Controllers.MahasiswaController;
import org.AbsensiMahasiswa.Models.Absensi;
import org.AbsensiMahasiswa.Models.Kelas;
import org.AbsensiMahasiswa.Models.Mahasiswa;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class AbsensiView {

    private final AbsensiController absensiController;
    private final MahasiswaController mahasiswaController;
    private final int idAdmin;

    public AbsensiView(AbsensiController absensiController, MahasiswaController mahasiswaController, int idAdmin) {
        this.absensiController = absensiController;
        this.mahasiswaController = mahasiswaController;
        this.idAdmin = idAdmin;
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Menu Absensi ===");
            System.out.println("1. Lakukan Absensi");
            System.out.println("2. Rekap Absensi Berdasarkan Tanggal");
            System.out.println("3. Edit Absensi");
            System.out.println("4. Kembali");
            System.out.print("Pilih menu: ");

            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    lakukanAbsensi();
                    break;
                case "2":
                    rekapAbsensi();
                    break;
                case "3":
                    editAbsensi();
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Tekan Enter untuk mencoba lagi.");
                    scanner.nextLine();
            }
        }
    }

    private void lakukanAbsensi() {
        Scanner scanner = new Scanner(System.in);
        List<Kelas> kelasList = absensiController.getAllKelas();

        if (kelasList == null || kelasList.isEmpty()) {
            System.out.println("Tidak ada data kelas.");
            return;
        }

        System.out.println("\n=== Daftar Kelas ===");
        for (int i = 0; i < kelasList.size(); i++) {
            System.out.println((i + 1) + ". " + kelasList.get(i).getNamaKelas());
        }

        System.out.print("Pilih kelas (masukkan angka): ");
        int kelasIndex;
        try {
            kelasIndex = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Pilihan tidak valid.");
            return;
        }

        if (kelasIndex < 0 || kelasIndex >= kelasList.size()) {
            System.out.println("Pilihan tidak valid.");
            return;
        }

        Kelas selectedKelas = kelasList.get(kelasIndex);
        System.out.println("Anda memilih kelas: " + selectedKelas.getNamaKelas());

        List<Mahasiswa> mahasiswaList = absensiController.getMahasiswaByKelas(selectedKelas.getIdKelas());
        if (mahasiswaList == null || mahasiswaList.isEmpty()) {
            System.out.println("Tidak ada mahasiswa dalam kelas ini.");
            return;
        }

        for (Mahasiswa mahasiswa : mahasiswaList) {
            System.out.println("Mahasiswa: " + mahasiswa.getNama() + " (" + mahasiswa.getNim() + ")");
            System.out.print("Masukkan status kehadiran (Hadir/Sakit/Alfa): ");
            String status = scanner.nextLine();

            if (!status.equalsIgnoreCase("Hadir") && !status.equalsIgnoreCase("Sakit") && !status.equalsIgnoreCase("Alfa")) {
                System.out.println("Status tidak valid. Mengabaikan absensi untuk mahasiswa ini.");
                continue;
            }

            Absensi absensi = new Absensi(mahasiswa.getIdMahasiswa(), LocalDate.now(), status, idAdmin);
            absensiController.insertAbsensi(absensi);
            System.out.println("Absensi untuk " + mahasiswa.getNama() + " berhasil disimpan!");
        }
    }

    private void rekapAbsensi() {
        Scanner scanner = new Scanner(System.in);

        List<Kelas> kelasList = absensiController.getAllKelas();

        if (kelasList == null || kelasList.isEmpty()) {
            System.out.println("Tidak ada data kelas.");
            return;
        }

        System.out.println("\n=== Daftar Kelas ===");
        for (int i = 0; i < kelasList.size(); i++) {
            System.out.println((i + 1) + ". " + kelasList.get(i).getNamaKelas());
        }

        System.out.print("Pilih kelas (masukkan angka): ");
        int kelasIndex;
        try {
            kelasIndex = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Pilihan tidak valid.");
            return;
        }

        if (kelasIndex < 0 || kelasIndex >= kelasList.size()) {
            System.out.println("Pilihan tidak valid.");
            return;
        }

        Kelas selectedKelas = kelasList.get(kelasIndex);
        System.out.println("Anda memilih kelas: " + selectedKelas.getNamaKelas());

        System.out.print("Masukkan tanggal (yyyy-MM-dd): ");
        LocalDate tanggal;
        try {
            tanggal = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Format tanggal tidak valid.");
            return;
        }

        // Convert LocalDate to java.util.Date
        java.util.Date utilDate = java.sql.Date.valueOf(tanggal);

        List<Absensi> rekapAbsensi = absensiController.getRekapAbsensiByTanggal(selectedKelas.getIdKelas(), utilDate);

        if (rekapAbsensi == null || rekapAbsensi.isEmpty()) {
            System.out.println("Tidak ada absensi pada tanggal ini.");
            return;
        }

        System.out.println("\n=== Rekap Absensi ===");
        for (Absensi absensi : rekapAbsensi) {
            System.out.println(absensi.getMahasiswa().getNama() + " (" + absensi.getMahasiswa().getNim() + ") - Status: " + absensi.getStatus());
        }
    }


    private void editAbsensi() {
        // Implementasi mirip dengan fungsi rekapAbsensi dengan tambahan untuk edit status absensi.
    }
}