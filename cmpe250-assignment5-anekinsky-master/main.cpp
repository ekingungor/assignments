/*
Student Name:
Student Number:
Project Number: 5
Compile Status: [SUCCESS/FAIL]
Running Status: [SUCCESS/FAIL]
Notes: Anything you want to say about your code that will be helpful in the grading process.
*/

#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <algorithm>

using namespace std;

void parseInput(ifstream &inFile, std::vector<long long int> &firstDayPrices, int &N, int &M);

int main(int argc, char* argv[]) {

    ifstream inFile (argv[1]);
    ofstream outFile(argv[2]);

    int N, M;

    vector<long long int> firstDayPrices;


    parseInput(inFile, firstDayPrices, N, M);
    vector<long long int> additions (M,0) ;
    vector<long long int> solution (N,0);



    sort(firstDayPrices.begin(), firstDayPrices.end());
    additions[0] = firstDayPrices[0];
    solution[0] = additions[0];

    int index = 0;
    for(int i = 1 ; i<N; i++){
        index = i%M;
        additions[index] =  additions[index] + firstDayPrices[i];
        solution[i] = solution[i-1] + additions[index];

    }

    for(int i = 0 ; i<N ; i++){
        outFile<<solution[i]<<" ";
        //cout<<solution[i]<<" "<<endl;
    }
    outFile<<endl;


}

void parseInput(ifstream &inFile, vector<long long int> &firstDayPrices, int &N, int &M) {


    string line;
    getline(inFile, line);

    istringstream linestream1(line);

    string gr_size;


    getline(linestream1, gr_size, ' ');
    N=stoi(gr_size);

    getline(linestream1, gr_size, ' ');
    M=stoi(gr_size);

    getline(inFile, line);

    istringstream linestream2(line);

    for(int i = 0 ; i <N ; i++){
        getline(linestream2, gr_size, ' ');
        firstDayPrices.push_back (stoi(gr_size));
    }

    inFile.close();





}




