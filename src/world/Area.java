package world;

import java.util.Random;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class Area {

    int dimension = 100;
    Cell[][] areaGrid;
    Cell[][] areaGrid2;

    /**
     * Tworzy akwen o domyslnym wymiarze
     */
    public Area () {
        areaGrid = new Cell[dimension][dimension];
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y] = new Cell(x,y,E_CellType.WATER);
        updateArea2();
    }

    /**
     * Tworzy obszar o podanym wymiarze z komorkami o podanym typie.
     *
     * @param dimension dlugosc boku kwadratu obszaru
     */
    public Area (int dimension, E_CellType type) {
        this.dimension = dimension;
        areaGrid = new Cell[dimension][dimension];
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y] = new Cell(x,y,type);
        updateArea2();
    }

    private void updateArea2 () {
        areaGrid2 = new Cell[dimension][dimension];
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid2[x][y] = new Cell(areaGrid[x][y]);
    }

    private void updateArea () {
        areaGrid = new Cell[dimension][dimension];
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y] = new Cell(areaGrid2[x][y]);

    }

    public int getDimension() {
        return this.dimension;
    }

    /**
     * Generuje losowy obszar - nieużyteczne!!!
     *
     * @param landPercent Prawdopodobienstwo powstania ladu
     */
    public void generateRandomArea (float landPercent) {
        Random generator = new Random();

        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                if (generator.nextInt(100)+1 < landPercent)
                    areaGrid[x][y] = new Cell(x,y,E_CellType.LAND);
        for (int i = 0; i<5; i++) {
            for (int x = 0; x < dimension; x++)
                for (int y = 0; y < dimension; y++)
                    if (areaGrid[x][y].getType() == E_CellType.LAND) {
                        if (areaGrid[x][y].howManyNeighborsIn8(this, E_CellType.WATER) >= 4)
                            areaGrid[x][y].setType(E_CellType.WATER);
                    }
                    else if (areaGrid[x][y].getType() == E_CellType.WATER)
                        if (areaGrid[x][y].howManyNeighborsIn8(this, E_CellType.LAND) >= 4)
                             areaGrid[x][y].setType(E_CellType.LAND);
        }
        generateCoast();
    }

    /**
     * Ustawia typ odpowiednich komorek na wybrzeze
     */
    public void generateCoast () {
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y].checkCoast(this);
    }

    public void generateRandomSpillSource () {
        Random generator = new Random();
        while (true) {
            int x = generator.nextInt(dimension);
            int y = generator.nextInt(dimension);
            if (areaGrid[x][y].getType() == E_CellType.WATER) {
                areaGrid[x][y].setType(E_CellType.OIL);
                areaGrid[x][y].setOilLevel(20);
                break;
            }
        }
    }

    public void checkOilForAll () {
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y].checkOil(this);

        updateArea();
    }

    /**
     *  Testowe wyswietlanie obszaru w konsoli
     */
    public void consoleDisplay () {
        for (int x = 0; x<dimension; x++) {
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y].consoleDisplay();
            System.out.println();
        }
    }

}
