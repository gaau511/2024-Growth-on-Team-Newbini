import sys
from collections import deque
input = sys.stdin.readline

#테케 개수
t = int(input())
for _ in range(t) :
    n , k = map(int, input().split())

    #n번 건물을 짓는데 소요되는 시간
    time = [0] + list(map(int,input().split()))

    #n번 건물을 짓기전 지어야 할 건물의 목록
    required = [[] for _ in range(n+1)]

    #n번 건물을 지은 후 지을 수 있는 건물의 목록
    order = [[] for _ in range(n+1)]

    #n번 건물이 지어졌는가
    built = [False] * (n+1)

    for i in range(k) :
        a,b = map(int, input().split())
        required[b].append(a)
        order[a].append(b)

    # 지어야 하는 건물
    target = int(input())


    #시작 건물 찾기
    for i in range(1,n+1) :
        if not required[i] :
            start = i

    q = deque()
    q.append(start)
    def build(a) :
        if built[a] == True :
            return time[a]
        
        if not required[a] :
            pass
        elif len(required[a]) == 1 :
            time[a] += build(required[a][0])
        else :
            maximum = -1
            for item in required[a] :
                maximum = max(maximum,build(item))
            time[a] += maximum

        global q
        q += deque(order[a])
        built[a] = True
        return time[a]

    while q :
        cur = q.popleft()
        build(cur)

    print(time[target])
        
