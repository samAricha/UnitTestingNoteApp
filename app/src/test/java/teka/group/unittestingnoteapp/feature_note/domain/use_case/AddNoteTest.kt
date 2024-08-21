package teka.group.unittestingnoteapp.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import teka.group.unittestingnoteapp.feature_note.data.repository.FakeNoteRepository
import teka.group.unittestingnoteapp.feature_note.domain.model.InvalidNoteException
import teka.group.unittestingnoteapp.feature_note.domain.model.Note

class AddNoteTest{

    private lateinit var addNote: AddNote
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setup(){
        fakeNoteRepository = FakeNoteRepository()
        addNote = AddNote(fakeNoteRepository)
    }

    @Test
    fun `Add Note to db, contents okay`() = runBlocking{
        val note: Note = Note(
            title = "theTitle",
            content = "theContent",
            timestamp = System.currentTimeMillis(),
            color = 2
        )

        addNote(note)

        val currentNotes:List<Note> = fakeNoteRepository.getNotes().first()
        assertThat(currentNotes).contains(note)
    }

    @Test(expected = InvalidNoteException::class)
    fun `Throws exception when note title is blank`() = runBlocking {
        val note: Note = Note(
            title = "",
            content = "theContent",
            timestamp = System.currentTimeMillis(),
            color = 2
        )

        addNote(note)
    }

    @Test(expected = InvalidNoteException::class)
    fun `Throws exception when note content is blank`() = runBlocking {
        val note: Note = Note(
            title = "Our Title",
            content = "",
            timestamp = System.currentTimeMillis(),
            color = 2
        )

        addNote(note)
    }
}