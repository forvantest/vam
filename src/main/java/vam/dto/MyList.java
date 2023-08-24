package vam.dto;

import java.util.ArrayList;

public class MyList<E> extends ArrayList<E> {

	@Override
	public boolean add(E e) {
		if (!super.contains(e)) {
			super.add(e);
			return true;
		}
		return false;
	}
}
