#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <random>
#include <ctime>

using namespace std;

struct Edge {
    char to;
    int weight;
};

struct Graph {
    map<char, vector<Edge>> adjList;

    void add_edge(char c1, char c2, int weight) {
        adjList[c1].push_back({c2, weight});
    }

    // Menghitung total biaya jalur
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
            if (!found) return 99999; 
        }
        return cost;
    }

    // DFS Random untuk Initial State
    bool findRandomPathDFS(char u, char target, vector<char>& currentPath, vector<char>& finalPath) {
        currentPath.push_back(u);
        if (u == target) {
            finalPath = currentPath;
            return true;
        }

        vector<Edge> neighbors = adjList[u];
        // Acak urutan tetangga agar rute awal benar-benar random
        shuffle(neighbors.begin(), neighbors.end(), default_random_engine(time(0) + rand()));

        for (auto& e : neighbors) {
            // Hindari siklus sederhana (tidak kembali ke node yang sudah ada di path saat ini)
            bool visited = false;
            for(char p : currentPath) if(p == e.to) visited = true;
            
            if (!visited) {
                if (findRandomPathDFS(e.to, target, currentPath, finalPath)) {
                    return true;
                }
            }
        }
        
        currentPath.pop_back();
        return false;
    }

    vector<char> getRandomPath(char start, char goal) {
        vector<char> path, temp;
        // Kita coba cari path berulang kali sampai ketemu, karena random bisa saja menemui jalan buntu
        for(int i=0; i<100; i++) {
            temp.clear();
            if(findRandomPathDFS(start, goal, temp, path)) return path;
        }
        return {};
    }

    // Mencari kelanjutan jalur (suffix) untuk menyambung tetangga
    bool findAnyPath(char u, char target, vector<char>& path) {
        path.push_back(u);
        if (u == target) return true;
        
        // Urutkan edge berdasarkan weight termurah agar tetangga yang digenerate cenderung logis
        // (Opsional, tapi membantu menemukan tetangga yang valid lebih cepat)
        vector<Edge> neighbors = adjList[u];
        sort(neighbors.begin(), neighbors.end(), [](const Edge& a, const Edge& b){
            return a.weight < b.weight;
        });

        for (auto& e : neighbors) {
            // Cek visited sederhana untuk suffix
            bool visited = false;
            for(char p : path) if(p == e.to) visited = true;

            if (!visited) {
                if (findAnyPath(e.to, target, path)) return true;
            }
        }
        path.pop_back();
        return false;
    }

    // Generate Neighbors: Mencari variasi jalur
    vector<vector<char>> getPathNeighbors(vector<char> currentPath, char goal) {
        vector<vector<char>> neighbors;

        for (size_t i = 0; i < currentPath.size() - 1; ++i) {
            char u = currentPath[i];
            char nextInPath = currentPath[i+1]; 

            for (auto& e : adjList[u]) {
                // Jika ada cabang lain selain yang sedang dilewati
                if (e.to != nextInPath) {
                    vector<char> newPathPrefix;
                    for(size_t k=0; k<=i; k++) newPathPrefix.push_back(currentPath[k]);
                    
                    vector<char> suffix;
                    // Coba sambungkan cabang ini ke Goal
                    if (findAnyPath(e.to, goal, suffix)) {
                        // Gabung: Prefix (S..u) + Suffix (cabang..G)
                        newPathPrefix.insert(newPathPrefix.end(), suffix.begin(), suffix.end());
                        neighbors.push_back(newPathPrefix);
                    }
                }
            }
        }
        return neighbors;
    }

    void hillClimbingSearch(char startNode, char goalNode) {
        // 1. Initial State
        vector<char> currentPath = getRandomPath(startNode, goalNode);
        int currentCost = calculatePathCost(currentPath);

        if (currentPath.empty()) {
            cout << "Error: Tidak dapat menemukan jalur awal (Graph mungkin terputus/buntu)." << endl;
            return;
        }

        cout << ">>> Initial State (Rute Random): ";
        for (char c : currentPath) cout << c << " ";
        cout << "| Cost: " << currentCost << endl;
        cout << "------------------------------------------------" << endl;

        bool improvement = true;
        int iteration = 1;

        while (improvement) {
            improvement = false;
            vector<vector<char>> neighbors = getPathNeighbors(currentPath, goalNode);
            
            vector<char> bestNeighborPath;
            int bestNeighborCost = 99999;

            // Cari Steepest Ascent (Tetangga terbaik)
            for (auto& p : neighbors) {
                int c = calculatePathCost(p);
                if (c < bestNeighborCost) {
                    bestNeighborCost = c;
                    bestNeighborPath = p;
                }
            }

            // Cek apakah tetangga terbaik lebih murah dari current
            if (!neighbors.empty() && bestNeighborCost < currentCost) {
                cout << "Iterasi " << iteration++ << ": IMPROVEMENT!" << endl;
                cout << "   [Rute Lama (" << currentCost << ")] -> [Rute Baru (" << bestNeighborCost << ")]" << endl;
                cout << "   Pindah ke rute: ";
                for (char c : bestNeighborPath) cout << c << " ";
                cout << endl;

                currentPath = bestNeighborPath;
                currentCost = bestNeighborCost;
                improvement = true;
            } else {
                cout << "Iterasi " << iteration << ": STUCK / OPTIMAL." << endl;
                cout << "   Tetangga terbaik (" << (neighbors.empty() ? "N/A" : to_string(bestNeighborCost)) 
                     << ") tidak lebih baik dari cost saat ini (" << currentCost << ")." << endl;
            }
        }

        cout << "------------------------------------------------" << endl;
        cout << ">>> FINAL SOLUTION: ";
        for (size_t i = 0; i < currentPath.size(); ++i) {
            cout << currentPath[i] << (i == currentPath.size() - 1 ? "" : " -> ");
        }
        cout << "\nTotal Cost Akhir: " << currentCost << endl;
    }
};

