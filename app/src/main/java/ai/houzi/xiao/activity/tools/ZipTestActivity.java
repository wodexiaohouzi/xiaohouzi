package ai.houzi.xiao.activity.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.juxin.common.FileActivity;
import com.juxin.common.FileListActivity;

import java.text.SimpleDateFormat;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.utils.IntentSystem;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.utils.ZipUtil;
import ai.houzi.xiao.widget.BigToast;
import ai.houzi.xiao.widget.MyAlertDialog;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 压缩
 */
public class ZipTestActivity extends BaseActivity {
    private TitleBar titleBar;
    private EditText etZip, etTarget, etUnZip, etUnTarget;
    private ImageView ivZipFile, ivZipForder, ivTarget, ivUnZip, ivUnTarget;
    private Button btnZip, btnUnZip;
    private Intent intent;

    private ZipUtil zipUtil;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_zip);
        intent = new Intent();
        zipUtil = new ZipUtil();
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("压缩和解压缩");
        titleBar.setLeftText("返回", this);
        etZip = (EditText) findViewById(R.id.etZip);
        ivZipFile = (ImageView) findViewById(R.id.ivZipFile);
        ivZipForder = (ImageView) findViewById(R.id.ivZipForder);
        etTarget = (EditText) findViewById(R.id.etTarget);
        ivTarget = (ImageView) findViewById(R.id.ivTarget);
        btnZip = (Button) findViewById(R.id.btnZip);

        etUnZip = (EditText) findViewById(R.id.etUnZip);
        ivUnZip = (ImageView) findViewById(R.id.ivUnZip);
        etUnTarget = (EditText) findViewById(R.id.etUnTarget);
        ivUnTarget = (ImageView) findViewById(R.id.ivUnTarget);
        btnUnZip = (Button) findViewById(R.id.btnUnZip);
    }

    @Override
    protected void initListener() {
        ivZipFile.setOnClickListener(this);
        ivZipForder.setOnClickListener(this);
        ivTarget.setOnClickListener(this);
        btnZip.setOnClickListener(this);
        ivUnZip.setOnClickListener(this);
        ivUnTarget.setOnClickListener(this);
        btnUnZip.setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    AlertDialog dialog;
    TextView tvZipName, tvFileCount, tvFileName, tvFileProgress, tvTotalProgress;
    private LinearLayout llFile, llTotal;
    ProgressBar pbFile, pbTotal;

    private void showProgressDialog(CharSequence title, int type) {
        if (dialog == null) {
            View view = View.inflate(ZipTestActivity.this, R.layout.view_zip, null);
            dialog = new AlertDialog.Builder(ZipTestActivity.this)
                    .setTitle(TextUtils.isEmpty(title) ? "正在创建压缩文件" : title)
                    .setView(view)
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            zipNotification();
                        }
                    }).create();
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            tvZipName = (TextView) view.findViewById(R.id.tvZipName);
            tvFileCount = (TextView) view.findViewById(R.id.tvFileCount);
            tvFileName = (TextView) view.findViewById(R.id.tvFileName);
            tvFileProgress = (TextView) view.findViewById(R.id.tvFileProgress);
            pbFile = (ProgressBar) view.findViewById(R.id.pbFile);
            tvTotalProgress = (TextView) view.findViewById(R.id.tvTotalProgress);
            pbTotal = (ProgressBar) view.findViewById(R.id.pbTotal);
            llFile = (LinearLayout) view.findViewById(R.id.llFile);
            llTotal = (LinearLayout) view.findViewById(R.id.llTotal);
        }
        if (dialog != null) {
            if (type == 1) {
                llFile.setVisibility(View.VISIBLE);
            } else {
                llFile.setVisibility(View.GONE);
            }
            dialog.show();
        }
    }

    NotificationManager mNotificationManager;
    Notification notification;

    private void zipNotification() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder
                .setContent(new RemoteViews(getPackageName(), R.layout.notification_zip))
                .setContentTitle("压缩文件")//设置通知栏标题
                .setContentText("") //设置通知栏显示内容
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.filesystem_icon_rar))
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                .setSmallIcon(R.drawable.ic_launcher)//设置通知小ICON
//  .setNumber(number) //设置通知集合的数量
                .setTicker("小侯子压缩文件") //通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        ;

        notification = mBuilder.build();
//        notification.contentView = new RemoteViews(getPackageName(), R.layout.notification_zip);
        notification.contentView.setTextViewText(R.id.tvTime, dateFormat.format(System.currentTimeMillis()));
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
            case R.id.ivZipFile:
                zipNotification();
