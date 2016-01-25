package com.raelifin.coi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by HP on 10/7/2015.
 */
public class DialogueTextView extends View {

    public ArrayList<String> text = new ArrayList<>();

    public DialogueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ( ! Bitmapbox.isReady()) { this.invalidate(); return; }

        Paint paint = new Paint();

        int y = canvas.getHeight()-14;
        for (String s : text) {
            y -= 114;
            String face = s.substring(0,s.indexOf(":"));
            s = s.substring(s.indexOf(":")+1);
            if (face.equals("Player")) {
                drawPlayerBubble(s,canvas.getWidth(),y,canvas,paint);
            } else if (face.equals("goalface_neutral")) {
                drawNPCBubble(null, face, y, canvas, paint);
            } else {
                drawNPCBubble(s, face, y, canvas, paint);
            }
        }

        paint.setColor(Color.GRAY);
        canvas.drawLine(0, 0, canvas.getWidth(), 0, paint);
    }

    private void drawNPCBubble(String text, String face, int y, Canvas canvas, Paint paint) {
        paint.setColor(Color.GRAY);

        int fontSize = 64;
        paint.setTextSize(fontSize);

        int x = 0;
        int allPadding = 14;
        int faceSize = 64;
        int headSize = 68;
        int bubbleX = x+headSize + allPadding*2 + 10;
        int bubbleY = y+allPadding;
        int textXPadding = 16;
        int textYOffset = 10;
        int bubbleHeight = fontSize+textYOffset+26;
        int bubbleWidth = (text==null)? 0 : (int)(paint.measureText(text)+textXPadding*2);
        int headX = x+allPadding;
        int headY = bubbleY+(bubbleHeight/2)-(headSize/2);

        canvas.drawRect(headX, headY, headX + headSize, headY+headSize, paint);
        canvas.drawBitmap(ThumbnailUtils.extractThumbnail(Bitmapbox.get(face), faceSize, faceSize), headX + (headSize - faceSize) / 2, headY+(headSize-faceSize)/2, paint);

        if (text != null) {
            canvas.drawRoundRect(bubbleX, bubbleY, bubbleX + bubbleWidth, bubbleY + bubbleHeight, 16, 16, paint);

            paint.setColor(Color.BLACK);
            canvas.drawText(text, bubbleX + textXPadding, bubbleY + textYOffset + fontSize, paint);
        }
    }

    private void drawPlayerBubble(String text, int x, int y, Canvas canvas, Paint paint) {
        paint.setColor(Color.GRAY);

        int fontSize = 64;
        paint.setTextSize(fontSize);

        int allPadding = 14;
        int textXPadding = 16;
        int textYOffset = 10;
        int bubbleHeight = fontSize+textYOffset+26;
        int bubbleWidth = (int)(paint.measureText(text)+textXPadding*2);
        int bubbleX = x-(allPadding*2 + 10 + bubbleWidth);
        int bubbleY = y+allPadding;

        canvas.drawRoundRect(bubbleX, bubbleY, bubbleX+bubbleWidth, bubbleY+bubbleHeight, 16, 16, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText(text, bubbleX+textXPadding, bubbleY+textYOffset+fontSize, paint);
    }
}
