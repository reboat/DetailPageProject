package com.zjrb.zjxw.detailproject.widget;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aliya.uimode.mode.Attr;
import com.aliya.uimode.utils.UiModeUtils;
import com.zjrb.core.permission.IPermissionCallBack;
import com.zjrb.core.permission.IPermissionOperate;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bean.ZBJTStartRecordRspBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.share.BaseDialogFragment;
import port.JsInterfaceCallBack;

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
    @BindView(R2.id.tv_des_text)
    TextView mTvDesText;
    @BindView(R2.id.iv_play_pause)
    ImageView mIvPlayPause;
    @BindView(R2.id.tv_time_1)
    TextView mTvTime1;
    @BindView(R2.id.tv_time_2)
    TextView mTvTime2;
    @BindView(R2.id.pb_bar)
    SeekBar mPbBar;

    //线程池
    private ExecutorService mExecutorService;
    //录音API
    private MediaRecorder mMediaRecorder;
    //录音所保存的文件
    private File mAudioFile;
    //录音文件保存位置
    private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio/";
    private String mFileName = "";
    //当前是否正在播放
    private volatile boolean isPlaying = false;
    //播放音频文件API
    private MediaPlayer mediaPlayer;

    //计时器
    private Timer mTimer = new Timer();

    private JsInterfaceCallBack callBack;
    private String mCallBack;
    private ZBJTStartRecordRspBean mBean;

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
        mTvTime1.setText("03:00");
        mPbBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return dialog;
    }

    public AudioDialog setJSCallBack(String callback) {
        mCallBack = callback;
        return this;
    }

    public AudioDialog setCallBack(JsInterfaceCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public AudioDialog setZBJTStartRecordRspBean(ZBJTStartRecordRspBean bean) {
        mBean = bean;
        return this;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mTvTime1.post(new Runnable() {
                        @Override
                        public void run() {
                            mPbBar.setProgress(count);
                            if (count == 180) {
                                stopRecord();
                            }
                            mTvTime1.setText(calculateTime(count) + "/");
                            mTvTime2.setVisibility(View.VISIBLE);
                            mTvTime2.setText("03:00");
                        }
                    });
                    break;
                case 2:
                    if (mediaPlayer != null) {
                        mTvTime1.post(new Runnable() {
                            @Override
                            public void run() {
                                //开始时间
                                mTvTime1.setText(calculateTime(position / 1000) + "/");
                                //结束时间
                                mTvTime2.setVisibility(View.VISIBLE);
                                mTvTime2.setText(calculateTime(duration2));
                                mPbBar.setProgress(position);
                            }
                        });
                    }
                    break;
                case 3:
                    if (mediaPlayer != null) {
                        mTvTime2.post(new Runnable() {
                            @Override
                            public void run() {
                                //将音乐总时间设置为SeekBar的最大值
                                mPbBar.setProgress(0);
                                mPbBar.setMax(duration);
                                //显示提交按钮
                                if (mTvSubmit.getVisibility() == View.GONE) {
                                    mTvSubmit.setVisibility(View.VISIBLE);
                                }
                                if (mTvTime2.getVisibility() == View.GONE) {
                                    mTvTime2.setVisibility(View.VISIBLE);
                                }
                                mTvDesText.setText(R.string.module_core_audio_play_pause);
                                UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC,
                                        R.mipmap.module_core_audio_playing);
                            }
                        });
                    }
                    break;
                case 4:
                    if (mediaPlayer != null) {
                        mTvTime2.post(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.seekTo(0);
                                mPbBar.setProgress(0);
                                //显示提交按钮
                                if (mTvSubmit.getVisibility() == View.GONE) {
                                    mTvSubmit.setVisibility(View.VISIBLE);
                                }
                                if (mTvTime2.getVisibility() == View.VISIBLE) {
                                    mTvTime2.setVisibility(View.GONE);
                                }
                                mTvDesText.setText(R.string.module_core_audio_start_play);
                                UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC,
                                        R.mipmap.module_core_audio_play);
                            }
                        });
                    }
                    break;
            }
        }
    };

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

    private void dismissFragmentDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (null != dialog) {
            dialog.getWindow().setLayout(-1, -2);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mTvDesText.getText().equals("点击暂停")) {
            playPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseRecorder();
        releasePlayer();
        dismissFragmentDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseRecorder();
        releasePlayer();
        mExecutorService.shutdownNow();
    }

    @OnClick({R2.id.iv_arrow, R2.id.tv_submit, R2.id.iv_play_pause})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        //隐藏
        if (v.getId() == R.id.iv_arrow) {
            releaseRecorder();
            releasePlayer();
            dismissFragmentDialog();
            //提交
        } else if (v.getId() == R.id.tv_submit) {
            if (callBack != null) {
                if (mBean != null) {
                    mBean.setCode("1");
                    mBean.getData().setAudioPath(mFilePath + mFileName);
                    callBack.startRecord(mBean, mCallBack);
                } else {
                    mBean.setCode("0");
                    callBack.startRecord(mBean, mCallBack);
                }
                releaseRecorder();
                releasePlayer();
                dismissFragmentDialog();
            }
            //播放/暂停/录音
        } else if (v.getId() == R.id.iv_play_pause) {
            //开始播放
            if (mTvDesText.getText().equals("点击播放")) {
                if (isPlayPause) {
                    playResume();
                } else {
                    if (Build.VERSION.SDK_INT > 22) {
                        startPlayFM(mAudioFile);
                    } else {
                        playAudio(mAudioFile);
                    }

                }

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
                playPause();
            }
        }
    }


    /**
     * 开始录音
     */
    private void startRecord() {
        //再次录音前将前一个录音文件删除
        if (mAudioFile != null && mAudioFile.exists()) {
            mAudioFile.delete();
        }
        if (mTvTime2.getVisibility() == View.GONE) {
            mTvTime2.setVisibility(View.VISIBLE);
        }
        mTvDesText.setText(R.string.module_core_audio_record_stop);
        UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC, R.mipmap.module_core_audio_record);
        //异步任务执行录音操作
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //播放前释放资源
//                releaseRecorder();
                //执行录音操作
                recordOperation();
            }
        });
    }

    /**
     * 释放录音/音频资源
     */
    private void releaseRecorder() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 释放音频资源
     */
    private void releasePlayer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnPreparedListener(null);
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 录音操作
     */
    private void recordOperation() {
        //创建MediaRecorder对象
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        mFileName = System.currentTimeMillis() + ".aac";
        mAudioFile = new File(mFilePath + mFileName);
        //创建父文件夹
        mAudioFile.getParentFile().mkdirs();
        try {
            //创建文件
            mAudioFile.createNewFile();
            //配置mMediaRecorder相应参数
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
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
            mPbBar.setMax(180);
            if (mTimer == null) {
                mTimer = new Timer();
            }
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    count++;
                    //TODO WLJ
//                    count += 30;
                    mHandler.sendEmptyMessage(1);
                }
            }, 0, 1000);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //隐藏提交按钮
            if (mTvSubmit.getVisibility() == View.VISIBLE) {
                mTvSubmit.setVisibility(View.GONE);
            }
            mTvDesText.setText(R.string.module_core_audio_start_record);
            UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC, R.mipmap.module_core_audio_unrecord);
            recordFail();
        } catch (Exception e) {
            e.printStackTrace();
            if (mTvSubmit.getVisibility() == View.VISIBLE) {
                mTvSubmit.setVisibility(View.GONE);
            }
            mTvDesText.setText(R.string.module_core_audio_start_record);
            UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC, R.mipmap.module_core_audio_unrecord);
            recordFail();
        }
    }

    private int count = 0;

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
        //显示提交按钮
        if (mTvSubmit.getVisibility() == View.GONE) {
            mTvSubmit.setVisibility(View.VISIBLE);
        }
        mTvTime1.setText(calculateTime(count));
        if (mTvTime2.getVisibility() == View.VISIBLE) {
            mTvTime2.setVisibility(View.GONE);
        }
        mTvDesText.setText(R.string.module_core_audio_start_play);
        UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC, R.mipmap.module_core_audio_play);
        //停止录音
        mMediaRecorder.stop();
