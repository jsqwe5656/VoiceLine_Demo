package zbf.voiceline_demo;

import android.content.Context;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.piterwilson.audio.MP3RadioStreamDelegate;
import com.piterwilson.audio.MP3RadioStreamPlayer;
import com.shuyu.waveview.AudioWaveView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放声音界面
 */
public class PlayActivity extends AppCompatActivity implements MP3RadioStreamDelegate
{
    VoiceLineView voiceLineView;
    Button play, share;
    private boolean isAlive = true;
    MediaPlayer mediaPlayer;
    AudioWaveView audioWaveView;

    MP3RadioStreamPlayer player;

    Timer timer;

    boolean playeEnd;

    boolean seekBarTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        myUri = intent.getStringExtra("path");
        viewInit();
        playInit();
//        try
//        {
//            mediaPlayerInit();
//
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

    }

    private void playInit()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                play();
            }
        }, 1000);
        timer = new Timer();
/*        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (playeEnd || player == null || !seekBar.isEnabled()) {
                    return;
                }
                long position = player.getCurPosition();
                if (position > 0 && !seekBarTouch) {
                    seekBar.setProgress((int) position);
                }
            }
        }, 1000, 1000);*/
    }

    String myUri;

    private void mediaPlayerInit() throws IOException
    {
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
        audioWaveView = (AudioWaveView) findViewById(R.id.audioWave);
        voiceLineView = (VoiceLineView) findViewById(R.id.voicLine);
        play = (Button) findViewById(R.id.btn_play);
        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                try
//                {
//                    mediaPlayer.start();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
                player.setPause(true);
                play();

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

    private void play() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        player = new MP3RadioStreamPlayer();
        //player.setUrlString(this, true, "http://www.stephaniequinn.com/Music/Commercial%20DEMO%20-%2005.mp3");
        player.setUrlString(myUri);
        player.setDelegate(PlayActivity.this);

        int size = getScreenWidth(this) / dip2px(this, 1);//控件默认的间隔是1
        player.setDataList(audioWaveView.getRecList(), size);

        //player.setStartWaveTime(5000);
        //audioWave.setDrawBase(false);
        //声波变色
//        audioWaveView.setBaseRecorder(player);
        audioWaveView.startView();
        try {
            player.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        player.stop();
    }


    @Override
    protected void onDestroy()
    {
//        mediaPlayer.stop();
//        mediaPlayer.release();
        audioWaveView.stopView();
        super.onDestroy();
    }

    @Override
    public void onRadioPlayerPlaybackStarted(MP3RadioStreamPlayer mp3RadioStreamPlayer)
    {
        Log.i(TAG, "onRadioPlayerPlaybackStarted");
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                playeEnd = false;
//                playBtn.setEnabled(true);
//                seekBar.setMax((int) player.getDuration());
//                seekBar.setEnabled(true);
            }
        });
    }

    @Override
    public void onRadioPlayerStopped(MP3RadioStreamPlayer mp3RadioStreamPlayer)
    {
        Log.i(TAG, "onRadioPlayerStopped");
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                playeEnd = true;
//                playBtn.setText("播放");
//                playBtn.setEnabled(true);
//                seekBar.setEnabled(false);
            }
        });
    }

    @Override
    public void onRadioPlayerError(MP3RadioStreamPlayer mp3RadioStreamPlayer)
    {
        Log.i(TAG, "onRadioPlayerError");
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                playeEnd = false;
//                playBtn.setEnabled(true);
//                seekBar.setEnabled(false);
            }
        });
    }

    @Override
    public void onRadioPlayerBuffering(MP3RadioStreamPlayer mp3RadioStreamPlayer)
    {
        Log.i(TAG, "onRadioPlayerBuffering");
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
//                playBtn.setEnabled(false);
//                seekBar.setEnabled(false);
            }
        });
    }

    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    String TAG = "zbf";
}
