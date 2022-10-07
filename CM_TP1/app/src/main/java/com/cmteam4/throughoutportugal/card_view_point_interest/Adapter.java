package com.cmteam4.throughoutportugal.card_view_point_interest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmteam4.throughoutportugal.MapPoint;
import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.model.PointOfInterest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<PointOfInterest> data;

    public Adapter(Context context, List<PointOfInterest> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.point_interest_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PointOfInterest pointOfInterest = data.get(position);

        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        holder.textViewTitle.setText(pointOfInterest.getTxtTitle());

        holder.textViewLikes.setText(String.valueOf(pointOfInterest.getAmountLikes()));
        holder.textViewDislikes.setText(String.valueOf(pointOfInterest.getAmountDislikes()));
        if (pointOfInterest.isFavourite(userUID))
            holder.btnFavorite.setImageResource(R.drawable.icons_favourite_red);

        if (pointOfInterest.getImgPoint() != null) {

            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("posts")
                    .child(pointOfInterest.getImgPoint())
                    .child("photo.jpg");

            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(holder.imageView);
            });
        } else {
            holder.imageView.setImageResource(R.drawable.paisagem);
        }


        holder.itemView.setOnClickListener(v -> {
            Log.e("TESTE", pointOfInterest.getTxtTitle());

            Intent intent = new Intent(v.getContext(), MapPoint.class);
            intent.putExtra("LATITUTE", pointOfInterest.getLatitude());
            intent.putExtra("LONGITUTE", pointOfInterest.getLongitude());
            intent.putExtra("NAME_POINT", pointOfInterest.getTxtTitle());

            if (pointOfInterest.getImgPoint() != null) {

                intent.putExtra("PHOTO", pointOfInterest.getImgPoint());
            } else {
                intent.putExtra("PHOTO", R.drawable.paisagem);
            }

            v.getContext().startActivity(intent);
        });


        holder.btnLike.setOnClickListener(v -> {
            //PointsInterest.load(v.getContext());
            Log.e("LIKE", "YA CLICLOU LIKE MESMO");
            boolean clickedLike = pointOfInterest.checkLiked(userUID);
            holder.btnDislikes.setClickable(true);
            if (!clickedLike) {
                int dislikes = pointOfInterest.getAmountDislikes();
                if (dislikes == 0) {
                    holder.textViewDislikes.setText(String.valueOf(0));
                    pointOfInterest.clearDislikeList();
                } else {
                    holder.textViewDislikes.setText(String.valueOf(pointOfInterest.getAmountDislikes() - 1));
                    pointOfInterest.removeDislike(userUID);
                }

                pointOfInterest.addLike(userUID);
                holder.textViewLikes.setText(String.valueOf(pointOfInterest.getAmountLikes()));
                //holder.btnLike.setClickable(false);
            } else {
                Toast toast = Toast.makeText(v.getContext(), "You already like it!", Toast.LENGTH_SHORT);
                toast.show();
            }
            PointsInterest pointsInterest = new PointsInterest();
            pointsInterest.savePointOfInterestToDatabase(pointOfInterest);
        });

        holder.btnDislikes.setOnClickListener(v -> {
            //PointsInterest.load(v.getContext());
            Log.e("DISLIKE", "YA CLICLOU DISLIKE MESMO");
            boolean clickedDislike = pointOfInterest.checkDislike(userUID);
            holder.btnLike.setClickable(true);
            if (!clickedDislike) {
                int likes = pointOfInterest.getAmountLikes();
                if (likes == 0) {
                    holder.textViewLikes.setText(String.valueOf(0));
                    pointOfInterest.clearLikeList();
                } else {
                    holder.textViewLikes.setText(String.valueOf(pointOfInterest.getAmountLikes() - 1));
                    pointOfInterest.removeLike(userUID);
                }


                pointOfInterest.addDislike(userUID);
                holder.textViewDislikes.setText(String.valueOf(pointOfInterest.getAmountDislikes()));
                //holder.btnDislikes.setClickable(false);
            } else {
                Toast toast = Toast.makeText(v.getContext(), "You already dislike it!", Toast.LENGTH_SHORT);
                toast.show();
            }
            PointsInterest pointsInterest = new PointsInterest();
            pointsInterest.savePointOfInterestToDatabase(pointOfInterest);
        });

        holder.btnFavorite.setOnClickListener(v -> {
            //PointsInterest.load(v.getContext());
            if (!pointOfInterest.isFavourite(userUID)) {
                holder.btnFavorite.setImageResource(R.drawable.icons_favourite_red);
                pointOfInterest.addToFavourites(userUID);
            } else {
                holder.btnFavorite.setImageResource(R.drawable.icons_favourite);
                pointOfInterest.removeFromFavourites(userUID);
            }
            PointsInterest pointsInterest = new PointsInterest();
            pointsInterest.savePointOfInterestToDatabase(pointOfInterest);
        });

        //adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;

        ImageView btnLike;
        TextView textViewLikes;

        ImageView btnDislikes;
        TextView textViewDislikes;
        ImageView imageView;

        ImageView btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnLike = itemView.findViewById(R.id.imgLike);
            btnDislikes = itemView.findViewById(R.id.imgDislike);
            btnFavorite = itemView.findViewById(R.id.imgFavourite);

            textViewTitle = itemView.findViewById(R.id.txtTitle);
            textViewLikes = itemView.findViewById(R.id.txtLikes);
            textViewDislikes = itemView.findViewById(R.id.txtDislikes);

            imageView = itemView.findViewById(R.id.imgPoint);
        }

    }

}
