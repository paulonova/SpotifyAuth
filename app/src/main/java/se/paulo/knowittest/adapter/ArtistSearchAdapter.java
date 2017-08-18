package se.paulo.knowittest.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

import java.util.ArrayList;
import java.util.List;

import se.paulo.knowittest.R;
import se.paulo.knowittest.helpers.VarHolder;
import se.paulo.knowittest.models.Artist;

public class ArtistSearchAdapter extends RecyclerView.Adapter<ArtistSearchAdapter.MyViewHolder> {

    private Activity activity;
    public VarHolder varHolder = VarHolder.getInstance();
//    private List<Artist> artistArrayList = varHolder.getArtistList();
    private List<Artist> artistArrayList;

    public ArtistSearchAdapter(Activity activity, List<Artist> artistArrayList) {
        this.activity = activity;
        this.artistArrayList = new ArrayList<>(artistArrayList);
    }

    @Override
    public ArtistSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_modell, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistSearchAdapter.MyViewHolder holder, int position) {
//        final Artist artist = artistArrayList.get(position);
//
//        if(artist == null){
//            return;
//        }
//
//        holder.name.setText(artist.getName());
//        holder.popularity.setText(artist.getPopularity());
//        holder.genres.setText(artist.getGenres());
//
//        String url = artist.getImageUrl();
//        if(url != null){
//            Picasso.with(activity)
//                    .load(url)
//                    .into(holder.imageUrl);
//        }else{
//            holder.imageUrl.setImageDrawable(activity.getResources().getDrawable(R.drawable.image));
//        }


    }

    @Override
    public int getItemCount() {
//        return artistArrayList.size();
        return 1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageUrl;
        private TextView name, popularity, genres;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageUrl = (ImageView) itemView.findViewById(R.id.imgUrlImage);
            name = (TextView) itemView.findViewById(R.id.txtArtistName);
            popularity = (TextView) itemView.findViewById(R.id.txtPopularity);
            genres = (TextView) itemView.findViewById(R.id.txtGenres);

        }



        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder vh = RecyclerViewAdapterUtils.getViewHolder(v);
            final int position = vh.getAdapterPosition();

            Toast.makeText(activity, "Position: " + position, Toast.LENGTH_LONG).show();

        }
    }
}
