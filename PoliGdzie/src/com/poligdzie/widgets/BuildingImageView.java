package com.poligdzie.widgets;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.poligdzie.interfaces.Constants;
import com.poligdzie.route.Line;

public class BuildingImageView extends ImageView implements Constants
{

	private int						mActivePointerId	= INVALID_CODE;

	private Bitmap					bitmap;
	private float					viewHeight;
	private float					viewWidth;
	float							canvasWidth, canvasHeight;

	private int						originalWidth		= 0;
	private int						originalHeight		= 0;

	long							startTime, stopTime;
	int								clickCount;
	private long					duration;

	private ScaleGestureDetector	mScaleDetector;
	private float					mScaleFactor		= 1.f;
	private float					maxScaleFactor;

	private float					mPosX;
	private float					mPosY;

	private float					mLastTouchX, mLastTouchY;

	private boolean					firstDraw			= true;

	private boolean					panEnabled			= true;
	private boolean					zoomEnabled			= true;
	List<Line>						lines				= new ArrayList<Line>();

	private List<Line>				routeLines;
	
	CustomBitmapPoint		startPoint = null;
	CustomBitmapPoint		goalPoint = null;
	
	private boolean 	routeMode = false;

	private int	bitmapHeight;

	private int	bitmapWidth;
	
	private float					firstScale		= 0.44f;

	private float					firstX;
	private float					firstY;
	private boolean 				firstView=false;

