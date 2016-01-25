package fais.com.neuroid.presentation.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BoardItemView extends ImageView {

    public BoardItemView(Context context) {
        super(context);
    }

    public BoardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
