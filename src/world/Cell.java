package world;

import java.io.Serializable;

/**
 * Created by vanitaz on 20.11.14.
 */
public class Cell implements Serializable {

    private E_CellType type = E_CellType.LAND;
    private int x;
    private int y;

    private double oilLevel = 0;
    private double newOilLevel = 0;

    private double currentPower = 0;
    private E_Direction currentDir = E_Direction.N;
    private double[] currentDirectionsPower = new double[8];

    /********************************************************************************************************/

    public Cell (int x, int y) {
        this.setXY(x,y);
    }

    public Cell (int x, int y, E_CellType type) {
        this.setXY(x,y);
        this.setType(type);
    }

    public Cell (int x, int y, E_CellType type, double curP, E_Direction curDir) {
        this.setXY(x,y);
        this.setType(type);
        this.setCurrent(curP,curDir);
    }

    public Cell (Cell cell) {
        this.x = cell.getX();
        this.y = cell.getY();
        this.type = cell.getType();
        this.oilLevel = cell.getOilLevel();
        this.currentPower = cell.getCurrentPower();
        this.currentDir = cell.getCurrentDir();
        this.setCurrentDirectionsPower();
    }

    public E_CellType getType () {
        return this.type;
    }

    public int getX () {
        return this.x;
    }

    public int getY () {
        return this.y;
    }

    public double getOilLevel() {
        return this.oilLevel;
    }

    public double getCurrentPower() {
        return this.currentPower;
    }

    public E_Direction getCurrentDir () {
            return this.currentDir;
    }

    public void setType (E_CellType type) {
        this.type = type;
    }

    private void setXY (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOilLevel (double level) {
        this.oilLevel = level;
    }

    public void setNewOilLevel (double level) {
        this.newOilLevel = level;
    }

    public void uptadeOilLevel () {
        this.oilLevel = this.newOilLevel > 0 ? this.newOilLevel : 0;
        if (this.getType() == E_CellType.WATER || this.getType() == E_CellType.OIL) {
            if (this.newOilLevel > Consts.OIL_VISIBLE_LEVEL)
                this.setType(E_CellType.OIL);
            else
                this.setType(E_CellType.WATER);
        }
    }

    public void setCurrent (double power, E_Direction dir) {
        this.currentPower = power;
        this.currentDir = dir;
        this.setCurrentDirectionsPower();
    }

    private void setCurrentDirectionsPower () {
        if (Consts.CURRENT_ON) {
            int dir = this.currentDir.ordinal();
            this.currentDirectionsPower[dir] = this.currentPower;
            this.currentDirectionsPower[(dir + 4) % 8] = -this.currentPower;
            this.currentDirectionsPower[(dir + 1) % 8] = this.currentPower / 2;
            this.currentDirectionsPower[(dir + 7) % 8] = this.currentPower / 2;
            this.currentDirectionsPower[(dir + 3) % 8] = -this.currentPower / 2;
            this.currentDirectionsPower[(dir + 5) % 8] = -this.currentPower / 2;
            this.currentDirectionsPower[(dir + 2) % 8] = 0;
            this.currentDirectionsPower[(dir + 6) % 8] = 0;
        }
        else {
            for (int i = 0; i < 8; i++) {
                this.currentDirectionsPower[i] = 0;
            }
        }
    }

    public double getCurrentPowerAtDirection (E_Direction dir) {
        return this.currentDirectionsPower[dir.ordinal()];
    }

    /**
     * Sprawdza czy komorka ladowa nalezy do wybrzeza, jesli tak ustawia jej odpowiedni typ
     *
     * @param area Obszar, w ktorym znajduje sie komorka
     */
    public void checkCoast (Area area) {
        if (this.type == E_CellType.LAND && this.howManyNeighborsIn8(area, E_CellType.WATER) > 0)
            this.type = E_CellType.COAST;
    }

    /**
     * Sprawdza ile komorek w otoczeniu Moor'a ma podany typ
     *
     * @param area Obszar, w ktorym znajduje sie komorka
     * @param type Typ poszukiwanych sasiadow
     * @return liczba sasiadow o podanym typie
     */
    public int howManyNeighborsIn4(Area area, E_CellType type) {
        int count = 0;
        if (x > 0 && area.getCellAt(x-1,y).getType() == type) // WEST
            count++;
        if (y > 0 && area.getCellAt(x,y-1).getType() == type) // NORTH
            count++;
        if (x < area.getDimension()-1 && area.getCellAt(x+1,y).getType() == type) // EAST
            count++;
        if (y < area.getDimension()-1 && area.getCellAt(x,y+1).getType() == type) // SOUTH
            count++;
        return count;
    }

    /**
     * Sprawdza ile komorek w otoczeniu von Neumanna ma podany typ
     *
     * @param area Obszar, w ktorym znajduje sie komorka
     * @param type Typ poszukiwanych sasiadow
     * @return liczba sasiadow o podanym typie
     */
    public int howManyNeighborsIn8(Area area, E_CellType type) {
        int count = 0;
        count += howManyNeighborsIn4(area,type);
        if (x > 0 && y > 0 && area.getCellAt(x-1,y-1).getType() == type) // NORTH WEST
            count++;
        if (x > 0 && y < area.getDimension()-1 && area.getCellAt(x-1,y+1).getType() == type) // SOUTH WEST
            count++;
        if (x < area.getDimension()-1 && y > 0 && area.getCellAt(x+1,y-1).getType() == type) // NORTH EAST
            count++;
        if (x < area.getDimension()-1 &&  y < area.getDimension()-1 && area.getCellAt(x+1,y+1).getType() == type) // SOUTH EAST
            count++;

        return count;
    }

    /**
     * Sprawdza, czy komorka znajduje sie na granicy obszaru
     *
     * @param area
     * @return true jeśli komorka znjaduje sie na granicy obszaru, false w przeciwnym wypadku
     */
    private boolean isBorder (Area area) {
        if (this.x == 0 || this.x == area.getDimension() || this.y == 0 || this.y == area.getDimension()) return true;
        else return false;
    }

    /**
     * Oblicza i ustawia nowy poziom oleju dla komorki
     * @param area obszar
     */
    public void checkOil(Area area) {
        if (this.isBorder(area)) return;
        type = this.getType();
        if (type == E_CellType.LAND || type == E_CellType.BLOCK || type == E_CellType.COAST) return;
        if (this.howManyNeighborsIn8(area,E_CellType.OIL) == 0 && this.howManyNeighborsIn8(area,E_CellType.SOURCE) == 0) return;
        double oilLevel = this.getOilLevel();

        double newOilLevel = this.oilLevel;

        double tmp = 0;
        Cell cell;
        E_CellType cellType;

        // Rozchodzenie sie ropy na komorki styczne bokiem z badana komorka.
        cell = area.getCellAt(x-1,y);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.S)+this.getCurrentPowerAtDirection(E_Direction.S)) * cell.getOilLevel() - oilLevel;

