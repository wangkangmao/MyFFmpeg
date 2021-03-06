package module.video.jnc.myffmpeg.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import module.video.jnc.myffmpeg.FFmpegUtils;
import module.video.jnc.myffmpeg.MyRender;
import module.video.jnc.myffmpeg.MyVideoGpuShow;
import module.video.jnc.myffmpeg.widget.MyGLSurfaceViewParent;
import module.video.jnc.myffmpeg.widget.TitleBar;

import static android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY;

public class VideoEditParentActivity extends BaseActivity implements FFmpegUtils.Lis{
    protected List<String> listPath = new ArrayList<>();
    protected TitleBar titleBar;
    protected MyGLSurfaceViewParent myVideoGpuShow;
    protected boolean activityFoucsFlag = false;
    protected boolean dealFlag ;
    protected int progress;
    private static final int PROGRESS = 500;
    private static final int SHOW_NATIVE_MSG = 501;
    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case PROGRESS:
                    if (progress == 100) {
                        dismissLoadPorgressDialog();
                        showToast("已完成");

                        destroyFFmpeg();
                        break;
                    }
                    setLoadPorgressDialogProgress(progress);
                    break;
                case  SHOW_NATIVE_MSG:
                    String str = (String)msg.obj;
                    showToast(str);
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealFlag = false;
        FFmpegUtils.addNativeNotify(this);
        Intent intent = getIntent();
        ArrayList<String> tempVideos = intent.getStringArrayListExtra("videos");
        if (tempVideos != null) {
            listPath.addAll(tempVideos);
        }


    }
    @Override
    public void onBackPressed() {
        Log.e("xhc" , " onbackPress");
        if(dealFlag){
            showAlertDialog(null, "放弃编辑?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismissLoadPorgressDialog();
                    dialog.dismiss();
                    finish();
                }
            });
        }
        else{
            super.onBackPressed();
        }
    }
    protected void init() {
        myVideoGpuShow.setEGLContextClientVersion(2);
        myVideoGpuShow.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        myVideoGpuShow.setRenderer(new MyRender());//android 8.0需要设置
        myVideoGpuShow.setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    //播放的线程
    private StartPlayThraed playThread;

    protected void startPlayThread(String path) {
        playThread = new StartPlayThraed(path);
        playThread.start();
    }

    protected void stopPlayThread() {
        FFmpegUtils.destroyMp4Play();
        if (playThread != null) {
            try {
                playThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class StartPlayThraed extends Thread {

        String playPath;

        StartPlayThraed(String playPath) {
            this.playPath = playPath;
        }

        @Override
        public void run() {
            super.run();
            synchronized (VideoEditParentActivity.class) {
                playVideo(this.playPath);
            }
        }
    }

    private void playVideo(String path) {
        myVideoGpuShow.setPlayPath(path);
    }


    protected int getProgress(){
        return 1;
    }

    protected int destroyFFmpeg(){
        stopProgressThread();
        return 1;
    }


    @Override
    public void nativeNotify(String str) {
        if ( FFmpegUtils.isShowToastMsg(str) ) {
            Message msg = myHandler.obtainMessage();
            msg.what = SHOW_NATIVE_MSG ;
            msg.obj = str ;
            myHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FFmpegUtils.removeNotify(this);
        stopPlayThread();
        stopProgressThread();
        destroyFFmpeg();
        myHandler.removeCallbacksAndMessages(null);
    }



    //查看进度
    private ProgressThread progressThread;
    protected void startProgressThread() {
        stopProgressThread();
        progressThread = new ProgressThread();
        progressThread.progressFlag = true;
        progressThread.start();
    }

    protected void stopProgressThread() {
        if (progressThread != null) {
            progressThread.progressFlag = false;
            try {
                progressThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressThread = null;
        }
    }

    class ProgressThread extends Thread {
        boolean progressFlag = false;

        @Override
        public void run() {
            super.run();
            while (progressFlag) {
                progress = getProgress();
                myHandler.sendEmptyMessage(PROGRESS);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
