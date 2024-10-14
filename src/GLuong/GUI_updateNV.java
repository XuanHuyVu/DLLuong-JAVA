package GLuong;

import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GUI_updateNV extends JFrame implements ActionListener, MouseListener{
    private JTextField tfMaHV;
    private JTextField tfHoten;
    private JComboBox<String> cbDiachi;
    private JTextField tfLuong;
    private JButton btSearch;
    private JButton btUpdate;
    private DefaultTableModel dfModel;
    private JTable tb;
    
    public GUI_updateNV() {
        setTitle("Chương trình quản lý lương");
        setResizable(true);
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        BuildGUI();
    }
    
    private void BuildGUI() {
        JPanel pnLeft = new JPanel(new GridBagLayout());
        pnLeft.setBorder(new EmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,5,5,5);
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lbMaNV = new JLabel("Mã nhân viên: ");
        pnLeft.add(lbMaNV, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        tfMaHV = new JTextField();
        tfMaHV.setPreferredSize(new Dimension(300,30));
        pnLeft.add(tfMaHV, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lbHoten = new JLabel("Họ tên: ");
        pnLeft.add(lbHoten, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        tfHoten = new JTextField();
        tfHoten.setPreferredSize(new Dimension(300,30));
        pnLeft.add(tfHoten, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lbDiachi = new JLabel("Địa chỉ: ");
        pnLeft.add(lbDiachi, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        cbDiachi = new JComboBox<> (new String[] {"Hà Nội", "Hải Phòng", "Nam Định"});
        cbDiachi.setPreferredSize(new Dimension(300,30));
        pnLeft.add(cbDiachi, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lbLuong = new JLabel("Lương: ");
        pnLeft.add(lbLuong, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        tfLuong = new JTextField();
        tfLuong.setPreferredSize(new Dimension(300,30));
        pnLeft.add(tfLuong, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel pnLeftBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btSearch = new JButton("Tìm kiếm nhân viên");
        btUpdate = new JButton("Cập nhật nhân viên");
        pnLeftBottom.add(btSearch);
        pnLeftBottom.add(btUpdate);
        btSearch.addActionListener(this);
        btUpdate.addActionListener(this);
        pnLeft.add(pnLeftBottom, gbc);
        
        JPanel pnLeftContainer = new JPanel(new BorderLayout());
        pnLeftContainer.add(pnLeft, BorderLayout.NORTH);        
        
        //Table
        JPanel pnRight = new JPanel(new GridLayout(1,1));
        String[] headers = {"Mã nhân viên", "Họ tên", "Địa chỉ", "Lương"};
        dfModel = new DefaultTableModel(headers,0);
        tb = new JTable(dfModel);
        tb.addMouseListener(this);
        pnRight.add(new JScrollPane(tb));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pnLeftContainer,pnRight);
        splitPane.setDividerLocation(400);
        this.add(splitPane);
        
        loadData(dfModel);
    }
    
    public static void main(String[] args) {
        new GUI_updateNV().setVisible(true);
        XLLuong.getCon();
    }
    
    private void loadData(DefaultTableModel dfModel) {
        try {
            ResultSet res = XLLuong.getNV();
            dfModel.setRowCount(0);
            dfModel.fireTableDataChanged();
            if(res != null) {
                while (res.next()) {                    
                    dfModel.addRow(new String[] {
                        res.getString("MaNV"),
                        res.getString("Hoten"),
                        res.getString("Diachi"),
                        res.getString("Luong"),
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to load data." + e.getMessage());
        }
    }
    
    private void searchEmployee() {
        String searchMaNV = tfMaHV.getText().toLowerCase();
        boolean found = false;
        
        for (int i = 0; i < dfModel.getRowCount(); i++) {
            String MaNV = dfModel.getValueAt(i, 0).toString().toLowerCase();
            if(MaNV.equals(searchMaNV)) {
                tb.setRowSelectionInterval(i, i);
                tfHoten.setText(dfModel.getValueAt(i, 1).toString());
                cbDiachi.setSelectedItem(dfModel.getValueAt(i, 2).toString());
                tfLuong.setText(dfModel.getValueAt(i, 3).toString());
                found = true;
                break;
            }
        }
        
        if(!found) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy nhân viên có mã " + searchMaNV);
        }
    }
    
    private void updateEmployee() {
        String MaNV = tfMaHV.getText().trim();
        String Hoten = tfHoten.getText().trim();
        String Diachi = cbDiachi.getSelectedItem().toString();
        String Luong = tfLuong.getText().trim();
        
        try {
            boolean update = false;
            for (int i = 0; i < dfModel.getRowCount(); i++) {
                String maNV = dfModel.getValueAt(i,0).toString();
                if(maNV.equals(MaNV)) {
                    boolean res = XLLuong.updateNV(maNV, new Nhanvien(maNV, Hoten, Diachi, Luong));
                    if(res) {
                        dfModel.setValueAt(Hoten, i, 1);
                        dfModel.setValueAt(Diachi, i, 2);
                        dfModel.setValueAt(Luong, i, 3);
                        JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                        update = true;
                    }
                    else JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
                }
            }
            
            if (!update) {
                JOptionPane.showMessageDialog(null, "Không có nhân viên mã " + MaNV);
            }
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật: " + e.getMessage());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btSearch) {
            searchEmployee();
        } else if(e.getSource() == btUpdate) {
            updateEmployee();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int selectedRow = tb.getSelectedRow();
        tfMaHV.setText((String) dfModel.getValueAt(selectedRow, 0));
        tfHoten.setText((String) dfModel.getValueAt(selectedRow, 1));
        cbDiachi.setSelectedItem(dfModel.getValueAt(selectedRow, 2));
        tfLuong.setText((String) dfModel.getValueAt(selectedRow, 3));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}