package librarymembers;

import java.util.ArrayList;

import books.Book;

/**
 * is the blueprint for the members that are Academic
 * @author Ekin
 *
 */
public final class Academic extends LibraryMember {
	/**
	 * is the Constructor of the Academic class. Initializes the Academic object
	 * with given id number and determined limitations for the academic member
	 * 
	 * @param id
	 */
	public Academic(int id) {
		super(id, 20, 50, "A");
	}

	/**
	 * returns the read or borrowed books' history of this member
	 */
	@Override
	public ArrayList<Book> getTheHistory() {
		return this.history;
	}

}