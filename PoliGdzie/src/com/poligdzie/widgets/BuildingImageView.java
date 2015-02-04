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

	private int						originalWidth		= 0;
	private int						originalHeight		= 0;

	long							startTime, stopTime;
	int								clickCount;

	private ScaleGestureDetector	mScaleDetector;
	private float					mScaleFactor		= 1.f;
	private float					minScaleFactor;

	private float					mPosX;
	private float					mPosY;

	private float					mLastTouchX, mLastTouchY;


	private boolean					panEnabled			= true;
	private boolean					zoomEnabled			= true;
	List<Line>						lines				= new ArrayList<Line>();

	private List<Line>				routeLines;
	
	CustomBitmapPoint		startPoint = null;
	CustomBitmapPoint		goalPoint = null;
	
	private boolean 	routeMode = false;

	private int	bitmapHeight;

	private int	bitmapWidth;
	
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
		if (bitmap == null)
		{
			super.onDraw(canvas);
			return;
		}
		
	
		mScaleFactor = Math.max(mScaleFactor, minScaleFactor);
		Log.i("SCALE","t3:"+mScaleFactor);		
		
		canvas.save();
		int minX, maxX, minY, maxY;
		maxX = (int) (((viewWidth / mScaleFactor) - bitmap.getWidth()) / 2);
		echo("maxX:"+maxX);
		echo("divide;:"+(viewWidth / mScaleFactor));
		echo("bitmap:"+bitmap.getWidth());
		echo("maxX:"+maxX);
		minX = 0;

		maxY = (int) (((viewHeight / mScaleFactor) - bitmap.getHeight()) / 2);
		minY = 0;
		
		if (mPosX > minX)
			mPosX = minX;
		if (mPosX < maxX)
			mPosX = maxX;

		if (mPosY > minY)
			mPosY = minY;
		if (mPosY < maxY)
			mPosY = maxY;

		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.translate(mPosX, mPosY);

		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, mPosX, mPosY, null);

		Paint redPaint = new Paint();
		redPaint.setColor(Color.RED);
		redPaint.setStrokeWidth(5);
		
		
		if(routeMode)
		{
			if (routeLines != null)
			{
				if(!routeLines.isEmpty())
				{
					lines.clear();
					for (Line l : routeLines)
					{
						addLine(l);
					}
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
				{
					final int newPointerIndex;
					if(pointerIndex == 0)
					{
						newPointerIndex = 1;
					}
					else
					{
						newPointerIndex = 0;
					}
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
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			invalidate();
			return true;
		}
	}

	// TODO: niech zmiana ekranu nie rysuje wszystkiego do nowa // zachowa translacje i skalowanie
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
		echo("Radius"+radius);
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
		this.radius*=1.5;
		this.mScaleFactor = (float) viewHeight/ (float) (this.radius);
		
		this.mPosX = (float)((startPoint.x - this.radius)* bitmapWidth) / originalWidth  ;
		this.mPosX = (this.mPosX )/(-2);
		
		this.mPosY = (float)((startPoint.y- this.radius ) * bitmapHeight) / originalHeight ;
		this.mPosY = (this.mPosY )/(-2);
		
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
