package fais.com.neuroid.utilities;

import android.graphics.Point;


public class  Util {

    public static int convert2DIndexTo1D(int x, int y, int size) {
        return y * size + x;
    }

    public static Point convert1DIndexTo2D(int index, int size) {
        Point point = new Point();
        int x = index % size;
        int y = (int) Math.floor(((double) index / size));
        point.set(x, y);
        return point;
    }
}
