package br.pi.iesb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AmigoRecyckeVuew extends RecyclerView.ViewHolder {
    public TextView nomeUsuario,idadeUsuario;

    public AmigoRecyckeVuew( View itemView) {
        super(itemView);
        nomeUsuario = (TextView) itemView.findViewById(R.id.nomeUsuario);
        idadeUsuario = (TextView) itemView.findViewById(R.id.idadeUsuario);
    }
}
