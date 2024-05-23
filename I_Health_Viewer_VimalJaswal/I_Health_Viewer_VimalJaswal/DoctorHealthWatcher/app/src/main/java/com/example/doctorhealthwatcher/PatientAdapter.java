package com.example.doctorhealthwatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {

    private static List<Patient> patientsList;
    private static OnItemClickListener listener;

    public PatientAdapter(List<Patient> patientsList){
        PatientAdapter.patientsList = patientsList;
    }

    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        return new PatientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
//        Patient currentPatient = getItem(position);
        holder.textViewUserName.setText(patientsList.get(position).getUserName());
        holder.textViewHeartRate.setText(String.valueOf(patientsList.get(position).getHeartRate()));
        holder.textViewEmail.setText(patientsList.get(position).getEmail());
    }

    public Patient getPatientAt(int position) {
        return patientsList.get(position);
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }



    static class PatientHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView textViewUserName;
        TextView textViewHeartRate;
        TextView textViewEmail;

        public PatientHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textViewUserName = (TextView) view.findViewById(R.id.text_view_username);
            textViewHeartRate = (TextView) view.findViewById(R.id.text_view_heartrate);
            textViewEmail = (TextView) view.findViewById(R.id.text_view_email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(patientsList.get(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Patient patient);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        PatientAdapter.listener = listener;
    }

}
