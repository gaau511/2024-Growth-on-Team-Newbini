# https://www.acmicpc.net/problem/1167
# tags : tree, dfs, 트리의 지름
# solved : X (Get idea from others)

import sys
from collections import deque
input = sys.stdin.readline
sys.setrecursionlimit(100000)

v = int(input())
tree = [deque() for _ in range(v+1)]
tree2 = [deque() for _ in range(v+1)]
stack = []
visited = [False] * (v+1)
dist_from_one = [0] * (v+1)

idx = 0
for i in range(1,v+1) :
    temp = list(map(int,input().split()))
    idx = temp[0]
    for j in range(1,len(temp)-1,2) :
        tree[idx].append((temp[j], temp[j+1]))
        tree2[idx].append((temp[j], temp[j+1]))

# node 1에서 가장 먼 node 찾기
visited[1] = True
stack.append(1)

while stack :
    node = stack[len(stack)-1]
    if tree[node] :
        child, weight = tree[node].popleft()
        if visited[child] == False :
            visited[child] = True
            stack.append(child)
            dist_from_one[child] = dist_from_one[node] + weight
    else :
        stack.pop()

max = 0
max_idx = 0
for i in range(2,v+1) :
    if dist_from_one[i] > max :
        max = dist_from_one[i]
        max_idx = i

dist_from_max = [0] * (v+1)
visited = [False] * (v+1)

visited[max_idx] = True
stack.append(max_idx)

while stack :
    node = stack[len(stack)-1]
    if tree2[node] :
        child, weight = tree2[node].popleft()
        if visited[child] == False :
            visited[child] = True
            stack.append(child)
            dist_from_max[child] = dist_from_max[node] + weight
    else :
        stack.pop()

max2 = 0
max_idx2 = 0
for i in range(1,v+1) :
    if dist_from_max[i] > max2 :
        max2 = dist_from_max[i]
        max_idx2 = i

print(max2)
        


