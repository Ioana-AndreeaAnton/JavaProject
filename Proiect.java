import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.util.*;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static java.lang.Math.acos;
import static java.lang.Math.sin;




public class Proiect extends Applet{
	
	
	
	
	Point[] c;//punctele de control
	int nrPuncte;//numarul de puncte de control
	int cX;
	int cY;
	int r;
	boolean nrMediane=false;
	boolean nrBisectoare=false;
	boolean nrInaltimi=false;
	Image im;
	Graphics img;
	double k=0.25;//pasul de timp folosit la desenarea triunghiului
	int moveflag=4;//indica punctul de control deplasat
	Button restart;
	Button mediane;
	Button inaltimi;
	Button bisectoare;
	
	public void init(){
		c=new Point[3];
		im=createImage(size().width, size().height);
		img=im.getGraphics();
		restart=new Button("Restart");
		add(restart);
		mediane=new Button("Mediane");
		add(mediane);
		inaltimi=new Button("Inaltimi");
		add(inaltimi);
		bisectoare=new Button("Bisectoare");
	    add(bisectoare);
	}
	
	public void update(Graphics g){paint(g);}
	
	public void paint(Graphics g){
		setBackground(Color.gray);
		img.setColor(Color.black);
		img.clearRect(0,0,size().width, size().height);
		//desenam punctele de control
		for(int i=0;i<nrPuncte;i++){
			img.setColor(Color.yellow);
			img.fillOval(c[i].x-3,c[i].y-3,8,8);
			img.setColor(Color.black);
			if(nrPuncte>1 && i<(nrPuncte-1)) img.drawLine(c[i].x,c[i].y,c[i+1].x,c[i+1].y);
		}
		if(nrPuncte==3){
			img.drawLine(c[0].x,c[0].y,c[2].x,c[2].y);
			Point O=circumcenter(c[0],c[1],c[2]);
			r=(int)raza(c[0],c[1],c[2]);
			img.setColor(Color.blue);
			img.drawOval(O.x-r,O.y-r,r*2,r*2);
			//img.setColor(Color.red);
			//img.fillOval(O.x,O.y,8,8);
			if(nrMediane){
			desenMediane(c[0],c[1],c[2]);
			}
			if(nrBisectoare){
			desenBisectoare(c[0],c[1],c[2]);
			}
			if(nrInaltimi){
				desenInaltimi(c[0],c[1],c[2]);
			}
		}

		g.drawImage(im,0,0,this);
	}
	
	static double distanta(Point p1, Point p2){
		int x=p1.x-p2.x;
		int y=p1.y-p2.y;
	    return sqrt(x*x+y*y); //returnam distanta dintre doua puncte
	}
	
	public double panta(Point A, Point B){
		double M=(B.y-A.y)/(B.x-A.x);
		return M;
	}
	
	
	public double raza(Point A, Point B, Point C){
		Point O=circumcenter(A,B,C);
		double r=distanta(O,C);
		return r;
	
	}
	
	public Point mijloc(Point A, Point B){
		double x=(A.x+B.x)/2;
		double y=(A.y+B.y)/2;
		return new Point((int)Math.round(x),(int)Math.round(y));
	}
		
	public void desenMediane(Point A, Point B, Point C){
		Point G=new Point();
		Point M= mijloc(A,B);
		Point N= mijloc (A,C);
		Point P= mijloc (B,C);
		img.setColor(Color.gray);
		img.drawLine(M.x,M.y,C.x,C.y);
		img.drawLine(N.x,N.y,B.x,B.y);
		img.drawLine(P.x,P.y,A.x,A.y);
		img.setColor(Color.yellow.darker());
		img.fillOval(M.x-3,M.y-3,8,8);
		img.fillOval(N.x-3,N.y-3,8,8);
		img.fillOval(P.x-3,P.y-3,8,8);
		G.x=(A.x+B.x+C.x)/3;
		G.y=(A.y+B.y+C.y)/3;
		img.setColor(Color.red);
		img.fillOval(G.x-3,G.y-3,8,8);
	}
	