        cell = area.getCellAt(x+1,y);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.N)+this.getCurrentPowerAtDirection(E_Direction.N)) * cell.getOilLevel() - oilLevel;

        cell = area.getCellAt(x,y-1);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.E)+this.getCurrentPowerAtDirection(E_Direction.E)) * cell.getOilLevel() - oilLevel;

        cell = area.getCellAt(x,y+1);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.W)+this.getCurrentPowerAtDirection(E_Direction.W)) * cell.getOilLevel() - oilLevel;

        newOilLevel += tmp * Consts.OIL_B_ADJ;
        tmp = 0;

        // Rozchodzenie sie ropy na komorki styczne naroznikami z badana komorka.
        cell = area.getCellAt(x-1,y-1);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.SW)+this.getCurrentPowerAtDirection(E_Direction.SW)) * cell.getOilLevel() - oilLevel;

        cell = area.getCellAt(x+1,y-1);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.NW)+this.getCurrentPowerAtDirection(E_Direction.NW)) * cell.getOilLevel() - oilLevel;

        cell = area.getCellAt(x-1,y+1);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.SE)+this.getCurrentPowerAtDirection(E_Direction.SE)) * cell.getOilLevel() - oilLevel;

        cell = area.getCellAt(x+1,y+1);
        cellType = cell.getType();
        if (cellType == E_CellType.WATER || cellType == E_CellType.OIL || cellType == E_CellType.SOURCE)
            tmp += (1+area.getWindPowerAtDirection(E_Direction.NE)+this.getCurrentPowerAtDirection(E_Direction.NE)) * cell.getOilLevel() - oilLevel;

        newOilLevel += tmp * Consts.OIL_B_DIA;

        // Zmniejszenie poziomu ropy poprzez parowanie
        if (Consts.EVAPORATE_ON) {
            newOilLevel -= Consts.EVAPORATION_RATE * 0.5 * (area.getTemperature()+273);
            if (newOilLevel < 0) newOilLevel = 0;
        }

        this.setNewOilLevel(newOilLevel);
    }

    /**
     * Wyswietla w konsoli litere odpowiadajaca typowi komorki
     */
    public void consoleDisplay () {
        switch (this.type) {
            case LAND:
                System.out.print('L');
                break;
            case WATER:
                System.out.print('W');
                break;
            case COAST:
                System.out.print('C');
                break;
            case OIL:
                System.out.print('O');
                break;
            case BLOCK:
                System.out.print('B');
                break;
            case SOURCE:
                System.out.print('S');
                break;
        }

    }


}
