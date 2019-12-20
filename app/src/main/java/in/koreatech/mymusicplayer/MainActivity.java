package in.koreatech.mymusicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private TextView musicTitleTextView;
    private Button musicPreviousButton;
    private Button musicPlayButton;
    private Button musicNextButton;
    private ListView musicListView;

    private MediaPlayer mediaPlayer;
    private boolean isPermitted;
    private ArrayList<HashMap<String, String>> musicHashMapList;
    private SimpleAdapter simpleAdapter;
    private ArrayList<Music> musicList;
    private int currentPlayingPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        currentPlayingPosition = -1;
        mediaPlayer = new MediaPlayer();
        musicList = new ArrayList<>();
        musicHashMapList = new ArrayList<>();
        musicTitleTextView = findViewById(R.id.music_title_textview);
        musicPreviousButton = findViewById(R.id.music_previous_button);
        musicPlayButton = findViewById(R.id.music_play_button);
        musicNextButton = findViewById(R.id.music_next_button);
        musicListView = findViewById(R.id.music_list_listview);
        simpleAdapter = new SimpleAdapter(this, musicHashMapList, android.R.layout.simple_expandable_list_item_2, new String[]{"title", "singer"},
                new int[]{android.R.id.text1, android.R.id.text2});
        musicListView.setAdapter(simpleAdapter);
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPlayingPosition = position;
                playMusic(position);
            }
        });

        musicPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayingPosition == -1) {
                    playMusic(0);
                    currentPlayingPosition = 0;
                    return;
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    musicPlayButton.setBackgroundResource(android.R.drawable.ic_media_play);
                } else {
                    mediaPlayer.start();
                    musicPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                }
            }
        });


        musicNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayingPosition + 1 >= musicList.size())
                    currentPlayingPosition = 0;
                else currentPlayingPosition++;

                playMusic(currentPlayingPosition);
            }
        });

        musicPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayingPosition - 1 < 0)
                    currentPlayingPosition = musicList.size() - 1;
                else currentPlayingPosition--;

                playMusic(currentPlayingPosition);
            }
        });

        requestRuntimePermission();
        if (isPermitted) getMusicFileList();


    }


    /**
     * 버전이 MashMallow 이상이거나 쓰기 권한이 없을 경우는 권한을 받아오고 아닐경우는 쓰기 권한이 있다고 판단합니다.
     */
    private void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            isPermitted = true;
        }
    }

    /**
     * 퍼미션의 결과를 가져옵니다.
     * 읽기 권한이 성공이 되었을 경우 isPermitted를 true로 아닐 경우에는 isPermitted를 false로 만들어 줍니다.
     * 읽기 권한이 성공했을때 음악리스트를 가져옵니다.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermitted = true;
                    getMusicFileList();
                } else {
                    isPermitted = false;

                }
                return;
            }
        }
    }

    /**
     * 음악파일 리스트를 가져온다.
     * 파일리스트를 가져온후 음악 파일 리스트를 업데이트를 해준다.
     */
    public void getMusicFileList() {
        File musicFolder = getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC); // Music 공용 폴더에서 파일정보를 가져옵니다.

        // musicFolder list가 null 일경우 return;
        if (musicFolder.listFiles() == null) return;
        musicList.clear();

        // 음악 리스트에서 음악 파일 정보를 가져옵니다.
        for (File musicFile : musicFolder.listFiles()) {
            MediaMetadataRetriever md = new MediaMetadataRetriever(); // 메타정보를 뽑아오는 class
            md.setDataSource(musicFile.getAbsolutePath()); // 메타 정보를 음악 절대 경로를 통해서 받아온다.

            String singer = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST); // 아티스트 정보를 가져온다.
            if (singer == null || singer.equals(""))
                singer = "Unknown";

            String songtitle = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE); // 음악파일 제목을 가져온다.
            if (songtitle == null)
                songtitle = musicFile.getName();

            Music music = new Music();  // 음악 class의 새로운 객체를 만들어서 정보를 추가한다.
            music.setTitle(songtitle);
            music.setArtist(singer);
            music.setFilePath(musicFile.getAbsolutePath());
            musicList.add(music); // 리스트에 추가한다.
        }
        updateListView();
    }

    /**
     * 음악 정보를 가져와서 리스트로 출력을 합니다.
     */
    public void updateListView() {
        musicHashMapList.clear();
        for (Music music : musicList) {
            HashMap<String, String> musicInfoHashMap = new HashMap<>();
            musicInfoHashMap.put("title", music.getTitle());
            musicInfoHashMap.put("singer", music.getArtist());
            musicHashMapList.add(musicInfoHashMap);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    /**
     * 음악 재생을 해주는 메소드
     *
     * @param index 재생 위치를 보내준다.
     */
    public void playMusic(int index) {
        if (index >= musicList.size() || index < 0) return;
        mediaPlayer.stop();
        mediaPlayer.reset();
        Uri musicUri = Uri.parse(musicList.get(index).getFilePath());
        try {
            mediaPlayer.setDataSource(MainActivity.this, musicUri);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "음악재생에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }

        musicTitleTextView.setText(musicList.get(index).getTitle());
        musicPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        mediaPlayer.start();
    }

    /**
     * MediaPlayer 리소스 반환
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}