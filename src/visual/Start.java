package visual;

import world.*;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class Start {

    public static Area area;

    public static void main(String[] args) {
        area = new Area();
        area.generateRandomArea(50);
        area.generateRandomSpillSource();
        area.consoleDisplay();
    }

}

