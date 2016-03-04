package net.l1ngdtkh3.itemlistz.other;


import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.l1ngdtkh3.itemlistz.MainActivity;
import net.l1ngdtkh3.itemlistz.R;
import net.l1ngdtkh3.itemlistz.db.TestDb;


/**
 * Created by Trunks on 02.03.2016.
 */
public class CMAdapter extends CursorAdapter {

    public static final String TAG = "itemlistZ";
    public static final int TYPE_1 = 1;// текст и картинка
    public static final int TYPE_2 = 2;// текст
    public static final int TYPE_3 = 3;// картинка

    public CMAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //так делать не стоит, лучше на каждый итем делать свой лейаут
        View view = LayoutInflater.from(context).inflate(R.layout.item_img_txt, null);
        final ViewHolder holder = new ViewHolder();
        holder.mImageView = (ImageView) view.findViewById(R.id.imgSpannView);
        holder.mTextView = (TextView) view.findViewById(R.id.spannTxt);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        int typeOfData = cursor.getInt(cursor.getColumnIndex(TestDb.C_TYPE));
        if (holder != null) {
            switch (typeOfData) {
                case TYPE_1:
                    //cтавим размеры картинке
                    holder.mImageView.setVisibility(View.VISIBLE);
                    holder.mTextView.setVisibility(View.VISIBLE);
                    holder.mImageView.getLayoutParams().height = MainActivity.imgSizeSmall;
                    holder.mImageView.getLayoutParams().width = MainActivity.imgSizeSmall;
                    //берем картинку из бд
                    byte[] img = cursor.getBlob(cursor.getColumnIndex(TestDb.C_IMG));
                    if (img != null) {
                        holder.mImageView.setImageBitmap(getImage(img));
                    }
                    //делаем отступ для текста
                    SpannableString spannableString = new SpannableString(cursor.getString(cursor.getColumnIndex(TestDb.C_TEXT)));
                    int lines = (int) (holder.mImageView.getLayoutParams().height / holder.mTextView.getTextSize());
                    int margine = (int) (holder.mImageView.getLayoutParams().width * 1.1f);
                    spannableString.setSpan(new MarginSpan(margine, lines), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.mTextView.setText(spannableString);
                    break;
                case TYPE_2:
                    holder.mImageView.setVisibility(View.INVISIBLE);
                    holder.mTextView.setVisibility(View.VISIBLE);
                    holder.mTextView.setText(cursor.getString(cursor.getColumnIndex(TestDb.C_TEXT)));
                    break;
                case TYPE_3:
                    holder.mTextView.setVisibility(View.INVISIBLE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mImageView.getLayoutParams();
                    params.width = ActionBar.LayoutParams.MATCH_PARENT;
                    params.height = MainActivity.imgSizeBig;
                    holder.mImageView.setLayoutParams(params);
                    holder.mImageView.setImageBitmap(getImage(cursor.getBlob(cursor.getColumnIndex(TestDb.C_IMG))));
                    break;
            }
        }
    }

    public static class ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
