package ashad.app.torch.flashlight.noticeaccepter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

public class Imageinzoom extends Activity implements OnTouchListener
{
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageinzoom);
        final ImageView view = (ImageView) findViewById(R.id.imageinzoomtoshow);
//        Glide.with(getApplicationContext())
//                .load("data:image/jpeg;base64,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")
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//
//                        return false;
//                    }
//                })
//                .into(view);

//        view.setImageResource(R.drawable.errore);
        view.setImageResource(R.mipmap.ic_launcher);
        Intent i=getIntent();
        String urlofimage=i.getStringExtra("urlofpic");
        Log.i("urlofpic","iss"+urlofimage);


        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();

        Glide.with(getApplicationContext())
                .load(urlofimage)
                .asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        view.setImageBitmap(resource);
                    }
                });


        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                //.d(TAG, "mode=DRAG"); // write to //Cat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                //.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                //.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    //.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    //.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the //Cat view, for debugging */
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        //.d("Touch Events ---------", sb.toString());
    }
}