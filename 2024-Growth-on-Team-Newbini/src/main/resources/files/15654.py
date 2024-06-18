n , m = map(int, input().split())
n_list = list(map(int, input().split()))

def dfs (i,fin,size) :
    # dfs(i, n, m) = [i] + dfs(i+1, n, m-1) + [i+1] + dfs(i+2,n,m-1) + [i+2] + dfs(i+3,n,m-1) ...
    # return point = [i+k] + dfs(i+k+1, n, m-1) 에서 n-(i+ k) == m-1이 되는 순간 k = n-i-m+1
    # ex) dp(2,6,3) = [2] + dp(3,6,2) + [3] dp(4,6,2) + [4] dp(5,6,2)

    for k in range(0,fin-i-size+2) :
        temp = [i+k] + dfs(i+k+1, fin, size-1)

    







    