//        //录音完成释放资源
        releaseRecorder();
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


    private void startPlayFM(final File mFile) {
        PermissionManager.get().request((IPermissionOperate) UIUtils.getActivity(), new IPermissionCallBack() {
            @Override
            public void onGranted(boolean isAlreadyDef) {
                playAudio(mFile);
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
        if (!mFile.exists()) {
            T.showShortNow(UIUtils.getApp(), "播放文件不存在");
            return;
        }
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
     * 初始化
     */
    public void initPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail();
                    return;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int duration2;
    private int position;
    private int duration;

    /**
     * 开始播放
     *
     * @param mFile
     */
    private void startPlay(File mFile) {
        try {
            releasePlayer();
            //初始化播放器
            initPlayer();
            //设置播放音频数据文件

            mediaPlayer.setDataSource(mFile.getAbsolutePath());
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail();
                    return true;
                }
            });
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    //播放器音量配置
                    mp.setVolume(1f, 1f);
                    duration = mediaPlayer.getDuration();
                    mHandler.sendEmptyMessage(3);
                    if (mTimer == null) {
                        mTimer = new Timer();
                    }
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                                duration2 = mediaPlayer.getDuration() / 1000;
                                //获取音乐当前播放的位置
                                position = mediaPlayer.getCurrentPosition();
                                mHandler.sendEmptyMessage(2);
                            }
                        }
                    }, 0, 50);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail();
        }

    }

    /**
     * 停止播放/播放失败
     */
    //TODO mediaPlayer == null??
    private void playEndOrFail() {
        isPlaying = false;
        if (null != mediaPlayer) {
            mTvTime1.setText(calculateTime(count));
            mediaPlayer.seekTo(0);
            mPbBar.setProgress(0);
            //显示提交按钮
            if (mTvSubmit.getVisibility() == View.GONE) {
                mTvSubmit.setVisibility(View.VISIBLE);
            }
            if (mTvTime2.getVisibility() == View.VISIBLE) {
                mTvTime2.setVisibility(View.GONE);
            }
            mTvDesText.setText(R.string.module_core_audio_start_play);
            UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC, R.mipmap.module_core_audio_play);
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            mHandler.sendEmptyMessage(4);
            releasePlayer();
        }
    }

    private boolean isPlayPause = false;

    /**
     * 暂停播放
     */
    private void playPause() {
        //显示提交按钮
        if (mTvSubmit.getVisibility() == View.GONE) {
            mTvSubmit.setVisibility(View.VISIBLE);
        }
        mTvTime1.setText(calculateTime(position / 1000));
        if (mTvTime2.getVisibility() == View.VISIBLE) {
            mTvTime2.setVisibility(View.GONE);
        }
        mTvDesText.setText(R.string.module_core_audio_start_play);
        UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC, R.mipmap.module_core_audio_play);
        isPlayPause = true;
        if (null != mediaPlayer && mTimer != null && mediaPlayer.isPlaying()) {
            mTimer.purge();
            mediaPlayer.pause();
        }
    }

    /**
     * 继续播放
     */
    private void playResume() {
        //显示提交按钮
        if (mTvSubmit.getVisibility() == View.GONE) {
            mTvSubmit.setVisibility(View.VISIBLE);
        }
        if (mTvTime2.getVisibility() == View.GONE) {
            mTvTime2.setVisibility(View.VISIBLE);
        }
        mTvDesText.setText(R.string.module_core_audio_play_pause);
        UiModeUtils.applySave(mIvPlayPause, Attr.NAME_SRC, R.mipmap.module_core_audio_playing);
        isPlayPause = false;
        if (null != mediaPlayer) {
            try {
                mediaPlayer.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                //播放失败正理
                playEndOrFail();
            }

        }
    }


    /**
     * 计算播放时间,到三分钟后自动停止
     *
     * @param time
     * @return
     */
    public String calculateTime(int time) {
        int minute;
        int second;

        if (time >= 60) {
            minute = time / 60;
            second = time % 60;
            //分钟在0-9
            if (minute >= 0 && minute <= 3) {
                //判断秒
                if (second >= 0 && second < 10) {
                    return "0" + minute + ":" + "0" + second;
                } else {
                    return "0" + minute + ":" + second;
                }

            }
        } else if (time < 60) {
            second = time;
            if (second >= 0 && second < 10) {
                return "00:" + "0" + second;
            } else {
                return "00:" + second;
            }

        }
        return null;
    }
}