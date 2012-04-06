import java.awt.*;

/** Represents a single square contained in Board
 * 
 * 
 * @author Matthew Proetsch
 * @version 0.9b
 */
@SuppressWarnings("serial")
public class Square extends Canvas {
    	
    /** The color that a square should be */
    enum BackgroundColor { DARKGRAY, LIGHTGRAY }

    
    /** The background color of this Square */
	private BackgroundColor bgColor;
    
    
    /** Whether or not this Square is occupied */
    private boolean occupied;
    
    
    /** The Piece that occupies this Square, may be NULL */
    private Piece occupant;

    
    
    /** The row of the game board that this square represents */
    private int row;
    
    /** The column of the game board that this square represents */
    private int col;

    
    /** Make a new Square with the specified background Board.Color
     *
     *  @param c        	The background color of this Square
     */
    public Square(BackgroundColor c, int myrow, int mycol) {
    	
    	this.setSize(64, 64);
    	
    	if(c == BackgroundColor.DARKGRAY)
    		this.setBackground(Color.DARK_GRAY);
    	else
    		this.setBackground(Color.LIGHT_GRAY);
    	
        bgColor = c;
        occupied = false;
        occupant = null;
        
        this.row = myrow;
        this.col = mycol;
        
    }

    
    

    /** Return whether or not this Square is occupied
     * 
     * @return 					Whether or not this Square is selected
     */
    public boolean isOccupied() {
        return this.occupied;
    }
    
    
    /** Get the row of the game board that this square represents
     * 
     * @return 			The row on the game board represented by this Square
     */
    public int getRow() {
    	return this.row;
    }
    
    /** Get the column of the game board that this square represents
     * 
     * @return 			The column on the game board represented by this Square
     */
    public int getCol() {
    	return this.col;
    }
    
    /** Get the background color of this Square */
    public Square.BackgroundColor getBackgroundColor() {
    	return this.bgColor;
    }
    
    /** Get the piece that occupies this Square
     * 
     * @return				The piece that occupies this Square, if any
     */
    public Piece getOccupant() {
    	if(this.isOccupied())
    		return this.occupant;
    	
    	return null;
    }
    
    
    
    /** Set whether or not this Square is highlighted
     * 
     * @param doHighlight 			Whether or not this square should be highlighted
     */
    public void setHighlight(boolean doHighlight) {
    	
    	
    	Graphics g = this.getGraphics();
    	g.setColor(Color.YELLOW);
    	
		if(doHighlight) {

	    	if(!this.isOccupied())
	    		//Draw a dotted oval where this piece may land
	    		for(int i = 0; i < 360; i+= 30)
	    			g.drawArc(7, 7, 49, 49, i, 15);
	    	
	    	else {
	    		//Draw a yellow rect around the border of this Square 
	    		
	    		g.draw3DRect(2, 2, 61, 61, false);
	    		
	    	}
    	}
    	else
    		super.update(this.getGraphics());
    	
    }


    /** Set the occupant of this Square
     *
     * @param visitor       The Piece that should now reside here
     */
    public void setOccupant(Piece visitor) {
    	if(visitor != null) {
    		
    		this.occupant = visitor;
    		this.occupied = true;
    		
    	}
    	
    	else {
    		
    		this.occupant = null;
    		this.occupied = false;
    		
    	}
    }
    
    
    
    
    
    @Override
	public void paint(Graphics g) {
		
		//Set the Canvas' background color equal to the Square's bgcolor
		if(this.getBackgroundColor() == Square.BackgroundColor.DARKGRAY) 
			this.setBackground(Color.DARK_GRAY);
		else
			this.setBackground(Color.LIGHT_GRAY);
		
		//Either draw a square or clear the rectangle
		if(this.isOccupied()) {
			
			Piece.Color pieceColor = occupant.getColor();
			
			if(pieceColor == Piece.Color.RED)
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);
			
			g.fillOval(7, 7, 49, 49);
		}
		
		else
			g.clearRect(0, 0, 64, 64);
			
	}
    

    
}
