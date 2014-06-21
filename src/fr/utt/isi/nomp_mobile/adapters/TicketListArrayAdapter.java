package fr.utt.isi.nomp_mobile.adapters;

import java.util.List;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
import fr.utt.isi.nomp_mobile.models.Status;
import fr.utt.isi.nomp_mobile.models.Ticket;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TicketListArrayAdapter extends ArrayAdapter<Ticket> {
	
	public static final String TAG = "TicketListArrayAdapter";
	
	private Context context;
	
	private int resource;
	
	private List<?> objects;

	public TicketListArrayAdapter(Context context, int resource,
			List<?> objects) {
		super(context, resource);
		this.context = context;
		this.resource = resource;
		this.objects = objects;
	}
	
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Ticket getItem(int position) {
		return (Ticket) objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ((Ticket) objects.get(position)).get_id();
	}

	@Override
	public int getPosition(Ticket ticket) {
		return objects.indexOf(ticket);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
		}
		
		Ticket ticket = (Ticket) objects.get(position);
		
		if (ticket != null) {
			// ticket status
			int status = ticket.getStatut();
			String statusLabel = Status.LABELS[status];
			String statusColor = Status.COLORS[status];
			((TextView) convertView.findViewById(R.id.ticket_status)).setText(statusLabel);
			((GradientDrawable) convertView.findViewById(R.id.ticket_status).getBackground()).setColor(Color.parseColor(statusColor));
			
			// ticket name
			String name = ticket.getName();
			((TextView) convertView.findViewById(R.id.ticket_name)).setText(name);
			
			// ticket creation date
			String creationDate = ticket.getCreationDate();
			((TextView) convertView.findViewById(R.id.ticket_creation_date)).setText(creationDate);
			
			// ticket classification
			Classification childClassification = (Classification) (new Classification(context).retrieve(ticket.getClassification()));
			String classificationLabel = childClassification.getName();
			if (!childClassification.isParent() && childClassification.getParent() != null) {
				Classification parentClassification = (Classification) (new Classification(context).retrieve(childClassification.getParent()));
				classificationLabel = parentClassification.getName() + " > " + classificationLabel;
			}
			((TextView) convertView.findViewById(R.id.ticket_classification)).setText(classificationLabel);
			
			// ticket target actor
			ActorType childTarget = (ActorType) (new ActorType(context).retrieve(ticket.getTargetActorType()));
			String targetLabel = childTarget.getName();
			if (!childTarget.isParent() && childTarget.getParent() != null) {
				ActorType parentTarget = (ActorType) (new ActorType(context).retrieve(childTarget.getParent()));
				targetLabel = parentTarget.getName() + " > " + targetLabel;
			}
			((TextView) convertView.findViewById(R.id.ticket_target)).setText(targetLabel);
		}
		
		return convertView;
	}

}
