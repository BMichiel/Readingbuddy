package com.readingbuddy;

public enum Themes {
    LIGHT_GREEN_THEME(0),
    LIGHT_BLUE_THEME(1),
    LIGHT_PINK_THEME(2),
    LIGHT_RED_THEME(3),
    LIGHT_ORANGE_THEME(4),
    LIGHT_PURPLE_THEME(5),

    DARK_GREEN_THEME(10),
    DARK_BLUE_THEME(11),
    DARK_PINK_THEME(12),
    DARK_RED_THEME(13),
    DARK_ORANGE_THEME(14),
    DARK_PURPLE_THEME(15);

    private int nr;

    Themes(int nr)
    {
        this.nr = nr;
    }

    public int getInt()
    {
        return nr;
    }
    public static Themes fromInteger(int x) {
        switch(x) {
            case 0:
                return LIGHT_GREEN_THEME;
            case 1:
                return LIGHT_BLUE_THEME;
            case 2:
                return LIGHT_PINK_THEME;
            case 3:
                return LIGHT_RED_THEME;
            case 4:
                return LIGHT_ORANGE_THEME;
            case 5:
                return LIGHT_PURPLE_THEME;


            case 10:
                return DARK_GREEN_THEME;
            case 11:
                return DARK_BLUE_THEME;
            case 12:
                return DARK_PINK_THEME;
            case 13:
                return DARK_RED_THEME;
            case 14:
                return DARK_ORANGE_THEME;
            case 15:
                return DARK_PURPLE_THEME;
        }
        return null;
    }
}