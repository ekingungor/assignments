package library;

import java.util.ArrayList;
import java.util.Scanner;

import books.*;
import librarymembers.*;
import interfaces.*;

/**
 * Validates and executes given commands from the LibrarySimulator, additionally
 * holds the books and members, like a library does.
 * 
 * @author Ekin
 *
 */
public class Library {

	/**
	 * A Scanner object that takes the input from the user's input file
	 */
	private Scanner scanner;
	/**
	 * A list that holds all the books in this library.
	 */
	private final Book[] bookList;
	/**
	 * A list that holds all the members in this library.
	 */
	private final LibraryMember[] memberList;
	/**
	 * The total fee earned by the library from late returns of the borrowed books
	 */
	private int totalFee;
	/**
	 * The id that will be given to the next added member
	 */
	private int nextMemberId = 1;
	/**
	 * The id that will be given to the next added book
	 */
	private int nextBookId = 1;
	/**
	 * A variable that gets pointed to the book object that the input file declares
	 * an action with
	 */
	Book book;
	/**
	 * A variable that gets pointed to the book object that the input file declares
	 * an action with
	 */
	LibraryMember member;

	/**
	 * is the Constructor of the Library Class.Additionally assigns total fee earned
	 * by the library to the value of 0
	 * 
	 * @param scanner is the Scanner object that points to the input file
	 */
	public Library(Scanner scanner) {
		this.scanner = scanner;
		this.totalFee = 0;
		this.bookList = new Book[1000000];
		this.memberList = new LibraryMember[1000000];
	}

	/**
	 * Returns the nextMemberId that is the id will be given to the next registered
	 * member
	 * 
	 * @return {@link #nextMemberId}
	 */
	public int getNextMemberId() {
		return this.nextMemberId;
	}

	/**
	 * The getter method of the total fee earned by the library.
	 * 
	 * @return {@link #totalFee}
	 * 
	 */
	public int getTotalFee() {
		return this.totalFee;
	}

	/**
	 * The getter method of the whole book archive of the library
	 * 
	 * @return {@link #bookList}
	 */
	public Book[] getBooks() {
		return bookList;
	}

	/**
	 * The getter method of the list that includes all members of the library
	 * 
	 * @return {@link #memberList}
	 */
	public LibraryMember[] getMembers() {
		return memberList;
	}

	/**
	 * Adds the given book to the book list of the library
	 * 
	 * @param bookList is the whole book list of the library
	 * @param book     is the book that will be added to the library archive
	 */
	private void addBookList(Book[] bookList, Book book) {
		bookList[nextBookId - 1] = book;
		nextBookId++;
	}

	/**
	 * Adds the given member to the member list of the library
	 * 
	 * @param memberList is the whole member list of the library
	 * @param member     is the member that will be added to the library archive
	 */
	private void addMemberList(LibraryMember[] memberList, LibraryMember member) {
		memberList[nextMemberId - 1] = member;
		nextMemberId++;
	}

	/**
	 * Makes necessary checks and adds a book to the library. checks; if the next
	 * book will not exceed the limits of the library, if so checks whether the
	 * input is "P" or "H", if P, creates a Printed object, otherwise creates a
	 * Handwritten object by using the {@link #addBookList(Book[], Book)} method
	 */
	public void addBook() {
		if (nextBookId <= 999999) {
			String type = scanner.next();

			if (type.equals("P")) {
				addBookList(bookList, new Printed(nextBookId));
			} else if (type.equals("H")) {
				addBookList(bookList, new Handwritten(nextBookId));
			}
		}

	}

	/**
	 * Makes necessary checks and adds a member to the library member list. Checks
	 * if the next member will not exceed the capacity of the library;if so , reads
	 * the input, if the input is "S" creates a Student object, otherwise creates an
	 * Academic object and add to the {@link #memberList} by using
	 * {@link #addMemberList(LibraryMember[], LibraryMember)}
	 */
	public void addMember() {
		if (nextMemberId <= 999999) {
			String type = scanner.next();

			if (type.equals("S")) {
				addMemberList(memberList, new Student(nextMemberId));
			} else if (type.equals("A")) {
				addMemberList(memberList, new Academic(nextMemberId));
			}
		}

	}

