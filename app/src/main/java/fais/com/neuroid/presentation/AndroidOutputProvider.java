package fais.com.neuroid.presentation;


import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;



import java.util.ArrayList;
import java.util.List;

import fais.com.neuroid.R;
import fais.com.neuroid.presentation.widgets.BoardItemView;
import fais.com.neuroid.utilities.Util;


/**
 * This class manages output for android
 */
public class AndroidOutputProvider {

    /**
     * Board size in DP for screen
     */
    private static final int BOARD_CELL_SIZE_DP = 80;

    /**
     * Board size in DP for screen
     */
    private static final int BOARD_CELL_PADDING = 5;

    /**
     * Board view object
     */
    private GridView boardView;


    /**
     * Current activity context
     */
    private Context context;

    /**
     * Board size
     */
    private int boardSize;

    /**
     * Board view adapter
     */
    private BoardAdapter boardAdapter;
    private ArrayList<Point> points = new ArrayList<>();

    public AndroidOutputProvider(GridView boardView, int boardSize, Context context) {
        this.boardView = boardView;
        this.context = context;
        this.boardSize = boardSize;
        this.boardAdapter = new BoardAdapter(boardSize, context);
        initBoardView();
    }

    private void initBoardView() {
        // set adapter for grid view
        boardView.setAdapter(boardAdapter);
        // set grid view params
        boardView.setNumColumns(boardSize);
        boardView.setColumnWidth(BOARD_CELL_SIZE_DP);
        // initial animation
        Animation cellAnimation = AnimationUtils.loadAnimation(context, R.anim.board_cell_appear);
        GridLayoutAnimationController animController = new GridLayoutAnimationController(cellAnimation);
        animController.setDirectionPriority(GridLayoutAnimationController.PRIORITY_NONE);
        animController.setColumnDelay(0.6f / boardSize);
        animController.setRowDelay(0.6f / boardSize);
        boardView.setLayoutAnimation(animController);
    }


    public void drawOnBoard(int x, int y, int resource) {
        //Log.i("drawOnBoard", "x = " + x + "  y = " + y);
        points.add(new Point(x, y));

        if (boardView != null && boardAdapter != null) {
            boardAdapter.drawOnBoard(x, y, resource);
            //  this.playSound(R.raw.draw_sound);
            boardAdapter.notifyDataSetChanged();
        }
    }


    public double[] getPointsTable() {
        double[] table = new double[boardSize*boardSize];
        int[] temp = boardAdapter.getBoardThumbnails();

        for(int i=0; i<boardSize*boardSize; i++){
            if(temp[i]==R.drawable.board_touched_thumbnail)
                table[i]=1.0;
            else
                table[i]=0.0;
        }
        return table;
    }

    public List<Point> getPointsList(){
        return points;
    }

    public void clearPointsList() {
        points.clear();
    }

    /**
     * Adapter for GridView
     */
    private class BoardAdapter extends BaseAdapter {

        private int boardThumbnails[];
        private Context context;

        public BoardAdapter(int boardSize, Context context) {
            this.context = context;
            this.initBoardThumbnails(boardSize);
        }

        @Override
        public int getCount() {
            return boardThumbnails.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BoardItemView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new BoardItemView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(BOARD_CELL_PADDING, BOARD_CELL_PADDING, BOARD_CELL_PADDING, BOARD_CELL_PADDING);
            } else {
                imageView = (BoardItemView) convertView;
            }
            imageView.setTag(position);
            imageView.setImageResource(boardThumbnails[position]);
            return imageView;
        }

        private void initBoardThumbnails(int boardSize) {
            boardThumbnails = new int[boardSize * boardSize];
            for (int i = 0; i < boardSize * boardSize; i++) {
                // init board with blank fields
                boardThumbnails[i] = R.drawable.board_blank_thumbnail;
            }
        }

        public void drawOnBoard(int x, int y, int resource) {
            boardThumbnails[Util.convert2DIndexTo1D(x, y, boardSize)] = resource;
        }

        public int[] getBoardThumbnails(){
            return boardThumbnails;
        }
    }


}
