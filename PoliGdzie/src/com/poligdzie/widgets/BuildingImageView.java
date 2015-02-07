package com.poligdzie.widgets;

import java.sql.SQLException;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.poligdzie.R;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.fragments.SearchDetailsFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.helpers.FragmentMapHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.route.Line;
import com.poligdzie.singletons.MapFragmentProvider;

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

	private float					mPosX = -1;
	private float					mPosY = -1;

	private float					mLastTouchX, mLastTouchY;
	private SearchDetailsFragment	searchDetailFragment;
	private DatabaseHelper			dbHelper;


	private boolean					panEnabled			= true;
	private boolean					zoomEnabled			= true;
	List<Line>						lines				= new ArrayList<Line>();
	List<RoomField>					rooms 				= new ArrayList<RoomField>();

	private List<Line>				routeLines;
	
	CustomBitmapPoint		startPoint = null;
	CustomBitmapPoint		goalPoint = null;
	
	private boolean 	routeMode = false;

	private int	bitmapHeight;

	private int	bitmapWidth;
	
	private boolean 				firstView=false;
	private boolean 				initialized = false;

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
		dbHelper = new DatabaseHelper(getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);

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
				long duration = System.currentTimeMillis() - startTime;
				if (clickCount == 2)
				{
					if (duration <= DOUBLE_CLICK_DURATION)
					{
						mScaleFactor *= VIEW_ZOOM_IN;
						mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
						invalidate();
					}
					clickCount = 0;
					duration = 0;
				}
				else if( clickCount == 1)
				{
					if(duration <= SINGLE_CLICK_DURATION)
					{
						
						float touchX = mLastTouchX/mScaleFactor - mPosX*2;
						float touchY = mLastTouchY/mScaleFactor - mPosY*2;
						echo("X:"+touchX);
						echo("Y:"+touchY);
						echo("mPosX:"+mPosX);
						echo("mPosY:"+mPosY);
						echo("scale:"+mScaleFactor);
						
						for(RoomField rf: rooms)
						{
							//echo(rf.minX+"|"+rf.maxX+"|"+rf.minY+"|"+rf.maxY);
							if( (touchX > rf.minX && touchX < rf.maxX) &&
									(touchY > rf.minY && touchY < rf.maxY))
							{
								try
								{
									echo("room:"+rf.roomId);
									Room room = dbHelper.getRoomDao().queryForId(rf.roomId);
									echo("floor:"+room.getFloor().getId());
									int buildId = dbHelper.getFloorDao().queryForId(room.getFloor().getId())
											.getBuilding().getId();
									Building build = dbHelper.getBuildingDao().queryForId(buildId);
									searchDetailFragment.setTextViews(room.getName(),build.getName() , room);
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
							}
						}
						/////echo("startX:"+startPoint.x);
						//echo("startY:"+startPoint.y);
						
						if(routeMode && startPoint.bmp != null && goalPoint.bmp != null)
						{
							float startX = startPoint.x * bitmapWidth /originalWidth;
							float startY = startPoint.y * bitmapHeight /originalHeight;
							float goalX = goalPoint.x * bitmapWidth /originalWidth;
							float goalY = goalPoint.y * bitmapHeight /originalHeight;
							MapFragmentProvider provider = MapFragmentProvider.getInstance();
							MapActivity activity = (MapActivity) searchDetailFragment.getActivity();
							if(touchX > startX && touchX < (startX +startPoint.bmp.getWidth())
									&& touchY > startY && touchY < (startY +startPoint.bmp.getHeight()))
							{
								if(provider.getPreviousKey() != null)
								{
									((PoliGdzieBaseActivity) activity)
									.switchFragment(R.id.map_container, provider.getPreviousFragment(),
											provider.getPreviousKey());

									((OnClickListener) searchDetailFragment.getActivity()).onClick(
											activity.findViewById(R.layout.map_activity));
								}
							}
							else if(touchX > goalX && touchX < (goalX +goalPoint.bmp.getWidth())
									&& touchY > goalY && touchY < (goalY +goalPoint.bmp.getHeight()))
							{
								echo("NEXT");
								if(provider.getNextKey() != null)
								{
									((PoliGdzieBaseActivity) activity)
									.switchFragment(R.id.map_container, provider.getNextFragment(),
											provider.getNextKey());

									((OnClickListener) searchDetailFragment.getActivity()).onClick(
											activity.findViewById(R.layout.map_activity));
								}
							}
						}
					}
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

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		viewHeight = h;
		viewWidth = w;

		if ((bitmap.getHeight() > 0) && (bitmap.getWidth() > 0) )
		{
			float minXScaleFactor = (float) viewWidth
					/ (float) bitmap.getWidth();
			float minYScaleFactor = (float) viewHeight
					/ (float) bitmap.getHeight();
			minScaleFactor = Math.max(minXScaleFactor, minYScaleFactor);
			
			if(!initialized)
			{
				mScaleFactor = minScaleFactor;
				mPosX = mPosY = 0;
				initialized = true;
			}

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
	
	private class RoomField
	{
		public float minX;
		public float maxX;
		public float minY;
		public float maxY;
		public int roomId;
		
		public RoomField(float x1, float x2, float y1, float y2,int id)
		{
			this.minX = x1;
			this.maxX = x2;
			this.minY = y1;
			this.maxY = y2;
			this.roomId = id;
		}
	}

	public void setSearchCustomPoint(int x, int y,int radius)
	{
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
	
	public void setRooms(List<Room> roomList)
	{
		if(originalWidth == 0 || originalHeight == 0) return;
		for(Room room : roomList)
		{
			float x1 = (room.getCoordX() - room.getRadius())* bitmapWidth / originalWidth;
			float x2 = (room.getCoordX() + room.getRadius())* bitmapWidth / originalWidth;
			float y1 = (room.getCoordY() - room.getRadius())* bitmapHeight / originalHeight;
			float y2 = (room.getCoordY() + room.getRadius())* bitmapHeight / originalHeight;
			rooms.add(new RoomField(x1,x2,y1,y2,room.getId()));
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

	public void setDetailFragmentFromContext(MapActivity activity)
	{
		searchDetailFragment = activity.getSearchDetailsFragment();
		
	}


	
}
