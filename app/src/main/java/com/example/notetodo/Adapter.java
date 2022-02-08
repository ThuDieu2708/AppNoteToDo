package com.example.notetodo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {
    Context context;
    Activity activity;
    List<ModelNote> noteList;
    List<ModelNote> newList;

    public Adapter(Context context, Activity activity, List<ModelNote> noteList) {
        this.context = context;
        this.activity = activity;
        this.noteList = noteList;
        this.newList = new ArrayList<>(noteList);
    }

    @NonNull
    @Override
    // tạo đối tượng ViewHolder và bên trong chứa View hiển thị dữ liệu
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    // chuyển dữ liệu phần tử vào ViewHolder
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(noteList.get(position).getTitle());
        holder.content.setText(noteList.get(position).getContent());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateNoteActivity.class);

                intent.putExtra("id", noteList.get(position).getId());
                intent.putExtra("title", noteList.get(position).getTitle());
                intent.putExtra("content", noteList.get(position).getContent());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {   //cho biết số phần tử của dữ liệu
        return noteList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ModelNote> filterList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filterList.addAll(newList);
            }else {
                String sFilter = charSequence.toString().toLowerCase().trim();
                for (ModelNote item: newList) {
                    if(item.getTitle().toLowerCase().contains(sFilter)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteList.clear();
            noteList.addAll((List)filterResults.values);
            notifyDataSetChanged();   //Thông báo khi dữ liệu bị thay đổi ( chỉ dùng trong trường hợp cuối cùng )
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, content;
        RelativeLayout relativeLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textTitle);
            content = (TextView) itemView.findViewById(R.id.textContent);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.noteLayout);
        }
    }
    public List<ModelNote> getList(){
        return noteList;
    }

    // xóa item note
    public void removeItem(int position){
        noteList.remove(position);
        notifyItemRemoved(position);
    }
    //khôi phục
    public void restoreItem(ModelNote noteItem , int position){
        noteList.add(position, noteItem);
        notifyItemInserted(position);
    }
}
