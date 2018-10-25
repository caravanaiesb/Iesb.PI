package br.pi.iesb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class EventRecycleViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomeEvent,txtDescDescEvent,txtAtracao;

    public EventRecycleViewHolder(@NonNull View itemView) {
        super(itemView);

        txtNomeEvent = (TextView) itemView.findViewById(R.id.txtNomeEvento);
        txtDescDescEvent = (TextView) itemView.findViewById(R.id.txtTipoEvento);
        txtAtracao = (TextView) itemView.findViewById(R.id.txtAtracaoPrincipal);


    }
}
