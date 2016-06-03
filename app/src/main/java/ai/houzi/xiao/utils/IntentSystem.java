package ai.houzi.xiao.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

/**
 * 系统跳转
 */
public class IntentSystem {
    /**
     * 调用文件选择软件来选择文件
     **/
    public static void showFileChooser(Activity activity, int requestCode, String name) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(name + "/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            activity.startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(activity, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 调用文件选择软件来选择文件
     **/
    public static void showForderChooser(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("forder/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            activity.startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(activity, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
