package br.pi.iesb;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class PassageiroRecycleView extends RecyclerView.ViewHolder {
    public TextView nomePassageiro,emailPassageiro;

    public PassageiroRecycleView( View itemView) {
        super(itemView);
        nomePassageiro = (TextView) itemView.findViewById(R.id.nomePassageiro);
        emailPassageiro = (TextView) itemView.findViewById(R.id.emailPassageiro);
    }
}
