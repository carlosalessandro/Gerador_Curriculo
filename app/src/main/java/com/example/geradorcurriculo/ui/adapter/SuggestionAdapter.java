package com.example.geradorcurriculo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geradorcurriculo.R;
import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    private Context context;
    private List<String> suggestions;

    public SuggestionAdapter(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        String suggestion = suggestions.get(position);
        holder.textSuggestion.setText(suggestion);
    }

    @Override
    public int getItemCount() {
        return suggestions != null ? suggestions.size() : 0;
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        ImageView iconSuggestion;
        TextView textSuggestion;

        SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            iconSuggestion = itemView.findViewById(R.id.icon_suggestion);
            textSuggestion = itemView.findViewById(R.id.text_suggestion);
        }
    }
}
