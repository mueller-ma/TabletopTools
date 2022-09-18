import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.muellerma.tabletoptools.db.CustomDie

@Dao
interface DieDao {
    @Query("SELECT * FROM CustomDie")
    fun getAll(): List<CustomDie>

    @Query("SELECT * FROM CustomDie WHERE id = :id")
    fun loadById(id: Int): CustomDie

    @Insert
    fun insertAll(vararg users: CustomDie)

    @Delete
    fun delete(user: CustomDie)
}