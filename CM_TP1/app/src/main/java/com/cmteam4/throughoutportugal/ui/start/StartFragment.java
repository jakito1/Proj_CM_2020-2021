package com.cmteam4.throughoutportugal.ui.start;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.cardViewRegions.Region;

public class StartFragment extends Fragment implements View.OnTouchListener {

    public ImageView iv;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_start_test, container, false);

        iv = root.findViewById(R.id.image);
        if (iv != null) {
            iv.setOnTouchListener(this);
        }

        return root;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        boolean handledHere = false;

        final int action = ev.getAction();

        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        int nextImage = -1;

        ImageView imageView = v.findViewById(R.id.image);
        if (imageView == null) return false;

        Integer tagNum = (Integer) imageView.getTag();
        int currentResource = (tagNum == null) ? R.drawable.map : tagNum;


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (currentResource == R.drawable.map) {
                    nextImage = R.drawable.map;

                }
                handledHere = true;
                break;

            case MotionEvent.ACTION_UP:

                int touchColor = getHotspotColor(R.id.image_areas, evX, evY);

                ColorTool ct = new ColorTool();
                int tolerance = 75;
                nextImage = R.drawable.map;
                if (ct.closeMatch(Color.RED, touchColor, tolerance)) {
                    nextImage = R.drawable.map_north;

                    Intent intent = new Intent(getContext(), Region.class);
                    intent.putExtra("REGION", "NORTE");
                    startActivity(intent);
                } else if (ct.closeMatch(Color.YELLOW, touchColor, tolerance)) {
                    nextImage = R.drawable.map_centro;

                    Intent intent = new Intent(getContext(), Region.class);
                    intent.putExtra("REGION", "CENTRO");
                    startActivity(intent);
                } else if (ct.closeMatch(Color.BLUE, touchColor, tolerance)) {
                    nextImage = R.drawable.map_sul;

                    Intent intent = new Intent(getContext(), Region.class);
                    intent.putExtra("REGION", "SUL");
                    startActivity(intent);
                }


                if (currentResource == nextImage) {
                    nextImage = R.drawable.map;
                }
                handledHere = true;
                break;

        }

        if (handledHere) {

            if (nextImage > 0) {
                imageView.setImageResource(nextImage);
                imageView.setTag(nextImage);
            }
        }
        return handledHere;
    }

    public int getHotspotColor(int hotspotId, int x, int y) {
        ImageView img = requireActivity().findViewById(hotspotId);
        if (img == null) {
            Log.d("ImageAreasActivity", "Hot spot image not found");
            return 0;
        } else {
            Bitmap hotspots = getBitmapFromView(img);
            if (hotspots == null) {
                Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                return hotspots.getPixel(x, y);
            }
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}
