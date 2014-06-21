package fr.utt.isi.nomp_mobile.tasks;

import android.os.AsyncTask;

public abstract class PauserTask extends AsyncTask<Integer, Void, Void> {

	@Override
	protected Void doInBackground(Integer... params) {
		try {
			// pause x milliseconds
			Thread.sleep(params[0]);
		} catch (InterruptedException e) {
			return null;
		}
		return null;
	}

	@Override
	protected abstract void onPostExecute(Void result);

}
