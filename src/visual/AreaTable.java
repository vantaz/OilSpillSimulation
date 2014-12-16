package visual;

import world.Area;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by VanitaZ on 2014-12-14.
 */

public class AreaTable extends JTable {

    private final VisualFrame fr;

    public AreaTable(Area area, VisualFrame frame) {
        super(area.getDimension(),area.getDimension());
        this.fr = frame;

        setDefaultRenderer(Object.class, new VisualCellRenderer(area));

        setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fr.setInfo(getSelectedColumn(),getSelectedRow());
            }
        });

        setTableHeader(null);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        setRowHeight(VisualConfigConsts.CELL_SIZE);

        for (int i = 0; i<area.getDimension(); i++) {
            getColumnModel().getColumn(i).setMaxWidth(VisualConfigConsts.CELL_SIZE);
            getColumnModel().getColumn(i).setMinWidth(VisualConfigConsts.CELL_SIZE);
            getColumnModel().getColumn(i).setPreferredWidth(VisualConfigConsts.CELL_SIZE);
        }

    }



}