int main() {
    srand(time(0)); 

    Graph g;
    
    // --- SKENARIO GRAF KOMPLEKS ---
    // Kita buat 3 "Jalur Besar" dari S ke G dengan kualitas berbeda.
    
    // 1. Jalur Atas (Sangat Mahal / Buruk) - Sering terpilih random karena banyak node
    g.add_edge('S','H', 20);
    g.add_edge('H','I', 15);
    g.add_edge('I','J', 15);
    g.add_edge('J','K', 20);
    g.add_edge('K','G', 20); // Total ~90

    // 2. Jalur Tengah (Sedang)
    g.add_edge('S','B', 15);
    g.add_edge('B','E', 20);
    g.add_edge('E','L', 15); // Node baru L
    g.add_edge('L','G', 15); // Total ~65
    
    // Konektor antar jalur (Agar bisa "lompat" jalur)
    g.add_edge('I','E', 5);  // Dari jalur buruk bisa pindah ke jalur sedang
    g.add_edge('J','L', 5);

    // 3. Jalur Bawah (Terbaik)
    g.add_edge('S','A', 10);
    g.add_edge('A','C', 10);
    g.add_edge('A','D', 5);  // Percabangan di A
    g.add_edge('C','F', 10);
    g.add_edge('D','F', 5);  // D lebih cepat ke F
    g.add_edge('F','G', 5);  // F sangat dekat ke G
    // Total S-A-D-F-G = 10+5+5+5 = 25 (Paling Optimal)

    // Konektor tambahan
    g.add_edge('B','C', 10); // Dari jalur tengah bisa pindah ke jalur bawah (C)

    cout << "=== SIMULASI LOCAL SEARCH HILL CLIMBING (COMPLEX GRAPH) ===" << endl;
    cout << "Mencoba mencari perbaikan rute secara iteratif..." << endl;
    
    // Kita jalankan loop beberapa kali untuk melihat variasi start random
    // Karena start-nya random, kadang bisa langsung dapat bagus, kadang dapat jelek.
    // User bisa menekan run ulang jika langsung dapat yang bagus.
    
    g.hillClimbingSearch('S', 'G');

    return 0;
}