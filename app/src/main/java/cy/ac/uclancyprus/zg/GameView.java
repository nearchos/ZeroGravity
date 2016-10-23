package cy.ac.uclancyprus.zg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Vector;

/**
 * @author Nearchos Paspallis
 */
public class GameView extends View
{
    public static final int NUM_OF_LIGHTS = 5;

    private Bitmap bannerBitmap = null;

    private Bitmap scaledBackground = null;
    private int selectedRoadLinesIndex = 0;
    private Vector<Bitmap> roadLinesBitmaps = null;
    private Bitmap scaledTopBackground = null;

    private Bitmap bitmapLightOff   = null;
    private Bitmap bitmapLightOn    = null;
    private Vector<Boolean> lightStates = new Vector<>(NUM_OF_LIGHTS);

    private Vector<Bitmap> blueCarBitmaps = null;
    private Vector<Bitmap> redCarBitmaps = null;
//    private Vector<Bitmap> greenCarBitmaps = null;

    private Paint paintScore;
    private long scorePlayer = 0;
    private long scoreEnemy  = 0;

    public GameView(final Context context)
    {
        super(context);

        while(lightStates.size() < NUM_OF_LIGHTS)
        {
            lightStates.add(true);
        }
    }

    private ActivityGame activity;

    void setActivity(final ActivityGame activity)
    {
        this.activity = activity;
    }

    private GameStateMachine gameStateMachine = null;
    void setGameStateMachine(final GameStateMachine gameStateMachine)
    {
        this.gameStateMachine = gameStateMachine;
        invalidate();
    }

    private GameParameters gameParameters;
    void setGameParameters(final GameParameters gameParameters)
    {
        this.gameParameters = gameParameters;
    }

    private void loadOnThread()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int canvasWidth  = getResources().getDisplayMetrics().widthPixels;
                int canvasHeight = getResources().getDisplayMetrics().heightPixels;

                // init text (score) paint
                paintScore = new Paint();
                paintScore.setStyle(Paint.Style.FILL_AND_STROKE);
                paintScore.setTextSize(canvasHeight / 15f);
                paintScore.setColor(Color.YELLOW);

                // loadOnThread background bitmap
                final Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.game_background);
                scaledBackground = Bitmap.createScaledBitmap(background, canvasWidth, canvasHeight, false);

                // loadOnThread lines bitmaps
                roadLinesBitmaps = new Vector<>();
                Bitmap tempBitmap;
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lines1);
                roadLinesBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, canvasWidth, canvasHeight, false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lines2);
                roadLinesBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, canvasWidth, canvasHeight, false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lines3);
                roadLinesBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, canvasWidth, canvasHeight, false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lines4);
                roadLinesBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, canvasWidth, canvasHeight, false));

                // loadOnThread cars' bitmaps
                blueCarBitmaps = new Vector<>();
                redCarBitmaps = new Vector<>();
//                greenCarBitmaps = new Vector<>();

                float ratio;

                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_blue_1);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                blueCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_blue_2);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                blueCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_blue_3);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                blueCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_blue_4);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                blueCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_blue_5);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                blueCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));

                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_red_1);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                redCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_red_2);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                redCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_red_3);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                redCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_red_4);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                redCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));
                tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_red_5);
                ratio = (canvasWidth / 3f) / tempBitmap.getWidth();
                redCarBitmaps.add(Bitmap.createScaledBitmap(tempBitmap, (int) (tempBitmap.getWidth() * ratio), (int) (tempBitmap.getHeight() * ratio), false));

                // loadOnThread top background (gray where the lights and the score are) bitmap
                final Bitmap topBackground = BitmapFactory.decodeResource(getResources(), R.drawable.top);
                scaledTopBackground = Bitmap.createScaledBitmap(topBackground, canvasWidth, canvasHeight / 5, false);

                // loadOnThread lights bitmaps
                final Bitmap unscaledBitmapLightOff = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_led_off);
                bitmapLightOff = Bitmap.createScaledBitmap(unscaledBitmapLightOff, canvasHeight / 10, canvasHeight / 10, false);
                final Bitmap unscaledBitmapLightOn = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_led_red);
                bitmapLightOn = Bitmap.createScaledBitmap(unscaledBitmapLightOn, canvasHeight / 10, canvasHeight / 10, false);

                gameStateMachine.setState(GameStateMachine.LOADED);
