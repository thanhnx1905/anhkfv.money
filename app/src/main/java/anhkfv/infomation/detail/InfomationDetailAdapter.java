package anhkfv.infomation.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anhth.myapplication.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import anhkfv.moneysum.PostData;
import anhkfv.processdata.DeleteData;

public class InfomationDetailAdapter extends RecyclerView.Adapter<InfomationDetailAdapter.MyViewHolder> {
    private final  static  String DATE_FORMAT ="yyyy-MM-dd";
    private final  static SimpleDateFormat dateFomat = new SimpleDateFormat(DATE_FORMAT);
    private List<InfomationDetail> infomation;
    private List<Person> persons;
    private ItemClickListener itemClickListener;
    View viewTemp;
    int positionTemp;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView date, personName, idMoney, money, info;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.textDate);
            personName = (TextView) view.findViewById(R.id.textPerson);
            idMoney = (TextView) view.findViewById(R.id.textIdMoney);
            money = (TextView) view.findViewById(R.id.textMoney);
            info = (TextView) view.findViewById(R.id.textInfo);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }

    public InfomationDetailAdapter(List<InfomationDetail> infomation, List<Person> persons) {
        this.infomation = infomation;
        this.persons = persons;
        //this.context = mContext;
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
        holder.idMoney.setText(detail.getApproval().equals("1") ? "Đã Xác Nhận" : "Chưa Xác Nhận");
        holder.money.setText(String.valueOf(detail.getMoney() + "K"));
        holder.info.setText(detail.getInfo().length() > 12 ? detail.getInfo().substring(0,12) : detail.getInfo());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                viewTemp = view;
                positionTemp = position;
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(view.getContext());
                boolean superUser = false;
                if( prefs.getInt("anhkfv", 0)== 1){
                    superUser = true;
                }
                if(isLongClick) {
                    if(position <= infomation.size() && infomation.size() > 0 && (superUser || infomation.get(position).getApproval().equals("0"))) {
                        //Toast.makeText(view.getContext(), "Long Click: " + infomation.get(position).getApproval(), Toast.LENGTH_SHORT).show();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        new DeleteData(viewTemp.getContext(), infomation.get(positionTemp).getKeyRandom()).execute();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Bạn có muốn xóa?").setPositiveButton("Có", dialogClickListener)
                                .setNegativeButton("Không", dialogClickListener).show();
                    }
                }
                else {
                    if(position <= infomation.size() && infomation.size() > 0 &&(infomation.get(position).getApproval().equals("0")||superUser)){
                        Intent intent = new Intent(view.getContext(), PostData.class);
                        intent.putExtra("persons", (Serializable) persons);
                        intent.putExtra("infomation", infomation.get(position));
                        view.getContext().startActivity(intent);
                    }else{
                        Toast.makeText(view.getContext(), "Không có quyền", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return infomation.size();
    }
}
