package cy.ac.uclancyprus.zg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import cy.ac.uclancyprus.zg.util.BaseGameActivity;

public class ActivityLeaderboard
        extends BaseGameActivity
{
    public static final String TAG = "zg.ActivityLeaderboard";

    public static final int REQUEST_LEADERBOARD_ADVANCED  = 10007;
    public static final int REQUEST_LEADERBOARD_BEGINNERS = 10008;

    public static final String INTENT_KEY_NEW_HIGH_SCORE        = "high_score";
    public static final String INTENT_KEY_LEADERBOARD_ADVANCED  = "leaderboard_advanced";
    public static final String INTENT_KEY_LEADERBOARD_BEGINNERS = "leaderboard_beginners";

    @Override
    protected void onCreate(Bundle b)
    {
        super.onCreate(b);
        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onSignInFailed()
    {
        new AlertDialog.Builder(this, R.style.ThemeDialogCustom)
                .setMessage(R.string.gamehelper_sign_in_failed)
                .setPositiveButton(R.string.Dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onSignInSucceeded()
    {
        handleIntent(getIntent());
    }

    private void handleIntent(final Intent intent)
    {
        final GoogleApiClient googleApiClient = getApiClient();

        String leaderboardId;
        int requestCode;
        if(intent.hasExtra(INTENT_KEY_LEADERBOARD_ADVANCED))
        {
            leaderboardId = getString(R.string.leaderboard_fastest_response_time_advanced);
            requestCode = REQUEST_LEADERBOARD_ADVANCED;
        }
        else // assume intent.hasExtra(INTENT_KEY_LEADERBOARD_BEGINNERS)
        {
            leaderboardId = getString(R.string.leaderboard_fastest_response_time_beginners);
            requestCode = REQUEST_LEADERBOARD_BEGINNERS;
        }

        Log.d(TAG, "handleIntent() --> " + intent.getIntExtra(INTENT_KEY_NEW_HIGH_SCORE, -1));
        if(intent.hasExtra(INTENT_KEY_NEW_HIGH_SCORE))
        {
            Log.d(TAG, "intent.hasExtra(INTENT_KEY_NEW_HIGH_SCORE) -> " + intent.hasExtra(INTENT_KEY_NEW_HIGH_SCORE));
            final int highScore = intent.getIntExtra(INTENT_KEY_NEW_HIGH_SCORE, -1);
            if(highScore != -1)
            {
                Games.Leaderboards.submitScore(googleApiClient, leaderboardId, highScore);

//                // handle achievements
//                if(highScore < 500)
//                {
//                    Games.Achievements.unlock(googleApiClient, getString(R.string.achievement_faster_than_500ms));
//                }
//                if(highScore < 400)
//                {
//                    Games.Achievements.increment(googleApiClient, getString(R.string.achievement_faster_than_400ms_2_times), 1);
//                }
//                if(highScore < 300)
//                {
//                    Games.Achievements.increment(googleApiClient, getString(R.string.achievement_faster_than_300ms_3_times), 1);
//                }
//                if(highScore < 200)
//                {
//                    Games.Achievements.increment(googleApiClient, getString(R.string.achievement_faster_than_200ms_4_times), 1);
//                }
//                if(highScore < 100)
//                {
//                    Games.Achievements.increment(googleApiClient, getString(R.string.achievement_faster_than_100ms_5_times), 1);
//                }
            }
        }
        final Intent leaderboardIntent = Games.Leaderboards.getLeaderboardIntent(googleApiClient, leaderboardId);
        Log.d(TAG, "leaderboardId: " + leaderboardId);
        Log.d(TAG, "Starting intent: " + leaderboardIntent);
        startActivityForResult(leaderboardIntent, requestCode);
        finish();
    }

}