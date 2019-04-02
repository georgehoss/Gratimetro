package ve.com.phl.gratimetro;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ve.com.phl.gratimetro.Service.NotificationService;
import ve.com.phl.gratimetro.Utils.StorageUtils;
import ve.com.phl.gratimetro.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "gratimetro";

    @OnClick(R.id.bt_gratitude) void onGratitude() {
        if (StorageUtils.getVibration(this))
            Utils.vibrar(200,this);
        startActivity(new Intent(this,GratitudeActivity.class));

    }

    @OnClick(R.id.bt_sugerences) void onSugerences()
    {
        if (StorageUtils.getVibration(this))
            Utils.vibrar(200,this);
        sugerenciaDialog();
    }

    @OnClick(R.id.bt_configuration) void onConfig(){
        if (StorageUtils.getVibration(this))
            Utils.vibrar(200,this);
        contactDialog();
    }

    @OnClick(R.id.bt_qualify) void onQualify(){
        if (StorageUtils.getVibration(this))
            Utils.vibrar(200,this);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=ve.com.phl.gratimetro"));
        startActivity(intent);
    }

    @OnClick(R.id.bt_apps) void onApps(){
        if (StorageUtils.getVibration(this))
            Utils.vibrar(200,this);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=com.enterprises.quejometroandroid"));
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startService(new Intent(this,NotificationService.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (StorageUtils.getUserEmail(this).isEmpty())
            loginDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.shareBt)
        {
            if (StorageUtils.getVibration(this))
                Utils.vibrar(200,this);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Te recomiendo descargar el Gratímetro.\n\nLa única herramienta que permite liberarte de las quejas, y empodérarte en la gratitud" +
                    " \n\nhttps://play.google.com/store/apps/details?id=ve.com.phl.gratimetro");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Descarga la aplicación Gratímetro");
            startActivity(Intent.createChooser(shareIntent, "Comparte el Gratímetro con tus amigos"));
        }

        if (item.getItemId()==R.id.configuration){
            if (StorageUtils.getVibration(this))
                Utils.vibrar(200,this);
            configDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    public void loginDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_login, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEtName = view.findViewById(R.id.et_name);
        final EditText mEtLastName = view.findViewById(R.id.et_last_name);
        final EditText mEtEmail = view.findViewById(R.id.et_email);
        final Button mBtCancel = view.findViewById(R.id.bt_cancel);
        final Button mBtAccept = view.findViewById(R.id.bt_accept);

        final AlertDialog dialog = alertDialogBuilder.create();
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtName.getText().toString().isEmpty()) {
                    mEtName.setError("Introduce tu nombre");
                    mEtName.requestFocus();
                }
                else
                if (mEtLastName.getText().toString().isEmpty()) {
                    mEtLastName.setError("Introduce tu apellido");
                    mEtLastName.requestFocus();
                }
                else
                if (mEtEmail.getText().toString().isEmpty()) {
                    mEtEmail.setError("Introduce tu correo");
                    mEtEmail.requestFocus();
                }
                else {
                    StorageUtils.saveUserEmail(MainActivity.this,mEtEmail.getText().toString().trim());
                    StorageUtils.saveUserName(MainActivity.this,
                            mEtName.getText().toString().trim()+
                                    " "+mEtLastName.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    public void sugerenciaDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_sugestions, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEtName = view.findViewById(R.id.et_name);
        final Button mBtCancel = view.findViewById(R.id.bt_cancel);
        final Button mBtAccept = view.findViewById(R.id.bt_accept);
        mEtName.requestFocus();
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
                    mEtName.setError("Introduce una sugerencia");
                    mEtName.requestFocus();
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:gratimetro@gmail.com")); // only email apps should handle this
                    //intent.putExtra(Intent.EXTRA_EMAIL, "ghoss@phl.com.ve");
                    String subject = "Sugerencias de Gratímetro";
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    String body = "Saludos,\n Por medio de la presente les hago llegar la siguiente sugerencia:" +
                            "\n\n" + mEtName.getText().toString()+
                            "\n\nCordialmente," +
                            "\n\n" +StorageUtils.getUserName(MainActivity.this) +
                            "\n\nMensaje generado desde el Gratímetro.";
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    public void configDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_configuration, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        SwitchCompat swSound = view.findViewById(R.id.sw_s);
        SwitchCompat swVibrate = view.findViewById(R.id.sw_v);
        SwitchCompat swNotification = view.findViewById(R.id.sw_n);
        swSound.setChecked(StorageUtils.getSound(MainActivity.this));
        swVibrate.setChecked(StorageUtils.getVibration(MainActivity.this));
        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StorageUtils.setSound(MainActivity.this,isChecked);
            }
        });
        swVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StorageUtils.setVibration(MainActivity.this,isChecked);
            }
        });
        swNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StorageUtils.setNotification(MainActivity.this,isChecked);
            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

    }


    public void contactDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_contact, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        LinearLayout llWh = view.findViewById(R.id.ll_wh);
        LinearLayout llCl = view.findViewById(R.id.ll_call);
        LinearLayout llEm = view.findViewById(R.id.ll_email);

        final AlertDialog dialog = alertDialogBuilder.create();
        llWh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://wa.me/+584123500600";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                dialog.dismiss();
            }
        });

        llCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+584123500600"));
                startActivity(intent);
                dialog.dismiss();
            }
        });

        llEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:gratimetro@gmail.com")); // only email apps should handle this
                startActivity(intent);
            }
        });

        dialog.show();

    }


}
