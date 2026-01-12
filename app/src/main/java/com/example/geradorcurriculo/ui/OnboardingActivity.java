package com.example.geradorcurriculo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.geradorcurriculo.MainActivity;
import com.example.geradorcurriculo.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button buttonSkip;
    private Button buttonGetStarted;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initViews();
        setupViewPager();
        setupClickListeners();
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        buttonSkip = findViewById(R.id.btn_skip);
        buttonGetStarted = findViewById(R.id.btn_get_started);
    }

    private void setupViewPager() {
        List<OnboardingItem> items = createOnboardingItems();
        adapter = new OnboardingAdapter(items);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Não precisa de texto nos tabs, apenas os indicadores
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateButtonVisibility(position);
            }
        });

        updateButtonVisibility(0);
    }

    private List<OnboardingItem> createOnboardingItems() {
        List<OnboardingItem> items = new ArrayList<>();

        items.add(new OnboardingItem(
            R.drawable.ic_create_cv,
            "Crie Currículos Profissionais",
            "Elabore currículos impactantes com nossa tecnologia de análise ATS avançada",
            "Destaque-se no mercado com currículos otimizados para sistemas de recrutamento"
        ));

        items.add(new OnboardingItem(
            R.drawable.ic_import_file,
            "Importe Seus Documentos",
            "Importe currículos existentes de PDF ou DOCX com extração automática de dados",
            "Economize tempo digitando - nossa IA reconhece e organiza suas informações"
        ));

        items.add(new OnboardingItem(
            R.drawable.ic_ats_analysis,
            "Análise ATS Inteligente",
            "Receba pontuação 0-100 e sugestões personalizadas para melhorar seu currículo",
            "Baseado em milhares de vagas reais e tendências do mercado de trabalho"
        ));

        items.add(new OnboardingItem(
            R.drawable.ic_templates,
            "Templates Profissionais",
            "Mais de 50 templates modernos para diferentes áreas e níveis de experiência",
            "Templates criados por especialistas em recrutamento e design"
        ));

        items.add(new OnboardingItem(
            R.drawable.ic_premium,
            "CV Pro Premium",
            "Desbloqueie recursos ilimitados e potencialize sua carreira",
            "Análise ATS avançada, templates exclusivos e suporte prioritário"
        ));

        return items;
    }

    private void setupClickListeners() {
        buttonSkip.setOnClickListener(v -> navigateToMain());
        buttonGetStarted.setOnClickListener(v -> navigateToMain());
    }

    private void updateButtonVisibility(int position) {
        if (position == adapter.getItemCount() - 1) {
            buttonSkip.setVisibility(View.GONE);
            buttonGetStarted.setText("Começar Agora");
            buttonGetStarted.setBackgroundColor(getResources().getColor(R.color.primary_color));
        } else {
            buttonSkip.setVisibility(View.VISIBLE);
            buttonGetStarted.setText("Próximo");
            buttonGetStarted.setBackgroundColor(getResources().getColor(R.color.accent_color));
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private static class OnboardingItem {
        private final int imageRes;
        private final String title;
        private final String description;
        private final String highlight;

        public OnboardingItem(int imageRes, String title, String description, String highlight) {
            this.imageRes = imageRes;
            this.title = title;
            this.description = description;
            this.highlight = highlight;
        }

        public int getImageRes() { return imageRes; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getHighlight() { return highlight; }
    }

    private static class OnboardingAdapter extends androidx.viewpager2.adapter.RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

        private final List<OnboardingItem> items;

        public OnboardingAdapter(List<OnboardingItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_onboarding, parent, false);
            return new OnboardingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private static class OnboardingViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            private final ImageView imageView;
            private final TextView titleView;
            private final TextView descriptionView;
            private final TextView highlightView;

            public OnboardingViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image_onboarding);
                titleView = itemView.findViewById(R.id.text_title);
                descriptionView = itemView.findViewById(R.id.text_description);
                highlightView = itemView.findViewById(R.id.text_highlight);
            }

            public void bind(OnboardingItem item) {
                imageView.setImageResource(item.getImageRes());
                titleView.setText(item.getTitle());
                descriptionView.setText(item.getDescription());
                highlightView.setText(item.getHighlight());
            }
        }
    }
}
