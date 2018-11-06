package module.video.jnc.myffmpeg;

import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhc on 2017/11/1.
 * http://blog.51cto.com/ticktick/1746136
 */

public class FFmpegUtils {
    static {
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");
        System.loadLibrary("avfilter");
        System.loadLibrary("my_ffmpeg");
    }

    private static List<Lis> listNativeNotify = new ArrayList<>();

    public static void addNativeNotify(Lis lis){
        listNativeNotify.add(lis);
    }

    public static void removeNotify(Lis lis){
        listNativeNotify.remove(lis);
    }

    public interface Lis{
        void nativeNotify(String str);
    }

    //下面是做音视频播放器的
    public static native int initMp4Play(String path, Object glSurfaceView);
    public static native float getDuration();
    public static native int destroyMp4Play();
    public static native int mp4Pause();
    public static native int mp4Play();
    public static native int getProgress();
    public static native int changeSpeed(float speed);
    public static native int seekStart();
    public static native int seek(float progress);


    //rtmp推流部分
    public static native int rtmpInit(String outPath , String inputPath);
    public static native int rtmpClose();

    //通过手机摄像头推送rtmp
    public static native int rtmpCameraInit(String outPath , int width , int height , int pcmSize);
    public static native int rtmpCameraStream(byte[] bytes);
    public static native int rtmpAudioStream(byte[] bytes , int size);
    public static native int rtmpDestroy();
    public static native int startRecord();
    public static native int pauseRecord();
    public static native int test();

    //srs_lib_rtmp
    public static native int srsTest(String path);
    public static native int srsDestroy();

    //flv
    public static native String flvParse(String path);

    //h264
    public static native String h264Parse(String path);
    public static native byte[] getNextNalu(String path);

    //aac
    public static native String aacParse(String path);
    public static native  byte[]  getAACFrame(String path);

    //视频剪辑
    public static native void startClip(String path , String output ,   int start , int end);
    public static native void destroyClip();

    //视频拼接
    public static native void startJoint(String[] paths , String output  , int outWidth , int outHeight);
    public static native void destroyJoint();


    //opengl test
    public static native void openGlTest(String path, Object glSurfaceView);
    public static native void openDestroy();

    //视频倒放
    public static native void startBackRun(String inputPath , String output);
    public static native void destroyBackRun();



    public static void nativeNotify(String str){
        for(Lis lis : listNativeNotify){
            lis.nativeNotify(str);
        }
    }

}
