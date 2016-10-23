package cy.ac.uclancyprus.zg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Nearchos Paspallis
 */
public class ActivityMain extends Activity
{
    public static final String TAG = "zg.ActivityMain";

    public static final String PREFERENCE_KEY_MUTED                 = "zg.muted";
    public static final String PREFERENCE_KEY_BEST_SCORE_BEGINNERS  = "zg.best_score.beginners";
    public static final String PREFERENCE_KEY_BEST_SCORE_ADVANCED   = "zg.best_score.advanced";

    private ImageButton playButton;
    private ImageButton muteUnmuteButton;
    private ImageButton leaderboardButton;
    private ImageButton facebookButton;
    private ImageButton websiteButton;
    private ImageButton aboutButton;

    private TextView versionTextView;

    private MediaPlayer mediaPlayer;
    private boolean muted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
Log.d(TAG, "zg->onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        playButton = (ImageButton) findViewById(R.id.activity_main_button_start_game);
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // show dialog to ask if the user wants to play beginners or advanced mode
                new AlertDialog.Builder(ActivityMain.this, R.style.ThemeDialogCustom)
                        .setTitle(R.string.Select_level)
                        .setMessage(R.string.Please_select_level)
                        .setNeutralButton(R.string.Beginners, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                final Intent intent = new Intent(ActivityMain.this, ActivityGame.class);
                                intent.putExtra(ActivityLeaderboard.INTENT_KEY_LEADERBOARD_BEGINNERS, true);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.Advanced, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                final Intent intent = new Intent(ActivityMain.this, ActivityGame.class);
                                intent.putExtra(ActivityLeaderboard.INTENT_KEY_LEADERBOARD_ADVANCED, true);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        });

        muteUnmuteButton = (ImageButton) findViewById(R.id.activity_main_button_mute_unmute);
        muteUnmuteButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                final SharedPreferences sharedPreferences
                        = ActivityMain.this.getSharedPreferences("cy.ac.uclancyprus.zg", Context.MODE_PRIVATE);
                muted = sharedPreferences.getBoolean(PREFERENCE_KEY_MUTED, false);
                sharedPreferences.edit().putBoolean(PREFERENCE_KEY_MUTED, !muted).apply();
                updateMutedMode();
            }
        });

        leaderboardButton = (ImageButton) findViewById(R.id.activity_main_button_leaderboard);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog to ask if the user wants to play beginners or advanced mode
                new AlertDialog.Builder(ActivityMain.this, R.style.ThemeDialogCustom)
                        .setTitle(R.string.Choose_leaderboard)
                        .setNeutralButton(R.string.Beginners, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                final Intent intent = new Intent(ActivityMain.this, ActivityLeaderboard.class);
                                intent.putExtra(ActivityLeaderboard.INTENT_KEY_LEADERBOARD_BEGINNERS, true);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.Advanced, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                final Intent intent = new Intent(ActivityMain.this, ActivityLeaderboard.class);
                                intent.putExtra(ActivityLeaderboard.INTENT_KEY_LEADERBOARD_ADVANCED, true);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        });

        facebookButton = (ImageButton) findViewById(R.id.activity_main_button_facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.facebook_url))));
            }
        });

        websiteButton = (ImageButton) findViewById(R.id.activity_main_button_website);
        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.website_url))));
            }
        });

        aboutButton = (ImageButton) findViewById(R.id.activity_main_button_about);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, ActivityAbout.class));
            }
        });

        versionTextView = (TextView) findViewById(R.id.activity_main_version_text_view);
        versionTextView.setText(getString(R.string.Version) + ": " + BuildConfig.VERSION_NAME);
