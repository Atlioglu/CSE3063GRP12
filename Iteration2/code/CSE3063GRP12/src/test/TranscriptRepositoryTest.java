package test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

import core.exceptions.UserNotFoundException;
import core.models.concretes.Transcript;
import core.repositories.TranscriptRepository;

public class TranscriptRepositoryTest {

    @Test()
    public void testStudentCurrentSemester() throws IOException, UserNotFoundException {
        TranscriptRepository transcriptRepository = new TranscriptRepository();
        Transcript transcript = transcriptRepository.getTranscript("o150121534");
        assertEquals(transcript.getCurrentSemester(), 1);
    }

    @Test(expected = UserNotFoundException.class)
    public void testNotFoundTranscript() throws IOException, UserNotFoundException {
        TranscriptRepository transcriptRepository = new TranscriptRepository();
        transcriptRepository.getTranscript("o1501215");
    }

}
