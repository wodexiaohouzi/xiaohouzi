package ai.houzi.xiao;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author yuxianglong
 * @ClassName: MyActionProvider
 * @Description: 自定义一个视窗操作器，实现构造函数和onCreateActionView即可
 * @date 2013-7-11 下午3:13:44
 */
public class MyActionProvider extends ActionProvider {

    private Context context;
    private LayoutInflater inflater;
    private View view;
    private ImageView button;

    public MyActionProvider(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.myactionprovider, null);
    }


    @Override
    public View onCreateActionView() {
//        button = (ImageView) view.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "是我，没错", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }

}

