package net.l1ngdtkh3.itemlistz.other;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

/**
 * Created by Trunks on 02.03.2016.
 */
public class MarginSpan implements LeadingMarginSpan.LeadingMarginSpan2 {
    private int margin;
    private int lines;

    public MarginSpan(int margin, int lines) {
        this.margin = margin;
        this.lines = lines;
    }

    @Override
    public int getLeadingMarginLineCount() {
        return lines;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return first ? margin : 0;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline,
                                  int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
    }
}

