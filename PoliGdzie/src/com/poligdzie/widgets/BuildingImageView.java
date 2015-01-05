package com.poligdzie.widgets;

import com.poligdzie.interfaces.Constants;

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

public class BuildingImageView extends ImageView implements Constants
{

	private int						mActivePointerId	= INVALID_CODE;

	private Bitmap					bitmap;
	private float					viewHeight;
	private float					viewWidth;
	float							canvasWidth, canvasHeight;

	long							startTime, stopTime;
	int								clickCount;
	private long					duration;

	private ScaleGestureDetector	mScaleDetector;
	private float					mScaleFactor		= 1.f;
	private float					minScaleFactor;

	private float					mPosX;
	private float					mPosY;

	private float					mLastTouchX, mLastTouchY;

	private boolean					firstDraw			= true;

	private boolean					panEnabled			= true;
	private boolean					zoomEnabled			= true;

	public BuildingImageView(Context context)
	{
		super(context);
		setup();
	}

	public BuildingImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setup();
	}

	public BuildingImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setup();
	}

	private void setup()
	{
		mScaleDetector = new ScaleGestureDetector(getContext(),
				new ScaleListener());
	}

	public void setBitmap(Bitmap bmp)
	{
		setImageBitmap(bmp);
	}

	public void setImageBitmap(Bitmap bmp)
	{
		bitmap = bmp;
		mScaleFactor = 1.0f;
		mPosX = mPosY = 0f;
	}

	public Bitmap getImageBitmap()
	{
		return bitmap;
	}

	public Bitmap getBitmap()
	{
		return getImageBitmap();
	}

	public void setImageDrawable(Drawable drawable)
	{
		setImageBitmap(((BitmapDrawable) drawable).getBitmap());
	}

	public BitmapDrawable getImageDrawable()
	{
		BitmapDrawable bd = new BitmapDrawable(getContext().getResources(),
				bitmap);
		return bd;
	}

	public BitmapDrawable getDrawable()
	{
		return getImageDrawable();
	}

	public void onDraw(Canvas canvas)
	{
		if (bitmap == null)
		{
			super.onDraw(canvas);
			return;
		}

		mScaleFactor = Math.max(mScaleFactor, minScaleFactor);

		canvasHeight = canvas.getHeight();
		canvasWidth = canvas.getWidth();
		// Log.d(TAG, "canvas density: " + canvas.getDensity() +
		// " bitmap density: " + bitmap.getDensity());
		// Log.d(TAG, "mScaleFactor: " + mScaleFactor);

		canvas.save();
		int maxX, minX, maxY, minY;
		minX = (int) (((viewWidth / mScaleFactor) - bitmap.getWidth()) / 2);
		maxX = 0;
		// How far can we move the image vertically without having a gap between
		// image and frame?
		minY = (int) (((viewHeight / mScaleFactor) - bitmap.getHeight()) / 2);
		maxY = 0;
		Log.d(TAG, "minX: " + minX + " maxX: " + maxX + " minY: " + minY
				+ " maxY: " + maxY);
		// Do not go beyond the boundaries of the image

		if (mPosX > maxX)
			mPosX = maxX;
		if (mPosX < minX)
			mPosX = minX;

		if (mPosY > maxY)
			mPosY = maxY;
		if (mPosY < minY)
			mPosY = minY;

		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.translate(mPosX, mPosY);

		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, mPosX, mPosY, null);
		canvas.restore(); // clear translation/scaling
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (zoomEnabled)
		{
			mScaleDetector.onTouchEvent(ev);
		}

		if (panEnabled)
		{
			final int action = ev.getAction();
			switch (action & MotionEvent.ACTION_MASK)
			{

			case MotionEvent.ACTION_DOWN:
			{
				mLastTouchX = ev.getX();
				mLastTouchY = ev.getY();
				mActivePointerId = ev.getPointerId(0);

				if ((System.currentTimeMillis() - startTime) > DOUBLE_CLICK_DURATION)
				{
					startTime = System.currentTimeMillis();
					clickCount = 1;
				} else
				{
					clickCount++;
				}

				break;
			}

			case MotionEvent.ACTION_MOVE:
			{
				final int pointerIndex = ev.findPointerIndex(mActivePointerId);
				final float x = ev.getX(pointerIndex);
				final float y = ev.getY(pointerIndex);

				if (!mScaleDetector.isInProgress())
				{
					float dx = x - mLastTouchX;
					float dy = y - mLastTouchY;

					dx /= (mScaleFactor * 2);
					dy /= (mScaleFactor * 2);

					mPosX += dx;
					mPosY += dy;

					invalidate();
				}

				mLastTouchX = x;
				mLastTouchY = y;

				break;
			}

			case MotionEvent.ACTION_UP:
			{
				mActivePointerId = INVALID_CODE;

				if (clickCount == 2)
				{
					long duration = System.currentTimeMillis() - startTime;
					if (duration <= DOUBLE_CLICK_DURATION)
					{
						mScaleFactor *= VIEW_ZOOM_IN;
						invalidate();
					}
					clickCount = 0;
					duration = 0;
				}
				break;
			}

			case MotionEvent.ACTION_CANCEL:
			{
				mActivePointerId = INVALID_CODE;
				break;
			}

			case MotionEvent.ACTION_POINTER_UP:
			{
				final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				final int pointerId = ev.getPointerId(pointerIndex);
				if (pointerId == mActivePointerId)
				{// TODO: zmienic skladnie
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

	private class ScaleListener
		extends
		ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			mScaleFactor *= detector.getScaleFactor();
			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			// Log.d(TAG, "detector scale factor: " + detector.getScaleFactor()
			// + " mscalefactor: " + mScaleFactor);

			invalidate();
			return true;
		}
	}

	// TODO: wyrzucic kontrole zooma i pana
	public boolean isPanEnabled()
	{
		return panEnabled;
	}

	public void setPanEnabled(boolean panEnabled)
	{
		this.panEnabled = panEnabled;
	}

	public boolean isZoomEnabled()
	{
		return zoomEnabled;
	}

	public void setZoomEnabled(boolean zoomEnabled)
	{
		this.zoomEnabled = zoomEnabled;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		viewHeight = h;
		viewWidth = w;

		if ((bitmap.getHeight() > 0) && (bitmap.getWidth() > 0))
		{
			float minXScaleFactor = (float) viewWidth
					/ (float) bitmap.getWidth();
			float minYScaleFactor = (float) viewHeight
					/ (float) bitmap.getHeight();
			minScaleFactor = Math.max(minXScaleFactor, minYScaleFactor);

			mScaleFactor = minScaleFactor;
			mPosX = mPosY = 0;

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		int desiredWidth = 200;
		int desiredHeight = 400;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;
// TODO: dodac nawiasy klamrowe
		
		if (widthMode == MeasureSpec.EXACTLY)
			width = widthSize;
		else if (widthMode == MeasureSpec.AT_MOST)
			width = Math.min(desiredWidth, widthSize);
		else
			width = desiredWidth;

		if (heightMode == MeasureSpec.EXACTLY)
			height = heightSize;
		else if (heightMode == MeasureSpec.AT_MOST)
			height = Math.min(desiredHeight, heightSize);
		else
			height = desiredHeight;

		setMeasuredDimension(width, height);
	}

}
