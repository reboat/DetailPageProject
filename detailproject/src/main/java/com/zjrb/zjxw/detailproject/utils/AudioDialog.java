package com.zjrb.zjxw.detailproject.utils;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.uimode.utils.UiModeUtils;
import com.zjrb.core.common.permission.IPermissionCallBack;
import com.zjrb.core.common.permission.IPermissionOperate;
import com.zjrb.core.common.permission.Permission;
import com.zjrb.core.common.permission.PermissionManager;
import com.zjrb.core.ui.UmengUtils.BaseDialogFragment;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 录音相关
 * Created by wanglinjie.
 * create time:2017/11/14  下午14:18
 */

public class AudioDialog extends BaseDialogFragment {

    protected Dialog dialog;


    /**
     * @return
     */
    private static AudioDialog fragment = null;
    @BindView(R2.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R2.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R2.id.v_top_container)
    RelativeLayout mVTopContainer;
    @BindView(R2.id.tv_des_text)
    TextView mTvDesText;
    @BindView(R2.id.iv_play_pause)
    ImageView mIvPlayPause;
    @BindView(R2.id.tv_time_1)
    TextView mTvTime1;
    @BindView(R2.id.tv_time_2)
    TextView mTvTime2;
    @BindView(R2.id.pb_bar)
    ProgressBar mPbBar;

    private final String PLAY_POSITION = "position";
    //录音成功
    private final int RECORD_SUCCESS = 100;
    //录音失败
    private final int RECORD_FAIL = 101;
    //录音时间太短
    private final int RECORD_TOO_SHORT = 102;
    //安卓6.0以上手机权限处理
    private final int PERMISSIONS_REQUEST_FOR_AUDIO = 1;
    //播放完成
    private final int PLAY_COMPLETION = 103;
    //播放错误
    private final int PLAY_ERROR = 104;

    //线程池
    private ExecutorService mExecutorService;
    //录音API
    private MediaRecorder mMediaRecorder;
    //录音所保存的文件
    private File mAudioFile;
    //录音文件保存位置
    private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio/";
    //当前是否正在播放
    private volatile boolean isPlaying;
    //播放音频文件API
    private MediaPlayer mediaPlayer;

    //录音开始时间与结束时间
    private long startTime, endTime;

    public static AudioDialog newInstance() {
        fragment = new AudioDialog();
        return fragment;
    }

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //录音及播放要使用单线程操作
        mExecutorService = Executors.newSingleThreadExecutor();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        View view = View.inflate(getContext(), R.layout.module_detail_dialog_audio, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        initWindow();
        return dialog;
    }

    /**
     * 设置底部弹出框的窗口样式
     */
    private void initWindow() {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (null != dialog) {
            dialog.getWindow().setLayout(-1, -2);
        }
    }


