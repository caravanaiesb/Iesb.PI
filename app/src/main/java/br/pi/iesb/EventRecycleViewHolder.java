package br.pi.iesb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EventRecycleViewHolder extends RecyclerView.ViewHolder {


    public TextView txtNomeEvent,txtDescEvent,txtAtracao,dataEvent;
    public ImageView circleImg;
    public EventRecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        circleImg = (ImageView) itemView.findViewById(R.id.circleImage);
        txtNomeEvent = (TextView) itemView.findViewById(R.id.txtListEvent);
        txtDescEvent = (TextView) itemView.findViewById(R.id.txtListDescEvent);
        txtAtracao = (TextView) itemView.findViewById(R.id.txtListAtracao);
        dataEvent = (TextView) itemView.findViewById(R.id.dataEvent);



    }
}
