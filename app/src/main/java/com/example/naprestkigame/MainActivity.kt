package com.example.naprestkigame
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var cup1: ImageView
    private lateinit var cup2: ImageView
    private lateinit var cup3: ImageView
    private lateinit var ball: ImageView
    private lateinit var playAgainButton: Button
    private lateinit var constraintLayout: ConstraintLayout

    private var ballPosition: Int = -1
    private var gameStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cup1 = findViewById(R.id.cup1)
        cup2 = findViewById(R.id.cup2)
        cup3 = findViewById(R.id.cup3)
        ball = findViewById(R.id.ball)
        playAgainButton = findViewById(R.id.playAgainButton)
        constraintLayout = findViewById(R.id.constraintLayout)

        cup1.setOnClickListener { checkCup(1, cup1) }
        cup2.setOnClickListener { checkCup(2, cup2) }
        cup3.setOnClickListener { checkCup(3, cup3) }

        playAgainButton.setOnClickListener {
            startGame()
        }

        startGame()
    }

    private fun startGame() {
        ball.visibility = View.GONE
        gameStarted = true
        ballPosition = Random.nextInt(1, 4) // случайное число от 1 до 3
        Toast.makeText(this, "Шарик спрятан! Попробуйте угадать.", Toast.LENGTH_SHORT).show()
    }

    private fun checkCup(selectedCup: Int, cupView: ImageView) {
        if (!gameStarted) {
            Toast.makeText(this, "Нажмите 'Играть снова' чтобы начать.", Toast.LENGTH_SHORT).show()
            return
        }

        // Анимация стакана
        animateCup(cupView) {
            // Показать шарик под стаканом
            showBallUnderCup(ballPosition)

            // Проверка на угаданный стакан
            if (selectedCup == ballPosition) {
                Toast.makeText(this, "Вы угадали!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Не угадали! Шарик был под другим стаканом.", Toast.LENGTH_SHORT).show()
            }

            gameStarted = false
        }
    }

    private fun animateCup(cup: ImageView, onEnd: () -> Unit) {
        val lift = ObjectAnimator.ofFloat(cup, "translationY", 0f, -100f) // поднимаем стакан на 100 пикселей
        lift.duration = 300

        val drop = ObjectAnimator.ofFloat(cup, "translationY", -100f, 0f) // опускаем стакан обратно
        drop.duration = 300

        // Соединяем анимации
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(lift, drop)

        
        animatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {

            }

            override fun onAnimationEnd(animation: android.animation.Animator) {
                onEnd() // Вызовем метод после окончания анимации
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {

            }

            override fun onAnimationRepeat(animation: android.animation.Animator) {

            }
        })

        animatorSet.start() // Запускаем анимацию
    }

    private fun showBallUnderCup(cupNumber: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        when (cupNumber) {
            1 -> {
                constraintSet.connect(R.id.ball, ConstraintSet.TOP, R.id.cup1, ConstraintSet.BOTTOM)
                constraintSet.connect(R.id.ball, ConstraintSet.END, R.id.cup1, ConstraintSet.END)
                constraintSet.connect(R.id.ball, ConstraintSet.START, R.id.cup1, ConstraintSet.START)
            }
            2 -> {
                constraintSet.connect(R.id.ball, ConstraintSet.TOP, R.id.cup2, ConstraintSet.BOTTOM)
                constraintSet.connect(R.id.ball, ConstraintSet.END, R.id.cup2, ConstraintSet.END)
                constraintSet.connect(R.id.ball, ConstraintSet.START, R.id.cup2, ConstraintSet.START)
            }
            3 -> {
                constraintSet.connect(R.id.ball, ConstraintSet.TOP, R.id.cup3, ConstraintSet.BOTTOM)
                constraintSet.connect(R.id.ball, ConstraintSet.END, R.id.cup3, ConstraintSet.END)
                constraintSet.connect(R.id.ball, ConstraintSet.START, R.id.cup3, ConstraintSet.START)
            }
        }


        // Применяем изменения
        constraintSet.applyTo(constraintLayout)
        ball.visibility = View.VISIBLE
    }
}
