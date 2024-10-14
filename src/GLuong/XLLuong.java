package GLuong;

import java.sql.*;

public class XLLuong {
    private static Connection cn;
    
    public static void getCon() {
        if(cn == null) {
            try {
                cn = DriverManager.getConnection("jdbc:sqlserver://XUY\\SQLEXPRESS;database=DLLuong;user=sa;password=12345678;trustServerCertificate=true");
                System.out.println("Connected");
            } catch (SQLException ex) {
                System.out.println("Not connected: " + ex.getMessage());
            }
        }
    }
    
    public static ResultSet getNV() {
        getCon();
        try {
            Statement st = cn.createStatement();
            return st.executeQuery("SELECT * FROM tbNhanvien");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }
    
    public static ResultSet getNVbyMa(String MaNV) {
        getCon();
        try {
            Statement st = cn.createStatement();
            return st.executeQuery("SELECT * FROM tbNhanvien WHERE MaNV = '"+MaNV+"'");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public static boolean updateNV(String MaNV, Nhanvien nv) {
        try {
            PreparedStatement pst = cn.prepareStatement("UPDATE tbNhanvien SET Hoten = ?, Diachi = ?, Luong = ? WHERE MaNV = ?");
            pst.setString(1, nv.getHoten());
            pst.setString(2, nv.getDiachi());
            pst.setString(3, nv.getLuong());
            pst.setString(4, MaNV);
            int res = pst.executeUpdate();
            return res>0;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
