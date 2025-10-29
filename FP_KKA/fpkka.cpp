#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
#include <set>
#include <chrono>

using namespace std;

struct Graph {
    int vertexCount;
    map<char, vector<pair<char, int>>> adjList;

    void init(int v) {
        vertexCount = v;
        adjList.clear();
    }

    void add_edge(char c1, char c2, int weight) {
        adjList[c1].push_back({c2, weight});
        adjList[c2].push_back({c1, weight}); 
    }

    vector<char> hill_climbing(char startNode, char goalNode, map<char, int> &h, int &totalCost) {
        totalCost = 0;
        vector<char> path;
        path.push_back(startNode);

        char currentNode = startNode;
        
        set<char> visitedOnPath;
        visitedOnPath.insert(currentNode);

        while (currentNode != goalNode) {
            char bestNeighbor = 0; 
            int costToNeighbor = 0;
            int minHeuristic = h[currentNode]; 

            for (auto& edge : adjList[currentNode]) {
                char neighbor = edge.first;
                int weight = edge.second;

                if (visitedOnPath.count(neighbor)) {
                    continue; 
                }

                if (h[neighbor] < minHeuristic) {
                    minHeuristic = h[neighbor];
                    bestNeighbor = neighbor;
                    costToNeighbor = weight;
                }
            }

            if (bestNeighbor == 0) {
                return {};
            }

            currentNode = bestNeighbor;
            visitedOnPath.insert(currentNode);
            path.push_back(currentNode);
            totalCost += costToNeighbor;
        }

        return path;
    }
};

int main() {
    Graph g;
    g.init(8); 

    g.add_edge('S','A',10);
    g.add_edge('S','B',12);
    g.add_edge('A','C',9);
    g.add_edge('A','D',6);
    g.add_edge('B','D',7);
    g.add_edge('B','E',15);
    g.add_edge('C','F',8);
    g.add_edge('D','F',11);
    g.add_edge('E','F',5);
    g.add_edge('F','G',0);

    map<char, int> h;
    h['S'] = 20;
    h['A'] = 14;
    h['B'] = 13;
    h['C'] = 10;
    h['D'] = 12;
    h['E'] = 9;
    h['F'] = 6;
    h['G'] = 0;

    cout << "--- Hill Climbing (Local Search) ---" << endl;
    
    int totalCost = 0;
    
    auto start = std::chrono::high_resolution_clock::now();

    vector<char> path = g.hill_climbing('A','G', h, totalCost);

    auto end = std::chrono::high_resolution_clock::now();
    
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);

    if (!path.empty()) {
        cout << "Jalur ditemukan : ";
        for (int i = 0; i < path.size(); ++i) {
            cout << path[i] << (i == path.size() - 1 ? "" : " -> ");
        }
        cout << "\nTotal cost      : " << totalCost << endl;
    } else {
        cout << "Jalur tidak ditemukan." << endl;
        cout << "(Algoritma terjebak di local minimum sebelum mencapai tujuan)" << endl;
    }

    cout << "\n--- Analisis Performa ---" << endl;
    cout << "Waktu eksekusi: " << duration.count() << " mikrosekon" << endl;
    cout << "Memori (Path)   : " << path.size() << " elemen node" << endl;

    return 0;
}
