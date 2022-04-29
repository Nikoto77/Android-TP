package Form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.todonicolasfloris.R
import com.android.todonicolasfloris.tasklist.Task
import com.android.todonicolasfloris.tasklist.TaskListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val button = findViewById<Button>(R.id.button_form)
        val titleText = findViewById<EditText>(R.id.titleText)
        val descriptionText = findViewById<EditText>(R.id.descriptionText)
        val taskEdit = intent.getSerializableExtra("task") as? Task
        descriptionText.setText(taskEdit?.description)
        titleText.setText(taskEdit?.title)

        button.setOnClickListener {
            val newTask = Task(id = taskEdit?.id ?: UUID.randomUUID().toString(),
                title = titleText.text.toString(),description = descriptionText.text.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish();

        }
    }

}