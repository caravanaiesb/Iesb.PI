package br.pi.iesb;

import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvento> {

    private List<Evento> dados;


    public EventAdapter(List<Evento>    dados){
        this.dados=dados;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolderEvento onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.linha_event,viewGroup,false);

        ViewHolderEvento viewHolderEvento = new ViewHolderEvento(view);

        return viewHolderEvento;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolderEvento viewHolder, int i) {

        if((dados!=null) && (dados.size() >0)) {
            Evento evento = dados.get(i);

            viewHolder.txtListEvent.setText(evento.txtNomeEvento);
            viewHolder.txtListDescEvent.setText(evento.txtTipoEvento);
            viewHolder.txtListAtracao.setText(evento.txtAtracaoPrincipal);
        }
    }

    @Override
    public int getItemCount() {

        return 1;
    }

    public class ViewHolderEvento extends RecyclerView.ViewHolder{

        public TextView txtListEvent,txtListDescEvent,txtListAtracao;
        public ImageView imgListEvent;

        public ViewHolderEvento(@NonNull View itemView) {
            super(itemView);

            txtListEvent = itemView.findViewById(R.id.txtNomeEvento);
            txtListDescEvent = itemView.findViewById(R.id.txtTipoEvento);
            txtListAtracao = itemView.findViewById(R.id.txtAtracaoPrincipal);

        }




    }
}
