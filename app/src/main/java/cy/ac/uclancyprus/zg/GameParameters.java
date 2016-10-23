package cy.ac.uclancyprus.zg;

/**
 * @author Nearchos Paspallis
 * 17/07/2015.
 */
public class GameParameters
{
    public static final long MAX_DELAY = 1000L;

    public static final int CAR_COLOR_RED   = 0x1;
    public static final int CAR_COLOR_BLUE  = 0x2;
    public static final int CAR_COLOR_GREEN = 0x3;

    private long targetTimestamp = 0L;
    private long touchTimestamp = 0L;
    private long enemyDelay = 0L;
    private float crosshairPenalty = 0f;

    private boolean advanced = false;

    private int playerCar = CAR_COLOR_BLUE;
    private int enemyCar = CAR_COLOR_RED;

    public long getTargetTimestamp()
    {
        return targetTimestamp;
    }

    public void setTargetTimestamp(long targetTimestamp)
    {
        this.targetTimestamp = targetTimestamp;
    }

    public long getTouchTimestamp()
    {
        return touchTimestamp;
    }

    public void setTouchTimestamp(long touchTimestamp)
    {
        this.touchTimestamp = touchTimestamp;
    }

    public long getPlayerDelay()
    {
        return touchTimestamp > targetTimestamp ?
                Math.min(touchTimestamp - targetTimestamp, MAX_DELAY)
                :
                MAX_DELAY;
    }

    public long getEnemyDelay()
    {
        return enemyDelay;
    }

    public void setEnemyDelay(long enemyDelay)
    {
        this.enemyDelay = enemyDelay;
    }

    public float getCrosshairPenalty()
    {
        return crosshairPenalty;
    }

    public void setCrosshairPenalty(float crosshairPenalty)
    {
        this.crosshairPenalty = crosshairPenalty;
    }

    public boolean isAdvanced()
    {
        return advanced;
    }

    public void setAdvanced(boolean advanced)
    {
        this.advanced = advanced;
    }

    public int getPlayerCar()
    {
        return playerCar;
    }

    public int getEnemyCar()
    {
        return enemyCar;
    }
}