//        // update versionTextView positioning
//        versionTextView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                final Display display = getWindowManager().getDefaultDisplay();
//                final Point size = new Point();
//                display.getSize(size);
//                final int screenHeight = size.y;
//                if (versionTextViewWidth == 0) versionTextViewWidth = right - left;
//                if (versionTextViewHeight == 0) versionTextViewHeight = bottom - top;
//                versionTextView.setLayoutParams(new AbsoluteLayout.LayoutParams(versionTextViewWidth, versionTextViewHeight, VERSION_TEXT_VIEW_MARGIN, screenHeight - VERSION_TEXT_VIEW_MARGIN - versionTextViewHeight));
//            }
//        });

        mediaPlayer = MediaPlayer.create(this, R.raw.flyhunter);
        mediaPlayer.setLooping(true);
    }

    private int versionTextViewWidth = 0;
    private int versionTextViewHeight = 0;

    private void updateMutedMode()
    {
        final SharedPreferences sharedPreferences
                = ActivityMain.this.getSharedPreferences("cy.ac.uclancyprus.zg", Context.MODE_PRIVATE);
        muted = sharedPreferences.getBoolean(PREFERENCE_KEY_MUTED, false);
        if(muted && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        else if(!muted && !mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
        muteUnmuteButton.setBackgroundResource(muted ? R.drawable.mute : R.drawable.unmute);
    }

    public static final int VERSION_TEXT_VIEW_MARGIN = 10;

    @Override
    protected void onResume()
    {
        super.onResume();
Log.d(TAG, "zg->onResume");

        positionButtons();

        // start playback music if needed
        final SharedPreferences sharedPreferences
                = ActivityMain.this.getSharedPreferences("cy.ac.uclancyprus.zg", Context.MODE_PRIVATE);
        muted = sharedPreferences.getBoolean(PREFERENCE_KEY_MUTED, false);
        muteUnmuteButton.invalidate();
        if(!muted)
        {
            if(!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
            }
        }
        muteUnmuteButton.setBackgroundResource(muted ? R.drawable.mute : R.drawable.unmute);
    }

    private void positionButtons()
    {
        // position the buttons
        final Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;
Log.d(TAG, "zg->height: " + screenHeight);
        final int iconSize = screenWidth / 4;

        int playButtonX = screenWidth / 2 - (int) (1.7 * iconSize);
        int playButtonY = screenHeight / 2 - 2 * iconSize;
        playButton.setLayoutParams(new AbsoluteLayout.LayoutParams(iconSize, iconSize, playButtonX, playButtonY));

        int muteUnmuteButtonX = screenWidth / 2 - iconSize / 2;
        int muteUnmuteButtonY = screenHeight / 2 - (int) (2.5 * iconSize);
        muteUnmuteButton.setLayoutParams(new AbsoluteLayout.LayoutParams(iconSize, iconSize, muteUnmuteButtonX, muteUnmuteButtonY));

        int leaderboardButtonX = screenWidth / 2 + (int) (0.7 * iconSize);
        int leaderboardButtonY = screenHeight / 2 - 2 * iconSize;
        leaderboardButton.setLayoutParams(new AbsoluteLayout.LayoutParams(iconSize, iconSize, leaderboardButtonX, leaderboardButtonY));

        int facebookButtonX = screenWidth / 2 - (int) (1.7 * iconSize);
        int facebookButtonY = screenHeight / 2 + (int) (1.2 * iconSize);
        facebookButton.setLayoutParams(new AbsoluteLayout.LayoutParams(iconSize, iconSize, facebookButtonX, facebookButtonY));

        int websiteButtonX = screenWidth / 2 - iconSize / 2;
        int websiteButtonY = screenHeight / 2 + (int) (1.7 * iconSize);
        websiteButton.setLayoutParams(new AbsoluteLayout.LayoutParams(iconSize, iconSize, websiteButtonX, websiteButtonY));

        int aboutButtonX = screenWidth / 2 + (int) (0.7 * iconSize);
        int aboutButtonY = screenHeight / 2 + (int) (1.2 * iconSize);
        aboutButton.setLayoutParams(new AbsoluteLayout.LayoutParams(iconSize, iconSize, aboutButtonX, aboutButtonY));
//
//        versionTextView.invalidate();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }
}