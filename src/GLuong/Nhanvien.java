package GLuong;


public class Nhanvien extends Person{
    private String Diachi;
    private String Luong;

    public Nhanvien() {
    }

    public Nhanvien(String MaNV, String Hoten, String Diachi, String Luong) {
        super(MaNV, Hoten);
        this.Diachi = Diachi;
        this.Luong = Luong;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String Diachi) {
        this.Diachi = Diachi;
    }

    public String getLuong() {
        return Luong;
    }

    public void setLuong(String Luong) {
        this.Luong = Luong;
    }
}
