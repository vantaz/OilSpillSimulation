package visual;

import world.*;

import java.awt.*;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class Start {

    public static Area area;

    public static void main(String[] args) {
        area = new Area(VisualConfigConsts.WORLD_DIMENSION);
        //area.generateTestArea();
        area.generateRandomArea();
        System.out.println(area.getSummaryOilLevel()+" "+area.getAverageOilLevel());

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    area.checkOilForCircle();
                }
                VisualFrame frame = new VisualFrame(area);
                frame.setVisible(true);
                System.out.println(area.getSummaryOilLevel()+" "+area.getAverageOilLevel()+" "+area.getWindPowerAtDirection(E_Direction.S));
                area.displayAreaInfo();
            }
        } );


    }

}


