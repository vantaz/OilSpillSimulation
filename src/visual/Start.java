package visual;

import world.*;

import java.awt.*;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class Start {

    public static Area area;

    public static void main(String[] args) {
        area = new Area(500);
        area.generateRandomArea();
        area.generateRandomSpillSource();


        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                VisualFrame frame = new VisualFrame(area);
                frame.setVisible(true);
                for (int i = 0; i < 3000; i++) {
                    area.checkOilForAll();
                }
            }
        } );


    }

}


