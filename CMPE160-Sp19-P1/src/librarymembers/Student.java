package librarymembers;

import java.util.ArrayList;

import books.Book;

/**
 * is the blueprint for the members that are Students.
 * @author Ekin
 *
 */
public final class Student extends LibraryMember {
	/**
	 * is the Constructor of the Student class initializes the student object with
	 * the given id and determined limits for the student
	 * 
	 * @param id is the id number of the student
	 */
	public Student(int id) {
		super(id, 10, 20, "S");
	}

	/**
	 * returns the read or borrowed book' history of this member
	 */
	@Override
	public ArrayList<Book> getTheHistory() {
		return this.history;
	}

}