package librarymembers;

import java.util.ArrayList;
import books.*;

/**
 * Constitutes a blueprint for the members of the library.
 * 
 * @author Ekin
 *
 */
public abstract class LibraryMember {

	/**
	 * is the id number of the library member.
	 */
	protected final int id;
	/**
	 * is the maximum number of books that a member is allowed to take.
	 */
	protected final int maxNumberOfBooks;
	/**
	 * is the maximum time duration that a member can enjoy a borrowed book.
	 */
	protected final int timeLimit;
	/**
	 * is the type of member whether "S" student or "A" academic.
	 */
	protected final String memberType;
	/**
	 * is the number of currently borrowed books
	 */
	protected int numberOfBooksBorrowed;
	/**
	 * is the list of books either borrowed or read for a specific member
	 */
	protected ArrayList<Book> history;

	/**
	 * is the Constructor of the LibraryMember Class. Initializes the fields of the
	 * LibraryMember by the given parameters.
	 * 
	 * @param id               is the id number of the member
	 * @param maxNumberOfBooks is the maximum number of the books that member can
	 *                         have simultaneously
	 * @param timeLimit        is the maximum allowed time between the borrow and
	 *                         the return of a book
	 * @param memberType       is the type of the member namely "S" or "A"
	 */
	public LibraryMember(int id, int maxNumberOfBooks, int timeLimit, String memberType) {
		this.id = id;
		this.maxNumberOfBooks = maxNumberOfBooks;
		this.timeLimit = timeLimit;
		this.memberType = memberType;
		this.numberOfBooksBorrowed = 0;
		this.history = new ArrayList<Book>();
	}

	/**
	 * adds the given book to this member's read or borrowed book history. if it is
	 * already included in the history list, the method gets terminated with no
	 * execution. otherwise book is added to the {@link #history}
	 * 
	 * @param book is the book that being added to the this member's history
	 */
	public void addToHistory(Book book) {
		boolean isAlreadyIncluded = false;

		for (Book booq : history) {

			if (booq.equals(book)) {
				isAlreadyIncluded = true;
			}
		}
		if (!isAlreadyIncluded) {
			history.add(book);
		}

	}

	/**
	 * Overrides the equals method of Object Class Checks if the given member has
	 * the same id number with this LibraryMember
	 * 
	 * @param object is the member that will be compared with this LibraryMember
	 */
	@Override
	public boolean equals(Object o) {
		return ((LibraryMember) o).getId() == this.id;
	}

	/**
	 * getter method of the {@link #getNumberOfBooksBorrowed()}
	 * 
	 * @return the number of books borrowed by this LibraryMember
	 */
	public int getNumberOfBooksBorrowed() {
		return numberOfBooksBorrowed;
	}

	/**
	 * increments the number of books borrowed by 1, by this member.
	 * 
	 * @see numberOfBooksBorrowed
	 */
	public void incrementNumberOfBooksBorrowed() {
		this.numberOfBooksBorrowed++;
	}

	/**
	 * decrements the number of the books borrowed by 1, by this member.
	 * 
	 * @see numberOfBooksBorrowed
	 */
	public void decrementNumberOfBooksBorrowed() {
		this.numberOfBooksBorrowed--;
	}

	/**
	 * is the getter method of the id number of this member
	 * 
	 * @return {@link #id}
	 */
	public int getId() {
		return id;
	}

	/**
	 * is the getter method of the maximum numbers of books of this member
	 * 
	 * @return {@link #maxNumberOfBooks}
	 */
	public int getMaxNumberOfBooks() {
		return maxNumberOfBooks;
	}

	/**
	 * is the getter method of the time limit of this member
	 * 
	 * @return {@link #timeLimit}
	 */
	public int getTimeLimit() {
		return timeLimit;
	}

	/**
	 * is the getter method of the member type
	 * 
	 * @return {@link #getMemberType()}
	 */
	public String getMemberType() {
		return memberType;
	}

	/**
	 * is the Abstract method that returns the book history of this member
	 * 
	 * @return {@link #history}
	 */
	public abstract ArrayList<Book> getTheHistory();

}