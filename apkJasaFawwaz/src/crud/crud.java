/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.sql.ResultSetMetaData;
/**
 *
 * @author User
 */
    public class crud {
    private Connection Koneksidb;
    private String username="root";
    private String password="";
    private String dbname="db_jasa"; 
    private String urlKoneksi="jdbc:mysql://localhost/"+dbname;
    public boolean duplikasi=false;

    public String CEK_NAMABARANG, CEK_JENISBARANG, CEK_DESKRIPSI, CEK_STOCK, CEK_SATUAN = null;
    public String CEK_IDBARANG_MASUK, CEK_TANGGAL_MASUK, CEK_NOTA_MASUK, CEK_KETERANGAN_MASUK, CEK_QTY_MASUK = null;
    public String CEK_IDBARANG_KELUAR, CEK_TANGGAL_KELUAR, CEK_PENERIMA_KELUAR, CEK_KEPERLUAN_KELUAR, CEK_QTY_KELUAR = null;
    public String CEK_IDBARANG_PINJAM, CEK_TANGGALPINJAM, CEK_QTY_PINJAM, CEK_PEMINJAM, CEK_KEPERLUAN_PINJAM, CEK_STATUS_PINJAM, CEK_JUMLAHKEMBALI, CEK_TANGGALKEMBALI = null;

    
    public crud(){
        try {
            Driver dbdriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(dbdriver);
            Koneksidb=DriverManager.getConnection(urlKoneksi,username,password);
            System.out.print("Database Terkoneksi");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }
    
   public void simpanStock01(String idbarang, String namabarang, String jenisbarang, String deskripsi, String stock, String satuan){
        try {
            String sqlsimpan="insert into stock(idbarang, namabarang, jenisbarang, deskripsi, stock, satuan) value"
                    + " ('"+idbarang+"', '"+namabarang+"', '"+jenisbarang+"', '"+deskripsi+"', '"+stock+"', '"+satuan+"')";
            String sqlcari="select*from stock where idbarang='"+idbarang+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Barang sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Stock berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void simpanStock02(String idbarang, String namabarang, String jenisbarang, String deskripsi, String stock, String satuan){
        try {
            String sqlsimpan="INSERT INTO stock (idbarang, namabarang, jenisbarang, deskripsi, stock, satuan) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM stock WHERE idbarang = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, idbarang);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Barang sudah terdaftar");
                this.duplikasi = true;
                this.CEK_NAMABARANG = data.getString("namabarang");
                this.CEK_JENISBARANG = data.getString("jenisbarang");
                this.CEK_DESKRIPSI = data.getString("deskripsi");
                this.CEK_STOCK = data.getString("stock");
                this.CEK_SATUAN = data.getString("satuan");
            } else {
                this.duplikasi = false;
                this.CEK_NAMABARANG = null;
                this.CEK_JENISBARANG = null;
                this.CEK_DESKRIPSI = null;
                this.CEK_STOCK = null;
                this.CEK_SATUAN = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, idbarang);
                perintah.setString(2, namabarang);
                perintah.setString(3, jenisbarang);
                perintah.setString(4, deskripsi);
                perintah.setString(5, stock);
                perintah.setString(6, satuan);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Stock berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahStock(String idbarang, String namabarang, String jenisbarang, String deskripsi, String stock, String satuan){
        try {
            String sqlubah="UPDATE stock SET namabarang = ?, jenisbarang = ?, deskripsi = ?, stock = ?, satuan = ? WHERE idbarang = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, namabarang);
            perintah.setString(2, jenisbarang);
            perintah.setString(3, deskripsi);
            perintah.setString(4, stock);
            perintah.setString(5, satuan);
            perintah.setString(6, idbarang); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Stock berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusStock(String idbarang){
        try {
            String sqlhapus="DELETE FROM stock WHERE idbarang = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, idbarang);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Stock berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataStock(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Barang");
            modeltabel.addColumn("Nama Barang");
            modeltabel.addColumn("Jenis Barang");
            modeltabel.addColumn("Deskripsi");
            modeltabel.addColumn("Stock");
            modeltabel.addColumn("Satuan");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }

    public void simpanMasuk01(String idmasuk, String idbarang, String tanggal, String nota, String keterangan, String qty){
        try {
            String sqlsimpan="insert into masuk(idmasuk, idbarang, tanggal, nota, keterangan, qty) value"
                    + " ('"+idmasuk+"', '"+idbarang+"', '"+tanggal+"', '"+nota+"', '"+keterangan+"', '"+qty+"')";
            String sqlcari="select*from masuk where idmasuk='"+idmasuk+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Masuk sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Masuk berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void simpanMasuk02(String idmasuk, String idbarang, String tanggal, String nota, String keterangan, String qty){
        try {
            String sqlsimpan="INSERT INTO masuk (idmasuk, idbarang, tanggal, nota, keterangan, qty) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM masuk WHERE idmasuk = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, idmasuk);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Masuk sudah terdaftar");
                this.duplikasi = true;
                this.CEK_IDBARANG_MASUK = data.getString("idbarang");
                this.CEK_TANGGAL_MASUK = data.getString("tanggal");
                this.CEK_NOTA_MASUK = data.getString("nota");
                this.CEK_KETERANGAN_MASUK = data.getString("keterangan");
                this.CEK_QTY_MASUK = data.getString("qty");
            } else {
                this.duplikasi = false;
                this.CEK_IDBARANG_MASUK = null;
                this.CEK_TANGGAL_MASUK = null;
                this.CEK_NOTA_MASUK = null;
                this.CEK_KETERANGAN_MASUK = null;
                this.CEK_QTY_MASUK = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, idmasuk);
                perintah.setString(2, idbarang);
                perintah.setString(3, tanggal);
                perintah.setString(4, nota);
                perintah.setString(5, keterangan);
                perintah.setString(6, qty);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Masuk berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahMasuk(String idmasuk, String idbarang, String tanggal, String nota, String keterangan, String qty){
        try {
            String sqlubah="UPDATE masuk SET idbarang = ?, tanggal = ?, nota = ?, keterangan = ?, qty = ? WHERE idmasuk = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, idbarang);
            perintah.setString(2, tanggal);
            perintah.setString(3, nota);
            perintah.setString(4, keterangan);
            perintah.setString(5, qty);
            perintah.setString(6, idmasuk); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Masuk berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusMasuk(String idmasuk){
        try {
            String sqlhapus="DELETE FROM masuk WHERE idmasuk = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, idmasuk);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Masuk berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataMasuk(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Masuk");
            modeltabel.addColumn("ID Barang");
            modeltabel.addColumn("Tanggal");
            modeltabel.addColumn("Nota");
            modeltabel.addColumn("Keterangan");
            modeltabel.addColumn("Qty");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void simpanKeluar01(String idkeluar, String idbarang, String tanggal, String penerima, String keperluan, String qty){
        try {
            String sqlsimpan="insert into keluar(idkeluar, idbarang, tanggal, penerima, keperluan, qty) value"
                    + " ('"+idkeluar+"', '"+idbarang+"', '"+tanggal+"', '"+penerima+"', '"+keperluan+"', '"+qty+"')";
            String sqlcari="select*from keluar where idkeluar='"+idkeluar+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Keluar sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Keluar berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void simpanKeluar02(String idkeluar, String idbarang, String tanggal, String penerima, String keperluan, String qty){
        try {
            String sqlsimpan="INSERT INTO keluar (idkeluar, idbarang, tanggal, penerima, keperluan, qty) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM keluar WHERE idkeluar = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, idkeluar);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Keluar sudah terdaftar");
                this.duplikasi = true;
                this.CEK_IDBARANG_KELUAR = data.getString("idbarang");
                this.CEK_TANGGAL_KELUAR = data.getString("tanggal");
                this.CEK_PENERIMA_KELUAR = data.getString("penerima");
                this.CEK_KEPERLUAN_KELUAR = data.getString("keperluan");
                this.CEK_QTY_KELUAR = data.getString("qty");
            } else {
                this.duplikasi = false;
                this.CEK_IDBARANG_KELUAR = null;
                this.CEK_TANGGAL_KELUAR = null;
                this.CEK_PENERIMA_KELUAR = null;
                this.CEK_KEPERLUAN_KELUAR = null;
                this.CEK_QTY_KELUAR = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, idkeluar);
                perintah.setString(2, idbarang);
                perintah.setString(3, tanggal);
                perintah.setString(4, penerima);
                perintah.setString(5, keperluan);
                perintah.setString(6, qty);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Keluar berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahKeluar(String idkeluar, String idbarang, String tanggal, String penerima, String keperluan, String qty){
        try {
            String sqlubah="UPDATE keluar SET idbarang = ?, tanggal = ?, penerima = ?, keperluan = ?, qty = ? WHERE idkeluar = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, idbarang);
            perintah.setString(2, tanggal);
            perintah.setString(3, penerima);
            perintah.setString(4, keperluan);
            perintah.setString(5, qty);
            perintah.setString(6, idkeluar); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Keluar berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusKeluar(String idkeluar){
        try {
            String sqlhapus="DELETE FROM keluar WHERE idkeluar = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, idkeluar);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Keluar berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataKeluar(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Keluar");
            modeltabel.addColumn("ID Barang");
            modeltabel.addColumn("Tanggal");
            modeltabel.addColumn("Penerima");
            modeltabel.addColumn("Keperluan");
            modeltabel.addColumn("Qty");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void simpanPeminjaman01(String idpeminjaman, String idbarang, String tanggalpinjam, String qty, String peminjam, String keperluan, String status, String jumlahkembali, String tanggalkembali){
        try {
            String sqlsimpan="insert into peminjaman(idpeminjaman, idbarang, tanggalpinjam, qty, peminjam, keperluan, status, jumlahkembali, tanggalkembali) value"
                    + " ('"+idpeminjaman+"', '"+idbarang+"', '"+tanggalpinjam+"', '"+qty+"', '"+peminjam+"', '"+keperluan+"', '"+status+"', '"+jumlahkembali+"', '"+tanggalkembali+"')";
            String sqlcari="select*from peminjaman where idpeminjaman='"+idpeminjaman+"'";
            
            Statement cari=Koneksidb.createStatement();
            ResultSet data=cari.executeQuery(sqlcari);
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Peminjaman sudah terdaftar");
            } else {
                Statement perintah=Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Peminjaman berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    // Pola 02 (Aman)
    public void simpanPeminjaman02(String idpeminjaman, String idbarang, String tanggalpinjam, String qty, String peminjam, String keperluan, String status, String jumlahkembali, String tanggalkembali){
        try {
            String sqlsimpan="INSERT INTO peminjaman (idpeminjaman, idbarang, tanggalpinjam, qty, peminjam, keperluan, status, jumlahkembali, tanggalkembali) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM peminjaman WHERE idpeminjaman = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, idpeminjaman);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Peminjaman sudah terdaftar");
                this.duplikasi = true;
                this.CEK_IDBARANG_PINJAM = data.getString("idbarang");
                this.CEK_TANGGALPINJAM = data.getString("tanggalpinjam");
                this.CEK_QTY_PINJAM = data.getString("qty");
                this.CEK_PEMINJAM = data.getString("peminjam");
                this.CEK_KEPERLUAN_PINJAM = data.getString("keperluan");
                this.CEK_STATUS_PINJAM = data.getString("status");
                this.CEK_JUMLAHKEMBALI = data.getString("jumlahkembali");
                this.CEK_TANGGALKEMBALI = data.getString("tanggalkembali");
            } else {
                this.duplikasi = false;
                this.CEK_IDBARANG_PINJAM = null;
                this.CEK_TANGGALPINJAM = null;
                this.CEK_QTY_PINJAM = null;
                this.CEK_PEMINJAM = null;
                this.CEK_KEPERLUAN_PINJAM = null;
                this.CEK_STATUS_PINJAM = null;
                this.CEK_JUMLAHKEMBALI = null;
                this.CEK_TANGGALKEMBALI = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, idpeminjaman);
                perintah.setString(2, idbarang);
                perintah.setString(3, tanggalpinjam);
                perintah.setString(4, qty);
                perintah.setString(5, peminjam);
                perintah.setString(6, keperluan);
                perintah.setString(7, status);
                perintah.setString(8, jumlahkembali);
                perintah.setString(9, tanggalkembali);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Peminjaman berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahPeminjaman(String idpeminjaman, String idbarang, String tanggalpinjam, String qty, String peminjam, String keperluan, String status, String jumlahkembali, String tanggalkembali){
        try {
            String sqlubah="UPDATE peminjaman SET idbarang = ?, tanggalpinjam = ?, qty = ?, peminjam = ?, keperluan = ?, status = ?, jumlahkembali = ?, tanggalkembali = ? WHERE idpeminjaman = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, idbarang);
            perintah.setString(2, tanggalpinjam);
            perintah.setString(3, qty);
            perintah.setString(4, peminjam);
            perintah.setString(5, keperluan);
            perintah.setString(6, status);
            perintah.setString(7, jumlahkembali);
            perintah.setString(8, tanggalkembali);
            perintah.setString(9, idpeminjaman); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Peminjaman berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusPeminjaman(String idpeminjaman){
        try {
            String sqlhapus="DELETE FROM peminjaman WHERE idpeminjaman = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, idpeminjaman);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Peminjaman berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataPeminjaman(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID Peminjaman");
            modeltabel.addColumn("ID Barang");
            modeltabel.addColumn("Tgl Pinjam");
            modeltabel.addColumn("Qty");
            modeltabel.addColumn("Peminjam");
            modeltabel.addColumn("Keperluan");
            modeltabel.addColumn("Status");
            modeltabel.addColumn("Jml Kembali");
            modeltabel.addColumn("Tgl Kembali");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }  
} 
    
