import heapq as hq
import sys
class Point(object):
    def __init__(self, x, y):
        self.X = x
        self.Y = y
    def __lt__(self, other):
        self_mag = (self.X ** 2) + (self.X ** 2)
        other_mag = (other.Y ** 2) + (other.Y ** 2)
        return self_mag < other_mag
    def distance(point1, point2):
        dx = abs(point1.X - point2.X)
        dy = abs(point1.Y-point2.Y)
        return dx+dy

numPoints = 0
points = []
vertex = Point(0,0)
run = 0
weight = 0
for line in sys.stdin:
    input = line.split()
    if len(input)==1:
        numPoints = int(input[0])
    elif (run == 0):
        vertex = Point(int(input[0]), int(input[1]))
        run= run+1
    else:
        point = Point(int(input[0]), int(input[1]))
        points.append((Point.distance(vertex, point),point))
        if (len(points)==numPoints-1):
            break
hq.heapify(points)
while(len(points)>0):
    vertex = hq.heappop(points)
    weight=weight+vertex[0]
    for i in range(len(points)):
        weight2 = Point.distance(vertex[1],points[i][1])
        if (not(weight2>points[i][0])):
            points[i]=(Point.distance(vertex[1],points[i][1]),points[i][1])
    hq.heapify(points)
    
print(weight)
