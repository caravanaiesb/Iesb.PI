package br.pi.iesb;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvent> {

    private List<Evento> dados;

    public EventAdapter(List<Evento> dados){
        this.dados= dados;
    }


    @NonNull
    @Override
    public EventAdapter.ViewHolderEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.linha_event,parent,false);

        ViewHolderEvent holderEvent = new ViewHolderEvent(view);

        return holderEvent;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolderEvent holder, int position) {
        if((dados!=null) && (dados.size()>0)) {
            if(holder!=null){
            holder.txtNome.setText("NOMAI");
            holder.txtDescricao.setText("DESC");
            holder.txtAtracao.setText("Atracao");
        }}


    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderEvent extends RecyclerView.ViewHolder{

        public TextView txtNome;
        public TextView txtDescricao;
        public TextView txtAtracao;






        public ViewHolderEvent(@NonNull View itemView) {
            super(itemView);

            txtNome = (TextView)itemView.findViewById(R.id.txtNomeEvento);
            txtDescricao = (TextView)itemView.findViewById(R.id.txtTipoEvento);
            txtAtracao = (TextView)itemView.findViewById(R.id.txtAtracaoPrincipal);

        }
    }

    //public ImageView imgListEvent;

          //  imgListEvent = itemView.findViewById(R.id.imgListEvent);
            //txtListEvent = itemView.findViewById(R.id.txtNomeEvento);
            //txtListDescEvent = itemView.findViewById(R.id.txtTipoEvento);
            //txtListAtracao = itemView.findViewById(R.id.txtAtracaoPrincipal);


}
