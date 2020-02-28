/*
Student Name:
Student Number:
Project Number: 3
Compile Status: [SUCCESS/FAIL]
Running Status: [SUCCESS/FAIL]
Notes: Anything you want to say about your code that will be helpful in the grading process.
*/
#include "SeatOperations.h"

using namespace std;

SeatOperations::SeatOperations(int N, int M){
    // IMPLEMENT ME!
    lines[0]= vector<Person> (N);
    lines[1]= vector<Person> (M);
    this->N = N;
    this->M= M;
}


void SeatOperations::addNewPerson(int personType, const string& ticketInfo){

    int line = ticketInfo[0] -'A' ;
    string nx = ticketInfo.substr(1);
    int seatNumber = std::stoi(nx);

    addNewPerson(personType,0,line,seatNumber,-1,ticketInfo);

}

void SeatOperations::addNewPerson(int personType,int type3LastOperation, int line, int seatNumber,int location,string ticketInfo) {


    int loc;
    if (location < 0) {
        if (line == 0) {
            loc = (seatNumber - 1) % this->N;
        } else {
            loc = (seatNumber - 1) % this->M;
        }
        location = loc;
    }

    Person * p = &(lines[line][location]);

    if (p->type == 0) {
        p->type = personType;
        p->type3LastOperation = type3LastOperation;
        p->seatNumber = seatNumber;
        p->line = line;
        p->ticketInfo = ticketInfo;

    } else {
        int tempPersonType = p->type;
        int tempLine = p->line;
        int tempType3LastOperation = p->type3LastOperation;
        int tempSeatNumber = p->seatNumber;
        string tempTicketInfo = p->ticketInfo;


        p->line = line;
        p->seatNumber = seatNumber;
        p->type3LastOperation = type3LastOperation;
        p->type = personType;
        p->ticketInfo = ticketInfo;

        if (tempPersonType == 1) {

            if (tempLine == 0) {
                location = (tempSeatNumber - 1) % this->M;
                addNewPerson(tempPersonType, tempType3LastOperation, 1, tempSeatNumber, location, tempTicketInfo);
            } else {
                location = (tempSeatNumber - 1) % this->N;
                addNewPerson(tempPersonType, tempType3LastOperation, 0, tempSeatNumber, location, tempTicketInfo);
            }

        } else if (tempPersonType == 2) {

            if (tempLine == 0) {
                if (location == (this->N - 1)) {
                    addNewPerson(tempPersonType, tempType3LastOperation, 1, tempSeatNumber, 0, tempTicketInfo);
                } else {
                    addNewPerson(tempPersonType, tempType3LastOperation, 0, tempSeatNumber, location + 1,
                                 tempTicketInfo);
                }
            } else if (tempLine == 1) {
                if (location == (this->M - 1)) {

                    addNewPerson(tempPersonType, tempType3LastOperation, 0, tempSeatNumber, 0, tempTicketInfo);

                } else {
                    addNewPerson(tempPersonType, tempType3LastOperation, 1, tempSeatNumber, location + 1,
                                 tempTicketInfo);
                }
            }

        } else if (tempPersonType == 3) {

            int shift = (tempType3LastOperation * 2 + 1)%(this->N+this->M);
            tempType3LastOperation++;
            int position = location + shift;

            while (true) {
                if (tempLine == 0) {
                    if (position >= this->N) {
                        tempLine = 1;
                        position = position - this->N;
                        if (position < this->M) {
                            break;
                        } else {
                            tempLine = 0;
                            position = position - this->M;
                        }
                    } else {
                        break;
                    }
                } else {
                    if (position >= this->M) {
                        tempLine = 0;
                        position = position - this->M;
                        if (position < this->N) {
                            break;
                        } else {
                            tempLine = 1;
                            position = position - this->N;
                        }
                    } else {
                        break;
                    }

                }
            }

            addNewPerson(tempPersonType, tempType3LastOperation, tempLine, tempSeatNumber, position, tempTicketInfo);


        }
    }
}


    void SeatOperations::printAllSeats(ofstream &outFile) {
        // IMPLEMENT ME!

        for (int i = 0; i < lines[0].size(); i++) {
            if (lines[0][i].type == 0) {
                outFile << 0 << "\n";
            } else {
                outFile << lines[0][i].type << " " << lines[0][i].ticketInfo << "\n";
            }
        }
        for (int i = 0; i < lines[1].size(); i++) {
            if (lines[1][i].type == 0) {
                outFile << 0 << "\n";
            } else {
                outFile << lines[1][i].type << " " << lines[1][i].ticketInfo << "\n";
            }
        }
        outFile<<endl;
    }



// YOU CAN ADD YOUR HELPER FUNCTIONS
