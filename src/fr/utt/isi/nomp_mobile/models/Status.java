package fr.utt.isi.nomp_mobile.models;

public class Status {
	
	public static final int OPEN = 0;
	public static final int IN_PROGRESS = 1;
	public static final int END = 2;
	public static final int CLOSED = 3;
	
	public static final String[] LABELS = {"OPEN", "IN PROGRESS", "END", "CLOSED"};
	
	public static final String[] COLORS = {"#428bca", "#5cb85c", "#f0ad4e", "#d9534f"};

	private Status() {
	}

}
