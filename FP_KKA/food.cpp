#include <bits/stdc++.h>
using namespace std;

struct Food {
    int Kalori;
    int Protein;
};

struct Solution {
    map<string, int> qty;
    double fitness;
    int total_cal;
    int total_prot;
};

int calculate_bmr(string gender, double weight, double height, int age, string activity_level, string goal) {
    transform(gender.begin(), gender.end(), gender.begin(), ::tolower);
    double bmr;

    if (gender == "female" || gender == "f")
        bmr = (weight * 10) + (height * 6.25) - (age * 5) - 161;
    else if (gender == "male" || gender == "m")
        bmr = (weight * 10) + (height * 6.25) - (age * 5) + 5;
    else
        return -1;

    if (goal == "cutting")
        bmr *= 0.8;
    else if (goal == "maintenance")
        bmr *= 1.0;
    else if (goal == "bulking")
        bmr *= 1.2;

    if (activity_level == "sedentary")
        return int(bmr * 1.2);
    else if (activity_level == "lightly_active")
        return int(bmr * 1.375);
    else if (activity_level == "moderately_active")
        return int(bmr * 1.55);
    else if (activity_level == "very_active")
        return int(bmr * 1.725);
    else if (activity_level == "super_active")
        return int(bmr * 1.9);
    else
        return -1;
}

Solution evaluate_solution(map<string, int> &qty, map<string, Food> &foods, int target_cal, double protein_need) {
    int total_cal = 0;
    int total_prot = 0;
    for (auto &[name, val] : qty) {
        total_cal += val * foods[name].Kalori;
        total_prot += val * foods[name].Protein;
    }

    double penalty = abs(target_cal - total_cal);
    if (total_prot < protein_need) penalty += (protein_need - total_prot) * 5;

    double fitness = 1.0 / (1.0 + penalty); 
    return {qty, fitness, total_cal, total_prot};
}


Solution local_search(map<string, Food> &foods, int target_cal, double protein_need, int max_iter = 5000) {
    vector<string> names;
    for (auto &p : foods) names.push_back(p.first);

    map<string, int> current_qty;
    for (auto &name : names)
        current_qty[name] = rand() % 3;

    Solution current = evaluate_solution(current_qty, foods, target_cal, protein_need);
    Solution best = current;

    for (int iter = 0; iter < max_iter; iter++) {
        string chosen = names[rand() % names.size()];
        int old_qty = current_qty[chosen];

        int change = (rand() % 2 ? 1 : -1);
        current_qty[chosen] = max(0, old_qty + change);

        Solution neighbor = evaluate_solution(current_qty, foods, target_cal, protein_need);

        if (neighbor.fitness > current.fitness) {
            current = neighbor;
            if (neighbor.fitness > best.fitness)
                best = neighbor;
        } else {
            double prob = exp((neighbor.fitness - current.fitness) * 50);
            if (((double)rand() / RAND_MAX) < prob)
                current = neighbor;
            else
                current_qty[chosen] = old_qty; 
        }
    }
    return best;
}

tuple<map<string, map<string, int>>, int, int>
food_recommendation(int bmr, double weight,
                    map<string, Food> pagi,
                    map<string, Food> siang,
                    map<string, Food> malam)
{
    int bmr_part = bmr / 3;
    double protein_need = weight * 1.5;
    double protein_part = protein_need / 3.0;

    vector<string> meals = {"Pagi", "Siang", "Malam"};
    vector<map<string, Food>> meal_foods = {pagi, siang, malam};
    map<string, map<string, int>> meal_plan;
    int total_calories = 0;
    int total_protein = 0;

    for (int i = 0; i < 3; i++) {
        Solution best = local_search(meal_foods[i], bmr_part, protein_part); 
        meal_plan[meals[i]] = best.qty;
        total_calories += best.total_cal;
        total_protein += best.total_prot;
    }

    return {meal_plan, total_calories, total_protein};
}


int main() {
    srand(time(0)); 

    map<string, Food> food_dict_pagi = {
        {"Nasi", {200, 2}},
        {"Roti", {150, 6}},
        {"Telur", {70, 6}},
        {"Oatmeal", {150, 5}},
        {"WheyProtein", {120, 20}},
        {"Susu Full Cream", {61, 3}},
    };
    map<string, Food> food_dict_siang = {
        {"Tahu", {50, 8}},
        {"Tempe", {150, 12}},
        {"Nasi", {200, 2}},
        {"Mie Goreng", {200, 8}},
        {"Dada Ayam Goreng", {250, 25}},
        {"Sandwich", {180, 7}},
    };

    map<string, Food> food_dict_malam = {
        {"Nasi", {200, 2}},
        {"Ikan Bakar", {250, 22}},
        {"Sate Ayam", {180, 25}},
        {"Sup Ayam", {150, 15}},
        {"Brokoli Rebus", {50, 3}},
    };

    string gender, activity_level, goal;
    double weight, height;
    int age;

    cout << "Masukkan jenis kelamin (male/female): ";
    cin >> gender;
    cout << "Masukkan berat badan (kg): ";
    cin >> weight;
    cout << "Masukkan tinggi badan (cm): ";
    cin >> height;
    cout << "Masukkan umur: ";
    cin >> age;
    cout << "Masukkan tingkat aktivitas (sedentary/lightly_active/moderately_active/very_active/super_active): ";
    cin >> activity_level;
    cout << "Masukkan goal (cutting/maintenance/bulking): ";
    cin >> goal;

    int bmr = calculate_bmr(gender, weight, height, age, activity_level, goal);

    if (bmr == -1) {
        cout << "Input tidak valid!\n";
        return 0;
    }

    cout << "\nTotal kebutuhan kalori harian: " << bmr << " kcal\n\n";

    auto start = chrono::high_resolution_clock::now();

    auto [meal_plan, total_cal, total_prot] = food_recommendation(bmr, weight, food_dict_pagi, food_dict_siang, food_dict_malam);

    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> duration = end - start;

    for (auto &[meal, foods] : meal_plan) {
        cout << "=== " << meal << " ===\n";
        for (auto &[name, qty] : foods)
            if (qty > 0)
                cout << name << " x" << qty << "\n";
        cout << "\n";
    }

    cout << "Total kalori: " << total_cal << " kcal\n";
    cout << "Total protein: " << total_prot << " gram\n";
    
    cout << "\n----------------------------------------\n";
    cout << "Waktu eksekusi algoritma: " << duration.count() << " ms\n";
    cout << "----------------------------------------\n";

    return 0;
}