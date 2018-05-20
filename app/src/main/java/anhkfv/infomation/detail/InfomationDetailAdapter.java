package anhkfv.infomation.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anhth.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class InfomationDetailAdapter extends RecyclerView.Adapter<InfomationDetailAdapter.MyViewHolder> {
    private final  static  String DATE_FORMAT ="yyyy-MM-dd";
    private final  static SimpleDateFormat dateFomat = new SimpleDateFormat(DATE_FORMAT);
    private List<InfomationDetail> infomation;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, personName, idMoney, money;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.textDate);
            personName = (TextView) view.findViewById(R.id.textPerson);
            idMoney = (TextView) view.findViewById(R.id.textIdMoney);
            money = (TextView) view.findViewById(R.id.textMoney);
        }
    }

    public InfomationDetailAdapter(List<InfomationDetail> infomation) {
        this.infomation = infomation;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.infomation_detail_content, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InfomationDetail detail = infomation.get(position);
        holder.date.setText(dateFomat.format(detail.getDate()));
        holder.personName.setText(detail.getPersonName());
        holder.idMoney.setText(detail.getIdMoney());
        holder.money.setText(String.valueOf(detail.getMoney()));
    }

    @Override
    public int getItemCount() {
        return infomation.size();
    }
}
