package in.koreatech.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private TextView musicTitleTextView;
    private Button musicPreviousButton;
    private Button musicPlayButton;
    private Button musicNextButton;
    private ListView musicListView;

    private boolean isPermitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        musicTitleTextView = findViewById(R.id.music_title_textview);
        musicPreviousButton = findViewById(R.id.music_previous_button);
        musicPlayButton = findViewById(R.id.music_play_button);
        musicNextButton = findViewById(R.id.music_next_button);
        musicListView = findViewById(R.id.music_list_listview);
        requestRuntimePermission();
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
     * 읽기 권한이 성공했을때 음악리스틀 가져옵니다.
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
                } else {
                    isPermitted = false;

                }
                return;
            }
        }
    }
}
