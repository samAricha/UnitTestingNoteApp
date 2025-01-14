package teka.group.unittestingnoteapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import teka.group.unittestingnoteapp.feature_note.data.data_source.NoteDatabase
import teka.group.unittestingnoteapp.feature_note.data.repository.NoteRepositoryImpl
import teka.group.unittestingnoteapp.feature_note.domain.repository.NoteRepository
import teka.group.unittestingnoteapp.feature_note.domain.use_case.AddNote
import teka.group.unittestingnoteapp.feature_note.domain.use_case.DeleteNote
import teka.group.unittestingnoteapp.feature_note.domain.use_case.GetNote
import teka.group.unittestingnoteapp.feature_note.domain.use_case.GetNotes
import teka.group.unittestingnoteapp.feature_note.domain.use_case.NoteUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}