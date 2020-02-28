package books;

import interfaces.ReadInLibrary;
import librarymembers.LibraryMember;

/**
 * Represents the specific type of book that is handwritten.
 *
 * @author Ekin
 *
 */
public final class Handwritten extends Book implements ReadInLibrary {
	/**
	 * is the Constructor method. Initializes the book with given id and always with
	 * the bookType "H"
	 * 
	 * @param bookId is the id that is given for a book to be added
	 */
	public Handwritten(int bookId) {
		super(bookId, "H");
	}

	/**
	 * assigns a book to a member to read in library. Assigns fields of book such
	 * that value of isTaken is assigned to true and whoHas is assigned to member.
	 * Additionally adds this book to the member's book history.
	 * 
	 * @param member is the Student or Academic who takes the book
	 */
	@Override
	public void readBook(LibraryMember member) {
		isTaken = true;
		whoHas = member;
		member.addToHistory(this);
	}

	@Override
	/**
	 * Returns a book back to the library. Assigns fields of book such that isTaken
	 * gets assigned to false and whoHas gets assigned to null.
	 * 
	 * @param member is the Student or Academic that returns the book
	 */
	public void returnBook(LibraryMember member) {
		isTaken = false;
		whoHas = null;
	}

}