	public void desenBisectoare(Point A, Point B, Point C){
		//bisectoarea AM
		Point M=new Point();
		double k1=distanta(A,B)/distanta(A,C);
		double xM=(B.x+k1*C.x)/(1+k1);
	    double yM=(B.y+k1*C.y)/(1+k1);
		M.x=(int)Math.round(xM);
		M.y=(int)Math.round(yM);
		// desenam bisectoarea AM
		img.setColor(Color.gray);
		img.drawLine(M.x,M.y,A.x,A.y);
		img.setColor(Color.yellow.darker());
		img.fillOval(M.x-3,M.y-3,8,8);
		
		
		//bisectoarea BE
		Point E=new Point();
		double k2=distanta(B,A)/distanta(B,C);
		double xE=(A.x+k2*C.x)/(1+k2);
		double yE=(A.y+k2*C.y)/(1+k2);
		E.x=(int)Math.round(xE);
		E.y=(int)Math.round(yE);
		//desenam bisectoarea BE
		img.setColor(Color.gray);
		img.drawLine(E.x,E.y,B.x,B.y);
		img.setColor(Color.yellow.darker());
		img.fillOval(E.x-3,E.y-3,8,8);
		
		
		//bisectoarea CP
		Point P=new Point();
		double k3=distanta(C,A)/distanta(C,B);
		double xP=(A.x+k3*B.x)/(1+k3);
		double yP=(A.y+k3*B.y)/(1+k3);
		P.x=(int)Math.round(xP);
		P.y=(int)Math.round(yP);
		//desenam bisectoarea CP
		img.setColor(Color.gray);
		img.drawLine(P.x,P.y,C.x,C.y);
		img.setColor(Color.yellow.darker());
		img.fillOval(P.x-3,P.y-3,8,8);
		
		
		//centrul cercului inscris
		double a=distanta(B,C);
		double b=distanta(A,C);
		double c=distanta(A,B);
		Point I=new Point();
		double xI=(a*A.x+b*B.x+c*C.x)/(a+b+c);
		double yI=(a*A.y+b*B.y+c*C.y)/(a+b+c);
		I.x=(int)Math.round(xI);
		I.y=(int)Math.round(yI);
		img.setColor(Color.red);
		img.fillOval(I.x-3,I.y-3,8,8);
		
		
	}
	
