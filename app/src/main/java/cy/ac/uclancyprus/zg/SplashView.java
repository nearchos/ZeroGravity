package cy.ac.uclancyprus.zg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import java.util.Vector;

/**
 * @author Nearchos Paspallis
 * 17/07/2015.
 */
public class SplashView extends View
{
    public static final String TAG = "zg.AboutView";

    public static final long SPLASH_SCREEN_INTERVAL = 2000L; // how much time the splash screen is displayed

    private Bitmap scaledBackground = null;

    private int currentLoadingBitmap = 0;
    private Vector<Bitmap> loadingImageBitmaps = new Vector<>();

    public SplashView(final Context context)
    {
        super(context);

        // init images
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading1));
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading2));
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading3));
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading4));
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading5));
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading6));
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading7));
        loadingImageBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.loading8));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (scaledBackground == null)
        {
            final Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
            scaledBackground = Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), false);
        }
        canvas.drawBitmap(scaledBackground, 0, 0, null);

        final Bitmap loadingBitmap = loadingImageBitmaps.get(currentLoadingBitmap);
        canvas.drawBitmap(loadingBitmap, canvas.getWidth() / 2 - loadingBitmap.getWidth() / 2, canvas.getHeight() - 2 * loadingBitmap.getHeight(), null);
    }

    void startWorkerThread(final Activity activity)
    {
        new WorkerThread(activity).start();
    }

    /**
     * Used to periodically update the GameView
     */
    class WorkerThread extends Thread
    {
        public static final long WORKER_THREAD_DELAY = 100L; // in ms

        private Activity activity;
        private long startTimestamp;

        WorkerThread(final Activity activity)
        {
            this.activity = activity;
            this.startTimestamp = System.currentTimeMillis();
        }

        @Override
        public void run()
        {
            while(System.currentTimeMillis() < startTimestamp + SPLASH_SCREEN_INTERVAL)
            {
                // todo rotate
                currentLoadingBitmap++;
                if(currentLoadingBitmap >= loadingImageBitmaps.size()) currentLoadingBitmap = 0;
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
            activity.startActivity(new Intent(activity, ActivityMain.class));
        }
    }
}