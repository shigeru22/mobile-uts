package id.ac.umn.uts_27018;

import android.content.Context;

import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeFileDescriptor;

public class SongsData {
    static int[] allSongsUri = {
            R.raw.alesso_heroes,
            R.raw.axwellingrosso_morethanyouknow,
            R.raw.falilv_justawake,
            R.raw.hardwell_arcadia,
            R.raw.kenshiyonezu_lemon
    };

    static String[] allSongsTitle = {
            "Heroes (We Could Be) [feat. Tove Lo]",
            "More Than You Know",
            "Just Awake",
            "Arcadia (feat. Joey Dale & Luciana)",
            "Lemon"
    };

    static String[] allSongsArtist = {
            "Alesso",
            "Axwell /\\ Ingrosso",
            "Fear, and Loathing in Las Vegas",
            "Hardwell",
            "Kenshi Yonezu"
    };

    static String[] allSongsAlbums = {
            "Forever",
            "More Than You Know - EP",
            "All That We Have Now",
            "United We Are",
            "STRAY SHEEP"
    };

    static int[] allSongsAlbumArts = {
            R.drawable.art_alesso_forever,
            R.drawable.art_axwellingrosso_morethanyouknow,
            R.drawable.art_falilv_allthatwehavenow,
            R.drawable.art_hardwell_unitedweare,
            R.drawable.art_kenshiyonezu_straysheep
    };

    /*
    public static ArrayList<Song> getAllSongs(String packageName, Context context) {
        int len = allSongsUri.length;
        if(allSongsTitle.length != len && allSongsArtist.length != len && allSongsAlbums.length != len && allSongsAlbumArts.length != len) {
            throw new ArrayStoreException();
        }

        ArrayList<Song> songList = new ArrayList<>();

        for(int i = 0; i < len; i++) {
            MediaMetadataRetriever mm = new MediaMetadataRetriever();
            FileDescriptor fd = context.getResources().openRawResourceFd(allSongsUri[i]).getFileDescriptor();

            try {
                mm.setDataSource(fd);
                songList.add(new Song(
                        mm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                        mm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                        mm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                        mm.getEmbeddedPicture(),
                        "android.resource://" + packageName + "/" + allSongsUri[i]
                ));
            }
            catch (Exception e) {
                Log.e("error", e.getMessage());
                continue;
            }
            finally {
                mm.close();
            }

            songList.add(new Song(
                    allSongsTitle[i],
                    allSongsArtist[i],
                    allSongsAlbums[i],
                    allSongsAlbumArts[i],
                    allSongsUri[i]
            ));
        }

        return songList;
    }
    */
}
