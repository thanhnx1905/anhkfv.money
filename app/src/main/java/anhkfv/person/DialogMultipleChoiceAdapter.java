package anhkfv.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhth.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import anhkfv.infomation.detail.Person;

public class DialogMultipleChoiceAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<Person> mItemList;

    public DialogMultipleChoiceAdapter(Context context, List<Person> itemList) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = itemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Person getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public List<Person> getCheckedItem() {
        List<Person> checkedItemList = new ArrayList<>();
        for (Person item : mItemList) {
            if (item.isCheck()) {
                checkedItemList.add(item);
            }
        }
        return checkedItemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.person, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Person item = getItem(position);
        holder.tvTitle.setText(item.getPersonName());
        holder.ivImage.setImageResource(R.drawable.ic_menu_camera);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setCheck(!item.isCheck());
                updateItemState(holder, item.isCheck());
            }
        });
        updateItemState(holder, item.isCheck());
        return convertView;
    }

    private void updateItemState(ViewHolder holder, boolean checked) {
        holder.root.setAlpha(checked ? 1 : 0.8f);
        holder.checkbox.setChecked(checked);
    }

    private static class ViewHolder {
        View root;
        TextView tvTitle;
        ImageView ivImage;
        CheckBox checkbox;

        ViewHolder(View view) {
            root = view;
            tvTitle = view.findViewById(R.id.text);
            ivImage = view.findViewById(R.id.image);
            checkbox = view.findViewById(R.id.checkbox);
        }
    }
}
