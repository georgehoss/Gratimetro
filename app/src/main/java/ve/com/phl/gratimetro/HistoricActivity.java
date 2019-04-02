package ve.com.phl.gratimetro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import ve.com.phl.gratimetro.Adapter.GratitudAdapter;
import ve.com.phl.gratimetro.Model.Gratitude;
import ve.com.phl.gratimetro.Utils.Utils;

public class HistoricActivity extends AppCompatActivity {
    private GratitudAdapter mAdapter;
    private RealmResults<Gratitude> realmResults;
    private Realm realm;
    private String date="";
    private int pos=0;
    @BindView(R.id.sp_items) Spinner mSpItems;
    @BindView(R.id.tv_date) TextView mTvDate;
    @BindView(R.id.tv_no_records) TextView mTvNoRecords;
    @BindView(R.id.ll_header) LinearLayout mLlHeader;


    @OnClick(R.id.tv_date) void onDate(){
        showDatePickerDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        ButterKnife.bind(this);
        Realm.init(this);
        date = Utils.getDateString();
        realm = Realm.getDefaultInstance();
        realmResults = realm.where(Gratitude.class).equalTo("date",date).findAll();
        RecyclerView recyclerView = findViewById(R.id.rv);
        if (realmResults!=null && realmResults.size()>0)
            mTvNoRecords.setVisibility(View.GONE);
        mAdapter = new GratitudAdapter(realmResults, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mSpItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
               search(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTvDate.setText(Utils.getDateString());
        mLlHeader.requestFocus();
    }

    private void search (int pos){
      if (pos!=0){
          realmResults = realm.where(Gratitude.class).equalTo("date",date)
                  .equalTo("type",pos-1).findAll();
      }
      else
      {
          realmResults = realm.where(Gratitude.class).equalTo("date",date).findAll();
      }


        if (realmResults!=null && realmResults.size()>0)
            mTvNoRecords.setVisibility(View.GONE);
        else
            mTvNoRecords.setVisibility(View.VISIBLE);


      mAdapter.setGratitudes(realmResults);
      mAdapter.notifyDataSetChanged();

    }


    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                String mes = String.valueOf(month+1);
                String dia = String.valueOf(day);
                String ano = String.valueOf(year);
                if (mes.length()==1)
                    mes = "0"+mes;

                if (dia.length()==1)
                    dia = "0"+dia;

                date= dia + "/" + mes + "/" + ano;
                mTvDate.setText(date);
                search(pos);
            }
        });
        datePickerFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }
}
