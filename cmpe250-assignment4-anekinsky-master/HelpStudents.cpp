/*
Student Name:
Student Number:
Project Number: 4
Compile Status: [SUCCESS/FAIL]
Running Status: [SUCCESS/FAIL]
Notes: Anything you want to say about your code that will be helpful in the grading process.
*/
#include <list>
#include <set>
#include <limits>
#include "HelpStudents.h"
#include <algorithm>
#include <queue>
#include <map>

using namespace std;
const int INF = numeric_limits<int>::max();


HelpStudents::HelpStudents(int N, int M, int K, vector<pair<pair<int, int>, int> > ways) {

    this->adjacencyList = new list<pair<int, int> >[N + 1];
    this->M = M;
    this->N = N;
    this->K = K;
    int u;
    int v;
    int w;
    for (pair<pair<int, int>, int> p : ways) {
        u = p.first.first;
        v = p.first.second;
        w = p.second;
        (adjacencyList[u]).push_back(make_pair(v, w));
        (adjacencyList[v]).push_back(make_pair(u, w));
    }
    this->adjacencyList = adjacencyList;


}

long long int HelpStudents::firstStudent(list<pair<int, int> > *adjacencyList) {

    set<pair<long long int, long long int> > mySet;

    vector<long long int> dist(N + 1, -1);
    vector<bool> marked(N + 1, false);

    mySet.insert(make_pair(0, 1));

    dist[1] = 0;


    while (!mySet.empty()) {

        pair<long long int, long long int> temp = *(mySet.begin());
        mySet.erase(mySet.begin());

        long long int label = temp.second;
        marked[label] = true;

        list<pair<int, int> >::iterator i;
        for (i = adjacencyList[label].begin(); i != adjacencyList[label].end(); ++i) {
            long long int v = (*i).first;
            long long int w = (*i).second;

            if (!marked[v]) {
                if (dist[v] == -1) {
                    dist[v] = dist[label] + w;
                    mySet.insert(make_pair(dist[v], v));
                } else {
                    if (dist[v] > dist[label] + w) {
                        mySet.erase(mySet.find(make_pair(dist[v], v)));
                        dist[v] = dist[label] + w;
                        mySet.insert(make_pair(dist[v], v));

                    }
                }

            }
        }
    }

    return dist[K];




    // IMPLEMENT ME!
}

long long int HelpStudents::secondStudent(list<pair<int, int> > *adjacencyList) {

    set<pair<long long int, long long int> > mySet;

    vector<long long int> maxEdgeWeight(N + 1, -1);
    vector<bool> marked(N + 1, false);

    mySet.insert(make_pair(0, 1));

    maxEdgeWeight[1] = 0;


    while (!mySet.empty()) {

        pair<long long int, long long int> temp = *(mySet.begin());
        mySet.erase(mySet.begin());

        long long int label = temp.second;
        marked[label] = true;

        list<pair<int, int> >::iterator i;
        for (i = adjacencyList[label].begin(); i != adjacencyList[label].end(); ++i) {
            long long int v = (*i).first;
            long long int w = (*i).second;

            if (!marked[v]) {
                if (maxEdgeWeight[v] == -1) {
                    maxEdgeWeight[v] = max(maxEdgeWeight[label], w);
                    mySet.insert(make_pair(maxEdgeWeight[v], v));
                } else {
                    if (maxEdgeWeight[v] > max(maxEdgeWeight[label], w)) {
                        mySet.erase(mySet.find(make_pair(maxEdgeWeight[v], v)));
                        maxEdgeWeight[v] = max(maxEdgeWeight[label], w);
                        mySet.insert(make_pair(maxEdgeWeight[v], v));

                    }
                }

            }
        }
    }

    return maxEdgeWeight[K];

}

long long int HelpStudents::thirdStudent(list<pair<int, int> > *adjacencyList) {

    set<pair<long long int, long long int> > mySet;

    vector<int> dist(N + 1, -1);
    vector<bool> marked(N + 1, false);

    mySet.insert(make_pair(0, 1));

    dist[1] = 0;


    while (!mySet.empty()) {

        pair<long long int, long long int> temp = *(mySet.begin());
        mySet.erase(mySet.begin());

        long long int label = temp.second;
        marked[label] = true;

        list<pair<int, int> >::iterator i;
        for (i = adjacencyList[label].begin(); i != adjacencyList[label].end(); ++i) {
            long long int v = (*i).first;
            long long int w = 1;

            if (!marked[v]) {
                if (dist[v] == -1) {
                    dist[v] = dist[label] + w;
                    mySet.insert(make_pair(dist[v], v));
                } else {
                    if (dist[v] > dist[label] + w) {
                        mySet.erase(mySet.find(make_pair(dist[v], v)));
                        dist[v] = dist[label] + w;
                        mySet.insert(make_pair(dist[v], v));

                    }
                }

            }

        }
    }

    return dist[K];


}

