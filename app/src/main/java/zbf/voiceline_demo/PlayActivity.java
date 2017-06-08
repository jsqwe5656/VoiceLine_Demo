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

import java.io.File;
import java.io.IOException;

/**
 * 播放声音界面
 */
public class PlayActivity extends AppCompatActivity implements Runnable
{
    VoiceLineView voiceLineView;
    Button play,share;
    private boolean isAlive = true;
    private MediaRecorder mMediaRecorder;
    MediaPlayer mediaPlayer;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (mMediaRecorder == null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            double db = 0;// 分贝
            //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
            //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
            //同时，也可以配置灵敏度sensibility
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            //只要有一个线程，不断调用这个方法，就可以使波形变化
            //主要，这个方法必须在ui线程中调用
            voiceLineView.setVolume((int) (db));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        viewInit();
        try
        {
            mediaPlayerInit();
            mediaRecorderInit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    private void mediaRecorderInit() throws IOException
    {
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setMaxDuration(1000 * 60 * 10);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "hello.log");
        if (!file.exists())
        {
            file.createNewFile();
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
        }
        mMediaRecorder.prepare();
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
                mediaPlayer.start();
                //好像不能这么玩
//                mMediaRecorder.start();
//                Thread thread = new Thread(PlayActivity.this);
//                thread.start();
            }
        });
        share = (Button) findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File file = new File(myUri);
                Intent intent = new Intent(Intent.EXTRA_STREAM,Uri.fromFile(file));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("*/*");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy()
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        isAlive = false;
        mMediaRecorder.release();
        mMediaRecorder = null;
        super.onDestroy();
    }

    @Override
    public void run()
    {
        while (isAlive)
        {
            handler.sendEmptyMessage(0);
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
