package com.android.todonicolasfloris.tasklist
import kotlinx.serialization.Serializable


@Serializable
data class Task(var id: String, val title: String, val description: String = "description 1", ):java.io.Serializable{


}
