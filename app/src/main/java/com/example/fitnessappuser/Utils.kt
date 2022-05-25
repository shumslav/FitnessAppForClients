import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.fitnessappuser.enums.Calculation
import com.example.fitnessappuser.models.CalendarDay
import com.example.fitnessappuser.models.Exercise
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val NODE_PASSWORD = "password"
val NODE_GROUP_MUSCLES = "GroupMuscles"
val NODE_EXERCISES = "Exercises"
val NODE_USERS = "Users"
val NODE_TRAINERS = "Trainers"
val NODE_TRAIN_NOTES = "TrainNotes"
val NODE_MEALS = "Meals"
val NODE_NAME = "Name"
val NODE_CLIENTS = "Clients"
val NODE_MESSAGES = "Messages"

fun makeToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun getDates(fromDate: String, toDate: String): MutableList<CalendarDay> {
    val dates = mutableListOf<CalendarDay>()
    val df1 = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    var date1: Date?
    var date2: Date?

    try {
        date1 = df1.parse(fromDate)
        date2 = df1.parse(toDate)
        val cal1 = Calendar.getInstance()
        cal1.time = date1!!


        val cal2 = Calendar.getInstance()
        cal2.time = date2!!

        while (!cal1.after(cal2)) {
            val time = cal1.time
            val weekday = time.toString().split(" ")[0]
            val day = time.toString().split(" ")[2]
            val month = time.toString().split(" ")[1]
            val year = time.toString().split(" ").last()
            dates.add(CalendarDay(day, weekday, month, year))
            cal1.add(Calendar.DATE, 1)
        }
    } catch (e: ParseException) {
        Log.i("ERROR", "ERROR")
        e.printStackTrace();
    }
    Log.i("Count days", "${dates.size}")
    return dates
}

//class ExercisesTypeConverter {
//
//    fun toExercises(exercisesString: String): List<Exercise> {
//        val data = exercisesString.split(',')
//        val exercises = mutableListOf<Exercise>()
//        data.forEach {
//            val dataList = it.split(";")
//            exercises.add(
//                Exercise(
//                    dataList[0],
//                    dataList[1],
//                    dataList[2].toInt(),
//                    dataList[3].toDouble()
//                )
//            )
//        }
//        return exercises
//    }
//
//
//    fun fromExercises(exercises: List<Exercise>): String {
//        var exercisesString = ""
//        for (i in 0..exercises.size) {
//            val exercise = exercises[i]
//            exercisesString += "${exercise.name}&${exercise.bodyPart}&${exercise.repetitions}&${exercise.liftedWeight}"
//            if (i != exercises.size-1 && exercises.size!=1)
//                exercisesString+="*"
//        }
//        return exercisesString
//    }
//
//}

fun getMonthFromNumber(month: String): String {
    return when (month) {
        "Jan" -> "01"
        "Feb" -> "02"
        "Mar" -> "03"
        "Apr" -> "04"
        "May" -> "05"
        "Jun" -> "06"
        "Jul" -> "07"
        "Aug" -> "08"
        "Sep" -> "09"
        "Oct" -> "10"
        "Nov" -> "11"
        "Dec" -> "12"
        else -> ""
    }
}

val standartGroupMuscles = listOf("Бицепс", "Грудь", "Пресс", "Ноги", "Спина", "Плечи", "Трицепс")
val standartExercises = listOf(
    Exercise("Бицепс", "Сгибание со штангой", Calculation.REPETITIONS, true),
    Exercise("Грудь", "Жим лежа", Calculation.REPETITIONS, true),
    Exercise("Пресс", "Планка", Calculation.SECONDS, false),
    Exercise("Ноги", "Присяд", Calculation.REPETITIONS, true),
    Exercise("Спина", "Подтягивания", Calculation.REPETITIONS, false),
    Exercise("Плечи", "Жим сидя", Calculation.REPETITIONS, true),
    Exercise("Трицепс", "Французский жим", Calculation.REPETITIONS, true)
)