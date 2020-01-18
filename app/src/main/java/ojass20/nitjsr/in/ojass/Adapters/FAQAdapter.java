package ojass20.nitjsr.in.ojass.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

import ojass20.nitjsr.in.ojass.Models.FaqModel;
import ojass20.nitjsr.in.ojass.Models.NotificationModal;
import ojass20.nitjsr.in.ojass.Models.TitleChild;
import ojass20.nitjsr.in.ojass.Models.TitleParent;
import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.TitleChildViewHolder;
import ojass20.nitjsr.in.ojass.Utils.TitleParentViewHolder;
public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {

    ArrayList<FaqModel> datalist;
    Context context;

    public FAQAdapter(Context context, ArrayList<FaqModel> datalist) {
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_notif,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.header.setText(datalist.get(position).getQues());
        holder.body.setText(datalist.get(position).getAns());
        holder.root.getBackground().setAlpha(50);

        boolean isExpanded = datalist.get(position).isExplandable();
        holder.body.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        TextView header,body;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            body = itemView.findViewById(R.id.body);
            root = itemView.findViewById(R.id.root);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FaqModel data = datalist.get(getAdapterPosition());
                    data.setExplandable(!data.isExplandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}