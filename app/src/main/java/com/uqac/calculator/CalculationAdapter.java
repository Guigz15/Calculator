package com.uqac.calculator;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalculationAdapter extends RecyclerView.Adapter<CalculationAdapter.CalculationViewHolder> {

    private Activity activity;
    private List<Calculation> calculations;

    public CalculationAdapter(Activity activity, List<Calculation> calculations) {
        this.activity = activity;
        this.calculations = calculations;
    }

    @NonNull
    @Override
    public CalculationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_history, parent, false);
        CalculationViewHolder calculationViewHolder = new CalculationViewHolder(view);

        calculationViewHolder.setOnClickListener((view1, position) -> {
            Intent intent = new Intent();
            intent.putExtra("result", calculations.get(position).getResult());
            activity.setResult(RESULT_OK, intent);
            activity.finish();
        });

        return calculationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalculationViewHolder holder, int position) {
        holder.operationTextView.setText(calculations.get(position).getOperation());
        holder.resultTextView.setText(calculations.get(position).getResult());
    }

    @Override
    public int getItemCount() {
        return calculations.size();
    }


    static class CalculationViewHolder extends RecyclerView.ViewHolder {

        private final TextView operationTextView;
        private final TextView resultTextView;

        public CalculationViewHolder(@NonNull View itemView) {
            super(itemView);
            operationTextView = itemView.findViewById(R.id.calculation_text);
            resultTextView = itemView.findViewById(R.id.result_text);

            itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getAdapterPosition()));
        }

        private CalculationViewHolder.ClickListener mClickListener;

        public interface ClickListener{
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(CalculationViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }
}
