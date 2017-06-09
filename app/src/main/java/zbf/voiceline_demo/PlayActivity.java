package zbf.voiceline_demo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.shuyu.waveview.AudioWaveView;

import java.io.File;
import java.io.IOException;

/**
 * 播放声音界面
 */
public class PlayActivity extends AppCompatActivity
{
    VoiceLineView voiceLineView;
    Button play, share;
    private boolean isAlive = true;
    MediaPlayer mediaPlayer;
    AudioWaveView audioWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        viewInit();
        try
        {
            mediaPlayerInit();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    String myUri;

    private void mediaPlayerInit() throws IOException
    {
        Intent intent = getIntent();
        myUri = intent.getStringExtra("path");
        Log.e("xbf", myUri);
        mediaPlayer = new MediaPlayer();
        //用于识别音频流的系统声音的音量
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //设置路径
        mediaPlayer.setDataSource(myUri);
        //完成初始化
        mediaPlayer.prepare();


    }

    private void viewInit()
    {
        voiceLineView = (VoiceLineView) findViewById(R.id.voicLine);
        play = (Button) findViewById(R.id.btn_play);
        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    mediaPlayer.start();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        share = (Button) findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File file = new File(myUri);
                Log.e("zbf", String.valueOf(file.exists()) + file.getPath());
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("*/*");
                startActivity(Intent.createChooser(intent, "发送"));
            }
        });

    }


    @Override
    protected void onDestroy()
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

}
