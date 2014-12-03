package world;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class Area implements Serializable {

    private int dimension = 100;
    private double temperature = 20;

    private double windPower = 0.5;
    private E_Direction windDirection = E_Direction.N;

    private double[] windDirectionsPower = new double[8];

    private Cell[][] areaGrid;

    /**
     * Tworzy akwen o domyslnym wymiarze
     */
    public Area (int dimension) {
        this.dimension = dimension;
        areaGrid = new Cell[dimension][dimension];
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y] = new Cell(x,y,E_CellType.WATER);
        this.setWindDirectionsPower();
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
    }

    public int getDimension() {
        return this.dimension;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public Cell[][] getAreaGrid() {
        return this.areaGrid;
    }

    public void setWind (double power, E_Direction dir) {
        this.windDirection = dir;
        this.windPower = power;
        this.setWindDirectionsPower();
    }

    public double getWindPower () {
        return this.windPower;
    }

    private void setWindDirectionsPower () {
        int dir = this.windDirection.ordinal();
        this.windDirectionsPower[dir] = this.windPower;
        this.windDirectionsPower[(dir+4)%8] = - this.windPower;
        this.windDirectionsPower[(dir+1)%8] = this.windPower / 2;
        this.windDirectionsPower[(dir+7)%8] = this.windPower / 2;
        this.windDirectionsPower[(dir+3)%8] = - this.windPower / 2;
        this.windDirectionsPower[(dir+5)%8] = - this.windPower / 2;
        this.windDirectionsPower[(dir+2)%8] = 0;
        this.windDirectionsPower[(dir+6)%8] = 0;
    }

    public double getWindPowerAtDirection (E_Direction dir) {
        return this.windDirectionsPower[dir.ordinal()];
    }

    public E_Direction getWindDirection () {
        return this.windDirection;
    }

    /**
     * Generuje losowy obszar
     *
     */
    public void generateRandomArea () {
        Random generator = new Random();

        for (int x = 0; x < dimension; x++)
            for (int y = 0; y < dimension; y++) {
                if (generator.nextInt(100) > 50)
                    areaGrid[x][y] = new Cell(x, y, E_CellType.LAND);
                else
                    areaGrid[x][y] = new Cell(x, y, E_CellType.WATER);
            }

        for (int i = 0; i < Consts.WORLD_GENERATION_ITERATION; i++) {
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

    /**
     * Generuje zrodlo wycieku w losowym miejscu na wodzie.
     *
     * Poziom wycieku w zrodle zalezy od stalej "OIL_SOURCE_LEVEL"
     */
    public void generateRandomSpillSource () {
        Random generator = new Random();
        while (true) {
            int x = generator.nextInt(dimension);
            int y = generator.nextInt(dimension);
            if (areaGrid[x][y].getType() == E_CellType.WATER) {
                areaGrid[x][y].setType(E_CellType.SOURCE);
                areaGrid[x][y].setOilLevel(Consts.OIL_SOURCE_LEVEL);
                break;
            }
        }
    }

    /**
     * Wykonuje przeliczenie poziomu oleju dla wszystkich komorek.
     */
    public void checkOilForAll () {
        for (int x = 1; x<dimension-1; x++)
            for (int y = 1; y<dimension-1; y++)
                areaGrid[x][y].checkOil(this);
        this.uptadeOilLevelForAll();
    }

    public void uptadeOilLevelForAll () {
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y].uptadeOilLevel();
    }

    /**
     * Zwraca komorka o podanych wspolrzednych
     * @param x wspolrzedna pozioma
     * @param y wspolrzedna pionowa
     * @return komorka
     */
    public Cell getCellAt(int x, int y) {
        return areaGrid[x][y];
    }

    /**
     * Oblicza sumaryczna ilosc oleju w komorkach.
     * @return sumaryczna ilosc oleju w komorkach
     */
    public double getSummaryOilLevel () {
        double level = 0;
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                level += areaGrid[x][y].getOilLevel();

        return level;
    }

    /**
     * Oblicza srednia ilosc oleju w komorkach z olejem.
     * @return srednia ilosc oleju w komorkach oleju
     */
    public double getAverageOilLevel () {
        double level = 0;
        int count = 0;
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                if (areaGrid[x][y].getOilLevel() > 0 && areaGrid[x][y].getType() != E_CellType.SOURCE) {
                    level += areaGrid[x][y].getOilLevel();
                    count++;
                }

        return level/count;
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

    public void consoleDisplayWind () {
        for (int i = 0; i<8; i++)
            System.out.println(windDirectionsPower[i]);
    }

}
