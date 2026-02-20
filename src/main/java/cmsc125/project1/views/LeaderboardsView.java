package cmsc125.project1.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardsView extends JInternalFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public LeaderboardsView() {
        super("System Leaderboards", true, true, true, true);
        setSize(500, 400);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        String[] columns = {"RANK", "HACKER", "DIFFICULTY", "SCORE"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setBackground(Color.BLACK);
        table.setForeground(new Color(0, 255, 65)); // Matrix green
        table.setFont(new Font("Monospaced", Font.BOLD, 14));
        table.setGridColor(Color.DARK_GRAY);
        table.getTableHeader().setBackground(new Color(0, 0, 51));
        table.getTableHeader().setForeground(Color.CYAN);
        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateScores(List<String[]> scores) {
        tableModel.setRowCount(0); // Clear old scores
        int rank = 1;
        for (String[] entry : scores) {
            tableModel.addRow(new Object[]{rank++, entry[0], entry[1], entry[2]});
        }
    }
}
