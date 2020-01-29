package learnJava;

import java.util.*;
import java.io.*;

enum GameState
{
   PLAYING, DRAW, CROSS_WON, CIRCLE_WON
}

enum CellState
{ 
   EMPTY, CROSS, CIRCLE, NOTA
}

interface display
{
   public void show();
}

interface init
{
   public void initialize();
}

class Cell implements display
{
   CellState val;
   int r,c;
   public Cell(int r, int c)
   {
      this.r = r;
      this.c = c;
      val = CellState.EMPTY;
   }
   public void clear()
   {
        val = CellState.EMPTY;
      }
   public void show()
   {
      switch (val)
      {
         case CROSS:  System.out.print(" X "); break;
         case CIRCLE: System.out.print(" O "); break;
         case EMPTY:  System.out.print("   "); break;
      }
   }
}


class Board implements display,init
{
   public int R;
   public int C;
   Cell[][] cells;
   int currRow, currCol;
   public Board(int R, int C)
   {
      this.R = R;
      this.C = C;
      cells = new Cell[R][C];
      for(int i=0;i<R;i++)
      {
         for(int j=0;j<C;j++)
            cells[i][j] = new Cell(i,j);
      }
   }
   public void initialize()
   {
      for(int i=0;i<R;i++)
         for(int j=0;j<C;j++)
            cells[i][j].clear();
   }

   public boolean draw(int R, int C)
   {
      for (int i = 0; i < R; i++)
      {
         for (int j = 0; j < C; j++)
         {
            if (cells[i][j].val == CellState.EMPTY)
            {
               return false;
            }
         }
      }
      return true;
   }
   public boolean won(int x, int y, CellState s, int p)
   {
         x = x*p;
         y = y*p;
         int v=0;
         System.out.println(x+" "+y);
         for(int i=y;i<y+p;i++)
         {
            for(int j=x;j<x+p;j++)
            {    
               if(cells[j][i].val!=s){
                  v=1;
                  break;
               }
            }
            if(v==0)
               return true;
         }
         v=0;
         for(int i=x;i<x+p;i++)
         {
            for(int j=y;j<y+p;j++)
            {
               if(cells[i][j].val != s)      // row
               {
                  v=1;
                  break;
               }
            }
            if(v==0)
               return true;
         }
         v=0;
         for(int i=0;i<p;i++)
         {
            if(cells[x+i][y+i].val!=s)
            {
               v=1;
               break;
            }
         }
         if(v==0)
            return true;
         v=0;
         for(int i=0;i<p;i++)
         {
            if(cells[x+i][y+p-i-1].val!=s){
               v=1;
               break;
            }
         }
         if(v==0)
            return true;
         return false;
   }




   public boolean final_won(int x, int y, CellState s)
   {
      int v=0;
      for(int i=0;i<x;i++)
      {
         for(int j=0;j<y;j++)
         {
            if(cells[i][j].val != s)      // row
            {
               v=1;
               break;
            }
         }
         if(v==0)
            return true;
      }
      v=0;
      for(int i=0;i<x;i++)
      {
         for(int j=0;j<y;j++)
         {
            if(cells[j][i].val!=s){
               v=1;
               break;
            }
         }
         if(v==0)
            return true;
      }
      v=0;
      for(int i=0;i<x;i++)
      {
         if(cells[i][i].val!=s){
            v=1;
            break;
         }
      }
      if(v==0)
         return true;
      v=0;
      for(int i=0;i<x;i++)
      {
         if(cells[i][x-i-1].val!=s){
            v=1;
            break;
         }
      }
      if(v==0)
         return true;
      return false;
   }



   public void show()
   {
      for (int i = 0; i < R; i++)
      {
         for (int j = 0; j < C; j++)
         {
            cells[i][j].show(); 
            if (j < C - 1) System.out.print("|");
         }
         System.out.println();
         if (i < R - 1) {
            for(int j=0;j<C;j++)
            System.out.print("----");
         }
         System.out.println();
      }
   }
   public void fill_entire_grid(int x, int y, CellState c, int p)
   {
      for(int i=x;i<x+p;i++)
      {
         for(int j=y;j<y+p;j++)
         {
            if(cells[i][j].val==c.EMPTY)
               cells[i][j].val = c;
         }
      }
   }

}

class Game implements init
{
   public  int choice,R,C,size;
   private Board b;
   private GameState currState;
   private CellState currPlayer;
   private Board result;
      
   Scanner sc = new Scanner(System.in);

