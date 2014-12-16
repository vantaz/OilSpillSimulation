package visual;

import world.Area;
import world.Consts;
import world.E_Direction;

import javax.swing.*;
import java.awt.*;

/**
 * Created by VanitaZ on 2014-11-24.
 */
public class VisualFrame extends JFrame {

    private AreaTable areaTable;
    private static Area area;

    private JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private VisualButtonPanel buttonPanel;
    private JScrollPane areaPanel;


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public VisualFrame(Area area) {
        setTitle("Spill some oil");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.area = area;
        areaTable = new AreaTable(area,this);

        setOptimalSize();
        setLocationRelativeTo(null);

        buttonPanel = new VisualButtonPanel(area,this);
        areaPanel = new JScrollPane(areaTable);

        initGUI();
    }

    private void setOptimalSize() {
        int dim = areaTable.getColumnCount() * VisualConfigConsts.CELL_SIZE + 10;
        setSize(dim+300,dim+50);
    }

    private void initGUI() {

        Container contentPane = getContentPane();

        mainPanel.setDividerLocation(areaTable.getColumnCount() * VisualConfigConsts.CELL_SIZE + 10);
        mainPanel.setLeftComponent(areaPanel);
        mainPanel.setRightComponent(buttonPanel);

        contentPane.add(mainPanel);

    }

    public void setInfo(int x, int y) {
        buttonPanel.setInfo(y,x);
    }

    public void setSpillAtSelected () {
        area.generateSpillSource(areaTable.getSelectedRow(),areaTable.getSelectedColumn());
    }

    public void nextIterations (int ile) {
        for (int i = 0; i < ile; i++) {
            area.checkOilForCircle();
        }
        areaPanel.repaint();
    }

    public void turnWindOn (boolean wind) {
        area.turnWindOn(wind);
    }

    public void turnCurrentOn (boolean current) {
        area.turnCurrentOn(current);
    }

    public void setWind (E_Direction dir) {
        area.setWind(dir);
    }

    public void generateArea () {
        area.generateRandomArea();
        areaPanel.repaint();
    }

}