	private int	radius;

	
	//TODO: zrefaktoryzowac klase, jest zbyt duza !!!
	
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
		lines = new ArrayList<Line>();

	}

	public void onDraw(Canvas canvas)
	{
		if(firstView)
		{
			SetPosAndScale();
			firstView=false;
		}
		//Log.i("SCALE","t1:"+mScaleFactor);
		if (bitmap == null)
		{
			super.onDraw(canvas);
			return;
		}
		
		
	    //Log.i("SCALE","t2:"+mScaleFactor);
		mScaleFactor = Math.max(mScaleFactor, maxScaleFactor);
		//Log.i("SCALE","t3:"+mScaleFactor);
		canvasHeight = canvas.getHeight();
		canvasWidth = canvas.getWidth();
		//Log.i("POSX1","X"+mPosX);
		//Log.i("POSY1","Y"+mPosY);
		
		
		canvas.save();
		int minX, maxX, minY, maxY;
		maxX = (int) (((viewWidth / mScaleFactor) - bitmap.getWidth()) / 2);
		minX = 0;

		maxY = (int) (((viewHeight / mScaleFactor) - bitmap.getHeight()) / 2);
		minY = 0;

		/*echo("------------------------");
		echo("BITMAP WIDTH:"+bitmap.getWidth());
		echo("BITMAP HEIGHT:"+bitmap.getHeight());
		
		echo("VIEW WIDTH:"+viewWidth);
		echo("VIEW HEIGHT:"+viewHeight);
		
		echo("MAXX:"+maxX);
		echo("MAXY:"+maxY);
		echo("------------------------");
		
		Log.i("POSX1","X"+mPosX);
		Log.i("POSY1","Y"+mPosY);*/
		
		if (mPosX > minX)
			mPosX = minX;
		if (mPosX < maxX)
			mPosX = maxX;

		if (mPosY > minY)
			mPosY = minY;
		if (mPosY < maxY)
			mPosY = maxY;
		
		//Log.i("POS2","X"+mPosX);
		//Log.i("POSY2","Y"+mPosY);
		
		//Log.i("SCALE","SCALE:"+mScaleFactor);
		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.translate(mPosX, mPosY);

		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, mPosX, mPosY, null);

		Paint redPaint = new Paint();
		redPaint.setColor(Color.RED);
		redPaint.setStrokeWidth(5);
		//TODO: przeniesc warunek is empty nizej pod nulla
		
		
		if(routeMode)
		{
			if (routeLines != null && !routeLines.isEmpty())
			{
				lines.clear();
				for (Line l : routeLines)
				{
					addLine(l);
				}
			}

			for (Line l : lines)
			{
				canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, redPaint);
			}
			if(startPoint.bmp != null)
			{
				float startX = (startPoint.x  - startPoint.bmp.getWidth()/2) * bitmapWidth / originalWidth + mPosX;
				float startY = (startPoint.y - startPoint.bmp.getHeight()/2) * bitmapHeight / originalHeight + mPosY;
				canvas.drawBitmap(startPoint.bmp, startX, startY, null);
			}
			if(goalPoint.bmp != null)
			{
				float startX = (goalPoint.x - goalPoint.bmp.getWidth()/2) * bitmapWidth / originalWidth + mPosX;
				float startY = (goalPoint.y- goalPoint.bmp.getHeight()/2) * bitmapHeight / originalHeight + mPosY;
				canvas.drawBitmap(goalPoint.bmp, startX, startY, null);
			}
		}
		

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
				float d = 1727 * viewWidth / bitmap.getWidth();
				Log.i("Poligdzie", "x:" + mLastTouchX + "-y:" + mLastTouchY
						+ "-W:" + viewWidth + "-H:" + viewHeight);
				Log.i("Poligdzie", "y:" + d + "-bitmap:" + bitmap.getWidth()
						+ "-view:" + viewWidth);

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
						mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
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
			maxScaleFactor = Math.max(minXScaleFactor, minYScaleFactor);

			mScaleFactor = maxScaleFactor;
			mPosX = mPosY = 0;

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		setMeasuredDimension(widthSize, heightSize);
	}

	public int getOriginalWidth()
	{
		return originalWidth;
	}

	public void setOriginalWidth(int originalWidth)
	{
		this.originalWidth = originalWidth;
	}

	public int getOriginalHeight()
	{
		return originalHeight;
	}

	public void setOriginalHeight(int originalHeight)
	{
		this.originalHeight = originalHeight;
	}

	private class CustomBitmapPoint
	{
		float x;
		float y;
		Bitmap bmp = null;
		
		public CustomBitmapPoint(float x,float y, Bitmap b)
		{
			this.x=x;
			this.y=y;
			this.bmp = b;
		}
	}

	public void setSearchCustomPoint(int x, int y,int radius)
	{
		echo("XXXX"+x);
		echo("XXXX"+y);
		this.startPoint = new CustomBitmapPoint(x, y, null);
		this.firstView = true;
		this.routeMode = false;
		this.radius = radius;
	}
	
	public void setStartCustomPoint(Bitmap bmp, int radius)
	{
		if(!routeLines.isEmpty())
		{
			float x = routeLines.get(0).startX;
			float y = routeLines.get(0).startY;
			this.startPoint = new CustomBitmapPoint(x, y, bmp);
			this.radius = radius;
			
			this.firstView = true;
		}
		this.routeMode = true;
		
	}
	
	public void setGoalCustomPoint(Bitmap bmp)
	{
		if(!routeLines.isEmpty())
		{
			int size = routeLines.size();
			float x = routeLines.get(size-1).stopX;
			float y = routeLines.get(size-1).stopY;
			this.goalPoint = new CustomBitmapPoint(x, y, bmp);
		}	
		this.routeMode = true;
	}
	
	private void SetPosAndScale()
	{
		//TODO: zrobiæ skalê przybli¿ania jako dynamiczn¹ do wymiarów ekranu
		//this.mScaleFactor = this.radius / originalWidth;
		//this.mScaleFactor = Math.max(mScaleFactor, minScaleFactor);
		//this.radius ...
		this.mScaleFactor = 0.55f;
		
		this.mPosX = (startPoint.x )* bitmapWidth / originalWidth  ;
		this.mPosX = (this.mPosX - viewWidth)*mScaleFactor*(-1);
		
		this.mPosY = (startPoint.y ) * bitmapHeight / originalHeight ;
		this.mPosY = (this.mPosY - viewHeight)*mScaleFactor*(-1);
		
		Log.i("mPosX:","X:"+mPosX);
		Log.i("mPosY:","Y:"+mPosY);
		Log.i("SCALE","SET:"+mScaleFactor);
		
		this.invalidate();
	}
	
	private void addLine(Line line)
	{
		float x1 = line.startX;
		float y1 = line.startY;
		float x2 = line.stopX;
		float y2 = line.stopY;

		float startX = x1 * bitmapWidth / originalWidth + mPosX;
		float startY = y1 * bitmapHeight / originalHeight + mPosY;
		float stopX = x2 * bitmapWidth / originalWidth + mPosX;
		float stopY = y2 * bitmapHeight / originalHeight + mPosY;

		lines.add(new Line(startX, startY, stopX, stopY));

	}

	public void setBitmap(Bitmap bmp)
	{
		setImageBitmap(bmp);
	}

	public void setLines(List<Line> routeLines)
	{
		if (routeLines == null)
		{
			this.routeLines = new ArrayList<Line>();
		} else
		{
			this.routeLines = routeLines;
		}
	}

	public void setImageBitmap(Bitmap bmp)
	{
		this.bitmap = bmp;
		this.bitmapHeight = bitmap.getHeight();
		this.bitmapWidth = bitmap.getWidth();
		this.mScaleFactor = 1.0f;
		this.mPosX = mPosY = 0f;

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
	

	private void echo(String s)
	{
		Log.i("Poligdzie",s);
	}
	
}
