package ve.com.phl.gratimetro.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import ve.com.phl.gratimetro.Model.Gratitude;
import ve.com.phl.gratimetro.R;
import ve.com.phl.gratimetro.Utils.StorageUtils;

public class GratitudAdapter extends RecyclerView.Adapter<GratitudAdapter.GratitudViewHolder> {
    RealmResults<Gratitude> gratitudes;
    Context context;



    public GratitudAdapter(RealmResults<Gratitude> gratitudes, Context context) {
        this.gratitudes = gratitudes;
        this.context = context;
    }

    @NonNull
    @Override
    public GratitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_gratitudes,parent,false);
        return new GratitudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GratitudViewHolder holder, int i) {
        if (gratitudes!=null && gratitudes.size()>0){
            Gratitude gratitude = gratitudes.get(i);
            if (gratitude!=null) {
                if (gratitude.getBody() != null)
                    holder.mTvBody.setText(gratitude.getBody());
                if (gratitude.getDate() != null)
                    holder.mTvDate.setText(gratitude.getDate());
                if (gratitude.getType()==Gratitude.GOD)
                    holder.mLlBg.setBackground(ContextCompat.getDrawable(context,R.drawable.god));
                else
                if (gratitude.getType()==Gratitude.ENVIROMENT)
                    holder.mLlBg.setBackground(ContextCompat.getDrawable(context,R.drawable.enviroment));
                else
                    holder.mLlBg.setBackground(ContextCompat.getDrawable(context,R.drawable.self));

                if(StorageUtils.getUserName(context)!=null)
                    holder.mTvAuthor.setText(StorageUtils.getUserName(context));

            }
        }
        else {
            holder.mTvBody.setText(context.getString(R.string.no_records));
        }
    }

    @Override
    public int getItemCount() {
        if (gratitudes!=null)
        return gratitudes.size();
        else
            return 0;
    }

    public RealmResults<Gratitude> getGratitudes() {
        return gratitudes;
    }

    public void setGratitudes(RealmResults<Gratitude> gratitudes) {
        this.gratitudes = gratitudes;
    }

    class GratitudViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date) TextView mTvDate;
        @BindView(R.id.ll_bg) RelativeLayout mLlBg;
        @BindView(R.id.tv_body) TextView mTvBody;
        @BindView(R.id.tv_author) TextView mTvAuthor;
        public GratitudViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
