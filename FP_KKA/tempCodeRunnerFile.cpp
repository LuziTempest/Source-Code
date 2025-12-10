#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
#include <cstdlib>
#include <ctime>
#include <chrono>

using namespace std;

struct Graph {
    map<char, vector<pair<char, int>>> adjList;
    vector<char> nodes;

    void add_edge(char c1, char c2, int weight) {
        adjList[c1].push_back({c2, weight});
        adjList[c2].push_back({c1, weight});
    }

    void add_node(char node) {
        if (find(nodes.begin(), nodes.end(), node) == nodes.end()) {
            nodes.push_back(node);
        }
    }

    int get_edge_cost(char from, char to) {
        for (auto& edge : adjList[from]) {
            if (edge.first == to) {
                return edge.second;
            }
        }
        return 99999;
    }

    int calculate_route_cost(const vector<char>& route) {
        int totalCost = 0;
        for (size_t i = 0; i < route.size() - 1; i++) {
            totalCost += get_edge_cost(route[i], route[i + 1]);
        }
        return totalCost;
    }

    void print_route(const vector<char>& route, int cost, int iteration = -1) {
        if (iteration >= 0) {
            cout << "Iterasi " << iteration << " - ";
        }
        cout << "Rute: ";
        for (size_t i = 0; i < route.size(); i++) {
            cout << route[i];
            if (i < route.size() - 1) cout << " -> ";
        }
        cout << " | Total Cost: " << cost << endl;
    }

    vector<char> generate_random_route(char start, char goal) {
        vector<char> route;
        route.push_back(start);
        
        vector<char> middle_nodes;
        for (char node : nodes) {
            if (node != start && node != goal) {
                middle_nodes.push_back(node);
            }
        }
        
        random_shuffle(middle_nodes.begin(), middle_nodes.end());
        
        for (char node : middle_nodes) {
            route.push_back(node);
        }
        
        route.push_back(goal);
        return route;
    }

    vector<char> hill_climbing_local_search(char start, char goal) {
        vector<char> current_route = generate_random_route(start, goal);
        int current_cost = calculate_route_cost(current_route);
        
        cout << "\n=== HILL CLIMBING LOCAL SEARCH ===" << endl;
        cout << "\nRute Awal (Random):" << endl;
        print_route(current_route, current_cost);
        cout << "\nProses Optimasi:" << endl;
        
        int iteration = 1;
        bool improved = true;
        
        while (improved) {
            improved = false;
            vector<char> best_neighbor = current_route;
            int best_cost = current_cost;
            
            for (size_t i = 1; i < current_route.size() - 2; i++) {
                for (size_t j = i + 1; j < current_route.size() - 1; j++) {
                    vector<char> neighbor = current_route;
                    swap(neighbor[i], neighbor[j]);
                    
                    int neighbor_cost = calculate_route_cost(neighbor);
                    
                    if (neighbor_cost < best_cost) {
                        best_neighbor = neighbor;
                        best_cost = neighbor_cost;
                        improved = true;
                    }
                }
            }
            
            if (improved) {
                current_route = best_neighbor;
                current_cost = best_cost;
                print_route(current_route, current_cost, iteration);
                iteration++;
            }
        }
        
        cout << "\n=== HASIL AKHIR ===" << endl;
        cout << "Solusi Optimal (Local Optimum):" << endl;
        print_route(current_route, current_cost);
        cout << "Total Iterasi: " << (iteration - 1) << endl;
        
        return current_route;
    }
};


int main() {
    srand(time(0));
    
    Graph g;

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

    g.add_node('S');
    g.add_node('A');
    g.add_node('B');
    g.add_node('C');
    g.add_node('D');
    g.add_node('E');
    g.add_node('F');
    g.add_node('G');

    cout << "========================================" << endl;
    cout << "  OPTIMASI RUTE DENGAN HILL CLIMBING" << endl;
    cout << "========================================" << endl;
    
    auto start_time = chrono::high_resolution_clock::now();
    vector<char> optimal_route = g.hill_climbing_local_search('S', 'G');
    auto end_time = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    
    cout << "\n=== ANALISIS PERFORMA ===" << endl;
    cout << "Waktu eksekusi: " << duration.count() << " mikrosekon" << endl;
    cout << "Panjang rute: " << optimal_route.size() << " node" << endl;

    return 0;
}