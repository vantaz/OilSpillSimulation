package world;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class Area implements Serializable {

    private int dimension = 100;
    private double temperature = 20;

    private double windPower = Consts.DEFAULT_WIND_POWER;
    private E_Direction windDirection = Consts.DEFAULT_WIND_DIRECTION;

    private double[] windDirectionsPower = new double[8];

    private Cell[][] areaGrid;

    private int sourceX = -1;
    private int sourceY = -1;

    private int iterations = 0;
    private double overallSourceLevel = Consts.OIL_SOURCE_OVERALL_LEVEL;

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

    public void setWind (E_Direction dir) {
        this.windDirection = dir;
        this.setWindDirectionsPower();
    }

    public double getWindPower () {
        return this.windPower;
    }

    public void setWindDirectionsPower () {
        if (Consts.windOn) {
            int dir = this.windDirection.ordinal();
            this.windDirectionsPower[dir] = this.windPower;
            this.windDirectionsPower[(dir + 4) % 8] = -this.windPower;
            this.windDirectionsPower[(dir + 1) % 8] = this.windPower / 2;
            this.windDirectionsPower[(dir + 7) % 8] = this.windPower / 2;
            this.windDirectionsPower[(dir + 3) % 8] = -this.windPower / 2;
            this.windDirectionsPower[(dir + 5) % 8] = -this.windPower / 2;
            this.windDirectionsPower[(dir + 2) % 8] = 0;
            this.windDirectionsPower[(dir + 6) % 8] = 0;
        }
        else {
            for (int i = 0; i < 8; i++) {
                this.windDirectionsPower[i] = 0;
            }
        }
    }

    public double getWindPowerAtDirection (E_Direction dir) {
        return this.windDirectionsPower[dir.ordinal()];
    }

    public E_Direction getWindDirection () {
        return this.windDirection;
    }

    public void generateTestArea () {
        this.dimension = 500;
        areaGrid = new Cell[dimension][dimension];
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y] = new Cell(x,y,E_CellType.WATER);

        generateSpillSource(250,250);
        generateCurrent(250);
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
                    areaGrid[x][y].setType(E_CellType.LAND);
                else
                    areaGrid[x][y].setType(E_CellType.WATER);
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
        //generateRandomSpillSource();
        generateCurrent(250);
        generateRandomCurrent();
    }

    /**
     * Ustawia typ odpowiednich komorek na wybrzeze
     */
    public void generateCoast () {
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y].checkCoast(this);
    }

    public void generateCurrent (int cX) {
        for (int x = cX - 30; x < cX + 30; x++)
            for (int y = 0; y < dimension; y++)
                areaGrid[x][y].setCurrent(Consts.DEFAULT_CURRENT_POWER,Consts.DEFAULT_CURRENT_DIR);
    }

    public void generateRandomCurrent () {
        Random generator = new Random();
        int x = generator.nextInt(dimension-15);
        E_Direction dir = E_Direction.values()[generator.nextInt(8)];
        for (int i = x; i <= x+10; i++)
            for (int y = 0; y < dimension; y++)
                areaGrid[x][y].setCurrent(Consts.DEFAULT_CURRENT_POWER,dir);
    }

    /**
     * Generuje źródło wycieku w podanym miejscu, poziom zalezy od stałej OIL_SOURCE_LEVEL
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     */
    public void generateSpillSource (int x, int y) {
        if (areaGrid[x][y].getType() == E_CellType.WATER) {
            areaGrid[x][y].setType(E_CellType.SOURCE);
            this.sourceX = x;
            this.sourceY = y;
            areaGrid[x][y].setOilLevel(Consts.OIL_SOURCE_LEVEL);
        }
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
                this.sourceX = x;
                this.sourceY = y;
                areaGrid[x][y].setOilLevel(Consts.OIL_SOURCE_LEVEL);
                break;
            }
        }
    }

    public void refillSource () {
        if (overallSourceLevel <= 0) return;
        overallSourceLevel -= Consts.OIL_SOURCE_LEVEL - getSource().getOilLevel();
        getSource().setOilLevel(Consts.OIL_SOURCE_LEVEL - (overallSourceLevel >= 0 ? 0 : overallSourceLevel));
    }

    private Cell getSource () {
        return areaGrid[sourceX][sourceY];
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

    public void checkOilForCircle () {
        iterations++;

        int minX = sourceX-iterations > 0 ? sourceX-iterations : 1;
        int maxX = sourceX+iterations < dimension ? sourceX+iterations : dimension-1;
        int minY = sourceY-iterations > 0 ? sourceY-iterations : 1;
        int maxY = sourceY+iterations < dimension ? sourceY+iterations : dimension-1;

        for (int x = minX; x < maxX; x++)
            for (int y = minY; y < maxY; y++)
                areaGrid[x][y].checkOil(this);
        this.uptadeOilLevelForCircle();
    }

    public void uptadeOilLevelForAll () {
        for (int x = 0; x<dimension; x++)
            for (int y = 0; y<dimension; y++)
                areaGrid[x][y].uptadeOilLevel();

        refillSource();
    }

    public void uptadeOilLevelForCircle () {
        int minX = sourceX-iterations > 0 ? sourceX-iterations : 1;
        int maxX = sourceX+iterations < dimension ? sourceX+iterations : dimension-1;
        int minY = sourceY-iterations > 0 ? sourceY-iterations : 1;
        int maxY = sourceY+iterations < dimension ? sourceY+iterations : dimension-1;

        for (int x = minX; x < maxX; x++)
            for (int y = minY; y < maxY; y++)
                areaGrid[x][y].uptadeOilLevel();

        refillSource();
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

    public void turnWindOn (boolean on) {
        if (!on) setWind(Consts.DEFAULT_WIND_POWER,windDirection);
        else setWind(0,windDirection);
    }

    public void turnCurrentOn (boolean on) {
        if (!on)
            for (int x = 0; x<dimension; x++)
                for (int y = 0; y<dimension; y++)
                    areaGrid[x][y].setCurrent(0);
        else
            for (int x = 0; x<dimension; x++)
                for (int y = 0; y<dimension; y++)
                    areaGrid[x][y].setCurrent(Consts.DEFAULT_CURRENT_POWER);
    }

    public void displayAreaInfo () {
        System.out.println("Wind power & direction: " + this.windPower + "" + this.windDirection.toString());
        System.out.print("Wind power at directions: ");
        for (int i = 0; i < 8; i++) System.out.print(windDirectionsPower[i] + " ");
        System.out.println();
        System.out.println("Source X Y level: " + sourceX + " " + sourceY + " " + getCellAt(sourceX,sourceY).getOilLevel());
        System.out.println("Source Overall: " + overallSourceLevel);
        System.out.println("Source Current: " + getCellAt(sourceX,sourceY).getCurrentPower() + getCellAt(sourceX,sourceY).getCurrentDir().toString());
        System.out.print("Source power at directions: ");
        for (int i = 0; i < 8; i++) System.out.print(getCellAt(sourceX,sourceY).getCurrentPowerAtDirection(E_Direction.values()[i]) + " ");
        System.out.println();
        System.out.println("Cell 150 250 level: " + getCellAt(150,250).getOilLevel());
        System.out.println("Cell 351 250 level: " + getCellAt(351,250).getOilLevel());
        System.out.println("Cell 250 150 level: " + getCellAt(250,150).getOilLevel());
        System.out.println("Cell 250 351 level: " + getCellAt(250,351).getOilLevel());
    }

}
