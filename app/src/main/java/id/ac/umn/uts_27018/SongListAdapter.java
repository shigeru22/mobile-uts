package id.ac.umn.uts_27018;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongItemViewHolder> {
    private ArrayList<Song> songsList;
    private LayoutInflater inflater;
    private Context context;
    private MediaMetadataRetriever retriever;

    public SongListAdapter(Context context, ArrayList<Song> songsList) {
        this.context = context;
        this.songsList = songsList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_list_menu, parent, false);
        return new SongItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        Song song = songsList.get(position);

        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
        holder.tvAlbum.setText(song.getAlbum());

        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, Uri.parse(song.getURI()));
        try {
            byte[] temp = retriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            holder.ivAlbumArt.setImageBitmap(bitmap);
        }
        catch (Exception e) {
            Log.i("Adapter", e.getMessage());
            holder.ivAlbumArt.setBackgroundColor(Color.GRAY);
        }

        retriever.close();
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SongListAdapter adapter;
        private Song song;

        private ImageView ivAlbumArt;
        private TextView tvTitle;
        private TextView tvArtist;
        private TextView tvAlbum;

        private int position;

        public SongItemViewHolder(@NonNull View itemView, SongListAdapter adapter) {
            super(itemView);
            this.adapter = adapter;

            ivAlbumArt = (ImageView) itemView.findViewById(R.id.ivAlbumArt);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvArtist = (TextView) itemView.findViewById(R.id.tvArtist);
            tvAlbum = (TextView) itemView.findViewById(R.id.tvAlbum);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent playSong = new Intent(context, NowPlayingActivity.class);
            position = getLayoutPosition();
            song = songsList.get(position);

            Bundle bundle = new Bundle();
            bundle.putSerializable("SongPosition", position);
            bundle.putSerializable("SongList", songsList);
            playSong.putExtras(bundle);
            context.startActivity(playSong);
        }
    }
}