long long int HelpStudents::fourthStudent(list<pair<int, int> > *adjacencyList) {


    set<pair<long long int, long long int> > usedEdgesSet;
    set<pair<long long int, long long int> > nextEdgeSet;

    long long int totalSum = 0;

    int label = 1;
    bool is_in1;
    bool is_in2;


    while (true) {

        list<pair<int, int> >::iterator i;
        for (i = adjacencyList[label].begin(); i != adjacencyList[label].end(); ++i) {
            is_in1 = usedEdgesSet.find(make_pair(label, (*i).first)) != usedEdgesSet.end();
            is_in2 = usedEdgesSet.find(make_pair((*i).first, label)) != usedEdgesSet.end();
            if (!(is_in1 & is_in2)) {
                nextEdgeSet.insert(make_pair((*i).second, (*i).first));
            }
        }
        if (nextEdgeSet.empty()) {
            return -1;
        } else {
            pair<long long int, long long int> temp = *(nextEdgeSet.begin());
            totalSum = totalSum + temp.first;
            usedEdgesSet.insert(make_pair(label, temp.second));
            usedEdgesSet.insert(make_pair(temp.second, label));
            label = temp.second;
            nextEdgeSet.clear();
            if (label == this->K) {
                return totalSum;
            }
        }

    }

}


long long int
HelpStudents::fifthStudentx(long long int node, long long int counter,
                            list<pair<int, int> > *adjacencyList, long long int totalSum,
                            map<int, int> usedVerticesHowMany) {
    if (totalSum > this->A) {
        return -1;
    }

    if (bmin != -1) {
        if (totalSum > bmin) {
            return -1;
        }
    }
    set<long long int> mySet;

    if (node == this->K) {
        return totalSum;
    }

    list<pair<int, int> >::iterator i;
    for (i = adjacencyList[node].begin(); i != adjacencyList[node].end(); ++i) {

        int v = (*i).first;
        int w = (*i).second;
        if (usedVerticesHowMany.count(v)) {

            if (usedVerticesHowMany[v] > 2) {
                continue;
            } else {
                usedVerticesHowMany[v]++;
                long long int x;
                if (counter % 3 != 0) {
                    x = (fifthStudentx(v, counter + 1, adjacencyList,
                                       totalSum + w, usedVerticesHowMany));
                } else {
                    x = (fifthStudentx(v, counter + 1, adjacencyList, totalSum,
                                       usedVerticesHowMany));
                }

                if (x >= 0) {
                    mySet.insert(x);
                }
                usedVerticesHowMany[v]--;

            }
        } else {
            usedVerticesHowMany.insert({v,1});
            long long int x;
            if (counter % 3 != 0) {
                x = (fifthStudentx(v, counter + 1, adjacencyList,
                                   totalSum + w ,usedVerticesHowMany));
            } else {
                x = (fifthStudentx(v, counter + 1, adjacencyList, totalSum,
                                   usedVerticesHowMany));
            }

            if (x >= 0) {
                mySet.insert(x);
            }
            usedVerticesHowMany.erase(v);

        }
    }

    if (!mySet.empty()) {
        long long int w = *(mySet.begin());
        if (bmin == -1) {
            bmin = w;
        } else {
            if (bmin > w) {
                bmin = w;
            }
        }
        return w;

    } else {
        return -1;
    }
}


long long int HelpStudents::firstStudent() {
    return firstStudent((this->adjacencyList));
}

long long int HelpStudents::secondStudent() {
    return secondStudent((this->adjacencyList));
}

long long int HelpStudents::thirdStudent() {
    return thirdStudent((this->adjacencyList));
}

long long int HelpStudents::fourthStudent() {
    return fourthStudent((this->adjacencyList));

}

long long int HelpStudents::fifthStudent() {
    return fifthStudent(this->adjacencyList);

}

long long int HelpStudents::fifthStudent(list<pair<int, int>> *adjacencyList) {
    this->A = firstStudent(adjacencyList);
    map<int, int> usedVerticesHowManyTimes;
    usedVerticesHowManyTimes.insert({1, 1});
    return fifthStudentx(1, 1,  adjacencyList, 0, usedVerticesHowManyTimes);
}




// YOU CAN ADD YOUR HELPER FUNCTIONS
