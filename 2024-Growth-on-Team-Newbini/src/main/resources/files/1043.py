#1043
#tags : dfs? bfs도 가능
#Solved : O
import sys
input = sys.stdin.readline

n , m = map(int, input().split())
truth = list(map(int,input().split()))[1:]

parties = []
participate = [[] for _ in range(n+1)]
for i in range(m) :
    parties.append(list(map(int,input().split()))[1:])

if not truth :
    print(m)
    quit()



"""
n번 파티에 거짓말을 할수 있는가? = canLie[n]
n번 사람을 조사한적 있는가? = visited[n]
진실을 아는 사람 리스트 = truth
n번 사람이 어떤 파티에 갔는가? = participate[n]


Step 1.
진실을 아는 사람을 모두 stack에 넣는다
ex stack = [1,2,3,4]

Step 2.
진실을 아는 사람이 갔던 파티에 간 사람도 모두 stack에 넣는다
단, 중복된 사람은 넣지 않는다.
진실을 아는 사람이 갔던 파티는 거짓말을 할 수 없는 파티이다.
ex [1,2,3] 4번이 간 0번 파티에 7,8번도 감 -> [1,2,3,7,8]
    visited[4] = True
    canGo[0] = False


"""
for i in range(m) :
    for person in parties[i] :
        participate[person].append(i)

canLie = [True] * m
visited = [False] * (n+1) 
stack = []

stack += truth
for i in truth :
    visited[i] = True

while stack :
    target = stack.pop()

    for party in participate[target] :
        canLie[party] = False
        for person in parties[party] :
            if visited[person] == False :
                stack.append(person)
                visited[person] = True

cnt = 0
for x in canLie :
    if x == True :
        cnt += 1

print(cnt)
    




    




