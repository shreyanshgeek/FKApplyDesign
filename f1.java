package learnJava;

import java.util.*;
import java.io.*;

enum GameState
{
   PLAYING, DRAW, CROSS_WON, CIRCLE_WON
}

enum CellState
{ 
   EMPTY, CROSS, CIRCLE
}

interface display
{
	void show();
}

interface init
{
	void initialize();
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
	public static int R = 3;
	public static int C = 3;
	Cell[][] cells;
	int currRow, currCol;
	public Board()
	{
		cells = new Cell[R][C];
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
				cells[i][j] = new Cell(i,j);
		}
	}
	public void initialize()
	{
		for(int i=0;i<R;i++)
			for(int j=0;j<C;j++)
				cells[i][j].clear();
	}

	public boolean draw()
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
   public boolean won(CellState s)
   {
   		if(cells[currRow][0].val == s && cells[currRow][1].val == s && cells[currRow][2].val == s)		// row
   			return true;
   		if(cells[0][currCol].val == s && cells[1][currCol].val == s && cells[2][currCol].val == s)		//column
   			return true;
   		if(cells[0][0].val == s && cells[1][1].val == s && cells[2][2].val == s)						// left diagonal
   			return true;
   		if(cells[0][2].val == s && cells[1][1].val == s && cells[2][0].val == s)						// right diagonal
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
            System.out.println("-----------");
         }
      }
   }

}


class Game
{
	private Board b;
	private GameState currState;
   	private CellState currPlayer;
   	private int choice;
   	
   	Scanner sc = new Scanner(System.in);

	public Game(int choice)
	{
		this.choice = choice;
		b = new Board();
		b.show();
		//initialize();
   	}

}

public class f1
{
	public static void main(String[] args)
	{
		System.out.println("Welcome to the world of Tic Tac Toe");
		System.out.println("Note: Indexing for entering starts from 1 and goes till 3");
		System.out.println("Press 1 for Human vs Human: ");
		System.out.println("Press 2 for Computer vs Human: ");
		Scanner sc = new Scanner(System.in);
		int c = sc.nextInt();
		new Game(c);
	}
}