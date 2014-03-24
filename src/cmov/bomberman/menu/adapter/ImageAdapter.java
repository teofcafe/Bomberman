package cmov.bomberman.menu.adapter;

import cmov.bomberman.menu.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    
    private Integer[] mThumbIds = {
            R.drawable.avatar_1, R.drawable.avatar_2,
            R.drawable.avatar_3, R.drawable.avatar_4,
            R.drawable.avatar_5, R.drawable.avatar_6,
    };
    
    boolean[] items;
    
    public ImageAdapter(Context c) {
        mContext = c;
        items = new boolean[mThumbIds.length];
        for(int i=0; i< mThumbIds.length; i++){
            items[i]= false;
        }   
    }
   
   public void chageState(int position){
     this.items[position] = !(this.items[position]);
     for(int i = 0; i< items.length; i++){
        if(i != position)
            items[i]=false;
     }
   }
   
    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public void paint(View v, int position){
        
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
        if (this.items[position])
            imageView.setBackgroundColor(0xFF00FF00);
        else
            imageView.setBackgroundColor(0x00000000);

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    
}