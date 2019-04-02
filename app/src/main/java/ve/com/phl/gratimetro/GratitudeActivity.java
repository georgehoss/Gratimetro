package ve.com.phl.gratimetro;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ve.com.phl.gratimetro.Model.Gratitude;
import ve.com.phl.gratimetro.Utils.StorageUtils;
import ve.com.phl.gratimetro.Utils.Utils;

public class GratitudeActivity extends AppCompatActivity {
    private static final int REALM_DATABASE_VERSION = 1;
    private Realm realm;
    private int pos =0;
    SoundPool soundPool;
    SoundPool.Builder spBuilder;
    AudioAttributes attributes;
    AudioAttributes.Builder aBuilder;
    int soundID;
    @BindView(R.id.tv_cD) TextView mTvCd;
    @BindView(R.id.iv_td) ImageView mIvD;
    @BindView(R.id.ll_bg) LinearLayout mLlD;
    @BindView(R.id.iv_te) ImageView mIvE;
    @BindView(R.id.ll_be) LinearLayout mLlE;
    @BindView(R.id.iv_ty) ImageView mIvY;
    @BindView(R.id.ll_by) LinearLayout mLlY;
    @BindView(R.id.tv_cE) TextView mTvCe;
    @BindView(R.id.tv_cY) TextView mTvCy;
    @BindView(R.id.tv_title) TextView mTvTitle;


    @OnClick(R.id.ll_bg) void onGodClick(){
        gratitudeDialog(Gratitude.GOD);
    }

    @OnClick(R.id.bt_t1) void onBtGodClick(){
        gratitudeDialog(Gratitude.GOD);
    }

    @OnClick(R.id.ll_be) void onEClick(){
        gratitudeDialog(Gratitude.ENVIROMENT);
    }

    @OnClick(R.id.bt_t2) void onBtEClick(){
        gratitudeDialog(Gratitude.ENVIROMENT);
    }

    @OnClick(R.id.ll_by) void onMeClick(){
        gratitudeDialog(Gratitude.SELF);
    }