//                IntentSystem.showFileChooser(this, 100, "*");//打开系统的文件管理器
//                intent.setClass(this, FileActivity.class);
//                intent.putExtra("type", FileListActivity.sFILE);
//                startActivityForResult(intent, 100);
                break;
            case R.id.ivZipForder:
                intent.setClass(this, FileActivity.class);
                intent.putExtra("type", FileListActivity.sFORDER);
                startActivityForResult(intent, 99);
                break;
            case R.id.ivTarget:
                intent.setClass(this, FileActivity.class);
                intent.putExtra("type", FileListActivity.sFORDER);
                startActivityForResult(intent, 101);
                break;
            case R.id.btnZip:
                showProgressDialog("正在压缩文件", 1);
                zipUtil.setZipListener(new ZipUtil.ZipListener() {
                    @Override
                    public void startZip(String zipName) {
                        tvZipName.setText("压缩文件  " + zipName);
                    }

                    @Override
                    public void endZip(boolean isSuccess) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (!isSuccess) {
                            showBigToast(BigToast.WRONG, "路径有误\n请重新选择路径！");
                        }
                        if (mNotificationManager != null) {
                            mNotificationManager.cancelAll();
                        }
                    }

                    @Override
                    public void progressFileZip(int progress, String fileName) {
                        tvFileCount.setText("正在添加");
                        tvFileName.setText(fileName);
                        tvFileProgress.setText(progress + "%");
                        pbFile.setProgress(progress);
                    }

                    @Override
                    public void progressTotalZip(int progress) {
                        if (dialog != null && dialog.isShowing()) {
                            tvTotalProgress.setText(progress + "%");
                            pbTotal.setProgress(progress);
                        } else if (notification != null && notification.contentView != null && mNotificationManager != null) {
                            notification.contentView.setProgressBar(R.id.rpb, 100, progress, false);
                            notification.contentView.setTextViewText(R.id.tvPb, progress + "%");
                            mNotificationManager.notify(1, notification);
                        }
                    }

                    @Override
                    public void countFileCount(int count) {
                        tvFileCount.setText(Html.fromHtml("找到 <font color=#17B4EB>" + count + "</font> 个文件"));
                    }
                });
                zipUtil.zipFolder(etZip.getText().toString().trim(), etTarget.getText().toString().trim());
                break;
            case R.id.ivUnZip:
                intent.setClass(this, FileActivity.class);
                intent.putExtra("type", FileListActivity.sFILE);
                startActivityForResult(intent, 102);
                break;
            case R.id.ivUnTarget:
                intent.setClass(this, FileActivity.class);
                intent.putExtra("type", FileListActivity.sFORDER);
                startActivityForResult(intent, 103);
                break;
            case R.id.btnUnZip:
                showProgressDialog("正在解压文件", 2);
                zipUtil.setUnZipListener(new ZipUtil.UnZipListener() {
                    @Override
                    public void startUnZip(String unZipName) {
                        tvZipName.setText("解压文件  " + unZipName);
                    }

                    @Override
                    public void endUnZip(boolean isSuccess) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (!isSuccess) {
                            showBigToast(BigToast.WRONG, "路径有误\n请重新选择路径！");
                        }
                        if (mNotificationManager != null) {
                            mNotificationManager.cancelAll();
                        }
                    }

                    @Override
                    public void progressUnZip(int progress) {
                        if (dialog != null && dialog.isShowing()) {
                            tvTotalProgress.setText(progress + "%");
                            pbTotal.setProgress(progress);
                        } else if (notification != null && notification.contentView != null && mNotificationManager != null) {
                            notification.contentView.setProgressBar(R.id.rpb, 100, progress, false);
                            notification.contentView.setTextViewText(R.id.tvPb, progress + "%");
                            mNotificationManager.notify(1, notification);
                        }
                    }
                });
                zipUtil.Unzip(etUnZip.getText().toString().trim(), etUnTarget.getText().toString().trim(), false);
                break;
        }
    }

    /**
     * 根据返回选择的文件，来进行上传操作
     **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                if (data != null) {
                    Uri uri = data.getData();
                    String url = uri.getPath();
                    etZip.setText(url);
                    Logg.d(url);
                }
            } else if (requestCode == 99) {
                if (data != null) {
                    String path = data.getStringExtra("path");
                    etZip.setText(path);
                    Logg.d(path);
                }
            } else if (requestCode == 101) {
                if (data != null) {
                    String path = data.getStringExtra("path");
                    etTarget.setText(path);
                    Logg.d(path);
                }
            } else if (requestCode == 102) {
                if (data != null) {
                    String path = data.getStringExtra("path");
                    etUnZip.setText(path);
                    Logg.d(path);
                }
            } else if (requestCode == 103) {
                if (data != null) {
                    String path = data.getStringExtra("path");
                    etUnTarget.setText(path);
                    Logg.d(path);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
