package world;

/**
 * Created by VanitaZ on 2014-12-01.
 */
public final class Consts {

    // Sterowanie efektami
    public static boolean evaporateOn = true;
    public static boolean windOn = true;
    public static boolean currentOn = true;

    // Stałe rozchodzenia sie ropy
    public static final double OIL_B_ADJ = 0.14;
    public static final double OIL_B_DIA = 0.18 * OIL_B_ADJ;

    // Poziom ropy w zrodle
    public static final double OIL_SOURCE_LEVEL = 100;
    public static final double OIL_SOURCE_OVERALL_LEVEL = 1000;

    // Wspolczynnik parowania
    public static final double EVAPORATION_RATE = 0.17E-20;

    // Domyslna predkosc wiatru
    public static final E_Direction DEFAULT_WIND_DIRECTION = E_Direction.N;
    public static final double DEFAULT_WIND_POWER = 0.2;

    // Domyslna predkosc pradu
    public static final E_Direction DEFAULT_CURRENT_DIR = E_Direction.W;
    public static final double DEFAULT_CURRENT_POWER = 0.7;

    // Poziom ropy, od ktorego komorka przyjmuje typ OIL
    public static final double OIL_VISIBLE_LEVEL = 0;

    // Iteracje w czasie generowania swiata
    public static final short WORLD_GENERATION_ITERATION = 10;
}