	/**
	 * Simulates the borrowing process of a book from the library,if it is
	 * validated. Method checks if the corresponding book or the member does exist,
	 * by comparing the {@link #nextBookId} and {@link #nextMemberId} with the
	 * current ones.If comparison fails,method immediately gets
	 * terminated.Furthermore, method checks if the user is indebted to the library
	 * by traversing the member's history list and checking if any book has exceeded
	 * it's deadline.If not, if the book is in the library and not a Handwritten
	 * book then if member has not exceeded his maximum borrowing limit, then method
	 * calls the {@link Printed#borrowBook(LibraryMember, int)} and book gets
	 * borrowed.
	 * 
	 * @param tick the time unit that the borrowing event will occur within
	 */
	public void borrowBook(int tick) {
		int bookId = scanner.nextInt();
		int memberId = scanner.nextInt();
		if (bookId >= nextBookId)
			return;
		this.book = bookList[bookId - 1];
		if (memberId >= nextMemberId)
			return;
		this.member = memberList[memberId - 1];

		boolean isMemberIndebted = false;
		for (Book booq : member.getTheHistory()) {
			if (booq.getBookType().equals("P")) {
				if (((Printed) booq).isBorrowed()) {
					if (booq.getWhoHas().equals(member)) {
						if (((Printed) booq).getDeadLine() < tick) {
							isMemberIndebted = true;

						}
					}
				}
			}
		}
		if (!isMemberIndebted) {

			if (!book.isTaken()) {

				if (book.getBookType().equals("P")) {

					if (member.getNumberOfBooksBorrowed() < member.getMaxNumberOfBooks()) {

						((Printed) book).borrowBook(member, tick);
					}
				}

			}
		}
	}

	/**
	 * Simulates the return of a book. Same with the {@link #borrowBook(int)}
	 * method, checks the existence of the member and book. Then; if the member does
	 * own the book that he wants to return, and if book is a borrowed Printed boo
	 * then method calculates the fee should be paid(if there is) and returns the
	 * book by {@link Printed#borrowBook(LibraryMember, int)} If it's not borrowed
	 * book then book will be returned without a fee should be paid. Other than
	 * these if book is Handwritten book is simply returned if the first conditions
	 * are yielded
	 * 
	 * @param tick is the time unit that event occurs within
	 */
	public void returnBook(int tick) {
		int bookId = scanner.nextInt();
		int memberId = scanner.nextInt();
		if (bookId >= nextBookId)
			return;
		this.book = bookList[bookId - 1];
		if (memberId >= nextMemberId)
			return;
		this.member = memberList[memberId - 1];

		if (book.getWhoHas().equals(member)) {

			if (book.getBookType().equals("P")) {

				if (((Printed) book).isBorrowed()) {

					if (tick > ((Printed) book).getDeadLine()) {
						this.totalFee += tick - ((Printed) book).getDeadLine();
					}

					((Printed) book).returnBook(member);

				} else {
					((Printed) book).returnBook(member);

				}

			} else {
				((Handwritten) book).returnBook(member);
			}

		}

	}

	/**
	 * Simulates the extension event of a book's dead line. First checks the
	 * existence of book and member similar to {@link #borrowBook(int)}. Then if the
	 * owner of the extension request of the book is really the one who currently
	 * has the book and the book is a Printed book (because Handwritten can't be
	 * borrowed) then if the dead line is not passed and the book has not extended
	 * before then book's deadline gets extended by
	 * {@link Printed#extend(LibraryMember, int)}
	 * 
	 * @param tick is the time unit that event occurs within
	 */
	public void extendBook(int tick) {
		int bookId = scanner.nextInt();
		int memberId = scanner.nextInt();
		if (bookId >= nextBookId)
			return;
		this.book = bookList[bookId - 1];
		if (memberId >= nextMemberId)
			return;
		this.member = memberList[memberId - 1];

		if (book.getWhoHas().equals(member)) {

			if (book.getBookType().equals("P")) {

				if (!((Printed) book).isExtended()) {

					if (tick <= ((Printed) book).getDeadLine()) {

						((Printed) book).extend(member, tick);
					}

				}
			}

		}

	}

	/**
	 * Simulates the book's taken for reading in the library. First checks the
	 * existence of book and member similar to {@link #extendBook(int)}. Then if
	 * book is not taken and a Printed one then it can be read by simply calling
	 * {@link Printed#readBook(LibraryMember)}. If it is a Handwritten book, then
	 * the member must be an Academic if so the book is read by calling the
	 * {@link Handwritten#readBook(LibraryMember)}
	 * 
	 */
	public void readInLibrary() {
		int bookId = scanner.nextInt();
		int memberId = scanner.nextInt();
		if (bookId >= nextBookId)
			return;
		this.book = bookList[bookId - 1];
		if (memberId >= nextMemberId)
			return;
		this.member = memberList[memberId - 1];

		if (!book.isTaken()) {

			if (book.getBookType().equals("P")) {

				((Printed) book).readBook(member);
			} else {
				if (member.getMemberType().equals("A")) {

					((Handwritten) book).readBook(member);

				}

			}

		}

	}

}