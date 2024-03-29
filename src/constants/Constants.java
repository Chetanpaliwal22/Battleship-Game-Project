package constants;

import tools.Coordinate;

/**
 * This class contains all the constants used across the project.
 */
public class Constants {

    public static final String CARRIER_NAME = "Carrier";
    public static final String BATTLESHIP_NAME = "Battleship";
    public static final String CRUISER_NAME = "Cruiser";
    public static final String SUBMARINE_NAME = "Submarine";
    public static final String DESTROYER_NAME = "Destroyer";

    public static final int CARRIER_SIZE = 5;
    public static final int BATTLESHIP_SIZE = 4;
    public static final int CRUISER_SIZE = 3;
    public static final int SUBMARINE_SIZE = 3;
    public static final int DESTROYER_SIZE = 2;
    public static final int[] SHIP_SIZES = {2,3,3,4,5};

    public static final int CARRIER_NB = 1;
    public static final int BATTLESHIP_NB = 1;
    public static final int CRUISER_NB = 1;
    public static final int SUBMARINE_NB = 1;
    public static final int DESTROYER_NB = 1;

    public static final int SHIP_NB = CARRIER_NB + BATTLESHIP_NB + CRUISER_NB + SUBMARINE_NB + DESTROYER_NB;

    public static final Coordinate BOARD_SIZE = new Coordinate(11, 9); // y, x !!
    public static final int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 750;

    public static final String ICON = "src/view/resources/images/Icon.png";

    public static final String FIVE_STARS = "src/view/resources/images/FiveStars.png";
    public static final String FOUR_STARS = "src/view/resources/images/FourStars.png";
    public static final String THREE_STARS = "src/view/resources/images/ThreeStars.png";
    public static final String TWO_STARS = "src/view/resources/images/TwoStars.png";
    public static final String ONE_STAR = "src/view/resources/images/OneStar.png";

    public static final String MUSIC_FILE_NAME = "src/view/resources/audio/BattleshipMusic.mp3";
    public static final String EXPLOSION_SOUND = "src/view/resources/audio/ExplosionSound.mp3";
    public static final String WATER_SPLASH_SOUND = "src/view/resources/audio/WaterSplashSound.mp3";
    	
}