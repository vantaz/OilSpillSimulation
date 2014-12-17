package visual;

import world.Area;
import world.Consts;
import world.E_Direction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by VanitaZ on 2014-12-14.
 * Panel przycisków
 */
public class VisualButtonPanel extends JPanel {

    private Area area;
    private VisualFrame fr;

    private JButton bPlay10 = new JButton("10");
    private JButton bPlay50 = new JButton("50");
    private JButton bPlay100 = new JButton("100");
    private JButton bGenerateArea = new JButton("Generate Area");

    private JButton bSetSpill = new JButton("Generate Spill");

    private JCheckBox cWindOn = new JCheckBox();
    private JLabel lWindOn = new JLabel("Wind");

    private JCheckBox cCurrentOn = new JCheckBox();
    private JLabel lCurrentOn = new JLabel("Current");

    private JComboBox<E_Direction> cWindDir = new JComboBox();
    private JSlider sWindPow = new JSlider();
    private JLabel lWindDir = new JLabel("Wind direction:");

    private JButton bLand = new JButton("Make Land");

    private JTextArea aInfo = new JTextArea();

    private JPanel buttonPanel = new JPanel();
    private JPanel infoPanel = new JPanel();

    public VisualButtonPanel (Area area, VisualFrame frame) {

        this.area = area;
        this.fr = frame;

        setLayout(new GridLayout(2,1));

        buildButtonsPanel();
        add(buttonPanel);

        buildInfoPanel();
        add(infoPanel);

    }

    private void buildButtonsPanel () {
        buttonPanel.setLayout(new GridLayout(10,1));


        JPanel panel1 = new JPanel();
        panel1.add(bPlay10);
        bPlay10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.nextIterations(10);
            }
        });
        panel1.add(bPlay50);
        bPlay50.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.nextIterations(50);
            }
        });
        panel1.add(bPlay100);
        bPlay100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.nextIterations(100);
            }
        });
        buttonPanel.add(panel1);

        JPanel panel2 = new JPanel();
        panel2.add(bGenerateArea);
        bGenerateArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.generateArea();
            }
        });
        buttonPanel.add(panel2);

        JPanel panel4 = new JPanel();
        bSetSpill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.setSpillAtSelected();
            }
        });
        panel4.add(bSetSpill);
        buttonPanel.add(panel4);

        JPanel panel5 = new JPanel();
        panel5.add(cWindOn);
        cWindOn.setSelected(true);
        cWindOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.turnWindOn(!cWindOn.isSelected());
            }
        });
        panel5.add(lWindOn);
        buttonPanel.add(panel5);

        JPanel panel7 = new JPanel();
        panel7.add(lWindDir);
        makeWindDirList();
        panel7.add(cWindDir);
        cWindDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.setWind((E_Direction)cWindDir.getSelectedItem());
            }
        });
        buttonPanel.add(panel7);

        JPanel panel75 = new JPanel ();
        panel75.add(sWindPow);
        sWindPow.setMinimum(0);
        sWindPow.setMaximum(100);
        sWindPow.setValue((int) (Consts.DEFAULT_WIND_POWER * 100));
        sWindPow.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fr.setWind((double)(sWindPow.getValue()/100));
            }
        });
        buttonPanel.add(panel75);

        JPanel panel6 = new JPanel();
        panel6.add(cCurrentOn);
        cCurrentOn.setSelected(true);
        cCurrentOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.turnCurrentOn(cCurrentOn.isSelected());
            }
        });
        panel6.add(lCurrentOn);
        buttonPanel.add(panel6);

        /*
        JPanel panel8 = new JPanel();
        panel8.add(bLand);
        bLand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.makeLandAtSelected();
            }
        });
        buttonPanel.add(panel8);
        */
    }

    private void buildInfoPanel () {
        infoPanel.add(aInfo);
        aInfo.setEditable(false);
    }

    private void makeWindDirList () {
        for (int i = 0; i < 8; i++) {
            cWindDir.addItem(E_Direction.values()[i]);
        }
    }

    public void setInfo(int x, int y) {
        aInfo.setText(area.getCellAt(x, y).getInfo());
    }

}


