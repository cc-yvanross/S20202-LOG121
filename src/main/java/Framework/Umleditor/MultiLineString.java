package Framework.Umleditor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.StringTokenizer;

/**
   A string that can extend over multiple lines.
*/
public class MultiLineString implements Cloneable, Serializable
{
   /**
    *
    */
   private static final long serialVersionUID = 8925953216238369077L;

   /**
    * Constructs an empty, centered, normal size multiline string that is not
    * underlined.
    */
   public MultiLineString() 
   { 
      text = ""; 
      justification = CENTER;
      size = NORMAL;
      underlined = false;
   }
   /**
      Sets the value of the text property.
      @param newValue the text of the multiline string
   */
   public void setText(String newValue) { text = newValue; }
   /**
      Gets the value of the text property.
      @return the text of the multiline string
   */
   public String getText() { return text; }
   /**
      Sets the value of the justification property.
      @param newValue the justification, one of LEFT, CENTER, 
      RIGHT
   */
   public void setJustification(int newValue) { justification = newValue; }
   /**
      Gets the value of the justification property.
      @return the justification, one of LEFT, CENTER, 
      RIGHT
   */
   public int getJustification() { return justification; }
   /**
      Gets the value of the underlined property.
      @return true if the text is underlined
   */
   public boolean isUnderlined() { return underlined; }
   /**
      Sets the value of the underlined property.
      @param newValue true to underline the text
   */
   public void setUnderlined(boolean newValue) { underlined = newValue; }
   /**
      Sets the value of the size property.
      @param newValue the size, one of SMALL, NORMAL, LARGE
   */
   public void setSize(int newValue) { size = newValue; }
   /**
      Gets the value of the size property.
      @return the size, one of SMALL, NORMAL, LARGE
   */
   public int getSize() { return size; }
   
   public String toString()
   {
      return text.replace('\n', '|');
   }

   /**
      Gets the bounding rectangle for this multiline string.
      @param g2 the graphics context
      @return the bounding rectangle (with top left corner (0,0))
   */
   public Rectangle2D getBounds(Graphics2D g2)
   {
      double width = 0;
      double height = 0;
      Font oldFont = g2.getFont();
      Font font = oldFont;
      if (size == LARGE)
      {
         float size = font.getSize() * 1.25F;
         font = font.deriveFont(size);
      }
      else if (size == SMALL)
      {
         float size = font.getSize() / 1.25F;
         font = font.deriveFont(size);
      }
      g2.setFont(font);
      FontRenderContext frc = g2.getFontRenderContext();
      StringTokenizer tokenizer = new StringTokenizer(text, "\n");
      while (tokenizer.hasMoreTokens())
      {
         String t = tokenizer.nextToken();
         Rectangle2D b = font.getStringBounds(t, frc);
         width = Math.max(width, b.getWidth() + 2 * GAP);
         height += b.getHeight();
      }
      g2.setFont(oldFont);
      return new Rectangle2D.Double(0, 0, width, height);
   }

   /**
      Draws this multiline string inside a given rectangle
      @param g2 the graphics context
      @param r the rectangle into which to place this multiline string
   */
   public void draw(Graphics2D g2, Rectangle2D r)
   {
      Font oldFont = g2.getFont();
      Font font = oldFont;
      if (size == LARGE)
      {
         float size = font.getSize() * 1.25F;
         font = font.deriveFont(size);
      }
      else if (size == SMALL)
      {
         float size = font.getSize() / 1.25F;
         font = font.deriveFont(size);
      }
      g2.setFont(font);

      FontRenderContext frc = g2.getFontRenderContext();
      Rectangle2D bounds = getBounds(g2);
      StringTokenizer tokenizer = new StringTokenizer(text, "\n");
      double xleft = r.getX();
      double ytop = r.getY() + (r.getHeight() - bounds.getHeight()) / 2;
      while (tokenizer.hasMoreTokens())
      {
         String t = tokenizer.nextToken();
         Rectangle2D b = font.getStringBounds(t, frc);

         double xstart;
         double xslack = r.getWidth() - b.getWidth();
         if (justification == CENTER) 
            xstart = xleft + xslack / 2;
         else if (justification == RIGHT)
            xstart = xleft + xslack - GAP;
         else 
            xstart = xleft + GAP;
         double ystart = ytop - b.getY();
         g2.drawString(t, (float)(xstart), (float)(ystart));
         if (underlined)
            g2.draw(new Line2D.Double(xstart, ystart + 1, xstart + b.getWidth(), ystart + 1));
         ytop += b.getHeight();         
      }
      g2.setFont(oldFont);
   }

   public Object clone()
   {
      try
      {
         return super.clone();
      }
      catch (CloneNotSupportedException exception)
      {
         return null;
      }
   }

   public static final int LEFT = 0;
   public static final int CENTER = 1;
   public static final int RIGHT = 2;
   public static final int LARGE = 3;
   public static final int NORMAL = 4;
   public static final int SMALL = 5;

   private static final int GAP = 3;

   private String text;
   private int justification;
   private int size;
   private boolean underlined;
}
