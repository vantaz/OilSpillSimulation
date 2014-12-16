package visual;

import world.*;

import java.awt.*;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class Start {

    public static Area area;

    public static void main(String[] args) {
        System.out.println("MAIN START");

        area = new Area(VisualConfigConsts.WORLD_DIMENSION);
        area.generateTestArea();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                VisualFrame frame = new VisualFrame(area);
                frame.setVisible(true);

            }
        } );

        System.out.println("MAIN END");
    }

}


