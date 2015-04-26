package com.quickblox.videochatsample.ui.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.quickblox.videochatsample.network.Pubnub;

public class SingleTouchEventView extends View {
	private Paint paint = new Paint();
	private Path path = new Path();
	private float eventX;
	private float eventY;
	private boolean fingerDown = false;
    Context mConext;

	public SingleTouchEventView(Context context, AttributeSet attrs) {
		super(context, attrs);

		/*paint.setAntiAlias(true);
		paint.setStrokeWidth(6f);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);*/

	}

	@Override
	protected void onDraw(Canvas canvas) {
		/*canvas.drawPath(path, paint);
		if (fingerDown) {
			canvas.drawCircle(eventX, eventY, 20, paint);
		}*/
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		eventX = event.getX();
		eventY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fingerDown = true;
			//path.moveTo(eventX, eventY);
			String data = "down";
            String x = new Float(eventX).toString();
            String y = new Float(eventY).toString();

            Pubnub.getInstance(mConext)._publish("HousingPosition","{\" "+x+"  \":\"  "+y+"   }\"",Pubnub.getInstance(mConext).callback);
    		Toast.makeText(this.getContext(), new Float(eventX).toString(), 
	    			   Toast.LENGTH_SHORT).show();

			return true;
		case MotionEvent.ACTION_MOVE:
			path.lineTo(eventX, eventY);
			break;
		case MotionEvent.ACTION_UP:
			fingerDown = false;
			String data1 = "up";
    		//Toast.makeText(this.getContext(), data1, 
	    		//	   Toast.LENGTH_SHORT).show();
			Toast.makeText(this.getContext(), new Float(eventY).toString(), 
	    			   Toast.LENGTH_SHORT).show();

			// nothing to do
			break;
		default:
			return false;
		}

		// Schedules a repaint.
		invalidate();
		return true;
	}
}