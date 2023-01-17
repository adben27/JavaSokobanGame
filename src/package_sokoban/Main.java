package package_sokoban;


import java.awt.event.KeyAdapter;


public class Main extends KeyAdapter{

    Matrice mat;
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
        Matrice mat = new Matrice("test", 4, tab, 1, 1);
        Main teste = new Main(mat);
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        System.out.println(mat.getPos_x()+","+mat.getPos_y());
        teste.mat.move();
        
    }


    public Main(Matrice mat) {
        this.mat = mat;
    }


    public void move_up(){
        this.mat.move_up();
    }
    public void move_down(){
        this.mat.move_down();
    }
    public void move_right(){
        this.mat.move_right();
    }
    public void move_left(){
        this.mat.move_left();
    }

}