   public Game(int choice, int R, int C, int s)
   {
      this.R = R;
      this.C = C;
      this.choice = choice;
      this.size = s;
      b = new Board(R,C);
      result = new Board(R/s,C/s);
      b.show();
      result.show();
      initialize();

      // play game until the final state


      while(currState==GameState.PLAYING)
      {
            playerMove(currPlayer);
            b.show();        
            updateGame(currPlayer);
            int val = check_status(currState);
            if (val==1)
            {
               System.out.println("'X' won! Bye!");
            }
            else if (val==2)
            {
               System.out.println("'O' won! Bye!");
            }
            else if (val==3)
            {
               System.out.println("It's Draw! Bye!");
            }
            currPlayer = changePlayer(currPlayer);
         }
      }

      public void initialize()
      {
         b.initialize();
         result.initialize();
         currPlayer = CellState.CROSS;    
         currState = GameState.PLAYING;
      }
      //check board status
      public int check_status(GameState g)
      {
         if(g==GameState.CROSS_WON)
            return 1;
         if(g==GameState.CIRCLE_WON)
            return 2;
         if(g==GameState.DRAW)
            return 3;
         return 0;
      }

      //change current player
      public CellState changePlayer(CellState c)
      {
         if(c==CellState.CROSS)
            return CellState.CIRCLE;
         else
            return CellState.CROSS;
      }

      // move of the current player


   public void playerMove(CellState c)
   {
         boolean validInput = false;
         while(!validInput)
         {
            int row,col;
            System.out.print(choice);
            if (c == CellState.CROSS)
            {
               System.out.print("Chance of Player 'X', enter your move: ");
               row = sc.nextInt() - 1;
               col = sc.nextInt() - 1;
               if (row >= 0 && row < R && col >= 0 && col < C && b.cells[row][col].val == CellState.EMPTY)
               {
                  b.cells[row][col].val = c;
                  b.show();
                  b.cells[row][col].val = c.EMPTY;
               }
               System.out.print("Want to Undo the chance?: Y/N.   ");
               String s1 = sc.next();
               if(s1.equals("Y"))
               {
                  System.out.print("Chance of Player 'X', enter your move: ");
                  b.cells[row][col].val = c.EMPTY;
                  row = sc.nextInt()-1;
                  col = sc.nextInt()-1;
               }
               else
               {
                  System.out.println("Go Ahead!!");
               }
            }
            else 
            {
               if(choice==2)
               {
                  Random rn = new Random();
                  row = rn.nextInt((R - 1) + 1) + 1;
                  col = rn.nextInt((R - 1) + 1) + 1;
                  System.out.println("Chance of Computer, Move entered: "+row+" "+col);
               }
               else
               {
                  System.out.print("Chance of Player 'O', enter your move: ");
                  row = sc.nextInt() - 1;
                  col = sc.nextInt() - 1;
               }
            }
            int x = row/size;
            int y = col/size;
            if (row >= 0 && row < R && col >= 0 && col < C && b.cells[row][col].val == CellState.EMPTY)
            {
               b.cells[row][col].val = c;
               b.currRow = row;
               b.currCol = col;
               validInput = true;
               boolean p = b.won(x,y,c,size);
               if(p){
                  result.cells[x][y].val = c;
                  b.fill_entire_grid(x,y,c,size);
                  result.show();
                  // System.out.println("sdjnvbasdhjbvjasbcjvbashjbvjabsebesb");
                  // result.show();
                  
               }

            }
            else
            {
               System.out.println("Invalid move. Try Again!!! ");
            }
         }
    }

    public void updateGame(CellState c)
    {
      // System.out.println(result.final_won(R/3,C/3,CellState.CROSS));
      // System.out.println("dcvsvcsvcghsd");
      if (result.final_won(R/size,C/size,CellState.CROSS))
      {
         currState = GameState.CROSS_WON;
      }
      else if(result.final_won(R/size,C/size,CellState.CIRCLE))
      {
         currState = GameState.CIRCLE_WON;
      }
      else if (result.draw(R/size,C/size))
      { 
         currState = GameState.DRAW;
      }
   }
}

public class f4
{

   public static void main(String[] args)
   {
      int c=0,n,size;
      Scanner sc = new Scanner(System.in);
      System.out.println("Welcome to the world of Tic Tac Toe");
      System.out.println("Enter the grid dimension: ");
      n = sc.nextInt();
      System.out.println("Enter the base grid size: ");
      size = sc.nextInt();
      System.out.println("Note: Indexing for entering starts from 1 and goes till 3");
      System.out.println("Press 1 for Human vs Human: ");
      System.out.println("Press 2 for Computer vs Human: ");
      c = sc.nextInt();
      new Game(c,n,n,size);
   }
}
