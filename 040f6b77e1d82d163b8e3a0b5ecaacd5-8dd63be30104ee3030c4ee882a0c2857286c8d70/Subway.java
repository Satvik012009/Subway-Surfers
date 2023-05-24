import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Subway {

	static ArrayList<Pair> adjList[];
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br;
		PrintWriter out = new PrintWriter(System.out);
		try{
			br = new BufferedReader(new FileReader("in.txt"));
		}
		catch(Exception e)
		{
			br = new BufferedReader(new InputStreamReader(System.in));
		}
		

		int T = Integer.parseInt(br.readLine());
		br.readLine();
		
		while(T-->0)
		{
			StringTokenizer st = new StringTokenizer(br.readLine());


			int xh = Integer.parseInt(st.nextToken());
			int yh = Integer.parseInt(st.nextToken());

			Point home = new Point(xh,yh);

			int xs = Integer.parseInt(st.nextToken());
			int ys = Integer.parseInt(st.nextToken());

			Point sch = new Point(xs,ys);

			ArrayList<Line> lines = new ArrayList<Line>();
			N = 2;


			while(true)
			{
				String line = br.readLine();

				if(line == null || line.trim().equals(""))
					break;

				st = new StringTokenizer(line);
				Line l = new Line();
				
				while(true)
				{
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					if(x == -1 && y == -1)
						break;
					l.stops.add(new Stop(new Point(x,y),N++));
				}
				lines.add(l);

			}

			adjList = new ArrayList[N];
			for(int i=0; i<N; i++)
				adjList[i] = new ArrayList<Pair>();

			adjList[0].add(new Pair(1,home.dist(sch)*0.006));
			adjList[1].add(new Pair(0,sch.dist(home)*0.006));


			// add edges between home/school and stops
			for(Line l : lines)
				for(Stop s : l.stops)
				{
					double distHome = home.dist(s.p)*0.006;
					double distSch = sch.dist(s.p)*0.006;

					adjList[0].add(new Pair(s.i,distHome));
					adjList[s.i].add(new Pair(0,distHome));
					adjList[1].add(new Pair(s.i,distSch));
					adjList[s.i].add(new Pair(1,distSch));
				}

			// add edges between stops of same line
			for(Line l : lines)
				for(int i=0; i<l.stops.size() - 1; i++)
				{
					Stop cur = l.stops.get(i);
					Stop nxt = l.stops.get(i+1);

					double dist = cur.p.dist(nxt.p)*0.0015;

					adjList[cur.i].add(new Pair(nxt.i,dist));
					adjList[nxt.i].add(new Pair(cur.i,dist));
				}

			// add edges between stops of different lines
			for(int i=0; i<lines.size(); i++)
				for(int j=i+1; j<lines.size(); j++)
					for(Stop s1 : lines.get(i).stops)
						for(Stop s2 : lines.get(j).stops)
						{
							double dist = s1.p.dist(s2.p)*0.006;
							adjList[s1.i].add(new Pair(s2.i,dist));
							adjList[s2.i].add(new Pair(s1.i,dist));

						}


			double secs = dijkstra(0, 1);

			out.println((int)(secs + 0.5));
			if(T > 0)
				out.println();


		}

		out.flush();

	}

	static int N;

	static double dijkstra(int s, int d)
	{
		PriorityQueue<Pair> q = new PriorityQueue<Pair>();
		double dist[] = new double[N];
		Arrays.fill(dist, 1e18);
		dist[s] = 0;

		q.add(new Pair(s,0));
		while(!q.isEmpty())
		{
			Pair p = q.remove();
			if(p.w > dist[p.v])
				continue;

			for(Pair v : adjList[p.v])
				if(dist[p.v] + v.w < dist[v.v])
				{
					dist[v.v] = dist[p.v] + v.w;
					q.add(new Pair(v.v, dist[v.v]));
				}

		}
		return dist[d];

	}

	static class Pair implements Comparable<Pair>{
		int v;
		double w;
		public Pair(int x, double y)
		{
			v = x;
			w = y;
		}

		public int compareTo(Pair p) {
			if(w > p.w)
				return 1;
			if(w < p.w)
				return -1;
			return 0;
		}

	}

	static class Point{
		int x;
		int y;
		public Point(int a, int b)
		{
			x = a; y = b;
		}

		double dist(Point p)
		{
			return Math.sqrt((long)(x - p.x) * (x - p.x) + (long)(y - p.y) * (y - p.y));
		}
	}
	static class Line{
		ArrayList<Stop> stops;
		public Line()
		{
			stops = new ArrayList<Stop>();
		}
	}

	static class Stop
	{
		Point p;
		int i;
		public Stop(Point x, int y)
		{
			p = x; i = y;
		}
	}
}
