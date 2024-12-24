package org.AbsensiMahasiswa.Controllers;

import org.AbsensiMahasiswa.Models.Kelas;
import org.AbsensiMahasiswa.Models.Mahasiswa;
import org.AbsensiMahasiswa.Utils.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaController {
    private DatabaseHelper databaseHelper;

    public MahasiswaController(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // Method to insert Mahasiswa
    public void insertMahasiswa(Mahasiswa mahasiswa) {
        try (Connection connection = databaseHelper.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Insert kelas if not exists
            String sqlInsertKelas = "INSERT INTO kelas (kelas) SELECT ? FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM kelas WHERE kelas = ?)";
            try (PreparedStatement cmdInsertKelas = connection.prepareStatement(sqlInsertKelas)) {
                cmdInsertKelas.setString(1, mahasiswa.getKelas().getNamaKelas());
                cmdInsertKelas.setString(2, mahasiswa.getKelas().getNamaKelas());
                cmdInsertKelas.executeUpdate();
            }

            // Get id_kelas from kelas table
            String sqlGetIdKelas = "SELECT id_kelas FROM kelas WHERE kelas = ?";
            int idKelas;
            try (PreparedStatement cmdGetIdKelas = connection.prepareStatement(sqlGetIdKelas)) {
                cmdGetIdKelas.setString(1, mahasiswa.getKelas().getNamaKelas());
                ResultSet rs = cmdGetIdKelas.executeQuery();
                if (rs.next()) {
                    idKelas = rs.getInt("id_kelas");
                } else {
                    throw new SQLException("Kelas not found.");
                }
            }

            // Insert mahasiswa
            String sqlInsertMahasiswa = "INSERT INTO mahasiswa (nama, nim, id_kelas, status) VALUES (?, ?, ?, 'Aktif')";
            try (PreparedStatement cmdInsertMahasiswa = connection.prepareStatement(sqlInsertMahasiswa)) {
                cmdInsertMahasiswa.setString(1, mahasiswa.getNama());
                cmdInsertMahasiswa.setString(2, mahasiswa.getNim());
                cmdInsertMahasiswa.setInt(3, idKelas);
                cmdInsertMahasiswa.executeUpdate();
            }

            connection.commit(); // Commit transaction
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Method to delete Mahasiswa by NIM
    public boolean deleteMahasiswa(String nim) {
        try (Connection connection = databaseHelper.getConnection()) {
            String sqlUpdateStatus = "UPDATE mahasiswa SET status = 'Tidak Aktif' WHERE nim = ?";
            try (PreparedStatement cmdUpdateStatus = connection.prepareStatement(sqlUpdateStatus)) {
                cmdUpdateStatus.setString(1, nim);
                int rowsAffected = cmdUpdateStatus.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    // Method to get Mahasiswa by NIM
    public Mahasiswa getMahasiswaByNIM(String nim) {
        try (Connection connection = databaseHelper.getConnection()) {
            String sqlGetMahasiswa = "SELECT m.nama, m.nim, k.kelas, m.status FROM mahasiswa m JOIN kelas k ON m.id_kelas = k.id_kelas WHERE m.nim = ?";
            try (PreparedStatement cmdGetMahasiswa = connection.prepareStatement(sqlGetMahasiswa)) {
                cmdGetMahasiswa.setString(1, nim);
                ResultSet rs = cmdGetMahasiswa.executeQuery();
                if (rs.next()) {
                    return new Mahasiswa(
                            rs.getString("nama"),
                            rs.getString("nim"),
                            rs.getString("status"),
                            new Kelas(rs.getString("kelas"))
                    );
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    // Method to update Mahasiswa
    public boolean updateMahasiswa(Mahasiswa mahasiswa) {
        try (Connection connection = databaseHelper.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Insert kelas if not exists
            String sqlInsertKelas = "INSERT INTO kelas (kelas) SELECT ? FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM kelas WHERE kelas = ?)";
            try (PreparedStatement cmdInsertKelas = connection.prepareStatement(sqlInsertKelas)) {
                cmdInsertKelas.setString(1, mahasiswa.getKelas().getNamaKelas());
                cmdInsertKelas.setString(2, mahasiswa.getKelas().getNamaKelas());
                cmdInsertKelas.executeUpdate();
            }

            // Get id_kelas from kelas table
            String sqlGetIdKelas = "SELECT id_kelas FROM kelas WHERE kelas = ?";
            int idKelas;
            try (PreparedStatement cmdGetIdKelas = connection.prepareStatement(sqlGetIdKelas)) {
                cmdGetIdKelas.setString(1, mahasiswa.getKelas().getNamaKelas());
                ResultSet rs = cmdGetIdKelas.executeQuery();
                if (rs.next()) {
                    idKelas = rs.getInt("id_kelas");
                } else {
                    throw new SQLException("Kelas not found.");
                }
            }

            // Update mahasiswa
            String sqlUpdateMahasiswa = "UPDATE mahasiswa SET nama = ?, id_kelas = ? WHERE nim = ?";
            try (PreparedStatement cmdUpdateMahasiswa = connection.prepareStatement(sqlUpdateMahasiswa)) {
                cmdUpdateMahasiswa.setString(1, mahasiswa.getNama());
                cmdUpdateMahasiswa.setInt(2, idKelas);
                cmdUpdateMahasiswa.setString(3, mahasiswa.getNim());
                int rowsAffected = cmdUpdateMahasiswa.executeUpdate();
                connection.commit(); // Commit transaction
                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    // Method to get all Kelas
    public List<Kelas> getAllKelas() {
        List<Kelas> kelasList = new ArrayList<>();
        try (Connection connection = databaseHelper.getConnection()) {
            String sqlGetAllKelas = "SELECT id_kelas, kelas FROM kelas";
            try (PreparedStatement cmd = connection.prepareStatement(sqlGetAllKelas)) {
                ResultSet rs = cmd.executeQuery();
                while (rs.next()) {
                    kelasList.add(new Kelas(
                            rs.getInt("id_kelas"),
                            rs.getString("kelas")
                    ));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return kelasList;
    }

    // Method to get Mahasiswa by Kelas ID
    public List<Mahasiswa> getMahasiswaByKelas(int idKelas) {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        try (Connection connection = databaseHelper.getConnection()) {
            String sqlGetMahasiswa = "SELECT m.nama, m.nim, k.kelas, m.status FROM mahasiswa m JOIN kelas k ON m.id_kelas = k.id_kelas WHERE m.id_kelas = ? AND m.status = 'Aktif'";
            try (PreparedStatement cmdGetMahasiswa = connection.prepareStatement(sqlGetMahasiswa)) {
                cmdGetMahasiswa.setInt(1, idKelas);
                ResultSet rs = cmdGetMahasiswa.executeQuery();
                while (rs.next()) {
                    mahasiswaList.add(new Mahasiswa(
                            rs.getString("nama"),
                            rs.getString("nim"),
                            rs.getString("status"),
                            new Kelas(rs.getString("kelas"))
                    ));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return mahasiswaList;
    }
}
