package ve.com.phl.gratimetro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ve.com.phl.gratimetro.Utils.StorageUtils;
import ve.com.phl.gratimetro.Utils.Utils;

public class SplashActivity extends AppCompatActivity {

    @OnClick (R.id.bt) void login(){
        if (StorageUtils.getVibration(this))
            Utils.vibrar(200,this);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }
}
