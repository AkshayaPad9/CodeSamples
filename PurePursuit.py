import math
import numpy as np

# Used for the simulation
SMALL_DISTANCE = 0.01
SMALL_ANGLE = 0.01


def init_paths(waypoints):
    """ Initializes paths from input waypoints """
    paths = []
    if len(waypoints) < 2:
        return paths
    else:
        for i in range(1, len(waypoints)):
            paths.append((waypoints[i-1], waypoints[i]))
        return paths

class Tank:
    def __init__(self, x, y, heading, waypoints):
        """
            x = starting x-coordinate, in m (+x points to the right)
            y = starting y-coordinate, in m (+y points upwards)
            heading = starting header, in radians (0 is +x axis, increases counterclockwise)
            waypoints = same format as the bike format - list of waypoints
            e.g. the path from 0,0 to 0,5 then to 7,6 would be passed in like this:
            [(0, 0), (0, 5), (7, 6)]
            """
        self.x = x
        self.y = y
        self.heading = heading
        self.waypoints = waypoints
        self.paths = init_paths(waypoints)
        self.previousPointIndex = 0;
	self.nextPointIndex = 1;
	self.lookAheadDistance = 8;
    
    def step(self, speed, yaw_dot):
        """Perform one simulation step of this tank"""
        self.x += SMALL_DISTANCE * speed * math.cos(self.heading)
        self.y += SMALL_DISTANCE * speed * math.sin(self.heading)
        
        self.heading += yaw_dot * SMALL_ANGLE
    
    def getClosestPoint(position):
        closestPoint = self.waypoints[0]
        distanceToClosestPoint=1000
        distance = 0
        coorClosest=[]
        for waypoint in self.waypoints:
            coor=[]
            for num in waypoint:
                coor.append(num)
                distance = math.sqrt((coor[0]-position[0])**2+(coor[1]-position[1])**2)
                if (distance<distanceToClosestPoint):
                    closestPoint = waypoint;
                    distanceToClosestPoint = distance;
                    coorClosest=coor;
        return coorClosest;


    def calcLookaheadPoint(self):
        if self.getDistance(self.waypoints[self.nextPointIndex]) < self.lookAheadDistance:
            self.previousPointIndex = self.nextPointIndex
            self.nextPointIndex += 1
            if self.nextPointIndex >= len(self.waypoints)-1:
                self.nextPointIndex = 0
            e=self.waypoints[self.previousPointIndex]
            l=self.waypoints[self.nextPointIndex]
            d=[l[0]-e[0],l[1]-e[1]]
            f=[e[0]-self.x,e[1]-self.y]
            a = (d[0]**2)+(d[1]**2)
            b = 2*((f[0]*d[0])+(f[1]*d[1]))
            c = ((f[0]**2)+(f[1]**2)) - (self.lookAheadDistance**2)
            discriminant = b*b - 4*a*c
	        t = 0;
            if (discriminant < 0):
                '''do nothing'''
            else:
                discriminant = math.sqrt(discriminant)
                t1 = (-b - discriminant)/(2*a)
                t2 = (-b + discriminant)/(2*a)
            if (t1 >= 0 and t1 <=1):
                t = t1
            if (t2 >= 0 and t2 <=1):
                t = t2
	    return [e[0]+t*d[0],e[1]+t*d[1]]

    def getDistance(self,position):
        d = math.sqrt( (self.x-position[0])**2 + (self.y-position[1])**2 )
        return d
    
    def get_x(self):
        lookaheadPoint = self.calcLookaheadPoint();
        angle =self.heading
        a=-math.tan(angle);
        b=1;
        c=math.tan(angle)*self.x-self.y;
        x=abs(a*lookaheadPoint[0]+b*lookaheadPoint[1]+c)/math.sqrt((a**2)+(b**2));
        return x;

    def get_nav_command(self):
        """Returns the pair (speed, yaw_dot)"""
        """Create look ahead points that are not the waypoints
            Find the distance between all the waypoints and determine the closest point
            """
        

        '''Find the curvature between the closestPoint and the vehicle'''
        distance = self.get_x()
        curvatureAngle = 2*distance/(self.lookAheadDistance**2);
        yaw_dot = curvatureAngle;
        speed = 1;
        
        return (speed,yaw_dot)




