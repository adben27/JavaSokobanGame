package package_sokoban;


import java.awt.event.KeyAdapter;


public class Main extends KeyAdapter{

    public static void main(String[] args) {
        Wall a= new Wall();
        Wall b= new Wall();
        Wall c= new Wall();
        Wall d= new Wall();
        Wall e= new Wall();
        Wall h= new Wall();
        Wall i= new Wall();
        Wall l= new Wall();
        Wall m= new Wall();
        Wall n= new Wall();
        Wall o= new Wall();
        Wall p= new Wall();

        Player f = new Player(false);

        Vide j = new Vide(false);
        Vide g = new Vide(false);
        Vide k = new Vide(true);
        Element[][] tab={{a,b,c,d},{e,f,g,h},{i,j,k,l},{m,n,o,p}};
        Matrice mat = new Matrice("test",'T',false, 4, tab, 1, 1);
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        print_mat(mat);
        mat.move();
        
        
    }

    public static void print_mat(Matrice mat) {/*petit test pour une matrice 4x4*/
    	int i,j;
    	for(i=0;i<=3;i++){
    		for(j=0;j<=3;j++){
        		System.out.print(mat.getElem(i, j).getSign());
        		
        	}
    		System.out.println();    	
    	}
    }
}