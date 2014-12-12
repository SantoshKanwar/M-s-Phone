package com.example.myphone;


import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
    }

    public void onSwipeRight() {
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener 
    {
    	 static final int SWIPE_MIN_DISTANCE = 120;
   	  static final int SWIPE_MAX_OFF_PATH = 250;
   	  static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onDown(MotionEvent e) 
        {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
        
        	if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) 
        	{
        		if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH|| Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) 
        		{
        			return false;
        		}
        		if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) 
        		{
        			onSwipeLeft();
        			//Toast.makeText(OnSwipeTouchListener.this, "bottomToTop",Toast.LENGTH_SHORT).show();
        		}
        		else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) 
        		{
        			onSwipeRight();
        			//Toast.makeText(OnSwipeTouchListener.this,"topToBottom  ", Toast.LENGTH_SHORT).show();
        		}
           } 
    	   else 
    	   {
    		   if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) 
    		   {
    			   return false;
    		   }
    		   if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) 
    		   {
    			   //Toast.makeText(OnSwipeTouchListener.this,"swipe RightToLeft", 5000).show();
    		   }	 
    		   else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) 
    		   {
    			   //Toast.makeText(OnSwipeTouchListener.this,"swipe LeftToright  ", 5000).show();
    		   }
    	   }
        		return super.onFling(e1, e2, velocityX, velocityY);
    	  }
    }
}
