package org.AbsensiMahasiswa.Views;

import org.AbsensiMahasiswa.Controllers.AbsensiController;
import org.AbsensiMahasiswa.Controllers.LoginController;
import org.AbsensiMahasiswa.Controllers.MahasiswaController;
import java.util.Scanner;

public class LoginView {
    private LoginController loginController;
    private MahasiswaController mahasiswaController;
    private AbsensiController absensiController;
    private int idAdmin; // Untuk menyimpan ID admin yang login

    public LoginView(LoginController loginController, MahasiswaController mahasiswaController, AbsensiController absensiController) {
        // Inisialisasi controller
        this.loginController = loginController;
        this.mahasiswaController = mahasiswaController;
        this.absensiController = absensiController;
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        boolean loggedIn = false;

        while (!exit) {
            clearConsole();

            if (loggedIn) {
                // Menu jika sudah login
                showMainMenu();
                String pilihan = scanner.nextLine();

                switch (pilihan) {
                    case "1":
                        // Navigasi ke MahasiswaView
                        MahasiswaView mahasiswaView = new MahasiswaView(mahasiswaController, this);
                        mahasiswaView.show();
                        break;

                    case "2":
                        // Navigasi ke AbsensiView
                        AbsensiView absensiView = new AbsensiView(absensiController, mahasiswaController, idAdmin);
                        absensiView.show();
                        break;

                    case "3":
                        System.out.println("Exit...");
                        exit = true;
                        break;

                    default:
                        System.out.println("Opsi tidak valid. Silakan coba lagi.");
                        break;
                }
            } else {
                // Menu jika belum login
                System.out.println("=== Selamat datang di Aplikasi Absensi ===");
                System.out.println("1. Login");
                System.out.println("2. Exit");
                System.out.print("Pilih opsi (1/2): ");
                String pilihan = scanner.nextLine();

                switch (pilihan) {
                    case "1":
                        int idAdmin = login();
                        if (idAdmin != -1) {
                            loggedIn = true;
                            this.idAdmin = idAdmin; // Simpan ID admin yang login
                        }
                        break;

                    case "2":
                        exit = true;
                        System.out.println("Terima kasih telah menggunakan aplikasi. Keluar...");
                        break;

                    default:
                        System.out.println("Opsi tidak valid, coba lagi.");
                        break;
                }
            }
        }
        scanner.close();
    }

    public void showMainMenu() {
        System.out.println("=== Menu Utama ===");
        System.out.println("1. Mahasiswa");
        System.out.println("2. Absensi");
        System.out.println("3. Exit");
        System.out.print("Pilih opsi (1/2/3): ");
    }

    private int login() {
        Scanner scanner = new Scanner(System.in);
        int maxAttempts = 3;
        int attemptCount = 0;

        while (attemptCount < maxAttempts) {
            clearConsole();
            System.out.println("=== Login Page ===");
            System.out.print("Masukkan Username: ");
            String username = scanner.nextLine();
            System.out.print("Masukkan Password: ");
            String password = scanner.nextLine();

            // Autentikasi admin
            int adminId = loginController.authenticate(username, password);

            if (adminId != -1) { // Login sukses
                System.out.println("Login Berhasil!");
                return adminId; // Return admin ID
            } else {
                attemptCount++;
                System.out.println("Login Gagal! Username atau Password salah. Percobaan ke-" + attemptCount + "/" + maxAttempts);

                if (attemptCount == maxAttempts) {
                    System.out.println("Terlalu banyak percobaan login gagal. Keluar aplikasi.");
                    System.exit(0); // 3x gagal exit
                }
            }

            System.out.println("Tekan Enter untuk mencoba lagi...");
            scanner.nextLine();
        }

        return -1;
    }

    private void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Tidak dapat membersihkan konsol.");
        }
    }
}
