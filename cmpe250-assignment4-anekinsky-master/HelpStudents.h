#ifndef CMPE250_ASSIGNMENT3_HELPSTUDENTS_H
#define CMPE250_ASSIGNMENT3_HELPSTUDENTS_H
#include <vector>
#include <iostream>
#include <fstream>
#include <list>
#include <set>
#include <map>

using namespace std;


class HelpStudents {

public:
    HelpStudents(int N, int M, int K, vector<pair<pair<int, int>, int> > ways);

    long long int firstStudent();

    long long int secondStudent();

    long long int thirdStudent();

    long long int fourthStudent();

    long long int fifthStudent();

    int N;
    int M;
    int K;
    long long int A;
    long long int bmin = -1;

    list<pair<int, int> > *adjacencyList;


    // YOU CAN ADD YOUR HELPER FUNCTIONS AND MEMBER FIELDS

    long long int firstStudent(list<pair<int, int>> *adjacencyList);

    long long int secondStudent(list<pair<int, int>> *adjacencyList);

    long long int thirdStudent(list<pair<int, int>> *adjacencyList);

    long long int fourthStudent(list<pair<int, int>> *adjacencyList);

    long long int fifthStudent(list<pair<int, int>> *adjacencyList);


    long long int
    fifthStudentx(long long int node, long long int counter, list<pair<int, int>> *adjacencyList,
                  long long int totalSum,
                  map<int, int> usedVerticesHowMany);
};


#endif //CMPE250_ASSIGNMENT3_HELPSTUDENTS_H

