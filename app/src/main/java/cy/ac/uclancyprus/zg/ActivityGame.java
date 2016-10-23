package cy.ac.uclancyprus.zg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Main game activity.
 * @author Nearchos Paspallis
 */
public class ActivityGame extends Activity implements SensorEventListener, View.OnTouchListener
{
    public static final String TAG = "zg.ActivityGame";

    private SensorManager sensorManager;
    private MediaPlayer mediaPlayerCircuit;
    private MediaPlayer mediaPlayerCapsule;
    private MediaPlayer mediaPlayerHorn;

    private GameView gameView = null;

    private GameStateMachine gameStateMachine;
    private GameParameters gameParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gameView = new GameView(this);
        gameStateMachine = new GameStateMachine();
        gameParameters = new GameParameters();
        gameView.setActivity(this);
        gameView.setGameStateMachine(gameStateMachine);
        gameView.setGameParameters(gameParameters);
        setContentView(gameView);

        gameView.startWorkerThread();
        gameView.setOnTouchListener(this);

        mediaPlayerCircuit = MediaPlayer.create(this, R.raw.circuit);
        mediaPlayerCircuit.setLooping(true);
        mediaPlayerCapsule = MediaPlayer.create(this, R.raw.capsule);
        mediaPlayerHorn = MediaPlayer.create(this, R.raw.horn);

        gameView.setMovingTargetSpeed(0.02f, 0.02f, 32, 32);//todo
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        gameStateMachine.setState(GameStateMachine.ACTIVITY_CLOSED);

        mediaPlayerCircuit.release();
        mediaPlayerCapsule.release();
        mediaPlayerHorn.release();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final Sensor sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // start playback music if needed
        final SharedPreferences sharedPreferences
                = getSharedPreferences("cy.ac.uclancyprus.zg", Context.MODE_PRIVATE);
        final boolean muted = sharedPreferences.getBoolean(ActivityMain.PREFERENCE_KEY_MUTED, false);
        if(!muted && !mediaPlayerCircuit.isPlaying())
        {
            mediaPlayerCircuit.start();
        }

        final Intent intent = getIntent();
        final boolean advanced = intent != null && intent.hasExtra(ActivityLeaderboard.INTENT_KEY_LEADERBOARD_ADVANCED);
        gameParameters.setAdvanced(advanced);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);

        mediaPlayerCircuit.pause();
    }

    public static final float THRESHOLD = 0.05f; // ~5%
    public static final float MAGNIFIER = 20f; // ~5%
    private float old_x = 0;
    private float old_y = -5;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1] - 5;
            // float z = sensorEvent.values[2]; // ignore z

            boolean changed = false;
            if(Math.abs(x - old_x) > THRESHOLD)
            {
                old_x = x;
                changed = true;
            }
            if(Math.abs(y - old_y) > THRESHOLD)
            {
                old_y = y;
                changed = true;
            }

            if(changed && gameView != null)
            {
                gameView.setTargetOffset(-(int) (old_x * MAGNIFIER), (int) (old_y * MAGNIFIER));
                int tiltOffset = (int) Math.max(-2.99, Math.min(x, 2.99)); // limit to -2..2
                tiltOffset += 2; // normalize to 0..4
                gameView.setTiltIndex(tiltOffset);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // ignore
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) // GameView touched
    {
        final long touchTimestamp = System.currentTimeMillis();
        gameParameters.setTouchTimestamp(touchTimestamp);
        final long targetTimestamp = gameParameters.getTargetTimestamp();
        if(targetTimestamp == 0 || touchTimestamp < targetTimestamp)
        {
            mediaPlayerHorn.start();
            // make sure the game stops and prepares for reset
            gameStateMachine.setState(GameStateMachine.DIALOG_GET_READY);

            // show dialog 'you lose'
            new AlertDialog.Builder(this, R.style.ThemeDialogCustom)
                    .setTitle(R.string.You_lose)
                    .setMessage(R.string.you_need_to_be_patient)
                    .setPositiveButton(R.string.Play_again, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            gameStateMachine.setState(GameStateMachine.RESET);
                        }
                    })
                    .setNegativeButton(R.string.Main_menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityGame.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }
        else // assume touchTimestamp >= targetTimestamp
        {
            long delay = gameParameters.getPlayerDelay();
            mediaPlayerCapsule.start();
        }

        gameView.ripple(event.getX(), event.getY());

        return false;
    }

    /**
     * Called back from the GameView when attention is needed (e.g. to show a pop-up)
     */
    void callback()
    {
        if(gameStateMachine.getState() == GameStateMachine.DIALOG_GET_READY)
        {
            final boolean advanced = gameParameters.isAdvanced();
            final String message = getString(R.string.When_all)
                    + (advanced ? "\n" + getString(R.string.Also_aim) : "");
            new AlertDialog.Builder(this, R.style.ThemeDialogCustom)
                    .setTitle(R.string.Get_ready)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            gameStateMachine.setState(GameStateMachine.RESET);
                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }
        else if(gameStateMachine.getState() == GameStateMachine.DIALOG_AFTER_STOPPED)
        {
            final SharedPreferences sharedPreferences
                    = getSharedPreferences("cy.ac.uclancyprus.zg", Context.MODE_PRIVATE);
            final boolean advanced = gameParameters.isAdvanced();
            final int bestScore = advanced ?
                    sharedPreferences.getInt(ActivityMain.PREFERENCE_KEY_BEST_SCORE_ADVANCED, Integer.MAX_VALUE)
                    :
                    sharedPreferences.getInt(ActivityMain.PREFERENCE_KEY_BEST_SCORE_BEGINNERS, Integer.MAX_VALUE);

            final int playerScore = advanced ?
                    (int) gameParameters.getPlayerDelay() + gameView.getCrosshairPenalty()
                    :
                    (int) gameParameters.getPlayerDelay();

            if(playerScore < bestScore) // less is better
            {
                Toast.makeText(ActivityGame.this, R.string.You_have_beaten, Toast.LENGTH_SHORT).show();

                // leaderboard
                sharedPreferences.edit().putInt(advanced ? ActivityMain.PREFERENCE_KEY_BEST_SCORE_ADVANCED : ActivityMain.PREFERENCE_KEY_BEST_SCORE_BEGINNERS, playerScore).apply();
                final Intent leaderboardIntent = new Intent(ActivityGame.this, ActivityLeaderboard.class);
                leaderboardIntent.putExtra(ActivityLeaderboard.INTENT_KEY_NEW_HIGH_SCORE, playerScore);
                leaderboardIntent.putExtra(gameParameters.isAdvanced() ? ActivityLeaderboard.INTENT_KEY_LEADERBOARD_ADVANCED : ActivityLeaderboard.INTENT_KEY_LEADERBOARD_BEGINNERS, true);
                startActivity(leaderboardIntent);
                finish();
            }
            else
            {
                new AlertDialog.Builder(this, R.style.ThemeDialogCustom)
                        .setTitle(R.string.Finish_line)
                        .setMessage("Your time: " + gameParameters.getPlayerDelay()
                                + "\nPenalty: " + gameView.getCrosshairPenalty()
                                + "\nTotal time: " + playerScore
                                + "\n\nYou have not beat your best time: " + bestScore)
                        .setPositiveButton(R.string.Play_again, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                gameStateMachine.setState(GameStateMachine.RESET);
                            }
                        })
                        .setNegativeButton(R.string.Main_menu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityGame.this.finish();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        }
    }
}