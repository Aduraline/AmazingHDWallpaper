package amazing.hd.amazinghdwallpaper.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import amazing.hd.amazinghdwallpaper.R;

public class CustomDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Button no;

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_row);
        no = (Button) findViewById(R.id.cancelnow);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelnow) {
            dismiss();
        }
        dismiss();
    }
}