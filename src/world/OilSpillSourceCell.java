package world;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public class OilSpillSourceCell extends Cell {

    private float sourceVolume = 0;
    private float spillVelocity = 0;

    public OilSpillSourceCell (int x, int y) {
        super(x,y);
        this.setType(E_CellType.SOURCE);
    }

    /**
     *
     * @param x
     * @param y
     * @param volume
     * @param velocity
     */
    public OilSpillSourceCell (int x, int y, float volume, float velocity) {
        super(x,y);
        this.setType(E_CellType.SOURCE);
        setSpill(volume,velocity);
    }

    public float getSourceVolume () {
        return this.sourceVolume;
    }

    public float getSourceVelocity () {
        return this.spillVelocity;
    }

    public void setSpill (float volume, float velocity) {
        this.sourceVolume = volume;
        this.spillVelocity = velocity;
    }
}
