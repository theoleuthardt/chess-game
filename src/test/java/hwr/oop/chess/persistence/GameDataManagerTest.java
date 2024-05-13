// import static org.junit.Assert.assertEquals;
// import static org.mockito.Mockito.*;
//
// import java.io.IOException;
//
// import org.junit.Test;
//
// public class GameDataManagerTest {
//
//  @Test
//  public void testLoadGame() {
//    CSVFilePersistence repository = new CSVFilePersistence();
//    // One can assert the behavior of loading game data from CSV
//  }
//
//  @Test
//  public void testSaveGame() {
//    // Create a mock FileWriter to simulate file writing
//    FileWriter mockWriter = mock(FileWriter.class);
//    try {
//      doNothing().when(mockWriter).write(anyString());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    // Mock the FileWriter creation
//    try {
//      doReturn(mockWriter).when(mockWriter).write(anyString());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    CSVFilePersistence repository = new CSVFilePersistence();
//    repository.saveGame(1);
//
//    // Verify that FileWriter was called with the correct file name
//    try {
//      verify(mockWriter).close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
// }
//
//// the code above does not test dynamically:
/// * dynamic version ??
