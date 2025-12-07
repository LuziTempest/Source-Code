#include <bits/stdc++.h>
#define ll long long
#define pb push_back
#define ub upper_bound
#define lb lower_bound
using namespace std;

void print(int arr[], int n){
    int temp = -1;
    sort(arr,arr+n);
    cout<<"Daftar pendaftar: ";
    for (int i = 0 ; i < n ; i++){
        if (temp != arr[i]){
            cout<<arr[i]<<" ";
            temp = arr[i];
        }
    }
}

int main (){
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    int n; cin>>n;
    int arr[n+1];
    for (int i = 0 ; i < n ; i++) cin>>arr[i];

    int x; cin>>x;
    for (int i = 0 ; i < n ; i++){
        if (arr[i] == x){
            cout<<"Pendaftaran ditemukan"<<endl;
            print(arr,n);
            return 0;
        }
    }
    cout<<"Pendaftaran tidak ditemukan"<<endl;
    print(arr,n);
    return 0;
}
