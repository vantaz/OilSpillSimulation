package visual;

import javafx.scene.control.Cell;
import world.Area;

import javax.swing.*;
import java.awt.*;

/**
 * Created by VanitaZ on 2014-11-24.
 */
public class VisualFrame extends JFrame {

    private JTable areaTable;

    private JButton bRefresh;

    public VisualFrame(Area area) {
        setTitle("Spill some oil");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initGUI(area);
    }

    private void initGUI(Area area) {

        Container pane = getContentPane();

        areaTable = new JTable(area.getDimension(),area.getDimension());
        areaTable.setDefaultRenderer(Object.class, new VisualCellRenderer(area));

        areaTable.setTableHeader(null);
        areaTable.setShowGrid(false);
        areaTable.setIntercellSpacing(new Dimension(0, 0));
        areaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        areaTable.setRowHeight(VisualConfigConsts.CELL_SIZE);
        for (int i = 0; i<area.getDimension(); i++) {
            areaTable.getColumnModel().getColumn(i).setMaxWidth(VisualConfigConsts.CELL_SIZE);
            areaTable.getColumnModel().getColumn(i).setMinWidth(VisualConfigConsts.CELL_SIZE);
            areaTable.getColumnModel().getColumn(i).setPreferredWidth(VisualConfigConsts.CELL_SIZE);
        }
        bRefresh = new JButton("Refresh");
        pane.add(bRefresh);
        pane.add(new JScrollPane(areaTable));



    }


}
