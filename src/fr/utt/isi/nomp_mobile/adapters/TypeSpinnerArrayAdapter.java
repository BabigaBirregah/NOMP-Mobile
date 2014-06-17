package fr.utt.isi.nomp_mobile.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import fr.utt.isi.nomp_mobile.models.Type;

public class TypeSpinnerArrayAdapter extends ArrayAdapter<Type> {

	private List<?> objects;

	public TypeSpinnerArrayAdapter(Context context, int resource,
			List<?> objects) {
		super(context, resource);
		this.objects = objects;
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Type getItem(int position) {
		return (Type) objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ((Type) objects.get(position)).get_id();
	}

	@Override
	public int getPosition(Type type) {
		return objects.indexOf(type);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, null);
		}

		CheckedTextView textView = (CheckedTextView) view
				.findViewById(android.R.id.text1);
		textView.setText(((Type) objects.get(position)).getName());

		return view;
	}

}