    public void dismissFragmentDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        playPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseRecorder();
        dismissFragmentDialog();
    }

    @OnClick({R2.id.iv_arrow, R2.id.tv_submit, R2.id.iv_play_pause})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        //隐藏
        if (v.getId() == R.id.iv_arrow) {

            //提交
        } else if (v.getId() == R.id.tv_submit) {

            //播放/暂停/录音
        } else if (v.getId() == R.id.iv_play_pause) {
            //开始播放
            if (mTvDesText.getText().equals("点击播放")) {
                startPlay(mAudioFile);
                //点击录音
            } else if (mTvDesText.getText().equals("点击录音")) {
                if (Build.VERSION.SDK_INT > 22) {
                    startRecordFM();
                } else {
                    startRecord();
                }
                //结束录音
            } else if (mTvDesText.getText().equals("点击结束")) {
                stopRecord();
                //暂停播放
            } else if (mTvDesText.getText().equals("点击暂停")) {
                if (isPlayPause) {
                    playResume();
                } else {
                    playPause();
                }
            }
        }
    }

    /**
     * 开始录音
     */
    private void startRecord() {
        mTvDesText.setText(R.string.module_detail_audio_start_record);
        UiModeUtils.applyImageSrc(mIvPlayPause, R.attr.module_detail_audio_unrecord);
        //异步任务执行录音操作
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //播放前释放资源
                releaseRecorder();
                //执行录音操作
                recordOperation();
            }
        });
    }

    /**
     * 释放录音资源
     */
    private void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 录音操作
     */
    private void recordOperation() {
        //创建MediaRecorder对象
        mMediaRecorder = new MediaRecorder();
        //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        mAudioFile = new File(mFilePath + System.currentTimeMillis() + ".m4a");
        //创建父文件夹
        mAudioFile.getParentFile().mkdirs();
        try {
            //创建文件
            mAudioFile.createNewFile();
            //配置mMediaRecorder相应参数
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
            mMediaRecorder.setAudioSamplingRate(44100);
            //设置声音数据编码格式,音频通用格式是AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置编码频率
            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置录音保存的文件
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //记录开始录音时间
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            mTvDesText.setText(R.string.module_detail_audio_start_record);
            UiModeUtils.applyImageSrc(mIvPlayPause, R.attr.module_detail_audio_unrecord);
            recordFail();
        }
    }

    /**
     * 录音失败
     */
    private void recordFail() {
        mAudioFile = null;
    }

    /**
     * 停止录音
     */
    private void stopRecord() {
        mTvDesText.setText(R.string.module_detail_audio_start_play);
        UiModeUtils.applyImageSrc(mIvPlayPause, R.attr.module_detail_audio_play);
        //停止录音
        mMediaRecorder.stop();
        //记录停止时间
        endTime = System.currentTimeMillis();
        //录音完成释放资源
        releaseRecorder();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdownNow();
    }

    /**
     * 录音
     * 6.0以上录音权限动态获取
     */
    private void startRecordFM() {
        PermissionManager.get().request((IPermissionOperate) UIUtils.getActivity(), new IPermissionCallBack() {
            @Override
            public void onGranted(boolean isAlreadyDef) {
                startRecord();
            }

            @Override
            public void onDenied(List<String> neverAskPerms) {
            }

            @Override
            public void onElse(List<String> deniedPerms, List<String> neverAskPerms) {

            }
        }, Permission.MICROPHONE_RECORD_AUDIO, Permission.STORAGE_WRITE, Permission.STORAGE_READE);

    }

    /**
     * 播放音频
     *
     * @param mFile
     */
    private void playAudio(final File mFile) {
        if (null != mFile && !isPlaying) {
            isPlaying = true;
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    startPlay(mFile);
                }
            });
        }
    }

    /**
     * 开始播放
     *
     * @param mFile
     */
    private void startPlay(File mFile) {
        try {
            mTvDesText.setText(R.string.module_detail_audio_record_pause);
            UiModeUtils.applyImageSrc(mIvPlayPause, R.attr.module_detail_audio_playing);
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(mFile.getAbsolutePath());
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }

    }

    /**
     * 停止播放/播放失败
     *
     * @param isEnd
     */
    private void playEndOrFail(boolean isEnd) {
        mTvDesText.setText(R.string.module_detail_audio_start_play);
        UiModeUtils.applyImageSrc(mIvPlayPause, R.attr.module_detail_audio_play);
        isPlaying = false;
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private boolean isPlayPause = false;

    /**
     * 暂停播放
     */
    private void playPause() {
        mTvDesText.setText(R.string.module_detail_audio_start_play);
        UiModeUtils.applyImageSrc(mIvPlayPause, R.attr.module_detail_audio_play);
        isPlayPause = true;
        if (null != mediaPlayer) {
            mediaPlayer.pause();
        }

    }

    /**
     * 继续播放
     */
    private void playResume() {
        mTvDesText.setText(R.string.module_detail_audio_record_pause);
        UiModeUtils.applyImageSrc(mIvPlayPause, R.attr.module_detail_audio_playing);
        isPlayPause = false;
        if (null != mediaPlayer) {
            mediaPlayer.start();
        }
    }
}
