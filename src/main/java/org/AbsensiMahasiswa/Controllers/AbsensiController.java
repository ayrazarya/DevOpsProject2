package org.AbsensiMahasiswa.Controllers;

import org.AbsensiMahasiswa.Models.*;
import org.AbsensiMahasiswa.Utils.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbsensiController {
    private final DatabaseHelper databaseHelper;

    public AbsensiController(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // Method untuk insert absensi
    public void insertAbsensi(Absensi absensi) {
        String query = "INSERT INTO absensi (id_mahasiswa, tanggal, status, id_admin) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, absensi.getIdMahasiswa());
            // Convert java.util.Date to java.sql.Date
            preparedStatement.setDate(2, new java.sql.Date(absensi.getTanggal().getTime()));
            preparedStatement.setString(3, absensi.getStatus());
            preparedStatement.setInt(4, absensi.getIdAdmin());

            preparedStatement.executeUpdate();
            System.out.println("Absensi berhasil disimpan!");
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("Error: Mahasiswa atau Admin tidak ditemukan.");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Method untuk mendapatkan semua kelas
    public List<Kelas> getAllKelas() {
        List<Kelas> kelasList = new ArrayList<>();
        String query = "SELECT id_kelas, kelas FROM kelas";

        try (Connection connection = databaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Kelas kelas = new Kelas(resultSet.getInt("id_kelas"), resultSet.getString("kelas"));
                kelasList.add(kelas);
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi kesalahan: " + ex.getMessage());
        }
        return kelasList;
    }


    // Method untuk mendapatkan mahasiswa berdasarkan kelas
    public List<Mahasiswa> getMahasiswaByKelas(int idKelas) {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String query = """
            SELECT m.id_mahasiswa, m.nama, m.nim, k.kelas, m.status
            FROM mahasiswa m
            JOIN kelas k ON m.id_kelas = k.id_kelas
            WHERE m.id_kelas = ? AND m.status = 'Aktif';
            """;

        try (Connection connection = databaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idKelas);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Mahasiswa mahasiswa = new Mahasiswa();
                    mahasiswa.setIdMahasiswa(resultSet.getInt("id_mahasiswa"));
                    mahasiswa.setNama(resultSet.getString("nama"));
                    mahasiswa.setNim(resultSet.getString("nim"));
                    mahasiswa.setStatus(resultSet.getString("status"));
                    mahasiswa.setKelas(new Kelas(resultSet.getString("kelas")));
                    mahasiswaList.add(mahasiswa);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi kesalahan: " + ex.getMessage());
        }
        return mahasiswaList;
    }


    // Method untuk mendapatkan mahasiswa berdasarkan NIM
    public Mahasiswa getMahasiswaByNIM(String nim) {
        String query = """
                SELECT m.nama, m.nim, k.kelas, m.status
                FROM mahasiswa m
                JOIN kelas k ON m.id_kelas = k.id_kelas
                WHERE m.nim = ?;
                """;

        try (Connection connection = databaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, nim);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Mahasiswa mahasiswa = new Mahasiswa();
                    mahasiswa.setNama(resultSet.getString("nama"));
                    mahasiswa.setNim(resultSet.getString("nim"));
                    mahasiswa.setStatus(resultSet.getString("status"));
                    mahasiswa.setKelas(new Kelas(resultSet.getString("kelas")));
                    return mahasiswa;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi kesalahan: " + ex.getMessage());
        }
        return null;
    }

    // Method untuk mendapatkan rekap absensi
    public List<Absensi> getRekapAbsensiByTanggal(int idKelas, java.util.Date tanggal) {
        List<Absensi> absensiList = new ArrayList<>();
        String query = """
        SELECT a.id_mahasiswa, m.nama, m.nim, a.status
        FROM absensi a
        JOIN mahasiswa m ON a.id_mahasiswa = m.id_mahasiswa
        JOIN kelas k ON m.id_kelas = k.id_kelas
        WHERE k.id_kelas = ? AND a.tanggal = ?;
        """;

        try (Connection connection = databaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idKelas);
            preparedStatement.setDate(2, new java.sql.Date(tanggal.getTime()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Absensi absensi = new Absensi();
                    absensi.setIdMahasiswa(resultSet.getInt("id_mahasiswa"));
                    absensi.setTanggal(new java.util.Date(tanggal.getTime()));
                    absensi.setStatus(resultSet.getString("status"));

                    Mahasiswa mahasiswa = new Mahasiswa();
                    mahasiswa.setNama(resultSet.getString("nama"));
                    mahasiswa.setNim(resultSet.getString("nim"));

                    absensi.setMahasiswa(mahasiswa);
                    absensiList.add(absensi);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi kesalahan: " + ex.getMessage());
        }
        return absensiList;
    }


    // Method untuk update absensi
    public void updateAbsensi(Absensi absensi) {
        String query = """
                UPDATE absensi 
                SET status = ? 
                WHERE id_mahasiswa = ? AND tanggal = ?;
                """;

        try (Connection connection = databaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, absensi.getStatus());
            preparedStatement.setInt(2, absensi.getIdMahasiswa());
            // Convert java.util.Date to java.sql.Date
            preparedStatement.setDate(3, new java.sql.Date(absensi.getTanggal().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
