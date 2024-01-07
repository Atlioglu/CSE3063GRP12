import unittest
from core.repositories.TranscriptRepository import TranscriptRepository
from core.exceptions.UserNotFoundException import UserNotFoundException

class TranscriptRepositoryTest(unittest.TestCase):

    def test_student_current_semester(self):
        transcript_repository = TranscriptRepository()
        transcript = transcript_repository.get_transcript("o150120060")
        self.assertEqual(transcript.currentSemester, 6)

    def test_not_found_transcript(self):
        transcript_repository = TranscriptRepository()
        transcript = transcript_repository.get_transcript("o1501asdasd20060")
        self.assertIsNone(transcript)

if __name__ == '__main__':
    unittest.main()