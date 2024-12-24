package org.AbsensiMahasiswa.Views;

import org.AbsensiMahasiswa.Controllers.MahasiswaController;
import org.AbsensiMahasiswa.Models.Kelas;
import org.AbsensiMahasiswa.Models.Mahasiswa;

import java.util.List;
import java.util.Scanner;

public class MahasiswaView {
    private final MahasiswaController mahasiswaController;
    private final LoginView loginView;
    private final Scanner scanner = new Scanner(System.in);

    public MahasiswaView(MahasiswaController mahasiswaController, LoginView loginView) {
        this.mahasiswaController = mahasiswaController;
        this.loginView = loginView;
    }

    public void show() {
        boolean continueMenu = true;

        while (continueMenu) {
            System.out.println("\n=== Menu Mahasiswa ===");
            System.out.println("1. Tambah Mahasiswa");
            System.out.println("2. Nonaktifkan Mahasiswa");
            System.out.println("3. Update Data Mahasiswa");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih opsi (1/2/3/4): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    insertMahasiswa();
                    break;
                case "2":
                    deleteMahasiswa();
                    break;
                case "3":
                    updateMahasiswa();
                    break;
                case "4":
                    continueMenu = false;
                    break;
                default:
                    System.out.println("Ups pilihan tidak valid. Coba lagi.");
                    break;
            }
        }

        System.out.println("\nKembali ke menu utama...");
        loginView.showMainMenu();
    }

    private int selectKelas() {
        while (true) {
            List<Kelas> kelasList = mahasiswaController.getAllKelas();
            if (kelasList.isEmpty()) {
                System.out.println("\nTidak ada kelas yang tersedia.");
                System.out.print("Kembali ke menu mahasiswa? (y/n): ");
                if (!scanner.nextLine().equalsIgnoreCase("y")) {
                    return -1;
                }
                continue;
            }

            System.out.println("\n=== Daftar Kelas ===");
            for (int i = 0; i < kelasList.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, kelasList.get(i).getNamaKelas());
            }

            System.out.print("Pilih kelas (1-" + kelasList.size() + "): ");
            try {
                int pilihan = Integer.parseInt(scanner.nextLine());
                if (pilihan >= 1 && pilihan <= kelasList.size()) {
                    return kelasList.get(pilihan - 1).getIdKelas();
                }
            } catch (NumberFormatException e) {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private Mahasiswa selectMahasiswaByKelas(int idKelas) {
        List<Mahasiswa> mahasiswaList = mahasiswaController.getMahasiswaByKelas(idKelas);
        if (mahasiswaList.isEmpty()) {
            System.out.println("Tidak ada mahasiswa di kelas ini.");
            return null;
        }

        System.out.println("\n=== Daftar Mahasiswa ===");
        for (int i = 0; i < mahasiswaList.size(); i++) {
            System.out.printf("%d. %s - %s\n", i + 1, mahasiswaList.get(i).getNama(), mahasiswaList.get(i).getNim());
        }

        System.out.print("Pilih mahasiswa (1-" + mahasiswaList.size() + "): ");
        try {
            int pilihan = Integer.parseInt(scanner.nextLine());
            if (pilihan >= 1 && pilihan <= mahasiswaList.size()) {
                return mahasiswaList.get(pilihan - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Pilihan tidak valid.");
        }

        return null;
    }

    private void insertMahasiswa() {
        System.out.println("\n=== Masukkan Mahasiswa ===");

        System.out.print("Berapa banyak mahasiswa yang ingin dimasukkan? ");
        int jumlahMahasiswa;
        try {
            jumlahMahasiswa = Integer.parseInt(scanner.nextLine());
            if (jumlahMahasiswa <= 0) {
                System.out.println("Masukkan jumlah yang valid (lebih besar dari 0).");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Masukkan angka yang valid.");
            return;
        }

        for (int i = 0; i < jumlahMahasiswa; i++) {
            System.out.printf("\nMasukkan Mahasiswa %d:%n", i + 1);

            System.out.print("Masukkan Nama Mahasiswa: ");
            String nama = scanner.nextLine();

            System.out.print("Masukkan NIM Mahasiswa: ");
            String nim = scanner.nextLine();

            System.out.print("Masukkan Nama Kelas Mahasiswa: ");
            String kelas = scanner.nextLine();

            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setNama(nama);
            mahasiswa.setNim(nim);
            mahasiswa.setStatus("Aktif");
            mahasiswa.setKelas(new Kelas(kelas));

            mahasiswaController.insertMahasiswa(mahasiswa);
        }

        System.out.println("\nSemua mahasiswa berhasil ditambahkan!");
    }

    private void deleteMahasiswa() {
        while (true) {
            int idKelas = selectKelas();
            if (idKelas == -1) return;

            Mahasiswa mahasiswa = selectMahasiswaByKelas(idKelas);
            if (mahasiswa == null) return;

            System.out.printf("Apakah Anda yakin ingin menghapus mahasiswa %s dengan NIM %s? (y/n): ", mahasiswa.getNama(), mahasiswa.getNim());
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                boolean success = mahasiswaController.deleteMahasiswa(mahasiswa.getNim());
                System.out.println(success ? "Status mahasiswa berhasil diubah menjadi Tidak Aktif!" : "Gagal mengubah status mahasiswa.");
            } else {
                System.out.println("Penghapusan dibatalkan.");
            }

            System.out.print("Kembali ke menu mahasiswa? (y/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    private void updateMahasiswa() {
        while (true) {
            int idKelas = selectKelas();
            if (idKelas == -1) return;

            Mahasiswa mahasiswa = selectMahasiswaByKelas(idKelas);
            if (mahasiswa == null) return;

            System.out.println("\nMasukkan data baru (kosongkan jika tidak ingin mengubah):");

            System.out.printf("Nama (%s): ", mahasiswa.getNama());
            String namaBaru = scanner.nextLine();
            if (!namaBaru.isEmpty()) mahasiswa.setNama(namaBaru);

            System.out.printf("Kelas (%s): ", mahasiswa.getKelas().getNamaKelas());
            String kelasBaru = scanner.nextLine();
            if (!kelasBaru.isEmpty()) mahasiswa.setKelas(new Kelas(kelasBaru));

            boolean success = mahasiswaController.updateMahasiswa(mahasiswa);
            System.out.println(success ? "Data mahasiswa berhasil diperbarui!" : "Gagal memperbarui data mahasiswa.");

            System.out.print("Kembali ke menu mahasiswa? (y/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }
    }
}
