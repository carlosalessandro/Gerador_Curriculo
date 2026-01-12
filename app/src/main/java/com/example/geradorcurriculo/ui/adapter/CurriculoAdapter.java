package com.example.geradorcurriculo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.geradorcurriculo.R;
import com.example.geradorcurriculo.model.Curriculo;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CurriculoAdapter extends BaseAdapter {

    private Context context;
    private List<Curriculo> curriculos;
    private SimpleDateFormat dateFormat;

    public CurriculoAdapter(Context context, List<Curriculo> curriculos) {
        this.context = context;
        this.curriculos = curriculos;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @Override
    public int getCount() {
        return curriculos.size();
    }

    @Override
    public Object getItem(int position) {
        return curriculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return curriculos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_curriculo, parent, false);
            holder = new ViewHolder();
            holder.textNome = convertView.findViewById(R.id.text_nome);
            holder.textEmail = convertView.findViewById(R.id.text_email);
            holder.textData = convertView.findViewById(R.id.text_data);
            holder.textAtsScore = convertView.findViewById(R.id.text_ats_score);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Curriculo curriculo = curriculos.get(position);
        
        holder.textNome.setText(curriculo.getNomeCompleto() != null ? curriculo.getNomeCompleto() : "Sem nome");
        holder.textEmail.setText(curriculo.getEmail() != null ? curriculo.getEmail() : "Sem e-mail");
        holder.textData.setText("Atualizado: " + dateFormat.format(curriculo.getDataAtualizacao()));
        
        if (curriculo.getAtsScore() > 0) {
            holder.textAtsScore.setText("ATS: " + curriculo.getAtsScore() + "/100");
            holder.textAtsScore.setVisibility(View.VISIBLE);
            
            if (curriculo.getAtsScore() >= 80) {
                holder.textAtsScore.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else if (curriculo.getAtsScore() >= 60) {
                holder.textAtsScore.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                holder.textAtsScore.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        } else {
            holder.textAtsScore.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView textNome;
        TextView textEmail;
        TextView textData;
        TextView textAtsScore;
    }
}