	public void desenInaltimi(Point A, Point B, Point C){
		
		/*
		//Inaltimea AM
		Point M=new Point();
		M.x=(((C.x-B.x)/(C.y-B.y))*A.x+((C.y-B.y)/(C.x-B.x))*B.x+A.y-B.y)/(((C.x-B.x)/(C.y-B.y))+((C.y-B.y)/(C.x-B.x)));
		M.y=(((C.y-B.y)/(C.x-B.x))*A.y+((C.x-B.x)/(C.y-B.y))*B.y+A.x-B.x)/(((C.x-B.x)/(C.y-B.y))+((C.y-B.y)/(C.x-B.x)));
		//M.x=(int)Math.round(xM);
		//M.y=(int)Math.round(yM);
		img.setColor(Color.orange);
		img.fillOval(M.x-3,M.y-3,8,8);
		img.setColor(Color.gray);
		img.drawLine(M.x,M.y,A.x,A.y);
	
	
	    //Inaltimea BN
	    Point N=new Point();
		N.x=(((C.x-A.x)/(C.y-A.y))*B.x+((C.y-A.y)/(C.x-A.x))*A.x+B.y-A.y)/(((C.x-A.x)/(C.y-A.y))+((C.y-A.y)/(C.x-A.x)));
		N.y=(((C.x-A.x)/(C.y-A.y))*A.y+((C.y-A.y)/(C.x-A.x))*B.y+B.x-A.x)/(((C.x-A.x)/(C.y-A.y))+((C.y-A.y)/(C.x-A.x)));
		img.setColor(Color.orange);
		img.fillOval(N.x-3,N.y-3,8,8);
	    */
		
		//Vom incerca cu proiectia unui punct pe o dreapta
		//stim ecuatia dreptei si coordonatele punctului proiectat
		//(x-xA)/a=(y-yA)/b=-(a*xA+b*yA+c)/(a*a+b*b)
		
		//AM
		double a1=C.y-B.y;
		double b1=B.x-C.x;
		double c1=B.x*(B.y-C.y)-B.y*(B.x-C.x);
		double d1=-1*(a1*A.x+b1*A.y+c1)/(a1*a1+b1*b1);
		Point M=new Point((int)Math.round(d1*a1+A.x),(int)Math.round(d1*b1+A.y));
		img.setColor(Color.gray);
		img.drawLine(A.x,A.y,M.x,M.y);
		img.setColor(Color.yellow.darker());
		img.fillOval(M.x-3,M.y-3,8,8);
		
		
		//BN
		double a2=A.y-C.y;
		double b2=C.x-A.x;
		double c2=C.x*(C.y-A.y)-C.y*(C.x-A.x);
		double d2=-1*(a2*B.x+b2*B.y+c2)/(a2*a2+b2*b2);
		Point N=new Point((int)Math.round(d2*a2+B.x),(int)Math.round(d2*b2+B.y));
		img.setColor(Color.gray);
		img.drawLine(B.x,B.y,N.x,N.y);
		img.setColor(Color.yellow.darker());
		img.fillOval(N.x-3,N.y-3,8,8);
		
		
		//CQ
		double a3=B.y-A.y;
		double b3=A.x-B.x;
		double c3=A.x*(A.y-B.y)-A.y*(A.x-B.x);
		double d3=-1*(a3*C.x+b3*C.y+c3)/(a3*a3+b3*b3);
		Point Q=new Point((int)Math.round(d3*a3+C.x),(int)Math.round(d3*b3+C.y));
		img.setColor(Color.gray);
		img.drawLine(C.x,C.y,Q.x,Q.y);
		img.setColor(Color.yellow.darker());
		img.fillOval(Q.x-3,Q.y-3,8,8);
		
		//ortocentrul
		Point H=new Point();
		H.x=(A.y*A.y*(C.y-B.y)+B.x*C.x*(C.y-B.y)+B.y*B.y*(A.y-C.y)+A.x*C.x*(A.y-C.y)+C.y*C.y*(B.y-A.y)+A.x*B.x*(B.y-A.y))/(A.x*(B.y-C.y)+B.x*(C.y-A.y)+C.x*(A.y-B.y));
		H.y=(A.x*A.x*(B.x-C.x)+B.y*C.y*(B.x-C.x)+B.x*B.x*(C.x-A.x)+A.y*C.y*(C.x-A.x)+C.x*C.x*(A.x-B.x)+A.y*B.y*(A.x-B.x))/(A.y*(C.x-B.x)+B.y*(A.x-C.x)+C.y*(B.x-A.x));
		img.setColor(Color.red);
		img.fillOval(H.x-3,H.y-3,8,8);
		
	
	}
	
	
	
	
	/*public Point circumcenter(Point A, Point B, Point C){
		//determinam mijloacele laturilor
		//este suficient sa determinam doar doua mijloacele
		
		double x1=(A.x+B.x)/2; //prima coordonata a mijlocului lui AB
		double y1=(A.y+B.y)/2; //a doua coordonata a mijlocului lui AB
		double x2=(B.x+C.x)/2; //prima coodronata a mijlocului lui BC
		double y2=(B.y+C.y)/2; //a doua coordonaya a mijlocului lui BC
		
		//calculam panta mediatoarelor lui AB si BC
		//stim ca produsul pantelor a doua drepte perpendiculare este -1
		
		double pantaMedAB=-((B.x-A.x)/(B.y-A.y));
		double pantaMedBC=-((C.x-B.x)/(C.y-B.y));
		
		//ecuatia prin punct si panta
		//y=mx+n
		double nMedAB=y1-pantaMedAB*x1;
		double nMedBC=y2-pantaMedBC*x2;
		
		//cautam x si y facand sistem
		double x=(nMedAB-nMedBC)/(pantaMedBC-pantaMedAB);
		
		return new Point((int)Math.round(x),(int)Math.round((pantaMedAB*x)+nMedAB));
		//nu prea functioneaza
		//revin mai tarziu aici
	}
	*/
	public Point circumcenter(Point A, Point B, Point C){
		Point M=mijloc(B,C);
		Point N=mijloc(A,C);
		double x=((C.x-A.x)*(C.y-B.y)*N.x+(C.y-A.y)*(C.y-B.y)*N.y-(C.x-B.x)*(C.y-A.y)*M.x-(C.y-B.y)*(C.y-A.y)*M.y)/((C.x-A.x)*(C.y-B.y)-(C.x-B.x)*(C.y-A.y));
		double y=((C.x-B.x)*(C.x-A.x)*N.x+(C.y-A.y)*(C.x-B.x)*N.y-(C.x-B.x)*(C.x-A.x)*M.x-(C.y-B.y)*(C.x-A.x)*M.y)/((C.y-A.y)*(C.x-B.x)-(C.y-B.y)*(C.x-A.x));
		
		return new Point((int)Math.round(x),(int)Math.round(y));
	}
		
	
	
	
	public boolean action(Event e, Object o){
		if(e.target==restart){
			nrPuncte=0;
			repaint();
		    return true;
		}
		if(e.target==mediane){
			if(nrMediane){
			nrMediane=false;
			}
			else{
				nrMediane=true;
				nrBisectoare=false;
				nrInaltimi=false;
			}
			repaint();
		}
		if(e.target==bisectoare){
			if(nrBisectoare){
				nrBisectoare=false;
			}
			else{
				nrBisectoare=true;
				nrMediane=false;
				nrInaltimi=false;
			}
			repaint();
		}
		if(e.target==inaltimi){
			if(nrInaltimi){
				nrInaltimi=false;
			}
			else{
				nrInaltimi=true;
				nrMediane=false;
				nrBisectoare=false;
			}
			repaint();
		}
		
		return false;
	}
	public boolean mouseDown(Event e, int x, int y){
	//3 parametri: evenimentul in sine, doua coordonate x si y unde evenimentul are loc
	Point p=new Point(x,y);
	if(nrPuncte<3){
        c[nrPuncte]=p;
	    nrPuncte++;
	repaint();
	}
	else{
		if(Math.abs(distanta(p,circumcenter(c[1],c[2],c[0]))-raza(c[1],c[2],c[0]))<3){
	         moveflag=4;
		}
		
		for(int i=0;i<nrPuncte;i++)
			//for(int j=-3;j<4;j++) for(int l=-3;l<4;l++)
				//if(p.equals(new Point1(c[i].x+j,c[i].y+l))) {
					if(Math.abs(Math.pow((c[i].x - x) * (c[i].x - x) + (c[i].y - y) * (c[i].y - y), 0.5)) <4 ){//distanta de la coordonatele centrului la coordonatele cursorului 
					moveflag=i;}
	}
	

return true;
	}
	


public boolean mouseDrag(Event evt, int x, int y){
	
		
	Point p=new Point(x,y);
	//pentru a misca triunghiul pe cerc
	if(moveflag<nrPuncte){
		r=(int)Math.round(raza(c[1],c[2],c[0]));
		
		double d=distanta(circumcenter(c[1],c[2],c[0]),p);//p.x, p.y coordonatele cursorului
		
		int xx = (int)(Math.round((x - circumcenter(c[1],c[2],c[0]).x) * (double)r /(double)d  + circumcenter(c[1],c[2],c[0]).x));
		int yy = (int)(Math.round((y - circumcenter(c[1],c[2],c[0]).y) * (double)r /(double)d  + circumcenter(c[1],c[2],c[0]).y));
			
			c[moveflag].x = xx;
			c[moveflag].y = yy;
			repaint();
		
		
		}
	
	//pentru a mari sau micsora cercul
	//pentru a mari cercul, marim doar triunghiul si redesenam cercul cu coordonatele noi
	//centrul cercului ramane acelasi, doar raza se modifica
	//centrul il mutam in origine, adica din coordonatele unui varf scadem coordonatele centrului
	//inmultim coordonatele cu cele ale cursorului
	//il mutam inapoi
	
	if(moveflag==4){
	
		cX=circumcenter(c[1],c[2],c[0]).x;
		cY=circumcenter(c[1],c[2],c[0]).y;
		r=(int)Math.round(raza(c[1],c[2],c[0]));
	
	


		double d=distanta(circumcenter(c[1],c[2],c[0]),p)/r;//distanta de la centru la coordonatele cursorului pe  care o impartim cu raza
		for(int i=0;i<3;i++){
			c[i].x=(int)Math.round((c[i].x-cX)*d+cX);//din coordonatele unui varf scadem coordonatele centrului, inmultim cu lungimea noua adica d si ducem centrul inapoi unde era 
			c[i].y=(int)Math.round((c[i].y-cY)*d+cY);
			
			//c[i].x=(int)((c[i].x-cX)*d+cX);
			//c[i].y=(int)((c[i].y-cY)*d+cY);
		}
		r=(int)Math.round(raza(c[1],c[2],c[0]));
		repaint();
	}
	
	
return true;
}

public boolean mouseUp(Event evt, int x, int y){moveflag=5;return true;}

	
}
