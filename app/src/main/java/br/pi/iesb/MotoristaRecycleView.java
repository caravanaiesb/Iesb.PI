package br.pi.iesb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MotoristaRecycleView extends RecyclerView.ViewHolder {


    public TextView txtNomeMotorista,veiculoMotorista,txtAtracao,txtVagasDiponiveis;
    public ImageView circleImageMotorista;
    public MotoristaRecycleView(View itemView) {
        super(itemView);
        circleImageMotorista = (ImageView) itemView.findViewById(R.id.circleImageMotorista);
        txtNomeMotorista = (TextView) itemView.findViewById(R.id.txtNomeMotorista);
        veiculoMotorista = (TextView) itemView.findViewById(R.id.veiculoMotorista);
        txtVagasDiponiveis = (TextView) itemView.findViewById(R.id.txtVagasDiponiveis);
        txtAtracao = (TextView) itemView.findViewById(R.id.txtListAtracao);

    }
}
