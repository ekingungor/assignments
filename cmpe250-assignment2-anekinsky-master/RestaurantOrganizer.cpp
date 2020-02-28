/*
Student Name:
Student Number:
Project Number: 2
Compile Status: [SUCCESS/FAIL]
Running Status: [SUCCESS/FAIL]
Notes: Anything you want to say about your code that will be helpful in the grading process.
*/
#include "RestaurantOrganizer.h"

using namespace std;

RestaurantOrganizer::RestaurantOrganizer(const vector<int>& tableCapacityInput){
    //puts the tables in a binary heap according to their capacity and forms the restaurant.

    numberOfTables = tableCapacityInput.size();

    for(int i=0;i<numberOfTables;i++){
        tableCapacity[i] = tableCapacityInput[i];
        heap[i] = i;
        heapUp(i);
    }





}

void RestaurantOrganizer::addNewGroup(int groupSize, ofstream& outFile){
    // make customers sit to a suitable table and prints the number of the table.
    // if there is no suitable table then prints -1.




        if(tableCapacity[heap[0]]-tableCurrent[heap[0]]>= groupSize){
            tableCurrent[heap[0]]+=groupSize;
            outFile<<heap[0]<< "\n";
            heapDown(0);

        }
        else{
            outFile<<"-1\n";
    }

}

void RestaurantOrganizer::heapUp(int index){
    // percolates up the the element with the given array index, in the heap.

    int tableNo = heap[index];

    int remaining =  tableCapacity[tableNo];

    int hole = index;

    int parent = ((hole+1)/2)-1;
    for(; hole>0 && remaining>=tableCapacity[heap[parent]];){

        if(tableCapacity[heap[parent]] == remaining){
            if(tableNo<heap[parent]){
                heap[ hole ] = heap[parent];
                hole=parent;
                parent=((parent+1)/2)-1;
                continue;
            }
            else{
                break;
            }
        }
        heap[ hole ] = heap[parent];
        hole=parent;
        parent=((parent+1)/2)-1;
}
    heap[hole] = tableNo;



}

void RestaurantOrganizer::heapDown(int index){
    // percolates down the element with the given array index, in the heap.
    int tableNo = heap[index];
    int remaining =  tableCapacity[tableNo]-tableCurrent[tableNo];

    int hole = index;

    int child = hole*2+1;
    for(; hole*2+1 < numberOfTables;){

        if( child != numberOfTables-1 ) {
            if(tableCapacity[heap[ child + 1 ]]-tableCurrent[heap[child+1]] >
               tableCapacity[heap[ child  ]]-tableCurrent[heap[child]] ){
                child++;
            }
            else if (tableCapacity[heap[ child + 1 ]]-tableCurrent[heap[child+1]] ==
                     tableCapacity[heap[ child  ]]-tableCurrent[heap[child]] && heap[child+1]<heap[child]){
                child++;
            }


        }
        if( remaining< tableCapacity[heap[child]]-tableCurrent[heap[child]]){
            heap[ hole ] = heap[ child ];
            hole= child;
            child= hole*2+1;


        }
        else if ( remaining == tableCapacity[heap[child]]-tableCurrent[heap[child]]){
            if(tableNo>heap[child]){
                heap[ hole ] = heap[ child ];
                hole=child;
                child= hole*2+1;
            }
            else {
                break;
            }

        }
        else {
            break;
        }
    }

    heap[hole] = tableNo;
}

void RestaurantOrganizer::printSorted(ofstream& outFile){

    // prints and removes the root of the binary heap tree, until tree is empty. eventually prints all the tables according to their remaining capacities.

    int count= numberOfTables;

    for(int i = 0 ; i<count ; i++){

        outFile<<tableCapacity[heap[0]]-tableCurrent[heap[0]]<<" ";
        heap[0] = heap[ numberOfTables-1 ];
        numberOfTables--;
        heapDown(0 );

        }
    }


// YOU CAN ADD YOUR HELPER FUNCTIONS

