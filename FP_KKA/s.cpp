#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <map>
#include <random>
#include <ctime>

using namespace std;

// Struktur untuk merepresentasikan Edge
struct Edge {
    char to;
    int weight;
};

struct Graph {
    map<char, vector<Edge>> adjList;

    void add_edge(char c1, char c2, int weight) {
        adjList[c1].push_back({c2, weight});
        // Karena graf berarah (S->A, tapi tidak A->S untuk mencapai G), 
        // kita asumsikan graf sesuai definisi soal (Directed Graph untuk alur)
        // Jika Undirected, uncomment baris bawah:
        // adjList[c2].push_back({c1, weight}); 
    }

    // Fungsi bantu untuk menghitung total cost dari sebuah jalur (path)
    int calculatePathCost(const vector<char>& path) {
        int cost = 0;
        for (size_t i = 0; i < path.size() - 1; ++i) {
            bool found = false;
            for (auto& e : adjList[path[i]]) {
                if (e.to == path[i+1]) {
                    cost += e.weight;
                    found = true;
                    break;
                }
            }
            if (!found) return 99999; // Jalur tidak valid
        }
        return cost;
    }

    // Fungsi DFS Random untuk mencari jalur acak dari Start ke Goal
    // Ini digunakan untuk mendapatkan "Initial State"
    bool findRandomPathDFS(char u, char target, vector<char>& currentPath, vector<char>& finalPath) {
        currentPath.push_back(u);
        if (u == target) {
            finalPath = currentPath;
            return true;
        }

        // Acak urutan tetangga agar jalur yang didapat random
        vector<Edge> neighbors = adjList[u];
        shuffle(neighbors.begin(), neighbors.end(), default_random_engine(time(0) + rand()));

        for (auto& e : neighbors) {
            if (findRandomPathDFS(e.to, target, currentPath, finalPath)) {
                return true;
            }
        }
        
        currentPath.pop_back();
        return false;
    }

    // Mendapatkan Initial State (Rute Random Awal)
    vector<char> getRandomPath(char start, char goal) {
        vector<char> path, temp;
        findRandomPathDFS(start, goal, temp, path);
        return path;
    }

    // Fungsi bantu mencari jalur pelengkap (dipakai saat generate neighbors)
    bool findAnyPath(char u, char target, vector<char>& path) {
        path.push_back(u);
        if (u == target) return true;
        for (auto& e : adjList[u]) {
            if (findAnyPath(e.to, target, path)) return true;
        }
        path.pop_back();
        return false;
    }

    // Generate Neighbors: Mencari variasi jalur dari jalur saat ini
    // Logika: Coba belok ke node lain di setiap titik persimpangan jalur saat ini
    vector<vector<char>> getPathNeighbors(vector<char> currentPath, char goal) {
        vector<vector<char>> neighbors;

        // Iterasi setiap node dalam jalur (kecuali node terakhir)
        for (size_t i = 0; i < currentPath.size() - 1; ++i) {
            char u = currentPath[i];
            char nextInPath = currentPath[i+1]; // Ke mana kita pergi di jalur saat ini

            // Cek cabang lain dari node u
            for (auto& e : adjList[u]) {
                if (e.to != nextInPath) {
                    // Ada cabang alternatif! Coba buat jalur baru lewat sini
                    vector<char> newPathPrefix;
                    // Copy jalur dari awal sampai u
                    for(size_t k=0; k<=i; k++) newPathPrefix.push_back(currentPath[k]);
                    
                    // Coba sambungkan cabang alternatif ini ke Goal
                    vector<char> suffix;
                    if (findAnyPath(e.to, goal, suffix)) {
                        // Gabungkan prefix dan suffix
                        newPathPrefix.insert(newPathPrefix.end(), suffix.begin(), suffix.end());
                        neighbors.push_back(newPathPrefix);
                    }
                }
            }
        }
        return neighbors;
    }

    // ALGORITMA UTAMA: Local Search Hill Climbing
    void hillClimbingSearch(char startNode, char goalNode) {
        // 1. Generate Initial State (Satu rute random)
        vector<char> currentPath = getRandomPath(startNode, goalNode);
        int currentCost = calculatePathCost(currentPath);

        if (currentPath.empty()) {
            cout << "Tidak ada jalur yang ditemukan dari awal." << endl;
            return;
        }

        cout << ">>> Initial State (Rute Random Awal): ";
        for (char c : currentPath) cout << c << " ";
        cout << "| Cost: " << currentCost << endl;
        cout << "------------------------------------------------" << endl;

        bool improvement = true;
        int iteration = 1;

        // Loop Hill Climbing
        while (improvement) {
            improvement = false;
            vector<vector<char>> neighbors = getPathNeighbors(currentPath, goalNode);
            
            vector<char> bestNeighborPath;
            int bestNeighborCost = 99999;

            // Cari tetangga terbaik (Steepest Ascent)
            for (auto& p : neighbors) {
                int c = calculatePathCost(p);
                if (c < bestNeighborCost) {
                    bestNeighborCost = c;
                    bestNeighborPath = p;
                }
            }

            // Bandingkan dengan state saat ini
            if (!neighbors.empty() && bestNeighborCost < currentCost) {
                cout << "Iterasi " << iteration++ << ": Ditemukan rute tetangga yang lebih baik." << endl;
                cout << "   Rute Lama Cost: " << currentCost << " -> Rute Baru Cost: " << bestNeighborCost << endl;
                cout << "   Rute Baru: ";
                for (char c : bestNeighborPath) cout << c << " ";
                cout << endl;

                // Move to better state
                currentPath = bestNeighborPath;
                currentCost = bestNeighborCost;
                improvement = true;
            } else {
                cout << "Iterasi " << iteration << ": Tidak ada tetangga yang lebih baik. (Local Optima tercapai)" << endl;
                // Jika cost sama atau lebih mahal, kita berhenti
            }
        }

        cout << "------------------------------------------------" << endl;
        cout << ">>> FINAL SOLUTION (Rute Terbaik): ";
        for (int i = 0; i < currentPath.size(); ++i) {
            cout << currentPath[i] << (i == currentPath.size() - 1 ? "" : " -> ");
        }
        cout << "\nTotal Cost Terakhir: " << currentCost << endl;
    }
};

int main() {
    srand(time(0)); // Seed untuk random

    Graph g;
    // Membangun Graf sesuai soal
    // Note: Saya menggunakan Directed Graph agar alur S->G jelas
    g.add_edge('S','A',10);
    g.add_edge('S','B',12);
    g.add_edge('A','C',9);
    g.add_edge('A','D',6); // Jalur ini lebih pendek dari A->C
    g.add_edge('B','D',7);
    g.add_edge('B','E',15);
    g.add_edge('C','F',8);
    g.add_edge('D','F',11);
    g.add_edge('E','F',5);
    g.add_edge('F','G',0);

    // Heuristik tidak lagi menjadi penentu utama dalam "Total Cost" path fisik, 
    // tapi Hill Climbing ini mengoptimalkan "Jarak Tempuh (Cost)" sebenarnya.

    cout << "=== SIMULASI LOCAL SEARCH HILL CLIMBING ===" << endl;
    // Skenario: Mencari rute S ke G
    g.hillClimbingSearch('S', 'G');

    return 0;
}