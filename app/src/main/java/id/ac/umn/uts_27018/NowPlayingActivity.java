package id.ac.umn.uts_27018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class NowPlayingActivity extends AppCompatActivity {
    private TextView tvTitle, tvArtist, tvAlbum, tvElapsedTime, tvTotalTime;
    private ImageView ivAlbumArt;
    private SeekBar sbTime;
    private ImageButton btnPrev, btnPlayPause, btnNext;

    private Context context;
    private ArrayList<Song> songsList = new ArrayList<>();
    private MediaPlayer player;
    private MediaMetadataRetriever retriever;

    private int position;
    private int totalTime;
    private boolean seekBarCounting;
    private int tempTime;
    private boolean playerLoaded;

    // time counter
    Thread thread = new Thread() {
        @Override
        public void run() {
        try {
            while (!thread.isInterrupted()) {
                Thread.sleep(100);
                runOnUiThread(() -> countElapsedTime(-1));

                if(player.getCurrentPosition() >= totalTime) {
                    if(position - 1 < songsList.size()) changeSong(songsList.get(++position));
                    else {
                        playerLoaded = false;
                        player.release();
                        btnPlayPause.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play));
                    }
                }
            }
        }
        catch (InterruptedException e) {
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvArtist = (TextView) findViewById(R.id.tvArtist);
        tvAlbum = (TextView) findViewById(R.id.tvAlbum);
        tvElapsedTime = (TextView) findViewById(R.id.tvElapsedTime);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
        ivAlbumArt = findViewById(R.id.albumArt);
        sbTime = findViewById(R.id.sbTime);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        btnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
        btnNext = (ImageButton) findViewById(R.id.btnNext);

        context = this;
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position - 1 >= 0) changeSong(songsList.get(--position));
                else Toast.makeText(context, R.string.first_reached, Toast.LENGTH_SHORT).show();
            }
        });
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { audioPlayPause(); }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position - 1 < songsList.size()) changeSong(songsList.get(++position));
                else Toast.makeText(context, R.string.last_reached, Toast.LENGTH_SHORT).show();
            }
        });
        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (!seekBarCounting) {
                    float divisor = i / 500f;
                    tempTime = (int) (divisor * totalTime);
                    countElapsedTime(tempTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarCounting = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                boolean played = player.isPlaying();

                player.seekTo(tempTime);
                if (played) {
                    player.start();
                    seekBarCounting = true;
                }
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = (int) bundle.getSerializable("SongPosition");
        songsList = (ArrayList<Song>) bundle.getSerializable("SongList");
        changeSong(songsList.get(position));
        if(!player.isPlaying()) audioPlayPause();

        thread.start();
    }

    // options item, only home (back) button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitActivity();
    }

    private void exitActivity() {
        try {
            playerLoaded = false;
            player.pause();
            player.release();
        }
        catch (Exception e) {
            Log.d("debug", "Player has nothing loaded.");
        }
        this.finish();
    }

    private String msecToTime(int miliseconds) {
        int minutes = miliseconds / 60000;
        int secs = (miliseconds / 1000) % 60;
        String time = String.format(Locale.US, "%d:%02d", minutes, secs);
        return time;
    }

    private void countElapsedTime(int miliseconds) {
        if(playerLoaded) {
            if(miliseconds == -1) miliseconds = player.getCurrentPosition();
            tvElapsedTime.setText(msecToTime(miliseconds));
            if(seekBarCounting) {
                int target = (int) ((miliseconds * 500) / totalTime);
                sbTime.setProgress(target);
            }
        }
    }

    private void audioPlayPause() {
        if(player.isPlaying()) {
            player.pause();
            seekBarCounting = false;
            btnPlayPause.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play));
        }
        else {
            if(position - 1 == songsList.size() - 1 && player.getCurrentPosition() >= totalTime) {
                // replay
                changeSong(songsList.get(position));
                return;
            }

            player.start();
            seekBarCounting = true;
            btnPlayPause.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pause));
        }
    }

    private void changeSong(Song song) {
        boolean wasPlaying = false;
        try {
            playerLoaded = false;
            wasPlaying = player.isPlaying();
            player.release();
        }
        catch(Exception e) {
            Log.d("debug", "Player has nothing loaded.");
        }

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());
        tvAlbum.setText(song.getAlbum());

        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, Uri.parse(song.getURI()));
        try {
            byte[] temp = retriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            ivAlbumArt.setImageBitmap(bitmap);
        }
        catch (Exception e) {
            Log.i("Adapter", e.getMessage());
            ivAlbumArt.setBackgroundColor(Color.GRAY);
        }
        retriever.close();

        player = MediaPlayer.create(this, Uri.parse(song.getURI()));
        totalTime = player.getDuration();
        tvTotalTime.setText(msecToTime(totalTime));
        playerLoaded = true;

        if(wasPlaying) {
            player.start();
            seekBarCounting = true;
            btnPlayPause.setImageDrawable(getDrawable(R.drawable.ic_pause));
        }
    }
}