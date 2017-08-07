package zbf.voiceline_demo;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * 播放的服务
 */
public class PlayService extends Service
{
    private MediaPlayer mediaPlayer;
    //把状态图存在静态变量中 播放完事设置为空
    private ImageView icon_play;
    private String myUri;

    private static final int PLAY_MUSIC = 0;
    private static final int STOP_MUSIC = 1;

    /**
     * 通过客户端发送消息的目标
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());


    public PlayService()
    {
    }

    /**
     * 处理从客户端发来的消息
     */
    class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            Log.e("zbf",msg.toString());
            switch (msg.what)
            {
                case PLAY_MUSIC:

                    break;
                case STOP_MUSIC:

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

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
        //播放结束时的回掉
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                //TODO
            }
        });
    }

    private void play()
    {

    }

    private void stop()
    {

    }




    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        return mMessenger.getBinder();
    }

}
