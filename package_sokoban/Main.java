package package_sokoban;


import java.awt.event.KeyAdapter;


public class Main extends KeyAdapter{

    public static void main(String[] args) {
    	Box b= new Box(false);
    	Box c= new Box(false);
    	
        Wall m= new Wall();

        Player f = new Player(false);

        Vide v = new Vide(false);
        Vide v1 = new Vide(false);
        Vide v2 = new Vide(false);
        Vide v3 = new Vide(false);
        Vide v4 = new Vide(false);
        Vide v5 = new Vide(false);
        Vide v6 = new Vide(false);
        Vide v7 = new Vide(false);
        Vide v8 = new Vide(false);
        Vide v9 = new Vide(false);

        
        Vide k = new Vide(true);
        Vide j = new Vide(true);
        Vide x = new Vide(true);
        
        Element[][] tab={{m,m,m,m,m,m},
                {m,f,v1,v2,v3,m},
                {m,c,k,v4,v5,m},
                {m,v,v8,v7,v6,m},
                {m,j,x,b,v9,m},
                {m,m,m,m,m,m}};
        Matrice mat = new Matrice("test",'T',false, 6, tab, 1, 1,true,true,-1,-1);
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
    	for(i=0;i<=5;i++){
    		for(j=0;j<=5;j++){
        		System.out.print(mat.getElem(i, j).getSign());
        		
        	}
    		System.out.println();    	
    	}
    }
}