package net.l1ngdtkh3.itemlistz;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ListView;

import net.l1ngdtkh3.itemlistz.db.TestDb;
import net.l1ngdtkh3.itemlistz.other.CMAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mListView;
    public TestDb testDb;
    public static Cursor mCursor;
    public CMAdapter cmAdapter;
    public static int WIDTH_SCR;
    public static int HEIGHT_SCR;
    public static int imgSizeSmall;
    public static int imgSizeBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAndFillFakeDB();
        mListView = (ListView) findViewById(R.id.listView);
        cmAdapter = new CMAdapter(this, mCursor, true);
        mListView.setAdapter(cmAdapter);
        getScreenSize();
        getSizeForImg();
        getSupportLoaderManager().initLoader(0, null, this);

    }

    private void getSizeForImg() {
        imgSizeSmall = WIDTH_SCR / 4;//
        imgSizeBig = HEIGHT_SCR / 3;
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WIDTH_SCR = size.x;
        HEIGHT_SCR = size.y;
    }

    private void createAndFillFakeDB() {
        testDb = new TestDb(this);
        //заполняем не понятно чем
        testDb.putImgData(3, BitmapFactory.decodeResource(getResources(), R.drawable.zond));
        testDb.putTxtData(2, getString(R.string.fake_txt2));
        testDb.putTxtImgData(1, getString(R.string.fake_txt1), BitmapFactory.decodeResource(getResources(), R.drawable.newton));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(this, testDb);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cmAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cmAdapter.swapCursor(null);
    }

    static class MyCursorLoader extends CursorLoader {
        private TestDb dbOperator;

        public MyCursorLoader(Context context, TestDb dbOperator) {
            super(context);
            this.dbOperator = dbOperator;

        }

        @Override
        public Cursor loadInBackground() {
            mCursor = dbOperator.fetchAllData();
            return mCursor;
        }
    }
}
