package org.AbsensiMahasiswa;


import org.AbsensiMahasiswa.Controllers.AbsensiController;
import org.AbsensiMahasiswa.Controllers.LoginController;
import org.AbsensiMahasiswa.Controllers.MahasiswaController;
import org.AbsensiMahasiswa.Utils.DatabaseHelper;
import org.AbsensiMahasiswa.Views.LoginView;

public class Main {
    public static void main(String[] args) {
         // Instance db helper
         DatabaseHelper databaseHelper = new DatabaseHelper("localhost", "absensi_mahasiswa", "root", "");

         // Instance mahasiswa controller
         MahasiswaController mahasiswaController = new MahasiswaController(databaseHelper);
 
         // Instance absensi controller
         AbsensiController absensiController = new AbsensiController(databaseHelper);
 
         // Instance login
         LoginController loginController = new LoginController(databaseHelper);
 
         // Instance login view x controller
         LoginView loginView = new LoginView(loginController, mahasiswaController, absensiController);
 
         // Menampilkan Login View
         loginView.show();
        
    }
}
