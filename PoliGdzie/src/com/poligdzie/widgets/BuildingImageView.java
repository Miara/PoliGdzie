package com.poligdzie.widgets;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class BuildingImageView extends ImageView{

	
	
	public static final String TAG = "test";

    private static final int INVALID_POINTER_ID = -1;

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;

    private Bitmap bitmap;
    private float viewHeight;
    private float viewWidth;
    float canvasWidth, canvasHeight;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float minScaleFactor;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX, mLastTouchY;

    private boolean firstDraw = true;

    private boolean panEnabled = true;
    private boolean zoomEnabled = true;
    
    public BuildingImageView(Context context) {
        super(context);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        this.setMeasuredDimension(100, 100);
        Log.i("poligdzie","W:"+this.getWidth()+":H:"+this.getHeight());
        this.setWillNotDraw(false);
        setup();
    }

    public BuildingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    public BuildingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public void setBitmap(Bitmap bmp) {
        setImageBitmap(bmp);
    }

    public void setImageBitmap(Bitmap bmp) {
    	if(bmp == null)
    	{
    		Log.i("poligdzie","null bmp");
    	}
    	else
    	{
    		Log.i("poligdzie","not null bmp");
    	}
        bitmap = bmp;
        resetZoom();
        resetPan();
        firstDraw = true;
        invalidate();
    }

    public Bitmap getImageBitmap() {
        return bitmap;
    }

    public Bitmap getBitmap() {
        return getImageBitmap();
    }

    public void resetZoom() {
        mScaleFactor = 1.0f;
    }

    public void resetPan() {
        mPosX = 0f;
        mPosY = 0f;
    }

    public void setImageDrawable(Drawable drawable) {
        setImageBitmap(((BitmapDrawable) drawable).getBitmap());
    }

    public BitmapDrawable getImageDrawable() {
        BitmapDrawable bd = new BitmapDrawable(getContext().getResources(), bitmap);
        return bd;
    }

    public BitmapDrawable getDrawable() {
        return getImageDrawable();
    }

    public void onDraw(Canvas canvas) {
//      Log.v(TAG, "onDraw()");
    	Log.i("poligdzie","on draw 0");
        if (bitmap == null) {
        	Log.i("poligdzie","on draw 1");
            Log.w(TAG, "nothing to draw - bitmap is null");
            super.onDraw(canvas);
            return;
        }

        if (firstDraw 
                && (bitmap.getHeight() > 0) 
                && (bitmap.getWidth() > 0)) {
        	Log.i("poligdzie","on draw 2");
            //Don't let the user zoom out so much that the image is smaller
            //than its containing frame
            float minXScaleFactor = (float) viewWidth / (float) bitmap.getWidth();
            float minYScaleFactor = (float) viewHeight / (float) bitmap.getHeight();
            minScaleFactor = Math.max(minXScaleFactor, minYScaleFactor);
            Log.d(TAG, "minScaleFactor: " + minScaleFactor);
            mScaleFactor = minScaleFactor; //start out "zoomed out" all the way

            mPosX = mPosY = 0;
            firstDraw = false;

        }
        mScaleFactor = Math.max(mScaleFactor, minScaleFactor);

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
//      Log.d(TAG, "canvas density: " + canvas.getDensity() + " bitmap density: " + bitmap.getDensity());

//      Log.d(TAG, "mScaleFactor: " + mScaleFactor);

        //Save the canvas without translating (panning) or scaling (zooming)
        //After each change, restore to this state, instead of compounding
        //changes upon changes
        canvas.save();
        int maxX, minX, maxY, minY;
        //Regardless of the screen density (HDPI, MDPI) or the scale factor, 
        //The image always consists of bitmap width divided by 2 pixels. If an image
        //is 200 pixels wide and you scroll right 100 pixels, you just scrolled the image
        //off the screen to the left.
        minX = (int) (((viewWidth / mScaleFactor) - bitmap.getWidth()) / 2);
        maxX = 0;
        //How far can we move the image vertically without having a gap between image and frame?
        minY = (int) (((viewHeight / mScaleFactor) - bitmap.getHeight()) / 2);
        maxY = 0;
        Log.d(TAG, "minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
        //Do not go beyond the boundaries of the image
        if (mPosX > maxX) {
            mPosX = maxX;
        }
        if (mPosX < minX) {
            mPosX = minX;
        }
        if (mPosY > maxY) {
            mPosY = maxY;
        }
        if (mPosY < minY) {
            mPosY = minY;
        }

//      Log.d(TAG, "view width: " + viewWidth + " view height: "
//              + viewHeight);
//      Log.d(TAG, "bitmap width: " + bitmap.getWidth() + " height: " + bitmap.getHeight());
//      Log.d(TAG, "translating mPosX: " + mPosX + " mPosY: " + mPosY);

//      Log.d(TAG, "zooming to scale factor of " + mScaleFactor);
        canvas.scale(mScaleFactor, mScaleFactor);

//      Log.d(TAG, "panning to " + mPosX + "," + mPosY); 
        canvas.translate(mPosX, mPosY);

        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, mPosX, mPosY, null);
        canvas.restore(); //clear translation/scaling
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        if (zoomEnabled) {
            mScaleDetector.onTouchEvent(ev);
        }

        if (panEnabled) {
            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    final float x = ev.getX();
                    final float y = ev.getY();

                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = ev.getPointerId(0);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);

                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!mScaleDetector.isInProgress()) {
                        float dx = x - mLastTouchX;
                        float dy = y - mLastTouchY;

                        //Adjust for zoom factor. Otherwise, the user's finger moving 10 pixels
                        //at 200% zoom causes the image to slide 20 pixels instead of perfectly
                        //following the user's touch
                        dx /= (mScaleFactor * 2);
                        dy /= (mScaleFactor * 2);

                        mPosX += dx;
                        mPosY += dy;

                        Log.v(TAG, "moving by " + dx + "," + dy + " mScaleFactor: " + mScaleFactor);

                        invalidate();
                    }

                    mLastTouchX = x;
                    mLastTouchY = y;

                    break;
                }

                case MotionEvent.ACTION_UP: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {
                    final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = ev.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = ev.getX(newPointerIndex);
                        mLastTouchY = ev.getY(newPointerIndex);
                        mActivePointerId = ev.getPointerId(newPointerIndex);
                    }
                    break;
                }
            }
        }
        return true;
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
//          Log.d(TAG, "detector scale factor: " + detector.getScaleFactor() + " mscalefactor: " + mScaleFactor);

            invalidate();
            return true;
        }
    }

    //Currently zoomEnabled/panEnabled can only be set programmatically, not in XML

    public boolean isPanEnabled() {
        return panEnabled;
    }

    public void setPanEnabled(boolean panEnabled) {
        this.panEnabled = panEnabled;
    }

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }

    /**
     * Calls getCroppedBitmap(int outputWidth, int outputHeight) without
     * scaling the resulting bitmap to any specific size.
     * @return
     */
    public Bitmap getCroppedBitmap() {
        return getCroppedBitmap(0, 0);
    }

    /**
     * Takes the section of the bitmap visible in its View object
     * and exports that to a Bitmap object, taking into account both
     * the translation (panning) and zoom (scaling).
     * WARNING: run this in a separate thread, not on the UI thread!
     * If you specify that a 200x200 image should have an outputWidth
     * of 400 and an outputHeight of 50, the image will be squished
     * and stretched to those dimensions.
     * @param outputWidth desired width of output Bitmap in pixels
     * @param outputHeight desired height of output Bitmap in pixels
     * @return the visible portion of the image in the PanZoomImageView
     */
    public Bitmap getCroppedBitmap(int outputWidth, int outputHeight) {
        int origX = -1 * (int) mPosX * 2;
        int origY = -1 * (int) mPosY * 2;
        int width = (int) (viewWidth / mScaleFactor);
        int height = (int) (viewHeight / mScaleFactor);
        Log.e(TAG, "origX: " + origX + " origY: " + origY + " width: " + width + " height: " + height + " outputWidth: " + outputWidth + " outputHeight: " + outputHeight + "getLayoutParams().width: " + getLayoutParams().width + " getLayoutParams().height: " + getLayoutParams().height);
        Bitmap b = Bitmap.createBitmap(bitmap, origX, origY, width, height);

        if (outputWidth > 0 && outputWidth > 0) {
            //Use the exact dimensions given--chance this won't match the aspect ratio
            b = Bitmap.createScaledBitmap(b, outputWidth, outputHeight, true);
        }

        return b;
    }

      @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            viewHeight = h;
            viewWidth = w;
        }
      
      @Override
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

          int desiredWidth = 200;
          int desiredHeight = 400;

          int widthMode = MeasureSpec.getMode(widthMeasureSpec);
          int widthSize = MeasureSpec.getSize(widthMeasureSpec);
          int heightMode = MeasureSpec.getMode(heightMeasureSpec);
          int heightSize = MeasureSpec.getSize(heightMeasureSpec);

          int width;
          int height;

          //Measure Width
          if (widthMode == MeasureSpec.EXACTLY) {
              //Must be this size
              width = widthSize;
          } else if (widthMode == MeasureSpec.AT_MOST) {
              //Can't be bigger than...
              width = Math.min(desiredWidth, widthSize);
          } else {
              //Be whatever you want
              width = desiredWidth;
          }

          //Measure Height
          if (heightMode == MeasureSpec.EXACTLY) {
              //Must be this size
              height = heightSize;
          } else if (heightMode == MeasureSpec.AT_MOST) {
              //Can't be bigger than...
              height = Math.min(desiredHeight, heightSize);
          } else {
              //Be whatever you want
              height = desiredHeight;
          }

          //MUST CALL THIS
          setMeasuredDimension(width, height);
      }

}
