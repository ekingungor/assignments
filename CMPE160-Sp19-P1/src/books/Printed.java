package books;

import interfaces.*;
import librarymembers.*;

/**
 * Constitutes a blueprint to a specific type of book that is Printed.
 * 
 * @author Ekin
 *
 */
public final class Printed extends Book implements ReadInLibrary, Borrow {

	/**
	 * is the dead line of the book if it is borrowed
	 */
	private int deadLine;
	/**
	 * shows whether the book's dead line is extended or not
	 */
	private boolean isExtended;
	/**
	 * shows whether this book is currently borrowed or not
	 */
	private boolean isBorrowed;

	/**
	 * is the Constructor of the Printed class. In addition to the standard
	 * initialization of the book construction, for printed books, deadLine field
	 * gets the value of 0 which is meaningless and whoHas field gets the value of
	 * null.
	 * 
	 * @param bookId the id number of the book that is being initialized.
	 */
	public Printed(int bookId) {
		super(bookId, "P");
		this.deadLine = 0;
		this.isExtended = false;
	}

	/**
	 * executes the action of borrowing a book from the library.
	 * {@link Book#isTaken} gets the value true, {@link Book#whoHas} is assigned to
	 * the member who borrows this book, deadLine gets the value of the summation of
	 * the time limit of the specific member and the current
	 * tick,{@link #isBorrowed} gets assigned to true.Because the book is
	 * borrowed,member's number of books currently borrowed is incremented by 1, by
	 * the {@link LibraryMember#addToHistory(Book)}, and the book is added to the
	 * history by the {@link LibraryMember#addToHistory(Book)}
	 * 
	 * @param member is the member borrowing the book
	 * @param tick   is the time unit that this execution is occurring
	 * @see LibraryMember#incrementNumberOfBooksBorrowed()
	 * @see LibraryMember#addToHistory(Book)
	 */
	@Override
	public void borrowBook(LibraryMember member, int tick) {
		this.isTaken = true;
		this.whoHas = member;
		this.deadLine = member.getTimeLimit() + tick;
		this.isBorrowed = true;
		member.incrementNumberOfBooksBorrowed();
		member.addToHistory(this);
	}

	/**
	 * extends the given Book object's deadline. isExtended field gets the value of
	 * true and the deadLine is incremented by the value of the member's time limit.
	 * 
	 * @param member is the member extends the deadLine of the book that she
	 *               borrowed
	 * @param tick   is the unit time that the execution occurring.
	 * @see LibraryMember#getTimeLimit()
	 */
	@Override
	public void extend(LibraryMember member, int tick) {
		this.isExtended = true;
		this.deadLine += member.getTimeLimit();

	}

	/**
	 * executes the reading a book in the library. isTaken gets the value of true
	 * and whoHas field is assigned to the member that took the book to
	 * read.Additionaly the book is added to the history list of the member by the
	 * addToHistory method.
	 * 
	 * @param member is the member that takes the book to read
	 * @see LibraryMember#addToHistory(Book)
	 */
	@Override
	public void readBook(LibraryMember member) {
		this.isTaken = true;
		this.whoHas = member;
		member.addToHistory(this);
	}

	/**
	 * executes the returning of a either borrowed or just a read book in the
	 * library. if book was borrowed than the changes that made in the
	 * {@link #borrowBook(LibraryMember,int)} are changed back if the book was read
	 * in the library then the changes made in {@link #readBook(LibraryMember)} are
	 * reversed.
	 * 
	 * @param member is the member that brought back the book
	 */
	@Override
	public void returnBook(LibraryMember member) {
		if (this.isBorrowed) {
			this.isTaken = false;
			this.whoHas = null;
			this.isExtended = false;
			this.deadLine = 0;
			this.isBorrowed = false;
			member.decrementNumberOfBooksBorrowed();
		} else {
			this.isTaken = false;
			this.whoHas = null;
		}

	}

	/**
	 * returns whether the book is borrowed or not
	 * 
	 * @return whether book is borrowed or not
	 */
	public boolean isBorrowed() {
		return this.isBorrowed;
	}

	/**
	 * returns the {@link #deadLine} of the book
	 * 
	 * @return {@link #deadLine} of the book
	 */
	public int getDeadLine() {
		return deadLine;
	}

	/**
	 * returns whether the book is extended or not
	 * 
	 * @return if this book is extended or not
	 * 
	 */
	public boolean isExtended() {
		return isExtended;
	}

}