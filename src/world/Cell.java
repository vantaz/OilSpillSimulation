package world;

/**
 * Created by vanitaz on 20.11.14.
 */
public class Cell {

    private E_CellType type = E_CellType.LAND;
    private int x;
    private int y;

    private double oilLevel = 0;
    private double currentPower = 0;
    private E_Direction currentDir = E_Direction.NONE;

    /********************************************************************************************************/

    public Cell (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell (int x, int y, E_CellType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Cell (Cell cell) {
        this.x = cell.getX();
        this.y = cell.getY();
        this.type = cell.getType();
        this.oilLevel = cell.getOilLevel();
        this.currentPower = cell.getCurrentPower();
        this.currentDir = cell.getCurrentDir();
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
        if (this.currentPower > 0)
            return this.currentDir;
        else
            return E_Direction.NONE;
    }

    public void setType (E_CellType type) {
        this.type = type;
    }

    public void setXY (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOilLevel (float level) {
        this.oilLevel = level;
    }

    public void setCurrent (float power, E_Direction dir) {
        this.currentPower = power;
        this.currentDir = dir;
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
        if (x > 0 && area.areaGrid[x-1][y].getType() == type)
            count++;
        if (y > 0 && area.areaGrid[x][y-1].getType() == type)
            count++;
        if (x < area.getDimension()-1 && area.areaGrid[x+1][y].getType() == type)
            count++;
        if (y < area.getDimension()-1 && area.areaGrid[x][y+1].getType() == type)
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
        if (x > 0 && area.areaGrid[x-1][y].getType() == type) // WEST
            count++;
        if (y > 0 && area.areaGrid[x][y-1].getType() == type) // NORTH
            count++;
        if (x < area.getDimension()-1 && area.areaGrid[x+1][y].getType() == type) // EAST
            count++;
        if (y < area.getDimension()-1 && area.areaGrid[x][y+1].getType() == type) // SOUTH
            count++;
        if (x > 0 && y > 0 && area.areaGrid[x-1][y-1].getType() == type) // NORTH WEST
            count++;
        if (x > 0 && y < area.getDimension()-1 && area.areaGrid[x-1][y+1].getType() == type) // SOUTH WEST
            count++;
        if (x < area.getDimension()-1 && y > 0 && area.areaGrid[x+1][y-1].getType() == type) // NORTH EAST
            count++;
        if (x < area.getDimension()-1 &&  y < area.getDimension()-1 && area.areaGrid[x+1][y+1].getType() == type) // SOUTH EAST
            count++;

        return count;
    }

    public void checkOil (Area area) {
        type = this.getType();
        int count = 0;
        if (type == E_CellType.OIL) count++;
        if (type == E_CellType.WATER || type == E_CellType.OIL || type == E_CellType.COAST) {
            count += this.howManyNeighborsIn4(area, E_CellType.OIL);
        }
        if (count > 0) {
            area.areaGrid2[this.getX()][this.getY()].oilLevel += count;
            if (type == E_CellType.WATER || type == E_CellType.OIL) area.areaGrid2[this.getX()][this.getY()].setType(E_CellType.OIL);
        }
    }

    private boolean isBorder (Area area) {
        if (this.x == 0 || this.x == area.dimension || this.y == 0 || this.y == area.dimension) return true;
        else return false;
    }

    public void checkOil2 (Area area) {
        if (this.isBorder(area)) return;
        type = this.getType();
        if (type == E_CellType.LAND || type == E_CellType.BLOCK || type == E_CellType.COAST) return;
        if (this.howManyNeighborsIn8(area,E_CellType.OIL) == 0) return;
        double oilLevel = this.getOilLevel();
/*
        double newOilLevel = oilLevel + Consts.OIL_B_ADJ *(
                (oilLevel - area.getCellAt(x-1,y).getOilLevel()) + (oilLevel - area.getCellAt(x+1,y).getOilLevel()) +
                        (oilLevel - area.getCellAt(x,y-1).getOilLevel()) + (oilLevel - area.getCellAt(x,y+1).getOilLevel()));

        newOilLevel += Consts.OIL_B_DIA * (
                (oilLevel - area.getCellAt(x-1,y-1).getOilLevel()) + (oilLevel - area.getCellAt(x+1,y-1).getOilLevel()) +
                        (oilLevel - area.getCellAt(x-1,y+1).getOilLevel()) + (oilLevel - area.getCellAt(x+1,y+1).getOilLevel()));
*/
        double newOilLevel = oilLevel + Consts.OIL_B_ADJ *(
                (area.getCellAt(x-1,y).getOilLevel() - oilLevel) + (area.getCellAt(x+1,y).getOilLevel() - oilLevel) +
                        (area.getCellAt(x,y-1).getOilLevel() - oilLevel) + (area.getCellAt(x,y+1).getOilLevel() - oilLevel));

        newOilLevel += Consts.OIL_B_DIA * (
                (area.getCellAt(x-1,y-1).getOilLevel() - oilLevel) + (area.getCellAt(x+1,y-1).getOilLevel() - oilLevel) +
                        (area.getCellAt(x-1,y+1).getOilLevel() - oilLevel) + (area.getCellAt(x+1,y+1).getOilLevel() - oilLevel));

        area.areaGrid2[this.getX()][this.getY()].oilLevel = newOilLevel;

        if (newOilLevel > 0) area.areaGrid2[this.getX()][this.getY()].setType(E_CellType.OIL);
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
