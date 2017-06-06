package zbf.voiceline_demo;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Runnable
{
    private MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    private VoiceLineView voiceLineView;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
//                    if (mMediaRecorder == null) return;
//                    double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
                    double ratio = recorder.getRealVolume() / 100;
                    double db = 0;// 分贝
                    //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
                    //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
                    //同时，也可以配置灵敏度sensibility
                    if (ratio > 1)
                        db = 20 * Math.log10(ratio);
                    //只要有一个线程，不断调用这个方法，就可以使波形变化
                    //主要，这个方法必须在ui线程中调用
                    voiceLineView.setVolume((int) (db));
                    break;
/*                case MP3Recorder.MSG_REC_STARTED:
                case MP3Recorder.MSG_REC_RESTORE:
//                    statusTextView.setText("开始录音");
                    break;
                case MP3Recorder.MSG_REC_STOPPED:
//                    statusTextView.setText("");
                    break;
                case MP3Recorder.MSG_REC_PAUSE:
//                    statusTextView.setText("暂停录音");
                    break;*/

            }

        }
    };

    Button btn_start, btn_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewInit();

/*        try{

            voiceInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/


    }

    private MP3Recorder recorder;

    private void viewInit()
    {
        voiceLineView = (VoiceLineView) findViewById(R.id.voicLine);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Calendar calendar = Calendar.getInstance();
                    // 8KHz录制MP3,记得要加上录音权限哦~~
                    String time = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.DAY_OF_MONTH);
                    File path = new File(Environment.getExternalStorageDirectory() + "/" + time + ".mp3");
                    recorder = new MP3Recorder(path);
                    recorder.start();
//                    voiceInit();
                    Thread thread = new Thread(MainActivity.this);
                    thread.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recorder.stop();
            }
        });
    }

    /**
     * 初始化录音
     */
    private void voiceInit() throws IOException
    {
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        //使用音频源设置
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //使用输出文件格式设置
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        //使用音频编码器
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        File file = new File(Environment.getExternalStorageDirectory().getPath(), "hello.log");
        if (!file.exists())
        {
            file.createNewFile();
        }
        //使用输出文件名设置
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        mMediaRecorder.setMaxDuration(1000 * 60 * 10);

        //完成初始化
        mMediaRecorder.prepare();
        mMediaRecorder.start();


        Thread thread = new Thread(this);
        thread.start();


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

    @Override
    protected void onDestroy()
    {
        isAlive = false;
        //释放资源
//        mMediaRecorder.release();
//        mMediaRecorder = null;
        recorder.stop();
        super.onDestroy();
    }
}
