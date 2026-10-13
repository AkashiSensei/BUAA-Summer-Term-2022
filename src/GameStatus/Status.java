package GameStatus;

public class Status {
    public static final int WIN_SMALL = 0, WIN_MEDIUM = 1, WIN_LARGE = 2;
    public static final int FRAME_SLOW = 1, FRAME_MEDIUM = 2, FRAME_HIGH = 4, FRAME_EXTRA = 8;
    public static final int DIFF_NORMAL = 0, DIFF_HARD = 1, DIFF_EXTRA = 2;
    public static final int GAMING = 1, GAME_PAUSE = 2, GAME_MOVIE = 4, GAME_SHOP = 8, GAME_OVER = 16;


    //游戏常量
    private static int game;
    private static int winWidth, winHeight;
    private static int frameInterval;
    private static int difficulty;
    private static int diffRate;


    //游戏过程变量
      //屏幕中心相对于小骑士的偏移量
    private static int cameraX, cameraY;

    public static void init(int winSize, int frameRate, int diff) {
        System.out.println("-----------------Initiate Status-----------------");
        System.out.println("Status : Set winSize " + winSize);
        switch (winSize) {
            case WIN_SMALL:
                winWidth = 1024;
                winHeight = 576;
                break;
            case WIN_MEDIUM:
                winWidth = 1280;
                winHeight = 720;
                break;
            case WIN_LARGE:
                winWidth = 1600;
                winHeight = 900;
                break;
        }

        System.out.println("Status : Set winSize " + frameRate);
        switch (frameRate) {
            case FRAME_SLOW:
                frameInterval = 64;
                break;
            case FRAME_MEDIUM:
                frameInterval = 32;
                break;
            case FRAME_HIGH:
                frameInterval = 16;
                break;
            case FRAME_EXTRA:
                frameInterval = 8;
                break;
        }

        System.out.println("Status : Set difficulty " + diff);
        difficulty = diff;
        switch (diff) {
            case DIFF_NORMAL:
                diffRate = 1;
                break;
            case DIFF_HARD:
            case DIFF_EXTRA:
                diffRate = 2;
                break;
        }
        game = GAMING;

        //设置小骑士位于屏幕中央
        cameraX = 0;
        cameraY = 0;
    }

    public static int getGame() {
        return game;
    }

    public static void setGame(int game) {
        Status.game = game;
    }

    public static int getWinWidth() {
        return winWidth;
    }

    public static int getWinHeight() {
        return winHeight;
    }

    public static int getFrameInterval() {
        return frameInterval;
    }

    public static int getDifficulty() {
        return difficulty;
    }

    public static int getDiffRate() {
        return diffRate;
    }

    public static int getCameraX() {
        return cameraX;
    }

    public static void setCameraX(int cameraX) {
        Status.cameraX = cameraX;
    }

    public static int getCameraY() {
        return cameraY;
    }

    public static void setCameraY(int cameraY) {
        Status.cameraY = cameraY;
    }
}
