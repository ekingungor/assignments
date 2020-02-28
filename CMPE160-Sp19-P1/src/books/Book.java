package books;

import librarymembers.LibraryMember;

/**
 * Constitutes a blueprint for the books in the library.
 * 
 * @author Ekin
 *
 */
public abstract class Book {

	/**
	 * is the id of this book 
	 */
	private final int bookID;
	/**
	 * is the type of this book either "P" printed or "H" handwritten
	 */
	private final String bookType;
	/**
	 *  shows whether this book is taken or not
	 */
	protected boolean isTaken;
	/**
	 * shows which member has this book
	 */
	protected LibraryMember whoHas;

	/**
	 * is the Constructor method. Initializes the book with given parameters while
	 * assigns {@link #isTaken} to false and {@link #whoHas} to null.
	 * 
	 * 
	 * @param bookId   represents the id of a specific book
	 * @param bookType represents the type of the book "P" or "H"
	 */
	public Book(int bookId, String bookType) {
		this.bookID = bookId;
		this.bookType = bookType;
		this.whoHas = null;
		this.isTaken = false;

	}

	/**
	 * Returns a book to the library.
	 * 
	 * Book can be either borrowed or read in the library.
	 * 
	 * @param member is any person who returns the book
	 */
	public abstract void returnBook(LibraryMember member);

	@Override
	/**
	 * Overrides the equals method in the Object Class.
	 * 
	 * @param o is the object that will be checked if it is equal or not
	 * @return whether two objects have the same id or not
	 */
	public boolean equals(Object o) {
		return (((Book) o).getBookID() == this.bookID);

	}

	/**
	 * Sets the value of the field whoHas to a given member.
	 * 
	 * @param whoHas is the member who gets the book
	 */
	public void setWhoHas(LibraryMember whoHas) {
		this.whoHas = whoHas;
	}

	/**
	 * Sets the field isTaken according to the current action.
	 * 
	 * @param isTaken the value that the isTaken field will be assigned to.
	 */
	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}

	/**
	 * Returns the id of the book
	 * 
	 * @return the id number of the book
	 */
	public int getBookID() {
		return bookID;
	}

	/**
	 * Returns the book's type.
	 * 
	 * @return whether "P" or "H" which are possible book types
	 */
	public String getBookType() {
		return bookType;
	}

	/**
	 * Returns whether book is taken or not
	 * 
	 * @return whether book is taken or not
	 */
	public boolean isTaken() {
		return isTaken;
	}

	/**
	 * Returns the current owner of the book
	 * 
	 * @return the member who currently has the book
	 */
	public LibraryMember getWhoHas() {
		return whoHas;
	}


}
