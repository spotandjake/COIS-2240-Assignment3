import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

// Workaround to avoid header on appending
public class AppendingObjectOutputStream extends ObjectOutputStream {
  public AppendingObjectOutputStream(OutputStream out) throws IOException {
    super(out);
  }

  @Override
  protected void writeStreamHeader() throws IOException {
    // do not write a header, but reset:
    // this line added after another question
    // showed a problem with the original
    reset();
  }
}
