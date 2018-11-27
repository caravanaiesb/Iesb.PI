package br.pi.iesb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecomendacaoRecycleView extends RecyclerView.ViewHolder {
    public TextView nomeUsuarioRec,idadeUsuarioRec;

    public RecomendacaoRecycleView( View itemView) {
        super(itemView);
        nomeUsuarioRec = (TextView) itemView.findViewById(R.id.nomeUsuarioRec);
        idadeUsuarioRec = (TextView) itemView.findViewById(R.id.idadeUsuarioRec);
    }
}