//                invalidate();
            }
        }).start();
    }

    public static final long RACE_DURATION = 2000L;

    public static final long MIN_MILESTONE = 1000L;
    public static final long MAX_MILESTONE = 5000L;
    private long milestone; // when the next light will go off

    private void randomizeMilestone()
    {
        milestone = System.currentTimeMillis() + (long) (MIN_MILESTONE + (MAX_MILESTONE - MIN_MILESTONE) * Math.random());
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(gameStateMachine.getState() == GameStateMachine.UNINITIALIZED)
        {
            // draw banner bitmap
            if(bannerBitmap == null)
            {
                bannerBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.banner), canvas.getWidth(), canvas.getHeight(), false);
                canvas.drawBitmap(bannerBitmap, 0, 0, null);
                invalidate();
            }
            gameStateMachine.setState(GameStateMachine.LOADING);
            loadOnThread();
        }
        else if(gameStateMachine.getState() == GameStateMachine.LOADING)
        {
            canvas.drawBitmap(bannerBitmap, 0, 0, null);
            invalidate();
        }
        else
        {
            if(gameStateMachine.getState() == GameStateMachine.LOADED)
            {
                gameStateMachine.setState(GameStateMachine.DIALOG_GET_READY);
                // show pop-up
                activity.callback();
            }
            else if(gameStateMachine.getState() == GameStateMachine.DIALOG_GET_READY)
            {
                // empty - showing dialog to get ready
            }
            else if(gameStateMachine.getState() == GameStateMachine.RESET)
            {
                // reset milestone
                milestone = 0L;
                scorePlayer = 0;
                scoreEnemy = 0;
                gameParameters.setTargetTimestamp(0L);
                final boolean advanced = gameParameters.isAdvanced();
                int bestScore = getContext()
                        .getSharedPreferences("cy.ac.uclancyprus.zg", Context.MODE_PRIVATE)
                        .getInt(advanced ? ActivityMain.PREFERENCE_KEY_BEST_SCORE_ADVANCED : ActivityMain.PREFERENCE_KEY_BEST_SCORE_BEGINNERS, 1000);
                gameParameters.setEnemyDelay(bestScore);
                gameStateMachine.setState(GameStateMachine.STAGE_1_ALL_GRAY);
            }
            else if(gameStateMachine.getState() == GameStateMachine.STAGE_1_ALL_GRAY)
            {
                lightStates.set(0, false);
                lightStates.set(1, false);
                lightStates.set(2, false);
                lightStates.set(3, false);
                lightStates.set(4, false);
                if(milestone == 0) randomizeMilestone();
                if(System.currentTimeMillis() > milestone)
                {
                    gameStateMachine.setState(GameStateMachine.STAGE_2);
                    randomizeMilestone();
                }
            }
            else if(gameStateMachine.getState() == GameStateMachine.STAGE_2)
            {
                lightStates.set(0, true);
                if(System.currentTimeMillis() > milestone)
                {
                    gameStateMachine.setState(GameStateMachine.STAGE_3);
                    randomizeMilestone();
                }
            }
            else if(gameStateMachine.getState() == GameStateMachine.STAGE_3)
            {
                lightStates.set(1, true);
                if(System.currentTimeMillis() > milestone)
                {
                    gameStateMachine.setState(GameStateMachine.STAGE_4);
                    randomizeMilestone();
                }
            }
            else if(gameStateMachine.getState() == GameStateMachine.STAGE_4)
            {
                lightStates.set(2, true);
                if(System.currentTimeMillis() > milestone)
                {
                    gameStateMachine.setState(GameStateMachine.STAGE_5);
                    randomizeMilestone();
                }
            }
            else if(gameStateMachine.getState() == GameStateMachine.STAGE_5)
            {
                lightStates.set(3, true);
                if(System.currentTimeMillis() > milestone)
                {
                    gameStateMachine.setState(GameStateMachine.STAGE_6);
                    randomizeMilestone();
                }
            }
            else if(gameStateMachine.getState() == GameStateMachine.STAGE_6)
            {
                lightStates.set(4, true);
                if(System.currentTimeMillis() > milestone)
                {
                    gameStateMachine.setState(GameStateMachine.RACE_STARTED);
                    milestone = System.currentTimeMillis() + RACE_DURATION;
                }
            }
            else if(gameStateMachine.getState() == GameStateMachine.RACE_STARTED)
            {
                lightStates.set(0, false);
                lightStates.set(1, false);
                lightStates.set(2, false);
                lightStates.set(3, false);
                lightStates.set(4, false);
                if(System.currentTimeMillis() > milestone)
                {
                    gameStateMachine.setState(GameStateMachine.RACE_STOPPED);
                    // update score
                    scorePlayer = gameParameters.getPlayerDelay();
                    scoreEnemy = gameParameters.getEnemyDelay();
                }
            }
            else if(gameStateMachine.getState() == GameStateMachine.RACE_STOPPED)
            {
                gameStateMachine.setState(GameStateMachine.DIALOG_AFTER_STOPPED);
                // show pop-up
                activity.callback();
            }
            else if(gameStateMachine.getState() == GameStateMachine.DIALOG_AFTER_STOPPED)
            {
                // empty - showing dialog with results
            }

            // draw background
            canvas.drawBitmap(scaledBackground, 0, 0, null);

            // draw road lines (with animation if needed)
            canvas.drawBitmap(roadLinesBitmaps.get(selectedRoadLinesIndex), 0, 0, null);

            // draw cars
            // todo decide which car is the player's and which one the enemy's
            final Bitmap playerBitmap = blueCarBitmaps.get(selectedTiltIndex);
            final Bitmap enemyBitmap = redCarBitmaps.get(selectedTiltIndex);
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            int carWidth = playerBitmap.getWidth();
            int carHeight = playerBitmap.getHeight();
            canvas.drawBitmap(playerBitmap, canvasWidth / 4 - carWidth / 2, canvasHeight - carHeight, null);
            canvas.drawBitmap(enemyBitmap, 3 * canvasWidth / 4 - carWidth / 2, canvasHeight - carHeight, null);

            // draw top (gray where the lights and the score are)
            canvas.drawBitmap(scaledTopBackground, 0, 0, null);

            // draw the lights
            drawLights(canvas);

            // draw the score
            drawScore(canvas);

            if(gameParameters.isAdvanced() &&
                    (gameStateMachine.getState() == GameStateMachine.STAGE_1_ALL_GRAY
                    || gameStateMachine.getState() == GameStateMachine.STAGE_2
                    || gameStateMachine.getState() == GameStateMachine.STAGE_3
                    || gameStateMachine.getState() == GameStateMachine.STAGE_4
                    || gameStateMachine.getState() == GameStateMachine.STAGE_5
                    || gameStateMachine.getState() == GameStateMachine.STAGE_6))
            {
                drawMovingTarget(canvas);
                drawCrosshair(canvas);
            }

            rippleStep(canvas);
        }
    }

    private void drawLights(final Canvas canvas)
    {
        int width = canvas.getWidth();
        float slotWidth = 1f * width / lightStates.size();
        float bitmapWidth = 1f * bitmapLightOff.getWidth();
        float leftInset = (slotWidth - bitmapWidth) / 2f;
        float top = 0f; // common y (top)
        for(int i = 0; i < lightStates.size(); i++)
        {
            float left = i * slotWidth + leftInset;
            canvas.drawBitmap(lightStates.get(i) ? bitmapLightOn : bitmapLightOff, left, top, null);
        }
    }

    private void drawScore(final Canvas canvas)
    {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        long playerSeconds = scorePlayer / 1000;
        long playerMilliseconds = scorePlayer % 1000;
        long enemySeconds  = scoreEnemy / 1000;
        long enemyMilliseconds = scoreEnemy % 1000;
        String scorePlayerS = playerSeconds < 10 ? "0" + playerSeconds + ":" : "" + playerSeconds + ":";
        scorePlayerS = scorePlayerS + (playerMilliseconds < 10 ? "00" + playerMilliseconds : (playerMilliseconds < 100 ? "0" + playerMilliseconds : "" + playerMilliseconds));
        String scoreEnemyS = enemySeconds < 10 ? "0" + enemySeconds + ":" : "" + enemySeconds + ":";
        scoreEnemyS = scoreEnemyS + (enemyMilliseconds < 10 ? "00" + enemyMilliseconds : (enemyMilliseconds < 100 ? "0" + enemyMilliseconds : "" + enemyMilliseconds));

        float textWidthPlayer = paintScore.measureText(scorePlayerS);
        float textWidthEnemy  = paintScore.measureText(scoreEnemyS);
        int xPosPlayer = (int) (width / 4 - textWidthPlayer / 2);
        int xPosEnemy  = (int) (3 * width / 4 - textWidthEnemy / 2);

        int yPos = (int) ((0.15d * height) - ((paintScore.descent() + paintScore.ascent()) / 2)) ;

        canvas.drawText(scorePlayerS, xPosPlayer, yPos, paintScore);
        canvas.drawText(scoreEnemyS, xPosEnemy, yPos, paintScore);
    }

    public static final int TARGET_COLOR = Color.rgb(0, 0, 0);
    public static final float TARGET_RADIUS = 80f;
    public static final float TARGET_Y_TOP_PERCENTAGE = 0.5f; // a value in 0..1 to indicate the ratio where Y is from top
    public static final float STROKE_WIDTH = 2f;

    private int offsetX = 0;
    private int offsetY = 0;

    private float lastCrosshairX;
    private float lastCrosshairY;

    int getCrosshairPenalty()
    {
        return (int) Math.sqrt(Math.pow(lastMovingTargetX - lastCrosshairX, 2) + Math.pow(lastMovingTargetY - lastCrosshairY, 2));
    }

    private void drawCrosshair(final Canvas canvas)
    {
        final float targetX = canvas.getWidth() / 2 + offsetX;
        final float targetY = canvas.getHeight() * TARGET_Y_TOP_PERCENTAGE + offsetY;
        final Paint paint = new Paint();
        paint.setColor(TARGET_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        canvas.drawCircle(targetX, targetY, TARGET_RADIUS, paint);
        canvas.drawLine(targetX - TARGET_RADIUS * 1.1f, targetY, targetX + TARGET_RADIUS * 1.1f, targetY, paint);
        canvas.drawLine(targetX, targetY - TARGET_RADIUS * 1.1f, targetX, targetY + TARGET_RADIUS * 1.1f, paint);

        lastCrosshairX = targetX;
        lastCrosshairY = targetY;
    }

    private float rippleX = 0;
    private float rippleY = 0;
    private long ripple_timestamp;
    public static final long RIPPLE_DURATION = 500L; // 1 second
    public static final float MAX_RIPPLE_RADIUS = 200f;
    public static final int RIPPLE_COLOR = Color.rgb(255, 255, 255);

    private void rippleStep(final Canvas canvas)
    {
        final float interval = System.currentTimeMillis() - ripple_timestamp;
        if(interval < RIPPLE_DURATION)
        {
            final float rippleRadius = MAX_RIPPLE_RADIUS * interval / RIPPLE_DURATION;
            final Paint paint = new Paint();
            paint.setColor(RIPPLE_COLOR);
            paint.setAlpha((int) (255 * (1 - interval / RIPPLE_DURATION)));
            canvas.drawCircle(rippleX, rippleY, rippleRadius, paint);
        }
    }

    private int movingTargetMaxOffsetX = 32;
    private int movingTargetMaxOffsetY = 32;
    public static final int MOVING_TARGET_RADIUS = 32;
    public static final int MOVING_TARGET_COLOR = Color.rgb(250, 60, 70);
    double timeX = 0d;
    double timeY = 0d;
    private float movingTargetOffsetX = 0;
    private float movingTargetOffsetY = 0;
    private float movingTargetSpeedX = 0;
    private float movingTargetSpeedY = 0;

    public static final double TWO_PI = 2d * Math.PI;

    private float lastMovingTargetX;
    private float lastMovingTargetY;

    private void drawMovingTarget(final Canvas canvas)
    {
        lastMovingTargetX = canvas.getWidth() / 2 // screen center
                + (float) (Math.sin(timeX + movingTargetOffsetX) * movingTargetMaxOffsetX);
        lastMovingTargetY = canvas.getHeight() / 2 // screen center
                + (float) (Math.sin(timeY + movingTargetOffsetY) * movingTargetMaxOffsetY);

        final Paint paint = new Paint();
        paint.setColor(MOVING_TARGET_COLOR);
        canvas.drawCircle(lastMovingTargetX, lastMovingTargetY, MOVING_TARGET_RADIUS, paint);
    }

    void startWorkerThread()
    {
        new WorkerThread().start();
    }

    /**
     * Used to periodically update the GameView
     */
    class WorkerThread extends Thread
    {
        public static final long WORKER_THREAD_DELAY = 25L; // in ms

        @Override
        public void run()
        {
            while(gameStateMachine.getState() != GameStateMachine.ACTIVITY_CLOSED)
            {
                // move target ball
                timeX += movingTargetSpeedX;
                if(timeX > TWO_PI) timeX -= TWO_PI;
                timeY += movingTargetSpeedY;
                if(timeY > TWO_PI) timeY -= TWO_PI;

                if(gameStateMachine.getState() == GameStateMachine.RACE_STARTED)
                {
                    // update target timestamp
                    if(gameParameters.getTargetTimestamp() == 0) gameParameters.setTargetTimestamp(System.currentTimeMillis());

                    // update lines
                    selectedRoadLinesIndex++;
                    if(selectedRoadLinesIndex >= roadLinesBitmaps.size()) selectedRoadLinesIndex = 0;
                }

                // update UI
                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        invalidate();
                    }
                });
                try { sleep(WORKER_THREAD_DELAY); } catch(InterruptedException ie) { /* nothing */ }
            }
        }
    }

    // API used by other classes
    void setTargetOffset(final int offsetX, final int offsetY)
    {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        invalidate();
    }

    private int selectedTiltIndex = 2; // 0=large tilt left, 1=small tilt left, 2=balanced, 3=small tilt right, 4=large tilt right
    void setTiltIndex(final int tildIndex)
    {
        this.selectedTiltIndex = tildIndex;
    }

    void setMovingTargetSpeed(final float movingTargetSpeedX, final float movingTargetSpeedY,
                              final int movingTargetMaxOffsetX, final int movingTargetMaxOffsetY)
    {
        this.movingTargetMaxOffsetX = movingTargetMaxOffsetX;
        this.movingTargetMaxOffsetY = movingTargetMaxOffsetY;
        this.movingTargetOffsetX = (float) (Math.random() * TWO_PI);
        this.movingTargetOffsetY = (float) (Math.random() * TWO_PI);
        this.movingTargetSpeedX = movingTargetSpeedX;
        this.movingTargetSpeedY = movingTargetSpeedY;
        invalidate();
    }

    void ripple(final float x, final float y)
    {
        rippleX = x;
        rippleY = y;
        ripple_timestamp = System.currentTimeMillis();
        invalidate();
    }
}