    @OnClick(R.id.bt_t3) void onBtYClick(){
        gratitudeDialog(Gratitude.SELF);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratitude);
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("gratimetro.realm")
                .schemaVersion(REALM_DATABASE_VERSION) // Must be bumped when the schema changes
                //.migration( migration ) // Migration to run instead of throwing an exception
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.compactRealm(realmConfiguration);
        Realm.setDefaultConfiguration(realmConfiguration);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        setData();
        createSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        createSound();
        pos = StorageUtils.getPosMsg(GratitudeActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
        soundPool.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gratitude_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.calendarBt)
            startActivity(new Intent(this,HistoricActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void setTitle(String name){
        String hi = getString(R.string.hello);
        String whom = getString(R.string.whom_would_you_like);
        String text = hi+", "+name+"\n"+whom;
        mTvTitle.setText(text);
    }

    public void gratitudeDialog(final int type) {
        if (StorageUtils.getVibration(this))
            Utils.vibrar(200,this);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_gratitude, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEtName = view.findViewById(R.id.et_name);
        final TextView mTvTitle = view.findViewById(R.id.tv_to);
        final Button mBtCancel = view.findViewById(R.id.bt_cancel);
        final Button mBtAccept = view.findViewById(R.id.bt_accept);
        mEtName.requestFocus();
        String text = getString(R.string.what_would_you_like);
        if (type == Gratitude.GOD)
            text+=" "+getString(R.string.to_god);
        else
        if (type == Gratitude.ENVIROMENT)
            text+=" "+getString(R.string.to_other_people);
        else
            text+=" "+getString(R.string.to_yourself);

        mTvTitle.setText(text);

        final AlertDialog dialog = alertDialogBuilder.create();
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtName.getText().toString().isEmpty()) {
                    mEtName.setError(getString(R.string.remember_to_thank));
                    mEtName.requestFocus();
                }
                else {
                    if (StorageUtils.getVibration(GratitudeActivity.this))
                        Utils.vibrar(400,GratitudeActivity.this);
                    if (StorageUtils.getSound(GratitudeActivity.this))
                        soundPool.play(soundID,1,1,0,0,1);
                    try {
                        realm.beginTransaction();
                        realm.copyToRealm(new Gratitude(Utils.getDateString(), mEtName.getText().toString(),
                                type, StorageUtils.getUserEmail(GratitudeActivity.this)));
                        realm.commitTransaction();
                    }
                    catch (Exception e){
                        Log.e("RealmError: ",e.getMessage());
                    }
                    finally {
                        imageDialog(type);
                        setData();
                    }

                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }



    public void imageDialog(int type) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_message, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        final TextView mTvTitle = view.findViewById(R.id.tv_title);
        String[] myResArray = getResources().getStringArray(R.array.messages);
        List<String> messages = Arrays.asList(myResArray);
        if (messages!=null && messages.size()>0) {
            mTvTitle.setText(messages.get(pos));
            pos++;
            if (pos>=messages.size())
                pos=0;

            StorageUtils.setPosMsg(GratitudeActivity.this,pos);
        }

        RelativeLayout linearLayout = view.findViewById(R.id.ll_image);
        if (type==Gratitude.ENVIROMENT)
            linearLayout.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.enviroment));
        if (type==Gratitude.SELF)
            linearLayout.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.self));
        final AlertDialog dialog = alertDialogBuilder.create();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();

    }

    private void setData(){
        RealmResults<Gratitude> dataD = realm.where(Gratitude.class).equalTo("type",Gratitude.GOD)
                .equalTo("date",Utils.getDateString()).equalTo("user",StorageUtils.getUserEmail(GratitudeActivity.this)).findAll();
        RealmResults<Gratitude> dataE = realm.where(Gratitude.class).equalTo("type",Gratitude.ENVIROMENT)
                .equalTo("date",Utils.getDateString()).equalTo("user",StorageUtils.getUserEmail(GratitudeActivity.this)).findAll();
        RealmResults<Gratitude> dataY = realm.where(Gratitude.class).equalTo("type",Gratitude.SELF)
                .equalTo("date",Utils.getDateString()).equalTo("user",StorageUtils.getUserEmail(GratitudeActivity.this)).findAll();
        setGvalue(dataD.size());
        setEvalue(dataE.size());
        setYvalue(dataY.size());
        setTitle(StorageUtils.getUserName(this));
    }

    public void setGvalue(int value){
        String text = value + "/10";
        mTvCd.setText(text);
        setDrawable(mIvD,value);
        setBackground(mLlD,value);
    }

    public void setEvalue(int value){
        String text = value + "/10";
        mTvCe.setText(text);
        setDrawable(mIvE,value);
        setBackground(mLlE,value);
    }

    public void setYvalue(int value){
        String text = value + "/10";
        mTvCy.setText(text);
        setDrawable(mIvY,value);
        setBackground(mLlY,value);
    }

    public void setDrawable(ImageView imageView, int value){

        switch (value){
            case 0:
                 imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro0));
                 break;
            case 1:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro10));
                break;
            case 2:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro20));
                break;
            case 3:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro30));
                break;
            case 4:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro40));
                break;
            case 5:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro50));
                break;
            case 6:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro60));
                break;
            case 7:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro70));
                break;
            case 8:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro80));
                break;
            case 9:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro90));
                break;
            default:
                imageView.setBackground(ContextCompat.getDrawable(GratitudeActivity.this,R.drawable.termometro100));
                break;
        }
    }

    public void setBackground(LinearLayout ll, int value){

        if (value==0)
            ll.setBackgroundColor(ContextCompat.getColor(GratitudeActivity.this,R.color.black));
        if (value>0 && value<=3)
            ll.setBackgroundColor(ContextCompat.getColor(GratitudeActivity.this,R.color.gray));
        if (value>3) {
            if (ll.getId()==R.id.ll_bg)
                ll.setBackground(ContextCompat.getDrawable(GratitudeActivity.this, R.drawable.gratitude1));
            else
            if (ll.getId()==R.id.ll_be)
                ll.setBackground(ContextCompat.getDrawable(GratitudeActivity.this, R.drawable.gratitude2));
            else
            if (ll.getId()==R.id.ll_by)
                ll.setBackground(ContextCompat.getDrawable(GratitudeActivity.this, R.drawable.gratitude3));
        }

    }

    private void createSound() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            aBuilder = new AudioAttributes.Builder();
            aBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
            aBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attributes=aBuilder.build();

            spBuilder = new SoundPool.Builder();
            spBuilder.setAudioAttributes(attributes);
            soundPool = spBuilder.build();}
        else
        {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        }

        soundID = soundPool.load(this,R.raw.chimes,1);
    }


}
