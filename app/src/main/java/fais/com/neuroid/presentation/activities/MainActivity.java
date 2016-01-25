package fais.com.neuroid.presentation.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.Toolbar;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fais.com.neuroid.R;
import fais.com.neuroid.presentation.AndroidOutputProvider;
import fais.com.neuroid.utilities.Util;


/**
 * This activity holds activity_result application view
 */
public class MainActivity extends AppCompatActivity {

    public final static String LIST_KEY = "LIST";

    @Bind(R.id.activity_game_grid_view)
    GridView gridView;


    AndroidOutputProvider outputProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content view
        setContentView(R.layout.activity_main);
        // bind this view
        ButterKnife.bind(this);
        // init game with starting params
        initGame();
        // set input listeners

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int position = gridView.pointToPosition((int) event.getX(), (int) event.getY());
                        Point point = Util.convert1DIndexTo2D(position, 16);
                        if (outputProvider != null) {
                            if (checkCoords(point.x, point.y, 16))
                                outputProvider.drawOnBoard(point.x, point.y, R.drawable.board_touched_thumbnail);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public boolean checkCoords(int x, int y, int boardSize) {
        if (x >= boardSize || x < 0) {
            return false;
        }
        if (y >= boardSize || y < 0) {
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void initGame() {
        outputProvider = new AndroidOutputProvider(gridView, 16, this);
    }


    @OnClick(R.id.clear_button)
    public void clearClick(View v) {
        outputProvider.clearPointsList();
        initGame();
    }

    @OnClick(R.id.go_button)
    public void goClick(View v) {
        if (!outputProvider.getPointsList().isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra(LIST_KEY, outputProvider.getPointsTable());
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Draw something!", Toast.LENGTH_SHORT).show();
        }
    }


}
